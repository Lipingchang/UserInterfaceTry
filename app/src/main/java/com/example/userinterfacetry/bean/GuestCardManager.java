package com.example.userinterfacetry.bean;

import android.util.Log;

import com.example.userinterfacetry.AesCBC;
import com.example.userinterfacetry.utils.UtilTools;
import com.google.gson.Gson;
import com.example.userinterfacetry.utils.UtilTools;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuestCardManager {
    private static final String TAG = "GuestCardManager";

    private static List<GuestCard> guestCardList = new ArrayList<>();


    public static List<GuestCard> getList(){
        return GuestCardManager.guestCardList;
    }
    public static void addCard(){

    }

    // 在副卡列表中查找 有这个 id 卡
    public static boolean containLockID(byte[] LockID){
        for( GuestCard card : GuestCardManager.guestCardList ){
            if( card.ismyLock( LockID )  )
                return true;
        }
        return false;
    }
    public static boolean hasValidCard(){
        boolean re = false;
        for( GuestCard card : guestCardList ){
            re  = (re || card.validCard);
        }
        return re;
    }

    class GuestCard{
        private String lockID;      // 卡 对应的锁的ID
        private String pwd;         // 认证的时候用的加密数据
        private String cardName;    // 卡 的名字
        private Date startDate;
        private Date expireDate;    // 卡 的截止日期
        private boolean validCard = false;  // 卡 有没有过期了

        public GuestCard(String lockID, String pwd, String cardName,Date startDate, Date expireDate) {
            this.lockID = lockID;
            this.pwd = pwd;
            this.cardName = cardName;
            this.expireDate = expireDate;
            this.validCard = (startDate.getTime()<System.currentTimeMillis()) && (startDate.getTime()>System.currentTimeMillis());
        }

        public boolean ismyLock(byte[] inputLockID){
            return validCard && UtilTools.lockID2String(inputLockID).equals(this.lockID);
        }
    }
    public static String generateCryptoCard(String guestName,Date start,Date end) throws  Exception{
        CryptoCard card = new CryptoCard(guestName,start,end);

        String s = "$";
        s += card.masterPwdCrypto();
        s += "$";
        s += card.commonCrypto();
        s += "$";

        return s;
    }
    // 启动MainActivity的时候 检查粘贴板上的内容
    public static boolean getCardFromClipBoard(String sdata){
        try {
            String datas[] = sdata.split("\\$");
            String s = datas[1];
            String data = datas[2];
            System.out.println("get data:"+s + "\t" + data);



            return true;
        }catch (Exception e) {
            return false;
        }
    }
    static class CryptoCard{
        private static final String commonPwd = "hahahahahahahaha";
        int masterID;
        String guestName,data;
        long start,end;
        public CryptoCard(String guestName,Date start,Date end){
            this.guestName = guestName;
            this.masterID = MasterCard.getMasterCardInstance().getMasterId();
            this.start = start.getTime()/1000; this.end = end.getTime()/1000;
            data = new Gson().toJson(this);
            Log.d(TAG,data);
        }
        public String masterPwdCrypto()throws Exception{
            String pwd = MasterCard.getMasterCardInstance().getMasterPwd();
            if( pwd.length() < 16 ){
                pwd += StringUtils.repeat("0",(16-pwd.length()) );
            }
            if( pwd.length() > 16 ){
                pwd = StringUtils.substring(pwd,0,16);
            }
            AesCBC aes = AesCBC.getInstance();
            return aes.encrypt(data,pwd);
        }
        public String commonCrypto() throws Exception{
            AesCBC aes = AesCBC.getInstance();
            return aes.encrypt(data,commonPwd);
        }
        // 第一个加密数据是给 和 pn532交流的时候用的，第二个加密数据 是为了在传输过程中保密用的（好像没软保护）
        public GuestCard decryptCard(String ssdata,String sdata) throws Exception{
            AesCBC aes = AesCBC.getInstance();
            String s = aes.decrypt(sdata,commonPwd);
            Gson gson = new Gson();
            CryptoCard c = gson.fromJson(s,CryptoCard.class);
            GuestCard g = new GuestCard(c.masterID,ssdata,c.guestName,new Date(c.start*1000),new Date(c.end*1000));
        }
    }
}
