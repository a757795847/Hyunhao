package com.zy.gcode.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.shiro.codec.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin5 on 17/3/21.
 */
public class JwtUtils {
    private static Algorithm algorithm;
    private static final String AUTHORIZATION = "authorization";
    private static final String ACCESS_CONTROL_EXPOSE_HEADERS = "access-control-expose-headers";
    private static final String secret = "";

    static {
        try {
            algorithm = Algorithm.HMAC256("secret");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String enJwt(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*600))
                .sign(algorithm);
    }

    public static Map deJwt(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        Map map = new HashMap();
        map.putAll(jwt.getClaims());
        return map;
    }
    public static Map deJwtWithExpires(String token) {
        JWTVerifier verifier = JWT.require(algorithm).acceptExpiresAt(3600).build();
        DecodedJWT jwt = verifier.verify(token);
        Map map = new HashMap();
        map.putAll(jwt.getClaims());
        return map;
    }

    public static void setResponse(HttpServletResponse response,String username){
        response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, AUTHORIZATION);
        response.setHeader(AUTHORIZATION, JwtUtils.enJwt(username));
    }

    public static Map deJwtWithTwo(String token){
        if(token==null)
            return null;
        Map map;
        try {
            map=deJwt(token);
        } catch (InvalidClaimException e) {
            e.printStackTrace();
            try {
                map=deJwtWithExpires(token);
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        }
        return map;

    }


    private static String deCode(String content, int type) {
        SecretKeySpec key = new SecretKeySpec(Hex.decode("369a854208a071782ff35262586dceb3"), "AES");
        Cipher cipher = null;// 创建密码器
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }
        byte[] byteContent = content.getBytes();
        try {
            cipher.init(type, key);// 初始化
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
        byte[] result;
        try {
            if (type == 2)
                byteContent = Hex.decode(byteContent);
            result = cipher.doFinal(byteContent);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
            return null;
        } catch (BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
        if (type == 2) {
            return new String(result);
        }
        return Hex.encodeToString(result); // 加密
    }

}
