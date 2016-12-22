package com.tiyujia.homesport.common.community.adapter;

import android.view.View;
import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiyujia.homesport.R;
import java.util.List;

/**
 * 作者: Cymbi on 2016/12/20 20:30.
 * 邮箱:928902646@qq.com
 */

public class CityAddressAdapter extends BaseQuickAdapter<PoiItem> {
    OnClickResultListener onClickResultListener;
    public CityAddressAdapter(List data) {
        super(R.layout.city_address_item, data);
    }
    public interface  OnClickResultListener{
    public void onClickResult(String result);
}
    public void setOnClickResultListener(OnClickResultListener onClickResultListener) {
        this.onClickResultListener = onClickResultListener;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final PoiItem poiItem) {
        baseViewHolder.setText(R.id.tvTitle,poiItem.getTitle())
                .setText(R.id.tvContent,poiItem.getSnippet());
        View view=baseViewHolder.getConvertView();
        if (onClickResultListener!=null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickResultListener.onClickResult(poiItem.getTitle());
                }
            });
        }
    }
}
