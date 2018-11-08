package com.example.userinterfacetry.bean;

import android.content.Context;

import com.example.userinterfacetry.MainActivity;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;

public class Master {
    static final public String MASTER_SAVE_FILE = "MASTERINFO";
    static private Master masterInstance;

    private boolean register = false;       // name 和 pwd 有没有设置
    private boolean haveLock = false;       // masterid 和 lockid 有没有设置  在和pn532关联之后才会初始化.

    private String masterName = "not set";
    private String masterPwd = "not set";
    private int    masterId = -1;
    private String LockID = "not set";      //

    public static Master getMasterInstance(){
        if( masterInstance == null ){
            masterInstance = new Master();
        }
        return masterInstance;
    }
    public static Master freshInstance(){
        masterInstance = new Master();
        return  masterInstance;
    }

    private Master(){
         try {
            InputStream inputfile =  MainActivity.mainContext.openFileInput(MASTER_SAVE_FILE);

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

        OutputStream out = MainActivity.mainContext.openFileOutput(MASTER_SAVE_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String s = gson.toJson(this,this.getClass());
        out.write(s.getBytes());
        out.close();
        this.register = true;

    }
    // 设置和门锁关联
    public void setLock(String LockID,int masterId) throws Exception{  // 给 apduService 用, 在接受到 pn532 返回后的lockid和masterid后设置下.
        this.LockID = LockID;
        this.masterId = masterId;

        OutputStream out = MainActivity.mainContext.openFileOutput(MASTER_SAVE_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String s = gson.toJson(this,this.getClass());
        out.write(s.getBytes());
        out.close();
        this.haveLock = true;
    }

}
class savedata{
    private String masterName;
    private String masterPwd;
    private int    masterId;
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

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }
}
