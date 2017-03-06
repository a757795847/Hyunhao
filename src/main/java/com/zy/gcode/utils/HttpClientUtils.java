package com.zy.gcode.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by admin5 on 17/1/17.
 */
public class HttpClientUtils {


    private static HttpClient httpClient;
    /**
     * 存储不同商户的集合
     */
    private static Map<String, HttpClient> stringHttpClientMap = new ConcurrentHashMap<>();

    //初始化httpClient
    static {
        try {
            httpClient = HttpClientBuilder.create().setMaxConnPerRoute(10).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * http get请求发送，支持部分https,当http请求半途终端或协议本身错误
     * 会返回空
     *
     * @param url
     * @return
     */
    public static HttpResponse getSend(String url) {
        HttpGet get = new HttpGet(url);
        try {
            return httpClient.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            get.releaseConnection();
        }
        return null;
    }

    public static void checkRespons(HttpResponse response) {
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new IllegalStateException("http 返回吗费等与200");
        }


    }

    /**
     * http get请求发送,参数可以通过可变字符串形式传入，形式为key，value的
     * 连续性结构
     *
     * @param url
     * @param params
     * @return
     */
    public static HttpResponse getSend(String url, String... params) {
        int len = params.length;
        if (len % 2 != 0) {
            throw new IllegalArgumentException();
        }
        StringBuilder builder = new StringBuilder(url).append("?");
        for (int i = 0; i < len; i += 2) {
            if (i != 0) {
                builder.append("&");
            }
            builder.append(params[i]).append("=").append(params[i + 1]);
        }
        return getSend(builder.toString());
    }

    /**
     * 处理文件下载http请求
     *
     * @param url  资源地址
     * @param path 存储路径，如果不存在，则会自动创建
     * @return
     */
    public static boolean fileGetSend(String url, String path) {
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(get);
            return transferTo(response, path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            get.releaseConnection();
        }
    }

    /**
     * 把response返回二进制流存储到制定路径
     *
     * @param response
     * @param path
     * @return 当创建新文件失败时会返回false
     */
    private static boolean transferTo(HttpResponse response, String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            response.getEntity().writeTo(fileOutputStream);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * http post 请求发送
     *
     * @param url
     * @param body
     * @return
     */
    public static HttpResponse postSend(String url, String body) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Connection", "close");
        StringEntity entity = new StringEntity(body, "utf-8");//解决中文乱码问题
        entity.setContentEncoding("UTF-8");
        httpPost.setEntity(entity);
        try {
            return httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }
        return null;
    }

    /**
     * http post 请求参数化发送
     *
     * @param url
     * @param params 会自动生成为json
     * @return
     */
    public static HttpResponse postSend(String url, String... params) {

        int len = params.length;
        if (len % 2 != 0) {
            throw new IllegalArgumentException();
        }
        StringBuilder builder = new StringBuilder("{");
        for (int i = 0; i < len; i += 2) {
            builder.append("\"").append(params[i]).append("\"").append(":")
                    .append("\"").append(params[i + 1]).append("\"").append(",");
        }
        return postSend(url, builder.substring(0, builder.length() - 1) + "}");

    }

    /**
     * http post请求发送，返回的json会被转化为map
     *
     * @param url
     * @param body
     * @return
     */
    public static Map mapPostSend(String url, String body) {
        HttpResponse response = postSend(url, body);
        return res2Map(response);
    }

    /**
     * 转化response的返回数据为map
     *
     * @param response
     * @return
     */
    private static Map res2Map(HttpResponse response) {
        if (response == null) {
            throw new NullPointerException("response is null");
        }
        try {
            if (response.getStatusLine().getStatusCode() != 200) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                return null;
            }
            Map map = JsonUtils.asObj(Map.class,response.getEntity().getContent());
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map mapPostSend(String url, String... params) {
        HttpResponse response = postSend(url, params);
        return res2Map(response);
    }

    public static String stringGetSend(String url,String... params){
        HttpResponse response = getSend(url,params);
        return res2String(response);
    }


    public static Map mapGetSend(String url) {
        HttpResponse response = getSend(url);
        return res2Map(response);
    }

    public static Map mapGetSend(String url, String... params) {
        HttpResponse response = getSend(url, params);
        return res2Map(response);
    }


    private static String res2String(HttpResponse response){
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        if (response.getStatusLine().getStatusCode() != 200) {
            System.out.println(builder.toString());
            return null;
        }
        return builder.toString();
    }

    /**
     * 用于商户支付的http请求
     *
     * @param mechid
     * @param path
     * @param url
     * @param body
     * @return
     */

    public static HttpResponse paySSLSend(String mechid, String path, String url, String body) {
        if (stringHttpClientMap.containsKey(mechid)) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Connection", "close");
            StringEntity entity = new StringEntity(body, "utf-8");//解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            httpPost.setEntity(entity);
            try {
                return stringHttpClientMap.get(mechid).execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpPost.releaseConnection();
            }
            return null;
        } else {
            try (InputStream stream = new FileInputStream(path)) {
                KeyStore keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(stream, mechid.toCharArray());
                SSLContext sslcontext = SSLContexts.custom()
                        .loadKeyMaterial(keyStore, mechid.toCharArray())
                        .build();
                HttpClient httpClient1 = HttpClientBuilder.create().setMaxConnPerRoute(2).setSSLContext(sslcontext).build();
                stringHttpClientMap.put(mechid, httpClient1);
                return paySSLSend(mechid, path, url, body);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }


}
