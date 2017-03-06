package com.zy.gcode.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by admin5 on 17/2/7.
 */
public abstract class Constants {
    public static Properties properties = new Properties();


    /**
     *微信token 回调url
     */

    /**
     * 获取code成功后的回调url
     */

    public final static String CALL_BACK_URL = "http://open.izhuiyou.com/access/wxaccess_token";

    /**
     * 微信js_ticket的字段名
     */

    public final static String JSSDK_TICKET_NAME = "jssdk_ticket";

    /**
     * 微信服务好的ticket的字段名
     */

    public final static String COMPONENTVERIFYTICKET = "componentverifyticket";

    /**
     * 返现截图存放的路径
     */

    public final static String RED_PICTURE_PATH = "/opt/zy/Pictures";

    /**
     * 上传的csv存放路径
     */

    public final static String RED_CSV_PATH = "/opt/zy/Downloads";

    /**
     * 支付二维码存放路径
     */

    public final static String PAY_QR_PATH = "/opt/zy/Downloads";

    /**
     * 这个应用的id
     */

    public final static String ZYAPPID = "zyappid1";



    static {
        try (InputStream inputStream = new ClassPathResource("config.properties").getInputStream()) {
            properties.load(inputStream);
        } catch (Exception e) {
            System.err.println("error 配置文件加载错误!");
            e.printStackTrace();
            System.exit(0);
        }
    }


}
