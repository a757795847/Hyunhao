package com.zy.gcode.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zy.gcode.utils.Du;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.AesCipherService;
import org.springframework.core.io.PathResource;
import redis.clients.jedis.Jedis;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.util.Base64;

/**
 * Created by admin5 on 17/3/22.
 */
public class SingleAesCipherService extends AesCipherService {
    private Key key;

    private final static String keyFile = "~/test/aeskey.obj";

    public SingleAesCipherService(){
        super();
        File file = new File(keyFile);
        if(!file.exists()){
            super.setKeySize(128);
            key = new SecretKeySpec(Hex.decode("369a854208a071782ff35262586dceb3"),"AES");
        }else {
         try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))){
            key = (Key)inputStream.readObject();

         }catch (FileNotFoundException e){
             e.printStackTrace();
             key = generateNewKey(128);
         }catch (IOException e){
             e.printStackTrace();
             key = generateNewKey(128);
         }catch (ClassNotFoundException e){
             e.printStackTrace();
             key = generateNewKey(128);
         }
        }
    }

    public String encryptToBase64(String str){
       return super.encrypt(str.getBytes(),key.getEncoded()).toHex();
    }

    public String decrypt(String str){
       return new String(super.decrypt(Hex.decode(str),key.getEncoded()).getBytes());
    }

    public static void main(String[] args){
        Jedis jedis = new  Jedis("192.29.188.190", 6379);
        Du.pl(jedis.get("key"));
    }


}
