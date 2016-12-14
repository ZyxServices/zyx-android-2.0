package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/13 09:37.
 * 邮箱:928902646@qq.com
 */

public class CityMapModel implements Serializable {
    public int state;
    public List<City> data;
    public class City implements Serializable{
        public int id;
        public int type;
        public String name;
        public String city;
        public String address;
        public String imgUrls;
        public String level;
        public double longitude;//精度
        public double latitude;//纬度

    }


}
