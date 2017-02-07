import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.AuthorizationInfo;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.AuthenticationService;
import com.zy.gcode.service.CodeService;
import com.zy.gcode.service.IPayService;
import com.zy.gcode.service.pay.WxXmlParser;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.HttpClientUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Created by admin5 on 17/1/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class DaoTest {
    @Autowired
    PersistenceService persistenceService;
    @Autowired
    IPayService payService;

    @Autowired
    CodeService codeService;

    @Autowired
    AuthenticationService authenticationService;


    @Test
    public void test(){
        Resource resource = new ClassPathResource("config.properties");
        Properties properties = new Properties();
        try {
            properties.load(resource.getInputStream());
            FileOutputStream outputStream = new FileOutputStream(resource.getFile());
            properties.store(outputStream,null);
            System.out.println(properties.getProperty("precode.insert")==null);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void dome(){
        String url = "http://mp.weixin.qq.com/wiki/2/88b2bf1265a707c031e51f26ca5e6512.html?component_access_token=nH9FksTs4eWAM4EGJrTfTMsSGyJfuzN_5atu4aQpx0rbF624OWrtDYlNpeaf78E7pa9EIlDFHpO2VksaOW1Gfs9iFbqYFOHAd2eHLPOib0IpAGVswZ_X1Sh4bQcztjM2YCGfAGDUYC";
        String body = "{\"component_appid\":\"wxa8febcce6444f95f\"," +
                "\"authorizer_appid\":\"wx653d39223641bea7\"," +
                "\"authorizer_refresh_token\":\"refreshtoken@@@abWK9VW_xjLWXecdIcWw8fyfA-iUIen-reaDPjL6r3E\"}";
        HttpClientUtils.mapSSLPostSend(url,body);

    }
    @Test
    public void utils(){
       /* RedPayInfo redPayInfo = new RedPayInfo();
        redPayInfo.setAct_name("哥哥");
        redPayInfo.setTotal_num(22);
        System.out.println(WxXmlParser.getWxXml(redPayInfo));*/
       authenticationService.saveServerToken("{\"authorization_info\":{\"authorizer_appid\":\"wx653d39223641bea7\",\n" +
               "                \"authorizer_access_token\":\"nH9FksTs4eWAM4EGJrTfTMsSGyJfuzN_5atu4aQpx0rbF624OWrtDYlNpeaf78E7pa9EIlDFHpO2VksaOW1Gfs9iFbqYFOHAd2eHLPOib0IpAGVswZ_X1Sh4bQcztjM2YCGfAGDUYC\",\n" +
               "                \"expires_in\":7200,\n" +
               "                \"authorizer_refresh_token\":\"refreshtoken@@@abWK9VW_xjLWXecdIcWw8fyfA-iUIen-reaDPjL6r3E\",\n" +
               "                \"func_info\":[{\"funcscope_category\":{\"id\":1}},\n" +
               "            {\"funcscope_category\":{\"id\":15}},{\"funcscope_category\":{\"id\":4}},\n" +
               "            {\"funcscope_category\":{\"id\":2}},{\"funcscope_category\":{\"id\":9}}]}}");
    }

    @Test
    public void payService() throws Exception{
    //  Constants.objectMapper.readValue("{openId=ooBfdwNcoMaol2CF0zlcRUYkYE_Q, phone=null, nick=桂, province=浙江, city=杭州, country=中国, headImgUrl=http://wx.qlogo.cn/mmopen/t7Grpf3YAiaDtC3sPADibEhITpkusZlOLnxzoub617SnqmAPXeAb2dPb36ic2lrqttTM0DS9HqJDnJ8VYlRzFf4I8JfcPBeL2fp/0, privilege=[], unionId=null, sex=1, insertTime=null, updateTime=null}".replace("=",":"), User.class);
    System.out.println("S767sdf5sdHUfy8Sj7edR86wsEr6dh5giYfu6Tr7g8h".length());
    }

    public String te1(){
        System.out.println("t1");
return "return";
    }

    public String te(){
        try{
            return te1();
        }catch (NullPointerException e){
            System.out.println("catch");
        }finally {
            System.out.println("finally");
        }
        return null;
    }
}
