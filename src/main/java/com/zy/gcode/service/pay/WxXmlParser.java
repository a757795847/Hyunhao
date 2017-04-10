package com.zy.gcode.service.pay;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin5 on 17/1/20.
 */
public class WxXmlParser {
    public static String getWxXml(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("xml");
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            try {
                Object value = field.get(obj);
                if (value == null || value.toString().equals("")) {
                    continue;
                }
                root.addElement(name).addCDATA(value.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return document.asXML();
    }

    public static String map2xml(Map map) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("xml");
        Set<String> keys = map.keySet();
        keys.forEach(k -> {
            root.addElement(k).addCDATA(String.valueOf(map.get(k)));
        });
        return document.asXML();
    }


    public static Map getWxMap(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Map<String, String> map = new HashMap();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            try {
                Object value = field.get(obj);
                if (value == null || value.toString().equals("")) {
                    continue;
                }
                map.put(name, value.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static String elementString(String data, String name) {
        Document document;
        try {
            document = DocumentHelper.parseText(data);
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
        Element root = document.getRootElement();
        return root.elementText(name);

    }

    public static Map Xml2Map(String data) {
        Document document;
        try {
            document = DocumentHelper.parseText(data);
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
        Element root = document.getRootElement();
        List<Element> childs = root.elements();
        Map<String, String> map = new HashMap<>();
        for (Element child : childs) {
            map.put(child.getName(), child.getText());
        }
        return map;
    }

}
