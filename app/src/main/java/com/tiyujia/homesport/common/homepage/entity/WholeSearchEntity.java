package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/12/6.
 */

public class WholeSearchEntity implements Serializable {
    List<SearchActiveEntity> activeList;
    List<SearchEquipEntity> equipList;
    List<SearchDynamicEntity> dynamicList;
    List<SearchVenueEntity> venueList;
    List<SearchCourseEntity> courseList;
    List<SearchUserEntity> userList;

    public List<SearchActiveEntity> getActiveList() {
        return activeList;
    }

    public void setActiveList(List<SearchActiveEntity> activeList) {
        this.activeList = activeList;
    }

    public List<SearchEquipEntity> getEquipList() {
        return equipList;
    }

    public void setEquipList(List<SearchEquipEntity> equipList) {
        this.equipList = equipList;
    }

    public List<SearchDynamicEntity> getDynamicList() {
        return dynamicList;
    }

    public void setDynamicList(List<SearchDynamicEntity> dynamicList) {
        this.dynamicList = dynamicList;
    }

    public List<SearchVenueEntity> getVenueList() {
        return venueList;
    }

    public void setVenueList(List<SearchVenueEntity> venueList) {
        this.venueList = venueList;
    }

    public List<SearchCourseEntity> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<SearchCourseEntity> courseList) {
        this.courseList = courseList;
    }

    public List<SearchUserEntity> getUserList() {
        return userList;
    }

    public void setUserList(List<SearchUserEntity> userList) {
        this.userList = userList;
    }
}
