package com.example.userinterfacetry;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCBC{

    private static String sKey="0000000000000000";         // 默认密码
    private static String ivParameter="0000000000000000"; // 偏移量
    private static String decode = "utf-8";

    private static AesCBC instance=null;
//    public static void runmain() throws Exception {
//        // 需要加密的字串
//        String cSrc = "a";
//        System.out.println(cSrc);
//        // 加密
//        long lStart = System.currentTimeMillis();
//        String enString = AesCBC.getInstance().encrypt(cSrc,decode,sKey,ivParameter);
//        System.out.println("加密后的字串是："+ enString);
//
//        long lUseTime = System.currentTimeMillis() - lStart;
//        System.out.println("加密耗时：" + lUseTime + "毫秒");
//        // 解密
//        lStart = System.currentTimeMillis();
//        String DeString = AesCBC.getInstance().decrypt(enString,decode,sKey,ivParameter);
//        System.out.println("解密后的字串是：" + DeString);
//        lUseTime = System.currentTimeMillis() - lStart;
//        System.out.println("解密耗时：" + lUseTime + "毫秒");
//    }

    private AesCBC(){

    }
    public static AesCBC getInstance(){
        if (instance==null)
            instance= new AesCBC();
        return instance;
    }

    // 加密
    public String encrypt(String sSrc, String sKey) throws Exception {
        String k = this.encrypt(sSrc,AesCBC.decode,sKey,AesCBC.ivParameter);
        System.out.println(k);
        return k;
    }
    public String decrypt(String sSrc,String sKey) throws Exception{
        String k = this.decrypt(sSrc,sKey, AesCBC.decode,AesCBC.ivParameter);
        System.out.println(k);
        return k;
    }

    // 加密
    public String encrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] raw = sKey.getBytes();
        System.out.println(sKey);
        System.out.println(UtilTools.bytesToHex(raw));

        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes(encodingFormat));
//        return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。
        System.out.println(UtilTools.bytesToHex(encrypted));
        return Base64.encodeToString(encrypted,Base64.DEFAULT).replaceAll("[\\s*\t\n\r]", "");
    }

    // 解密
    public String decrypt(String sSrc, String encodingFormat, String sKey, String ivParameter) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);//先用base64解密
            byte[] encrypted1 = Base64.decode(sSrc,Base64.DEFAULT);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original,encodingFormat);
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }




}
