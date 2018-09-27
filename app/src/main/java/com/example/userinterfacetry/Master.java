package com.example.userinterfacetry;

import android.content.Context;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Master {
    static final public String MASTER_SAVE_FILE = "MASTERINFO";
    static private Master masterInstance;

    private boolean register = false;
    private boolean haveLock = false;
    private String masterName = "not set";
    private String masterPwd = "not set";
    private int    masterId = -1;
    private String LockID = "not set";

    public static Master getMasterInstance(){
        if( masterInstance == null ){
            masterInstance = new Master();
        }
        return masterInstance;
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
             haveLock = this.masterId != -1;// master 有没有 和门锁关联过。
         }catch (Exception e){
             //file not Found!
             register = false;
             haveLock = false;
             return;
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

    public void setMaster(String name,String pwd) throws Exception {
        this.masterName = name;
        this.masterPwd = pwd;
        this.masterId = -1;

        OutputStream out = MainActivity.mainContext.openFileOutput(MASTER_SAVE_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String s = gson.toJson(this,this.getClass());
        out.write(s.getBytes());
        out.close();

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
