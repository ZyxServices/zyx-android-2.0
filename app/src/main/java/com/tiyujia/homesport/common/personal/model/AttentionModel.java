package com.tiyujia.homesport.common.personal.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: Cymbi on 2016/11/29 11:23.
 * 邮箱:928902646@qq.com
 */

public class AttentionModel implements Serializable {

    public int state;
    public List<AttentionList> data;

    public  class AttentionList implements Serializable {
        public  int id;
        public  String phone;
        public  String nickname;
        public  String avatar;
        public  String signatures;
        public  leves level;
    }
    public class leves{
        public String pointDesc;
    }
}
