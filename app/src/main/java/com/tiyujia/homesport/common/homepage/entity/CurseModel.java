package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/6 11:36.
 * 邮箱:928902646@qq.com
 */

public class CurseModel implements Serializable {
    public int state;
    public List<Curse> data;
    public class Curse{
        public int id;//教程id
        public int labelId;
        public int commentNumber;
        public String title;
        public String content;
        public String courseType;
        public String labelName;
        public String imgUrls;

    }
}
