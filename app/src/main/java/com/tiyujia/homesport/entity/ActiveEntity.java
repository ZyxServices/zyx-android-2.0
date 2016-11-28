package com.tiyujia.homesport.entity;

import java.io.Serializable;

/**
 * Created by zzqybyb19860112 on 2016/10/19.
 */

public class ActiveEntity implements Serializable{
    private String title;
    private String picUrl;

    public ActiveEntity(String title, String picUrl) {
        this.title = title;
        this.picUrl = picUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
