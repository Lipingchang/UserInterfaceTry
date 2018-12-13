package com.example.userinterfacetry;

import android.app.Service;
import android.os.Vibrator;

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
    public static byte int2byte(int i ){
        return (byte)( i & 0xff);
    }
    public static void longVibrator(){
        Vibrator vibrator = (Vibrator)MainActivity.mainContext.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(800);
    }
    public static void doubleVibrator(){
        Vibrator vibrator = (Vibrator)MainActivity.mainContext.getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{0,200,300,300},-1);

    }
    public static int byte2int(byte b){
        int i = 0;
        i = (b>>4) & 0x0f;
        i = i<<4;
        i += (b & 0x0f);
        return i;
    }
    public static long bytes2long(byte[] bytes){
        long ii = 0;
        for( int i = 7; i>=0; i--){

            ii = (ii<<8) + ((int)(bytes[i]) & 0xff );
            System.out.printf("==> %x %x\n",bytes[i] ,ii);
        }
        return ii;
    }
    public static String bytesToHex(byte[] apdu){
        if( apdu  == null ){
            return "null apdu byte[]";
        }
        StringBuffer k = new StringBuffer("");
        for( int i = 0; i < apdu.length; i++ ){
            k.append(String.format("%02x ",apdu[i]));
        }
        return k.toString();
    }

}
