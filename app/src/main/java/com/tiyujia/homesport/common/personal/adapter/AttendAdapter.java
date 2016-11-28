package com.tiyujia.homesport.common.personal.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.entity.ActiveModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 作者: Cymbi on 2016/11/14 11:10.
 * 邮箱:928902646@qq.com
 */

public class AttendAdapter extends BaseQuickAdapter<ActiveModel> {
    private SimpleDateFormat sdf =new SimpleDateFormat("MM月-dd日 HH:mm");
    public AttendAdapter(List<ActiveModel> data) {
        super(R.layout.attend_fragment_item,data);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, ActiveModel activeModel) {
        baseViewHolder.setText(R.id.tv_title,activeModel.getTitle())
                .setText(R.id.tv_nickname,activeModel.getNickname())
                .setText(R.id.tv_time,sdf.format(new Date(activeModel.getTime()))+"")
                .setText(R.id.tv_apply_lable,activeModel.getApply_lable())
                .setText(R.id.tv_active_lable,activeModel.getActive_lable())
                .setText(R.id.tv_apply,activeModel.getApply()+"")
                .setText(R.id.tv_award,activeModel.getAward()+"")
                .setText(R.id.tv_address,activeModel.getAddress())
                .setText(R.id.tv_msg,activeModel.getMsg()+"")
                .setText(R.id.tv_zan,activeModel.getZan()+"");

    }
}
