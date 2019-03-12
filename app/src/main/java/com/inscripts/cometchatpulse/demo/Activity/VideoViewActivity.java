package com.inscripts.cometchatpulse.demo.Activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;

public class VideoViewActivity extends AppCompatActivity {

    private VideoView video_view;

    private String mediaUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);


        if (getIntent().hasExtra(StringContract.IntentStrings.MEDIA_URL)) {
            mediaUrl = getIntent().getStringExtra(StringContract.IntentStrings.MEDIA_URL);
        }

        initComponentView();

    }

    private void initComponentView() {

        try {


            video_view = findViewById(R.id.video_view);
            final MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(video_view);

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
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}