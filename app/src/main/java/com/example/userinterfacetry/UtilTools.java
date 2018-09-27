package com.example.userinterfacetry;

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
}
