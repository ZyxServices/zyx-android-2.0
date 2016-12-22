package com.tiyujia.homesport.common.personal.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ff.imagezoomdrag.ImageDetailActivity;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.adapter.NGLAdapter;
import com.tiyujia.homesport.common.personal.model.EquipmentShowModel;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.util.TimeUtil;
import com.w4lle.library.NineGridlayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/7 12:08.
 * 邮箱:928902646@qq.comcxzczcxcxcxz
 */

public class EquipmentShowAdapter extends BaseQuickAdapter<EquipmentShowModel.Model> {
    public EquipmentShowAdapter(List<EquipmentShowModel.Model> data) {
        super(R.layout.personal_equipment_show_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, EquipmentShowModel.Model model) {
        baseViewHolder.setText(R.id.tv_nickname,model.userIconVo.nickName)
        .setText(R.id.desc,"  "+model.content)
        .setText(R.id.tv_msg,model.commentCounts+"")
        .setText(R.id.tv_zan,model.zanCounts+"")
        .setText(R.id.tv_time,API.simpleDateFormat.format(model.createTime)+"发布");
        NineGridlayout nineGrid= baseViewHolder.getView(R.id.nineGrid);
        ImageView ivAvatar= baseViewHolder.getView(R.id.ivAvatar);
        if (model.imgUrl != null) {
            String str = model.imgUrl;
            final ArrayList<String> imgUrls =(ArrayList) StringUtil.stringToList(str);;
            NGLAdapter adapter = new NGLAdapter(mContext, imgUrls);
            nineGrid.setVisibility(View.VISIBLE);
            nineGrid.setGap(6);
            nineGrid.setAdapter(adapter);
            nineGrid.setOnItemClickListerner(new NineGridlayout.OnItemClickListerner() {
                @Override
                public void onItemClick(View view, int position) {
                    mContext.startActivity(ImageDetailActivity.getMyStartIntent(mContext, imgUrls,position, ImageDetailActivity.url_path));
                }
            });
        }else {
            nineGrid.setVisibility(View.GONE);
        }
        PicassoUtil.handlePic(mContext, PicUtil.getImageUrlDetail(mContext, StringUtil.isNullAvatar(model.userIconVo.avatar), 320, 320), ivAvatar, 320, 320);
    }
}
