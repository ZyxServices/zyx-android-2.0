package com.tiyujia.homesport.common.homepage.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.activity.HomePageSearchResultActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageVenueSurveyActivity;
import com.tiyujia.homesport.common.homepage.dao.DBVenueContext;
import com.tiyujia.homesport.common.homepage.entity.HomePageRecentVenueEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageSearchEntity;
import com.tiyujia.homesport.util.DegreeUtil;
import com.tiyujia.homesport.util.TypeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zzqybyb19860112 on 2016/11/11.1
 */

public class HomePageRecentVenueAdapter extends RecyclerView.Adapter implements Filterable {
    Context context;
    List<HomePageRecentVenueEntity> values;
    List<HomePageRecentVenueEntity> mCopyInviteMessages;
    List<HomePageRecentVenueEntity> inviteMessages;
    private static final int VIEW_TYPE = -1;
    public                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            HomePageRecentVenueAdapter(Context context, List<HomePageRecentVenueEntity> values) {
        if (values.size()!=0){
            this.values = values;
        }else {
            this.values=new ArrayList<>();
        }
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(context).inflate(R.layout.item_rv_recent_venue, null);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewItem.setLayoutParams(lp1);
        if (viewType==VIEW_TYPE){
            View view=LayoutInflater.from(context).inflate(R.layout.empty_view, null);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(lp2);
            return new empty(view);
        }
        return new RecentVenueHolder(viewItem);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof RecentVenueHolder) {
            RecentVenueHolder holder = (RecentVenueHolder) viewHolder;
            final HomePageRecentVenueEntity data = values.get(position);
            List<String> urls=data.getImgUrls();
            if (urls!=null&&!urls.equals("")&&!urls.equals("null")&&urls.size()!=0){
                Picasso.with(context).load(data.getImgUrls().get(0)).into(holder.ivPicVenue);
            }else {
                int []demo=new int[]{R.drawable.demo_05,R.drawable.demo_06,R.drawable.demo_09,R.drawable.demo_10};
                Picasso.with(context).load(demo[new Random().nextInt(4)]).into(holder.ivPicVenue);
            }
            holder.tvVenueName.setText(data.getName());
            int type = data.getType();
            String typeA=type==1?"室内":"室外";
            holder.tvVenueTypeA.setText(typeA);
            holder.tvVenueTypeB.setText("抱石");
            TypeUtil.handleType(holder.tvVenueTypeA, typeA);
            int degree = data.getLevel();
            DegreeUtil.handleDegrees(degree, holder.ivDegree1, holder.ivDegree2, holder.ivDegree3, holder.ivDegree4, holder.ivDegree5);
            holder.tvGoneNumber.setText(data.getPnumber() + "人去过");
            holder.tvTalkNumber.setText(data.getTalkNumber() + "");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        String searchData = HomePageVenueSurveyActivity.getSearchText();
                        if (!searchData.equals("") && searchData != null) {
                            DBVenueContext dbVenueContext = new DBVenueContext(context);
                            List<HomePageSearchEntity> list = dbVenueContext.query();
                            List<String> texts = new ArrayList<String>();
                            for (HomePageSearchEntity entity : list) {
                                String searchText = entity.getSearchText();
                                texts.add(searchText);
                            }
                            if (!texts.contains(searchData)) {
                                ContentValues value = new ContentValues();
                                value.put("content", HomePageVenueSurveyActivity.getSearchText());
                                dbVenueContext.insert(value);
                        }
                        }
                    Intent intent=new Intent(context, HomePageSearchResultActivity.class);
                    intent.putExtra("venueId",data.getId());
                    context.startActivity(intent);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return values.size() > 0 ? values.size() : 1;
    }
    @Override
    public int getItemViewType(int position) {
        if (values.size() <= 0) {
            return VIEW_TYPE;
        }
        return 0;
    }
    public void setFriends(List<HomePageRecentVenueEntity> data) {
        //复制数据
        mCopyInviteMessages = new ArrayList<>();
        this.mCopyInviteMessages.addAll(data);
        this.inviteMessages = data;
        this.notifyDataSetChanged();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //初始化过滤结果对象
                FilterResults results = new FilterResults();
                //假如搜索为空的时候，将复制的数据添加到原始数据，用于继续过滤操作
                if (results.values == null) {
                    values.clear();
                    values.addAll(mCopyInviteMessages);
                }
                //关键字为空的时候，搜索结果为复制的结果
                if (constraint == null || constraint.length() == 0) {
                    results.values = mCopyInviteMessages;
                    results.count = mCopyInviteMessages.size();
                } else {
                    String searchText= HomePageVenueSurveyActivity.getSearchText();
                    String prefixString;
                    if (searchText.equals("")){
                        prefixString=searchText.toString();
                    }else {
                        prefixString= constraint.toString();
                    }
                    final int count = inviteMessages.size();
                    //用于存放暂时的过滤结果
                    final ArrayList<HomePageRecentVenueEntity> newValues = new ArrayList<HomePageRecentVenueEntity>();
                    for (int i = 0; i < count; i++) {
                        final HomePageRecentVenueEntity value = inviteMessages.get(i);
                        String username = value.getName();
                        // First match against the whole ,non-splitted value，假如含有关键字的时候，添加
                        if (username.contains(prefixString)) {
                            newValues.add(value);
                        } else {
                            //过来空字符开头
                            final String[] words = username.split(" ");
                            final int wordCount = words.length;
                            // Start at index 0, in case valueText starts with space(s)
                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].contains(prefixString)) {
                                    newValues.add(value);
                                    break;
                                }
                            }
                        }
                    }
                    results.values = newValues;
                    results.count = newValues.size();
                }
                return results;//过滤结果
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                inviteMessages.clear();//清除原始数据
                inviteMessages.addAll((List<HomePageRecentVenueEntity>) results.values);//将过滤结果添加到这个对象
                if (results.count > 0) {
                    notifyDataSetChanged();//有关键字的时候刷新数据
                } else {
                    //关键字不为零但是过滤结果为空刷新数据
                    if (constraint.length() != 0) {
                        notifyDataSetChanged();
                        return;
                    }
                    //加载复制的数据，即为最初的数据
                    setFriends(mCopyInviteMessages);
                }
            }
        };
    }
    class empty extends RecyclerView.ViewHolder{
        public empty(View itemView) {
            super(itemView);
        }
    }
    class RecentVenueHolder extends RecyclerView.ViewHolder{
        ImageView ivPicVenue,ivDegree1,ivDegree2,ivDegree3,ivDegree4,ivDegree5;
        TextView tvVenueName,tvVenueTypeA,tvVenueTypeB,tvGoneNumber,tvTalkNumber;
        public RecentVenueHolder(View itemView) {
            super(itemView);
            ivPicVenue= (ImageView) itemView.findViewById(R.id.ivPicVenue);
            ivDegree1= (ImageView) itemView.findViewById(R.id.ivDegree1);
            ivDegree2= (ImageView) itemView.findViewById(R.id.ivDegree2);
            ivDegree3= (ImageView) itemView.findViewById(R.id.ivDegree3);
            ivDegree4= (ImageView) itemView.findViewById(R.id.ivDegree4);
            ivDegree5= (ImageView) itemView.findViewById(R.id.ivDegree5);
            tvVenueName= (TextView) itemView.findViewById(R.id.tvVenueName);
            tvVenueTypeA= (TextView) itemView.findViewById(R.id.tvVenueTypeA);
            tvVenueTypeB= (TextView) itemView.findViewById(R.id.tvVenueTypeB);
            tvGoneNumber= (TextView) itemView.findViewById(R.id.tvGoneNumber);
            tvTalkNumber= (TextView) itemView.findViewById(R.id.tvTalkNumber);
        }
    }
}
