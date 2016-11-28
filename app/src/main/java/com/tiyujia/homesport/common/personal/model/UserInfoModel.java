package com.tiyujia.homesport.common.personal.model;

import com.tiyujia.homesport.entity.User;

import java.io.Serializable;
import java.util.logging.Level;

/**
 * 作者: Cymbi on 2016/11/24 16:21.
 * 邮箱:928902646@qq.com
 */

public class UserInfoModel implements Serializable{

    public int state;
    public String successmsg;
    public UserModel data;
    public class UserModel{
        public String phone;
        public String nickname;
        public String avatar;
        public String sex;
        public String address;
        public String signature;
        public long birthday;
        public Levels level;
        public int gz;
        public int fs;
        public int coin;
        public class Levels{
            public int id;
            public int userId;
            public int pointCount;
            public String pointDesc;

            @Override
            public String toString() {
                return  "Levels {"+
                        "pointCount"+pointCount+'\''+
                        ", pointDesc"+pointDesc+'\''+
                        '}';
            }
        }
        @Override
        public String toString() {

            return "UserModel{"+
                    "phone ="+phone+'\''+
                    ", nickname ="+nickname+'\''+
                    ", avatar ="+avatar+'\''+
                    ", signature ="+signature+'\''+
                    "}";
        }
    }

}
