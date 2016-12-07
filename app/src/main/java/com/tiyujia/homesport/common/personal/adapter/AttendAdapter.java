package com.tiyujia.homesport.common.personal.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.ActiveModel;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 作者: Cymbi on 2016/11/14 11:10.
 * 邮箱:928902646@qq.com
 */

public class AttendAdapter extends BaseQuickAdapter<ActiveModel.Active> {
    Context context;
    private SimpleDateFormat sdf =new SimpleDateFormat("MM-dd HH:mm");
    public AttendAdapter(Context context,List<ActiveModel.Active> data) {
        super(R.layout.attend_fragment_item,data);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ActiveModel.Active active) {
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        ImageView ivLv=baseViewHolder.getView(R.id.ivLv);
        ImageView iv_background=baseViewHolder.getView(R.id.iv_background);
        TextView tv_active_lable=baseViewHolder.getView(R.id.tv_active_lable);
        String createTime= sdf.format(active.createTime);
        long lastTime=active.lastTime;
        long currentTime=System.currentTimeMillis();
        if(lastTime>currentTime){
            baseViewHolder.setText(R.id.tv_apply_lable,"正在报名");
        }else {
            baseViewHolder.setText(R.id.tv_apply_lable,"报名已结束");
        }
        int memberPeople=active.memberPeople;
        int maxPeople=active.maxPeople;
        baseViewHolder.setText(R.id.tvNickname,active.user.nickname)
                .setText(R.id.tv_title,active.title)
                .setText(R.id.tv_address,active.city)
                .setText(R.id.tv_msg,active.commentNumber+"")
                .setText(R.id.tvTime,createTime)
                .setText(R.id.tv_apply,"已报名："+memberPeople+" 剩余名额:0人")
                .setText(R.id.tv_zan,active.zan+"");
        if (active.activityType==0){
            tv_active_lable.setText("求约");
        }else if (active.activityType==1){
            tv_active_lable.setText("求带");
        }
        if (active.user.level!=null||active.user.level.equals("")){
            LvUtil.setLv(ivLv,active.user.level.pointDesc);
        }else {}
        PicassoUtil.handlePic(context, PicUtil.getImageUrlDetail(context, StringUtil.isNullAvatar(active.user.avatar), 320, 320),ivAvatar,320,320);
        PicassoUtil.handlePic(context, PicUtil.getImageUrlDetail(context, StringUtil.isNullImage(active.imgUrls), 720, 720),iv_background,720,720);

    }
}
