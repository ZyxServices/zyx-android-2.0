package com.tiyujia.homesport.common.personal.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.personal.model.MsgModel;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.tiyujia.homesport.util.TimeUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 作者: Cymbi on 2016/11/16 10:58.
 * 邮箱:928902646@qq.com
 */

public class MsgReplyAdapter extends BaseQuickAdapter<MsgModel.Msg> {

    public MsgReplyAdapter(List<MsgModel.Msg> data) {
        super(R.layout.personal_msg_reply_item, data);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, MsgModel.Msg msg) {
        String  s=API.simpleDateFormat.format(msg.createTime);
        Date date = null;
        try {
            date=API.simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        baseViewHolder.setText(R.id.tvNickname,msg.userVo.nickName)
                .setText(R.id.tvTime, TimeUtil.formatFriendly(date))
                .setText(R.id.tvReplyContent,msg.fromContent)
                .setText(R.id.tvTitle,msg.toObject.toTitle);
        if(msg.bodyType==0){
            baseViewHolder.setText(R.id.tvReplyType,"回复了我的文章： ");
        }else if(msg.bodyType==1){
            baseViewHolder.setText(R.id.tvReplyType,"回复了我的动态： ");
        }else if(msg.bodyType==2){
            baseViewHolder.setText(R.id.tvReplyType,"回复了我的装备： ");
        }else if(msg.bodyType==3){
            baseViewHolder.setText(R.id.tvReplyType,"回复了我的活动： ");
        }
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        ImageView ivLv=baseViewHolder.getView(R.id.ivLv);
        if (msg.userVo.level!=null&&msg.userVo.level.equals("")){
            LvUtil.setLv(ivLv,msg.userVo.level.pointDesc);
        }else {
            LvUtil.setLv(ivLv,"初学乍练");
        }
        PicassoUtil.handlePic(mContext, PicUtil.getImageUrlDetail(mContext, StringUtil.isNullAvatar(msg.userVo.avatar), 320, 320),ivAvatar,320,320);
    }
}
