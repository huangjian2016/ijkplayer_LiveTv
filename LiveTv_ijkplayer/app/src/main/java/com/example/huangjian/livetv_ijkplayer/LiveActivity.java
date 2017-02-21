package com.example.huangjian.livetv_ijkplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.huangjian.livetv_ijkplayer.ijkplayer.media.IjkVideoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by huangjian on 2017/2/20.
 */
public class LiveActivity extends Activity{
    private IjkVideoView mVideoView;
    private RelativeLayout mVideoViewLayout;
    private RelativeLayout mLoadingLayout;
    private TextView mLoadingText;
    private TextView mTextClock;
    private String mVideoUrl = "";
    private int mRetryTimes = 0;
    private static final int CONNECT_TIMES = 5;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        mVideoUrl = getIntent().getStringExtra("url");
        mVideoView = (IjkVideoView) findViewById(R.id.videoview);
        mVideoViewLayout =(RelativeLayout)findViewById(R.id.fl_videoview);
        mLoadingLayout =(RelativeLayout)findViewById(R.id.rl_loading);
        mLoadingText = (TextView)findViewById(R.id.tv_load_msg);
        mTextClock =(TextView)findViewById(R.id.tv_time);
        mTextClock.setText(getDateFormate());
        mLoadingText.setText("节目加载中...");
        initVideo();
    }

    private String getDateFormate(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public void initVideo(){
        //init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        mVideoView.setVideoURI(Uri.parse(mVideoUrl));
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                mVideoView.start();
            }
        });

        mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                switch (i) {
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        break;
                    case IjkMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    case IjkMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        mLoadingLayout.setVisibility(View.GONE);
                        break;
                }
                return false;
            }
        });

        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                mLoadingLayout.setVisibility(View.VISIBLE);
                mVideoView.stopPlayback();
                mVideoView.release(true);
                mVideoView.setVideoURI(Uri.parse(mVideoUrl));
            }
        });

        mVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                if(mRetryTimes > CONNECT_TIMES){
                    new AlertDialog.Builder(LiveActivity.this)
                            .setMessage("节目暂时不能播放")
                            .setPositiveButton(R.string.VideoView_error_button, new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog,int whichButton){
                                    LiveActivity.this.finish();
                                }
                            })
                            .setCancelable(false)
                            .show();
                } else {
                    mVideoView.stopPlayback();
                    mVideoView.release(true);
                    mVideoView.setVideoURI(Uri.parse(mVideoUrl));
                }

                return false;
            }
        });
    }

    protected void onResume(){
        super.onResume();
    }

    protected void onPause(){
        super.onPause();
    }

    protected void onStop(){
        super.onStop();
        if(!mVideoView.isBackgroundPlayEnabled()){
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        }
        IjkMediaPlayer.native_profileEnd();
    }

    public static void activityStart(Context context,String url){
        Intent intent = new Intent(context,LiveActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public void onConfiguartionChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }
}
