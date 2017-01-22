import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.AuthorizationInfo;
import com.zy.gcode.service.AuthenticationService;
import com.zy.gcode.service.CodeService;
import com.zy.gcode.service.IPayService;
import com.zy.gcode.utils.HttpClientUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        codeService.token(null,null,"wx653d39223641bea7");
        AuthorizationInfo info = persistenceService.get(AuthorizationInfo.class,"wx653d39223641bea7");
        System.out.println(info.getAuthorizerAccessToken());
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
    public void payService(){
        System.out.println(HttpClientUtils.class.getClassLoader().getResource("").getFile());

       /* try {
            KeyStore keyStore  = KeyStore.getInstance("pem");
            FileInputStream instream = new FileInputStream(this.getClass().getClassLoader().getResourceAsStream("rootca.pem"));
            try {
                keyStore.load(instream, "10016225".toCharArray());
            } finally {
                instream.close();
            }

            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, "1426499802".toCharArray())
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[] { "TLSv1" },
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
          HttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
