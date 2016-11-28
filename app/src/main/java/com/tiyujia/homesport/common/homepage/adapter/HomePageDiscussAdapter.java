package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.entity.CallBackDetailEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageDiscussEntity;
import com.w4lle.library.NineGridAdapter;
import com.w4lle.library.NineGridlayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/11/22.1
 */

public class HomePageDiscussAdapter  extends RecyclerView.Adapter{
    Context context;
    List<HomePageDiscussEntity> mValues;
    public HomePageDiscussAdapter(Context context, List<HomePageDiscussEntity> mValues) {
        this.context = context;
        if (mValues.size()==0){
            this.mValues=new ArrayList<>();
        }else {
            this.mValues = mValues;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(context).inflate(R.layout.item_rv_homepage_venuedetail_user_say, null);
        view.setLayoutParams(lp);
        return new VenueDetailDiscussHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof VenueDetailDiscussHolder) {
            VenueDetailDiscussHolder holder = (VenueDetailDiscussHolder) viewHolder;
            HomePageDiscussEntity data=mValues.get(position);
            Picasso.with(context).load(data.getMainUserPhotoUrl()).into(holder.rivMainUserPhoto);
            holder.tvMainUserName.setText(data.getMainUserName());
            holder.ivMainUserLevel.setImageResource(Integer.valueOf(data.getMainUserLevelUrl()));
            holder.tvTalkTime.setText(data.getMainUserSendTime());
            holder.tvTalkContent.setText(data.getMainUserSendContent());
            List<String> picUrls=data.getMainUserSendPicUrlList();
            if (picUrls!=null&&picUrls.size()!=0){
                NGLAdapter adapter = new NGLAdapter(context, picUrls);
                holder.nglMainUserImage.setVisibility(View.VISIBLE);
                holder.nglMainUserImage.setGap(6);
                holder.nglMainUserImage.setAdapter(adapter);
            }else {
                holder.nglMainUserImage.setVisibility(View.GONE);
            }
            List<CallBackDetailEntity> discussList=data.getDiscussList();
            if (discussList!=null&&discussList.size()!=0){
                holder.rvDiscussCallBack.setVisibility(View.VISIBLE);
                holder.rvDiscussCallBack.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
                CallBackDetailAdapter rvAdapter=new CallBackDetailAdapter(context,discussList);
                holder.rvDiscussCallBack.setAdapter(rvAdapter);
            }else {
                holder.rvDiscussCallBack.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }
    class VenueDetailDiscussHolder extends RecyclerView.ViewHolder{
        RoundedImageView rivMainUserPhoto;
        TextView tvMainUserName;
        TextView tvTalkTime;
        TextView tvTalkContent;
        ImageView ivMainUserLevel;
        NineGridlayout nglMainUserImage;
        RecyclerView rvDiscussCallBack;
        public VenueDetailDiscussHolder(View itemView) {
            super(itemView);
            rivMainUserPhoto= (RoundedImageView) itemView.findViewById(R.id.rivMainUserPhoto);
            tvMainUserName= (TextView) itemView.findViewById(R.id.tvMainUserName);
            tvTalkTime= (TextView) itemView.findViewById(R.id.tvTalkTime);
            tvTalkContent= (TextView) itemView.findViewById(R.id.tvTalkContent);
            ivMainUserLevel= (ImageView) itemView.findViewById(R.id.ivMainUserLevel);
            nglMainUserImage= (NineGridlayout) itemView.findViewById(R.id.nglMainUserImage);
            rvDiscussCallBack= (RecyclerView) itemView.findViewById(R.id.rvDiscussCallBack);
        }
    }
   private class NGLAdapter extends NineGridAdapter {

        public NGLAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public int getCount() {
            return (list == null) ? 0 : list.size();
        }

        @Override
        public String getUrl(int position) {
            return (String)getItem(position);
        }

        @Override
        public Object getItem(int position) {
            return (list == null) ? null : list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

       @Override
       public View getView(int i, View view) {
           ImageView iv = new ImageView(context);
           iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
           Picasso.with(context).load(getUrl(i)).into(iv);
           return iv;
       }
    }
}
