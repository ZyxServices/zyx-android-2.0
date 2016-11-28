package com.tiyujia.homesport.common.homepage.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tiyujia.homesport.common.homepage.entity.HomePageSearchEntity;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zzqybyb19860112 on 2016/11/15.1
 */

public class DBVenueContext {
    public DBVenueHelper dbHelper;//声明一个内部数据库（SQLite）帮助类的实例
    public DBVenueContext(Context context){
        dbHelper=new DBVenueHelper(context,"homesporthomepagesearch.db",null,1);//新建DBContext实例时对帮助类初始化
    }
    public long insert(ContentValues values){//通过开启事务的形式插入数据到数据库表中
        SQLiteDatabase sdb=dbHelper.getWritableDatabase();
        sdb.beginTransaction();
        long rawID=sdb.insert("homesporthomepagesearchtab",null,values);
        sdb.setTransactionSuccessful();
        sdb.endTransaction();
        sdb.close();
        return rawID;
    }
    public CopyOnWriteArrayList<HomePageSearchEntity> query(){//查询数据库表中的所有信息
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        String sql="select * from homesporthomepagesearchtab";
        Cursor c=sdb.rawQuery(sql,null);
        CopyOnWriteArrayList<HomePageSearchEntity> result=new CopyOnWriteArrayList<>();
        for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){
            HomePageSearchEntity data=new HomePageSearchEntity();
            data.setNumber(c.getInt(c.getColumnIndex("_id")));
            data.setSearchText(c.getString(c.getColumnIndex("content")));
            result.add(data);
        }
        c.close();
        sdb.close();
        return result;
    }
    public void deleteAllData(){
        SQLiteDatabase sdb=dbHelper.getReadableDatabase();
        sdb.beginTransaction();
        String sql="drop table if exists homesporthomepagesearchtab";
        sdb.execSQL(sql);
        sdb.setTransactionSuccessful();
        sdb.endTransaction();
        dbHelper.onCreate(sdb);
    }
    class DBVenueHelper extends SQLiteOpenHelper {
        public DBVenueHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {//创建数据库表
            String sql="create table if not exists homesporthomepagesearchtab " +
                    "(_id integer primary key autoincrement," +
                    "content varchar(100) not null)";
            sqLiteDatabase.execSQL(sql);
        }
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            String sql="drop table if exists homesporthomepagesearchtab";//删除已有的数据库表，并且重新新建一张数据库表
            sqLiteDatabase.execSQL(sql);
            onCreate(sqLiteDatabase);
        }
        public boolean onUpgradeDel(SQLiteDatabase db,int position){//删除掉对应位置的表中的信息
            String sql="delete from notetab where _id="+position;
            db.execSQL(sql);
            return true;
        }
    }
}

