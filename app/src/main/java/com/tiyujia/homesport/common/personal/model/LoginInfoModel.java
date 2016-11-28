package com.tiyujia.homesport.common.personal.model;

import java.io.Serializable;

/**
 * 作者: Cymbi on 2016/11/24 11:00.
 * 邮箱:928902646@qq.com
 */

public class LoginInfoModel implements Serializable {
    private int id;
    private long birthday;
    private String phone;
    private String nickname;
    private String sex;
    private String avatar;
    private String token;
    private String signature;
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public class level{
            public int id;
            public int userId;
            public int pointCount;
            public String pointDesc;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getPointCount() {
            return pointCount;
        }

        public void setPointCount(int pointCount) {
            this.pointCount = pointCount;
        }

        public String getPointDesc() {
            return pointDesc;
        }

        public void setPointDesc(String pointDesc) {
            this.pointDesc = pointDesc;
        }
    }
}
 /*  {
  "data": {
    "id": 186,
    "phone": "18180770000",
    "nickname": "yf",
    "sex": null,
    "avatar": null,
    "del": 0,
    "mask": 0,
    "token": "dcb08d3e8cb64d00b3fc1d63b3638f5f",
    "birthday": 0,
    "signature": null,
    "address": null,
    "level": {
      "id": 412,
      "userId": 186,
      "pointCount": 100,
      "pointDesc": "初学乍练"
    }
  },
  "state": 200,
  "successmsg": "登录成功"
}*/
