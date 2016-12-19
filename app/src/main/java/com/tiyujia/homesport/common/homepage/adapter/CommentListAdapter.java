package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.entity.CallBackDetailEntity;
import com.tiyujia.homesport.common.homepage.entity.HomePageCommentEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/9/17.1
 */
public class CommentListAdapter extends RecyclerView.Adapter {
    Context context;
    List<HomePageCommentEntity.HomePage.ReplyData> list;
    public static int TYPE_COMMENT=1;
    public CommentListAdapter(Context context, List<HomePageCommentEntity.HomePage.ReplyData> list) {
        this.context = context;
        if (list.size()==0){
            this.list=new ArrayList<>();
        }else {
            this.list = list;
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (viewType==TYPE_COMMENT){
            View viewItem =inflater.inflate(R.layout.item_callback_comment, null);
            viewItem.setLayoutParams(lp);
            return new CommentViewHolder(viewItem);
        }
        View viewItem =inflater.inflate(R.layout.item_rv_callback_detail, null);
        viewItem.setLayoutParams(lp);
        return new CallBackDetailViewHolder(viewItem);
    }
    @Override
    public int getItemViewType(int position) {
        HomePageCommentEntity.HomePage.ReplyData entity= list.get(position);
        if (entity.toUserVo==null||entity.toUserVo.equals("")) {
            return TYPE_COMMENT;
        }
        return super.getItemViewType(position);
    }
    private OnCommentItemClickListener onCommentItemClickListener;
    public void setOnItemClickListener(OnCommentItemClickListener onCommentItemClickListener) {
        this.onCommentItemClickListener = onCommentItemClickListener;
    }
    public interface OnCommentItemClickListener {
        void onCommentItem(int toID,String backTo);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final HomePageCommentEntity.HomePage.ReplyData entity=list.get(position);
        if (viewHolder instanceof CallBackDetailViewHolder){
           final CallBackDetailViewHolder holder=(CallBackDetailViewHolder)viewHolder;
            holder.tvFrom.setText(entity.fromUserVo.nickName);
            holder.tvTo.setText(entity.toUserVo.nickName);
            holder.tvContent.setText(entity.replyContent);
            if(onCommentItemClickListener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCommentItemClickListener.onCommentItem(entity.fromUserVo.id,holder.tvFrom.getText().toString());
                    }
                });
            }
        }else if (viewHolder instanceof CommentViewHolder){
            final CommentViewHolder holder= (CommentViewHolder) viewHolder;
            holder.tvCommenter.setText(entity.fromUserVo.nickName+"：");
            holder.tvCommentText.setText(entity.replyContent);
            if(onCommentItemClickListener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onCommentItemClickListener.onCommentItem(entity.fromUserVo.id,holder.tvCommenter.getText().toString());
                    }
                });
            }
        }

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    class CallBackDetailViewHolder extends RecyclerView.ViewHolder{
        TextView tvFrom;
        TextView tvTo;
        TextView tvContent;
        public CallBackDetailViewHolder(View itemView) {
            super(itemView);
            tvFrom= (TextView) itemView.findViewById(R.id.tvFrom);
            tvTo= (TextView) itemView.findViewById(R.id.tvTo);
            tvContent= (TextView) itemView.findViewById(R.id.tvContent);
        }
    }
    class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView tvCommenter,tvCommentText;
        public CommentViewHolder(View itemView) {
            super(itemView);
            tvCommenter= (TextView) itemView.findViewById(R.id.tvCommenter);
            tvCommentText= (TextView) itemView.findViewById(R.id.tvCommentText);
        }
    }
}
