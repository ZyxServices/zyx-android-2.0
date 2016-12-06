package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;

/**
 * Created by zzqybyb19860112 on 2016/12/5.
 */

public class SearchActiveEntity implements Serializable {
    private String type;//类型（求带。。。）
    private String imageUrl;//展示图片地址
    private String title;//标题
    private int alreadyRegistered;//已经报名人数
    private int restNumber;//剩余名额
    private int reward;//奖励金额
    private int activeId;//活动id

    public int getActiveId() {
        return activeId;
    }

    public void setActiveId(int activeId) {
        this.activeId = activeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAlreadyRegistered() {
        return alreadyRegistered;
    }

    public void setAlreadyRegistered(int alreadyRegistered) {
        this.alreadyRegistered = alreadyRegistered;
    }

    public int getRestNumber() {
        return restNumber;
    }

    public void setRestNumber(int restNumber) {
        this.restNumber = restNumber;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }
}
