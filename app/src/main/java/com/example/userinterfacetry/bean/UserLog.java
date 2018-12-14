package com.example.userinterfacetry.bean;

import android.content.Context;
import android.util.Log;

import com.example.userinterfacetry.MainActivity;
import com.example.userinterfacetry.MyServiceAPDU;
import com.example.userinterfacetry.utils.UtilTools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UserLog {
    static String TAG = "UserLog";

    public String name; // 进门人的昵称
    public UserType user;// 请求的时候的Type
    public int userID;     // masterid 或者是
    public PassType pass;
    public Date time;
    public int rawdatalen=0;

    public static List<UserLog> logList = new ArrayList<>();
    static {
        try {
            logList = LogSaver.loadFromFile();
            Log.d(TAG,"读取"+logList.size()+"条Log到内存");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public UserLog(String n, UserType u, int i, PassType p, Date t, int r){
        this.name = n; user = u; userID = i; time = t; pass = p;rawdatalen = r;
        android.util.Log.d(TAG,this.toString());
    }
    public static UserLog byte2Log(byte[] rawdata){
        int len = UtilTools.byte2int(rawdata[0]);
        byte[] name_b = Arrays.copyOfRange(rawdata,1,1+len);
        String name = UtilTools.hexByte2Pwd(name_b);
        UserType ut = UserType.Unknow_e;
        switch ( (rawdata[1+len] >> 4) & 0x0f ){
            case 1:
                ut = UserType.Master_e;
                break;
            case 2:
                ut = UserType.Guest_e;
                break;
            case 3:
                ut = UserType.Unknow_e;
                break;
        }

        PassType pt = PassType.Reject_e;
        switch ( (rawdata[1+len]) & 0x0f ){
            case 1:
                pt = PassType.Pass_e;
                break;
            case 2:
                pt = PassType.Reject_e;
                break;
        }

        int id = UtilTools.byte2int(rawdata[2+len]);
        long date_l = UtilTools.bytes2long( Arrays.copyOfRange(rawdata,3+len,11+len) );
        Log.e(TAG, "byte2Log: "+UtilTools.bytesToHex( Arrays.copyOfRange(rawdata,3+len,11+len) )+"\n"+date_l );
        Date date = new Date(date_l*1000);

        return new UserLog(name,ut,id,pt,date,11+len);

    }

    public static void addLogs(List<UserLog> logs) throws Exception{
        int c = 0;
        for( UserLog log : logs ){
            if( ! logList.contains(log) ) {
                c++;
                logList.add(log);
            }
        }
        LogSaver.writeToFile(logList);
        Log.d(TAG,"成功保存"+c+"条记录到文件中");
    }

    @Override
    public String toString() {
        return String.format("%s %s %s %s %s\n",
                name,user,userID,pass,
                time.toString());
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(((UserLog)obj).toString());
    }

    public static enum UserType{
        Master_e,Guest_e,Unknow_e;

    }
    public static enum PassType{
        Pass_e,Reject_e;
    }



}

class LogSaver{
    static String LogFileName = "LOG.txt";
    protected static List<UserLog> loadFromFile() throws Exception {
        List<UserLog> re = new ArrayList<>();
        Context c = MyServiceAPDU.serviceContext == null ? MainActivity.mainContext : MyServiceAPDU.serviceContext;
        InputStream inpufile = c.openFileInput(LogFileName);
        re = new Gson().fromJson( IOUtils.toString(inpufile) , new TypeToken<List<UserLog>>(){}.getType() );
        inpufile.close();
        return re;
    }
    protected static void writeToFile(List<UserLog> data) throws Exception{
        OutputStream out = MyServiceAPDU.serviceContext.openFileOutput(LogFileName,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        out.write(gson.toJson(UserLog.logList).getBytes());
        out.close();
    }
}
