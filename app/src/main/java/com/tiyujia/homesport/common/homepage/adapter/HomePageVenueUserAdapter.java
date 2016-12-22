package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.activity.MorePeopleGoneActivity;
import com.tiyujia.homesport.common.homepage.entity.HomePageVenueWhomGoneEntity;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.util.EmptyViewHolder;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/11/22.1
 */

public class HomePageVenueUserAdapter extends RecyclerView.Adapter {
    Context context;
    List<HomePageVenueWhomGoneEntity> mValues;
    private static final int LAST_DATA=1;
    private static final int VIEW_TYPE = -1;
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
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if(VIEW_TYPE==viewType){
            view=LayoutInflater.from(context).inflate(R.layout.small_empty_view,parent, false);
            view.setLayoutParams(lp2);
            return new EmptyViewHolder(view);
        }else {
            if (viewType==LAST_DATA){
                view = LayoutInflater.from(context).inflate(R.layout.item_rv_homepage_venuedetail_user_gone_last, null);
                view.setLayoutParams(lp1);
                return new VenueUserLastHolder(view);
            }else {
                view = LayoutInflater.from(context).inflate(R.layout.item_rv_homepage_venuedetail_user_gone, null);
                view.setLayoutParams(lp1);
                return new VenueUserHolder(view);
            }
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof VenueUserHolder) {
            VenueUserHolder holder = (VenueUserHolder) viewHolder;
            final HomePageVenueWhomGoneEntity data=mValues.get(position);
            String url=data.getUserPhotoUrl();
            if (url.contains("http")){
                PicassoUtil.showImage(context,0,url,holder.rivHomePageUserPhoto);
            }else {
               int localUrl=Integer.valueOf(url);
                PicassoUtil.showImage(context,localUrl,"",holder.rivHomePageUserPhoto);
            }
            holder.tvHomePageUserName.setText(data.getUserName());
            LvUtil.setLv(holder.ivHomePageUserLevel,data.getUserLevelUrl());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, PersonalOtherHome.class);
                    int userId=data.getUserId();
                    intent.putExtra("id",userId);
                    context.startActivity(intent);
                }
            });
        }else if (viewHolder instanceof VenueUserLastHolder){
            VenueUserLastHolder holder= (VenueUserLastHolder) viewHolder;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, MorePeopleGoneActivity.class);
                    intent.putExtra("venueId",mValues.get(0).getVenueId());
                    context.startActivity(intent);
                }
            });
        }else if (viewHolder instanceof EmptyViewHolder){
            EmptyViewHolder emptyHolder= (EmptyViewHolder) viewHolder;
            emptyHolder.tvEmpty.setText("暂时没有人去过该场馆");
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (mValues.size() <= 0) {
            return VIEW_TYPE;
        }else if (position==mValues.size()-1&&mValues.size()==5){
            return LAST_DATA;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size() > 0 ? mValues.size() : 1;
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
