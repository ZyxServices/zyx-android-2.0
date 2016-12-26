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

import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.activity.CommunityDynamicDetailActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageSearchResultActivity;
import com.tiyujia.homesport.common.homepage.activity.HomePageWholeSearchActivity;
import com.tiyujia.homesport.common.homepage.entity.SearchDynamicEntity;
import com.tiyujia.homesport.common.homepage.entity.SearchVenueEntity;
import com.tiyujia.homesport.util.DegreeUtil;
import com.tiyujia.homesport.util.TypeUtil;
import com.w4lle.library.NineGridlayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.tiyujia.homesport.R.id.nglDynamicImages;

/**
 * Created by zzqybyb19860112 on 2016/12/5.
 */

public class SearchVenueAdapter extends RecyclerView.Adapter implements Filterable {
    private static final int VIEW_TYPE = -1;
    Context context;
    List<SearchVenueEntity> values;
    List<SearchVenueEntity> mCopyInviteMessages;
    List<SearchVenueEntity> inviteMessages;

    public SearchVenueAdapter(Context context, List<SearchVenueEntity> values) {
        this.context = context;
        if (values.size()!=0){
            this.values = values;
        }else {
            this.values=new ArrayList<>();
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(context).inflate(R.layout.item_rv_venue, null);
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewItem.setLayoutParams(lp1);
        if (viewType==VIEW_TYPE){
            View view=LayoutInflater.from(context).inflate(R.layout.empty_view, null);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(lp2);
            return new empty(view);
        }
        return new SearchVenueHolder(viewItem);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof SearchVenueHolder){
            SearchVenueHolder holder= (SearchVenueHolder) viewHolder;
            final SearchVenueEntity entity=values.get(position);
            String url=entity.getVenuePicture();
            if (url!=null&&!url.equals("")&&!url.equals("null")){
                Picasso.with(context).load(url).into(holder.ivVenueSearchPic);
            }else {
                int []demo=new int[]{R.drawable.demo_05,R.drawable.demo_06,R.drawable.demo_09,R.drawable.demo_10};
                Picasso.with(context).load(demo[new Random().nextInt(4)]).into(holder.ivVenueSearchPic);
            }
            holder.tvVenueSearchName.setText(entity.getVenueName());
            int type = entity.getVenueType();
            String typeA=type==1?"室内":"室外";
            holder.tvVenueSearchTypeA.setText(typeA);
            TypeUtil.handleType(holder.tvVenueSearchTypeA, typeA);
            int degree = entity.getVenueDegree();
            DegreeUtil.handleDegrees(degree, holder.ivDegree1, holder.ivDegree2, holder.ivDegree3, holder.ivDegree4, holder.ivDegree5);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, HomePageSearchResultActivity.class);
                    intent.putExtra("venueId",entity.getVenueId());
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
    public void setFriends(List<SearchVenueEntity> data) {
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
                    final ArrayList<SearchVenueEntity> newValues = new ArrayList<SearchVenueEntity>();
                    for (int i = 0; i < count; i++) {
                        final SearchVenueEntity value = inviteMessages.get(i);
                        String username = value.getVenueName();
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
                inviteMessages.addAll((List<SearchVenueEntity>) results.values);//将过滤结果添加到这个对象
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
    class SearchVenueHolder extends RecyclerView.ViewHolder{
        ImageView ivVenueSearchPic;
        ImageView ivDegree1;
        ImageView ivDegree2;
        ImageView ivDegree3;
        ImageView ivDegree4;
        ImageView ivDegree5;
        TextView tvVenueSearchName;//场馆名称
        TextView tvVenueSearchTypeA;//场馆类型
        public SearchVenueHolder(View itemView) {
            super(itemView);
            ivVenueSearchPic= (ImageView) itemView.findViewById(R.id.ivVenueSearchPic);
            ivDegree1= (ImageView) itemView.findViewById(R.id.ivDegree1);
            ivDegree2= (ImageView) itemView.findViewById(R.id.ivDegree2);
            ivDegree3= (ImageView) itemView.findViewById(R.id.ivDegree3);
            ivDegree4= (ImageView) itemView.findViewById(R.id.ivDegree4);
            ivDegree5= (ImageView) itemView.findViewById(R.id.ivDegree5);
            tvVenueSearchName= (TextView) itemView.findViewById(R.id.tvVenueSearchName);
            tvVenueSearchTypeA= (TextView) itemView.findViewById(R.id.tvVenueSearchTypeA);
        }
    }
}
