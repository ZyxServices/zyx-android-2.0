package com.tiyujia.homesport.common.community.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.tiyujia.homesport.R;
import java.io.File;
import java.util.Calendar;

public class CommunityNewVideoActivity extends Activity implements View.OnClickListener,SurfaceHolder.Callback{
    ImageView ivCreateVideoOver;//提前结束录制视频
    TextView tvCreateVideoBegin;//按住拍摄
    // 显示视频的SurfaceView
    SurfaceView svCamera;
    MediaRecorder mRecorder;
    Camera mCamera;
    private Camera.Parameters myParameters;
    private SurfaceHolder mSurfaceHolder;
    private boolean isView = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initScreen();
        setContentView(R.layout.activity_community_new_video);
        ivCreateVideoOver = (ImageView) findViewById(R.id.ivCreateVideoOver);
        tvCreateVideoBegin = (TextView) findViewById(R.id.tvCreateVideoBegin);
        svCamera = (SurfaceView) findViewById(R.id.svCamera);
        initCamera();
        setListeners();
        SurfaceHolder holder = svCamera.getHolder();// 取得holder
        holder.addCallback(this); // holder加入回调接口
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    private void initScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        // 选择支持半透明模式,在有surfaceview的activity中使用。
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }
    private void setListeners() {
        ivCreateVideoOver.setOnClickListener(this);
        tvCreateVideoBegin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startRecord();
                        break;
                    case MotionEvent.ACTION_UP:
                        try {
                            mRecorder.stop();
                            mRecorder.reset();
                            tvCreateVideoBegin.setText("按住拍");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startFlag=false;
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivCreateVideoOver:
                finish();
                break;
        }
    }
    boolean startFlag=false;
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
    }
    private void startRecord() {
        if (!startFlag){
            try{
                mRecorder.prepare();
                mRecorder.start();
                startFlag=true;
                tvCreateVideoBegin.setText("正在摄像");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mSurfaceHolder=holder;
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        svCamera = null;
        mSurfaceHolder = null;
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }
    public static String getDate(){
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int day = ca.get(Calendar.DATE);
        int minute = ca.get(Calendar.MINUTE);
        int hour = ca.get(Calendar.HOUR);
        int second = ca.get(Calendar.SECOND);
        String date = "" + year + (month + 1 )+ day + hour + minute + second;
        return date;
    }
    /**
     * 获取SD path
     */
    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist){
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }
        return null;
    }
    private void initCamera(){
        if(mCamera == null && !isView){
            mCamera = Camera.open();
        }
        if(mCamera != null && !isView) {
            try {
                myParameters = mCamera.getParameters();
                myParameters.setPreviewSize(1920, 1080);
                myParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                mCamera.setParameters(myParameters);
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
                mCamera.unlock();
                isView = true;
                if (mRecorder==null){
                    mRecorder=new MediaRecorder();
                    mRecorder.reset();
                }
                mRecorder.setCamera(mCamera);
                mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
                mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                mRecorder.setVideoSize(1920, 1080);
                mRecorder.setVideoEncodingBitRate(1 * 1024 * 512);
                mRecorder.setOrientationHint(90);
//                mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
                String path = getSDPath();
                if (path!=null){
                    File dir = new File(path + "/VideoTest");
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    path = dir + "/" + getDate() + ".mp4";
                    mRecorder.setOutputFile(path);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(CommunityNewVideoActivity.this, "初始化相机错误", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
