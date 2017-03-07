package service;

import com.zy.gcode.service.OrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by admin5 on 17/3/7.
 */
public class OrderServiceTest extends DaoTest {

    @Autowired
    OrderService orderService;

    @Test
    public void redinfoTest(){
      println(orderService.redInfo("1426499802201703066574510102"));
    }

}
