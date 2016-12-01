package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/11/30.
 */

public class VenueWholeBean implements Serializable {
    private List<String> venueImages;//场馆介绍背景图片集合
    private String venueName;//场馆名字
    private int venueType;//攀岩类型
    private int venueDegree;//攀岩难度
    private String venueAddress;//场馆详细地址
    private String venuePhone;//场馆联系电话
    private String venueDescription;//场馆介绍
    private String developBackground;//开发背景

    public List<String> getVenueImages() {
        return venueImages;
    }

    public void setVenueImages(List<String> venueImages) {
        this.venueImages = venueImages;
    }

    public String getDevelopBackground() {
        return developBackground;
    }

    public void setDevelopBackground(String developBackground) {
        this.developBackground = developBackground;
    }


    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public int getVenueType() {
        return venueType;
    }

    public void setVenueType(int venueType) {
        this.venueType = venueType;
    }

    public int getVenueDegree() {
        return venueDegree;
    }

    public void setVenueDegree(int venueDegree) {
        this.venueDegree = venueDegree;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public String getVenuePhone() {
        return venuePhone;
    }

    public void setVenuePhone(String venuePhone) {
        this.venuePhone = venuePhone;
    }

    public String getVenueDescription() {
        return venueDescription;
    }

    public void setVenueDescription(String venueDescription) {
        this.venueDescription = venueDescription;
    }
}
