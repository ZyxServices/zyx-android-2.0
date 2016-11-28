package com.tiyujia.homesport.entity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by zzqybyb19860112 on 2016/11/24.1
 */

public class VideoEntity implements Serializable {
    private String title;//视频标题
    private String path;//视频源路径
    private String totalTime;//视频总时长
    private int localPath;//本地资源路径
    private Drawable drawable;//本地资源路径

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getLocalPath() {
        return localPath;
    }

    public void setLocalPath(int localPath) {
        this.localPath = localPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

}
