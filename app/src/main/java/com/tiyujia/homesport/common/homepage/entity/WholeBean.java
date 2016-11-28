package com.tiyujia.homesport.common.homepage.entity;

/**
 * Created by zzqybyb19860112 on 2016/11/23.1
 */

import java.io.Serializable;

/**
 * 项目中全局搜索的实体类
 */
public class WholeBean implements Serializable{
    private int orderNumber;
    private String text;

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
