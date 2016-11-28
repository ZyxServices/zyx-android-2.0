package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/11/22.1
 */

public class HomePageGridViewPicAdapter extends BaseAdapter {
    Context context;
    List<String> urlList;

    public HomePageGridViewPicAdapter(Context context, List<String> urlList) {
        this.context = context;
        if (urlList.size()!=0) {
            this.urlList = urlList;
        }else {
            this.urlList=new ArrayList<>();
        }
    }
    @Override
    public int getCount() {
        return urlList.size();
    }
    @Override
    public Object getItem(int position) {
        return urlList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GVViewHolder holder=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_gridview_image,null);
            holder=new GVViewHolder();
            convertView.setTag(holder);
        }else {
            holder= (GVViewHolder) convertView.getTag();
        }
        holder.ivItemPic= (ImageView) convertView.findViewById(R.id.ivItemPic);
        String picUrl=urlList.get(position);
        Picasso.with(context).load(picUrl).into(holder.ivItemPic);
        return convertView;
    }
    class GVViewHolder{
        ImageView ivItemPic;
    }
}
