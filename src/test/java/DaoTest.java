import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.AuthorizationInfo;
import com.zy.gcode.pojo.DataOrder;
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

       System.out.println(persistenceService.getOneByColumn(DataOrder.class,"orderNumber","24423067612233391"));
    }
    @Test
    public void dome(){
        String a = "沈坚\uD83D\uDCAF";
        System.out.println(a.trim());
        }
    @Test
    public void utils(){
       /* RedPayInfo redPayInfo = new RedPayInfo();
        redPayInfo.setAct_name("哥哥");
        redPayInfo.setTotal_num(22);
        System.out.println(WxXmlParser.getWxXml(redPayInfo));*/

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
