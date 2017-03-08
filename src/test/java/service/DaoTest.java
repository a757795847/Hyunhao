package service;

import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.service.AuthenticationService;
import com.zy.gcode.service.CodeService;
import com.zy.gcode.service.IPayService;
import com.zy.gcode.utils.DateUtils;
import org.apache.shiro.authc.credential.PasswordService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
    public void test() {
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
        int n = 10;
        int i = 1;

        System.out.println(n*i++);
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
    protected void println(Object object){
        System.out.println(object);
    }
}
