package com.example.userinterfacetry;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.userinterfacetry.bean.GuestCardManager;
import com.example.userinterfacetry.bean.MasterCard;
import com.example.userinterfacetry.bean.UserLog;
import com.example.userinterfacetry.utils.UtilTools;

import org.apache.commons.lang3.ArrayUtils;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static com.example.userinterfacetry.utils.UtilTools.bytesToHex;

public class MyServiceAPDU extends HostApduService {
    public static String TAG = "MyServiceAPUD";
    static byte[] AID = {
            (byte)0x00,
            (byte)0x66,
            (byte)0x88,
            (byte)0x66,
            (byte)0x88,
            (byte)0x66,
            (byte)0x88
    };
    final static byte AccessRequestHead =         (byte)0x02;
    final static byte LockIDHead =                (byte)0x01;
    final static byte AccessReplyHead =           (byte)0x03;
        final static byte WelcomeMaster = (byte)0x01;
        final static byte WelcomeGuest = (byte)0x02;
        final static byte AccessDeny = (byte)0x03;

    final static byte GetRecentRecordHead =       (byte)0x04;

    final static byte SendRecentRecord = (byte)0x15;
        final static byte SendRecentAccessRecord =    (byte)0x05;
        final static byte SendRecentRefuseRecord =    (byte)0x06;
        final static byte SendRecentKeyAccessRecord = (byte)0x07;

    final static byte ByeByeHead =                (byte)0x08;

    final static byte StartAuthHead =             (byte)0x90;
    final static byte ReceiveFuckHead =           (byte)0x99;
    final static byte ReceiveOKHead   =           (byte)0x66;
    final static byte  AIDHead  =                 (byte)0x00;

    final static byte MasterMode =                 (byte)0x01;
    final static byte GuestMode =                 (byte)0x02;
    final static byte StartRelateHead = (byte) 0x78;
    final static byte RelateAcceptHead = (byte) 0x79;
    final static byte[] RelateResult_Success = {(byte)0x80,(byte)0x11};
    final static byte[] RelateResult_Fail = {(byte)0x80,(byte)0x22};

    public static Service  serviceContext = null;
    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        Log.d(TAG,"process input apdu:"+bytesToHex(commandApdu));
        //   没关联锁 也 没有有效的副卡  也 不在配对模式  退出
        MasterCard card = MasterCard.getMasterCardInstance();
        Log.d(TAG,GuestCardManager.hasValidCard() +" "+ card.isHaveLock() +" "+ MainActivity.DoingRegister);
        if( !GuestCardManager.hasValidCard() && !card.isHaveLock()  && !MainActivity.DoingRegister ){
            return byeByePN532();

        }
        // 无效数据
        if( commandApdu==null || commandApdu.length==0)
            return new byte[]{ReceiveFuckHead};


        // 拿出 收到的APDU的 第一个 Byte (Head) ，然后分发给不同的 方法:
        byte head = commandApdu[0]; Log.d(TAG,"apdu head:"+head);
        byte re[] = null;

        switch (head){
            case AIDHead:                           // 收到的是AID，手机刚刚和pn532接触
                if( MainActivity.DoingRegister ){   // 关联模式
                    re = makeRelateRequest();
                }else{
                    re = new byte[]{StartAuthHead};     // 向pn532发起开始认证的请求
                }
                break;
            case LockIDHead:                        // 收到的是LockID，就开始在本机中查找密码. 然后返回发送给pn532的认证用的数组
                re = makeAccessRequest(commandApdu); // TODO ???
                break;
            case AccessReplyHead:
                re = handleAccessReply(commandApdu);
                break;
            case RelateAcceptHead:  // todo 没有保存状态,如果上一个应答不是 要关联的呢?
                re = setRelateSuccess(commandApdu);
                break;
            case SendRecentAccessRecord:
            case SendRecentRefuseRecord:
            case SendRecentKeyAccessRecord:
            case SendRecentRecord:
                re = saveRecentRecord(commandApdu);
                break;
        }

