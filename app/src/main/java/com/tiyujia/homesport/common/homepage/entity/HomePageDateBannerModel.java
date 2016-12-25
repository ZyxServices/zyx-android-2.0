package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/25 14:42.
 * 邮箱:928902646@qq.com
 */

public class HomePageDateBannerModel implements Serializable {
    public int state;
    public List<Banner> data;
    public class Banner implements Serializable{
        public int id;
        public int model;
        public int modelId;
        public String imageUrl;
        public long createTime;
    }
}
