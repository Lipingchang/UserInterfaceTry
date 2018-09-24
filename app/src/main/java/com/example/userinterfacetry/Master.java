package com.example.userinterfacetry;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Master {
    static final public String MASTER_SAVE_FILE = "MASTERINFO";
    static private Master masterInstance;

    private boolean register = false;
    private String masterName = "not set";
    private String masterPwd = "not set";
    private int    masterId = -1;

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
            this.masterName = data.getMasterName();
            this.masterPwd = data.getMasterPwd();

             register = true;
         }catch (Exception e){
             //file not Found!
             register = false;
             return;
        }
    }

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
}
class savedata{
    private String masterName;
    private String masterPwd;
    private int    masterId;

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
