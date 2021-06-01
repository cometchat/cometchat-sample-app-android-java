package com.cometchat.pro.uikit.ui_components.messages.media_view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.uikit.R;

import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.cometchat.pro.uikit.ui_resources.utils.zoom_imageView.ZoomImageView;

public class CometChatMediaViewActivity extends AppCompatActivity {

    private ImageView imageMessage;
    private VideoView videoMessage;
    private Toolbar toolbar;
    private String senderName;
    private long sentAt;
    private String mediaUrl;
    private String mediaType;

    private int mSize;
    private ImageView playBtn;
    private MediaPlayer mediaPlayer;
    private TextView mediaSize;

    private RelativeLayout audioMessage;
    private String TAG = CometChatMediaViewActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cometchat_media_view);
        handleIntent();
        mediaPlayer = new MediaPlayer();
        toolbar = findViewById(R.id.toolbar);
        toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.textColorWhite));
        toolbar.setTitle(senderName);
        toolbar.setSubtitle(Utils.getLastMessageDate(this,sentAt));
        imageMessage = findViewById(R.id.image_message);
        videoMessage = findViewById(R.id.video_message);
        audioMessage = findViewById(R.id.audio_message);
        mediaSize = findViewById(R.id.media_size_tv);
        playBtn = findViewById(R.id.playBtn);
        if (mediaType.equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
            Glide.with(this).asBitmap().load(mediaUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageMessage);
            imageMessage.setVisibility(View.VISIBLE);
        } else if (mediaType.equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
            MediaController mediacontroller = new MediaController(this);
            mediacontroller.setAnchorView(videoMessage);
            videoMessage.setMediaController(mediacontroller);
            videoMessage.setVideoURI(Uri.parse(mediaUrl));
            videoMessage.setVisibility(View.VISIBLE);
        } else if (mediaType.equals(CometChatConstants.MESSAGE_TYPE_AUDIO)) {
            mediaPlayer.reset();
            mediaSize.setText(Utils.getFileSize(mSize));
            playBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mediaPlayer.setDataSource(mediaUrl);
                        mediaPlayer.prepare();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                playBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "MediaPlayerError: "+e.getMessage());
                    }
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        playBtn.setImageResource(R.drawable.ic_pause_24dp);
                    } else {
                        mediaPlayer.pause();
                        playBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    }
                }
            });
            audioMessage.setVisibility(View.VISIBLE);
        }
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    private void handleIntent() {
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.MEDIA_SIZE))
            mSize = getIntent().getIntExtra(UIKitConstants.IntentStrings.MEDIA_SIZE,0);
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.NAME))
            senderName = getIntent().getStringExtra(UIKitConstants.IntentStrings.NAME);
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.SENTAT))
            sentAt = getIntent().getLongExtra(UIKitConstants.IntentStrings.SENTAT,0);
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.INTENT_MEDIA_MESSAGE))
            mediaUrl = getIntent().getStringExtra(UIKitConstants.IntentStrings.INTENT_MEDIA_MESSAGE);
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE))
            mediaType = getIntent().getStringExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE);
    }
}