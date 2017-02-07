package com.zy.gcode.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by admin5 on 17/2/7.
 */
public abstract class Constants {
    public static Properties properties = new Properties();

    public static ObjectMapper objectMapper = new ObjectMapper();

    /**
     *微信token 回调url
     */
    public final static String CALL_BACK_URL="http://open.izhuiyou.com/code/userinfo";

    /**
     * 商户号
     */
    public  static  final String MCH_ID="1426499802";
    static {
        try(InputStream inputStream = new ClassPathResource("config.properties").getInputStream()) {
            properties.load(inputStream);
        } catch (Exception e) {
            System.err.println("error 配置文件加载错误!");
            e.printStackTrace();
            System.exit(0);
        }
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }


    static {

    }


}