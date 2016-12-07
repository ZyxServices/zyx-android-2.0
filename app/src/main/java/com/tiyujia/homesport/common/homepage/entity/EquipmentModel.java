package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/6 15:38.
 * 邮箱:928902646@qq.com
 */

public class EquipmentModel implements Serializable {
    public int state;
    public List<Equipment> data;
    public class Equipment{
        public int id;
        public long createTime;
        public String title;
        public String content;
        public String imgUrl;
        public String labelName;
        public int accountId;
        public int labelId;//主锁 4，头盔3，动力绳2 ，鞋子 1
        public int del;
        public int mask;
        public int equipType;
        public int commentCounts;
        public int zanCounts;
        public UserIconVo userIconVo;
        public class UserIconVo{
            public  int id;
            public  int authenticate;
            public  String nickName;
            public  String avatar;
            public Level level;
            public class Level{
                public String pointDesc;
            }
        }
    }
}
