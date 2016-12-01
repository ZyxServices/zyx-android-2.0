package com.tiyujia.homesport.common.personal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.MyDynamicModel;
import com.tiyujia.homesport.entity.GridAdapter;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * 作者: Cymbi on 2016/11/14 15:37.
 * 邮箱:928902646@qq.com
 */

public class DynamicAdapter extends RecyclerView.Adapter{
    Context context;
    List<MyDynamicModel> list;

    public DynamicAdapter(Context context, List<MyDynamicModel> list) {
        this.context = context;
        if (list.size()==0){
            this.list=new ArrayList<>();
        }else {
            this.list = list;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View view=  mInflater.inflate(R.layout.personal_dynamic_item,null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new Holder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof Holder){
            final Holder viewHolder=(Holder)holder;
            MyDynamicModel model =list.get(position);
            viewHolder.tvNickname.setText(model.getUserIconVo().getNickName());
            viewHolder.tvAddress.setText(model.getLocal());
            viewHolder.tvDesc.setText(model.getTopicContent());
            viewHolder.tvMsg.setText(model.getCommentCounts()+"");
            viewHolder.tvZan.setText(model.getZanCounts()+"");
            if(model.getImageUrl()!=null){
                ArrayList<String> urls =model.getImageUrl();
                GridAdapter adapter=new GridAdapter(context,urls);
                viewHolder.gridview.setAdapter(adapter);
                viewHolder.gridview.invalidate();
            }else {}

            PicassoUtil.handlePic(context, PicUtil.getImageUrlDetail(context, StringUtil.isNullAvatar(model.getUserIconVo().getAvatar()), 320, 320),viewHolder.ivAvatar,320,320);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class Holder extends RecyclerView.ViewHolder{
        private final TextView tvNickname,tvZan,tvDesc,tvAddress,tvMsg,tvTime;
        private final GridView gridview;
        private final ImageView ivAvatar;

        public Holder(View itemView) {
            super(itemView);
            tvNickname=(TextView)itemView.findViewById(R.id.tvNickname);
            tvDesc=(TextView)  itemView.findViewById(R.id.tvDesc);
            gridview=(GridView)  itemView.findViewById(R.id.gridview);
            tvAddress=(TextView)  itemView.findViewById(R.id.tvAddress);
            tvMsg=(TextView)  itemView.findViewById(R.id.tvMsg);
            tvZan=(TextView)  itemView.findViewById(R.id.tvZan);
            tvTime=(TextView)  itemView.findViewById(R.id.tvTime);
            ivAvatar=(ImageView)  itemView.findViewById(R.id.ivAvatar);


        }
    }
}
