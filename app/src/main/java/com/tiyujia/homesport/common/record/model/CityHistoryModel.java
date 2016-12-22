package com.tiyujia.homesport.common.record.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/21 09:59.
 * 邮箱:928902646@qq.com
 */

public class CityHistoryModel implements Serializable {
    public int state;
    public List<History> data;
    public class History{
        public int id;
        public int userId;
        public int sportInfoId;
        public int score;
        public long spendTime;
        public Integer concernId;
        public int venueId;
        public long createTime;
        public String venueName;
        public String level;
        public String path;
        public String imgUrls;
    }
}
