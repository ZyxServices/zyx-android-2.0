package com.tiyujia.homesport.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tiyujia.homesport.R;

/**
 * Created by zzqybyb19860112 on 2016/12/22.
 */

public class EmptyViewHolder extends RecyclerView.ViewHolder{
    public TextView tvEmpty;
    public EmptyViewHolder(View itemView) {
        super(itemView);
        tvEmpty= (TextView) itemView.findViewById(R.id.text_empty);
    }
}
