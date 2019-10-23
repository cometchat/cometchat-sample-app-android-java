package com.inscripts.cometchatpulse.demo.Activity;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;

public class VideoViewActivity extends AppCompatActivity {

    private VideoView video_view;

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
            ImageView imageView = findViewById(R.id.image_view);
            final MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(video_view);

             if (isVideo) {
                 imageView.setVisibility(View.GONE);
                 video_view.setMediaController(mediacontroller);
                 video_view.setVideoURI(Uri.parse(mediaUrl));
                 video_view.requestFocus();

                 video_view.setOnPreparedListener(mediaPlayer -> mediaPlayer.setOnVideoSizeChangedListener((mediaPlayer1, i, i1) -> {
                     video_view.setMediaController(mediacontroller);
                     mediacontroller.setAnchorView(video_view);
                 }));

                 video_view.setOnErrorListener((mediaPlayer, i, i1) -> false);
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