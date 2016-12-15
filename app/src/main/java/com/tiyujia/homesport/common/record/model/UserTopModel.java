package com.tiyujia.homesport.common.record.model;

import java.io.Serializable;

/**
 * 作者: Cymbi on 2016/12/15 10:05.
 * 邮箱:928902646@qq.com
 */

public class UserTopModel implements Serializable {
    public int rankNum;
    public int userId;
    public int totalScore;
    public UserIconVo userIconVo;
    public class UserIconVo{
        public String nickName;
        public String avatar;
        public String levelName;
        public int step;
    }
}
