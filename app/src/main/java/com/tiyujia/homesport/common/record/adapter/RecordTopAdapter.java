package com.tiyujia.homesport.common.record.adapter;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiyujia.homesport.R;
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
    protected void convert(BaseViewHolder baseViewHolder, TopModel.Top top) {
        baseViewHolder.setText(R.id.tvNickname,top.userIconVo.nickName)
        .setText(R.id.tvRankNum,top.rankNum+"");
        TextView tvTotalScore=baseViewHolder.getView(R.id.tvTotalScore);
        if (top.totalScore!=null&&top.totalScore.equals("")){
            tvTotalScore.setText(top.totalScore+"");
        }else {
            tvTotalScore.setText("0");
        }
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        ImageView ivLv=baseViewHolder.getView(R.id.ivLv);
        if (top.userIconVo.level!=null&&top.userIconVo.level.equals("")){
            LvUtil.setLv(ivLv,top.userIconVo.level.pointDesc);
        }else {
            LvUtil.setLv(ivLv,"初学乍练");
        }
        PicassoUtil.handlePic(mContext, PicUtil.getImageUrlDetail(mContext, StringUtil.isNullAvatar(top.userIconVo.avatar), 320, 320),ivAvatar,320,320);
    }
}
