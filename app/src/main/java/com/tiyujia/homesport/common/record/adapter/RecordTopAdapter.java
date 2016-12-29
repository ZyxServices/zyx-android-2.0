package com.tiyujia.homesport.common.record.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.common.record.model.TopModel;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.util.List;

/**
 * 作者: Cymbi on 2016/12/12 11:11.
 * 邮箱:928902646@qq.com
 */

public class RecordTopAdapter extends BaseQuickAdapter<TopModel.Top> {
    public RecordTopAdapter(List<TopModel.Top> data) {
        super(R.layout.record_top_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final TopModel.Top top) {
        baseViewHolder.setText(R.id.tvNickname,top.userIconVo.nickName);
        TextView tvRankNum=baseViewHolder.getView(R.id.tvRankNum);
        if(baseViewHolder.getAdapterPosition()==0){
            tvRankNum.setBackgroundResource(R.mipmap.one);
            tvRankNum.setText("");
        }else if(baseViewHolder.getAdapterPosition()==1){
            tvRankNum.setBackgroundResource(R.mipmap.two);
            tvRankNum.setText("");
        }else if(baseViewHolder.getAdapterPosition()==2){
            tvRankNum.setBackgroundResource(R.mipmap.three);
            tvRankNum.setText("");
        }else{
            tvRankNum.setText("  "+top.rankNum+"");
        }
        TextView tvTotalScore=baseViewHolder.getView(R.id.tvTotalScore);
        if (top.totalScore==null||top.totalScore.equals("")){
            tvTotalScore.setText("0");
        }else {
            tvTotalScore.setText(top.totalScore+"");
        }
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        ImageView ivLv=baseViewHolder.getView(R.id.ivLv);
        if (top.userIconVo.level!=null&&top.userIconVo.level.equals("")){
            LvUtil.setLv(ivLv,top.userIconVo.level.pointDesc);
        }else {
            LvUtil.setLv(ivLv,"初学乍练");
        }
        PicassoUtil.handlePic(mContext, PicUtil.getImageUrlDetail(mContext, StringUtil.isNullAvatar(top.userIconVo.avatar), 320, 320),ivAvatar,320,320);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(mContext, PersonalOtherHome.class);
                i.putExtra("id",top.userId);
                mContext.startActivity(i);
            }
        });
    }
}
