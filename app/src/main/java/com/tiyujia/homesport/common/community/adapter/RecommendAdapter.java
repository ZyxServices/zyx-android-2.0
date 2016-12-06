package com.tiyujia.homesport.common.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.tiyujia.homesport.common.community.model.RecommendModel;
import com.tiyujia.homesport.common.personal.model.ActiveModel;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 作者: Cymbi on 2016/11/14 15:37.1
 * 邮箱:928902646@qq.com
 */

public class RecommendAdapter extends BaseQuickAdapter<RecommendModel.Recommend> {
    Context context;

    public RecommendAdapter(Context context, List<RecommendModel.Recommend> data) {
        super(R.layout.personal_dynamic_item, data);
        this.context=context;
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, RecommendModel.Recommend recommend) {
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        baseViewHolder.setText(R.id.tvNickname,recommend.userIconVo.nickName)
                .setText(R.id.tvDesc,recommend.topicContent)
                .setText(R.id.tvMsg,recommend.commentCounts+"")
                .setText(R.id.tvZan,recommend.zanCounts+"");
        if(!TextUtils.isEmpty(recommend.local)){
            baseViewHolder.setText(R.id.tvAddress,recommend.local);
        }else {
            baseViewHolder.setText(R.id.tvAddress,"先写一个成都好了");
        }
        PicassoUtil.handlePic(context, PicUtil.getImageUrlDetail(context, StringUtil.isNullAvatar(recommend.userIconVo.avatar), 320, 320),ivAvatar,320,320);
    }
}
