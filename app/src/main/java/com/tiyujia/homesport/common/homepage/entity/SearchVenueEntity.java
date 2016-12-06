package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;

/**
 * Created by zzqybyb19860112 on 2016/12/6.
 */

public class SearchVenueEntity implements Serializable {
    private  int venueId;
    private  int venueType;//场馆类型（1-室内 2-室外）
    private String venueName;//场馆名称
    private String venueMark;//描述标签
    private String venuePicture;//场馆封面图片地址
    private int venueDegree;//场馆综合难度

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public int getVenueType() {
        return venueType;
    }

    public void setVenueType(int venueType) {
        this.venueType = venueType;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueMark() {
        return venueMark;
    }

    public void setVenueMark(String venueMark) {
        this.venueMark = venueMark;
    }

    public String getVenuePicture() {
        return venuePicture;
    }

    public void setVenuePicture(String venuePicture) {
        this.venuePicture = venuePicture;
    }

    public int getVenueDegree() {
        return venueDegree;
    }

    public void setVenueDegree(int venueDegree) {
        this.venueDegree = venueDegree;
    }
}
