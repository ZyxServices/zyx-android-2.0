package com.tiyujia.homesport.common.personal.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/1 10:32.
 * 邮箱:928902646@qq.com
 */

public class MyDynamicModel implements Serializable {
    public int id;//动态id
    public long createTime;//创建时间
    public int userId; // 创建者id
    public int type;//0为所有可见,1为好友可见
    public String topicTitle;//Title
    public String topicContent;//内容
    public String videoUrl;//视频地址
    public String local;//来自哪里
    public int zanCounts;//点赞数
    public int commentCounts;//评论数
    public UserIconVo userIconVo;
    public String imgUrl;

    public static class NewsImage implements Serializable {
        private static final long serialVersionUID = -7441255403568397400L;
        public int width;
        public int height;
        public String imgUrl;
    }
    public class  UserIconVo{
        public int id;//用户id
        public String  nickName;//创建者用户名
        public String  avatar;//用户头像
    }
}
