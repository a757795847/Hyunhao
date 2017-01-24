package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.gcode.service.pay.WxXmlParser;
import com.zy.gcode.utils.wx.AesException;
import com.zy.gcode.utils.wx.WXBizMsgCrypt;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
/**
 * Created by admin5 on 17/1/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:applicationContext.xml","classpath:dispatcher-servlet.xml"})
public class CodeControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void geAppid() throws Exception {
        /*signature=bff3299d80cc285f7a5d339698672bdb9046750c
        timestamp=1484976266
        nonce=1056442305
        encrypt_type=aes
        msg_signature=33ba00212c783c8d3893a754e01a1575118ca83a
                <xml>
    <AppId><![CDATA[wxa8febcce6444f95f]]></AppId>
    <Encrypt><![CDATA[Xlp1l7cm/uRkfZtTs2LPW1EP5B5A28BMo1iHV4QBMQUqqE7pf7NVQQlLS7mDdpQjQd9cXFEEIRghU0nDG95zsiqeIUNGa4bvyiKsr+lYgRV+pifhR948EVNXyLcuwFF+pxNfHTH/KztVBFOeYVVobiUyFynxeJlZz3FR+aeI49QcWWoh++G6f9Vce2dAbcnVszSpOkCc5zb4lPBqkLGnZJ/ViqgKKCchi7JSmTyd1IYEow6r/xOxEqgOEYkSpQAsj35LYTd1I/KC1v8kVk/rufD4y1/WS7zmA4V5dLbz69V9XQO5Jo0j4UC92D1XXeEbHb6fNU3wfggGnR8YL9aNdY6it3ltCBtncvHjyiTWn6G5dodtfzQGMqxzZkO9iZ5/ZPT2X+JBYHBa5B1W7qMY+eDmhhJxv7juUjFXRld+fLne5o/CRPaLv6FQuHGX8oN1zHCEgU69C8sGDGoIg0LkyQ==]]></Encrypt>
</xml>*/
        /*System.out.println("S767sdf5sdHUfy8Sj7edR86wsEr6dh5giYfu6Tr7g8h".length());
        try {
            String msg_signature="33ba00212c783c8d3893a754e01a1575118ca83a";
            String timestamp = "1484976266";
            String nonce="1056442305";
            String str =" <xml>\n" +
                    "    <AppId><![CDATA[wxa8febcce6444f95f]]></AppId>\n" +
                    "    <Encrypt><![CDATA[Xlp1l7cm/uRkfZtTs2LPW1EP5B5A28BMo1iHV4QBMQUqqE7pf7NVQQlLS7mDdpQjQd9cXFEEIRghU0nDG95zsiqeIUNGa4bvyiKsr+lYgRV+pifhR948EVNXyLcuwFF+pxNfHTH/KztVBFOeYVVobiUyFynxeJlZz3FR+aeI49QcWWoh++G6f9Vce2dAbcnVszSpOkCc5zb4lPBqkLGnZJ/ViqgKKCchi7JSmTyd1IYEow6r/xOxEqgOEYkSpQAsj35LYTd1I/KC1v8kVk/rufD4y1/WS7zmA4V5dLbz69V9XQO5Jo0j4UC92D1XXeEbHb6fNU3wfggGnR8YL9aNdY6it3ltCBtncvHjyiTWn6G5dodtfzQGMqxzZkO9iZ5/ZPT2X+JBYHBa5B1W7qMY+eDmhhJxv7juUjFXRld+fLne5o/CRPaLv6FQuHGX8oN1zHCEgU69C8sGDGoIg0LkyQ==]]></Encrypt>\n" +
                    "</xml>";
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt("ji0a4n2s6o7n","S767sdf5sdHUfy8Sj7edR86wsEr6dh5giYfu6Tr7g8h","wxa8febcce6444f95f");
            System.out.println(wxBizMsgCrypt.decryptMsg(msg_signature,timestamp,nonce,str));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
/*     System.out.println(   WxXmlParser.elementString("<xml><AppId><![CDATA[wxa8febcce6444f95f]]></AppId>\n" +
                "<CreateTime>1484976266</CreateTime>\n" +
                "<InfoType><![CDATA[component_verify_ticket]]></InfoType>\n" +
                "<ComponentVerifyTicket><![CDATA[ticket@@@ckeb06Yq3ou7tWt3ibSH28nxzf3IaOmMLBhaMeM5Y5xiJvN54iK0lBJFP04EwxSHpNVN2_O6EC6endK3-9GzHQ]]></ComponentVerifyTicket>\n" +
                "</xml>","ComponentVerifyTicket"));*/
    }
   @Test
    public void code() throws Exception{
        this.mockMvc.perform(get("/middle/testa").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(MockMvcResultHandlers.print());
      /* ObjectMapper objectMapper = new ObjectMapper();
     Map<String,Map> map = objectMapper.readValue("{\"authorization_info\":{\"authorizer_appid\":\"wx653d39223641bea7\",\"authorizer_access_token\":\"nH9FksTs4eWAM4EGJrTfTMsSGyJfuzN_5atu4aQpx0rbF624OWrtDYlNpeaf78E7pa9EIlDFHpO2VksaOW1Gfs9iFbqYFOHAd2eHLPOib0IpAGVswZ_X1Sh4bQcztjM2YCGfAGDUYC\",\"expires_in\":7200,\"authorizer_refresh_token\":\"refreshtoken@@@abWK9VW_xjLWXecdIcWw8fyfA-iUIen-reaDPjL6r3E\",\"func_info\":[{\"funcscope_category\":{\"id\":1}},{\"funcscope_category\":{\"id\":15}},{\"funcscope_category\":{\"id\":4}},{\"funcscope_category\":{\"id\":2}},{\"funcscope_category\":{\"id\":9}}]}}", Map.class);
        println(map.get("authorization_info").get("func_info").getClass());*/
    }
    public void println(Object str){
        System.out.println(str);
    }


}
