package com.zy.gcode.utils;


import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by admin5 on 17/1/17.
 */
public class UniqueStringGenerator {

    private static int generateCount = 0;


    private static Random random = new Random();
    private static AtomicInteger value = new AtomicInteger(100);

    private UniqueStringGenerator() {

    }

    private static int currentIncrement() {
        int v;
        do {
            v = value.get();
            if(v >999){
                value.set(100);
            }

        } while (!value.compareAndSet(v, v + 1));
        return v + 1;
    }


    public static synchronized String getUniqueCode() {
        if (generateCount > 999999)
            generateCount = 0;
        String uniqueNumber = Integer.toString(random.nextInt(1000000) + generateCount);
        generateCount++;
        return toBase64(uniqueNumber);
    }

    public static synchronized String getUniqueToken() {
        String uniqueNumber = Long.toString(System.currentTimeMillis()) + Integer.toString(generateCount);
        generateCount++;
        return toBase64(uniqueNumber);
    }


    private static String toBase64(String str) {
        return Base64Utils.encodeToString(Md5Crypt.apr1Crypt(str, "zykjss").getBytes()).replace("=", "A").substring(17);
    }

    public static String wxbillno(String mchId) {
        String beg = String.valueOf(System.currentTimeMillis()).substring(6);
        int i = currentIncrement() ;
        return mchId + DateUtils.format(new Date(), "yyyyMMdd") + beg + i;
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

    public static String SHA1(String str) {
        System.out.println(str);
        if (null == str || 0 == str.length()) {
            return null;
        }
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
