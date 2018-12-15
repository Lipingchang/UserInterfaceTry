package com.example.userinterfacetry.bean;

import android.content.Context;
import android.util.Log;

import com.example.userinterfacetry.MainActivity;
import com.example.userinterfacetry.MyServiceAPDU;
import com.example.userinterfacetry.utils.UtilTools;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;

public class MasterCard {
    static final String TAG = "MasterCard";
    static final public String MASTER_SAVE_FILE = "MASTERINFO";
    static private MasterCard masterCardInstance;

    private boolean register = false;       // name 和 pwd 有没有设置
    private boolean haveLock = false;       // masterid 和 lockid 有没有设置  在和pn532关联之后才会初始化.

    private String masterName = "not set";
    private String masterPwd = "not set";
    private byte    masterId = -1; //
    private String LockID = "not set";      //

    public String getLockID() {
        return LockID;
    }

    public static MasterCard getMasterCardInstance(){
        if( masterCardInstance == null ){
            masterCardInstance = new MasterCard();
        }
        return masterCardInstance;
    }
    public static MasterCard freshInstance(){
        masterCardInstance = new MasterCard();
        return masterCardInstance;
    }

    private MasterCard(){
         try {
            InputStream inputfile = null;
            if( MainActivity.mainContext == null ){
                inputfile = MyServiceAPDU.serviceContext.openFileInput(MASTER_SAVE_FILE);
            }else{
                inputfile =  MainActivity.mainContext.openFileInput(MASTER_SAVE_FILE);;
            }
            String s = IOUtils.toString(inputfile);
            Gson gson = new Gson();
            savedata data = gson.fromJson(s,savedata.class);
            this.masterId = data.getMasterId();
            this.LockID = data.getLockID();
            this.masterName = data.getMasterName();
            this.masterPwd = data.getMasterPwd();

             register = true;               // 手机上有没有保存 master的信息
             haveLock = (this.masterId != -1 && (!this.LockID.equals("not set")));// master 有没有 和门锁关联过。
         }catch (Exception e){
             //file not Found!
             Log.e(TAG,e.getMessage());
             register = false;
             haveLock = false;
        }
    }
    public boolean isHaveLock(){return  haveLock;}
    public boolean isRegister() {
        return register;
    }

    public String getMasterName() {
        return masterName;
    }

    public String getMasterPwd() {
        return masterPwd;
    }

    public int getMasterId() {
        return masterId;
    }

    // 设置本机的用户
    public void setMaster(String name,String pwd) throws Exception {
        this.masterName = name;
        this.masterPwd = pwd;
        this.register = true;

        OutputStream out = MainActivity.mainContext.openFileOutput(MASTER_SAVE_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String s = gson.toJson(this,this.getClass());
        out.write(s.getBytes());
        out.close();


    }
    public void setName(String name) throws Exception {
        this.masterName = name;

        OutputStream out = MainActivity.mainContext.openFileOutput(MASTER_SAVE_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String s = gson.toJson(this,this.getClass());
        out.write(s.getBytes());
        out.close();

    }
    public void setPwd(String name) throws Exception {
        this.masterPwd = name;

        OutputStream out = MainActivity.mainContext.openFileOutput(MASTER_SAVE_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String s = gson.toJson(this,this.getClass());
        out.write(s.getBytes());
        out.close();

    }
    // 设置和门锁关联
    public void setLock(String LockID,byte masterId) throws Exception{  // 给 apduService 用, 在接受到 pn532 返回后的lockid和masterid后设置下.
        this.LockID = LockID;
        this.masterId = masterId;
        this.haveLock = true;

        OutputStream out = MainActivity.mainContext.openFileOutput(MASTER_SAVE_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String s = gson.toJson(this,this.getClass());
        out.write(s.getBytes());
        out.close();
    }
    public void setUnRelate() throws Exception{
        masterCardInstance.haveLock = false;
        masterCardInstance.LockID = "not set";
        masterCardInstance.masterId = -1;

        OutputStream out = MainActivity.mainContext.openFileOutput(MASTER_SAVE_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String s = gson.toJson(this,this.getClass());
        out.write(s.getBytes());
        out.close();
    }
    public boolean ismyLock(byte[] inputLockID){
        return haveLock && UtilTools.lockID2String(inputLockID).equals(this.LockID);
    }
}
class savedata{
    private String masterName;
    private String masterPwd;
    private byte    masterId;
    private String LockID = "not set";


    public String getLockID() {
        return LockID;
    }
    public void setLockID(String s){
        this.LockID = s;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getMasterPwd() {
        return masterPwd;
    }

    public void setMasterPwd(String masterPwd) {
        this.masterPwd = masterPwd;
    }

    public byte getMasterId() {
        return masterId;
    }

    public void setMasterId(byte masterId) {
        this.masterId = masterId;
    }
}
