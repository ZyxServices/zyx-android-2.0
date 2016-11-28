package com.tiyujia.homesport.common.homepage.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;
import com.tiyujia.homesport.R;
import com.tiyujia.homesport.entity.VideoEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzqybyb19860112 on 2016/11/24.1
 */

public class HomePageCourseVideoAdapter extends RecyclerView.Adapter {
    List<VideoEntity> videos;
    Context context;

    public HomePageCourseVideoAdapter(Context context,List<VideoEntity> videos ) {
        if (videos.size()!=0) {
            this.videos = videos;
        }else {
            this.videos=new ArrayList<>();
        }
        this.context = context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_course, null);
        view.setLayoutParams(lp);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof VideoViewHolder){
            final VideoViewHolder holder= (VideoViewHolder) viewHolder;
            final VideoEntity data=videos.get(position);
            final SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
            holder.ivMicroPic.setImageDrawable(data.getDrawable());
            holder.tvVideoName.setText(data.getTitle());
            holder.tvVideoWholeTime.setText(data.getTotalTime());
            holder.tvVideoSeekBarWholeTime.setText(data.getTotalTime());
            holder.ivVideoPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.ivMicroPic.setVisibility(View.GONE);//隐藏缩略图
                    holder.vvVideo.setVisibility(View.VISIBLE);//显示播放视图
                    holder.ivVideoPlay.setVisibility(View.GONE);//隐藏大播放按钮
                    String uri = "android.resource://" + context.getPackageName() + "/" + data.getLocalPath();
                    holder.vvVideo.setVideoURI(Uri.parse((uri)));
                    MediaController controller=new MediaController(context);
                    holder.vvVideo.setMediaController(controller);//添加控制台
                    holder.vvVideo.requestFocus();//获取焦点
                    //监听播完了重播
                    holder.vvVideo.start();
                    holder.llPlayControl.setVisibility(View.VISIBLE);//显示播放控制器
                    holder.tvVideoWholeTime.setVisibility(View.GONE);//隐藏显示总时间的视图
                    int currentTime=holder.vvVideo.getCurrentPosition();
                    long nowTime=(long)currentTime;
                    String timeNow=sdf.format(nowTime);
                    String []hms=timeNow.split(":");
                    if (hms[0].equals("00")){
                        timeNow=timeNow.substring(3);
                    }
                    holder.tvVideoNowTime.setText(timeNow);
                    holder.sbPlayProgress.setMax(holder.vvVideo.getDuration());
                    holder.sbPlayProgress.setProgress(holder.vvVideo.getCurrentPosition());
                    holder.sbPlayProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            holder.vvVideo.seekTo(progress);
                        }
                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }
                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });
                    holder.vvVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            mp=null;
                            holder.llPlayControl.setVisibility(View.GONE);
                            holder.vvVideo.setVisibility(View.GONE);
                            holder.ivVideoPlay.setVisibility(View.VISIBLE);
                            holder.ivMicroPic.setVisibility(View.VISIBLE);
                            holder.tvVideoWholeTime.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
            holder.ivVideoStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.vvVideo.isPlaying()){
                        holder.vvVideo.pause();
                    }else {
                        holder.vvVideo.resume();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
    class VideoViewHolder extends RecyclerView.ViewHolder{
        VideoView vvVideo;                  //播放主视图
        TextView tvVideoName;               //视频标题
        TextView tvVideoWholeTime;          //视频总时间,与容器相互排斥
        TextView tvVideoNowTime;            //已播放时间
        TextView tvVideoSeekBarWholeTime;   //进度条上显示的视频总时间
        ImageView ivMicroPic;               //视频的缩略图
        ImageView ivVideoPlay;              //开始播放的按钮
        ImageView ivVideoStop;              //暂停或继续播放的按钮
        ImageView ivVideoFullScreen;        //全屏按钮
        SeekBar sbPlayProgress;             //进度条
        LinearLayout llPlayControl;         //播放控制的容器

        public VideoViewHolder(View itemView) {
            super(itemView);
            vvVideo= (VideoView) itemView.findViewById(R.id.vvVideo);
            tvVideoName= (TextView) itemView.findViewById(R.id.tvVideoName);
            tvVideoWholeTime= (TextView) itemView.findViewById(R.id.tvVideoWholeTime);
            tvVideoNowTime= (TextView) itemView.findViewById(R.id.tvVideoNowTime);
            tvVideoSeekBarWholeTime= (TextView) itemView.findViewById(R.id.tvVideoSeekBarWholeTime);
            ivMicroPic= (ImageView) itemView.findViewById(R.id.ivMicroPic);
            ivVideoPlay= (ImageView) itemView.findViewById(R.id.ivVideoPlay);
            ivVideoStop= (ImageView) itemView.findViewById(R.id.ivVideoStop);
            ivVideoFullScreen= (ImageView) itemView.findViewById(R.id.ivVideoFullScreen);
            sbPlayProgress= (SeekBar) itemView.findViewById(R.id.sbPlayProgress);
            llPlayControl= (LinearLayout) itemView.findViewById(R.id.llPlayControl);
        }
    }
}
