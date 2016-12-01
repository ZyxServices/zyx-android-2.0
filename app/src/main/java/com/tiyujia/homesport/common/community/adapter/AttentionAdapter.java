package com.tiyujia.homesport.common.community.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
