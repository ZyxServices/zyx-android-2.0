package com.tiyujia.homesport.common.personal.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.MyDynamicModel;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: Cymbi on 2016/11/14 15:37.
 * 邮箱:928902646@qq.com
 */

public class DynamicAdapter extends BaseQuickAdapter<MyDynamicModel> {
    Context context;

    public DynamicAdapter(Context context,List<MyDynamicModel> data) {
        super(R.layout.personal_dynamic_item, data);
        this.context=context;
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, MyDynamicModel dynamicModel) {
        baseViewHolder.setText(R.id.tvNickname,dynamicModel.userIconVo.nickName)
                .setText(R.id.tvAddress,dynamicModel.local)
                .setText(R.id.tvMsg,dynamicModel.commentCounts)
                .setText(R.id.tvDesc,dynamicModel.topicContent)
                .setText(R.id.tvZan,dynamicModel.zanCounts);
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        PicassoUtil.handlePic(context, PicUtil.getImageUrlDetail(context, StringUtil.isNullAvatar(dynamicModel.userIconVo.avatar), 320, 320),ivAvatar,320,320);
    }
}
