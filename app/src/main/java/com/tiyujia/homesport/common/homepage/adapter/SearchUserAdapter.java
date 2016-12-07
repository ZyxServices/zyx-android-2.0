package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.activity.HomePageWholeSearchActivity;
import com.tiyujia.homesport.common.homepage.entity.SearchUserEntity;
import com.tiyujia.homesport.common.homepage.entity.UserModelEntity;
import com.tiyujia.homesport.util.LvUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/12/5.
 */

public class SearchUserAdapter extends RecyclerView.Adapter implements Filterable {
    private static final int VIEW_TYPE = -1;
    Context context;
    List<SearchUserEntity> values;
    List<SearchUserEntity> mCopyInviteMessages;
    List<SearchUserEntity> inviteMessages;

    public SearchUserAdapter(Context context, List<SearchUserEntity> values) {
        this.context = context;
        if (values.size()!=0){
            this.values = values;
        }else {
            this.values=new ArrayList<>();
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(context).inflate(R.layout.item_rv_user, null);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewItem.setLayoutParams(lp1);
        if (viewType==VIEW_TYPE){
            View view=LayoutInflater.from(context).inflate(R.layout.empty_view, null);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(lp2);
            return new empty(view);
        }
        return new SearchUserHolder(viewItem);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof SearchUserHolder){
            final SearchUserHolder holder= (SearchUserHolder) viewHolder;
            SearchUserEntity entity=values.get(position);
            Picasso.with(context).load(entity.getUserPhotoUrl()).into(holder.rivUserPhoto);
            holder.tvUserName.setText(entity.getUserName());
            UserModelEntity modelEntity=entity.getEntity();
            if (modelEntity==null){
                LvUtil.setLv(holder.ivUserLabel,"初学乍练");
                holder.tvUserText.setText("一条用户最新动态");
            }else {
                LvUtil.setLv(holder.ivUserLabel,modelEntity.getUserLabel());
                holder.tvUserText.setText(modelEntity.getLastDynamic());
            }
            holder.tvUserConcern.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tvUserCancel.setVisibility(View.VISIBLE);
                    holder.tvUserConcern.setVisibility(View.GONE);
                }
            });
            holder.tvUserCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.tvUserConcern.setVisibility(View.VISIBLE);
                    holder.tvUserCancel.setVisibility(View.GONE);
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
    public void setFriends(List<SearchUserEntity> data) {
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
                    final ArrayList<SearchUserEntity> newValues = new ArrayList<SearchUserEntity>();
                    for (int i = 0; i < count; i++) {
                        final SearchUserEntity value = inviteMessages.get(i);
                        String username = value.getUserName();
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
                inviteMessages.addAll((List<SearchUserEntity>) results.values);//将过滤结果添加到这个对象
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
    class SearchUserHolder extends RecyclerView.ViewHolder{
        RoundedImageView rivUserPhoto;
        TextView tvUserName;
        ImageView ivUserLabel;
        TextView tvUserText;
        TextView tvUserCancel;
        TextView tvUserConcern;
        public SearchUserHolder(View itemView) {
            super(itemView);
            rivUserPhoto= (RoundedImageView) itemView.findViewById(R.id.rivUserPhoto);
            tvUserName= (TextView) itemView.findViewById(R.id.tvUserName);
            tvUserText= (TextView) itemView.findViewById(R.id.tvUserText);
            tvUserCancel= (TextView) itemView.findViewById(R.id.tvUserCancel);
            tvUserConcern= (TextView) itemView.findViewById(R.id.tvUserConcern);
            ivUserLabel= (ImageView) itemView.findViewById(R.id.ivUserLabel);
        }
    }
}
