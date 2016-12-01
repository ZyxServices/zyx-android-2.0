package com.tiyujia.homesport.util;

import android.graphics.Color;
import android.widget.TextView;

import com.tiyujia.homesport.R;

/**
 * Created by zzqybyb19860112 on 2016/11/30.
 */

public class TypeUtil {
    public static void handleType(TextView tvVenueTypeA, String typeA) {
        if (typeA.equals("室内")){
            tvVenueTypeA.setBackgroundResource(R.drawable.border_orange);
            tvVenueTypeA.setTextColor(Color.parseColor("#ff702a"));
        }else {
            tvVenueTypeA.setBackgroundResource(R.drawable.border_blue);
            tvVenueTypeA.setTextColor(Color.parseColor("#2aa7ff"));
        }
    }
}
