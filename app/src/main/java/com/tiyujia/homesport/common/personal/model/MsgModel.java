package com.tiyujia.homesport.common.personal.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/8 17:55.
 * 邮箱:928902646@qq.com
 */

public class MsgModel implements Serializable {
    public int state;
    public List<Msg> data;
    public class Msg{
        public int id;
        public int bodyId;
        public int bodyType;
        public int msgType;
        public long createTime;
        public String fromContent;
        public String toContent;
        public UserVo userVo;
        public ToObject toObject;
        public class UserVo{
            public int id;
            public String nickName;
            public String avatar;
            public int authenticate;
            public Level level;
            public class Level{
                public String pointDesc;
                public int step;
                public int pointCount;
            }
        }
        public class ToObject{
            public String toUri;
            public String toTitle;
            public String toContent;
        }
    }
}
