package com.zy.gcode.utils;

import com.zy.gcode.service.CodeService;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by admin5 on 17/1/17.
 */
public class HttpClientUtils {
    private static HttpClient httpClient;

    static {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            Resource resource = new ClassPathResource("apiclient_cert.p12");
            keyStore.load(resource.getInputStream(), "1426499802".toCharArray());
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, "1426499802".toCharArray())
                    .build();
            httpClient = HttpClientBuilder.create().setSSLContext(sslcontext).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static HttpResponse SSLGetSend(String url) {
        HttpGet get = new HttpGet(url);
        get.setHeader("Connection","close");
        try {
            return httpClient.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpResponse SSLPostSend(String url, String body) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Connection","close");
        StringEntity entity = new StringEntity(body, "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        httpPost.setEntity(entity);
        try {
            return httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map mapSSLPostSend(String url, String body) {
        HttpResponse response = SSLPostSend(url, body);
        if (response == null) {
            return null;
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            return null;
        }

        try {
            Map map = CodeService.objectMapper.readValue(response.getEntity().getContent(), Map.class);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map mapSSLGetSend(String url) {
        HttpResponse response = SSLGetSend(url);
        if (response == null) {
            return null;
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            return null;
        }

        try {
            Map map = CodeService.objectMapper.readValue(response.getEntity().getContent(), Map.class);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
