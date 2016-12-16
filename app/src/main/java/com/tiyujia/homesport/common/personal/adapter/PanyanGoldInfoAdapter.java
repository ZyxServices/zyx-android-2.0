package com.tiyujia.homesport.common.personal.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.PanyanGoldInfoModel;

import java.util.List;

/**
 * 作者: Cymbi on 2016/12/16 10:41.
 * 邮箱:928902646@qq.com
 */

public class PanyanGoldInfoAdapter extends BaseQuickAdapter<PanyanGoldInfoModel> {
    public PanyanGoldInfoAdapter(List<PanyanGoldInfoModel> data) {
        super(R.layout.panyan_coin_info_item, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, PanyanGoldInfoModel Model) {
        TextView tvTitle =(TextView) baseViewHolder.getView(R.id.tvTitle);
        TextView tvTime =(TextView) baseViewHolder.getView(R.id.tvTime);
        TextView tvNumber =(TextView) baseViewHolder.getView(R.id.tvNumber);
        if(Model.operId==1){
            tvTitle.setText("新用户第一次使用 ");
        }else if(Model.operId==2){
            tvTitle.setText("完善资料");
        }else if(Model.operId==4){
            tvTitle.setText("关注");
        }else if(Model.operId==5){
            tvTitle.setText("发布评论");
        }else if(Model.operId==6){
            tvTitle.setText("发布活动");
        }else if(Model.operId==7){
            tvTitle.setText("发布动态");
        }else if(Model.operId==8){
            tvTitle.setText("老带新");
        }else if(Model.operId==9){
            tvTitle.setText("装备秀");
        }else if(Model.operId==10){
            tvTitle.setText("被举报 ");
        }
        tvTime.setText(API.simpleYear.format(Model.createTime));
        tvNumber.setText("+"+Model.coinNum+"");
    }
}
