package com.tiyujia.homesport.common.personal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.NineGridView;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.AttentionModel;
import com.tiyujia.homesport.entity.ActiveModel;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/15 11:58.
 * 邮箱:928902646@qq.com
 */

public class AttentionAdapter extends  BaseQuickAdapter<AttentionModel.AttentionList> {
Context context;

    public AttentionAdapter(List<AttentionModel.AttentionList> data) {
        super(R.layout.personal_attention_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final AttentionModel.AttentionList attention) {
        baseViewHolder.setText(R.id.tvNickname,attention.nickname)
        .setText(R.id.tvContent,attention.signatures);
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        ImageView ivLv=baseViewHolder.getView(R.id.ivLv);
        TextView tv_not=baseViewHolder.getView(R.id.tv_not);
        tv_not.setVisibility(View.VISIBLE);
       // LvUtil.setLv(ivLv,attention.level.pointDesc);
        PicassoUtil.handlePic(context,PicUtil.getImageUrlDetail(context,StringUtil.isNullAvatar(attention.avatar), 320, 320),ivAvatar,320,320);
    }
}
