package com.tiyujia.homesport.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

/**
 * Created by zzqybyb19860112 on 2016/11/24.1
 */

public class VideoUtil {
    public static Drawable getThumbnail(Context context,Uri aVideoUri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, aVideoUri);
        Bitmap bitmap = retriever
                .getFrameAtTime(1*1000*1000, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
        return drawable;
    }
}
