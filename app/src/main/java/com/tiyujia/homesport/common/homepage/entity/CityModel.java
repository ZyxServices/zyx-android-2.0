package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/11/29.
 */

public class CityModel implements Serializable {
    public int state;
    public List<CityList> data;
    public class CityList{
        public int id;
        public String cityName;
        public long createTime;
    }
}
