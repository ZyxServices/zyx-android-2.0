package com.tiyujia.homesport.common.personal.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/1 10:32.
 * 邮箱:928902646@qq.com
 */

public class MyDynamicModel implements Serializable {
    private int id;//动态id
    private long createTime;//创建时间
    private int userId; // 创建者id
    private int type;//0为所有可见,1为好友可见
    private String topicTitle;//Title
    private String topicContent;//内容
    private String videoUrl;//视频地址
    private String local;//来自哪里
    private int zanCounts;//点赞数
    private int commentCounts;//评论数
    private String imgUrl;
    private ArrayList<String> imageUrl;
    private UserIconVo userIconVo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public int getZanCounts() {
        return zanCounts;
    }

    public void setZanCounts(int zanCounts) {
        this.zanCounts = zanCounts;
    }

    public int getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(int commentCounts) {
        this.commentCounts = commentCounts;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public UserIconVo getUserIconVo() {
        return userIconVo;
    }

    public void setUserIconVo(UserIconVo userIconVo) {
        this.userIconVo = userIconVo;
    }

    public static class  UserIconVo implements Serializable{
        public int id;//用户id
        public String  nickName;//创建者用户名
        public String  avatar;//用户头像

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
