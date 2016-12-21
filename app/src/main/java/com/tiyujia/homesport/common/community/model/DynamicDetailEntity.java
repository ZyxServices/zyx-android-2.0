package com.tiyujia.homesport.common.community.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/12/21.
 */

public class DynamicDetailEntity implements Serializable {
    public boolean isZan;
    public Concern concern;
    public class Concern implements Serializable{
        public int id;
        public long createTime;
        public int userId;
        public int type;
        public String topicTitle;
        public String topicContent;
        public String imgUrl;
        public String videoUrl;
        public int topicVisible;
        public int state;
        public String fromId;
        public String fromType;
        public String local;
        public String appType;
        public DynamicUserInfo userIconVo;
        public class DynamicUserInfo implements Serializable{
            public int id;
            public String nickName;
            public String avatar;
            public int authenticate;
            public String step;
            public String levelName;
            public DynamicLevel level;
            public class DynamicLevel implements Serializable{
                public int id;
                public int step;
                public int pointCount;
                public String pointDesc;
            }
        }
        public int zanCounts;
        public int commentCounts;
        public String authInfo;
        public Integer follow;
    }
}
