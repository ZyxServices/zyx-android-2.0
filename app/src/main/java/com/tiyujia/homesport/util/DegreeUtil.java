package com.tiyujia.homesport.util;

import android.widget.ImageView;

import com.tiyujia.homesport.R;

/**
 * Created by zzqybyb19860112 on 2016/11/30.
 */

public class DegreeUtil {
    public static void handleDegrees(int degree, ImageView ivDegree1, ImageView ivDegree2, ImageView ivDegree3, ImageView ivDegree4, ImageView ivDegree5) {
        degree-=1;
        ImageView[] ivDegrees=new ImageView[]{ivDegree1,ivDegree2,ivDegree3,ivDegree4,ivDegree5};
        for (int i=0;i<5;i++){
            for (int j=0;j<=degree;j++){
                ivDegrees[j].setImageResource(R.mipmap.tab_start_s);
            }
        }
    }
}
