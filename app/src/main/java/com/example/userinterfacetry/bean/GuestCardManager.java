package com.example.userinterfacetry.bean;

import com.example.userinterfacetry.UtilTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuestCardManager {
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
        private String pwd;
        private String cardName;    // 卡 的名字
        private Date expireDate;    // 卡 的截止日期
        private boolean validCard = false;  // 卡 有没有过期了

        public GuestCard(){

        }
        public boolean ismyLock(byte[] inputLockID){
            return validCard && UtilTools.lockID2String(inputLockID).equals(this.lockID);
        }
    }
}
