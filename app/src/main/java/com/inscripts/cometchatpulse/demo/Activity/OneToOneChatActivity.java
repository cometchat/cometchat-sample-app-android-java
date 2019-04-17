package com.inscripts.cometchatpulse.demo.Activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Adapter.OneToOneAdapter;
import com.inscripts.cometchatpulse.demo.Contracts.OneToOneActivityContract;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.CustomView.AttachmentTypeSelector;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.CustomView.RecordAudio;
import com.inscripts.cometchatpulse.demo.CustomView.RecordMicButton;
import com.inscripts.cometchatpulse.demo.CustomView.StickyHeaderDecoration;
import com.inscripts.cometchatpulse.demo.Helper.AttachmentHelper;
import com.inscripts.cometchatpulse.demo.Helper.CCPermissionHelper;
import com.inscripts.cometchatpulse.demo.Helper.RecordListener;
import com.inscripts.cometchatpulse.demo.Helper.RecyclerTouchListener;
import com.inscripts.cometchatpulse.demo.Presenters.OneToOneActivityPresenter;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Utils.AnimUtil;
import com.inscripts.cometchatpulse.demo.Utils.ColorUtils;
import com.inscripts.cometchatpulse.demo.Utils.DateUtils;
import com.inscripts.cometchatpulse.demo.Utils.FileUtils;
import com.inscripts.cometchatpulse.demo.Utils.FontUtils;
import com.inscripts.cometchatpulse.demo.Utils.KeyboardVisibilityEvent;
import com.inscripts.cometchatpulse.demo.Utils.Logger;
import com.inscripts.cometchatpulse.demo.Utils.MediaUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OneToOneChatActivity extends AppCompatActivity implements OneToOneActivityContract.OneToOneView, View.OnClickListener, TextWatcher, ActionMode.Callback {

    private static final int LIMIT = 30;

    public static final int RECORD_CODE = 22;

    private RecyclerView messageRecyclerView;

    private String ownerUid;

    private Toolbar toolbar;

    private EditText messageField;

    private int messageCount = -1;

    private LinearLayoutManager linearLayoutManager;

    private TextView toolbarTitle, toolbarSubTitle;

    private Button btnScroll;

    private long newMessageCount;

    private ImageButton ivAttchament;

    private Button sendButton;

    private OneToOneAdapter oneToOneAdapter;

    private AttachmentTypeSelector attachmentTypeSelector;

    private CircleImageView contactPic;

    private Animation viewAniamtion, goneAnimation;

    private OneToOneActivityContract.OneToOnePresenter oneToOnePresenter;

    private RelativeLayout rlTitleContainer;

    private String contactUid;

    private MediaRecorder mediaRecorder;

    private static final String TAG = "OneToOneChatActivity";

    private String userStatus;

    private RecordMicButton recordMicButton;

    private RecordAudio recordAudioLayout;

    public static String[] RECORD_PERMISSION = {CCPermissionHelper.REQUEST_PERMISSION_RECORD_AUDIO,
            CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE};

    String[] CAMERA_PERMISSION = {CCPermissionHelper.REQUEST_PERMISSION_CAMERA,
            CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE};

    private String audioFileNamewithPath;

    private String avatar;

    private String contactName;

    public static boolean isReply;

    public static JSONObject metaData;

    private BaseMessage baseMessage;

    private RelativeLayout rlMain;

    private static RelativeLayout rlReplyContainer;

    private TextView tvNameReply;

    private TextView tvTextMessage;

    private ImageView ivReplyImage;

    public static  String contactId;

    private User user;

    private Timer timer=new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_to_one_chat);
        attachmentTypeSelector = null;
        new FontUtils(this);
        oneToOnePresenter = new OneToOneActivityPresenter();
        oneToOnePresenter.attach(this);
        initViewComponent();

    }

    private void initViewComponent() {

        ivAttchament = findViewById(R.id.iv_attchment);
        ivAttchament.setOnClickListener(this);
        sendButton = findViewById(R.id.buttonSendMessage);
        sendButton.setOnClickListener(this);
        recordMicButton = findViewById(R.id.record_button);
        recordAudioLayout = findViewById(R.id.record_audio_view);

        //set recordAudioview
        recordMicButton.setListenForRecord(true, this);
        recordAudioLayout.setCancelOffset(8);
        recordAudioLayout.setLessThanSecondAllowed(false);
        recordAudioLayout.setSlideToCancelText(getString(R.string.slide_to_cancel));
        recordAudioLayout.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);
        recordMicButton.setRecordAudio(recordAudioLayout);
        setRecordListener();

        messageField = findViewById(R.id.editTextChatMessage);
        messageField.addTextChangedListener(this);
        messageField.setTypeface(FontUtils.openSansRegular);

        messageRecyclerView = findViewById(R.id.rvChatMessages);

        btnScroll = findViewById(R.id.btn_new_message);
        btnScroll.setOnClickListener(this);

        rlReplyContainer = findViewById(R.id.rlReply);
        tvNameReply = findViewById(R.id.tvNameReply);
        tvTextMessage = findViewById(R.id.tvTextMessage);
        ivReplyImage = findViewById(R.id.ivReplyImage);
        rlMain=findViewById(R.id.rl_main);
        ImageView ivClose = findViewById(R.id.ivClose);

        linearLayoutManager = new LinearLayoutManager(this);
        //        linearLayoutManager.setReverseLayout(true);

        messageRecyclerView.setLayoutManager(linearLayoutManager);
        messageRecyclerView.getItemAnimator().setChangeDuration(0);

        toolbar = findViewById(R.id.cometchat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        rlTitleContainer = findViewById(R.id.rl_titlecontainer);
        rlTitleContainer.setOnClickListener(this);
        ivClose.setOnClickListener(this);

        contactPic = findViewById(R.id.contact_pic);

        toolbarTitle = findViewById(R.id.title);
        toolbarSubTitle = findViewById(R.id.subTitle);

        toolbarTitle.setTypeface(FontUtils.robotoMedium);
        toolbarSubTitle.setTypeface(FontUtils.robotoRegular);
        toolbarSubTitle.setSelected(true);

        viewAniamtion = AnimationUtils.loadAnimation(this, R.anim.animate);
        goneAnimation = AnimationUtils.loadAnimation(this, R.anim.gone_animation);
        oneToOnePresenter.setContext(this);
        oneToOnePresenter.getOwnerDetail();
        oneToOnePresenter.handleIntent(getIntent());

        new Thread(() -> oneToOnePresenter.fetchPreviousMessage(contactUid, LIMIT)).start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setScrollListener();

        } else {
            messageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                    if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                        Logger.error("slow scroll");
                        oneToOnePresenter.fetchPreviousMessage(contactUid, LIMIT);

                    }

                    //for toolbar elevation animation i.e stateListAnimator
                    toolbar.setSelected(messageRecyclerView.canScrollVertically(-1));


                }
            });
        }


        KeyboardVisibilityEvent.setEventListener(this, var1 -> {
            if (messageCount - linearLayoutManager.findLastVisibleItemPosition() < 5) {

                if (oneToOneAdapter != null) {
                    messageRecyclerView.scrollToPosition(oneToOneAdapter.getItemCount() - 1);
                }
            }
        });

        messageRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                messageRecyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View var1, int var2) {

            }

            @Override
            public void onLongClick(View var1, int var2) {

                toolbar.startActionMode(OneToOneChatActivity.this);
                baseMessage = (BaseMessage) var1.getTag(R.string.message);


            }
        }));

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setScrollListener() {

        messageRecyclerView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {

            int temp = linearLayoutManager.findFirstVisibleItemPosition();

            if (temp < 5) {
                Logger.error("Fast scroll");
                oneToOnePresenter.fetchPreviousMessage(contactUid, LIMIT);

            }

            toolbar.setSelected(messageRecyclerView.canScrollVertically(-1));


            if (messageCount - linearLayoutManager.findLastVisibleItemPosition() < 5) {
                if (btnScroll.getVisibility() == View.VISIBLE) {
                    btnScroll.startAnimation(goneAnimation);
                    btnScroll.setVisibility(View.GONE);

                }

            } else {
                if (btnScroll.getVisibility() == View.GONE) {
                    btnScroll.startAnimation(viewAniamtion);
                    btnScroll.setVisibility(View.VISIBLE);
                }
            }

            if (messageCount - 2 == linearLayoutManager.findLastVisibleItemPosition()) {
                newMessageCount = 0;
                btnScroll.setText(getString(R.string.jump_to_latest));
                btnScroll.getBackground().setColorFilter(Color.parseColor("#8e8e92"), PorterDuff.Mode.SRC_ATOP);
            }
        });

    }


    private void setRecordListener() {
        recordAudioLayout.setOnRecordListener(new RecordListener() {
            @Override
            public void onStart() {

                Logger.error(TAG, "onStart: " + " record start");
                messageField.setHint("");
                startRecording();
            }

            @Override
            public void onCancel() {

                Logger.error(TAG, "onCancel: " + "record cancel");

                messageField.setHint(getString(R.string.message_hint));
                stopRecording(true);
            }

            @Override
            public void onFinish(long time) {
                Logger.error(TAG, "onFinish: " + "record finish");

                messageField.setHint(getString(R.string.message_hint));
                File audioFile;
                if (audioFileNamewithPath != null) {
                    audioFile = new File(audioFileNamewithPath);
                    Logger.error("audioFileNamewithPath", audioFileNamewithPath);
                    oneToOnePresenter.sendMediaMessage(audioFile, contactUid, CometChatConstants.MESSAGE_TYPE_AUDIO);
                }

                stopRecording(false);
            }

            @Override
            public void onLessTime() {
                Logger.error(TAG, "onLessTime: ");
                messageField.setHint(getString(R.string.message_hint));
                stopRecording(true);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.error(TAG, "onDestroy: ");
        oneToOnePresenter.detach();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.one_to_one_chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_custom_action_video_call:
                oneToOnePresenter.sendCallRequest(this, contactUid, CometChatConstants.RECEIVER_TYPE_USER, CometChatConstants.CALL_TYPE_VIDEO);
                break;

            case R.id.menu_custom_action_audio_call:
                oneToOnePresenter.sendCallRequest(this, contactUid, CometChatConstants.RECEIVER_TYPE_USER, CometChatConstants.CALL_TYPE_AUDIO);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addAttachment(int type) {

        String[] STORAGE_PERMISSION = {CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE};
        String[] CAMERA_PERMISSION = {CCPermissionHelper.REQUEST_PERMISSION_CAMERA, CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE};
        switch (type) {
            case StringContract.RequestCode.ADD_GALLERY:

                if (CCPermissionHelper.hasPermissions(this, CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE)) {
                    AttachmentHelper.selectMedia(this, "*/*", StringContract.IntentStrings.EXTRA_MIME_TYPE, StringContract.RequestCode.ADD_GALLERY);
                } else {
                    CCPermissionHelper.requestPermissions(this, STORAGE_PERMISSION, StringContract.RequestCode.ADD_GALLERY);
                }
                break;
            case StringContract.RequestCode.ADD_DOCUMENT:

                if (CCPermissionHelper.hasPermissions(this, CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE)) {
                    AttachmentHelper.selectMedia(this, StringContract.IntentStrings.DOCUMENT_TYPE,
                            StringContract.IntentStrings.EXTRA_MIME_DOC, StringContract.RequestCode.ADD_DOCUMENT);
                } else {
                    CCPermissionHelper.requestPermissions(this, STORAGE_PERMISSION, StringContract.RequestCode.ADD_DOCUMENT);
                }
                break;
            case StringContract.RequestCode.ADD_SOUND:
                if (CCPermissionHelper.hasPermissions(this, CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE)) {
                    AttachmentHelper.selectMedia(this, StringContract.IntentStrings.AUDIO_TYPE, null, StringContract.RequestCode.ADD_SOUND);
                } else {
                    CCPermissionHelper.requestPermissions(this, STORAGE_PERMISSION, StringContract.RequestCode.ADD_SOUND);
                }
                break;

            case StringContract.RequestCode.TAKE_PHOTO:
                if (CCPermissionHelper.hasPermissions(this, CAMERA_PERMISSION)) {
                    AttachmentHelper.captureImage(this, StringContract.RequestCode.TAKE_PHOTO);
                } else {
                    CCPermissionHelper.requestPermissions(this, CAMERA_PERMISSION, StringContract.RequestCode.TAKE_PHOTO);
                }

                break;
            case StringContract.RequestCode.TAKE_VIDEO:
                if (CCPermissionHelper.hasPermissions(this, CAMERA_PERMISSION)) {
                    AttachmentHelper.captureVideo(this, StringContract.RequestCode.TAKE_VIDEO);
                } else {
                    CCPermissionHelper.requestPermissions(this, CAMERA_PERMISSION, StringContract.RequestCode.TAKE_VIDEO);
                }
                break;

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case StringContract.RequestCode.ADD_DOCUMENT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    AttachmentHelper.selectMedia(this, StringContract.IntentStrings.DOCUMENT_TYPE,
                            null, StringContract.RequestCode.ADD_DOCUMENT);
                } else {
                    showToast();
                }
                break;

            case StringContract.RequestCode.ADD_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    AttachmentHelper.selectMedia(this, "*/*",
                            StringContract.IntentStrings.EXTRA_MIME_TYPE, StringContract.RequestCode.ADD_GALLERY);
                } else {
                    showToast();
                }
                break;

            case StringContract.RequestCode.TAKE_PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    AttachmentHelper.captureImage(this, StringContract.RequestCode.TAKE_PHOTO);
                } else {
                    showToast();
                }
                break;

            case StringContract.RequestCode.TAKE_VIDEO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    AttachmentHelper.captureVideo(this, StringContract.RequestCode.TAKE_VIDEO);
                } else {
                    showToast();
                }
                break;

            case RECORD_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    showToast();
                }

                break;

            case StringContract.RequestCode.ADD_SOUND:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AttachmentHelper.selectMedia(this, StringContract.IntentStrings.AUDIO_TYPE, null, StringContract.RequestCode.ADD_SOUND);
                } else {
                    showToast();
                }
                break;

            case StringContract.RequestCode.FILE_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    FileUtils.makeDirectory(this, CometChatConstants.MESSAGE_TYPE_AUDIO);
                } else {
                    showToast();
                }
                break;

            case StringContract.RequestCode.READ_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    showToast();
                }
                break;


        }
    }

    private void startRecording() {
        try {

            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            audioFileNamewithPath = FileUtils.getOutputMediaFile(OneToOneChatActivity.this);
            mediaRecorder.setOutputFile(audioFileNamewithPath);

            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRecording(boolean isCancel) {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                if (isCancel) {
                    new File(audioFileNamewithPath).delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.error(TAG, "onResume: ");
        oneToOnePresenter.addPresenceListener(getString(R.string.presenceListener));
        oneToOnePresenter.addMessageReceiveListener(contactUid);
        oneToOnePresenter.addCallEventListener(TAG);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Logger.error(TAG, "onPause: ");
        if (oneToOneAdapter!=null) {
            oneToOneAdapter.stopPlayer();
        }
        timer();
        oneToOnePresenter.removeMessageLisenter(getString(R.string.message_listener));
        oneToOnePresenter.removePresenceListener(getString(R.string.presenceListener));
        oneToOnePresenter.removeCallListener(TAG);
    }


    @Override
    protected void onStop() {
        super.onStop();

        stopRecording(false);
        timer();
        Logger.error(TAG, "onStop:");

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && data != null && oneToOnePresenter != null) {

            switch (requestCode) {
                case StringContract.RequestCode.ADD_GALLERY:
                    AttachmentHelper.handlefile(this, "*/*", oneToOnePresenter, data, contactUid);
                    break;
                case StringContract.RequestCode.TAKE_PHOTO:
                    AttachmentHelper.handleCameraImage(this, oneToOnePresenter, data, contactUid);
                    break;
                case StringContract.RequestCode.TAKE_VIDEO:
                    AttachmentHelper.handleCameraVideo(this, oneToOnePresenter, data, contactUid);
                    break;
                case StringContract.RequestCode.ADD_SOUND:
                    AttachmentHelper.handlefile(OneToOneChatActivity.this, CometChatConstants.MESSAGE_TYPE_AUDIO, oneToOnePresenter, data, contactUid);
                    break;
                case StringContract.RequestCode.ADD_DOCUMENT:
                    AttachmentHelper.handlefile(OneToOneChatActivity.this, CometChatConstants.MESSAGE_TYPE_FILE, oneToOnePresenter, data, contactUid);
                    break;
            }
        }
    }

    private void showToast() {
        Toast.makeText(this, "PERMISSION NOT GRANTED", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void setAdapter(List<BaseMessage> messageArrayList) {

        if (oneToOneAdapter != null) {
            messageCount = oneToOneAdapter.getItemCount() - 1;
        } else {
            messageCount = 0;
        }
        if (oneToOneAdapter == null) {
            oneToOneAdapter = new OneToOneAdapter(this, messageArrayList, ownerUid);
            oneToOneAdapter.setHasStableIds(true);
            messageRecyclerView.setAdapter(oneToOneAdapter);
            StickyHeaderDecoration decor = new StickyHeaderDecoration(oneToOneAdapter);
            messageRecyclerView.addItemDecoration(decor);
            if (oneToOneAdapter.getItemCount() != 0) {
                messageRecyclerView.scrollToPosition(oneToOneAdapter.getItemCount() - 1);
            }
        } else if (messageArrayList != null && messageArrayList.size() != 0) {
            oneToOneAdapter.refreshData(messageArrayList);
        }

    }

    @Override
    public void addMessage(BaseMessage newMessage) {

        if (oneToOneAdapter != null) {
            newMessageCount++;
            oneToOneAdapter.addMessage(newMessage);

            if ((oneToOneAdapter.getItemCount() - 1) - linearLayoutManager.findLastVisibleItemPosition() < 5) {
                messageRecyclerView.scrollToPosition(oneToOneAdapter.getItemCount() - 1);
                newMessageCount = 0;
            } else {
                btnScroll.setVisibility(View.VISIBLE);
                btnScroll.setText(newMessageCount + " " + getString(R.string.new_message));
                AnimUtil.getShakeAnimation(btnScroll);
                btnScroll.getBackground().setColorFilter(getResources().getColor(R.color.secondaryColor), PorterDuff.Mode.SRC_ATOP);

            }
        }

    }

    @Override
    public void setOwnerDetail(User user) {
        ownerUid = user.getUid();
    }

    @Override
    public void setTitle(String name) {
        toolbarTitle.setText(name);
        contactName = name;
    }


    @Override
    public void addSendMessage(BaseMessage baseMessage) {
        if (oneToOneAdapter != null) {
            oneToOneAdapter.addMessage(baseMessage);
            if (btnScroll.getVisibility() == View.GONE) {
                messageRecyclerView.scrollToPosition(oneToOneAdapter.getItemCount() - 1);
            }
        }
    }

    @Override
    public void setContactUid(String stringExtra) {

        contactUid = stringExtra;

    }

    @Override
    public void setAvatar(String avatar) {
        if (avatar != null) {
            this.avatar = avatar;
            oneToOnePresenter.setContactPic(OneToOneChatActivity.this, avatar, contactPic);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.default_avatar);

            try {
                Bitmap bitmap = MediaUtils.getPlaceholderImage(this, drawable);
                contactPic.setCircleBackgroundColor(ColorUtils.getMaterialColor(this));
                contactPic.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setPresence(User user) {


        if (user != null && user.getUid().equals(contactUid)) {
            this.user=user;
            if (user.getStatus().equals(CometChatConstants.USER_STATUS_ONLINE)) {
                userStatus = user.getStatus();
            } else if (user.getStatus().equals(CometChatConstants.USER_STATUS_OFFLINE)) {
                userStatus = DateUtils.getLastSeenDate(user.getLastActiveAt(), this);

            }

            toolbarSubTitle.setText(userStatus);
        }
    }

    @Override
    public void setTyping() {

        toolbarSubTitle.setText(getString(R.string.typing));
    }

    @Override
    public void endTyping() {

        if (user.getStatus().equals(CometChatConstants.USER_STATUS_ONLINE)) {
            userStatus = user.getStatus();
        } else if (user.getStatus().equals(CometChatConstants.USER_STATUS_OFFLINE)) {
            userStatus = DateUtils.getLastSeenDate(user.getLastActiveAt(), this);
        }
        toolbarSubTitle.setText(userStatus);
    }

    @Override
    public void setMessageDelivered(MessageReceipt messageReceipt) {
        if (oneToOneAdapter!=null)
        oneToOneAdapter.Delivered(messageReceipt);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_attchment:
                showPopUp();
                break;

            case R.id.buttonSendMessage:

                String message = messageField.getText().toString().trim();

                if (!TextUtils.isEmpty(message)) {

                    oneToOnePresenter.endTypingIndicator(contactUid);
                    oneToOnePresenter.sendMessage(message, contactUid);
                    messageField.setText("");
                }
                break;

            case R.id.rl_titlecontainer:
                Intent intent = new Intent(this, UsersProfileViewActivity.class);
                intent.putExtra(StringContract.IntentStrings.USER_ID, contactUid);
                intent.putExtra(StringContract.IntentStrings.USER_AVATAR, avatar);
                intent.putExtra(StringContract.IntentStrings.USER_NAME, contactName);
                startActivity(intent);
                break;

            case R.id.btn_new_message:
                newMessageCount = 0;
                messageRecyclerView.scrollToPosition(oneToOneAdapter.getItemCount() - 1);
                btnScroll.startAnimation(goneAnimation);
                btnScroll.setVisibility(View.GONE);
                break;

            case R.id.ivClose:
                hideReply();
                break;
        }
    }


    public static void hideReply(){

        metaData=null;
        isReply=false;
        rlReplyContainer.setVisibility(View.GONE);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        int len = messageField.getText().toString().length();

        if (len > 0) {
            sendButton.setTextColor(getResources().getColor(R.color.secondaryDarkColor));
             oneToOnePresenter.sendTypingIndicator(contactUid);

        } else {
            sendButton.setTextColor(getResources().getColor(R.color.secondaryTextColor));

        }

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (timer!=null){
            timer();
        }
        else {
            timer=new Timer();
            timer();
        }

    }

    private void timer(){

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                oneToOnePresenter.endTypingIndicator(contactUid);
            }
        },2000);
    }

    private void showPopUp() {

        if (attachmentTypeSelector == null) {
            attachmentTypeSelector =
                    new AttachmentTypeSelector(OneToOneChatActivity.this,
                            new AttachmentTypeListener());
        }
        attachmentTypeSelector.show(OneToOneChatActivity.this, ivAttchament);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        getMenuInflater().inflate(R.menu.action_mode, menu);

        mode.setTitle(contactName);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        mode.finish();
        isReply = true;
        metaData = null;
        metaData = new JSONObject();
        rlReplyContainer.setVisibility(View.VISIBLE);
        tvNameReply.setText(baseMessage.getSender().getName());

        if (baseMessage instanceof TextMessage) {

            try {
                metaData.put("reply", "reply");
                metaData.put("senderName", ((TextMessage) baseMessage).getSender().getName());
                metaData.put("senderUid", ((TextMessage) baseMessage).getSender().getUid());
                metaData.put("type", ((TextMessage) baseMessage).getType());
                metaData.put("id", ((TextMessage) baseMessage).getId());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            tvTextMessage.setVisibility(View.VISIBLE);
            tvTextMessage.setText(((TextMessage) baseMessage).getText());
            try {
                metaData.put("text",((TextMessage)baseMessage).getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (baseMessage instanceof MediaMessage) {

            tvTextMessage.setVisibility(View.GONE);

                try {
                    metaData.put("reply", "reply");
                    metaData.put("senderName",  baseMessage.getSender().getName());
                    metaData.put("senderUid",  baseMessage.getSender().getUid());
                    metaData.put("type",  baseMessage.getType());
                    metaData.put("id",  baseMessage.getId());
                    metaData.put("url",((MediaMessage)baseMessage).getUrl());

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            switch (baseMessage.getType()) {
                case CometChatConstants.MESSAGE_TYPE_IMAGE:
                case CometChatConstants.MESSAGE_TYPE_VIDEO:

                    ivReplyImage.setVisibility(View.VISIBLE);

                    Glide.with(this).load(((MediaMessage) baseMessage).getUrl()).into(ivReplyImage);
                    break;
                case CometChatConstants.MESSAGE_TYPE_AUDIO:

                    ivReplyImage.setVisibility(View.INVISIBLE);

                    tvTextMessage.setText(getString(R.string.audio_message));
                    break;
                case CometChatConstants.MESSAGE_TYPE_FILE:

                    ivReplyImage.setVisibility(View.INVISIBLE);

                    tvTextMessage.setText(getString(R.string.file_message));
                    break;
            }
        }

        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mode = null;
    }

    private class AttachmentTypeListener implements AttachmentTypeSelector.AttachmentClickedListener {
        @Override
        public void onClick(int type) {
            addAttachment(type);
        }

    }
}
