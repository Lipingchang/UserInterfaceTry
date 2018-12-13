package com.example.userinterfacetry.bean;

import android.util.Log;

import com.example.userinterfacetry.utils.UtilTools;

import java.util.Arrays;
import java.util.Date;

public class UserLog {
    static String TAG = "UserLog";
    private String name;
    private UserType user;
    private int userID;
    private PassType pass;
    private Date time;
    public int rawdatalen=0;

    public UserLog(String n, UserType u, int i, PassType p, Date t, int r){
        this.name = n; user = u; userID = i; time = t; pass = p;rawdatalen = r;
        android.util.Log.d(TAG,String.format("%s %s %s %s %s\n",n,u,i,p,t.toString()));
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
}
enum UserType{
    Master_e,Guest_e,Unknow_e;

}
enum PassType{
    Pass_e,Reject_e;
}

