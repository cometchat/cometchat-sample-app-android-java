package com.cometchat.pro.uikit.ui_components.messages.message_list;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupMembersRequest;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.helpers.CometChatHelper;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Attachment;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.TypingIndicator;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.groups.group_details.CometChatGroupDetailActivity;
import com.cometchat.pro.uikit.ui_components.messages.extensions.ExtensionResponseListener;
import com.cometchat.pro.uikit.ui_components.messages.extensions.Extensions;
import com.cometchat.pro.uikit.ui_components.messages.forward_message.CometChatForwardMessageActivity;
import com.cometchat.pro.uikit.ui_components.messages.live_reaction.LiveReactionListener;
import com.cometchat.pro.uikit.ui_components.messages.live_reaction.ReactionClickListener;
import com.cometchat.pro.uikit.ui_components.messages.message_actions.CometChatMessageActions;
import com.cometchat.pro.uikit.ui_components.messages.message_actions.listener.MessageActionCloseListener;
import com.cometchat.pro.uikit.ui_components.messages.message_actions.listener.OnMessageLongClick;
import com.cometchat.pro.uikit.ui_components.messages.message_information.CometChatMessageInfoScreenActivity;
import com.cometchat.pro.uikit.ui_components.messages.thread_message_list.CometChatThreadMessageListActivity;
import com.cometchat.pro.uikit.ui_components.shared.CometChatSnackBar;
import com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar;
import com.cometchat.pro.uikit.ui_components.shared.cometchatComposeBox.CometChatComposeBox;
import com.cometchat.pro.uikit.ui_components.shared.cometchatComposeBox.listener.ComposeActionListener;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.CometChatReactionDialog;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.listener.OnReactionClickListener;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.model.Reaction;
import com.cometchat.pro.uikit.ui_components.shared.cometchatSmartReplies.CometChatSmartReplies;
import com.cometchat.pro.uikit.ui_components.shared.cometchatStickers.StickerView;
import com.cometchat.pro.uikit.ui_components.shared.cometchatStickers.listener.StickerClickListener;
import com.cometchat.pro.uikit.ui_components.shared.cometchatStickers.model.Sticker;
import com.cometchat.pro.uikit.ui_components.users.user_details.CometChatUserDetailScreenActivity;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_resources.utils.CallUtils;
import com.cometchat.pro.uikit.ui_resources.utils.CometChatError;
import com.cometchat.pro.uikit.ui_resources.utils.FontUtils;
import com.cometchat.pro.uikit.ui_resources.utils.MediaUtils;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.cometchat.pro.uikit.ui_resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.pro.uikit.ui_resources.utils.keyboard_utils.KeyBoardUtils;
import com.cometchat.pro.uikit.ui_resources.utils.pattern_utils.PatternUtils;
import com.cometchat.pro.uikit.ui_resources.utils.sticker_header.StickyHeaderDecoration;
import com.cometchat.pro.uikit.ui_settings.FeatureRestriction;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

/**
 * Purpose - CometChatMessageScreen class is a fragment used to display list of messages and perform certain action on click of message.
 * It also provide search bar to perform search operation on the list of messages. User can send text,images,video and file as messages
 * to each other and in groups. User can also perform actions like edit message,delete message and forward messages to other user and groups.
 *
 * @see CometChat
 * @see User
 * @see Group
 * @see TextMessage
 * @see MediaMessage
 * <p>
 * Created on - 20th December 2019
 * <p>
 * Modified on  - 16th January 2020
 */


