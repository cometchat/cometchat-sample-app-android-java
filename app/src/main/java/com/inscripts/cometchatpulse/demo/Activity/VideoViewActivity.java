package com.inscripts.cometchatpulse.demo.Activity;

import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;

public class VideoViewActivity extends AppCompatActivity {

    private VideoView video_view;

    private ImageView imageView;

    private String mediaUrl;

    private boolean isVideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);


        if (getIntent().hasExtra(StringContract.IntentStrings.MEDIA_URL)) {
            mediaUrl = getIntent().getStringExtra(StringContract.IntentStrings.MEDIA_URL);
        }

        if (getIntent().hasExtra(StringContract.IntentStrings.ISVIDEO)){
            isVideo=getIntent().getBooleanExtra(StringContract.IntentStrings.ISVIDEO,false);
        }

        initComponentView();

    }

    private void initComponentView() {

        try {

            video_view = findViewById(R.id.video_view);
            imageView=findViewById(R.id.image_view);
            final MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(video_view);

             if (isVideo) {
                 imageView.setVisibility(View.GONE);
                 video_view.setMediaController(mediacontroller);
                 video_view.setVideoURI(Uri.parse(mediaUrl));
                 video_view.requestFocus();

                 video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                     @Override
                     public void onPrepared(MediaPlayer mediaPlayer) {
                         mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                             @Override
                             public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                                 video_view.setMediaController(mediacontroller);
                                 mediacontroller.setAnchorView(video_view);
                             }
                         });
                     }
                 });

                 video_view.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                     @Override
                     public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                         return false;
                     }
                 });
             }
             else {
                 video_view.setVisibility(View.GONE);
                 Glide.with(this).load(mediaUrl).into(imageView);
             }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}