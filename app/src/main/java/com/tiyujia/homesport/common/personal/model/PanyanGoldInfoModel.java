package com.tiyujia.homesport.common.personal.model;

import java.io.Serializable;

/**
 * 作者: Cymbi on 2016/12/16 10:42.
 * 邮箱:928902646@qq.com
 *  {
 "id": 41,
 "createTime": 1481020415669,
 "userId": 186,
 "operId": 5,
 "modelId": null,
 "coinNum": 1,
 "state": 1
 },
 */

public class PanyanGoldInfoModel implements Serializable{
    public int id;
    public int userId;
    public int operId;
    public int coinNum;
    public int state;
    public long createTime;
}
