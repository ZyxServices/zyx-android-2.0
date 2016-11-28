package com.tiyujia.homesport.common.homepage.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.tiyujia.homesport.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by zzqybyb19860112 on 2016/11/18.1
 */

public class CityDBManager {
    private final int BUFFER_SIZE=10240;
    private static final String PACKAGE_NAME="com.tiyujia.homesport";
    public static final String DB_NAME="china_city_name.db";
    public static final String DB_PATH = "/data"+ Environment.getDataDirectory().getAbsolutePath()+"/" + PACKAGE_NAME+"/databases";
    private Context mContext;
    private SQLiteDatabase database;
    public CityDBManager(Context context) {
        this.mContext=context;
    }
    public void openDateBase() {
         this.database=this.openDateBase(DB_PATH+"/"+DB_NAME );
        }
    private SQLiteDatabase openDateBase(String dbFile) {
        File file=new File(dbFile);
        if (!file.exists()) {
            File folder=new File(DB_PATH);
            if (!folder.exists()) {
                folder.mkdir();
            }
            InputStream stream=mContext.getResources().openRawResource(R.raw.china_city_name);
            try{
                FileOutputStream outputStream=new FileOutputStream(dbFile);
                byte[] buffer=new byte[BUFFER_SIZE];
                int count=0;
                while ((count=stream.read(buffer))>0) {
                    outputStream.write(buffer, 0, count);
                }
                outputStream.close();
                stream.close();
                SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(dbFile,null);
                return db;
            } catch (Exception e) {
                e.printStackTrace();
        }
        }
        return database;
    }
    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            this.database.close();
        }
    }
}
