package com.zy.gcode.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zy.gcode.filter.JwtHttpAuthenticationFilter;
import com.zy.gcode.service.OperatorService;
import com.zy.gcode.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin5 on 17/3/21.
 */
public class JwtUtils{
    private static Algorithm algorithm;
    private static final String AUTHORIZATION = "authorization";
    private static final String ACCESS_CONTROL_EXPOSE_HEADERS = "access-control-expose-headers";
    private static final String secret = "369a854208a071782ff35262586dceb3";
    private static ApplicationContext applicationContext;

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
                .withJWTId(getUserState(username))
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*10))
                .sign(algorithm);
    }

    public static String enJwt(String username,String... params) {
        JWTCreator.Builder builder = JWT.create()
                .withSubject(username)
                .withJWTId(getUserState(username))
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*10));
            if(params.length!=0){
                if(params.length%2==0){
                    throw  new IllegalArgumentException();
                }
                for(int i = 0 ; i <params.length;i+=2){
                    builder.withClaim(params[i],params[i+1]);
                }

            }
            return    builder.sign(algorithm);
    }
    public static String enJwtWithNoExpires(Map map) {
        JWTCreator.Builder builder = JWT.create();
           if(!map.isEmpty()){
             map.forEach((k,v)->{
                 if(!k.equals(PublicClaims.EXPIRES_AT)){
                     if(v instanceof Claim)
                     builder.withClaim((String)k,((Claim) v).asString());
                     builder.withClaim(k.toString(),v.toString());
                 }
             });
           }
        return builder.sign(algorithm);
    }

    public static String claimAsString(String key){
        return ((Claim)SecurityUtils.getSubject().getSession().getAttribute("key")).asString();
    }


    private static String getUserState(String username){
        UserService service = applicationContext.getBean(UserService.class);
       return service.getState(username);
    }


    public static Map deJwt(String token) {
        if(token==null){
            return null;
        }
        if(Constants.debug){
            Du.pl("jwtExpiresAt:"+JWT.decode(token).getExpiresAt());
        }
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt;
        jwt = verifier.verify(token);
        if(!jwt.getClaim(PublicClaims.JWT_ID).asString().equals(getUserState(jwt.getSubject()))){
            return null;
        }

        Map map = new HashMap();
        map.putAll(jwt.getClaims());
        return map;
    }
    public static Map deJwtWithExpires(String token) {
        JWTVerifier verifier = JWT.require(algorithm).acceptExpiresAt(3600).build();
        DecodedJWT jwt = verifier.verify(token);
        if(!jwt.getClaim(PublicClaims.JWT_ID).asString().equals(getUserState(jwt.getSubject()))){
            return null;
        }
        Map map = new HashMap();
        map.putAll(jwt.getClaims());
        return map;
    }

    public static void setResponse(HttpServletResponse response,String username){
        if(response.getHeader(AUTHORIZATION)!=null){
            return;
        }

        if(username==null){
            response.setStatus(401);
            return;
        }
        response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, AUTHORIZATION);
        response.setHeader(AUTHORIZATION, JwtUtils.enJwt(username));
    }
    public static void setResponseWithNoExpires(HttpServletResponse response,String username){
        if(response.getHeader(AUTHORIZATION)!=null){
            return;
        }

        if(username==null){
            response.setStatus(401);
            return;
        }
        response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, AUTHORIZATION);
        Session session = SecurityUtils.getSubject().getSession();
        Map map = new HashMap();

        session.getAttributeKeys().forEach(k->
            map.put(k,session.getAttribute(k))
        );


        response.setHeader(AUTHORIZATION, JwtUtils.enJwtWithNoExpires(map));
    }

    public static Map deJwtWithTwo(String token){
        if(token==null)
            return null;
        Map map;
        Map result = new HashMap();
        try {
            map=deJwt(token);
        } catch (InvalidClaimException e) {
            e.printStackTrace();
            try {
                result.put("authenticated",false);
                map=deJwtWithExpires(token);
            } catch (InvalidClaimException e1) {
                e1.printStackTrace();
                return null;
            }
        }
        if(map!=null&&!map.isEmpty()){
            result.putAll(map);

        }

        return result;

    }


    private static String deCode(String content, int type) {
        SecretKeySpec key = new SecretKeySpec(Hex.decode(secret), "AES");
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

    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JwtUtils.applicationContext = applicationContext;
    }
}