        Log.d(TAG,"reply bytes:"+ bytesToHex(re));
        return re;
    }

    private byte[] makeAccessRequest(byte[] apdu){
        byte[] accessRequest = new byte[3];
        accessRequest[0] = AccessRequestHead;

        // 先从apdu中拿出 LockID
        byte[] LockID = Arrays.copyOfRange(apdu,1,apdu.length); Log.d(TAG,"LockID:"+bytesToHex(LockID));
        // 然后到 我保存的密码中去找这个ID的锁
        MasterCard masterCard = MasterCard.getMasterCardInstance();
        if ( masterCard.ismyLock(LockID) ){
            // 是主人的锁：
            accessRequest[1] = MasterMode;
            accessRequest[2] = (byte)masterCard.getMasterId();
            accessRequest = ArrayUtils.addAll(
                    accessRequest,
                    UtilTools.pwd2HexByte(masterCard.getMasterPwd())
            );

        }else if( GuestCardManager.containLockID(LockID)){
            // 是别人家 的锁
            // TODO
        }else{
            // 不能开的锁
            // TODO
        }
        return accessRequest;
    }
    private byte[] makeRelateRequest(){
        byte[] relateRequest = new byte[2];
        MasterCard c = MasterCard.getMasterCardInstance();
        relateRequest[0] = StartRelateHead;
        relateRequest[1] = UtilTools.int2byte(
                c.getMasterPwd().length()
        );
        relateRequest = ArrayUtils.addAll(
                relateRequest,
                UtilTools.pwd2HexByte(
                        c.getMasterPwd()
                )
        );
        relateRequest = ArrayUtils.addAll(relateRequest,
                UtilTools.pwd2HexByte(
                        c.getMasterName()
                )
        );

        return  relateRequest;
    }
    private byte[] setRelateSuccess(byte[] commandApdu){
        byte masterID = commandApdu[1];
        byte[] lockID_b = Arrays.copyOfRange(commandApdu,2,commandApdu.length);
        try {
            MasterCard.getMasterCardInstance().setLock(UtilTools.lockID2String(lockID_b), masterID);
            UtilTools.longVibrator();
            MainActivity.mainContext.relateResult(true);

        }catch (Exception e){
            // 保存失败
            Toast.makeText(MainActivity.mainContext,R.string.relate_fail,Toast.LENGTH_SHORT);
            UtilTools.doubleVibrator();
            MainActivity.mainContext.relateResult(false);
            return RelateResult_Fail ;

        }
        return RelateResult_Success;
    }
    private byte[] byeByePN532(){
        return new byte[]{ ByeByeHead};
    }

    private byte[] saveRecentRecord(byte[] commandApdu){
        // 收到门锁发送的 最近的用户的列表：
        // 2 记录日志
        int start = 1;
        while(true){
            try {
               UserLog k = UserLog.byte2Log(Arrays.copyOfRange(commandApdu,start,commandApdu.length));
               start += k.rawdatalen;
            }catch (Exception e){
                break;
            }
        }
        return byeByePN532();
    }


    private byte[] handleAccessReply(byte[] commandApdu){
        // 1 成功进门
        byte result = commandApdu[1];
        // 取出 command Apdu 最后的语句
        byte[] bytewords = Arrays.copyOfRange(commandApdu,2,commandApdu.length);
        String words = UtilTools.hexByte2Pwd(bytewords);
        switch (result) {
            case WelcomeMaster:
                UtilTools.longVibrator();
                // 向门锁拿 最近的进门记录：
                byte[] re = new byte[]{GetRecentRecordHead,
                        UtilTools.int2byte(
                                MasterCard.getMasterCardInstance().getMasterId())
                };
                re = ArrayUtils.addAll(re,UtilTools.pwd2HexByte(MasterCard.getMasterCardInstance().getMasterPwd()));
                return re;

            case AccessDeny:
                UtilTools.doubleVibrator();
                Toast.makeText(this,words,Toast.LENGTH_LONG).show();
                break;
            case WelcomeGuest:
                UtilTools.longVibrator();
                Toast.makeText(this,words,Toast.LENGTH_LONG).show();
                break;
        }
        return byeByePN532();

    }





    private void addLogToFile(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd>HH:mm:ss");
        Date now = new Date(System.currentTimeMillis());
        String msg = format.format(now);

        FileOutputStream outputStream;

        try{
            outputStream = openFileOutput(MainActivity.LogFileName, Context.MODE_APPEND);
            outputStream.write(msg.getBytes());
            outputStream.flush();
            outputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void onDeactivated(int reason) {
        System.out.println("on Deactivated");
    }

    public MyServiceAPDU() {
        serviceContext = this;
        Log.d(TAG,"APDU Service start...");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 启动主activity
        //startUserInterface();
    }
    private void startUserInterface(){
        Intent startMain = new Intent(getBaseContext(),MainActivity.class);
        startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

}
