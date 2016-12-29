package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;

/**
 * 作者: Cymbi on 2016/12/8 11:03.
 * 邮箱:928902646@qq.com
 */

public class DateInfoModel implements Serializable {
    public int state;
    public Info data;
    public class  Info{
        public User user;
        public int id;
        public class User{
            public int id;
            public String nickname;
            public String avatar;
            public String signature;
            public String phone ;
            public int isFollow;//0没去过，1去过
            public Level level;
            public class Level{
                public int id;
                public int userId;
                public int step;
                public int pointCount;
                public String pointDesc;
            }
        }
        public String title;
        public String descContent;
        public String imgUrls;
        public int activityType;
        public int activityModule;
        public long startTime;
        public long endTime;
        public long lastTime;
        public long createTime;
        public int maxPeople;
        public int memberPeople;
        public String address;
        public String city;
        public int paymentType;
        public int price;
        public String targetUrl;
        public String editDescImgUrl;
     }
}
