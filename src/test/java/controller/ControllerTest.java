package controller;

import com.zy.gcode.service.OrderService;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Created by admin5 on 17/1/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:applicationContext.xml","classpath:dispatcher-servlet.xml"})
public class ControllerTest {

    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void geAppid() throws Exception {
       ParameterizedType clazz =  (ParameterizedType) OrderService.class.getDeclaredMethod("saveOrderList", List.class,String.class).getParameters()[0].getParameterizedType();
        System.out.println(clazz.getActualTypeArguments()[0]);
    }

    protected void jsonPost(String url,Object... params) throws Exception{
       this.mockMvc.perform(post(url).content(buildBody(params)).contentType(MediaType.APPLICATION_JSON))
       .andDo(MockMvcResultHandlers.print());
    }

    private String buildBody(Object[] body) throws Exception{
        Map map = new HashMap();
        for(int i = 0 ; i < body.length;i+=2){
            map.put(body[i],body[i+1]);
        }
        return JsonUtils.objAsString(map);
    }

   @Test
    public void file() throws Exception{
       File file = new File("/Users/admin5/play/ExportOrderList201612281719.csv");
       System.out.println(file.exists());
       FileInputStream inputStream = new FileInputStream(file);
       MockMultipartFile multipartFile = new MockMultipartFile("file",inputStream);
        this.mockMvc.perform(fileUpload("/order/parseCsv").file(multipartFile).
                accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void importCsv() throws Exception{
/*       this.mockMvc.perform(post("/order/importCsv").content("[{\"id\":null,\"weixinId\":null,\"orderNumber\":\"2943843414580221\",\"giftMoney\":null,\"giftDetail\":null,\"giftState\":0,\"commentFile1\":null,\"commentFile2\":null,\"commentFile3\":null,\"applyDate\":null,\"approveDate\":null,\"sendDate\":null,\"recieveDate\":null,\"rejectReason\":null,\"createUserId\":null,\"createDate\":null,\"updateUserId\":null,\"updateDate\":null,\"delFlag\":null,\"buyerName\":\"y025s188527043\",\"buyerZhifubao\":\"18852704390\",\"dues\":\"10.50\",\"postage\":\"0.00\",\"payPoints\":\"0\",\"amount\":\"10.50\",\"rebatePoint\":\"0\",\"actualAmount\":\"10.50\",\"actualPayPoints\":\"0\",\"orderState\":\"买家已付款，等待卖家发货\",\"buyerNotice\":\"\",\"receiver\":\"杨石磊\",\"receiverAddress\":\"江苏省 扬州市 邗江区 汊河街道华扬西路196号(225100)\",\"postKind\":\"快递\",\"receiverTel\":\"\",\"receiverMobile\":\"'18852704390\",\"orderCreateTime\":\"2016-12-28 17:18:14\",\"orderPayTime\":\"2016-12-28 17:18:31\",\"goodsTitle\":\"痔疮膏痔疮栓\",\"goodsKind\":\"1\",\"logisticsNumber\":\"\",\"logisticsCompany\":\"\",\"orderRemark\":\"\",\"goodsNumber\":\"1\",\"shopId\":\"0\",\"shopName\":\"刘氏刘大夫草药\",\"orderCloseReason\":\"订单未关闭\",\"solderFee\":\"0\",\"buyerFee\":\"0元\",\"invoiceTitle\":\"\",\"isMobileOrder\":\"手机订单\",\"phaseOrderInfo\":\"\",\"privilegeOrderId\":\"=\\\"\\\"\",\"isTransferAgreementPhoto\":\"否\",\"isTransferReceipt\":\"否\",\"isPayByAnother\":\"否\",\"earnestRanking\":\"\",\"skuChanged\":\"\",\"receiverAddressChanged\":\"\",\"errorInfo\":\"\",\"tmallCardsDeduction\":\"\",\"pointDedution\":\"\",\"isO2OTrade\":\"\"},{\"id\":null,\"weixinId\":null,\"orderNumber\":\"2943278005233010\",\"giftMoney\":null,\"giftDetail\":null,\"giftState\":0,\"commentFile1\":null,\"commentFile2\":null,\"commentFile3\":null,\"applyDate\":null,\"approveDate\":null,\"sendDate\":null,\"recieveDate\":null,\"rejectReason\":null,\"createUserId\":null,\"createDate\":null,\"updateUserId\":null,\"updateDate\":null,\"delFlag\":null,\"buyerName\":\"诚信为主17\",\"buyerZhifubao\":\"18630297849\",\"dues\":\"10.50\",\"postage\":\"0.00\",\"payPoints\":\"0\",\"amount\":\"10.50\",\"rebatePoint\":\"0\",\"actualAmount\":\"10.50\",\"actualPayPoints\":\"0\",\"orderState\":\"买家已付款，等待卖家发货\",\"buyerNotice\":\"\",\"receiver\":\"张卫\",\"receiverAddress\":\"河北省 保定市 徐水县 东史端乡西史端村(072550)\",\"postKind\":\"快递\",\"receiverTel\":\"\",\"receiverMobile\":\"'18630297849\",\"orderCreateTime\":\"2016-12-28 17:16:27\",\"orderPayTime\":\"2016-12-28 17:16:31\",\"goodsTitle\":\"痔疮膏痔疮栓\",\"goodsKind\":\"1\",\"logisticsNumber\":\"\",\"logisticsCompany\":\"\",\"orderRemark\":\"\",\"goodsNumber\":\"1\",\"shopId\":\"0\",\"shopName\":\"刘氏刘大夫草药\",\"orderCloseReason\":\"订单未关闭\",\"solderFee\":\"0\",\"buyerFee\":\"0元\",\"invoiceTitle\":\"\",\"isMobileOrder\":\"手机订单\",\"phaseOrderInfo\":\"\",\"privilegeOrderId\":\"=\\\"\\\"\",\"isTransferAgreementPhoto\":\"否\",\"isTransferReceipt\":\"否\",\"isPayByAnother\":\"否\",\"earnestRanking\":\"\",\"skuChanged\":\"\",\"receiverAddressChanged\":\"\",\"errorInfo\":\"\",\"tmallCardsDeduction\":\"\",\"pointDedution\":\"\",\"isO2OTrade\":\"\"}]")
       .contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print());*/
    }


}
