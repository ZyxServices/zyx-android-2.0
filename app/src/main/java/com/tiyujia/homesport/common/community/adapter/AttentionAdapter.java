package com.tiyujia.homesport.common.community.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiyujia.homesport.common.personal.model.AttentionModel;

import java.util.List;

/**
 * 作者: Cymbi on 2016/11/14 15:37.1
 * 邮箱:928902646@qq.com
 */

public class AttentionAdapter extends BaseQuickAdapter<AttentionModel.AttentionList> {
    Context context;


    public AttentionAdapter(List<AttentionModel.AttentionList> data) {
        super(data);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, AttentionModel.AttentionList attentionList) {

    }
}
