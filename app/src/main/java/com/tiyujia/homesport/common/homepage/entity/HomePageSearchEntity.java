package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;

/**
 * Created by zzqybyb19860112 on 2016/11/15.1
 */

/**
 * 岩场搜索实体类
 */
public class HomePageSearchEntity implements Serializable {
    private int number;
    private String searchText;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
