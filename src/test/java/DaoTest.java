import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.AuthorizationInfo;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.AuthenticationService;
import com.zy.gcode.service.CodeService;
import com.zy.gcode.service.IPayService;
import com.zy.gcode.service.annocation.CsvPush;
import com.zy.gcode.service.pay.WxXmlParser;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.HttpClientUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashService;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.AnnotationBeanWiringInfoResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ClassUtils;
import sun.jvm.hotspot.runtime.Bytes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
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

    @Autowired
    PasswordService passwordService;

    @Test
    public void test(){

       System.out.println(persistenceService.getOneByColumn(DataOrder.class,"orderNumber","24423067612233391"));
    }
    @Test
    public void dome(){
        System.out.println(persistenceService.count(DataOrder.class));
        }
    @Test
    public void utils(){
        DefaultPasswordService passwordService = new DefaultPasswordService();
        DefaultHashService hashService = new DefaultHashService();
        hashService.setHashIterations(2);
        passwordService.setHashService(hashService);

        hashService.setGeneratePublicSalt(true);
        PasswordMatcher passwordMatcher = new PasswordMatcher();
        passwordMatcher.setPasswordService(passwordService);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("zhang123","123456");
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo("zhang123",passwordService.encryptPassword("123456"),"myreal");
       boolean flag = passwordMatcher.doCredentialsMatch(usernamePasswordToken,simpleAuthenticationInfo);

        System.out.println(flag);
        System.out.println(passwordService.encryptPassword("123456"));
        System.out.println(passwordService.encryptPassword("123456"));
    }

    @Test
    public void password(){
        try {
            String bs =  InetAddress.getLocalHost().getHostAddress();
            System.out.println(new String(bs));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
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
