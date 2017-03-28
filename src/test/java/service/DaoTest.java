package service;

import com.zy.gcode.cache.UserCache;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.AuthenticationService;
import com.zy.gcode.service.CodeService;
import com.zy.gcode.service.IPayService;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.Du;
import com.zy.gcode.utils.JsonUtils;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.crypto.AesCipherService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    UserCache userCache;

    @Autowired
    Jedis jedis;

    @Test
    public void test() {
        HashMap map = new HashMap();
        User user = new User();
        user.setUsername("桂敏瑞");
        map.put("user",user);

        userCache.put("123",user);
        long start = System.currentTimeMillis();
        System.out.println(start);
        Du.pl(userCache.get("123").get());
        System.out.println(System.currentTimeMillis()-start);
        long start1 = System.currentTimeMillis();
        System.out.println(start1);
        Du.pl(map.get("user"));
        System.out.println(System.currentTimeMillis()-start1);
        jedis.set("key1",user.toString());
        long start2 = System.currentTimeMillis();
        System.out.println(start2);
        Du.pl(jedis.get("key1"));
        System.out.println(System.currentTimeMillis()-start2);

/*
       System.out.println(persistenceService.getOneByColumn(DataOrder.class,"orderNumber","24423067612233391")==null);*/
    }
  /*  @Test
    public void dome(){
        System.out.println(persistenceService.count(DataOrder.class));
        }
    @Test
    public void utils(){
       System.out.println(persistenceService.get(DataOrder.class,"2943278005233010")==null);
    }*/

    @Test
    public void password() {
        AesCipherService aesCipherService = new AesCipherService();
        System.out.println(aesCipherService.generateNewKey());
    }

    @Test
    @Transactional
    public void payService() throws Exception {
        DetachedCriteria criteria = DetachedCriteria.forClass(DataOrder.class);
        criteria.add(Restrictions.sqlRestriction("DATE_FORMAT({alias}.create_date,'%Y%m%d')=?",
                DateUtils.format(new Date(), "yyyyMMdd"), new StringType()));
        persistenceService.getList(criteria).forEach(System.out::println);
    }

    public String te1() {
        System.out.println("t1");
        return "return";
    }

    public String te() {
        try {
            return te1();
        } catch (NullPointerException e) {
            System.out.println("catch");
        } finally {
            System.out.println("finally");
        }
        return null;
    }

    protected void println(Object object) {
        System.out.println(object);
    }
}
