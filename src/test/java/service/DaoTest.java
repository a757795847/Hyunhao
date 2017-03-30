package service;

import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.zy.gcode.cache.MyCache;
import com.zy.gcode.cache.OperatorCache;
import com.zy.gcode.cache.SerializeUtils;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.AuthenticationService;
import com.zy.gcode.service.CodeService;
import com.zy.gcode.service.OrderService;
import com.zy.gcode.service.intef.IPayService;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.Du;
import com.zy.gcode.utils.Timing;
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

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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



    @Test
    public void test() {
        HashMap map = new HashMap();
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
    String sql = " insert \n" +
          "    into\n" +
          "        jt_platform.data_order\n" +
          "        (id,weixin_id, mch_number, order_number, gift_money, gift_detail, gift_state, comment_file1, comment_file2, comment_file3, apply_date, approve_date, send_date, recieve_date, reject_reason, create_user_id, create_date, update_user_id, update_date, del_flag, buyer_name, buyer_zhifubao, dues, postage, pay_points, amount, rebate_point, actual_amount, actual_pay_points, order_state, buyer_notice, receiver, receiver_address, post_kind, receiver_tel, receiver_mobile, order_create_time, order_pay_time, goods_title, goods_kind, logistics_number, logistics_company, order_remark, goods_number, shop_id, shop_name, order_close_reason, solder_fee, buyer_fee, invoice_title, is_mobile_order, phase_order_info, privilege_order_id, is_transfer_agreement_photo, is_transfer_receipt, is_pay_by_another, earnest_ranking, sku_changed, receiver_address_changed, error_info, tmall_cards_deduction, point_dedution, is_o2o_trade) \n" +
          "    values\n" +
          "        (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    @Test
    public void password()  throws Exception{
        List<DataOrder> list = new ArrayList();
        List list1 = new ArrayList();
        for(int i = 0 ; i <10000;i++){
            DataOrder order = new DataOrder();
            order.setOrderNumber("888"+i);
            list.add(order);
            list1.add(order);
        }

        System.out.println(SerializeUtils.en(list).length/1024);
/*        ThreadPoolExecutor poolExecutor =new ThreadPoolExecutor(20,20,100, TimeUnit.MINUTES,new ArrayBlockingQueue<Runnable>(10));
        for(int i = 0 ; i < 20;i++){
            List<DataOrder> list2 = list.subList(i*500,i*500+500);

            poolExecutor.execute(()->{
                Timing timing = new Timing();
                timing.start();
                timing.end();
            });
        }*/

/*
        List<DataOrder> list = new ArrayList();
        List list1 = new ArrayList();
        for(int i = 0 ; i <10000;i++){
            DataOrder order = new DataOrder();
            order.setOrderNumber("888"+i);
            list.add(order);
            list1.add(order);
        }


            Timing timing1 = new Timing();
            timing1.start();
            System.out.println("2222");
            Du.pl(Arrays.toString(persistenceService.insertBatch(list,DataOrder.class,sql)));
            timing1.end();
*/

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

    public static void main(String[] args){
       // BitMatrix bitMatrix = new MultiFormatWriter().encode()
    }

}
