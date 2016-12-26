package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.activity.CommunityDynamicDetailActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageDateInfo;
import com.tiyujia.homesport.common.homepage.activity.HomePageWholeSearchActivity;
import com.tiyujia.homesport.common.homepage.entity.SearchActiveEntity;
import com.tiyujia.homesport.util.PicassoUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/12/5.
 */

public class SearchActiveAdapter extends RecyclerView.Adapter implements Filterable {
    private static final int VIEW_TYPE = -1;
    Context context;
    List<SearchActiveEntity> values;
    List<SearchActiveEntity> mCopyInviteMessages;
    List<SearchActiveEntity> inviteMessages;

    public SearchActiveAdapter(Context context, List<SearchActiveEntity> values) {
        this.context = context;
        if (values.size()!=0){
            this.values = values;
        }else {
            this.values=new ArrayList<>();
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(context).inflate(R.layout.item_rv_active, null);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewItem.setLayoutParams(lp1);
        if (viewType==VIEW_TYPE){
            View view=LayoutInflater.from(context).inflate(R.layout.empty_view, null);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(lp2);
            return new empty(view);
        }
        return new SearchActiveHolder(viewItem);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof SearchActiveHolder){
            SearchActiveHolder holder= (SearchActiveHolder) viewHolder;
            final SearchActiveEntity entity=values.get(position);
            PicassoUtil.handlePic(context,entity.getImageUrl(),holder.ivActiveBgImg,240,180);
            holder.tvActiveType.setText(entity.getType()+"");
            holder.tvActiveTitle.setText(entity.getTitle()+"");
            holder.tvActiveRest.setText("已报名:"+entity.getAlreadyRegistered()+"  剩余名额:"+entity.getRestNumber());
            holder.tvActiveAward.setText("奖励:"+entity.getReward()+"元");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, HomePageDateInfo.class);
                    intent.putExtra("id",entity.getActiveId());
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
    public void setFriends(List<SearchActiveEntity> data) {
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
                    String searchText= HomePageWholeSearchActivity.getSearchText();
                    String prefixString;
                    if (searchText.equals("")){
                        prefixString=searchText.toString();
                    }else {
                        prefixString= constraint.toString();
                    }
                    final int count = inviteMessages.size();
                    //用于存放暂时的过滤结果
                    final ArrayList<SearchActiveEntity> newValues = new ArrayList<SearchActiveEntity>();
                    for (int i = 0; i < count; i++) {
                        final SearchActiveEntity value = inviteMessages.get(i);
                        String username = value.getTitle();
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
                inviteMessages.addAll((List<SearchActiveEntity>) results.values);//将过滤结果添加到这个对象
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
    class SearchActiveHolder extends RecyclerView.ViewHolder{
        ImageView ivActiveBgImg;//展示图片
        TextView tvActiveType;//活动类型（求带）
        TextView tvActiveTitle;//标题（找个老岩友带带我）
        TextView tvActiveRest;//剩余名额（已报名:0 剩余名额:1）
        TextView tvActiveAward;//奖励（奖励:200块）
        public SearchActiveHolder(View itemView) {
            super(itemView);
            ivActiveBgImg= (ImageView) itemView.findViewById(R.id.ivActiveBgImg);
            tvActiveType= (TextView) itemView.findViewById(R.id.tvActiveType);
            tvActiveTitle= (TextView) itemView.findViewById(R.id.tvActiveTitle);
            tvActiveRest= (TextView) itemView.findViewById(R.id.tvActiveRest);
            tvActiveAward= (TextView) itemView.findViewById(R.id.tvActiveAward);
        }
    }
}
