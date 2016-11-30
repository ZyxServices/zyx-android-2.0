package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/11/11.1
 */

public class HomePageRecentVenueEntity implements Serializable{
    private  int id;
    private  int type;//场馆类型（1-室内 2-室外）
    private String name;//场馆名称
    private double longitude;//经度
    private double latitude;//纬度
    private String city;//所属城市
    private String mark;//描述标签
    private String description;//场馆介绍
    private String phone;//联系电话
    private String address;//场馆地址
    private List<String> imgUrls;//场馆封面图片地址
    private int level;//场馆综合难度
    private long create_time;//创建时间
    private int distance;//距离当前位置多少米
    private int pnumber;//到过场馆的人数
    private int talkNumber;//评论过该场馆的人数？？？？？？？？？？？？？？

    public int getTalkNumber() {
        return talkNumber;
    }

    public void setTalkNumber(int talkNumber) {
        this.talkNumber = talkNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getPnumber() {
        return pnumber;
    }

    public void setPnumber(int pnumber) {
        this.pnumber = pnumber;
    }
}
