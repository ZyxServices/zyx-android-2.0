package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;

/**
 * Created by zzqybyb19860112 on 2016/11/22.1
 */

public class HomePageVenueWhomGoneEntity implements Serializable {
    String userPhotoUrl;//头像图片地址
    String userName;//用户名
    String userLevelUrl;//用户等级---levelName
    int userId;
    String authenticate;
    String step;
    String level;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(String authenticate) {
        this.authenticate = authenticate;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLevelUrl() {
        return userLevelUrl;
    }

    public void setUserLevelUrl(String userLevelUrl) {
        this.userLevelUrl = userLevelUrl;
    }
}
