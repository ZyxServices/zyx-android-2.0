package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.entity.HomePageVenueWhomGoneEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/11/22.1
 */

public class HomePageVenueUserAdapter extends RecyclerView.Adapter {
    Context context;
    List<HomePageVenueWhomGoneEntity> mValues;
    private static final int LAST_DATA=1;
    public HomePageVenueUserAdapter(Context context, List<HomePageVenueWhomGoneEntity> mValues) {
        this.context = context;
        if (mValues.size()==0){
            this.mValues=new ArrayList<>();
        }else {
            this.mValues = mValues;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewType==LAST_DATA){
            view = LayoutInflater.from(context).inflate(R.layout.item_rv_homepage_venuedetail_user_gone_last, null);
            view.setLayoutParams(lp);
            return new VenueUserLastHolder(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_rv_homepage_venuedetail_user_gone, null);
            view.setLayoutParams(lp);
            return new VenueUserHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof VenueUserHolder) {
            VenueUserHolder holder = (VenueUserHolder) viewHolder;
            HomePageVenueWhomGoneEntity data=mValues.get(position);
            Picasso.with(context).load(data.getUserPhotoUrl()).into(holder.rivHomePageUserPhoto);
            holder.tvHomePageUserName.setText(data.getUserName());
            //Picasso.with(context).load(data.getUserLevelUrl()).into(holder.ivHomePageUserLevel);//Picasso不能加载？？？？
            holder.ivHomePageUserLevel.setImageResource(Integer.valueOf(data.getUserLevelUrl()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else if (viewHolder instanceof VenueUserLastHolder){
            VenueUserLastHolder holder= (VenueUserLastHolder) viewHolder;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (position==mValues.size()-1){
            return LAST_DATA;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    class VenueUserHolder extends RecyclerView.ViewHolder{
        RoundedImageView rivHomePageUserPhoto;
        TextView tvHomePageUserName;
        ImageView ivHomePageUserLevel;
        public VenueUserHolder(View itemView) {
            super(itemView);
            rivHomePageUserPhoto= (RoundedImageView) itemView.findViewById(R.id.rivHomePageUserPhoto);
            tvHomePageUserName= (TextView) itemView.findViewById(R.id.tvHomePageUserName);
            ivHomePageUserLevel= (ImageView) itemView.findViewById(R.id.ivHomePageUserLevel);
        }
    }
    class VenueUserLastHolder extends RecyclerView.ViewHolder{
        public VenueUserLastHolder(View itemView) {
            super(itemView);
        }
    }
}
