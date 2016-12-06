package com.tiyujia.homesport.common.personal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.NGLAdapter;
import com.tiyujia.homesport.common.personal.model.MyDynamicModel;
import com.tiyujia.homesport.entity.GridAdapter;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.w4lle.library.NineGridlayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * 作者: Cymbi on 2016/11/14 15:37.
 * 邮箱:928902646@qq.com
 */

public class DynamicAdapter extends BaseQuickAdapter<MyDynamicModel.Dynamic>{

    public DynamicAdapter( List<MyDynamicModel.Dynamic> data) {
        super(R.layout.personal_dynamic_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MyDynamicModel.Dynamic dynamic) {
        baseViewHolder.setText(R.id.tvNickname, dynamic.userIconVo.nickName)
                .setText(R.id.tvDesc, dynamic.topicContent)
                .setText(R.id.tvMsg, dynamic.commentCounts + "")
                .setText(R.id.tvZan, dynamic.zanCounts + "");
        ImageView ivAvatar = baseViewHolder.getView(R.id.ivAvatar);
        TextView tvTime = baseViewHolder.getView(R.id.tvTime);
        NineGridlayout nineGrid = baseViewHolder.getView(R.id.nineGrid);
        tvTime.setText(API.simpleDateFormat.format(dynamic.createTime));
        PicassoUtil.handlePic(mContext, PicUtil.getImageUrlDetail(mContext, StringUtil.isNullAvatar(dynamic.userIconVo.avatar), 320, 320), ivAvatar, 320, 320);
        if (dynamic.imgUrl != null) {
            String str = dynamic.imgUrl;
            ArrayList<String> imgUrls = new ArrayList<>();
            if (str.contains(",")) {
                String[] s = str.split(",");
                for (String s1 : s) {
                    imgUrls.add(API.PICTURE_URL + s1);
                }
            }
            NGLAdapter adapter = new NGLAdapter(mContext, imgUrls);
            nineGrid.setVisibility(View.VISIBLE);
            nineGrid.setGap(6);
            nineGrid.setAdapter(adapter);
        }
    }}
