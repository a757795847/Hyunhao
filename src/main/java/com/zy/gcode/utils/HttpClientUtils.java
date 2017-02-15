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
import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin5 on 17/1/17.
 */
public class HttpClientUtils {
    private static HttpClient httpClient;

    private static Map<String,HttpClient> stringHttpClientMap = new ConcurrentHashMap<>();

    static {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            Resource resource = new ClassPathResource("apiclient_cert.p12");
            keyStore.load(resource.getInputStream(), "1426499802".toCharArray());
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, Constants.MCH_ID.toCharArray())
                    .build();
            httpClient = HttpClientBuilder.create().setMaxConnPerRoute(5).setSSLContext(sslcontext).build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static HttpResponse SSLGetSend(String url) {
        HttpGet get = new HttpGet(url);
        try {
            return httpClient.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            get.releaseConnection();
        }
        return null;
    }

    public static boolean fileGetSend(String url,String path) {
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(get);
            return transferTo(response,path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }finally {
            get.releaseConnection();
        }
    }

    private static boolean transferTo(HttpResponse response,String path){
        File file = new File(path);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
            response.getEntity().writeTo(fileOutputStream);

        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
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
        }finally {
            httpPost.releaseConnection();
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
            Map map = Constants.objectMapper.readValue(response.getEntity().getContent(), Map.class);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map mapSSLGetSend(String url) {
        HttpResponse response = SSLGetSend(url);
        if (response == null) {
            throw new NullPointerException("response is null");
        }


        try {
            if (response.getStatusLine().getStatusCode() != 200) {
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))){
                    String line;
                    while((line =reader.readLine())!=null){
                        System.out.println(line);
                    }
                }
                return null;
            }
            Map map = Constants.objectMapper.readValue(response.getEntity().getContent(), Map.class);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpResponse paySSLSend(String mechid,String path,String url,String body){
        if(stringHttpClientMap.containsKey(mechid)){
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Connection","close");
            StringEntity entity = new StringEntity(body, "utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            httpPost.setEntity(entity);
            try {
                return stringHttpClientMap.get(mechid).execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                httpPost.releaseConnection();
            }
            return null;
        }else {
            try (InputStream stream = new FileInputStream(path)){
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(stream, mechid.toCharArray());
                SSLContext sslcontext = SSLContexts.custom()
                        .loadKeyMaterial(keyStore, Constants.MCH_ID.toCharArray())
                        .build();
            HttpClient   httpClient1 = HttpClientBuilder.create().setMaxConnPerRoute(5).setSSLContext(sslcontext).build();
            stringHttpClientMap.put(mechid,httpClient1);
            return paySSLSend(mechid,path,url,body);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (UnrecoverableKeyException e){
                e.printStackTrace();
            }catch (KeyStoreException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return null;
    }


}
