package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/11/22.1
 */

public class HomePageDiscussEntity implements Serializable {
    private String mainUserPhotoUrl;
    private String mainUserName;
    private String mainUserLevelUrl;
    private String mainUserSendTime;
    private String mainUserSendContent;
    private List<String> mainUserSendPicUrlList;
    private List<CallBackDetailEntity> discussList;

    public String getMainUserPhotoUrl() {
        return mainUserPhotoUrl;
    }

    public void setMainUserPhotoUrl(String mainUserPhotoUrl) {
        this.mainUserPhotoUrl = mainUserPhotoUrl;
    }

    public String getMainUserName() {
        return mainUserName;
    }

    public void setMainUserName(String mainUserName) {
        this.mainUserName = mainUserName;
    }

    public String getMainUserLevelUrl() {
        return mainUserLevelUrl;
    }

    public void setMainUserLevelUrl(String mainUserLevelUrl) {
        this.mainUserLevelUrl = mainUserLevelUrl;
    }

    public String getMainUserSendTime() {
        return mainUserSendTime;
    }

    public void setMainUserSendTime(String mainUserSendTime) {
        this.mainUserSendTime = mainUserSendTime;
    }

    public String getMainUserSendContent() {
        return mainUserSendContent;
    }

    public void setMainUserSendContent(String mainUserSendContent) {
        this.mainUserSendContent = mainUserSendContent;
    }

    public List<String> getMainUserSendPicUrlList() {
        return mainUserSendPicUrlList;
    }

    public void setMainUserSendPicUrlList(List<String> mainUserSendPicUrlList) {
        this.mainUserSendPicUrlList = mainUserSendPicUrlList;
    }

    public List<CallBackDetailEntity> getDiscussList() {
        return discussList;
    }

    public void setDiscussList(List<CallBackDetailEntity> discussList) {
        this.discussList = discussList;
    }
}
