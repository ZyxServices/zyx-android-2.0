package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.entity.WhomGoneEntity;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/12/15.
 */

public class MorePeopleGoneAdapter extends BaseQuickAdapter<WhomGoneEntity> {
    public MorePeopleGoneAdapter(List<WhomGoneEntity> data) {
        super(R.layout.personal_attention_item,data);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, final WhomGoneEntity entity) {
        RoundedImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        ImageView ivLv=baseViewHolder.getView(R.id.ivLv);
        Picasso.with(mContext).load(StringUtil.isNullAvatar(entity.avatar)).into(ivAvatar);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, PersonalOtherHome.class);
                intent.putExtra("id",entity.id);
                mContext.startActivity(intent);
            }
        });
        baseViewHolder.setText(R.id.tvNickname,entity.nickName)
        .setText(R.id.tvContent,(entity.authenticate==null)?"该用户很懒，什么也没有留下！":entity.authenticate);
        String text=entity.levelName;
        if (text==null){
            text="";
        }
        LvUtil.setLv(ivLv,LvUtil.setLevelTXT(text));
    }
}
