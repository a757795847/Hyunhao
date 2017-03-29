package com.zy.gcode.service;

import com.csvreader.CsvReader;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.pojo.DataStrategy;
import com.zy.gcode.pojo.RedStatus;
import com.zy.gcode.pojo.User;
import com.zy.gcode.service.annocation.CsvPush;
import com.zy.gcode.service.intef.IMultipartService;
import com.zy.gcode.service.intef.IOrderService;
import com.zy.gcode.utils.*;
import org.apache.shiro.SecurityUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by admin5 on 17/2/15.
 */
@Component
public class OrderService implements IOrderService {
    Logger log = LoggerFactory.getLogger(OrderService.class);



    String sql = " insert \n" +
            "    into\n" +
            "        jt_platform.data_order\n" +
            "        (id,weixin_id, mch_number, order_number, gift_money, gift_detail, gift_state, comment_file1, comment_file2, comment_file3, apply_date, approve_date, send_date, recieve_date, reject_reason, create_user_id, create_date, update_user_id, update_date, del_flag, buyer_name, buyer_zhifubao, dues, postage, pay_points, amount, rebate_point, actual_amount, actual_pay_points, order_state, buyer_notice, receiver, receiver_address, post_kind, receiver_tel, receiver_mobile, order_create_time, order_pay_time, goods_title, goods_kind, logistics_number, logistics_company, order_remark, goods_number, shop_id, shop_name, order_close_reason, solder_fee, buyer_fee, invoice_title, is_mobile_order, phase_order_info, privilege_order_id, is_transfer_agreement_photo, is_transfer_receipt, is_pay_by_another, earnest_ranking, sku_changed, receiver_address_changed, error_info, tmall_cards_deduction, point_dedution, is_o2o_trade) \n" +
            "    values\n" +
            "        (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    int HANDLE_COUNT = 10000;
    int HANDLE_MOST_COUNT = 10000;

    @Autowired
    PersistenceService persistenceService;

    @Autowired
    IMultipartService multipartService;

    @Override
    @Transactional
    public List<DataOrder> getOrderByCondition(List<Integer> status, Page page, String userId, Timestamp applyTime, Timestamp importTime) {
        DetachedCriteria criteria = DetachedCriteria.forClass(DataOrder.class);
        criteria.add(Restrictions.eq("createUserId", getWxOperator()));
        criteria.add(Restrictions.in("giftState", status)).add(Restrictions.eq("createUserId", userId));
        if (importTime != null) {
            criteria.add(Restrictions.sqlRestriction("DATE_FORMAT({alias}.create_date,'%Y%m%d')=?",
                    DateUtils.format(importTime, "yyyyMMdd"), new StringType()));
        }

        if (applyTime != null) {
            criteria.add(Restrictions.sqlRestriction("DATE_FORMAT({alias}.apply_date,'%Y%m%d')=?",
                    DateUtils.format(applyTime, "yyyyMMdd"), new StringType()));
        }

        if (applyTime != null) {
            criteria.addOrder(Order.desc("applyDate"));
        } else if (importTime != null) {
            criteria.addOrder(Order.desc("createDate"));
        }

        return persistenceService.getListAndSetCount(DataOrder.class, criteria, page);
    }

