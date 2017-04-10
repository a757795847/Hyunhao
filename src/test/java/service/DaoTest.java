package service;

import com.csvreader.CsvWriter;
import com.zy.gcode.cache.ErrorOrderCache;
import com.zy.gcode.cache.MyCache;
import com.zy.gcode.cache.OperatorCache;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.service.AuthenticationService;
import com.zy.gcode.service.CodeService;
import com.zy.gcode.service.OrderService;
import com.zy.gcode.service.annocation.CsvPush;
import com.zy.gcode.service.intef.IApplicationService;
import com.zy.gcode.service.intef.IPayService;
import com.zy.gcode.utils.Du;
import org.apache.shiro.authc.credential.PasswordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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
    MyCache userCache;

    @Autowired
    Jedis jedis;

    @Autowired
    OperatorCache operatorCache;

    @Autowired
    OrderService orderService;

    @Autowired
    ErrorOrderCache errorOrderCache;

    @Autowired
    IApplicationService applicationService;
    /*  @Test
      public void dome(){
          System.out.println(persistenceService.count(DataOrder.class));
          }
      @Test
      public void utils(){
         System.out.println(persistenceService.get(DataOrder.class,"2943278005233010")==null);
      }*/
    String sql = " insert \n" +
            "    into\n" +
            "        jt_platform.data_order\n" +
            "        (id,weixin_id, mch_number, order_number, gift_money, gift_detail, gift_state, comment_file1, comment_file2, comment_file3, apply_date, approve_date, send_date, recieve_date, reject_reason, create_user_id, create_date, update_user_id, update_date, del_flag, buyer_name, buyer_zhifubao, dues, postage, pay_points, amount, rebate_point, actual_amount, actual_pay_points, order_state, buyer_notice, receiver, receiver_address, post_kind, receiver_tel, receiver_mobile, order_create_time, order_pay_time, goods_title, goods_kind, logistics_number, logistics_company, order_remark, goods_number, shop_id, shop_name, order_close_reason, solder_fee, buyer_fee, invoice_title, is_mobile_order, phase_order_info, privilege_order_id, is_transfer_agreement_photo, is_transfer_receipt, is_pay_by_another, earnest_ranking, sku_changed, receiver_address_changed, error_info, tmall_cards_deduction, point_dedution, is_o2o_trade) \n" +
            "    values\n" +
            "        (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    @Test
    public void test() {
        applicationService.closeApp(1611L);
    }

    @Test
    public void password() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("DSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        Signature signature = Signature.getInstance(generator.getAlgorithm());
        signature.initSign(keyPair.getPrivate());
        String str = "ab c";
        signature.update(str.getBytes());
        byte[] ab = signature.sign();
        signature.initVerify(keyPair.getPublic());
        signature.update((str + "1").getBytes());
        Du.dPl(signature.verify(ab));


    }

    @Test
    public void payService() throws Exception {
        Map<String, String> title2Value = getCsvMap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CsvWriter writer = new CsvWriter(outputStream, ',', Charset.forName("utf-8"));
        String[] params = new String[title2Value.keySet().size()];
        title2Value.keySet().toArray(params);
        writer.writeRecord(params);
        DataOrder dataOrder = new DataOrder();
        dataOrder.setId("112512");
        dataOrder.setReceiverAddress("2w35423");
        dataOrder.setOrderNumber("34562");
        BeanWrapper beanWrapper = new BeanWrapperImpl(dataOrder);
        String[] values = new String[title2Value.size()];
        int i = 0;
        Set<String> set = title2Value.keySet();
        for (String str : set) {
            values[i] = (String) beanWrapper.getPropertyValue(title2Value.get(str));
            i++;
        }
        writer.writeRecord(values);
        writer.flush();
        Du.pl(new String(outputStream.toByteArray()));
    }

    private Map<String, String> getCsvMap() {
        Field[] fields = DataOrder.class.getDeclaredFields();
        HashMap map = new LinkedHashMap(64);
        int len = fields.length;
        for (int i = 0; i < len; i++) {
            CsvPush csvPush = fields[i].getAnnotation(CsvPush.class);
            if (csvPush != null) {
                map.put(csvPush.value(), fields[i].getName());
            }
        }
        return map;
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
