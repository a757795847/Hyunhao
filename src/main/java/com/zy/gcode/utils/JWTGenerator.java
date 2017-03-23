package com.zy.gcode.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin5 on 17/3/21.
 */
public class JWTGenerator {

    private static final String salt = "xyoj;";

    private Map<String,String> header = new HashMap<>();
    private Map<String,String> payload = new HashMap<>();
    private String secret;
    private String signature;

    public void putHeader(String key,String value){
        header.put(key,value);
    }

    public void putPayload(String key,String value){
        payload.put(key,value);
    }

    public void setSecret(String secret){
        this.secret = secret;
    }

    public String getResult(){
        StringBuilder builder = new StringBuilder();
      builder.append(toBase64(JsonUtils.objAsString(header)))
              .append(".").append(toBase64(JsonUtils.objAsString(payload)));
        if(header.containsKey("alg")){
            return builder.toString()+"."+generateSignature(builder.toString(),header.get("alg"));
        }

        return builder.toString()+"."+generateSignature(builder.toString(),"md5");
    }



    private String toBase64(String str){
       return Base64.getEncoder().encodeToString(str.getBytes());
    }

    private String generateSignature(String content,String type){
        try {
            MessageDigest messageDigest =  MessageDigest.getInstance(type);
            messageDigest.update((content+salt).getBytes());
            StringBuilder builder = new StringBuilder("");
            byte[] b = messageDigest.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    builder.append("0");
                builder.append(Integer.toHexString(i));
            }
            //32位加密
            return builder.toString();

        } catch (NoSuchAlgorithmException e) {
            throw  new IllegalArgumentException("签名算法("+type+")不存在！");
        }
    }



}
