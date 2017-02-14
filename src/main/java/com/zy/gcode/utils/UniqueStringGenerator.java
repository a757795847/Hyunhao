package com.zy.gcode.utils;


import org.apache.commons.codec.digest.Md5Crypt;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by admin5 on 17/1/17.
 */
public class UniqueStringGenerator {

    private static int generateCount = 0;



    private static Random random = new Random();
    private static BASE64Encoder base64Encoder = new BASE64Encoder();
    private static AtomicInteger integer = new AtomicInteger(100);
    private UniqueStringGenerator() {

    }


    public static synchronized String getUniqueCode() {
        if (generateCount > 999999)
            generateCount = 0;
        String uniqueNumber = Integer.toString(random.nextInt(1000000)+generateCount);
        generateCount++;
        return toBase64(uniqueNumber);
    }
    public static synchronized String getUniqueToken() {
        String uniqueNumber = Long.toString(System.currentTimeMillis())+Integer.toString(generateCount);
        generateCount++;
        return toBase64(uniqueNumber);
    }


    private static String toBase64(String str){
            return  base64Encoder.encode(Md5Crypt.apr1Crypt(str,"zykjss").getBytes()).replace("=","A").substring(17);
    }

    public static  String wxbillno(){
        String beg = String.valueOf(System.currentTimeMillis()).substring(6);
       int i =  integer.getAndIncrement();
        if(i>=999){
            integer.set(100);
        }
        return Constants.MCH_ID + DateUtils.format(new Date(),"yyyyMMdd")+beg+i;
    }

    public static String getMd5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String SHA1(String decrypt) {
        //获取信息摘要 - 参数字典排序后字符串

        try {
            //指定sha1算法
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decrypt.getBytes());
            //获取字节数组
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException();
    }


}
