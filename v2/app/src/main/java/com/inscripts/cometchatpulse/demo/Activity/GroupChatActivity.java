package com.inscripts.cometchatpulse.demo.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.TypingIndicator;
import com.inscripts.cometchatpulse.demo.Helper.RecyclerTouchListener;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Adapter.GroupMessageAdapter;
import com.inscripts.cometchatpulse.demo.Contracts.GroupChatActivityContract;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.CustomView.AttachmentTypeSelector;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.CustomView.RecordAudio;
import com.inscripts.cometchatpulse.demo.CustomView.RecordMicButton;
import com.inscripts.cometchatpulse.demo.CustomView.StickyHeaderDecoration;
import com.inscripts.cometchatpulse.demo.Helper.AttachmentHelper;
import com.inscripts.cometchatpulse.demo.Helper.CCPermissionHelper;
import com.inscripts.cometchatpulse.demo.Helper.RecordListener;
import com.inscripts.cometchatpulse.demo.Presenters.GroupChatPresenter;
import com.inscripts.cometchatpulse.demo.Utils.AnimUtil;
import com.inscripts.cometchatpulse.demo.Utils.ColorUtils;
import com.inscripts.cometchatpulse.demo.Utils.FileUtils;
import com.inscripts.cometchatpulse.demo.Utils.FontUtils;
import com.inscripts.cometchatpulse.demo.Utils.KeyboardVisibilityEvent;
import com.inscripts.cometchatpulse.demo.Utils.Logger;
import com.inscripts.cometchatpulse.demo.Utils.MediaUtils;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GroupChatActivity extends AppCompatActivity implements GroupChatActivityContract.GroupChatView, TextWatcher, View.OnClickListener, ActionMode.Callback {

    private static final int LIMIT = 100;

    private static final String TAG = "GroupChatActivity";

    private RecyclerView messageRecyclerView;

    private String ownerId;

    private Toolbar toolbar;

    private EditText messageField;

    private int messageCount = -1;

    private LinearLayoutManager linearLayoutManager;

    private StickyHeaderDecoration decor;

    private TextView toolbarTitle, toolbarSubTitle;

    private Button btnScroll;

    private long newMessageCount;

    private String names = null;

    private ImageButton ivAttchament;

    private Button sendButton;

    private AttachmentTypeSelector attachmentTypeSelector;

    private CircleImageView groupIcon;

    private Animation viewAniamtion, goneAnimation;

    private RelativeLayout rlTitleContainer;

    private String groupId;/// Guid

    private Context context;

    private GroupChatActivityContract.GroupChatPresenter groupChatPresenter;

    private GroupMessageAdapter groupMessageAdapter;

    private Group group;

    private RecordMicButton recordMicButton;

    private RecordAudio recordAudioLayout;

    private MediaRecorder mediaRecorder;

    private String audioFileNamewithPath;

    private Drawable drawable;

    public static boolean isReply;

    public static JSONObject metaData;

    private BaseMessage baseMessage;

    private RelativeLayout rlMain;

    private static RelativeLayout rlReplyContainer;

    private TextView tvNameReply;

    private TextView tvTextMessage;

    private ImageView ivReplyImage;

    private String groupName;

    private Timer timer=new Timer();

    private boolean isEditMessage;

    private MenuItem searchItem;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        attachmentTypeSelector = null;
        context = this;
        groupChatPresenter = new GroupChatPresenter();
        groupChatPresenter.attach(this);
        initViewComponent();

    }

    @Override
    public void setSubTitle(String[] users) {


        if (users != null && users.length != 0) {
            StringBuilder stringBuilder = new StringBuilder();

            for (String user : users) {
                stringBuilder.append(user).append(",");
            }

            names = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
        }

        toolbarSubTitle.setText(names);
    }

    @Override
    public void setOwnerUid(String id) {
        this.ownerId = id;
    }

    private void initViewComponent() {


        new FontUtils(this);
        ivAttchament = findViewById(R.id.iv_attchment);
        ivAttchament.setOnClickListener(this);

        sendButton = findViewById(R.id.buttonSendMessage);
        sendButton.setOnClickListener(this);

        messageField = findViewById(R.id.editTextChatMessage);
        messageField.addTextChangedListener(this);
        messageField.setTypeface(FontUtils.openSansRegular);

        messageRecyclerView = findViewById(R.id.rvChatMessages);

        btnScroll = findViewById(R.id.btn_new_message);
        btnScroll = findViewById(R.id.btn_new_message);
        btnScroll.setOnClickListener(this);

        rlReplyContainer = findViewById(R.id.rlReply);
        tvNameReply = findViewById(R.id.tvNameReply);
        tvTextMessage = findViewById(R.id.tvTextMessage);
        ivReplyImage = findViewById(R.id.ivReplyImage);
        rlMain = findViewById(R.id.rl_main);
        ImageView ivClose = findViewById(R.id.ivClose);

        ivClose.setOnClickListener(this);

        linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setReverseLayout(true);

        messageRecyclerView.setLayoutManager(linearLayoutManager);
        messageRecyclerView.getItemAnimator().setChangeDuration(0);

        toolbar = findViewById(R.id.cometchat_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

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


        rlTitleContainer = findViewById(R.id.rl_titlecontainer);
        rlTitleContainer.setOnClickListener(this);

        groupIcon = findViewById(R.id.contact_pic);

        toolbarTitle = findViewById(R.id.title);
        toolbarTitle.setOnClickListener(this);
        toolbarTitle.setTypeface(FontUtils.robotoMedium);

        toolbarSubTitle = findViewById(R.id.subtitle);
        toolbarSubTitle.setTypeface(FontUtils.robotoRegular);

        viewAniamtion = AnimationUtils.loadAnimation(this, R.anim.animate);
        goneAnimation = AnimationUtils.loadAnimation(this, R.anim.gone_animation);

        drawable = getResources().getDrawable(R.drawable.cc_ic_group);

        groupChatPresenter.getContext(this);

        groupChatPresenter.handleIntent(getIntent());


        new Thread(() -> groupChatPresenter.fetchPreviousMessage(groupId, LIMIT)).start();

        new Thread(() -> groupChatPresenter.fetchGroupMembers(groupId)).start();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setScrollListener();
        } else {
            messageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                    if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {

                        groupChatPresenter.fetchPreviousMessage(groupId, LIMIT);

                    }

                    //for toolbar elevation animation i.e stateListAnimator
                    toolbar.setSelected(recyclerView.canScrollVertically(-1));


                }
            });
        }

        KeyboardVisibilityEvent.setEventListener(this, var1 -> {
            if (messageCount - linearLayoutManager.findLastVisibleItemPosition() < 5) {

                if (groupMessageAdapter != null) {
                    messageRecyclerView.scrollToPosition(groupMessageAdapter.getItemCount() - 1);
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

                toolbar.startActionMode(GroupChatActivity.this);
                baseMessage = (BaseMessage) var1.getTag(R.string.message);


            }
        }));
    }


    public static void hideReply() {

        metaData = null;
        isReply = false;
        rlReplyContainer.setVisibility(View.GONE);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setScrollListener() {

        messageRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int temp = linearLayoutManager.findFirstVisibleItemPosition();

                if (temp < 5) {

                    groupChatPresenter.fetchPreviousMessage(groupId, LIMIT);

                }
                if (messageCount - linearLayoutManager.findLastVisibleItemPosition() < 5) {
                    if (btnScroll.getVisibility() == View.VISIBLE) {
                        btnScroll.startAnimation(goneAnimation);
                        btnScroll.setVisibility(View.GONE);
                        newMessageCount = 0;

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
                Log.d(TAG, "onFinish: " + "record finish");

                messageField.setHint(getString(R.string.message_hint));
                File audioFile;
                if (audioFileNamewithPath != null) {
                    audioFile = new File(audioFileNamewithPath);
                    Logger.error("audioFileNamewithPath", audioFileNamewithPath);
                    groupChatPresenter.sendMediaMessage(audioFile, groupId, CometChatConstants.MESSAGE_TYPE_AUDIO);

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

    private void startRecording() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            audioFileNamewithPath = FileUtils.getOutputMediaFile(GroupChatActivity.this);
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
    protected void onStop() {
        super.onStop();
        stopRecording(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_chat_menu, menu);


        searchItem=menu.findItem(R.id.app_bar_search);

        SearchManager searchManager=((SearchManager)getSystemService(Context.SEARCH_SERVICE));

        if (searchItem!=null){

            searchView=((SearchView)searchItem.getActionView());

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    groupChatPresenter.searchMessage(s,groupId);
                       return false;
                }
            });

            searchView.setOnCloseListener(() -> {
                groupChatPresenter.fetchPreviousMessage(groupId,30);
                return false;
            });
        }

        if (searchView!=null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_custom_action_video_call:
                groupChatPresenter.sendCallRequest(this, groupId, CometChatConstants.RECEIVER_TYPE_GROUP, CometChatConstants.CALL_TYPE_VIDEO);
                break;

            case R.id.menu_custom_action_audio_call:
                groupChatPresenter.sendCallRequest(this, groupId, CometChatConstants.RECEIVER_TYPE_GROUP, CometChatConstants.CALL_TYPE_AUDIO);
                break;

            case R.id.menu_group_leave:
                groupChatPresenter.leaveGroup(group, GroupChatActivity.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        int len = messageField.getText().toString().length();

        if (len > 0) {
            sendButton.setTextColor(getResources().getColor(R.color.secondaryDarkColor));
            groupChatPresenter.sendTypingIndicator(groupId);
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
                groupChatPresenter.endTypingIndicator(groupId);
            }
        },2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        groupChatPresenter.addMessageReceiveListener(getResources().getString(R.string.group_message_listener), groupId, ownerId);
        groupChatPresenter.addGroupEventListener("action_message", groupId, ownerId);
        groupChatPresenter.refreshList(groupId,ownerId,LIMIT);
        groupChatPresenter.addCallListener("call_listener");
    }

    @Override
    protected void onPause() {
        super.onPause();
         if (groupMessageAdapter!=null) {
             groupMessageAdapter.stopPlayer();
         }
        groupChatPresenter.removeMessageReceiveListener(getResources().getString(R.string.group_message_listener));
        groupChatPresenter.removeMessageReceiveListener("action_message");
        groupChatPresenter.removeCallListener("call_listener");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        groupChatPresenter.detach();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_attchment:
                showPopUp();
                break;

            case R.id.btn_new_message:
                newMessageCount = 0;
                messageRecyclerView.scrollToPosition(groupMessageAdapter.getItemCount() - 1);
                btnScroll.startAnimation(goneAnimation);
                btnScroll.setVisibility(View.GONE);
                break;

            case R.id.rl_titlecontainer:
                Intent intent = new Intent(this, GroupDetailActivity.class);
                intent.putExtra(StringContract.IntentStrings.INTENT_GROUP_NAME, group.getName());
                intent.putExtra(StringContract.IntentStrings.INTENT_GROUP_ID, group.getGuid());
                finish();
                startActivity(intent);
                break;

            case R.id.buttonSendMessage:
                String message = messageField.getText().toString().trim();
                if (!TextUtils.isEmpty(message)&&!isEditMessage)
                {
                    groupChatPresenter.sendTextMessage(message, groupId);
                    messageField.setText("");
                }
                else if (isEditMessage&&!TextUtils.isEmpty(message)){
                    isEditMessage=false;
                    groupChatPresenter.editMessage(baseMessage,message);
                    messageField.setText("");
                }

                break;

            case R.id.ivClose:
                hideReply();
                break;


        }
    }

    private void showPopUp() {

        if (attachmentTypeSelector == null) {
            attachmentTypeSelector =
                    new AttachmentTypeSelector(GroupChatActivity.this,
                            new AttachmentTypeListener());
        }
        attachmentTypeSelector.show(GroupChatActivity.this, ivAttchament);
    }

    @Override
    public void setTitle(String title) {
        groupName = title;
        toolbarTitle.setText(title);

    }

    @Override
    public void setGroupId(String stringExtra) {
        groupId = stringExtra;

    }

    @Override
    public void setAdapter(List<BaseMessage> messageList) {

        if (groupMessageAdapter != null) {
            messageCount = groupMessageAdapter.getItemCount() - 1;
        } else {
            messageCount = 0;
        }
        if (groupMessageAdapter == null) {
            groupMessageAdapter = new GroupMessageAdapter(messageList, context, groupId, ownerId);
            messageRecyclerView.setAdapter(groupMessageAdapter);
            decor = new StickyHeaderDecoration(groupMessageAdapter);
            messageRecyclerView.addItemDecoration(decor, 0);
            messageRecyclerView.scrollToPosition(groupMessageAdapter.getItemCount() - 1);
        } else {
            groupMessageAdapter.refreshData(messageList);
        }


    }

    @Override
    public void addSentMessage(BaseMessage baseMessage) {
        if (groupMessageAdapter != null) {
            groupMessageAdapter.add(baseMessage);
            messageRecyclerView.scrollToPosition(groupMessageAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void addReceivedMessage(BaseMessage baseMessage) {

        if (groupMessageAdapter != null) {
            newMessageCount++;
            groupMessageAdapter.add(baseMessage);
            if (messageCount - linearLayoutManager.findLastVisibleItemPosition() < 5) {
                messageRecyclerView.scrollToPosition(groupMessageAdapter.getItemCount() - 1);
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
    public void typingStarted(TypingIndicator typingIndicator) {
       toolbarSubTitle.setText(typingIndicator.getSender().getName()+" "+getString(R.string.is_typing));
    }

    @Override
    public void typingEnded(TypingIndicator typingIndicator) {
           toolbarSubTitle.setText(names);
    }

    @Override
    public void setDeliveryReceipt(MessageReceipt messageReceipt) {
         if (groupMessageAdapter!=null){
             groupMessageAdapter.setDelivered(messageReceipt);
         }
    }

    @Override
    public void onMessageRead(MessageReceipt messageReceipt) {
        if (groupMessageAdapter!=null){
            groupMessageAdapter.setRead(messageReceipt);
        }
    }

    @Override
    public void setDeletedMessage(BaseMessage baseMessage) {
        if (groupMessageAdapter!=null){
            groupMessageAdapter.deleteMessage(baseMessage);
        }
    }

    @Override
    public void setEditedMessage(BaseMessage baseMessage) {
        if (groupMessageAdapter!=null){
            groupMessageAdapter.setEditMessage(baseMessage);
        }
    }

    @Override
    public void setFilterList(List<BaseMessage> list) {
        if (groupMessageAdapter!=null){
            groupMessageAdapter.setFilteredList(list);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case StringContract.RequestCode.ADD_DOCUMENT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                    AttachmentHelper.selectMedia(this, StringContract.IntentStrings.DOCUMENT_TYPE,
                            null, StringContract.RequestCode.ADD_DOCUMENT);
                } else {
                    showToast();
                }
                break;

            case StringContract.RequestCode.ADD_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    AttachmentHelper.selectMedia(this, StringContract.IntentStrings.IMAGE_TYPE,
                            StringContract.IntentStrings.EXTRA_MIME_TYPE, StringContract.RequestCode.ADD_GALLERY);
                } else {
                    showToast();
                }
                break;

            case StringContract.RequestCode.TAKE_PHOTO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    AttachmentHelper.captureImage(this, StringContract.RequestCode.TAKE_PHOTO);
                } else {
                    showToast();
                }
                break;

            case StringContract.RequestCode.TAKE_VIDEO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    AttachmentHelper.captureVideo(this, StringContract.RequestCode.TAKE_VIDEO);
                } else {
                    showToast();
                }
                break;
            case StringContract.RequestCode.ADD_SOUND:
                if (CCPermissionHelper.hasPermissions(this, CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE)) {
                    AttachmentHelper.selectMedia(this, StringContract.IntentStrings.AUDIO_TYPE, null, StringContract.RequestCode.ADD_SOUND);
                } else {
                    CCPermissionHelper.requestPermissions(this,
                            new String[]{CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE}, StringContract.RequestCode.ADD_SOUND);
                }
                break;

            case StringContract.RequestCode.FILE_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    FileUtils.makeDirectory(this, CometChatConstants.MESSAGE_TYPE_AUDIO);
                } else {
                    showToast();
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && data != null && groupChatPresenter != null) {
            switch (requestCode) {
                case StringContract.RequestCode.ADD_GALLERY:
                    AttachmentHelper.handlefile(context, CometChatConstants.MESSAGE_TYPE_IMAGE, groupChatPresenter, data, groupId);
                    break;

                case StringContract.RequestCode.TAKE_PHOTO:
                    AttachmentHelper.handleCameraImage(this, groupChatPresenter, data, groupId);
                    break;

                case StringContract.RequestCode.TAKE_VIDEO:
                    AttachmentHelper.handleCameraVideo(this, groupChatPresenter, data, groupId);
                    break;

                case StringContract.RequestCode.ADD_DOCUMENT:
                    AttachmentHelper.handlefile(context, CometChatConstants.MESSAGE_TYPE_FILE, groupChatPresenter, data, groupId);
                    break;

                case StringContract.RequestCode.ADD_SOUND:
                    AttachmentHelper.handlefile(context, CometChatConstants.MESSAGE_TYPE_AUDIO, groupChatPresenter, data, groupId);
                    break;
            }
        }
    }

    private void showToast() {
        Toast.makeText(this, getString(R.string.pemssion_warning), Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setGroup(Group group) {
        this.group = group;
        if (group != null && group.getIcon() != null && !group.getIcon().isEmpty()) {
            groupChatPresenter.setGroupIcon(this, group.getIcon(), groupIcon);
        } else {
            try {
                Bitmap bitmap = MediaUtils.getPlaceholderImage(this, drawable);
                groupIcon.setCircleBackgroundColor(ColorUtils.getMaterialColor(this));
                groupIcon.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        getMenuInflater().inflate(R.menu.action_mode, menu);
        mode.setTitle(groupName);

        if (menu!=null&&baseMessage!=null){
            if (!baseMessage.getSender().getUid().equals(ownerId)) {
                menu.findItem(R.id.delete).setVisible(false);
                menu.findItem(R.id.info).setVisible(false);
            }

            if (!baseMessage.getSender().getUid().equals(ownerId) && baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)){
                menu.findItem(R.id.edit).setVisible(false);
            }


        }
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        mode.finish();

        switch (item.getItemId()) {

            case R.id.delete:
                groupChatPresenter.deleteMessage(baseMessage);
                break;
            case R.id.edit:
                TextMessage textMessage;
                isEditMessage=true;

                if (baseMessage instanceof TextMessage){
                    textMessage=(TextMessage)baseMessage;
                    messageField.setText(textMessage.getText());
                }
                break;

            case R.id.info:
                Intent intent =new Intent(this,MessageInfoActivity.class);
                intent.putExtra("category",baseMessage.getCategory());
                intent.putExtra("type",baseMessage.getType());
                intent.putExtra("id",baseMessage.getId());
                intent.putExtra("timestamp",baseMessage.getSentAt());
                intent.putExtra("senderUID",baseMessage.getSender().getUid());
                intent.putExtra("receiverUID",baseMessage.getReceiverUid());

                if (baseMessage instanceof TextMessage){
                    intent.putExtra("text",((TextMessage)baseMessage).getText());
                }
                if (baseMessage instanceof MediaMessage){
                    intent.putExtra("url",((MediaMessage)baseMessage).getUrl());
                }
                startActivity(intent);
                break;
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

    private void addAttachment(int type) {
        String[] STORAGE_PERMISSION = {CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE};
        String[] CAMERA_PERMISSION = {CCPermissionHelper.REQUEST_PERMISSION_CAMERA, CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE};
        switch (type) {
            case StringContract.RequestCode.ADD_GALLERY:

                if (CCPermissionHelper.hasPermissions(this, STORAGE_PERMISSION)) {
                    AttachmentHelper.selectMedia(this, StringContract.IntentStrings.IMAGE_TYPE, StringContract.IntentStrings.EXTRA_MIME_TYPE, StringContract.RequestCode.ADD_GALLERY);
                } else {
                    CCPermissionHelper.requestPermissions(this, STORAGE_PERMISSION, StringContract.RequestCode.ADD_GALLERY);
                }
                break;
            case StringContract.RequestCode.ADD_DOCUMENT:

                if (CCPermissionHelper.hasPermissions(this, STORAGE_PERMISSION)) {
                    AttachmentHelper.selectMedia(this, StringContract.IntentStrings.DOCUMENT_TYPE, null, StringContract.RequestCode.ADD_DOCUMENT);
                } else {
                    CCPermissionHelper.requestPermissions(this, STORAGE_PERMISSION, StringContract.RequestCode.ADD_DOCUMENT);
                }
                break;
            case StringContract.RequestCode.ADD_SOUND:
                if (CCPermissionHelper.hasPermissions(this, STORAGE_PERMISSION)) {
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
}
