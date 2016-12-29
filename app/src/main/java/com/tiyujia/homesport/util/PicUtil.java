package com.tiyujia.homesport.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.WindowManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Administrator on 2016/8/3.
 */
public class PicUtil {
    public static Bitmap compress(Bitmap bitmap, int width, int height) {
        return ThumbnailUtils.extractThumbnail(bitmap, width, height);
    }

    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 设置为ture,只读取图片的大小，不把它加载到内存中去
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 1280, 720);// 此处，选取了480x800分辨率的照片
        options.inJustDecodeBounds = false;// 处理完后，同时需要记得设置为false
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return bitmap;
    }

    // 计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap getImageBitmap(String path) {
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inStream);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getImageUrl(Context context, String url) {
        String resultUrl = "";
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth() - 48;
        String addUrl = width + "x384";
        String baseUrl = url.substring(0, url.lastIndexOf("."));
        String resUrl = url.substring(url.lastIndexOf("."), url.length());
        resultUrl = baseUrl + "__" + addUrl + resUrl;
        return resultUrl;
    }

    public static String getImageUrlDetail(Context context, String url, int width, int height) {
        String resultUrl = "";
        String type = url.substring(url.lastIndexOf("."), url.length());
        if (type.equals(".gif")) {
            resultUrl = url;
        } else {
            String addUrl = width + "x" + height;
            String baseUrl = url.substring(0, url.lastIndexOf("."));
            String resUrl = url.substring(url.lastIndexOf("."), url.length());
            resultUrl = baseUrl + "__" + addUrl + resUrl;
        }
        return resultUrl;
    }

    public static Bitmap returnBitMap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return bitmap;
    }

    /**
     * 获取视频缩略图
     *
     * @param videoPath
     * @param width
     * @param height
     * @param kind      MediaStore.Images.Thumbnails.MICRO_KIND 或 MediaStore.Images.Thumbnails.MINI_KIND
     * @return
     */
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        if (bitmap == null) {
            return null;
        }

        if (width <= 0 || height <= 0) {
            width = bitmap.getWidth();
            height = bitmap.getHeight();
        }

        return ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
    }

    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


}
