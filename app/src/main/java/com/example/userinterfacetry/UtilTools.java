package com.example.userinterfacetry;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class UtilTools {
    static String[] hexlist = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
    /**
     * 检查传入的字符串 能不能作为密码。
     * 8-16位
     * @param s
     * @return
     */
    public static boolean MasterPwdCheck(String s){
        if( s.length() <8 || s.length() >16)
            return false;
        return true;
    }
    public static String elapsedTime(Date before){
        String[] append = {"分钟前","小时前","天前","月前"};
        int[][] split = {{60,60},{3600,24},{24*3600,30},{24*3600*30,12} };
        Date now = new Date(System.currentTimeMillis());
        int passedSeconds = (int)(now.getTime() - before.getTime())/1000;
        for( int i = 0; i<append.length; i++ ){
            if( passedSeconds / split[i][0] < split[i][1] )
                return (passedSeconds/split[i][0]+1)+ append[i];
        }
        return "几年前";
    }

    public static String lockID2String(byte[] lockID){
        String re = "";
        for( int i = 0; i < lockID.length; i++ ){
            re += hexByte2String(lockID[i]);
        }
        return re;
    }
    public static String hexByte2String(byte b){
        return hexlist[ 0x0f & (b>>4) ]+hexlist[ 0x0f & b ];
    }
    public static String hexByte2Pwd(byte[] pwd){
        String re = "";
        for ( int i = 0; i < pwd.length; i++ ){
            int ascii = 0;
            ascii += ( 0x0f & pwd[i]) + ((0x0f & (pwd[i]>>4))<<4) ;
            re += Character.toString((char)ascii);
        }
        return re;
    }
    public static byte[] pwd2HexByte(String pwd){
        System.out.println(pwd);
        return pwd.getBytes( StandardCharsets.US_ASCII );
    }
}
