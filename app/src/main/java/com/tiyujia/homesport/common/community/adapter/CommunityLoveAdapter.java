package com.tiyujia.homesport.common.community.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.community.model.CommunityLoveEntity;
import com.tiyujia.homesport.common.personal.activity.PersonalOtherHome;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.StringUtil;

import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/12/21.
 */

public class CommunityLoveAdapter extends BaseQuickAdapter<CommunityLoveEntity.LoveData.LoveUser> {
    public CommunityLoveAdapter(List<CommunityLoveEntity.LoveData.LoveUser> data) {
        super(R.layout.item_rv_homepage_venuedetail_user_gone,data);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, final CommunityLoveEntity.LoveData.LoveUser entity) {
        ImageView ivAvatar=baseViewHolder.getView(R.id.rivHomePageUserPhoto);
        TextView tvName=baseViewHolder.getView(R.id.tvHomePageUserName);
        ImageView ivLevel=baseViewHolder.getView(R.id.ivHomePageUserLevel);
        Picasso.with(mContext).load(StringUtil.isNullAvatar(entity.avatar)).into(ivAvatar);
        tvName.setText(entity.nickName);
        String level=(entity.level==null)?"初学乍练":(entity.level.pointDesc);
        LvUtil.setLv(ivLevel,level);
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, PersonalOtherHome.class);
                int userId=entity.id;
                intent.putExtra("id",userId);
                mContext.startActivity(intent);
            }
        });
    }
}
