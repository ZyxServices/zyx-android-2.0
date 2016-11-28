package com.tiyujia.homesport.entity;

import java.io.Serializable;

/**
 * Created by zzqybyb19860112 on 2016/10/19.
 */

public class ActiveLiveEntity implements Serializable {
    private int type;
    private String bmpUrl;
    private String title;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBmpUrl() {
        return bmpUrl;
    }

    public void setBmpUrl(String bmpUrl) {
        this.bmpUrl = bmpUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
