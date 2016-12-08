package com.tiyujia.homesport.common.community.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/5 17:58.
 * 邮箱:928902646@qq.com
 */

public class RecommendModel implements Serializable {
    public int state;
    public List<Recommend> data;
    public class  Recommend{
        public int id;//动态id
        public long createTime;//创建时间
        public int userId;//用户id
        public String topicTitle;//Title
        public String topicContent;//内容
        public String imgUrl;//图片
        public String videoUrl;//视频地址
        public int state;
        public int fromId;
        public int fromType;
        public String local;
        public UserIconVo userIconVo;
        public int zanCounts;
        public int commentCounts;
        public UserAuthVo userAuthVo;
        public class UserAuthVo{
            public String authInfo;//签名
        }
        public class UserIconVo implements Serializable{
            public String nickName;
            public String avatar;
            public int authenticate;
            public int id;
            public Level level;
            public class Level implements Serializable{
                public int id;
                public int step;
                public int pointCount;
                public String pointDesc;
            }
        }
    }

}
