package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/12/6.
 */

public class SearchDynamicEntity implements Serializable {
    private int dynamicId;
    private String dynamicTitle;
    private List<String> dynamicImageList;

    public int getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(int dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getDynamicTitle() {
        return dynamicTitle;
    }

    public void setDynamicTitle(String dynamicTitle) {
        this.dynamicTitle = dynamicTitle;
    }

    public List<String> getDynamicImageList() {
        return dynamicImageList;
    }

    public void setDynamicImageList(List<String> dynamicImageList) {
        this.dynamicImageList = dynamicImageList;
    }
}
