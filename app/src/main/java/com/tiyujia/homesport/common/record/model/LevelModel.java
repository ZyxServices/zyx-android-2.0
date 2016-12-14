package com.tiyujia.homesport.common.record.model;

import java.io.Serializable;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/13 19:39.
 * 邮箱:928902646@qq.com
 */

public class LevelModel implements Serializable {
    public int state;
    public List<Level> data;
    public class Level implements Serializable{
        public String level;
    }
}
