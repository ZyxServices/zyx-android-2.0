package com.tiyujia.homesport.common.homepage.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/12/15.
 */

public class HomePageCommentEntity implements Serializable{
    public int state;
    public List<HomePage> data;
    public class HomePage{
        public int id;                      //"id": 853,
        public long createTime;             //"createTime": 1481098738751,
        public int commentType;             //"commentType": 3,
        public int commentId;               //"commentId": 322,
        public String  commentContent;      //"commentContent": "哈哈",
        public int  commentAccount;         //"commentAccount": 214,
        public int  commentState;           //"commentState": 0,
        public String commentImgPath; //"commentImgPath": "group1/M00/00/14/dz1CN1hHxrqAfSCeAABm8P_gVmY264.png,group1/M00/00/14/dz1CN1hHxrqAQ1gWAABFEW0m2QM553.png",
        public List<ReplyData> replyVos;    //回复的内容集合
        public class ReplyData implements Serializable{
            public int id;//"id": 531,
            public long createTime;//"createTime": 1481098773876,
            public int replyParentId;//"replyParentId": 853,
            public int replyFromUser;//"replyFromUser": 214,
            public int replyToUser;//"replyToUser": -1,
            public String replyContent;//"replyContent": "还多",
            public int replyState;//"replyState": 0,
            public String replyImgPath;//"replyImgPath": null,
            public FromUserInfo fromUserVo;//评论来源
            public class FromUserInfo implements Serializable{
                public int id;//"id": 214,
                public String  nickName;//"nickName": "趣小妹",
                public String  avatar;//"avatar": "group1/M00/00/0F/dz1CN1g3_KmAXQW8AAAGQaCJo8Q077.jpg",
                public int  authenticate;//"authenticate": 0,
                public String  step;//"step": null,
                public String  levelName;//"levelName": null,
                public String  level;//"level": null
            }
            public ToUserInfo toUserVo;//评论对象信息
            public class ToUserInfo implements Serializable{
                public int id;//"id": 214,
                public String  nickName;//"nickName": "趣小妹",
                public String  avatar;//"avatar": "group1/M00/00/0F/dz1CN1g3_KmAXQW8AAAGQaCJo8Q077.jpg",
                public int  authenticate;//"authenticate": 0,
                public String  step;//"step": null,
                public String  levelName;//"levelName": null,
                public String  level;//"level": null
            }
        }
        public MainUserInfo userVo;
        public class MainUserInfo implements Serializable{
            public int id;//"id": 214,
            public String  nickName;//"nickName": "趣小妹",
            public String  avatar;//"avatar": "group1/M00/00/0F/dz1CN1g3_KmAXQW8AAAGQaCJo8Q077.jpg",
            public int  authenticate;//"authenticate": 0,
            public String  step;//"step": null,
            public String  levelName;//"levelName": null,
            public String  level;//"level": null
        }
    }

}