public class CometChatMessageList extends Fragment implements View.OnClickListener,
        OnMessageLongClick, MessageActionCloseListener {

    private static final String TAG = "CometChatMessageScreen";

    private static final int LIMIT = 30;

    private LinearLayout bottomLayout;

    private String name = "";

    private String status = "";

    private MessagesRequest messagesRequest;    //Used to fetch messages.

    private CometChatComposeBox composeBox;

    private RecyclerView rvChatListView;    //Used to display list of messages.

    private MessageAdapter messageAdapter;

    private LinearLayoutManager linearLayoutManager;

    private CometChatSmartReplies rvSmartReply;

    private ShimmerFrameLayout messageShimmer;

    /**
     * <b>Avatar</b> is a UI Kit Component which is used to display user and group avatars.
     */
    private CometChatAvatar userAvatar;

    private TextView tvName;

    private TextView tvStatus;

    private String Id;

    private Context context;

    private LinearLayout blockUserLayout;

    private MaterialButton unblockBtn;

    private TextView blockedUserName;

    private StickyHeaderDecoration stickyHeaderDecoration;

    private String avatarUrl;

    private String profileLink;

    private Toolbar toolbar;

    private String type;

    private String groupType;

    private boolean isBlockedByMe;

    private String loggedInUserScope;

    private RelativeLayout editMessageLayout;

    private TextView tvMessageTitle;

    private TextView tvMessageSubTitle;

    private RelativeLayout replyMessageLayout;

    private TextView replyTitle;

    private TextView replyMessage;

    private ImageView replyMedia;

    private ImageView replyClose;

    private BaseMessage baseMessage;

    private List<BaseMessage> baseMessages = new ArrayList<>();

    private List<BaseMessage> messageList = new ArrayList<>();

    private boolean isEdit;

    private boolean isReply;

    private String groupOwnerId;

    private int memberCount;

    private String memberNames;

    private String groupDesc;

    private String groupPassword;

    private Timer timer = new Timer();

    private Timer typingTimer = new Timer();

    private View view;

    private boolean isNoMoreMessages;

    private FontUtils fontUtils;

    private User loggedInUser = CometChat.getLoggedInUser();

    String[] CAMERA_PERMISSION = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private boolean isInProgress;

    private boolean isSmartReplyClicked;

    private RelativeLayout onGoingCallView;

    private TextView onGoingCallTxt;

    private ImageView onGoingCallClose;

    public int count = 0;

    private CometChatMessageActions messageActionFragment;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double LATITUDE = 0;
    double LONGITUDE = 0;
    private ArrayList<View> optionsArrayList = new ArrayList<>();

    private StickerView stickersView;
    private RelativeLayout stickerLayout;
    private ImageView closeStickerView;

    private boolean isKeyboardVisible;
    private int resultIntentCode;

    private ImageView imageView;
    private FrameLayout container;
    public ObjectAnimator animation;

    private View newMessageLayout;
    private TextView newMessageLayoutText;
    private int newMessageCount = 0;

    boolean shareVisible;
    boolean copyVisible;
    boolean forwardVisible;
    boolean threadVisible;
    boolean replyVisible;
    boolean translateVisible;
    boolean reactionVisible;
    boolean editVisible;
    boolean retryVisible = false;
    boolean replyPrivately;
    boolean deleteVisible;


    boolean hideDeleteMessage;

    private ImageView infoAction;
    private ImageView audioCallAction;
    private ImageView videoCallAction;

    private ImageView backIcon;
    private BaseMessage repliedMessage;

    public CometChatMessageList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleArguments();
        if (getActivity() != null)
            fontUtils = FontUtils.getInstance(getActivity());
    }

    /**
     * This method is used to handle arguments passed to this fragment.
     */
    private void handleArguments() {
        if (getArguments() != null) {
            Id = getArguments().getString(UIKitConstants.IntentStrings.UID);
            avatarUrl = getArguments().getString(UIKitConstants.IntentStrings.AVATAR);
            status = getArguments().getString(UIKitConstants.IntentStrings.STATUS);
            name = getArguments().getString(UIKitConstants.IntentStrings.NAME);
            profileLink = getArguments().getString(UIKitConstants.IntentStrings.LINK);
            type = getArguments().getString(UIKitConstants.IntentStrings.TYPE);
            if (type != null && type.equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                Id = getArguments().getString(UIKitConstants.IntentStrings.GUID);
                memberCount = getArguments().getInt(UIKitConstants.IntentStrings.MEMBER_COUNT);
                groupDesc = getArguments().getString(UIKitConstants.IntentStrings.GROUP_DESC);
                groupPassword = getArguments().getString(UIKitConstants.IntentStrings.GROUP_PASSWORD);
                groupType = getArguments().getString(UIKitConstants.IntentStrings.GROUP_TYPE);
            }

            String message = getArguments().getString(UIKitConstants.IntentStrings.MESSAGE);
            if (message!=null) {
                try {
                    JSONObject repliedMessageJSON = new JSONObject(message);
                    repliedMessage = CometChatHelper.processMessage(repliedMessageJSON);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cometchat_messagelist, container, false);
        initViewComponent(view);
        return view;
    }


    /**
     * This is a main method which is used to initialize the view for this fragment.
     *
     * @param view
     */
    private void initViewComponent(View view) {

        setHasOptionsMenu(true);

        CometChatError.init(getContext());
        backIcon = view.findViewById(R.id.back_action);
        infoAction = view.findViewById(R.id.info_action);
        audioCallAction = view.findViewById(R.id.audio_call_action);
        videoCallAction = view.findViewById(R.id.video_call_action);

        backIcon.setOnClickListener(this);
        infoAction.setOnClickListener(this);
        audioCallAction.setOnClickListener(this);
        videoCallAction.setOnClickListener(this);

        bottomLayout = view.findViewById(R.id.bottom_layout);
        messageShimmer = view.findViewById(R.id.shimmer_layout);
        composeBox = view.findViewById(R.id.message_box);
        composeBox.usedIn(CometChatMessageListActivity.class.getName());
        if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            composeBox.hideGroupCallOption(true);

        setComposeBoxListener();
        if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER)) {
            FeatureRestriction.isOneOnOneChatEnabled(new FeatureRestriction.OnSuccessListener() {
                @Override
                public void onSuccess(Boolean booleanVal) {
                    if (booleanVal)
                        composeBox.setVisibility(View.VISIBLE);
                    else
                        composeBox.setVisibility(View.GONE);
                }
            });
        } else if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP)) {
            FeatureRestriction.isGroupChatEnabled(new FeatureRestriction.OnSuccessListener() {
                @Override
                public void onSuccess(Boolean booleanVal) {
                    if (booleanVal)
                        composeBox.setVisibility(View.VISIBLE);
                    else
                        composeBox.setVisibility(GONE);
                }
            });
        }

        container = view.findViewById(R.id.reactions_container);
        composeBox.liveReactionBtn.setOnTouchListener(new LiveReactionListener(700, 1000, new ReactionClickListener() {
            @Override
            public void onClick(View var1) {
                container.setAlpha(1.0f);
                sendLiveReaction();
            }

            @Override
            public void onCancel(View var1) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (imageView!=null && animation!=null && animation.isRunning()) {
                            ObjectAnimator animator = ObjectAnimator.ofFloat(container,"alpha",0.2f);
                            animator.setDuration(700);
                            animator.start();
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    if (imageView!=null)
                                        imageView.clearAnimation();
                                    container.removeAllViews();
                                    if (typingTimer!=null)
                                        typingTimer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                JSONObject metaData = new JSONObject();
                                                try {
                                                    metaData.put("reaction", "heart");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                TypingIndicator typingIndicator = new TypingIndicator(Id, type, metaData);
                                                CometChat.endTyping(typingIndicator);
                                            }
                                        },2000);
                                }
                            });
                        }
                    }
                },1400);
            }
        }));


        newMessageLayout = view.findViewById(R.id.new_message_layout);
        newMessageLayoutText = view.findViewById(R.id.new_message_tv);
        newMessageLayout.setVisibility(GONE);

        rvSmartReply = view.findViewById(R.id.rv_smartReply);

        editMessageLayout = view.findViewById(R.id.editMessageLayout);
        tvMessageTitle = view.findViewById(R.id.tv_message_layout_title);
        tvMessageSubTitle = view.findViewById(R.id.tv_message_layout_subtitle);
        ImageView ivMessageClose = view.findViewById(R.id.iv_message_close);
        ivMessageClose.setOnClickListener(this);

        stickersView = view.findViewById(R.id.stickersView);
        stickerLayout = view.findViewById(R.id.sticker_layout);
        closeStickerView = view.findViewById(R.id.close_sticker_layout);

        closeStickerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerLayout.setVisibility(GONE);
            }
        });

        stickersView.setStickerClickListener(new StickerClickListener() {
            @Override
            public void onClickListener(Sticker sticker) {
                JSONObject stickerData = new JSONObject();
                try {
                    stickerData.put("sticker_url",sticker.getUrl());
                    stickerData.put("sticker_name",sticker.getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendCustomMessage(UIKitConstants.IntentStrings.STICKERS,stickerData);
                stickerLayout.setVisibility(GONE);
            }
        });

        replyMessageLayout = view.findViewById(R.id.replyMessageLayout);
        replyTitle = view.findViewById(R.id.tv_reply_layout_title);
        replyMessage = view.findViewById(R.id.tv_reply_layout_subtitle);
        replyMedia = view.findViewById(R.id.iv_reply_media);
        replyClose = view.findViewById(R.id.iv_reply_close);
        replyClose.setOnClickListener(this);

        if (repliedMessage!=null) {
            baseMessage = repliedMessage;
            replyMessage();
        }

        rvChatListView = view.findViewById(R.id.rv_message_list);
        MaterialButton unblockUserBtn = view.findViewById(R.id.btn_unblock_user);
        unblockUserBtn.setOnClickListener(this);
        blockedUserName = view.findViewById(R.id.tv_blocked_user_name);
        unblockBtn = view.findViewById(R.id.btn_unblock_user);
        blockUserLayout = view.findViewById(R.id.blocked_user_layout);
        tvName = view.findViewById(R.id.tv_name);
        tvStatus = view.findViewById(R.id.tv_status);
        userAvatar = view.findViewById(R.id.iv_chat_avatar);
        toolbar = view.findViewById(R.id.chatList_toolbar);
        toolbar.setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        tvName.setTypeface(fontUtils.getTypeFace(FontUtils.robotoMedium));
        tvName.setText(name);
        setAvatar();

        rvChatListView.setLayoutManager(linearLayoutManager);

        if (Utils.isDarkMode(context)) {
            bottomLayout.setBackgroundColor(getResources().getColor(R.color.darkModeBackground));
            toolbar.setBackgroundColor(getResources().getColor(R.color.grey));
            editMessageLayout.setBackground(getResources().getDrawable(R.drawable.left_border_dark));
            replyMessageLayout.setBackground(getResources().getDrawable(R.drawable.left_border_dark));
            composeBox.setBackgroundColor(getResources().getColor(R.color.darkModeBackground));
            rvChatListView.setBackgroundColor(getResources().getColor(R.color.darkModeBackground));
            tvName.setTextColor(getResources().getColor(R.color.textColorWhite));
        } else {
            bottomLayout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            toolbar.setBackgroundColor(getResources().getColor(R.color.textColorWhite));
            editMessageLayout.setBackground(getResources().getDrawable(R.drawable.left_border));
            replyMessageLayout.setBackground(getResources().getDrawable(R.drawable.left_border));
            composeBox.setBackgroundColor(getResources().getColor(R.color.textColorWhite));
            rvChatListView.setBackgroundColor(getResources().getColor(R.color.textColorWhite));
            tvName.setTextColor(getResources().getColor(R.color.primaryTextColor));
        }

        KeyBoardUtils.setKeyboardVisibilityListener(getActivity(), (View) rvChatListView.getParent(), keyboardVisible -> {
            isKeyboardVisible = keyboardVisible;
            if (keyboardVisible) {
                scrollToBottom();
                composeBox.ivMic.setVisibility(GONE);
                composeBox.ivSend.setVisibility(View.VISIBLE);
            } else {
                if (isEdit) {
                    composeBox.ivMic.setVisibility(GONE);
                    composeBox.ivSend.setVisibility(View.VISIBLE);
                }else {
                    FeatureRestriction.isVoiceNotesEnabled(new FeatureRestriction.OnSuccessListener() {
                        @Override
                        public void onSuccess(Boolean booleanVal) {
                            if (booleanVal) {
                                composeBox.ivMic.setVisibility(View.VISIBLE);
                                composeBox.ivSend.setVisibility(GONE);
                            } else {
                                composeBox.ivMic.setVisibility(GONE);
                                composeBox.ivSend.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });


        // Uses to fetch next list of messages if rvChatListView (RecyclerView) is scrolled in downward direction.
        rvChatListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                //for toolbar elevation animation i.e stateListAnimator
                toolbar.setSelected(rvChatListView.canScrollVertically(-1));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                    if (!isNoMoreMessages && !isInProgress) {
                        if (linearLayoutManager.findFirstVisibleItemPosition() == 10 || !rvChatListView.canScrollVertically(-1)) {
                            isInProgress = true;
                            fetchMessage();
                        }
                    }
                    if (messageAdapter!=null && ((messageAdapter.getItemCount() - 1) - linearLayoutManager.findLastVisibleItemPosition() < 5)) {
                        newMessageLayout.setVisibility(GONE);
                        newMessageCount = 0;
                    }
            }

        });
        rvSmartReply.setItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void OnItemClick(String var, int position) {
                if (!isSmartReplyClicked) {
                    isSmartReplyClicked = true;
                    rvSmartReply.setVisibility(GONE);
                    sendMessage(var);
                }
            }
        });
        fetchSettings();

        //Check Ongoing Call
        onGoingCallView = view.findViewById(R.id.ongoing_call_view);
        onGoingCallClose = view.findViewById(R.id.close_ongoing_view);
        onGoingCallTxt = view.findViewById(R.id.ongoing_call);
        checkOnGoingCall();
    }

        private void checkOnGoingCall(String callType) {
        if(CometChat.getActiveCall()!=null && CometChat.getActiveCall().getCallStatus().equals(CometChatConstants.CALL_STATUS_ONGOING) && CometChat.getActiveCall().getSessionId()!=null) {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setTitle(getResources().getString(R.string.ongoing_call))
                    .setMessage(getResources().getString(R.string.ongoing_call_message))
                    .setPositiveButton(getResources().getString(R.string.join), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CallUtils.joinOnGoingCall(context,CometChat.getActiveCall());
                        }
                    }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    audioCallAction.setClickable(true);
                    videoCallAction.setClickable(true);
                }
            }).create().show();
        }
        else {
            Call call = null;
            if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP))
                call = new Call(Id, CometChatConstants.RECEIVER_TYPE_GROUP,callType);
            else
                call = new Call(Id, CometChatConstants.RECEIVER_TYPE_USER,callType);
            CometChat.initiateCall(call, new CometChat.CallbackListener<Call>() {
                @Override
                public void onSuccess(Call call) {
                    CallUtils.startCallIntent(context,
                            ((User)call.getCallReceiver()),call.getType(),true,
                            call.getSessionId());
                }

                @Override
                public void onError(CometChatException e) {
                    audioCallAction.setClickable(true);
                    videoCallAction.setClickable(true);
                    Log.e(TAG, "onError: "+e.getMessage());
                    CometChatSnackBar.show(context,
                            rvChatListView,
                            CometChatError.localized(e),CometChatSnackBar.ERROR);
                }
            });
        }
    }
    /**
     * This method is used to check if the app has ongoing call or not and based on it show the view
     * through which user can join ongoing call.
     *
     */
    private void checkOnGoingCall() {
            if(CometChat.getActiveCall()!=null
                    && (CometChat.getActiveCall().getReceiverUid().equalsIgnoreCase(Id) ||
                            CometChat.getActiveCall().getReceiverUid()
                                    .equalsIgnoreCase(loggedInUser.getUid()))
                    && CometChat.getActiveCall().getCallStatus().
                            equals(CometChatConstants.CALL_STATUS_ONGOING)
                    && CometChat.getActiveCall().getSessionId()!=null) {

                if(onGoingCallView!=null)
                    onGoingCallView.setVisibility(View.VISIBLE);
                if(onGoingCallTxt!=null) {
                    onGoingCallTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onGoingCallView.setVisibility(View.GONE);
                            if (CometChat.getActiveCall()!=null)
                                CallUtils.joinOnGoingCall(context,CometChat.getActiveCall());
                            else {
                                Toast.makeText(context,getString(R.string.call_ended),Toast.LENGTH_LONG).show();
                                onGoingCallView.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                if(onGoingCallClose!=null) {
                    onGoingCallClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onGoingCallView.setVisibility(GONE);
                        }
                    });
                }
            } else if (CometChat.getActiveCall()!=null){
                if (onGoingCallView!=null)
                    onGoingCallView.setVisibility(GONE);
                Log.e(TAG, "checkOnGoingCall: "+CometChat.getActiveCall().toString());
            }
    }

    private void setComposeBoxListener() {

        composeBox.setComposeBoxListener(new ComposeActionListener() {
            @Override
            public void onEditTextMediaSelected(InputContentInfoCompat inputContentInfo) {
                Log.e(TAG, "onEditTextMediaSelected: Path=" + inputContentInfo.getLinkUri().getPath()
                        + "\nHost=" + inputContentInfo.getLinkUri().getFragment());
                String messageType = inputContentInfo.getLinkUri().toString().substring(inputContentInfo.getLinkUri().toString().lastIndexOf('.'));
                MediaMessage mediaMessage = new MediaMessage(Id, null, CometChatConstants.MESSAGE_TYPE_IMAGE, type);
                Attachment attachment = new Attachment();
                attachment.setFileUrl(inputContentInfo.getLinkUri().toString());
                attachment.setFileMimeType(inputContentInfo.getDescription().getMimeType(0));
                attachment.setFileExtension(messageType);
                attachment.setFileName(inputContentInfo.getDescription().getLabel().toString());
                mediaMessage.setAttachment(attachment);
                Log.e(TAG, "onClick: " + attachment.toString());
                sendMediaMessage(mediaMessage);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    sendTypingIndicator(false);
                } else {
                    sendTypingIndicator(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    composeBox.hideSendButton(false);
                    composeBox.hideRecordOption(true);
                } else {
                    composeBox.hideSendButton(true);
                    composeBox.hideRecordOption(false);
                }
                if (typingTimer == null) {
                    typingTimer = new Timer();
                }
                endTypingTimer();
            }

            @Override
            public void onAudioActionClicked() {
                if (Utils.hasPermissions(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    startActivityForResult(MediaUtils.openAudio(getActivity()), UIKitConstants.RequestCode.AUDIO);
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, UIKitConstants.RequestCode.AUDIO);
                }
            }

            @Override
            public void onCameraActionClicked() {
                if (Utils.hasPermissions(getContext(), CAMERA_PERMISSION)) {
                    startActivityForResult(MediaUtils.openCamera(getContext()), UIKitConstants.RequestCode.CAMERA);
                } else {
                    requestPermissions(CAMERA_PERMISSION, UIKitConstants.RequestCode.CAMERA);
                }
            }


            @Override
            public void onGalleryActionClicked() {
                if (Utils.hasPermissions(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    startActivityForResult(MediaUtils.openGallery(getActivity()), UIKitConstants.RequestCode.GALLERY);
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, UIKitConstants.RequestCode.GALLERY);
                }
            }

            @Override
            public void onFileActionClicked() {
                if (Utils.hasPermissions(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    startActivityForResult(MediaUtils.getFileIntent(UIKitConstants.IntentStrings.EXTRA_MIME_DOC), UIKitConstants.RequestCode.FILE);
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, UIKitConstants.RequestCode.FILE);
                }
            }

            @Override
            public void onSendActionClicked(EditText editText) {
                String message = editText.getText().toString().trim();
                String filterEmojiMessage = PatternUtils.removeEmojiAndSymbol(message);
                editText.setText("");
                editText.setHint(getString(R.string.message));
                FeatureRestriction.isEmojisEnabled(new FeatureRestriction.OnSuccessListener() {
                    @Override
                    public void onSuccess(Boolean booleanVal) {
                        if (!booleanVal) {
                            if (filterEmojiMessage.trim().length() > 0) {
                                if (isEdit) {
                                    editMessage(baseMessage, message);
                                    editMessageLayout.setVisibility(GONE);
                                } else if (isReply) {
                                    replyMessage(baseMessage, message);
                                    replyMessageLayout.setVisibility(GONE);
                                } else if (!message.isEmpty())
                                    sendMessage(message);
                            } else {
                                Toast.makeText(getContext(), "Emoji Support is not enabled",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else  {
                            if (isEdit) {
                                editMessage(baseMessage, message);
                                editMessageLayout.setVisibility(GONE);
                            } else if (isReply) {
                                replyMessage(baseMessage, message);
                                replyMessageLayout.setVisibility(GONE);
                            } else if (!message.isEmpty())
                                sendMessage(message);
                        }
                    }
                });
            }

            @Override
            public void onVoiceNoteComplete(String string) {
                if (string != null) {
                    File audioFile = new File(string);
                    sendMediaMessage(audioFile, CometChatConstants.MESSAGE_TYPE_AUDIO);
                }
            }

            @Override
            public void onLocationActionClicked() {
                if (Utils.hasPermissions(getContext(), Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    initLocation();
//                    locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
                    boolean provider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if (!provider) {
                        turnOnLocation();
                    } else {
                        getLocation();
                    }
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, UIKitConstants.RequestCode.LOCATION);
                }
            }

            @Override
            public void onPollActionClicked() {
                createPollDialog();
            }

            @Override
            public void onStickerClicked() {
                stickerLayout.setVisibility(View.VISIBLE);
                Extensions.fetchStickers(new ExtensionResponseListener() {
                    @Override
                    public void OnResponseSuccess(Object var) {
                        JSONObject stickersJSON = (JSONObject) var;
                        stickersView.setData(Id, type, Extensions.extractStickersFromJSON(stickersJSON));
                    }
                    @Override
                    public void OnResponseFailed(CometChatException e) {
                        CometChatSnackBar.show(context,stickersView, CometChatError.localized(e), CometChatSnackBar.ERROR);
                    }
                });
            }

            @Override
            public void onWhiteboardClicked() {
                Extensions.callWhiteBoardExtension(Id, type, new ExtensionResponseListener() {
                    @Override
                    public void OnResponseSuccess(Object var) {
                        JSONObject jsonObject = (JSONObject)var;
                    }

                    @Override
                    public void OnResponseFailed(CometChatException e) {
                        CometChatSnackBar.show(context,rvChatListView, CometChatError.localized(e), CometChatSnackBar.ERROR);
                    }
                });
            }

            @Override
            public void onWriteboardClicked() {
                Extensions.callWriteBoardExtension(Id, type, new ExtensionResponseListener() {
                    @Override
                    public void OnResponseSuccess(Object var) {
                        JSONObject jsonObject = (JSONObject)var;
                    }

                    @Override
                    public void OnResponseFailed(CometChatException e) {
                        CometChatSnackBar.show(context,rvChatListView, CometChatError.localized(e), CometChatSnackBar.ERROR);
                    }
                });
            }
        });
    }


    private void createPollDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        View view = LayoutInflater.from(context).inflate(R.layout.add_polls_layout,null);
        alertDialog.setView(view);
        Dialog dialog = alertDialog.create();
        optionsArrayList.clear();
        EditText questionEdt = view.findViewById(R.id.question_edt);
        View option1 = view.findViewById(R.id.option_1);
        View option2 = view.findViewById(R.id.option_2);
        option1.findViewById(R.id.delete_option).setVisibility(GONE);
        option2.findViewById(R.id.delete_option).setVisibility(GONE);
        MaterialCardView addOption = view.findViewById(R.id.add_options);
        LinearLayout optionLayout = view.findViewById(R.id.options_layout);
        MaterialButton addPoll = view.findViewById(R.id.add_poll);
        addPoll.setBackgroundColor(Color.parseColor(FeatureRestriction.getColor()));
        ImageView cancelPoll = view.findViewById(R.id.close_poll);
        addOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View optionView = LayoutInflater.from(context).inflate(R.layout.polls_option,null);
                optionsArrayList.add(optionView);
                optionLayout.addView(optionView);
                optionView.findViewById(R.id.delete_option).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        optionsArrayList.remove(optionView);
                        optionLayout.removeView(optionView);
                    }
                });
            }
        });
        addPoll.setEnabled(true);
        addPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionEdt.requestFocus();
                EditText option1Text = option1.findViewById(R.id.option_txt);
                EditText option2Text = option2.findViewById(R.id.option_txt);
                if (questionEdt.getText().toString().trim().isEmpty())
                    questionEdt.setError(getString(R.string.fill_this_field));
                else if (option1Text.getText().toString().trim().isEmpty())
                    option1Text.setError(getString(R.string.fill_this_field));
                else if (option2Text.getText().toString().trim().isEmpty())
                    option2Text.setError(getString(R.string.fill_this_field));
                else {
                    ProgressDialog progressDialog;
                    progressDialog = ProgressDialog.show(context, "", getResources().getString(R.string.create_a_poll));
                    addPoll.setEnabled(false);
                    try {
                        JSONArray optionJson = new JSONArray();
                        optionJson.put(option1Text.getText().toString());
                        optionJson.put(option2Text.getText().toString());
                        for(View views : optionsArrayList) {
                            EditText optionsText = views.findViewById(R.id.option_txt);
                            if (!optionsText.getText().toString().trim().isEmpty())
                                optionJson.put(optionsText.getText().toString());
                        }
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("question", questionEdt.getText().toString());
                        jsonObject.put("options", optionJson);
                        jsonObject.put("receiver", Id);
                        jsonObject.put("receiverType", type);
                        CometChat.callExtension("polls", "POST", "/v2/create",
                                jsonObject, new CometChat.CallbackListener<JSONObject>() {
                                    @Override
                                    public void onSuccess(JSONObject jsonObject) {
                                        progressDialog.dismiss();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onError(CometChatException e) {
                                        progressDialog.dismiss();
                                        addPoll.setEnabled(true);
                                        Log.e(TAG, "onErrorCallExtension: "+e.getMessage());
                                        if (view!=null)
                                            CometChatSnackBar.show(context,view,
                                                CometChatError.Extension.localized(e,"polls")
                                                    , CometChatSnackBar.ERROR);
                                    }
                                });
                    } catch (Exception e) {
                        addPoll.setEnabled(true);
                        Log.e(TAG, "onErrorJSON: "+e.getMessage());
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        cancelPoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getLocation() {

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)
                {
                    double lon = location.getLongitude();
                    double lat = location.getLatitude();

                    JSONObject customData = new JSONObject();
                    try {
                        customData.put("latitude", lat);
                        customData.put("longitude", lon);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    initAlert(customData);
                } else {
                    Toast.makeText(context,getString(R.string.unable_to_get_location),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void initAlert(JSONObject customData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(context).inflate(R.layout.map_share_layout,null);
        builder.setView(view);
        try {
            LATITUDE = customData.getDouble("latitude");
            LONGITUDE = customData.getDouble("longitude");
        } catch (JSONException e) {
        e.printStackTrace();
        }
        TextView address = view.findViewById(R.id.address);
        address.setText("Address: "+ Utils.getAddress(context,LATITUDE,LONGITUDE));
        ImageView mapView = view.findViewById(R.id.map_vw);
        String mapUrl = UIKitConstants.MapUrl.MAPS_URL +LATITUDE+","+LONGITUDE+"&key="+
                UIKitConstants.MapUrl.MAP_ACCESS_KEY;
        Glide.with(this)
                .load(mapUrl)
                .placeholder(R.drawable.default_map)
                .into(mapView);

        builder.setPositiveButton(getString(R.string.share), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendCustomMessage(UIKitConstants.IntentStrings.LOCATION, customData);
            }
        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    /**
     * This method sends custom message based on parameter received
     * @param customType
     * @param customData
     */
    private void sendCustomMessage(String customType, JSONObject customData) {
        CustomMessage customMessage;

        if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            customMessage = new CustomMessage(Id, CometChatConstants.RECEIVER_TYPE_USER, customType, customData);
        else
            customMessage = new CustomMessage(Id, CometChatConstants.RECEIVER_TYPE_GROUP, customType, customData);

        String pushNotificationMessage="";
        if (customType.equalsIgnoreCase(UIKitConstants.IntentStrings.LOCATION))
            pushNotificationMessage = getString(R.string.shared_location);
        else if (customType.equalsIgnoreCase(UIKitConstants.IntentStrings.STICKERS))
            pushNotificationMessage = getString(R.string.shared_a_sticker);
        try {
            JSONObject jsonObject = customMessage.getMetadata();
            if (jsonObject==null) {
                jsonObject = new JSONObject();
                jsonObject.put("incrementUnreadCount", true);
                jsonObject.put("pushNotification",pushNotificationMessage);
            } else {
                jsonObject.accumulate("incrementUnreadCount", true);
            }
            customMessage.setMetadata(jsonObject);
        } catch(Exception e) {
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        CometChat.sendCustomMessage(customMessage, new CometChat.CallbackListener<CustomMessage>() {
            @Override
            public void onSuccess(CustomMessage customMessage) {
                Log.e(TAG, "onSuccessCustomMesage: "+customMessage.toString());
                if (customType.equalsIgnoreCase(UIKitConstants.IntentStrings.GROUP_CALL)) {
                    if (CometChat.getActiveCall() == null)
                        CallUtils.startDirectCall(context, Utils.getDirectCallData(customMessage));
                }

                if (messageAdapter != null) {
                    messageAdapter.addMessage(customMessage);
                    scrollToBottom();
                }
            }

            @Override
            public void onError(CometChatException e) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void turnOnLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.turn_on_gps));
        builder.setPositiveButton(getString(R.string.on), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), UIKitConstants.RequestCode.LOCATION);
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void initLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {}
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: ");
        switch (requestCode) {

            case UIKitConstants.RequestCode.CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)

                    startActivityForResult(MediaUtils.openCamera(getActivity()), UIKitConstants.RequestCode.CAMERA);
                else
                    showPermissionSnackBar(view.findViewById(R.id.message_box), getResources().getString(R.string.grant_camera_permission));
                break;
            case UIKitConstants.RequestCode.GALLERY:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startActivityForResult(MediaUtils.openGallery(getActivity()), UIKitConstants.RequestCode.GALLERY);
                else
                    showPermissionSnackBar(view.findViewById(R.id.message_box), getResources().getString(R.string.grant_storage_permission));
                break;
            case UIKitConstants.RequestCode.FILE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startActivityForResult(MediaUtils.getFileIntent(UIKitConstants.IntentStrings.EXTRA_MIME_DOC), UIKitConstants.RequestCode.FILE);
                else
                    showPermissionSnackBar(view.findViewById(R.id.message_box), getResources().getString(R.string.grant_storage_permission));
                break;
            case UIKitConstants.RequestCode.LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLocation();
//                    locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
                    boolean provider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if (!provider) {
                        turnOnLocation();
                    }
                    else {
                        getLocation();
                    }
                }
                else
                    showPermissionSnackBar(view.findViewById(R.id.message_box), getResources().getString(R.string.grant_location_permission));
                break;
        }
    }

    private void showPermissionSnackBar(View view, String message) {
        CometChatSnackBar.show(context,view,message, CometChatSnackBar.WARNING);
    }

    /**
     * This method is used to get Group Members and display names of group member.
     *
     * @see GroupMember
     * @see GroupMembersRequest
     */
    private void getMember() {
        GroupMembersRequest groupMembersRequest = new GroupMembersRequest.GroupMembersRequestBuilder(Id).setLimit(30).build();

        groupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> list) {
                String s[] = new String[0];
                if (list != null && list.size() != 0) {
                    s = new String[list.size()];
                    for (int j = 0; j < list.size(); j++) {

                        s[j] = list.get(j).getName();
                    }

                }
                setSubTitle(s);

            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "Group Member list fetching failed with exception: " + e.getMessage());
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    /**
     * Incase if user is blocked already, then this method is used to unblock the user .
     *
     * @see CometChat#unblockUsers(List, CometChat.CallbackListener)
     */
    private void unblockUser() {
        ArrayList<String> uids = new ArrayList<>();
        uids.add(Id);
        CometChat.unblockUsers(uids, new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> stringStringHashMap) {
                CometChatSnackBar.show(context,rvChatListView,
                        name+" "+getResources().getString(R.string.unblocked_successfully), CometChatSnackBar.SUCCESS);
                blockUserLayout.setVisibility(GONE);
                composeBox.setVisibility(View.VISIBLE);
                isBlockedByMe = false;
                messagesRequest=null;
            }

            @Override
            public void onError(CometChatException e) {
                CometChatSnackBar.show(context,rvChatListView,e.getMessage(), CometChatSnackBar.ERROR);
            }
        });
    }

    /**
     * This method is used to set GroupMember names as subtitle in toolbar.
     *
     * @param users
     */
    private void setSubTitle(String... users) {
        if (users != null && users.length != 0) {
            StringBuilder stringBuilder = new StringBuilder();

            for (String user : users) {
                stringBuilder.append(user).append(",");
            }

            memberNames = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();

            tvStatus.setText(memberNames);
        }

    }


    /**
     * This method is used to fetch message of users & groups. For user it fetches previous 100 messages at
     * a time and for groups it fetches previous 30 messages. You can change limit of messages by modifying
     * number in <code>setLimit()</code>
     * This method also mark last message as read using markMessageAsRead() present in this class.
     * So all the above messages get marked as read.
     *
     * @see MessagesRequest#fetchPrevious(CometChat.CallbackListener)
     */
    private void fetchMessage() {

        if (messagesRequest == null) {
            if (type != null) {

                if (type.equals(CometChatConstants.RECEIVER_TYPE_USER))
                    messagesRequest = new MessagesRequest.MessagesRequestBuilder().setLimit(LIMIT)
                            .setTypes(UIKitConstants.MessageRequest.messageTypesForUser)
                            .setCategories(UIKitConstants.MessageRequest.messageCategoriesForUser)
                            .hideReplies(true).hideDeletedMessages(hideDeleteMessage).setUID(Id).build();
                else
                    messagesRequest = new MessagesRequest.MessagesRequestBuilder().setLimit(LIMIT)
                            .setTypes(UIKitConstants.MessageRequest.messageTypesForGroup)
                            .setCategories(UIKitConstants.MessageRequest.messageCategoriesForGroup)
                            .hideReplies(true).hideDeletedMessages(hideDeleteMessage).setGUID(Id).build();
            }
        }
        messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {

            @Override
            public void onSuccess(List<BaseMessage> baseMessages) {
                isInProgress = false;
//                List<BaseMessage> filteredMessageList = filterBaseMessages(baseMessages);
//                initMessageAdapter(filteredMessageList);
                Log.e(TAG, "onSuccess:fetchMessage "+baseMessages );
                initMessageAdapter(baseMessages);
                if (baseMessages.size() != 0) {
                    stopHideShimmer();
                    BaseMessage baseMessage = baseMessages.get(baseMessages.size() - 1);
                    markMessageAsRead(baseMessage);
                }

                if (baseMessages.size() == 0) {
                    stopHideShimmer();
                    isNoMoreMessages = true;
                }
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }
        });
    }

    private void stopHideShimmer() {
        messageShimmer.stopShimmer();
        messageShimmer.setVisibility(GONE);
    }

    private void getSmartReplyList(BaseMessage baseMessage) {

        HashMap<String, JSONObject> extensionList = Extensions.extensionCheck(baseMessage);
        if (extensionList != null && extensionList.containsKey("smartReply")) {
            rvSmartReply.setVisibility(View.VISIBLE);

            JSONObject replyObject = extensionList.get("smartReply");
            List<String> replyList = new ArrayList<>();
            try {
                replyList.add(replyObject.getString("reply_positive"));
                replyList.add(replyObject.getString("reply_neutral"));
                replyList.add(replyObject.getString("reply_negative"));
            } catch (Exception e) {
                Log.e(TAG, "onSuccess: " + e.getMessage());
            }
            setSmartReplyAdapter(replyList);
        } else {
            rvSmartReply.setVisibility(GONE);
        }
    }

    private void setSmartReplyAdapter(List<String> replyList) {
        rvSmartReply.setSmartReplyList(replyList);
//        scrollToBottom();
    }


    /**
     * This method is used to initialize the message adapter if it is empty else it helps
     * to update the messagelist in adapter.
     *
     * @param messageList is a list of messages which will be added.
     */
    private void initMessageAdapter(List<BaseMessage> messageList) {
        if (messageAdapter == null) {
            messageAdapter = new MessageAdapter(getActivity(), messageList, CometChatMessageList.class.getName());
            rvChatListView.setAdapter(messageAdapter);
            stickyHeaderDecoration = new StickyHeaderDecoration(messageAdapter);
            rvChatListView.addItemDecoration(stickyHeaderDecoration, 0);
            scrollToBottom();
            messageAdapter.notifyDataSetChanged();
        } else {
            messageAdapter.updateList(messageList);

        }
        if (!isBlockedByMe && rvSmartReply.getAdapter().getItemCount()==0) {
            BaseMessage lastMessage = messageAdapter.getLastMessage();
            checkSmartReply(lastMessage);
        }
    }

    /**
     * This method is used to send typing indicator to other users and groups.
     *
     * @param isEnd is boolean which is used to differentiate between startTyping & endTyping Indicators.
     * @see CometChat#startTyping(TypingIndicator)
     * @see CometChat#endTyping(TypingIndicator)
     */
    private void sendTypingIndicator(boolean isEnd) {
        if (isEnd) {
            if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                CometChat.endTyping(new TypingIndicator(Id, CometChatConstants.RECEIVER_TYPE_USER));
            } else {
                CometChat.endTyping(new TypingIndicator(Id, CometChatConstants.RECEIVER_TYPE_GROUP));
            }
        } else {
            if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                CometChat.startTyping(new TypingIndicator(Id, CometChatConstants.RECEIVER_TYPE_USER));
            } else {
                CometChat.startTyping(new TypingIndicator(Id, CometChatConstants.RECEIVER_TYPE_GROUP));
            }
        }
    }

    private void endTypingTimer() {
        if (typingTimer!=null) {
            typingTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    sendTypingIndicator(true);
                }
            }, 2000);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");

        if (resultCode==RESULT_OK) {
            switch (requestCode) {
                case UIKitConstants.RequestCode.AUDIO:
                    if (data != null) {
                        resultIntentCode = UIKitConstants.RequestCode.AUDIO;
                        File file = MediaUtils.getRealPath(getContext(), data.getData(),false);
                        ContentResolver cr = getActivity().getContentResolver();
                        sendMediaMessage(file, CometChatConstants.MESSAGE_TYPE_AUDIO);
                    }
                    break;
                case UIKitConstants.RequestCode.GALLERY:
                    if (data != null) {
                        resultIntentCode = UIKitConstants.RequestCode.GALLERY;
                        File file = MediaUtils.getRealPath(getContext(), data.getData(),false);
                        ContentResolver cr = getActivity().getContentResolver();
                        String mimeType = cr.getType(data.getData());
                        if (mimeType != null && mimeType.contains("image")) {
                            if (file.exists())
                                sendMediaMessage(file, CometChatConstants.MESSAGE_TYPE_IMAGE);
                            else
                                CometChatSnackBar.show(context,rvChatListView, getString(R.string.file_not_exist), CometChatSnackBar.WARNING);
                        } else if (mimeType!=null && mimeType.contains("video")){
                            if (file.exists())
                                sendMediaMessage(file, CometChatConstants.MESSAGE_TYPE_VIDEO);
                            else
                                CometChatSnackBar.show(context,rvChatListView, getString(R.string.file_not_exist), CometChatSnackBar.WARNING);
                        } else {
                            if (file.exists())
                                sendMediaMessage(file,CometChatConstants.MESSAGE_TYPE_FILE);
                            else
                                CometChatSnackBar.show(context,rvChatListView, getString(R.string.file_not_exist), CometChatSnackBar.WARNING);
                        }
                    }

                    break;
                case UIKitConstants.RequestCode.CAMERA:
                    File file;
                    resultIntentCode = UIKitConstants.RequestCode.CAMERA;
                    if (Build.VERSION.SDK_INT >= 29) {
                        file = MediaUtils.getRealPath(getContext(), MediaUtils.uri,false);
                    } else {
                        file = new File(MediaUtils.pictureImagePath);
                    }
                    if (file.exists())
                        sendMediaMessage(file, CometChatConstants.MESSAGE_TYPE_IMAGE);
                    else
                        Snackbar.make(rvChatListView, R.string.file_not_exist, Snackbar.LENGTH_LONG).show();

                    break;
                case UIKitConstants.RequestCode.FILE:
                    if (data != null) {
                        resultIntentCode = UIKitConstants.RequestCode.FILE;
                        sendMediaMessage(MediaUtils.getRealPath(getActivity(), data.getData(),false), CometChatConstants.MESSAGE_TYPE_FILE);
                    }
                    break;
                case UIKitConstants.RequestCode.BLOCK_USER:
                    name = data.getStringExtra("");
                    break;
                case UIKitConstants.RequestCode.LOCATION:
                    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Toast.makeText(getContext(), getString(R.string.gps_enabled), Toast.LENGTH_SHORT).show();
                        getLocation();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.gps_disabled), Toast.LENGTH_SHORT).show();
                    }
            }
        }

    }

    /**
     * This method is used to send media messages to other users and group
     * @param mediaMessage is a MediaMessageObject
     */
    private void sendMediaMessage(MediaMessage mediaMessage) {
        CometChat.sendMediaMessage(mediaMessage, new CometChat.CallbackListener<MediaMessage>() {
            @Override
            public void onSuccess(MediaMessage mediaMessage) {
                if (messageAdapter != null) {
                    messageAdapter.addMessage(mediaMessage);
                    scrollToBottom();
                }
            }

            @Override
            public void onError(CometChatException e) {
                if (getActivity() != null) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This method is used to send media messages to other users and group.
     *
     * @param file     is an object of File which is been sent within the message.
     * @param filetype is a string which indicate a type of file been sent within the message.
     * @see CometChat#sendMediaMessage(MediaMessage, CometChat.CallbackListener)
     * @see MediaMessage
     */
    private void sendMediaMessage(File file, String filetype) {
        MediaMessage mediaMessage;

        if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            mediaMessage = new MediaMessage(Id, file, filetype, CometChatConstants.RECEIVER_TYPE_USER);
        else
            mediaMessage = new MediaMessage(Id, file, filetype, CometChatConstants.RECEIVER_TYPE_GROUP);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("path", file.getAbsolutePath());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mediaMessage.setMetadata(jsonObject);

        mediaMessage.setMuid(""+System.currentTimeMillis());
        mediaMessage.setCategory(CometChatConstants.CATEGORY_MESSAGE);
        mediaMessage.setSender(loggedInUser);

        if (messageAdapter != null) {
            messageAdapter.addMessage(mediaMessage);
            scrollToBottom();
        }
        CometChat.sendMediaMessage(mediaMessage, new CometChat.CallbackListener<MediaMessage>() {
            @Override
            public void onSuccess(MediaMessage mediaMessage) {
                Log.d(TAG, "sendMediaMessage onSuccess: " + mediaMessage.toString());
                if (messageAdapter != null) {
                    messageAdapter.updateChangedMessage(mediaMessage);
                }
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
                if (messageAdapter != null) {
                    mediaMessage.setSentAt(-1);
                    messageAdapter.updateChangedMessage(mediaMessage);
                }
                if (getActivity() != null) {
                    CometChatSnackBar.show(context,rvChatListView,e.getMessage(), CometChatSnackBar.ERROR);
                }
            }
        });
    }

    /**
     * This method is used to get details of reciever.
     *
     * @see CometChat#getUser(String, CometChat.CallbackListener)
     */
    private void getUser() {

        CometChat.getUser(Id, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {

                if (getActivity() != null) {
                    if (user.isBlockedByMe()) {
                        tvStatus.setVisibility(View.GONE);
                        isBlockedByMe = true;
                        rvSmartReply.setVisibility(GONE);
                        toolbar.setSelected(false);
                        blockedUserName.setText(getString(R.string.you_ve_blocked) + user.getName());
                        unblockBtn.setVisibility(View.VISIBLE);
                        blockUserLayout.setVisibility(View.VISIBLE);
                        rvSmartReply.setVisibility(GONE);
                        composeBox.setVisibility(GONE);
                    } else if (user.isHasBlockedMe()) {
                        tvStatus.setVisibility(View.GONE);
                        isBlockedByMe = true;
                        rvSmartReply.setVisibility(GONE);
                        toolbar.setSelected(false);
                        blockedUserName.setText(getString(R.string.you_have_blocked_by) + user.getName());
                        blockUserLayout.setVisibility(View.VISIBLE);
                        unblockBtn.setVisibility(GONE);
                        rvSmartReply.setVisibility(GONE);
                        composeBox.setVisibility(GONE);
                    } else {
                        tvStatus.setVisibility(View.VISIBLE);
                        isBlockedByMe = false;
                        composeBox.setVisibility(View.VISIBLE);
                        blockUserLayout.setVisibility(GONE);
                        avatarUrl = user.getAvatar();
                        profileLink = user.getLink();
                        if (user.getStatus().equals(CometChatConstants.USER_STATUS_ONLINE)) {
                            tvStatus.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                        }

                        FeatureRestriction.isUserPresenceEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                if (booleanVal) {
                                    status = user.getStatus().toString();
                                    tvStatus.setText(status);
                                }
                            }
                        });
                        setAvatar();
                    }
                    name = user.getName();
                    tvName.setText(name);
                    Log.d(TAG, "onSuccess: " + user.toString());
                }

            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAvatar() {
        if (avatarUrl != null && !avatarUrl.isEmpty())
            userAvatar.setAvatar(avatarUrl);
        else {
            userAvatar.setInitials(name);
        }
    }

    /**
     * This method is used to get Group Details.
     *
     * @see CometChat#getGroup(String, CometChat.CallbackListener)
     */
    private void getGroup() {

        CometChat.getGroup(Id, new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group) {
                if (getActivity() != null) {
                    name = group.getName();
                    avatarUrl = group.getIcon();
                    loggedInUserScope = group.getScope();
                    groupOwnerId = group.getOwner();

                    tvName.setText(name);
                    if (context != null) {
                        userAvatar.setAvatar(getActivity().getResources().getDrawable(R.drawable.ic_account), avatarUrl);
                    }
                    setAvatar();
                }

            }

            @Override
            public void onError(CometChatException e) {

            }
        });
    }

    /**
     * This method is used to send Text Message to other users and groups.
     *
     * @param message is a String which is been sent as message.
     * @see TextMessage
     * @see CometChat#sendMessage(TextMessage, CometChat.CallbackListener)
     */
    private void sendMessage(String message) {
        TextMessage textMessage;
        if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            textMessage = new TextMessage(Id, message, CometChatConstants.RECEIVER_TYPE_USER);
        else
            textMessage = new TextMessage(Id, message, CometChatConstants.RECEIVER_TYPE_GROUP);


        sendTypingIndicator(true);
        Log.e(TAG, "sendMessage: "+textMessage );
        textMessage.setCategory(CometChatConstants.CATEGORY_MESSAGE);
        textMessage.setSender(loggedInUser);
        textMessage.setMuid(System.currentTimeMillis()+"");
        if (messageAdapter != null) {
            MediaUtils.playSendSound(context, R.raw.outgoing_message);
            messageAdapter.addMessage(textMessage);
            scrollToBottom();
        }
        isSmartReplyClicked=false;
        rvSmartReply.setVisibility(GONE);
        CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
            @Override
            public void onSuccess(TextMessage textMessage) {
                if (messageAdapter!=null)
                    messageAdapter.updateChangedMessage(textMessage);
            }

            @Override
            public void onError(CometChatException e) {
                if (e.getCode().equalsIgnoreCase("ERROR_INTERNET_UNAVAILABLE")) {
                    CometChatSnackBar.show(context, rvChatListView,
                            getString(R.string.no_internet_available), CometChatSnackBar.ERROR);
                } else if (!e.getCode().equalsIgnoreCase("ERR_BLOCKED_BY_EXTENSION")) {
                    if (messageAdapter == null) {
                        Log.e(TAG, "onError: MessageAdapter is null");
                    } else {
                        textMessage.setSentAt(-1);
                        messageAdapter.updateChangedMessage(textMessage);
                    }
                } else if (messageAdapter != null) {
                    messageAdapter.remove(textMessage);
                }
            }
        });

    }

    /**
     * This method is used to delete the message.
     *
     * @param baseMessage is an object of BaseMessage which is being used to delete the message.
     * @see BaseMessage
     * @see CometChat#deleteMessage(int, CometChat.CallbackListener)
     */
    private void deleteMessage(BaseMessage baseMessage) {
        CometChat.deleteMessage(baseMessage.getId(), new CometChat.CallbackListener<BaseMessage>() {
            @Override
            public void onSuccess(BaseMessage baseMessage) {
                if (messageAdapter != null) {
                    if (hideDeleteMessage)
                        messageAdapter.remove(baseMessage);
                    else
                        messageAdapter.setUpdatedMessage(baseMessage);
                }
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }
        });
    }

    /**
     * This method is used to edit the message. This methods takes old message and change text of old
     * message with new message i.e String and update it.
     *
     * @param baseMessage is an object of BaseMessage, It is a old message which is going to be edited.
     * @param message     is String, It is a new message which will be replaced with text of old message.
     * @see TextMessage
     * @see BaseMessage
     * @see CometChat#editMessage(BaseMessage, CometChat.CallbackListener)
     */
    private void editMessage(BaseMessage baseMessage, String message) {

        isEdit = false;

        TextMessage textMessage;
        if (baseMessage.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            textMessage = new TextMessage(baseMessage.getReceiverUid(), message, CometChatConstants.RECEIVER_TYPE_USER);
        else
            textMessage = new TextMessage(baseMessage.getReceiverUid(), message, CometChatConstants.RECEIVER_TYPE_GROUP);
        sendTypingIndicator(true);
        textMessage.setId(baseMessage.getId());
        CometChat.editMessage(textMessage, new CometChat.CallbackListener<BaseMessage>() {
            @Override
            public void onSuccess(BaseMessage message) {
                if (messageAdapter != null) {
                    Log.e(TAG, "onSuccess: " + message.toString());
                    messageAdapter.setUpdatedMessage(message);
                }
            }

            @Override
            public void onError(CometChatException e) {
                Log.d(TAG, "onError: " + e.getMessage());
            }
        });

    }

    /**
     * This method is used to send reply message by link previous message with new message.
     * @param baseMessage is a linked message
     * @param message is a String. It will be new message sent as reply.
     */
    private void replyMessage(BaseMessage baseMessage, String message) {
        isReply = false;
        try {
            TextMessage textMessage;
            if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
                textMessage = new TextMessage(Id, message, CometChatConstants.RECEIVER_TYPE_USER);
            else
                textMessage = new TextMessage(Id, message, CometChatConstants.RECEIVER_TYPE_GROUP);
            JSONObject replyObject = new JSONObject();
            replyObject.put("reply-message", baseMessage.getRawMessage());
            textMessage.setMetadata(replyObject);
            sendTypingIndicator(true);
            textMessage.setCategory(CometChatConstants.CATEGORY_MESSAGE);
            textMessage.setSender(loggedInUser);
            textMessage.setMuid(System.currentTimeMillis()+"");
            if (messageAdapter != null) {
                MediaUtils.playSendSound(context, R.raw.outgoing_message);
                messageAdapter.addMessage(textMessage);
                scrollToBottom();
            }
            isSmartReplyClicked=false;
            rvSmartReply.setVisibility(GONE);
            CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
                @Override
                public void onSuccess(TextMessage textMessage) {
                    if (messageAdapter != null) {
                        MediaUtils.playSendSound(context, R.raw.outgoing_message);
                        messageAdapter.updateChangedMessage(textMessage);
                        scrollToBottom();
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    if (e.getCode().equalsIgnoreCase("ERROR_INTERNET_UNAVAILABLE")) {
                        CometChatSnackBar.show(context, rvChatListView,
                                getString(R.string.no_internet_available), CometChatSnackBar.ERROR);
                    } else if (!e.getCode().equalsIgnoreCase("ERR_BLOCKED_BY_EXTENSION")) {
                        if (messageAdapter == null) {
                            Log.e(TAG, "onError: MessageAdapter is null");
                        } else {
                            textMessage.setSentAt(-1);
                            messageAdapter.updateChangedMessage(textMessage);
                        }
                    } else if (messageAdapter != null) {
                        messageAdapter.remove(textMessage);
                    }
                }
            });
        }catch (Exception e) {
            Log.e(TAG, "replyMessage: "+e.getMessage());
        }
    }

    private void scrollToBottom() {
        if (messageAdapter != null && messageAdapter.getItemCount() > 0) {
            rvChatListView.scrollToPosition(messageAdapter.getItemCount() - 1);

        }
    }

    /**
     * This method is used to recieve real time group events like onMemberAddedToGroup, onGroupMemberJoined,
     * onGroupMemberKicked, onGroupMemberLeft, onGroupMemberBanned, onGroupMemberUnbanned,
     * onGroupMemberScopeChanged.
     *
     * @see CometChat#addGroupListener(String, CometChat.GroupListener)
     */
    private void addGroupListener() {
        CometChat.addGroupListener(TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberJoined(Action action, User joinedUser, Group joinedGroup) {
                super.onGroupMemberJoined(action, joinedUser, joinedGroup);
                if (joinedGroup.getGuid().equals(Id))
                    tvStatus.setText(memberNames + "," + joinedUser.getName());
                onMessageReceived(action);
            }

            @Override
            public void onGroupMemberLeft(Action action, User leftUser, Group leftGroup) {
                super.onGroupMemberLeft(action, leftUser, leftGroup);
                Log.d(TAG, "onGroupMemberLeft: " + leftUser.getName());
                if (leftGroup.getGuid().equals(Id)) {
                    if (memberNames != null)
                        tvStatus.setText(memberNames.replace("," + leftUser.getName(), ""));
                }
                onMessageReceived(action);
            }

            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group kickedFrom) {
                super.onGroupMemberKicked(action, kickedUser, kickedBy, kickedFrom);
                Log.d(TAG, "onGroupMemberKicked: " + kickedUser.getName());
                if (kickedUser.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    if (getActivity() != null)
                        getActivity().finish();

                }
                if (kickedFrom.getGuid().equals(Id))
                    tvStatus.setText(memberNames.replace("," + kickedUser.getName(), ""));
                onMessageReceived(action);
            }

            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group bannedFrom) {
                if (bannedUser.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                        Toast.makeText(getActivity(), "You have been banned", Toast.LENGTH_SHORT).show();
                    }
                }
                onMessageReceived(action);

            }

            @Override
            public void onGroupMemberUnbanned(Action action, User unbannedUser, User unbannedBy, Group unbannedFrom) {
                onMessageReceived(action);
            }

            @Override
            public void onGroupMemberScopeChanged(Action action, User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {
                onMessageReceived(action);
            }

            @Override
            public void onMemberAddedToGroup(Action action, User addedby, User userAdded, Group addedTo) {
                if (addedTo.getGuid().equals(Id))
                    tvStatus.setText(memberNames + "," + userAdded.getName());
                onMessageReceived(action);
            }
        });
    }

    /**
     * This method is used to get real time user status i.e user is online or offline.
     *
     * @see CometChat#addUserListener(String, CometChat.UserListener)
     */
    private void addUserListener() {
        if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            CometChat.addUserListener(TAG, new CometChat.UserListener() {
                @Override
                public void onUserOnline(User user) {
                    Log.d(TAG, "onUserOnline: " + user.toString());
                    if (user.getUid().equals(Id)) {
                        status = CometChatConstants.USER_STATUS_ONLINE;
                        tvStatus.setText(getString(R.string.online));
                        tvStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                    }
                }

                @Override
                public void onUserOffline(User user) {
                    Log.d(TAG, "onUserOffline: " + user.toString());
                    if (user.getUid().equals(Id)) {
                        if (Utils.isDarkMode(getContext()))
                            tvStatus.setTextColor(getResources().getColor(R.color.textColorWhite));
                        else
                            tvStatus.setTextColor(getResources().getColor(android.R.color.black));
                        tvStatus.setText(getString(R.string.offline));
                        status = CometChatConstants.USER_STATUS_OFFLINE;
                    }
                }
            });
        }
    }


    /**
     * This method is used to mark users & group message as read.
     *
     * @param baseMessage is object of BaseMessage.class. It is message which is been marked as read.
     */
    private void markMessageAsRead(BaseMessage baseMessage) {
        FeatureRestriction.isDeliveryReceiptsEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
//        CometChat.markAsRead(baseMessage);  //Used for v3
                if (type.equals(CometChatConstants.RECEIVER_TYPE_USER))
                    CometChat.markAsRead(baseMessage.getId(), baseMessage.getSender().getUid(), baseMessage.getReceiverType());
                else
                    CometChat.markAsRead(baseMessage.getId(), baseMessage.getReceiverUid(), baseMessage.getReceiverType());
            }
        });

    }


    /**
     * This method is used to add message listener to recieve real time messages between users &
     * groups. It also give real time events for typing indicators, edit message, delete message,
     * message being read & delivered.
     *
     * @see CometChat#addMessageListener(String, CometChat.MessageListener)
     */
    private void addMessageListener() {

        CometChat.addMessageListener(TAG, new CometChat.MessageListener() {
            @Override
            public void onTextMessageReceived(TextMessage message) {
                Log.d(TAG, "onTextMessageReceived: " + message.toString());
                onMessageReceived(message);
            }

            @Override
            public void onMediaMessageReceived(MediaMessage message) {
                Log.d(TAG, "onMediaMessageReceived: " + message.toString());
                onMessageReceived(message);
            }

            @Override
            public void onCustomMessageReceived(CustomMessage message) {
                Log.d(TAG, "onCustomMessageReceived: "+ message.toString());
                onMessageReceived(message);
            }

            @Override
            public void onTypingStarted(TypingIndicator typingIndicator) {
                Log.e(TAG, "onTypingStarted: " + typingIndicator);
                setTypingIndicator(typingIndicator,true);
            }

            @Override
            public void onTypingEnded(TypingIndicator typingIndicator) {
                Log.d(TAG, "onTypingEnded: " + typingIndicator.toString());
                setTypingIndicator(typingIndicator,false);
            }

            @Override
            public void onMessagesDelivered(MessageReceipt messageReceipt) {
                Log.d(TAG, "onMessagesDelivered: " + messageReceipt.toString());
                setMessageReciept(messageReceipt);
            }

            @Override
            public void onMessagesRead(MessageReceipt messageReceipt) {
                Log.e(TAG, "onMessagesRead: " + messageReceipt.toString());
                setMessageReciept(messageReceipt);
            }

            @Override
            public void onMessageEdited(BaseMessage message) {
                Log.d(TAG, "onMessageEdited: " + message.toString());
                updateMessage(message);
            }

            @Override
            public void onMessageDeleted(BaseMessage message) {
                Log.d(TAG, "onMessageDeleted: ");
                if (messageAdapter!=null) {
                    if (hideDeleteMessage)
                        messageAdapter.remove(message);
                    else
                        updateMessage(message);
                }
            }
        });
    }

    private void setMessageReciept(MessageReceipt messageReceipt) {
        if (messageAdapter != null) {
            if (messageReceipt!=null && messageReceipt.getReceiptType()!=null &&
                    messageReceipt.getReceivertype().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                if (Id!=null &&messageReceipt.getSender()!=null && messageReceipt.getSender().getUid().equals(Id)) {
                    if (messageReceipt.getReceiptType().equals(MessageReceipt.RECEIPT_TYPE_DELIVERED))
                        messageAdapter.setDeliveryReceipts(messageReceipt);
                    else
                        messageAdapter.setReadReceipts(messageReceipt);
                }
            }
        }
    }

    private void setTypingIndicator(TypingIndicator typingIndicator,boolean isShow) {
        if (typingIndicator.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER)) {
            Log.e(TAG, "onTypingStarted: " + typingIndicator);
            if (Id != null && Id.equalsIgnoreCase(typingIndicator.getSender().getUid())) {
                typingIndicator(typingIndicator, isShow);
            }
        } else {
            if (Id != null && Id.equalsIgnoreCase(typingIndicator.getReceiverId()))
                typingIndicator(typingIndicator, isShow);
        }
    }

    private void onMessageReceived(BaseMessage message) {

        MediaUtils.playSendSound(context, R.raw.incoming_message);
        if (message.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            if (Id != null && Id.equalsIgnoreCase(message.getSender().getUid())) {
                setMessage(message);
            } else if(Id != null && Id.equalsIgnoreCase(message.getReceiverUid()) && message.getSender().getUid().equalsIgnoreCase(loggedInUser.getUid())) {
                setMessage(message);
            }
        } else {
            if (Id != null && Id.equalsIgnoreCase(message.getReceiverUid())) {
                setMessage(message);
            }
        }
    }

    /**
     * This method is used to update edited message by calling <code>setEditMessage()</code> of adapter
     *
     * @param message is an object of BaseMessage and it will replace with old message.
     * @see BaseMessage
     */
    private void updateMessage(BaseMessage message) {
        if (messageAdapter!=null)
            messageAdapter.setUpdatedMessage(message);
    }


    /**
     * This method is used to mark message as read before adding them to list. This method helps to
     * add real time message in list.
     *
     * @param message is an object of BaseMessage, It is recieved from message listener.
     * @see BaseMessage
     */
    private void setMessage(BaseMessage message) {
        if (message.getParentMessageId() == 0) {
            if (messageAdapter != null) {
                messageAdapter.addMessage(message);
                checkSmartReply(message);
                markMessageAsRead(message);
                if ((messageAdapter.getItemCount() - 1) - ((LinearLayoutManager) rvChatListView.getLayoutManager()).findLastVisibleItemPosition() < 5)
                    scrollToBottom();
                else {
                    showNewMessage(++newMessageCount);
                }
            } else {
                messageList.add(message);
                initMessageAdapter(messageList);
            }
        }
    }

    private void showNewMessage(int messageCount) {
        rvSmartReply.setVisibility(GONE);
        newMessageLayout.setVisibility(View.VISIBLE);
        if (messageCount==1)
            newMessageLayoutText.setText(messageCount+getString(R.string.new_message));
        else
            newMessageLayoutText.setText(messageCount+getString(R.string.new_messages));
        newMessageLayout.setOnClickListener(v-> {
            newMessageCount = 0;
            scrollToBottom();
            newMessageLayout.setVisibility(GONE);
        });
    }
    private void checkSmartReply(BaseMessage lastMessage) {
        if (lastMessage != null && !lastMessage.getSender().getUid().equals(loggedInUser.getUid())) {
            if (lastMessage.getMetadata() != null) {
                getSmartReplyList(lastMessage);
            }
        }
    }

    /**
     * This method is used to display typing status to user.
     *
     * @param show is boolean, If it is true then <b>is Typing</b> will be shown to user
     *             If it is false then it will show user status i.e online or offline.
     */
    private void typingIndicator(TypingIndicator typingIndicator, boolean show) {
        if (messageAdapter != null) {
            if (show) {
                if (typingIndicator.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                    if (typingIndicator.getMetadata() == null) {
                        FeatureRestriction.isTypingIndicatorsEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                if (booleanVal)
                                    tvStatus.setText(getString(R.string.is_typing));
                            }
                        });
                    }
                    else
                        setLiveReaction();
                } else {
                    if (typingIndicator.getMetadata() == null) {
                        FeatureRestriction.isTypingIndicatorsEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                if (booleanVal)
                                    tvStatus.setText(typingIndicator.getSender().getName() + getString(R.string.is_typing));
                            }
                        });
                    }
                    else
                        setLiveReaction();
                }
            } else {
                if (typingIndicator.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                    if (typingIndicator.getMetadata() == null)
                        tvStatus.setText(status);
                    else {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(container,"alpha",0.2f);
                        animator.setDuration(700);
                        animator.start();
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                if (imageView!=null)
                                    imageView.clearAnimation();
                                container.removeAllViews();
                            }
                        });
                    }
                } else{
                    if (typingIndicator.getMetadata() == null)
                        tvStatus.setText(memberNames);
                    else {
                        ObjectAnimator animator = ObjectAnimator.ofFloat(container,"alpha",0.2f);
                        animator.setDuration(700);
                        animator.start();
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                if (imageView!=null)
                                    imageView.clearAnimation();
                                container.removeAllViews();
                            }
                        });
                    }
                }
            }

        }
    }

    /**
     * This method is used to remove message listener
     *
     * @see CometChat#removeMessageListener(String)
     */
    private void removeMessageListener() {
        CometChat.removeMessageListener(TAG);
    }

    /**
     * This method is used to remove user presence listener
     *
     * @see CometChat#removeUserListener(String)
     */
    private void removeUserListener() {
        CometChat.removeUserListener(TAG);
    }



    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        if (messageAdapter!=null)
            messageAdapter.stopPlayingAudio();
        removeMessageListener();
        removeUserListener();
        removeGroupListener();
        sendTypingIndicator(true);
    }

    private void removeGroupListener() {
        CometChat.removeGroupListener(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        checkOnGoingCall();
        if (!(resultIntentCode== UIKitConstants.RequestCode.GALLERY ||
                resultIntentCode== UIKitConstants.RequestCode.CAMERA ||
                resultIntentCode == UIKitConstants.RequestCode.FILE ||
                resultIntentCode == UIKitConstants.RequestCode.AUDIO)) {
            rvChatListView.removeItemDecoration(stickyHeaderDecoration);
            messagesRequest = null;
            messageAdapter = null;
            fetchMessage();
        }
        addMessageListener();

        audioCallAction.setClickable(true);
        videoCallAction.setClickable(true);

        if (messageActionFragment!=null && messageActionFragment.getFragmentManager()!=null)
            messageActionFragment.dismiss();

        if (type != null) {
            if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                FeatureRestriction.isUserPresenceEnabled(new FeatureRestriction.OnSuccessListener() {
                    @Override
                    public void onSuccess(Boolean booleanVal) {
                        if (booleanVal) {
                            addUserListener();
                            tvStatus.setText(status);
                        }
                    }
                });
                new Thread(this::getUser).start();
            } else {
                if (!FeatureRestriction.isGroupActionMessagesEnabled())
                    addGroupListener();
                new Thread(this::getGroup).start();
                new Thread(this::getMember).start();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.iv_message_close) {
            if (messageAdapter != null) {
                messageAdapter.clearLongClickSelectedItem();
                messageAdapter.notifyDataSetChanged();
            }
            isEdit = false;
            baseMessage = null;
            editMessageLayout.setVisibility(GONE);
        }
        else if (id == R.id.iv_reply_close) {
            if (messageAdapter!=null) {
                messageAdapter.clearLongClickSelectedItem();
                messageAdapter.notifyDataSetChanged();
            }
            isReply = false;
            baseMessage = null;
            replyMessageLayout.setVisibility(GONE);
        }
        else if (id == R.id.btn_unblock_user) {
            unblockUser();
        }
        else if (id == R.id.info_action) {
            if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                Intent intent = new Intent(getContext(), CometChatUserDetailScreenActivity.class);
                intent.putExtra(UIKitConstants.IntentStrings.UID, Id);
                intent.putExtra(UIKitConstants.IntentStrings.NAME, name);
                intent.putExtra(UIKitConstants.IntentStrings.AVATAR, avatarUrl);
                intent.putExtra(UIKitConstants.IntentStrings.LINK,profileLink);
                intent.putExtra(UIKitConstants.IntentStrings.IS_BLOCKED_BY_ME, isBlockedByMe);
                intent.putExtra(UIKitConstants.IntentStrings.STATUS, status);
                intent.putExtra(UIKitConstants.IntentStrings.TYPE, type);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getContext(), CometChatGroupDetailActivity.class);
                intent.putExtra(UIKitConstants.IntentStrings.GUID, Id);
                intent.putExtra(UIKitConstants.IntentStrings.NAME, name);
                intent.putExtra(UIKitConstants.IntentStrings.AVATAR, avatarUrl);
                intent.putExtra(UIKitConstants.IntentStrings.TYPE, type);
                intent.putExtra(UIKitConstants.IntentStrings.GROUP_TYPE,groupType);
                intent.putExtra(UIKitConstants.IntentStrings.MEMBER_SCOPE, loggedInUserScope);
                intent.putExtra(UIKitConstants.IntentStrings.GROUP_OWNER, groupOwnerId);
                intent.putExtra(UIKitConstants.IntentStrings.MEMBER_COUNT,memberCount);
                intent.putExtra(UIKitConstants.IntentStrings.GROUP_DESC,groupDesc);
                intent.putExtra(UIKitConstants.IntentStrings.GROUP_PASSWORD,groupPassword);
                startActivity(intent);
            }
        }
        else if (id==R.id.audio_call_action) {
            audioCallAction.setClickable(false);
            checkOnGoingCall(CometChatConstants.CALL_TYPE_AUDIO);
        }
        else if (id==R.id.video_call_action) {
            if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                JSONObject customData = new JSONObject();
                try {
                    customData.put("callType", CometChatConstants.CALL_TYPE_VIDEO);
                    customData.put("sessionID",Id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sendCustomMessage(UIKitConstants.IntentStrings.GROUP_CALL,customData);
            } else {
                checkOnGoingCall(CometChatConstants.CALL_TYPE_VIDEO);
            }
        }
        else if (id==R.id.back_action) {
            if (getActivity()!=null)
                getActivity().onBackPressed();
        }
    }

    private void startForwardMessageActivity() {
        Intent intent = new Intent(getContext(), CometChatForwardMessageActivity.class);
        if (baseMessage.getCategory().equals(CometChatConstants.CATEGORY_MESSAGE)) {
            intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_CATEGORY, CometChatConstants.CATEGORY_MESSAGE);
            if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                intent.putExtra(CometChatConstants.MESSAGE_TYPE_TEXT, ((TextMessage) baseMessage).getText());
                intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.MESSAGE_TYPE_TEXT);
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE) ||
                    baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_AUDIO) ||
                    baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_VIDEO) ||
                    baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_FILE)) {
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME, ((MediaMessage) baseMessage).getAttachment().getFileName());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL, ((MediaMessage) baseMessage).getAttachment().getFileUrl());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_MIME_TYPE, ((MediaMessage) baseMessage).getAttachment().getFileMimeType());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION, ((MediaMessage) baseMessage).getAttachment().getFileExtension());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE, ((MediaMessage) baseMessage).getAttachment().getFileSize());
                intent.putExtra(UIKitConstants.IntentStrings.TYPE, baseMessage.getType());
            }
        } else if (baseMessage.getCategory().equals(CometChatConstants.CATEGORY_CUSTOM)) {
            intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_CATEGORY, CometChatConstants.CATEGORY_CUSTOM);
            intent.putExtra(UIKitConstants.IntentStrings.TYPE, UIKitConstants.IntentStrings.LOCATION);
            try {
                intent.putExtra(UIKitConstants.IntentStrings.LOCATION_LATITUDE,
                        ((CustomMessage)baseMessage).getCustomData().getDouble("latitude"));
                intent.putExtra(UIKitConstants.IntentStrings.LOCATION_LONGITUDE,
                        ((CustomMessage)baseMessage).getCustomData().getDouble("longitude"));
            } catch (Exception e) {
                Log.e(TAG, "startForwardMessageActivityError: "+e.getMessage());
            }
        }

        startActivity(intent);
    }

    private void shareMessage() {
        if (baseMessage!=null && baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TITLE,getResources().getString(R.string.app_name));
                shareIntent.putExtra(Intent.EXTRA_TEXT, ((TextMessage)baseMessage).getText());
                shareIntent.setType("text/plain");
                Intent intent = Intent.createChooser(shareIntent, getResources().getString(R.string.share_message));
                startActivity(intent);
        } else if (baseMessage!=null && baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                String mediaName = ((MediaMessage)baseMessage).getAttachment().getFileName();
                Glide.with(context).asBitmap().load(((MediaMessage)baseMessage).getAttachment().getFileUrl()).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), resource, mediaName, null);
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
                        shareIntent.setType(((MediaMessage)baseMessage).getAttachment().getFileMimeType());
                        Intent intent = Intent.createChooser(shareIntent, getResources().getString(R.string.share_message));
                        startActivity(intent);
                    }
                });
        } else if (baseMessage!=null && 
                baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_VIDEO) ||
                baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_FILE) || 
                baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_AUDIO)) {
            MediaUtils.downloadAndShareFile(context,((MediaMessage)baseMessage));
        }
    }



    private void startThreadActivity() {
        Intent intent = new Intent(getContext(), CometChatThreadMessageListActivity.class);
        intent.putExtra(UIKitConstants.IntentStrings.CONVERSATION_NAME,name);
        intent.putExtra(UIKitConstants.IntentStrings.NAME,baseMessage.getSender().getName());
        intent.putExtra(UIKitConstants.IntentStrings.UID,baseMessage.getSender().getName());
        intent.putExtra(UIKitConstants.IntentStrings.AVATAR,baseMessage.getSender().getAvatar());
        intent.putExtra(UIKitConstants.IntentStrings.PARENT_ID,baseMessage.getId());
        intent.putExtra(UIKitConstants.IntentStrings.REPLY_COUNT,baseMessage.getReplyCount());
        intent.putExtra(UIKitConstants.IntentStrings.SENTAT,baseMessage.getSentAt());
        intent.putExtra(UIKitConstants.IntentStrings.REACTION_INFO, Extensions.getReactionsOnMessage(baseMessage));
        if (baseMessage.getCategory().equalsIgnoreCase(CometChatConstants.CATEGORY_MESSAGE)) {
            intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,baseMessage.getType());
            if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT))
                intent.putExtra(UIKitConstants.IntentStrings.TEXTMESSAGE, ((TextMessage) baseMessage).getText());
            else {
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME, ((MediaMessage) baseMessage).getAttachment().getFileName());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION, ((MediaMessage) baseMessage).getAttachment().getFileExtension());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL, ((MediaMessage) baseMessage).getAttachment().getFileUrl());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE, ((MediaMessage) baseMessage).getAttachment().getFileSize());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_MIME_TYPE, ((MediaMessage) baseMessage).getAttachment().getFileMimeType());
            }
        } else {
            try {
                if (baseMessage.getType().equals(UIKitConstants.IntentStrings.LOCATION)) {
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE, UIKitConstants.IntentStrings.LOCATION);
                    intent.putExtra(UIKitConstants.IntentStrings.LOCATION_LATITUDE,
                            ((CustomMessage) baseMessage).getCustomData().getDouble("latitude"));
                    intent.putExtra(UIKitConstants.IntentStrings.LOCATION_LONGITUDE,
                            ((CustomMessage) baseMessage).getCustomData().getDouble("longitude"));
                } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.STICKERS)) {
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME,((CustomMessage)baseMessage).getCustomData().getString("name"));
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL,((CustomMessage)baseMessage).getCustomData().getString("url"));
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE, UIKitConstants.IntentStrings.STICKERS);
                } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WHITEBOARD)) {
                    intent.putExtra(UIKitConstants.IntentStrings.TEXTMESSAGE, Extensions.getWhiteBoardUrl(baseMessage));
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE, UIKitConstants.IntentStrings.WHITEBOARD);
                } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WRITEBOARD)) {
                    intent.putExtra(UIKitConstants.IntentStrings.TEXTMESSAGE, Extensions.getWriteBoardUrl(baseMessage));
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE, UIKitConstants.IntentStrings.WRITEBOARD);
                } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.POLLS)) {
                    JSONObject options = ((CustomMessage)baseMessage).getCustomData().getJSONObject("options");
                    intent.putExtra(UIKitConstants.IntentStrings.POLL_QUESTION,((CustomMessage)baseMessage).getCustomData().getString("question"));
                    intent.putExtra(UIKitConstants.IntentStrings.POLL_OPTION,options.toString());
                    intent.putExtra(UIKitConstants.IntentStrings.POLL_VOTE_COUNT, Extensions.getVoteCount(baseMessage));
                    intent.putExtra(UIKitConstants.IntentStrings.POLL_RESULT, Extensions.getVoterInfo(baseMessage,options.length()));
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE, UIKitConstants.IntentStrings.POLLS);
                }
            }catch (Exception e) {
                Log.e(TAG, "startThreadActivityError: "+e.getMessage());
            }
        }
        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_CATEGORY,baseMessage.getCategory());
        intent.putExtra(UIKitConstants.IntentStrings.TYPE,type);
        if (type.equals(CometChatConstants.CONVERSATION_TYPE_GROUP)) {
            intent.putExtra(UIKitConstants.IntentStrings.GUID,Id);
        }
        else {
            intent.putExtra(UIKitConstants.IntentStrings.UID,Id);
        }
        startActivity(intent);
    }

    @Override
    public void setLongMessageClick(List<BaseMessage> baseMessagesList) {
        Log.e(TAG, "setLongMessageClick: " + baseMessagesList);
        isReply = false;
        isEdit = false;
        messageActionFragment = new CometChatMessageActions();
        replyMessageLayout.setVisibility(GONE);
        editMessageLayout.setVisibility(GONE);
        fetchSettings();
        List<BaseMessage> textMessageList = new ArrayList<>();
        List<BaseMessage> mediaMessageList = new ArrayList<>();
        List<BaseMessage> locationMessageList = new ArrayList<>();
        List<BaseMessage> pollsMessageList = new ArrayList<>();
        List<BaseMessage> stickerMessageList = new ArrayList<>();
        List<BaseMessage> whiteBoardMessageList = new ArrayList<>();
        List<BaseMessage> writeBoardMessageList = new ArrayList<>();
        List<BaseMessage> meetingMessageList = new ArrayList<>();

        for (BaseMessage baseMessage : baseMessagesList) {
            if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                textMessageList.add(baseMessage);
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE) ||
                    baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_VIDEO) ||
                    baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_FILE) ||
                    baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_AUDIO)){
                mediaMessageList.add(baseMessage);
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.LOCATION)) {
                locationMessageList.add(baseMessage);
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.STICKERS)) {
                stickerMessageList.add(baseMessage);
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.POLLS)) {
                pollsMessageList.add(baseMessage);
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WHITEBOARD)) {
                whiteBoardMessageList.add(baseMessage);
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WRITEBOARD)) {
                writeBoardMessageList.add(baseMessage);
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.GROUP_CALL)) {
                meetingMessageList.add(baseMessage);
            }
        }
        if (textMessageList.size() == 1) {
            BaseMessage basemessage = textMessageList.get(0);
            if (basemessage != null && basemessage.getSender() != null) {
                if (!(basemessage instanceof Action) && basemessage.getDeletedAt() == 0) {
                    baseMessage = basemessage;
                    if (basemessage.getReplyCount()>0)
                        threadVisible = false;

                    if (basemessage.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                        FeatureRestriction.isDeleteMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                deleteVisible = booleanVal;
                            }
                        });
                    } else {
                        editVisible = false;
                        if (loggedInUserScope!=null && (loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN) || loggedInUserScope.equals(CometChatConstants.SCOPE_MODERATOR))) {
                            FeatureRestriction.isDeleteMemberMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                                @Override
                                public void onSuccess(Boolean booleanVal) {
                                    deleteVisible = booleanVal;
                                }
                            });
                        } else {
                            deleteVisible = false;
                        }
                    }
                    if (basemessage.getSentAt()==-1) {
                        translateVisible = false;
                        threadVisible = false;
                        deleteVisible = false;
                        editVisible = false;
                        copyVisible = false;
                        forwardVisible = false;
                        reactionVisible = false;
                        replyVisible = false;
                        shareVisible = false;
                        retryVisible = true;
                    }
                }
            }
        }

        if (mediaMessageList.size() == 1) {
            translateVisible = false;
            BaseMessage basemessage = mediaMessageList.get(0);
            if (basemessage != null && basemessage.getSender() != null) {
                if (!(basemessage instanceof Action) && basemessage.getDeletedAt() == 0) {
                    baseMessage = basemessage;
                    if (basemessage.getReplyCount()>0)
                        threadVisible = false;

                    copyVisible = false;
//                    if (basemessage.getType().equalsIgnoreCase(CometChatConstants.MESSAGE_TYPE_FILE)||
//                            basemessage.getType().equalsIgnoreCase(CometChatConstants.MESSAGE_TYPE_AUDIO) ||
//                            basemessage.getType().equalsIgnoreCase(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
//                        shareVisible = false;
//                    }
                    if (basemessage.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                        FeatureRestriction.isDeleteMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                deleteVisible = booleanVal;
                            }
                        });
                        editVisible = false;
                    } else {
                        if (loggedInUserScope!=null && (loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN) || loggedInUserScope.equals(CometChatConstants.SCOPE_MODERATOR))){
                            FeatureRestriction.isDeleteMemberMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                                @Override
                                public void onSuccess(Boolean booleanVal) {
                                    deleteVisible = booleanVal;
                                }
                            });
                        } else {
                            deleteVisible = false;
                        }
                        editVisible = false;
                    }
                }
                if (basemessage.getSentAt()==-1) {
                    translateVisible = false;
                    threadVisible = false;
                    deleteVisible = false;
                    editVisible = false;
                    copyVisible = false;
                    forwardVisible = false;
                    reactionVisible = false;
                    replyVisible = false;
                    shareVisible = false;
                    retryVisible = true;
                }
            }
        }
        if (locationMessageList.size() == 1){
            translateVisible = false;
            BaseMessage basemessage = locationMessageList.get(0);
            if (basemessage != null && basemessage.getSender() != null) {
                if (!(basemessage instanceof Action) && basemessage.getDeletedAt() == 0) {
                    baseMessage = basemessage;
                    if (basemessage.getReplyCount()>0)
                        threadVisible = false;
                    else {
                        FeatureRestriction.isThreadedMessagesEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                threadVisible = booleanVal;
                            }
                        });
                    }
                    copyVisible = false;
                    FeatureRestriction.isMessageRepliesEnabled(new FeatureRestriction.OnSuccessListener() {
                        @Override
                        public void onSuccess(Boolean booleanVal) {
                            replyVisible = booleanVal;
                        }
                    });
                    shareVisible = false;
                    FeatureRestriction.isShareCopyForwardMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                        @Override
                        public void onSuccess(Boolean booleanVal) {
                            forwardVisible = booleanVal;
                        }
                    });
                    if (basemessage.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                        FeatureRestriction.isDeleteMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                deleteVisible = booleanVal;
                            }
                        });
                        editVisible = false;
                    } else {
                        if (loggedInUserScope!=null && (loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN) || loggedInUserScope.equals(CometChatConstants.SCOPE_MODERATOR))){
                            FeatureRestriction.isDeleteMemberMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                                @Override
                                public void onSuccess(Boolean booleanVal) {
                                    deleteVisible = booleanVal;
                                }
                            });
                        } else {
                            deleteVisible = false;
                        }
                        editVisible = false;
                    }
                }
                if (basemessage.getSentAt()==-1) {
                    translateVisible = false;
                    threadVisible = false;
                    deleteVisible = false;
                    editVisible = false;
                    copyVisible = false;
                    forwardVisible = false;
                    reactionVisible = false;
                    replyVisible = false;
                    shareVisible = false;
                    retryVisible = true;
                }
            }
        }
        if (pollsMessageList.size() == 1){
            forwardVisible = false;
            translateVisible = false;
            copyVisible = false;
            editVisible = false;
            shareVisible = false;
            BaseMessage basemessage = pollsMessageList.get(0);
            if (basemessage != null && basemessage.getSender() != null) {
                if (!(basemessage instanceof Action) && basemessage.getDeletedAt() == 0) {
                    baseMessage = basemessage;
                    if (basemessage.getReplyCount()>0)
                        threadVisible = false;
                    else {
                        FeatureRestriction.isThreadedMessagesEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                threadVisible = booleanVal;
                            }
                        });
                    }
                    if (basemessage.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                        FeatureRestriction.isDeleteMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                deleteVisible = booleanVal;
                            }
                        });
                    } else {
                        if (loggedInUserScope!=null && (loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN) || loggedInUserScope.equals(CometChatConstants.SCOPE_MODERATOR))){
                            FeatureRestriction.isDeleteMemberMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                                @Override
                                public void onSuccess(Boolean booleanVal) {
                                    deleteVisible = booleanVal;
                                }
                            });
                        } else {
                            deleteVisible = false;
                        }
                    }
                }
            }
        }
        if (stickerMessageList.size()==1) {
            forwardVisible = false;
            copyVisible = false;
            editVisible = false;
            translateVisible = false;
            shareVisible = false;
            BaseMessage basemessage = stickerMessageList.get(0);
            if (basemessage!=null && basemessage.getSender()!=null) {
                if (basemessage.getDeletedAt() == 0) {
                    baseMessage = basemessage;
                    if (basemessage.getReplyCount()>0)
                        threadVisible = false;
                    else {
                        FeatureRestriction.isThreadedMessagesEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                threadVisible = booleanVal;
                            }
                        });
                    }
                    if (basemessage.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                        FeatureRestriction.isDeleteMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                deleteVisible = booleanVal;
                            }
                        });
                    }
                    else {
                        if (loggedInUserScope!=null && (loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN) || loggedInUserScope.equals(CometChatConstants.SCOPE_MODERATOR))){
                            FeatureRestriction.isDeleteMemberMessageEnabled(new FeatureRestriction.OnSuccessListener(){
                                @Override
                                public void onSuccess(Boolean booleanVal) {
                                    deleteVisible = booleanVal;
                                }
                            });
                        } else {
                            deleteVisible = false;
                        }
                    }
                }
            }
        }
        if (whiteBoardMessageList.size()==1) {
            forwardVisible = false;
            copyVisible = false;
            translateVisible = false;
            editVisible = false;
            shareVisible = false;
            BaseMessage basemessage = whiteBoardMessageList.get(0);
            if (basemessage!=null && basemessage.getSender()!=null) {
                if (basemessage.getDeletedAt() == 0) {
                    baseMessage = basemessage;
                    if (basemessage.getReplyCount()>0)
                        threadVisible = false;
                    else {
                        FeatureRestriction.isThreadedMessagesEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                threadVisible = booleanVal;
                            }
                        });
                    }
                    if (basemessage.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                        FeatureRestriction.isDeleteMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                deleteVisible = booleanVal;
                            }
                        });
                    }
                    else {
                        if (loggedInUserScope!=null && (loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN) || loggedInUserScope.equals(CometChatConstants.SCOPE_MODERATOR))){
                            FeatureRestriction.isDeleteMemberMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                                @Override
                                public void onSuccess(Boolean booleanVal) {
                                    deleteVisible = booleanVal;
                                }
                            });
                        } else {
                            deleteVisible = false;
                        }
                    }
                }
            }
        }

        if (writeBoardMessageList.size()==1) {
            forwardVisible = false;
            copyVisible = false;
            editVisible = false;
            translateVisible = false;
            shareVisible = false;
            BaseMessage basemessage = writeBoardMessageList.get(0);
            if (basemessage!=null && basemessage.getSender()!=null) {
                if (basemessage.getDeletedAt() == 0) {
                    baseMessage = basemessage;
                    if (basemessage.getReplyCount()>0)
                        threadVisible = false;
                    else {
                        FeatureRestriction.isThreadedMessagesEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                threadVisible = booleanVal;
                            }
                        });
                    }
                    if (basemessage.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                        FeatureRestriction.isDeleteMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                deleteVisible = booleanVal;
                            }
                        });
                    }
                    else {
                        if (loggedInUserScope!=null && (loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN) || loggedInUserScope.equals(CometChatConstants.SCOPE_MODERATOR))){
                            FeatureRestriction.isDeleteMemberMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                                @Override
                                public void onSuccess(Boolean booleanVal) {
                                    deleteVisible = booleanVal;
                                }
                            });
                        } else {
                            deleteVisible = false;
                        }
                    }
                }
            }
        }

        if (meetingMessageList.size()==1) {
            forwardVisible = false;
            copyVisible = false;
            editVisible = false;
            shareVisible = false;
            replyVisible = true;
            translateVisible = false;
            threadVisible = false;
            BaseMessage basemessage = meetingMessageList.get(0);
            if (basemessage!=null && basemessage.getSender()!=null) {
                if (basemessage.getDeletedAt() == 0) {
                    baseMessage = basemessage;
                    if (basemessage.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                        FeatureRestriction.isDeleteMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                deleteVisible = booleanVal;
                            }
                        });
                    }
                    else {
                        if (loggedInUserScope!=null && (loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN) || loggedInUserScope.equals(CometChatConstants.SCOPE_MODERATOR))){
                            FeatureRestriction.isDeleteMemberMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                                @Override
                                public void onSuccess(Boolean booleanVal) {
                                    deleteVisible = booleanVal;
                                }
                            });
                        } else {
                            deleteVisible = false;
                        }
                    }
                }
            }
        }

        baseMessages = baseMessagesList;
        Bundle bundle = new Bundle();
        bundle.putBoolean("copyVisible",copyVisible);
        bundle.putBoolean("threadVisible",threadVisible);
        bundle.putBoolean("shareVisible",shareVisible);
        bundle.putBoolean("editVisible",editVisible);
        bundle.putBoolean("deleteVisible",deleteVisible);
        bundle.putBoolean("replyVisible",replyVisible);
        bundle.putBoolean("forwardVisible",forwardVisible);
        bundle.putBoolean("translateVisible",translateVisible);
        bundle.putBoolean("retryVisible",retryVisible);
        bundle.putBoolean("isReactionVisible", reactionVisible);
        if (baseMessage.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP) &&
                !baseMessage.getSender().getUid().equalsIgnoreCase(loggedInUser.getUid())) {
            bundle.putBoolean("privateReplyVisible", replyPrivately);
            bundle.putBoolean("replyPrivatelyVisible",replyVisible);
        }

        if(CometChat.isExtensionEnabled("reactions")) {
            bundle.putBoolean("isReactionVisible", true);
        } else {
            bundle.putBoolean("isReactionVisible",false);
        }

        if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP) &&
                baseMessage.getSender().getUid().equals(loggedInUser.getUid())) {
            FeatureRestriction.isDeliveryReceiptsEnabled(new FeatureRestriction.OnSuccessListener() {
                @Override
                public void onSuccess(Boolean booleanVal) {
                    if (booleanVal)
                        bundle.putBoolean("messageInfoVisible", true);
                }
            });
        }
        bundle.putString("type", CometChatMessageListActivity.class.getName());
        messageActionFragment.setArguments(bundle);
        if (baseMessage.getSentAt()!=0) {
            if (retryVisible || editVisible || copyVisible || threadVisible || shareVisible || deleteVisible
                    || replyVisible || forwardVisible || reactionVisible) {
                messageActionFragment.show(getFragmentManager(), messageActionFragment.getTag());
            }
        }
        messageActionFragment.setMessageActionListener(new CometChatMessageActions.MessageActionListener() {
            @Override
            public void onReplyMessagePrivately() {
                if (baseMessage!=null) {
                    User user = baseMessage.getSender();
                    Intent intent = new Intent(context, CometChatMessageListActivity.class);
                    intent.putExtra(UIKitConstants.IntentStrings.UID, user.getUid());
                    intent.putExtra(UIKitConstants.IntentStrings.AVATAR, user.getAvatar());
                    intent.putExtra(UIKitConstants.IntentStrings.STATUS, user.getStatus());
                    intent.putExtra(UIKitConstants.IntentStrings.LINK,user.getLink());
                    intent.putExtra(UIKitConstants.IntentStrings.NAME, user.getName());
                    intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER);
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE,baseMessage.getRawMessage().toString());
                    startActivity(intent);
                    if (getActivity()!=null)
                        getActivity().finish();
                }
            }

            @Override
            public void onPrivateReplyToUser() {
                if (baseMessage!=null) {
                    User user = baseMessage.getSender();
                    Intent intent = new Intent(context, CometChatMessageListActivity.class);
                    intent.putExtra(UIKitConstants.IntentStrings.UID, user.getUid());
                    intent.putExtra(UIKitConstants.IntentStrings.AVATAR, user.getAvatar());
                    intent.putExtra(UIKitConstants.IntentStrings.STATUS, user.getStatus());
                    intent.putExtra(UIKitConstants.IntentStrings.LINK,user.getLink());
                    intent.putExtra(UIKitConstants.IntentStrings.NAME, user.getName());
                    intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER);
                    startActivity(intent);
                    if (getActivity()!=null)
                        getActivity().finish();
                }
            }

            @Override
            public void onThreadMessageClick() {
                startThreadActivity();
            }

            @Override
            public void onEditMessageClick() {
                if (baseMessage!=null&&baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                    isEdit = true;
                    isReply = false;
                    tvMessageTitle.setText(getResources().getString(R.string.edit_message));
                    tvMessageSubTitle.setText(((TextMessage) baseMessage).getText());
                    composeBox.ivMic.setVisibility(GONE);
                    composeBox.ivSend.setVisibility(View.VISIBLE);
                    editMessageLayout.setVisibility(View.VISIBLE);
                    composeBox.etComposeBox.setText(((TextMessage) baseMessage).getText());
                    if (messageAdapter != null) {
                        messageAdapter.setSelectedMessage(baseMessage.getId());
                        messageAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onReplyMessageClick() {
                replyMessage();
            }

            @Override
            public void onForwardMessageClick() {
                startForwardMessageActivity();
            }

            @Override
            public void onDeleteMessageClick() {
                deleteMessage(baseMessage);
                if (messageAdapter != null) {
                    messageAdapter.clearLongClickSelectedItem();
                    messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCopyMessageClick() {
                String message = "";
                for (BaseMessage bMessage : baseMessages) {
                    if (bMessage.getDeletedAt() == 0 && bMessage instanceof TextMessage) {
                        message = message + "[" + Utils.getLastMessageDate(context,bMessage.getSentAt()) + "] " + bMessage.getSender().getName() + ": " + ((TextMessage) bMessage).getText();
                    }
                }
                Log.e(TAG, "onCopy: " + message);
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("MessageAdapter", message);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, getResources().getString(R.string.text_copied), Toast.LENGTH_LONG).show();
                if (messageAdapter != null) {
                    messageAdapter.clearLongClickSelectedItem();
                    messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onShareMessageClick() { shareMessage(); }

            @Override
            public void onMessageInfoClick() {
                Intent intent = new Intent(context, CometChatMessageInfoScreenActivity.class);
                intent.putExtra(UIKitConstants.IntentStrings.ID,baseMessage.getId());
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,baseMessage.getType());
                intent.putExtra(UIKitConstants.IntentStrings.SENTAT,baseMessage.getSentAt());
                if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                    intent.putExtra(UIKitConstants.IntentStrings.TEXTMESSAGE,
                            Extensions.checkProfanityMessage(context,baseMessage));
                } else if(baseMessage.getCategory().equals(CometChatConstants.CATEGORY_CUSTOM)){
                    if (((CustomMessage)baseMessage).getCustomData()!=null)
                        intent.putExtra(UIKitConstants.IntentStrings.CUSTOM_MESSAGE,
                                ((CustomMessage) baseMessage).getCustomData().toString());
                    if (baseMessage.getType().equals(UIKitConstants.IntentStrings.LOCATION)) {
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
                                UIKitConstants.IntentStrings.LOCATION);
                    } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.STICKERS)) {
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE, UIKitConstants.IntentStrings.STICKERS);
                    } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WHITEBOARD)) {
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
                                UIKitConstants.IntentStrings.WHITEBOARD);
                        intent.putExtra(UIKitConstants.IntentStrings.TEXTMESSAGE, Extensions.getWhiteBoardUrl(baseMessage));
                    } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WRITEBOARD)) {
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
                                UIKitConstants.IntentStrings.WRITEBOARD);
                        intent.putExtra(UIKitConstants.IntentStrings.TEXTMESSAGE, Extensions.getWriteBoardUrl(baseMessage));
                    }  else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.GROUP_CALL)) {
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
                                UIKitConstants.IntentStrings.GROUP_CALL);
                    }  else {
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
                                UIKitConstants.IntentStrings.POLLS);
                    }
                } else {
                    boolean isImageNotSafe = Extensions.getImageModeration(context,baseMessage);
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL,
                            ((MediaMessage)baseMessage).getAttachment().getFileUrl());
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME,
                            ((MediaMessage)baseMessage).getAttachment().getFileName());
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE,
                            ((MediaMessage)baseMessage).getAttachment().getFileSize());
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION,
                            ((MediaMessage)baseMessage).getAttachment().getFileExtension());
                    intent.putExtra(UIKitConstants.IntentStrings.IMAGE_MODERATION,isImageNotSafe);
                }
                context.startActivity(intent);
            }

            @Override
            public void onReactionClick(Reaction reaction) {
                if (reaction.getName().equals("add_emoji")) {
                    CometChatReactionDialog reactionDialog = new CometChatReactionDialog();
                    reactionDialog.setOnEmojiClick(new OnReactionClickListener() {
                        @Override
                        public void onEmojiClicked(Reaction emojicon) {
                            sendReaction(emojicon);
                            reactionDialog.dismiss();
                        }
                    });
                    reactionDialog.show(getFragmentManager(),"ReactionDialog");
                } else {
                    sendReaction(reaction);
                }
            }

            @Override
            public void onTranslateMessageClick() {
                try {
                    String localeLanguage = Locale.getDefault().getLanguage();
                    JSONObject body = new JSONObject();
                    JSONArray languages = new JSONArray();
                    languages.put(localeLanguage);
                    body.put("msgId", baseMessage.getId());
                    body.put("languages", languages);
                    body.put("text", ((TextMessage)baseMessage).getText());

                    CometChat.callExtension("message-translation", "POST", "/v2/translate", body,
                            new CometChat.CallbackListener<JSONObject>() {
                                @Override
                                public void onSuccess(JSONObject jsonObject) {
                                    if (Extensions.isMessageTranslated(jsonObject,((TextMessage)baseMessage).getText())) {
                                        if (baseMessage.getMetadata()!=null) {
                                            JSONObject meta = baseMessage.getMetadata();
                                            try {
                                                meta.accumulate("values",jsonObject);
                                                baseMessage.setMetadata(meta);
                                                updateMessage(baseMessage);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            baseMessage.setMetadata(jsonObject);
                                            updateMessage(baseMessage);
                                        }
                                    } else {
                                        CometChatSnackBar.show(context,rvChatListView,
                                                context.getString(R.string.no_translation_available), CometChatSnackBar.WARNING);
                                    }
                                }

                                @Override
                                public void onError(CometChatException e) {
                                    Toast.makeText(context,e.getCode(),Toast.LENGTH_LONG).show();
                                }
                            });
                } catch (Exception e) {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onRetryClick() {
                if (baseMessage!=null) {
                    messageAdapter.remove(baseMessage);
                    if (baseMessage.getType().equalsIgnoreCase(CometChatConstants.MESSAGE_TYPE_TEXT))
                        sendMessage(((TextMessage)baseMessage).getText());
                }
            }
        });
    }

    private void sendReaction(Reaction reaction) {
        JSONObject body = new JSONObject();
        try {
            body.put("msgId", baseMessage.getId());
            body.put("emoji", reaction.getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CometChat.callExtension("reactions", "POST", "/v1/react", body,
                new CometChat.CallbackListener<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject responseObject) {
                        Log.e(TAG, "onSuccess: " + responseObject.toString());
                        // ReactionModel added successfully.
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "onError: " + e.getCode() + e.getMessage() + e.getDetails());
                    }
                });
    }


    private void replyMessage() {
        if (baseMessage != null) {
            isReply = true;
            replyTitle.setText(baseMessage.getSender().getName());
            replyMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            replyMedia.setVisibility(View.VISIBLE);
            if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                replyMessage.setText(((TextMessage) baseMessage).getText());
                replyMedia.setVisibility(GONE);
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                replyMessage.setText(getResources().getString(R.string.shared_a_image));
                Glide.with(context).load(((MediaMessage) baseMessage).getAttachment().getFileUrl()).into(replyMedia);
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_AUDIO)) {
                String messageStr = String.format(getResources().getString(R.string.shared_a_audio),
                        Utils.getFileSize(((MediaMessage) baseMessage).getAttachment().getFileSize()));
                replyMessage.setText(messageStr);
                replyMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_library_music_24dp, 0, 0, 0);
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
                replyMessage.setText(getResources().getString(R.string.shared_a_video));
                Glide.with(context).load(((MediaMessage) baseMessage).getAttachment().getFileUrl()).into(replyMedia);
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_FILE)) {
                String messageStr = String.format(getResources().getString(R.string.shared_a_file),
                        Utils.getFileSize(((MediaMessage) baseMessage).getAttachment().getFileSize()));
                replyMessage.setText(messageStr);
                replyMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_insert_drive_file_black_24dp, 0, 0, 0);
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.LOCATION)) {
                try {
                    JSONObject jsonObject = ((CustomMessage) baseMessage).getCustomData();
                    String messageStr = String.format(getString(R.string.shared_location),
                            Utils.getAddress(context, jsonObject.getDouble("latitude"),
                                    jsonObject.getDouble("longitude")));
                    replyMessage.setText(messageStr);
                }catch (Exception e) {
                    Log.e(TAG, "replyMessageError: "+e.getMessage() );
                }
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.STICKERS)) {
                replyMessage.setText(getResources().getString(R.string.shared_a_sticker));
                try {
                    Glide.with(context).load(((CustomMessage) baseMessage).getCustomData().getString("url")).into(replyMedia);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.POLLS)) {
                try {
                    JSONObject jsonObject = ((CustomMessage) baseMessage).getCustomData();
                    String messageStr = String.format(getString(R.string.shared_a_polls),jsonObject.getString("question"));
                    replyMessage.setText(messageStr);
                }catch (Exception e) {
                    Log.e(TAG, "replyMessageError: "+e.getMessage() );
                }
            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WHITEBOARD)) {
                replyMessage.setText(getString(R.string.shared_a_whiteboard));
                replyMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_whiteboard_24dp, 0, 0, 0);

            } else if (baseMessage.getType().equals(UIKitConstants.IntentStrings.WRITEBOARD)) {
                replyMessage.setText(getString(R.string.shared_a_writeboard));
                replyMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_writeboard_24dp, 0, 0, 0);

            }
            composeBox.ivMic.setVisibility(GONE);
            composeBox.ivSend.setVisibility(View.VISIBLE);
            replyMessageLayout.setVisibility(View.VISIBLE);
            if (messageAdapter != null) {
                messageAdapter.setSelectedMessage(baseMessage.getId());
                messageAdapter.notifyDataSetChanged();
            }
        }
    }

    //Live Reactions
    private void sendLiveReaction() {
        try {
            JSONObject metaData = new JSONObject();
            metaData.put("reaction", "heart");
            TypingIndicator typingIndicator = new TypingIndicator(Id, type, metaData);
            CometChat.startTyping(typingIndicator);
            setLiveReaction();
        } catch (Exception e) {
            Log.e(TAG, "sendLiveReaction: "+e.getMessage());
        }
    }

    private void setLiveReaction() {
        container.setAlpha(1.0f);
        flyEmoji(R.drawable.heart_reaction);
    }

    private void flyEmoji(final int resId) {
        imageView = new ImageView(getContext());

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.BOTTOM|Gravity.END;
        layoutParams.rightMargin = 16;
        imageView.setLayoutParams(layoutParams);
        container.addView(imageView);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        if (bitmap!=null) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.2f), (int) (bitmap.getHeight() * 0.2f), false);
            imageView.setImageBitmap(scaledBitmap);
        }

        animation = ObjectAnimator.ofFloat(imageView, "translationY", -700f);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.setDuration(700);
        animation.start();
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        if (messageAdapter!=null)
            messageAdapter.clearLongClickSelectedItem();
        dialog.dismiss();
    }

     private void fetchSettings() {
        if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP)) {

            FeatureRestriction.isGroupVideoCallEnabled(new FeatureRestriction.OnSuccessListener() {
                @Override
                public void onSuccess(Boolean booleanVal) {
                    if (booleanVal)
                        videoCallAction.setVisibility(View.VISIBLE);
                    else
                        videoCallAction.setVisibility(View.GONE);
                }
            });
            audioCallAction.setVisibility(View.GONE);
        }
        else {
            FeatureRestriction.isOneOnOneVideoCallEnabled(new FeatureRestriction.OnSuccessListener() {
                @Override
                public void onSuccess(Boolean booleanVal) {
                    if (booleanVal)
                        videoCallAction.setVisibility(View.VISIBLE);
                    else
                        videoCallAction.setVisibility(View.GONE);
                }
            });

            FeatureRestriction.isOneOnOneAudioCallEnabled(new FeatureRestriction.OnSuccessListener() {
                @Override
                public void onSuccess(Boolean booleanVal) {
                    if (booleanVal)
                        audioCallAction.setVisibility(View.VISIBLE);
                    else
                        audioCallAction.setVisibility(View.GONE);
                }
            });
        }

        if (FeatureRestriction.getColor()!=null) {
            audioCallAction.setImageTintList(ColorStateList.valueOf(
                    Color.parseColor(FeatureRestriction.getColor())));
            videoCallAction.setImageTintList(ColorStateList.valueOf(
                    Color.parseColor(FeatureRestriction.getColor())));
            infoAction.setImageTintList(ColorStateList.valueOf(
                    Color.parseColor(FeatureRestriction.getColor())));
        }

        FeatureRestriction.isShareCopyForwardMessageEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                shareVisible = booleanVal;
                copyVisible = booleanVal;
                forwardVisible = booleanVal;
            }
        });
        FeatureRestriction.isThreadedMessagesEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                threadVisible = booleanVal;
            }
        });
        FeatureRestriction.isMessageRepliesEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                replyVisible = booleanVal;
            }
        });
        FeatureRestriction.isEditMessageEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                editVisible = booleanVal;
            }
        });
        FeatureRestriction.isDeleteMessageEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                deleteVisible = booleanVal;
            }
        });
        FeatureRestriction.isReactionEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                reactionVisible = booleanVal;
            }
        });
        FeatureRestriction.isMessageTranslationEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                translateVisible = booleanVal;
            }
        });
        FeatureRestriction.isShowReplyPrivately(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                replyPrivately = booleanVal;
            }
        });

        FeatureRestriction.isLiveReactionEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (!booleanVal)
                    composeBox.liveReactionBtn.setVisibility(View.GONE);
            }
        });

         FeatureRestriction.isHideDeleteMessageEnabled(new FeatureRestriction.OnSuccessListener() {
             @Override
             public void onSuccess(Boolean booleanVal) {
                 hideDeleteMessage = booleanVal;
             }
         });

    }

}
