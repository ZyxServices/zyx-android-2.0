package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/11/11.1
 */

public class HomePageRecentVenueEntity implements Serializable{
    String bigPicUrl;//大图片地址
    String venueName;//场馆名字
    List<String> venueType;//室内室外类型
    int degreeNumber;//难度系数
    int numberGone;//去过人数
    int numberTalk;//评论人数

    public String getBigPicUrl() {
        return bigPicUrl;
    }

    public void setBigPicUrl(String bigPicUrl) {
        this.bigPicUrl = bigPicUrl;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public List<String> getVenueType() {
        return venueType;
    }

    public void setVenueType(List<String> venueType) {
        this.venueType = venueType;
    }

    public int getDegreeNumber() {
        return degreeNumber;
    }

    public void setDegreeNumber(int degreeNumber) {
        this.degreeNumber = degreeNumber;
    }

    public int getNumberGone() {
        return numberGone;
    }

    public void setNumberGone(int numberGone) {
        this.numberGone = numberGone;
    }

    public int getNumberTalk() {
        return numberTalk;
    }

    public void setNumberTalk(int numberTalk) {
        this.numberTalk = numberTalk;
    }
}
