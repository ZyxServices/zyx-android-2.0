package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.w4lle.library.NineGridAdapter;

import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/12/1.
 */

public class NGLAdapter extends NineGridAdapter {

    public NGLAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public int getCount() {
        return (list == null) ? 0 : list.size();
    }

    @Override
    public String getUrl(int position) {
        return (String)getItem(position);
    }

    @Override
    public Object getItem(int position) {
        return (list == null) ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view) {
        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(context).load(getUrl(i)).into(iv);
        return iv;
    }
}
