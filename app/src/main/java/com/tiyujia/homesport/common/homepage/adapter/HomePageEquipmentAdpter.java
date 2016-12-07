package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiyujia.homesport.API;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.common.homepage.activity.HomePageEquipmentInfo;
import com.tiyujia.homesport.common.homepage.entity.EquipmentModel;
import com.tiyujia.homesport.common.personal.model.ActiveModel;
import com.tiyujia.homesport.util.LvUtil;
import com.tiyujia.homesport.util.PicUtil;
import com.tiyujia.homesport.util.PicassoUtil;
import com.tiyujia.homesport.util.StringUtil;
import com.w4lle.library.NineGridlayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 作者: Cymbi on 2016/11/17 17:39.
 * 邮箱:928902646@qq.com1
 */

public class HomePageEquipmentAdpter extends BaseQuickAdapter<EquipmentModel.Equipment> {
    Context context;
    public HomePageEquipmentAdpter(Context context, List<EquipmentModel.Equipment> data) {
        super(R.layout.homepage_equipment_item, data);
        this.context=context;
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, final EquipmentModel.Equipment equipment) {
        baseViewHolder.setText(R.id.tvNickname,equipment.userIconVo.nickName)
                .setText(R.id.tvTitle,equipment.title)
                .setText(R.id.tMsg,equipment.commentCounts+"")
                .setText(R.id.tZan,equipment.zanCounts+"");
        String time= API.simpleDateFormat.format(equipment.createTime);
        TextView tvTime=baseViewHolder.getView(R.id.tvTime);
        TextView tvLabel=baseViewHolder.getView(R.id.tvLabel);
        tvTime.setText(time);
        ImageView ivAvatar=baseViewHolder.getView(R.id.ivAvatar);
        ImageView ivLv=baseViewHolder.getView(R.id.ivLv);
        NineGridlayout nineGrid=baseViewHolder.getView(R.id.nineGrid);
        if(equipment.imgUrl!=null){
            String str= equipment.imgUrl;
            ArrayList<String> imgUrls=new ArrayList<>();
            if(str.contains(",")){
                String[] s=str.split(",");
                for(String s1:s){
                    imgUrls.add(API.PICTURE_URL+s1);
                }
            }
            NGLAdapter adapter = new NGLAdapter(context, imgUrls);
            nineGrid.setVisibility(View.VISIBLE);
            nineGrid.setGap(6);
            nineGrid.setAdapter(adapter);
        }else {}

        if( equipment.labelId==1){
            tvLabel.setText("鞋子");
        }
        if( equipment.labelId==2){
            tvLabel.setText("动力绳");
        }
        if( equipment.labelId==3){
            tvLabel.setText("头盔");
        }
        if( equipment.labelId==4){
            tvLabel.setText("主锁");
        }
        if(equipment.userIconVo.level!=null){
            LvUtil.setLv(ivLv,equipment.userIconVo.level.pointDesc);
        }else {
            LvUtil.setLv(ivLv,"初学乍练");
        }
        PicassoUtil.handlePic(context, PicUtil.getImageUrlDetail(context, StringUtil.isNullAvatar(equipment.userIconVo.avatar), 320, 320),ivAvatar,320,320);
       View view= baseViewHolder.getConvertView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext,HomePageEquipmentInfo.class);
                i.putExtra("id",equipment.id);
                mContext.startActivity(i);
            }
        });
    }
}
