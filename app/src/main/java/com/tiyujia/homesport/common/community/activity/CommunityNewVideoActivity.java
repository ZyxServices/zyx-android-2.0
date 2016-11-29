package com.tiyujia.homesport.common.community.activity;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tiyujia.homesport.ImmersiveActivity;
import com.tiyujia.homesport.R;

public class CommunityNewVideoActivity extends ImmersiveActivity {
    ImageView ivCreateVideoOver;//提前结束录制视频
    TextView tvCreateVideoBegin;//正常录制视频
    // 系统视频文件
    MediaRecorder mRecorder;
    // 显示视频的SurfaceView
    SurfaceView svCamera;
    // 记录是否正在进行录制
    boolean isRecording = false;
    private SurfaceHolder mSurfaceHolder;
    private Camera myCamera;
    private Camera.Parameters myParameters;
    int width,height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_community_new_video);
        ivCreateVideoOver = (ImageView) findViewById(R.id.ivCreateVideoOver);
        tvCreateVideoBegin = (TextView) findViewById(R.id.tvCreateVideoBegin);
        svCamera = (SurfaceView) findViewById(R.id.svCamera);
        ivCreateVideoOver.setEnabled(false);
    }
}
