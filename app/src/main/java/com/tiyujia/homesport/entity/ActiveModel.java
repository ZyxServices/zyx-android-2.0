package com.tiyujia.homesport.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: Cymbi on 2016/11/14 11:24.
 * 邮箱:928902646@qq.com
 */

public class ActiveModel implements Serializable {
    public int state;
    public List<Active> data;
    public class Active implements Serializable{
        public int id;//活动id
        public Userinfo user;
        public String title;
        public String imgUrls;
        public String activityType;
        public long startTime;//活动开始时间
        public long endTime;//活动结束时间
        public long lastTime;//报名截止时间
        public long createTime;//创建时间
        public int maxPeople;//最大人数
        public int memberPeople;//活动报名人数
        public int paymentType;//付费类型（0奖励 1免费 2AA）
        public int price;//活动价格
        public int zan;//赞
        public int commentNumber;//评论数
        public String city;//城市
        public class Userinfo{
            public int id;
            public String nickname;
            public String avatar;
            public Level level;
            public class Level{
                public int id;//不知道是啥id
                public int userId;//用户id
                public int step;//不知道
                public int pointCount;
                public String pointDesc;//等级名
            }
        }
    }

}
