package com.example.userinterfacetry;

import android.content.Context;
import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import com.example.userinterfacetry.bean.GuestCardManager;
import com.example.userinterfacetry.bean.MasterCard;

import org.apache.commons.lang3.ArrayUtils;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

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
    final static byte GetRecentRecordHead =       (byte)0x04;
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


    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        Log.d(TAG,"process input apdu:"+bytesToHex(commandApdu));
        // 如果没关联锁 也 没有有效的副卡 退出
        MasterCard card = MasterCard.getMasterCardInstance();
        if( !(GuestCardManager.hasValidCard() || card.isHaveLock()) ){
            return new byte[]{ ByeByeHead};
        }
        // 无效数据
        if( commandApdu==null || commandApdu.length==0)
            return new byte[]{ReceiveFuckHead};


        // 拿出 收到的APDU的 第一个 Byte (Head) ，然后分发给不同的 方法:
        byte head = commandApdu[0]; Log.d(TAG,"apdu head:"+head);
        byte re[] = null;

        switch (head){
            case AIDHead:                           // 收到的是AID，手机刚刚和pn532接触
                re = new byte[]{StartAuthHead};     // 向pn532发起开始认证的请求
                break;
            case LockIDHead:                        // 收到的是LockID，就开始在本机中查找密码. 然后返回发送给pn532的认证用的数组
                re = makeAccessRequest(commandApdu); // TODO ???
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
            accessRequest = ArrayUtils.addAll(accessRequest,UtilTools.pwd2HexByte(masterCard.getMasterPwd()));

        }else if( GuestCardManager.containLockID(LockID)){
            // 是别人家 的锁
            // TODO
        }else{
            // 不能开的锁
            // TODO
        }
        return accessRequest;
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
    public static String bytesToHex(byte[] apdu){
        if( apdu  == null ){
            return "null apdu byte[]";
        }
        StringBuffer k = new StringBuffer("");
        for( int i = 0; i < apdu.length; i++ ){
            k.append(String.format("%02x ",apdu[i]));
        }
        return k.toString();
    }

    @Override
    public void onDeactivated(int reason) {
        System.out.println("on Deactivated");
    }

    public MyServiceAPDU() {
        Log.d(TAG,"APDU Service start...");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 启动主activity
        startUserInterface();
    }
    private void startUserInterface(){
        Intent startMain = new Intent(getBaseContext(),MainActivity.class);
        startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

}
