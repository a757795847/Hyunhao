package com.zy.gcode.pojo;

/**
 * Created by admin5 on 17/2/23.
 */
public class ApplicationInfo {
    private String id;
    private String name;
    private Integer price;

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
