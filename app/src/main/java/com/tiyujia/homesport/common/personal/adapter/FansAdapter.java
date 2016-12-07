package com.tiyujia.homesport.common.personal.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.AttentionModel;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.util.List;

/**
 * 作者: Cymbi on 2016/11/15 11:58.
 * 邮箱:928902646@qq.com
 */

public class FansAdapter extends BaseQuickAdapter<AttentionModel.AttentionList> {
Context context;
    public FansAdapter(Context context,List<AttentionModel.AttentionList> data) {
        super(R.layout.personal_attention_item, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AttentionModel.AttentionList attentionList) {
        baseViewHolder.setText(R.id.tvNickname,attentionList.nickname)
                .setText(R.id.tvContent,attentionList.signatures);
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        ImageView ivLv=baseViewHolder.getView(R.id.ivLv);
        TextView tv_yes=baseViewHolder.getView(R.id.tv_yes);
        tv_yes.setVisibility(View.VISIBLE);
        if (attentionList.level!=null&&attentionList.level.equals("")){
            LvUtil.setLv(ivLv,attentionList.level.pointDesc);
        }else {
            LvUtil.setLv(ivLv,"初学乍练");
        }
        PicassoUtil.handlePic(context, PicUtil.getImageUrlDetail(context, StringUtil.isNullAvatar(attentionList.avatar), 320, 320),ivAvatar,320,320);
    }
}
