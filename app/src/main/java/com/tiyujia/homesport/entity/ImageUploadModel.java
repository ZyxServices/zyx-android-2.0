package com.tiyujia.homesport.entity;

import java.io.Serializable;

/**
 * 作者: Cymbi on 2016/11/29 18:14.
 * 邮箱:928902646@qq.com
 */

public class ImageUploadModel implements Serializable {
   public int state;
    public Imagelist data;
    public class Imagelist{
      public  String url;
    }
}
