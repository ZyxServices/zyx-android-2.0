package com.tiyujia.homesport.util;

import android.text.TextUtils;
import android.widget.ImageView;

import com.tiyujia.homesport.R;

/**
 * 作者: Cymbi on 2016/11/29 13:39.
 * 邮箱:928902646@qq.com
 */

public class LvUtil  {
    public static void setLv(ImageView iv,String s){
        if(s.equals("初学乍练")){
            iv.setImageResource(R.mipmap.img_lv1);
        }else if(s.equals("登堂入室")){
            iv.setImageResource(R.mipmap.img_lv2);
        }else if(s.equals("圆转成熟")){
            iv.setImageResource(R.mipmap.img_lv3);
        }else if(s.equals("初窥堂奥")){
            iv.setImageResource(R.mipmap.img_lv4);
        }else if(s.equals("略有小成")){
            iv.setImageResource(R.mipmap.img_lv5);
        }else if(s.equals("渐入佳境")){
            iv.setImageResource(R.mipmap.img_lv6);
        }else if(s.equals("炉火纯青")){
            iv.setImageResource(R.mipmap.img_lv7);
        }else if(s.equals("自成一派")){
            iv.setImageResource(R.mipmap.img_lv8);
        }else if(s.equals("已臻大成")){
            iv.setImageResource(R.mipmap.img_lv9);
        }else if(s.equals("功行圆满")){
            iv.setImageResource(R.mipmap.img_lv10);
        }else if(s.equals("登峰造极")){
            iv.setImageResource(R.mipmap.img_lv11);
        }else if(s.equals("出神入化")){
            iv.setImageResource(R.mipmap.img_lv12);
        }else if(s.equals("神功绝世")){
            iv.setImageResource(R.mipmap.img_lv13);
        }
    }
    public static String setLevelTXT(String levelName){
        String result="";
        if (levelName.equals("")||levelName.equals("null")||levelName==null){
            result="初学乍练";
        }else {
            result=levelName;
        }
        return result;
    }
}
