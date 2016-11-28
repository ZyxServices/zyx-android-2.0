package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;

/**
 * Created by zzqybyb19860112 on 2016/11/22.1
 */

public class CallBackDetailEntity implements Serializable {
    private String callFrom;
    private String callTo;
    private String callDetail;
    private int fromID;
    private int toID;
    public String getCallFrom() {
        return callFrom;
    }

    public void setCallFrom(String callFrom) {
        this.callFrom = callFrom;
    }

    public String getCallTo() {
        return callTo;
    }

    public void setCallTo(String callTo) {
        this.callTo = callTo;
    }

    public String getCallDetail() {
        return callDetail;
    }

    public void setCallDetail(String callDetail) {
        this.callDetail = callDetail;
    }

    public int getFromID() {
        return fromID;
    }

    public void setFromID(int fromID) {
        this.fromID = fromID;
    }

    public int getToID() {
        return toID;
    }

    public void setToID(int toID) {
        this.toID = toID;
    }
}
