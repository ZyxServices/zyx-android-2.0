package com.tiyujia.homesport.common.homepage.entity;

/**
 * Created by zzqybyb19860112 on 2016/11/18.1
 */

public class CityBean {
    private String name;
    private String firstAlpha;
    public String getCityName() {
         return name;
    }
    public void setCityName(String cityName) {
        name=cityName;
    }
    public String getNameSort() {
         return firstAlpha;
    }
    public void setNameSort(String nameSort) {
        firstAlpha=nameSort;
    }
}
