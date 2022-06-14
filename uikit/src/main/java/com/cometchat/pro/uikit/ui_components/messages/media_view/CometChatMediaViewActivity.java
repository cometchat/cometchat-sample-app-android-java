package com.cometchat.pro.uikit.ui_components.messages.media_view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.uikit.R;

import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_components.shared.CometChatSnackBar;
import com.cometchat.pro.uikit.ui_components.shared.cometchatComposeBox.CometChatComposeBox;
import com.cometchat.pro.uikit.ui_components.shared.cometchatComposeBox.listener.ComposeActionListener;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_resources.utils.CometChatError;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.cometchat.pro.uikit.ui_resources.utils.zoom_imageView.ZoomImageView;

import java.io.File;

public class CometChatMediaViewActivity extends AppCompatActivity {

    private ImageView imageMessage;
    private VideoView videoMessage;
    private Toolbar toolbar;
    private String senderName;
    private long sentAt;
    private String mediaUrl;
    private String mediaType;

    private String Id;
    private String type;
    private boolean allowCaption;
    private int mSize;
    private ImageView playBtn;
    private MediaPlayer mediaPlayer;
    private TextView mediaSize;

    private CometChatComposeBox composeBox;

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
        if (sentAt!=0)
            toolbar.setSubtitle(Utils.getLastMessageDate(this,sentAt));
        imageMessage = findViewById(R.id.image_message);
        videoMessage = findViewById(R.id.video_message);
        audioMessage = findViewById(R.id.audio_message);
        mediaSize = findViewById(R.id.media_size_tv);
        composeBox = findViewById(R.id.compose_box);

        if (allowCaption)
            composeBox.setVisibility(View.VISIBLE);
        else
            composeBox.setVisibility(View.GONE);

        composeBox.etComposeBox.setHint("Caption");
        composeBox.etComposeBox.setSingleLine();
        composeBox.ivMic.setVisibility(View.GONE);
        composeBox.ivSend.setVisibility(View.VISIBLE);
        composeBox.etComposeBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND || i == EditorInfo.IME_ACTION_DONE) {
                    Utils.hideKeyBoard(CometChatMediaViewActivity.this,composeBox);
                    sendMediaMessage(mediaUrl,composeBox.etComposeBox.getText());
                }
                return true;
            }
        });
        composeBox.setComposeBoxListener(new ComposeActionListener() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) { }

            @Override
            public void onSendActionClicked(EditText editText) {
                sendMediaMessage(mediaUrl,editText.getText());
            }
        });
        composeBox.setPadding(16,0,16,8);
        composeBox.ivArrow.setVisibility(View.GONE);
        composeBox.ivMic.setVisibility(View.GONE);
        composeBox.liveReactionBtn.setVisibility(View.GONE);

        playBtn = findViewById(R.id.playBtn);
        if (mediaType.equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
            Glide.with(this).asBitmap().load(mediaUrl)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).into(imageMessage);
            imageMessage.setVisibility(View.VISIBLE);
        } else if (mediaType.equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
            MediaController mediacontroller = new MediaController(this,true);
            mediacontroller.setAnchorView(videoMessage);
            videoMessage.setMediaController(mediacontroller);
            videoMessage.setVideoURI(Uri.parse(mediaUrl));
            videoMessage.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediacontroller.show(0);
                    mediaPlayer.start();
                }
            });
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

    private void sendMediaMessage(String mediaUrl, Editable caption) {
        ProgressDialog progressDialog =
                ProgressDialog.show(this,"",getString(R.string.media_in_progress));
        File file = new File(mediaUrl);
        MediaMessage mediaMessage = new MediaMessage(Id, file, mediaType, type);
        mediaMessage.setCaption(caption.toString());
        CometChat.sendMediaMessage(mediaMessage, new CometChat.CallbackListener<MediaMessage>() {
            @Override
            public void onSuccess(MediaMessage mediaMessage) {
                progressDialog.dismiss();
                launchMessageList();
            }

            @Override
            public void onError(CometChatException e) {
                CometChatSnackBar.show(CometChatMediaViewActivity.this,
                        toolbar,e.getCode(),CometChatSnackBar.ERROR);
            }
        });

    }

    private void launchMessageList() {
        Intent intent = new Intent(CometChatMediaViewActivity.this, CometChatMessageListActivity.class);
        if (type.equals(CometChatConstants.RECEIVER_TYPE_USER))
            intent.putExtra(UIKitConstants.IntentStrings.UID,Id);
        else
            intent.putExtra(UIKitConstants.IntentStrings.GUID,Id);
//        intent.putExtra(UIKitConstants.IntentStrings.NAME,senderName);
        intent.putExtra(UIKitConstants.IntentStrings.TYPE,type);
        startActivity(intent);
        finish();
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
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.ID))
            Id = getIntent().getStringExtra(UIKitConstants.IntentStrings.ID);
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.TYPE))
            type = getIntent().getStringExtra(UIKitConstants.IntentStrings.TYPE);
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.ALLOW_CAPTION))
            allowCaption = getIntent().getBooleanExtra(UIKitConstants.IntentStrings.ALLOW_CAPTION,false);
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