    @Override
    @Transactional
    public CodeRe handleCsv(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return CodeRe.error("上传文件不能为空");
        }
        Timing timing =new Timing();
        Date date = new Date();
        File file = new File(MzUtils.merge(Constants.RED_CSV_PATH, "/", DateUtils.format(date, "yyyyMM"), "/", getWxOperator(), ":",
                DateUtils.format(date, "yyyyMMddhhmmss")));
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        CsvReader csvReader;
        List<String[]> csvValueList = new ArrayList<>(HANDLE_COUNT);
        try {
            multipartFile.transferTo(file);
            csvReader = new CsvReader(file.getAbsolutePath(), ',', Charset.forName("GBK"));
            int count = 1;
            while (csvReader.readRecord()) {
                if (count > HANDLE_MOST_COUNT) { //判断是否大于每次csv解析条数上线
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
        String userId = getWxOperator();
        for (int j = 1; j < csvValueList.size(); j++) {
            DataOrder dataOrder = new DataOrder();
            BeanWrapper beanWrapper = new BeanWrapperImpl(dataOrder);//使用spring 包装bean设置csv读入属性到pojo
            String[] values = csvValueList.get(j);
            log.debug("解析的订单:" + Arrays.toString(values));
            for (int i = 0; i < titles.length; i++) {
                if (titles[i].equals("订单编号")) {
                    String str = values[i];
                    //因为csv订单号格式有问题，所以进行特别处理
                    values[i] = str.substring(2, str.length() - 1);
                    orderNoList.add(values[i]);
                }
                beanWrapper.setPropertyValue(title2Value.get(titles[i]), values[i]);
            }
           DataOrder dataOrder1 = (DataOrder) beanWrapper.getWrappedInstance();
            dataOrder1.setCreateUserId(userId);
            dataOrderList.add(dataOrder1);
        }
        timing.start();
        List<DataOrder> existDataOrderList = persistenceService.getListByIn(DataOrder.class, "orderNumber", orderNoList.toArray());
        dataOrderList.removeAll(existDataOrderList);
        timing.end();
        timing.start();
        persistenceService.insertBatch(dataOrderList,DataOrder.class,sql);
        timing.end();
        System.out.println("插入的数量:"+dataOrderList.size());
        System.out.println("库存数量:"+persistenceService.count(DataOrder.class));

        return CodeRe.correct("ok");
    }

    /**
     * 如果集合中存在订单号与传入参数相等的订单，则返回单号相等的订单，否则返回null
     *
     * @param list
     * @param dataOrder
     * @return
     */
    private DataOrder getContainsOrder(List<DataOrder> list, DataOrder dataOrder) {
        for (DataOrder order : list) {
            if (order.getOrderNumber().equals(dataOrder.getOrderNumber())) {
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
    @Transactional
    public CodeRe saveOrderList(List<DataOrder> orderList, String userId) {
        int len = orderList.size();
        String[] dataNos = new String[len];
        for (int i = 0; i < len; i++) {
            dataNos[i] = orderList.get(i).getOrderNumber();
        }
        List<DataOrder> dataOrderList = persistenceService.getListByIn(DataOrder.class, "orderNumber", dataNos);
        List<String> inconsequenceNos = new ArrayList<>();
        for (DataOrder dataOrder : orderList) {
            DataOrder containOrder = getContainsOrder(dataOrderList, dataOrder);
            if (containOrder != null) {
                inconsequenceNos.add(containOrder.getId());
                continue;
            }
            dataOrder.setCreateUserId(userId);
            dataOrder.setCreateDate(new Timestamp(System.currentTimeMillis()));
            persistenceService.save(dataOrder);
        }
        return CodeRe.correct(inconsequenceNos);
    }

    @Override
    @Transactional
    public CodeRe passAuditing(String uuid) {

        DataOrder dataOrder = persistenceService.get(DataOrder.class, uuid);
        if (dataOrder.getGiftState() != 1) {
            log.debug("红包状态非等于1");
            return CodeRe.error("该红包未申领");
        }
        if (!getWxOperator().equals(dataOrder.getCreateUserId())) {
            log.debug("订单号的用户名与当前登录者用户名不符和");
            return CodeRe.error("您无此权限更改用户状态");
        }

        dataOrder.setGiftState(2);
        dataOrder.setApproveDate(new Timestamp(System.currentTimeMillis()));
        persistenceService.updateOrSave(dataOrder);

        return CodeRe.correct("处理成功");
    }

    @Override
    @Transactional
    public CodeRe redSend(String orderno, long strategyid) {
        User operator = (User) SecurityUtils.getSubject().getSession().getAttribute("operator");
        if (operator == null) {
            CodeRe.error("操作超时,请重新登录!");
        }
        CodeRe<String> muCodeRe = multipartService.getTappidByApp(operator.getUsername(), Constants.ZYAPPID);
        if (muCodeRe.isError()) {
            return muCodeRe;
        }

        String tappid = muCodeRe.getMessage();
        DataOrder order = persistenceService.get(DataOrder.class, orderno);
        if (!operator.getUsername().equals(order.getCreateUserId()))
            return CodeRe.error("您无权操作次订单");

        int giftState;
        try {
            giftState = order.getGiftState();
        } catch (NullPointerException e) {
            return CodeRe.error("订单不存在!");
        }
        if (giftState != 2) {
            return CodeRe.error("订单未处于审核通过状态!");
        }
        DataStrategy strategy = persistenceService.get(DataStrategy.class, strategyid);
        /*String openid,String count,String wxAppid*/
        if (strategy == null) {
            return CodeRe.error("红包策略不存在");
        }
        Map map = HttpClientUtils.mapGetSend("http://open.izhuiyou.com/pay/send", "openid", order.getWeixinId(),
                "count", String.valueOf(strategy.getMoney()), "tappid", tappid, "sign", "13468794sagag");
        if (map == null) {
            log.error("http请求错误");
            return CodeRe.error("红包发送失败");
        }

        if (map.get("status").equals("0")) {
            return CodeRe.error((String) map.get("message"));
        }
        order.setGiftState(3);
        order.setMchNumber(map.get("message").toString());
        order.setSendDate(new Timestamp(System.currentTimeMillis()));
        persistenceService.updateOrSave(order);
        return CodeRe.correct("success");
    }

    @Override
    @Transactional
    public CodeRe redInfo(String mchNumber) {
        RedStatus redStatus = persistenceService.get(RedStatus.class, mchNumber);
        CodeRe<String> muCodeRe = multipartService.getTappidByApp("2", Constants.ZYAPPID);
        if (muCodeRe.isError()) {
            return muCodeRe;
        }

        String tappid = muCodeRe.getMessage();
        if (redStatus == null) {
            String str = HttpClientUtils.stringGetSend("http://open.izhuiyou.com/pay/redinfo", "billno", mchNumber, "zyid", tappid);
            Map map = JsonUtils.asObj(Map.class, str);
            if (map == null) {
                return CodeRe.error("请求异常");
            }

            if (map.get("status").equals("0")) {
                log.error("获取红包信息错误:" + map.get("message"));
                return CodeRe.error("请求异常");
            }
            return CodeRe.correct(map.get("redInfo"));
        }
        return CodeRe.correct(redStatus);
    }

    @Override
    @Transactional
    public CodeRe getOrderByNumber(String orderNo) {
        DataOrder order = persistenceService.getOneByColumn(DataOrder.class, "orderNumber", orderNo.trim(), "createUserId", getWxOperator());
        if (order == null) {
            return CodeRe.error("订单不存在!");
        }
        return CodeRe.correct(order);
    }

    private String getWxOperator() {
        return (String) SecurityUtils.getSubject().getPrincipal();
    }

}
