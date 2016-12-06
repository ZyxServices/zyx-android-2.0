package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;

/**
 * Created by zzqybyb19860112 on 2016/12/6.
 */

public class SearchUserEntity implements Serializable {
    private  int userId;//用户在系统中的统一标识id
    private String userPhotoUrl;//用户的头像地址
    private String userName;//用户名称
    private UserModelEntity entity;

    public UserModelEntity getEntity() {
        return entity;
    }

    public void setEntity(UserModelEntity entity) {
        this.entity = entity;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

}
