package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tiyujia.homesport.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/11/18.1
 */

public class HomePageGVHotCityAdapter extends BaseAdapter {
    List<String> cities;
    Context context;
    public HomePageGVHotCityAdapter(Context context,List<String> cities) {
        if (cities.size()==0){
            this.cities=new ArrayList<>();
        }else {
            this.cities = cities;
        }
        this.context=context;
    }
    @Override
    public int getCount() {
        return cities.size();
    }
    @Override
    public Object getItem(int position) {
        return cities.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView=View.inflate(context, R.layout.item_gv_hot_city,null);
            holder=new ViewHolder();
            holder.tvItemHotCity= (TextView) convertView.findViewById(R.id.tvItemHotCity);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tvItemHotCity.setText(cities.get(position));
        return convertView;
    }
    class ViewHolder{
        TextView tvItemHotCity;
    }
}
