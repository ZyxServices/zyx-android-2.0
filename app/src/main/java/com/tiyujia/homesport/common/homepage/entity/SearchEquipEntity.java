package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/12/6.
 */

public class SearchEquipEntity implements Serializable {
    private int equipId;
    private String equipTitle;//标题
    private List<String> equipImageUrls;//图片地址集合

    public List<String> getEquipImageUrls() {
        return equipImageUrls;
    }

    public void setEquipImageUrls(List<String> equipImageUrls) {
        this.equipImageUrls = equipImageUrls;
    }

    public int getEquipId() {
        return equipId;
    }

    public void setEquipId(int equipId) {
        this.equipId = equipId;
    }

    public String getEquipTitle() {
        return equipTitle;
    }

    public void setEquipTitle(String equipTitle) {
        this.equipTitle = equipTitle;
    }
}
