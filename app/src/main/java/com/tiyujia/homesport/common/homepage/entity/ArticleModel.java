package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;

/**
 * 作者: Cymbi on 2016/12/6 19:53.
 * 邮箱:928902646@qq.com
 */

public class ArticleModel implements Serializable {
    public int state;
    public Article data;
    public class Article{
        public int id;//教程id
        public int labelId;
        public int commentNumber;
        public String title;
        public String content;
        public String courseType;
        public String labelName;
        public String imgUrls;
        public long createTime;
        public boolean concern;
    }
}
