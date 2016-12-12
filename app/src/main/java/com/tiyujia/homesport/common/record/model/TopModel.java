package com.tiyujia.homesport.common.record.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/12 11:11.
 * 邮箱:928902646@qq.com
 */

public class TopModel implements Serializable {
    public int state;
    public List<Top> data;
    public class Top implements Serializable{
        public int rankNum;//排名
        public int userId;
        public Integer totalScore;//荣誉值
        public UserIconVo userIconVo;
        public class UserIconVo{
        public int id;//默认为null不知道是做什么的
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
