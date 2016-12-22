package com.tiyujia.homesport.common.record.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/22 11:13.
 * 邮箱:928902646@qq.com
 */

public class CityMapHistroyModel implements Serializable{
    public int state;
    public List<History> data;
    public class History implements Serializable {
        public String city;
        public int fpNumber;
        public List<Footprints> footprints;
        public class Footprints implements Serializable{
            public String city;
            public int venueId;
            public int fpNumber;
            public String venueName;
            public String address;
            public double longitude;
            public double latitude;
            public String imgUrls;
        }
    }
}
