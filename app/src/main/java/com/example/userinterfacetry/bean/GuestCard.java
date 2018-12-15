package com.example.userinterfacetry.bean;

import com.example.userinterfacetry.utils.UtilTools;

import java.util.Date;

public class GuestCard{
    public String lockID;      // 卡 对应的锁的ID
    private String pwd;         // 认证的时候用的加密数据
    public String cardName;    // 卡 的名字
    public Date startDate;
    public Date expireDate;    // 卡 的截止日期
    protected boolean validCard = false;  // 卡 有没有过期了
    private int sendCardMasterID;   // 发卡人的id号

    public GuestCard(int sendCardMasterID,String lockID, String pwd, String cardName,Date startDate, Date expireDate) {
        this.lockID = lockID;
        this.pwd = pwd;
        this.cardName = cardName;
        this.expireDate = expireDate;
        this.startDate = startDate;
        this.validCard = (startDate.getTime()<System.currentTimeMillis()) && (expireDate.getTime()>System.currentTimeMillis());
        this.sendCardMasterID = sendCardMasterID;
    }

    public boolean ismyLock(byte[] inputLockID){
        return validCard && UtilTools.lockID2String(inputLockID).equals(this.lockID);
    }
    public boolean ismyLock(String inputLockID){
        return validCard && inputLockID.equals(this.lockID);
    }

    public boolean isValidCard() {
        return validCard;
    }
}
