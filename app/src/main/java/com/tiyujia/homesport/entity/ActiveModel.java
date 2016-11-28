package com.tiyujia.homesport.entity;

import java.io.Serializable;

/**
 * 作者: Cymbi on 2016/11/14 11:24.
 * 邮箱:928902646@qq.com
 */

public class ActiveModel implements Serializable {
    private String avatar;
    private String nickname;
    private String title;
    private String apply_lable;
    private String background;
    private String active_lable;
    private String address;
    private String lv;
    private Long time;
    private int apply;
    private int award;
    private int msg;
    private int zan;

    public String getLv() {
        return lv;
    }

    public void setLv(String lv) {
        this.lv = lv;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApply_lable() {
        return apply_lable;
    }

    public void setApply_lable(String apply_lable) {
        this.apply_lable = apply_lable;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getActive_lable() {
        return active_lable;
    }

    public void setActive_lable(String active_lable) {
        this.active_lable = active_lable;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getApply() {
        return apply;
    }

    public void setApply(int apply) {
        this.apply = apply;
    }

    public int getAward() {
        return award;
    }

    public void setAward(int award) {
        this.award = award;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }
}
