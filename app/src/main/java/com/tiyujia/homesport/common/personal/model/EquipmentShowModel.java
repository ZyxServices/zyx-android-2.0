package com.tiyujia.homesport.common.personal.model;

import com.tiyujia.homesport.common.homepage.entity.EquipmentInfoModel;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/7 12:03.
 * 邮箱:928902646@qq.com
 */

public class EquipmentShowModel implements Serializable {
    public int state;
    public List<Model> data;
    public class Model{
        public int id;//装备秀ID
        public long createTime;
        public String title;
        public String content;
        public int accountId;//发起者ID
        public int labelId;//装备类型
        public int commentCounts;
        public int zanCounts;
        public String imgUrl;
        public EquipmentInfoModel.Info.UserIconVo userIconVo;
        public class UserIconVo{
            public String nickName;
            public String avatar;
            public String authenticate;
            public Level level;
            public class Level{
                public String pointDesc;
            }
        }
    }
}
