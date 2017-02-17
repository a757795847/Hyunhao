package com.zy.gcode.service;

import com.csvreader.CsvReader;
import com.zy.gcode.controller.delegate.CodeRe;
import com.zy.gcode.dao.PersistenceService;
import com.zy.gcode.pojo.DataOrder;
import com.zy.gcode.service.annocation.CsvPush;
import com.zy.gcode.utils.Constants;
import com.zy.gcode.utils.DateUtils;
import com.zy.gcode.utils.MzUtils;
import com.zy.gcode.utils.Page;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by admin5 on 17/2/15.
 */
@Component
public class OrderService implements IOrderService {
    @Autowired
    PersistenceService persistenceService;


    @Override
    public List<DataOrder> getOrderByStatus(int status, Page page) {
        DetachedCriteria criteria = DetachedCriteria.forClass(DataOrder.class);
        criteria.add(Restrictions.eq("giftState", status));
        return persistenceService.getListAndSetCount(DataOrder.class, criteria, page);
    }

    @Override
    public CodeRe handleCsv(MultipartFile multipartFile, String operatorName) {
        File file = new File(MzUtils.merge(Constants.RED_CSV_PATH, "/", operatorName, ":",
                DateUtils.format(new Date(), "yyyy-MM-dd")));
        CsvReader csvReader;
        List<String[]> csvValueList = new ArrayList<>(512);
        try {
            multipartFile.transferTo(file);
            csvReader = new CsvReader(file.getAbsolutePath(), ',', Charset.forName("GBK"));
            while (csvReader.readRecord()) {
                csvValueList.add(csvReader.getValues());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return CodeRe.error("csv文件处理错误");
        }
        Map<String, String> title2Value = getCsvMap();
        String[] titles = MzUtils.trimArray(csvValueList.get(0));
        List<String> orderNoList = new ArrayList(512);
        List<DataOrder> dataOrderList = new ArrayList<>(512);
        for (int j = 1; j < csvValueList.size(); j++) {
            DataOrder dataOrder = new DataOrder();
            BeanWrapper beanWrapper = new BeanWrapperImpl(dataOrder);
            String[] values = csvValueList.get(j);
            for (int i = 0; i < titles.length; i++) {
                if(titles[i].equals("订单编号")){
                    orderNoList.add(values[i]);
                }
                System.out.println(titles[i]+i);
                beanWrapper.setPropertyValue(title2Value.get(titles[i]), values[i]);
              dataOrderList.add((DataOrder)beanWrapper.getWrappedInstance());
            }
        }
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DataOrder.class);
        detachedCriteria.add(Restrictions.in("orderNumber", orderNoList.toArray()));
        List<DataOrder> existDataOrderList = persistenceService.getList(detachedCriteria);

        List<Map> resultList = new ArrayList<>(512);
        dataOrderList.forEach(dataOrder ->{
            Map resultMap = new HashMap(3);
            resultMap.put("order",dataOrder);
            if(existDataOrderList.contains(dataOrder)){
                resultMap.put("state","订单已存在");
            }else {
                resultMap.put("state","success");
                persistenceService.save(dataOrder);
            }
            resultList.add(resultMap);
        } );

        return CodeRe.correct(resultList);
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
}
