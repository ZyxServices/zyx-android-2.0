package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;

/**
 * Created by zzqybyb19860112 on 2016/12/6.
 */

public class UserModelEntity implements Serializable {
    private int userModelId;//等级制度下的id
    private int userStep;//用户等级
    private String userLabel;//用户等级名称（初学乍练）
    private String lastDynamic="这是用户的最新动态";//用户最新动态信息（初学乍练）
    private int praiseCount;//点赞次数

    public String getLastDynamic() {
        return lastDynamic;
    }

    public void setLastDynamic(String lastDynamic) {
        this.lastDynamic = lastDynamic;
    }

    public int getUserModelId() {
        return userModelId;
    }

    public void setUserModelId(int userModelId) {
        this.userModelId = userModelId;
    }

    public int getUserStep() {
        return userStep;
    }

    public void setUserStep(int userStep) {
        this.userStep = userStep;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }
}
