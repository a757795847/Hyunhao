package com.zy.gcode.service;

import com.csvreader.CsvReader;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.pojo.WxOperator;
import com.zy.gcode.service.annocation.CsvPush;
import com.zy.gcode.utils.*;
import org.apache.shiro.SecurityUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by admin5 on 17/2/15.
 */
@Component
public class OrderService implements IOrderService {

    Logger log = LoggerFactory.getLogger(OrderService.class);

    int HANDLE_COUNT = 256;
    int HANDLE_MOST_COUNT = 200;

    @Autowired
    PersistenceService persistenceService;


    @Override
    public List<DataOrder> getOrderByStatus(int status, Page page,String userId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(DataOrder.class);
        criteria.add(Restrictions.eq("giftState", status)).add(Restrictions.eq("createUserId",userId));
        return persistenceService.getListAndSetCount(DataOrder.class, criteria, page);
    }

    @Override
    public CodeRe handleCsv(MultipartFile multipartFile, String operatorName) {
        WxOperator operator = getWxOperator();
        if(operator == null){
            return CodeRe.error("登录过期!");
        }

        Date date = new Date();
        File file = new File(MzUtils.merge(Constants.RED_CSV_PATH, "/",DateUtils.format(date,"yyyyMM"),"/", operatorName, ":",
                DateUtils.format(date, "yyyy-MM-dd hh:mm:ss")));
        CsvReader csvReader;
        List<String[]> csvValueList = new ArrayList<>(HANDLE_COUNT);
        try {
            multipartFile.transferTo(file);
            csvReader = new CsvReader(file.getAbsolutePath(), ',', Charset.forName("GBK"));
            int count =1;
            while (csvReader.readRecord()) {
                if(count>HANDLE_MOST_COUNT){
                    break;
                }
                csvValueList.add(csvReader.getValues());
                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return CodeRe.error("csv文件处理错误");
        }
        Map<String, String> title2Value = getCsvMap();
        String[] titles = MzUtils.trimArray(csvValueList.get(0));
        List<String> orderNoList = new ArrayList(HANDLE_COUNT);
        List<DataOrder> dataOrderList = new ArrayList<>(HANDLE_COUNT);
        for (int j = 1; j < csvValueList.size(); j++) {
            DataOrder dataOrder = new DataOrder();
            BeanWrapper beanWrapper = new BeanWrapperImpl(dataOrder);
            String[] values = csvValueList.get(j);
            for (int i = 0; i < titles.length; i++) {
                if (titles[i].equals("订单编号")) {
                    String str = values[i];
                    values[i] = str.substring(2,str.length()-1);
                    orderNoList.add(values[i]);
                }
                beanWrapper.setPropertyValue(title2Value.get(titles[i]), values[i]);
            }
            dataOrderList.add((DataOrder) beanWrapper.getWrappedInstance());
        }

        List<DataOrder> existDataOrderList = persistenceService.getListByIn(DataOrder.class,"orderNumber",orderNoList.toArray());

        List<Map> resultList = new ArrayList<>(HANDLE_COUNT);
        dataOrderList.forEach(dataOrder -> {
            Map resultMap = new HashMap(3);
            resultMap.put("order", dataOrder);
            DataOrder containOrder = getContainsOrder(existDataOrderList,dataOrder);
            if (containOrder!=null) {
                if(!containOrder.getCreateUserId().equals(operator.getUsername())){
                    resultMap.put("state","-1");
                }else {
                    resultMap.put("state", "0");
                }
            } else {
                resultMap.put("state", "1");
            }
            resultList.add(resultMap);
        });

        return CodeRe.correct(resultList);
    }
    private DataOrder getContainsOrder(List<DataOrder> list,DataOrder dataOrder){
        for(DataOrder order:list){
            if(order.getOrderNumber().equals(dataOrder.getOrderNumber())){
                return order;
            }
        }
        return null;
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

    @Override
    public CodeRe saveOrderList(List<DataOrder> orderList,String userId) {
        int len = orderList.size();
        String[] dataNos = new String[len];
        for(int i = 0 ; i < len;i++){
            dataNos[i]=orderList.get(i).getOrderNumber();
        }
        List<DataOrder> dataOrderList = persistenceService.getListByIn(DataOrder.class,"orderNumber", dataNos);
       for(DataOrder dataOrder:orderList){
           DataOrder containOrder = getContainsOrder(dataOrderList,dataOrder);
           if(containOrder!=null&&containOrder.getCreateUserId().equals(userId)){
               continue;
           }
           dataOrder.setCreateUserId(userId);
           persistenceService.save(dataOrder);
       }
        return CodeRe.correct("success");
    }

    @Override
    public CodeRe passAuditing(String uuid) {

      DataOrder dataOrder =   persistenceService.get(DataOrder.class,uuid);
      if(dataOrder.getGiftState()!=1){
        return   CodeRe.error("该红包未申领");
      }
      WxOperator operator = getWxOperator();
      if(operator == null){
          return CodeRe.error("登录超时!");
      }
      if(!operator.getUsername().equals(dataOrder.getCreateUserId())){
          return CodeRe.error("您无此权限更改用户状态");
      }

      dataOrder.setGiftState(2);
      dataOrder.setApproveDate(new Timestamp(System.currentTimeMillis()));
      persistenceService.updateOrSave(dataOrder);

        return CodeRe.correct("处理成功");
    }

    @Override
    public CodeRe redSend(String orderno, int count) {
        WxOperator operator = (WxOperator) SecurityUtils.getSubject().getSession().getAttribute("operator");
        if(operator ==null){
            CodeRe.error("操作超时,请重新登录!");
        }
       String wxappid =  operator.getWxAppid();
       DataOrder order =  persistenceService.get(DataOrder.class,orderno);
       if(!operator.getUsername().equals(order.getCreateUserId()))
       return CodeRe.error("您无权操作次订单");

       int giftState;
       try {
         giftState = order.getGiftState();
       }catch (NullPointerException e){
           return CodeRe.error("订单不存在!");
       }
       if(giftState!=2){
           return CodeRe.error("订单未处于审核通过状态!");
       }

       if(count < 100){
           return  CodeRe.error("红包金额必须大于100");
       }
        /*String openid,String count,String wxAppid*/

      Map map =  HttpClientUtils.mapGetSend("http://open.izhuiyou.com/pay/send","openid",order.getWeixinId(),
                "count",String.valueOf(count),"wxAppid",wxappid,"sign","13468794sagag");
      if(map ==null){
          log.error("http请求错误");
          return CodeRe.error("红包发送失败");
      }

      if(map.get("status").equals("0")){
          CodeRe.error((String)map.get("message"));
      }
      order.setSendDate(new Timestamp(System.currentTimeMillis()));
      persistenceService.updateOrSave(order);

        return CodeRe.correct("success");
    }

    private WxOperator getWxOperator(){
       return (WxOperator) SecurityUtils.getSubject().getPrincipal();
    }
}
