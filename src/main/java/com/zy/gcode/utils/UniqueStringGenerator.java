package com.zy.gcode.utils;


import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
            if (v > 999) {
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
        int i = currentIncrement();
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
    private static final char[] toTappidTable= {
            'M', 'F', 'O', 'E', 'D', 'B', 'G', 'H', 'I', 'V', 'P', 'Z', 'A',
            'N', 'C', 'K', 'Q', 'e', 'S', 'T', 'i', 'J', 'W', 'X', 'l', 'm',
            '2', 'b', 'c', 'd', '8', '5', 'g', 'u', 'U', '9', 'j', 'y', 'L',
            'n', 'o', 'p', '3', '7', 's', 't', 'r', 'v', 'w', 'x', 'Y',
            '0', '1', 'a', 'q', '4', 'f', '6', 'h', 'R', 'k'};
    public static String toTappid(long id,long time){
        return transform(id)+"z"+transform(time);
    }

    public static long[] deTappid(String tappid){
        String[] strs = tappid.split("z");
        return new long[]{to10(strs[0]),to10(strs[1])};
    }

    private static int charAt(char a){
        int len = toTappidTable.length;
        for(int i = 0 ; i < len;i++){
            if(a==toTappidTable[i]){
                return i;
            }
        }
        return -1;
    }

    private static long to10(String str){
        long num = 0;
        int len = toTappidTable.length;
        char[] ids = str.toCharArray();
        int j =0;
        for(int i =ids.length-1 ;i>=0;i--){
            int n = charAt(ids[j]);
         //   Du.pl(ids[j]+":"+n);
            j++;
            num+=baseNum(len,i)*n;
        }
        return num^176999825;
    }
    private static long baseNum(int len,int n){
        long num = 1;
        for(int i=0;i < n;i++){
            num*=len;
        }
        return num;
    }


    private static String transform(long num){
        int n = toTappidTable.length;
        num^=176999825;
        StringBuilder builder = new StringBuilder();
        while(num!=0){//当输入的数不为0时循环执行求余和赋值
            Long remainder=num%n;
            num=num/n;
            builder.append(toTappidTable[remainder.intValue()]);
        }
        return builder.reverse().toString();
    }

    public static void main(String[] args) throws Exception{
        long i =0;
      while(true){
          Thread.sleep(10);
          long time = i++;
          Du.pl(time);
          if(time!=to10(transform(time))){
              Du.pl(transform(time));
              Du.pl(to10(transform(time)));
              throw new IllegalArgumentException(transform(time));
          }else {
              Du.pl(true);
          }

      }





    }


}
