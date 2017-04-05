package service;

import com.csvreader.CsvWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.zy.gcode.cache.ErrorOrderCache;
import com.zy.gcode.cache.MyCache;
import com.zy.gcode.cache.OperatorCache;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.pojo.UserConfig;
import com.zy.gcode.service.AuthenticationService;
import com.zy.gcode.service.CodeService;
import com.zy.gcode.service.OrderService;
import com.zy.gcode.service.annocation.CsvPush;
import com.zy.gcode.service.intef.IPayService;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.Du;
import com.zy.gcode.utils.SubjectUtils;
import org.apache.shiro.authc.credential.PasswordService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.crypto.KeyGenerator;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.util.*;

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



    @Test
    public void test() {

        UserConfig userConfig = new UserConfig();
        userConfig.setUserId("2");
        userConfig.setAppOpenTime(DateUtils.tNow());
        userConfig.setDeleteFlag("1");
        persistenceService.save(userConfig);
     /*   HashMap map = new HashMap();
        User user = new User();

        userCache.put("123",user);
        long start = System.currentTimeMillis();
        System.out.println(start);
        Du.pl(userCache.get("123").get());
        System.out.println(System.currentTimeMillis()-start);
        long start1 = System.currentTimeMillis();
        System.out.println(start1);
        Du.pl(map.get("user"));
        System.out.println(System.currentTimeMillis()-start1);

        long start3 = System.currentTimeMillis();
        Du.pl(operatorCache.get("key2",User.class));
        System.out.println(System.currentTimeMillis()-start3);

        long start2 = System.currentTimeMillis();
        Du.pl(jedis.get("key1"));
        System.out.println(System.currentTimeMillis()-start2);*/

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
    String sql = " insert \n" +
          "    into\n" +
          "        jt_platform.data_order\n" +
          "        (id,weixin_id, mch_number, order_number, gift_money, gift_detail, gift_state, comment_file1, comment_file2, comment_file3, apply_date, approve_date, send_date, recieve_date, reject_reason, create_user_id, create_date, update_user_id, update_date, del_flag, buyer_name, buyer_zhifubao, dues, postage, pay_points, amount, rebate_point, actual_amount, actual_pay_points, order_state, buyer_notice, receiver, receiver_address, post_kind, receiver_tel, receiver_mobile, order_create_time, order_pay_time, goods_title, goods_kind, logistics_number, logistics_company, order_remark, goods_number, shop_id, shop_name, order_close_reason, solder_fee, buyer_fee, invoice_title, is_mobile_order, phase_order_info, privilege_order_id, is_transfer_agreement_photo, is_transfer_receipt, is_pay_by_another, earnest_ranking, sku_changed, receiver_address_changed, error_info, tmall_cards_deduction, point_dedution, is_o2o_trade) \n" +
          "    values\n" +
          "        (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Test
    public void password()  throws Exception{
        KeyPairGenerator generator = KeyPairGenerator.getInstance("DSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        Signature signature = Signature.getInstance(generator.getAlgorithm());
        signature.initSign(keyPair.getPrivate());
        String str = "ab c";
        signature.update(str.getBytes());
       byte[] ab =  signature.sign();
       signature.initVerify(keyPair.getPublic());
       signature.update((str+"1").getBytes());
        Du.dPl(signature.verify(ab));


    }

    @Test
    public void payService() throws Exception {

    }
    private Map<String, String> getCsvMap() {
        Field[] fields = DataOrder.class.getDeclaredFields();
        HashMap map = new HashMap(64);
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

    public static void main(String[] args) throws Exception{
        BitMatrix bitMatrix = new MultiFormatWriter().encode("不会的", BarcodeFormat.QR_CODE,200,200);
      //  Path path = FileSystems.getDefault().getPath("~/Downloads","QRtest");
        File file = new File("/Users/admin5/Downloads/QRtest");
        if(!file.exists()){
            file.createNewFile();
        }
        MatrixToImageWriter.writeToPath(bitMatrix,"png",file.toPath());
    }

}
