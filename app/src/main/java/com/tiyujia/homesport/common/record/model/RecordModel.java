package com.tiyujia.homesport.common.record.model;

import java.io.Serializable;

/**
 * 作者: Cymbi on 2016/12/28 12:06.
 * 邮箱:928902646@qq.com
 */

public class RecordModel implements Serializable {
    public int id;
    public int userId;
    public int type;
    public int venueId;
    public int score;
    public long createTime;
    public long spendTime;
    public String level;

}
