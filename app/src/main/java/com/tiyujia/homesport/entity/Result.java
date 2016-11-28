package com.tiyujia.homesport.entity;

/**
 * Created by zzqybyb19860112 on 2016/11/10.
 */

public class Result<T> {
    public int state;
    public String successmsg;
    public String phone;
    public int code;
    public T data;
    @Override
    public String toString() {
        return "{" +
                "state=" + state +
                ", successmsg='" + successmsg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
