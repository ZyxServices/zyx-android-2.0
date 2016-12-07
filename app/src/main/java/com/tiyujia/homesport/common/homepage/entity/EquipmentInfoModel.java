package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;

/**
 * 作者: Cymbi on 2016/12/7 10:40.
 * 邮箱:928902646@qq.com
 */

public class EquipmentInfoModel implements Serializable {
    public int state;
    public Info data;
    public class Info{
        public int id;//装备秀ID
        public long createTime;
        public String title;
        public String content;
        public int accountId;//发起者ID
        public int labelId;//装备类型
        public int commentCounts;
        public int zanCounts;
        public String imgUrl;
        public UserIconVo userIconVo;
        public class UserIconVo{
            public String nickName;
            public String avatar;
            public String authenticate;
        }
    }
}
