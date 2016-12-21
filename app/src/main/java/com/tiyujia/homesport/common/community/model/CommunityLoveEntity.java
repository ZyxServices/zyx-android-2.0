package com.tiyujia.homesport.common.community.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/12/21.
 */

public class CommunityLoveEntity implements Serializable {
    public int state;
    public String successmsg;
    public LoveData data;
    public class LoveData implements Serializable{
        public List<LoveUser> userList;
        public int totalCount;
        public class LoveUser implements Serializable{
            public int id;
            public String nickName;
            public String avatar;
            public String authenticate;
            public String step;
            public String levelName;
            public LoveUserLevel level;
            public class LoveUserLevel implements Serializable{
                public int id;
                public int step;
                public int pointCount;
                public String pointDesc;
            }
        }
    }
}
