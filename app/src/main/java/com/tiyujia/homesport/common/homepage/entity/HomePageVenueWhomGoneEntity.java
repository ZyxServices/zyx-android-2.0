package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;

/**
 * Created by zzqybyb19860112 on 2016/11/22.1
 */

public class HomePageVenueWhomGoneEntity implements Serializable {
    String userPhotoUrl;
    String userName;
    String userLevelUrl;

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
