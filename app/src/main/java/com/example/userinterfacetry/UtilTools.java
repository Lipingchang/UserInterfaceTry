package com.example.userinterfacetry;

import java.util.Date;

public class UtilTools {
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
}
