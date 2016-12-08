package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.activity.HomePageArticleActivity;
import com.tiyujia.homesport.common.homepage.entity.CurseModel;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.util.List;

/**
 * 作者: Cymbi on 2016/12/6 11:25.
 * 邮箱:928902646@qq.com
 */

public class CourseAdapter extends BaseQuickAdapter<CurseModel.Curse> {
    Context context;
    public CourseAdapter(Context context, List<CurseModel.Curse> data) {
        super(R.layout.homepage_curse_item, data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final CurseModel.Curse curse) {
        ImageView ivBack=baseViewHolder.getView(R.id.ivBack);
        baseViewHolder.setText(R.id.tvTitle,curse.title)
                .setText(R.id.tvLabel,curse.labelName)
                .setText(R.id.tMsg,curse.commentNumber+"");
        PicassoUtil.handlePic(context, PicUtil.getImageUrlDetail(context, StringUtil.isNullImage(curse.imgUrls), 1280,720 ),ivBack,1280,720);
       View view= baseViewHolder.getConvertView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,HomePageArticleActivity.class);
                i.putExtra("id",curse.id);
                context.startActivity(i);
            }
        });

    }
}
