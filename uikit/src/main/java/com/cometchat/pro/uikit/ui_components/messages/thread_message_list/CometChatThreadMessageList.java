package com.cometchat.pro.uikit.ui_components.messages.thread_message_list;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.media.MediaPlayer;
import android.media.MediaRecorder;
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
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.TypingIndicator;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.groups.group_details.CometChatGroupDetailActivity;
import com.cometchat.pro.uikit.ui_components.messages.extensions.Collaborative.CometChatWebViewActivity;
import com.cometchat.pro.uikit.ui_components.messages.extensions.Extensions;
import com.cometchat.pro.uikit.ui_components.messages.forward_message.CometChatForwardMessageActivity;
import com.cometchat.pro.uikit.ui_components.messages.live_reaction.LiveReactionListener;
import com.cometchat.pro.uikit.ui_components.messages.live_reaction.ReactionClickListener;
import com.cometchat.pro.uikit.ui_components.messages.message_actions.CometChatMessageActions;
import com.cometchat.pro.uikit.ui_components.messages.message_actions.listener.MessageActionCloseListener;
import com.cometchat.pro.uikit.ui_components.messages.message_actions.listener.OnMessageLongClick;
import com.cometchat.pro.uikit.ui_components.messages.message_information.CometChatMessageInfoScreenActivity;
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_components.shared.CometChatSnackBar;
import com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar;
import com.cometchat.pro.uikit.ui_components.shared.cometchatComposeBox.CometChatComposeBox;
import com.cometchat.pro.uikit.ui_components.shared.cometchatComposeBox.listener.ComposeActionListener;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.CometChatReactionDialog;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.listener.OnReactionClickListener;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.model.Reaction;
import com.cometchat.pro.uikit.ui_components.shared.cometchatSmartReplies.CometChatSmartReplies;
import com.cometchat.pro.uikit.ui_components.users.user_details.CometChatUserDetailScreenActivity;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_resources.utils.CallUtils;
import com.cometchat.pro.uikit.ui_resources.utils.CometChatError;
import com.cometchat.pro.uikit.ui_resources.utils.FontUtils;
import com.cometchat.pro.uikit.ui_resources.utils.MediaUtils;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.cometchat.pro.uikit.ui_resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.pro.uikit.ui_resources.utils.keyboard_utils.KeyBoardUtils;
import com.cometchat.pro.uikit.ui_resources.utils.sticker_header.StickyHeaderDecoration;
import com.cometchat.pro.uikit.ui_settings.FeatureRestriction;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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


public class CometChatThreadMessageList extends Fragment implements View.OnClickListener,
        OnMessageLongClick, MessageActionCloseListener {

    private static final String TAG = "CometChatThreadScreen";
    private static final int LIMIT = 30;
    private RelativeLayout bottomLayout;
    private String name = "";
    private String conversationName = "";
    private MessagesRequest messagesRequest;    //Used to fetch messages.
    private CometChatComposeBox composeBox;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioFileNameWithPath;
    private RecyclerView rvChatListView;    //Used to display list of messages.
    private ThreadAdapter messageAdapter;
    private LinearLayoutManager linearLayoutManager;
    private CometChatSmartReplies rvSmartReply;
    private ShimmerFrameLayout messageShimmer;
    /**
     * <b>Avatar</b> is a UI Kit Component which is used to display user and group avatars.
     */
    private TextView tvName;
    private TextView tvTypingIndicator;
    private CometChatAvatar senderAvatar;
    private TextView senderName;
    private TextView tvSentAt;
    private String Id;
    private Context context;
    private LinearLayout blockUserLayout;
    private TextView blockedUserName;
    private StickyHeaderDecoration stickyHeaderDecoration;
    private String avatarUrl;
    private Toolbar toolbar;
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
    private long messageSentAt;
    private String messageType;
    private String message;
    private String messageFileName;
    private int messageSize;
    private String messageMimeType;
    private String messageExtension;
    private int parentId;
    private String type;
    private String groupOwnerId;

    private TextView textMessage;
    private ImageView imageMessage;
    private VideoView videoMessage;
    private RelativeLayout fileMessage;
    private RelativeLayout locationMessage;
    private LinearLayout pollMessage;
    private ImageView stickerMessage;
    private RelativeLayout whiteboardMessage;
    private RelativeLayout writeboardMessage;


    private TextView whiteBoardTxt;
    private TextView writeBoardTxt;
    private MaterialButton joinWhiteBoard;
    private MaterialButton joinWriteBoard;

    private TextView pollQuestionTv;
    private LinearLayout pollOptionsLL;

    private ImageView mapView;
    private TextView addressView;
    private TextView fileName;
    private TextView fileSize;
    private TextView fileExtension;
    private TextView sentAt;
    private int replyCount;
    private TextView tvReplyCount;
    private NestedScrollView nestedScrollView;
    private LinearLayout noReplyMessages;
    private ImageView ivForwardMessage;
    private boolean isParent = true;
    private ImageView ivMoreOption;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private String parentMessageCategory;
    private double LATITUDE;
    private double LONGITUDE;
    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double parentMessageLatitude, parentMessageLongitude;
    private String pollQuestion,pollOptions;
    private ArrayList<String> pollResult;
    private TextView totalCount;
    private int voteCount;
    private CometChatMessageActions messageActionFragment;

    private ImageView imageView;
    private FrameLayout container;
    public ObjectAnimator animation;

    private int resultIntentCode;

    private ImageView addReaction;
    private ChipGroup reactionLayout;
    private HashMap<String,String> reactionInfo = new HashMap<>();

    boolean shareVisible;
    boolean copyVisible;
    boolean forwardVisible;
    boolean threadVisible = false;
    boolean replyVisible;
    boolean translateVisible;
    boolean reactionVisible;
    boolean editVisible;
    boolean retryVisible = false;
    boolean replyPrivately;
    boolean deleteVisible;

    boolean hideDeleteMessages;

    public CometChatThreadMessageList() {
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
            parentId = getArguments().getInt(UIKitConstants.IntentStrings.PARENT_ID,0);
            replyCount = getArguments().getInt(UIKitConstants.IntentStrings.REPLY_COUNT,0);
            type = getArguments().getString(UIKitConstants.IntentStrings.TYPE);
            Id = getArguments().getString(UIKitConstants.IntentStrings.ID);
            avatarUrl = getArguments().getString(UIKitConstants.IntentStrings.AVATAR);
            name = getArguments().getString(UIKitConstants.IntentStrings.NAME);
            conversationName = getArguments().getString(UIKitConstants.IntentStrings.CONVERSATION_NAME);
            messageType = getArguments().getString(UIKitConstants.IntentStrings.MESSAGE_TYPE);
            messageSentAt = getArguments().getLong(UIKitConstants.IntentStrings.SENTAT);
            parentMessageCategory = getArguments().getString(UIKitConstants.IntentStrings.MESSAGE_CATEGORY);
            if (messageType.equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                message = getArguments().getString(UIKitConstants.IntentStrings.TEXTMESSAGE);
            } else if (messageType.equals(UIKitConstants.IntentStrings.LOCATION)) {
                parentMessageLatitude = getArguments().getDouble(UIKitConstants.IntentStrings.LOCATION_LATITUDE);
                parentMessageLongitude = getArguments().getDouble(UIKitConstants.IntentStrings.LOCATION_LONGITUDE);

            } else if (messageType.equals(UIKitConstants.IntentStrings.POLLS)) {
                pollQuestion = getArguments().getString(UIKitConstants.IntentStrings.POLL_QUESTION);
                pollOptions = getArguments().getString(UIKitConstants.IntentStrings.POLL_OPTION);
                pollResult = getArguments().getStringArrayList(UIKitConstants.IntentStrings.POLL_RESULT);
                voteCount = getArguments().getInt(UIKitConstants.IntentStrings.POLL_VOTE_COUNT);
            } else if (messageType.equals(UIKitConstants.IntentStrings.STICKERS)) {
                message = getArguments().getString(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL);
                messageFileName = getArguments().getString(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME);
            } else if (messageType.equals(UIKitConstants.IntentStrings.WHITEBOARD) ||
                    messageType.equals(UIKitConstants.IntentStrings.WRITEBOARD)) {
                message = getArguments().getString(UIKitConstants.IntentStrings.TEXTMESSAGE);
            } else {
                message = getArguments().getString(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL);
                messageFileName = getArguments().getString(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME);
                messageExtension = getArguments().getString(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION);
                messageSize = getArguments().getInt(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE, 0);
                messageMimeType = getArguments().getString(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_MIME_TYPE);
            }

            reactionInfo = (HashMap<String, String>) getArguments().getSerializable(UIKitConstants.IntentStrings.REACTION_INFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cometchat_thread_message, container, false);
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
        nestedScrollView = view.findViewById(R.id.nested_scrollview);
        noReplyMessages = view.findViewById(R.id.no_reply_layout);
        ivMoreOption = view.findViewById(R.id.ic_more_option);
        ivMoreOption.setOnClickListener(this);
        ivForwardMessage = view.findViewById(R.id.ic_forward_option);
        ivForwardMessage.setOnClickListener(this);
        textMessage = view.findViewById(R.id.tv_textMessage);
        imageMessage = view.findViewById(R.id.iv_imageMessage);
        videoMessage = view.findViewById(R.id.vv_videoMessage);
        fileMessage = view.findViewById(R.id.rl_fileMessage);
        locationMessage = view.findViewById(R.id.rl_locationMessage);
        mapView = view.findViewById(R.id.iv_mapView);
        addressView = view.findViewById(R.id.tv_address);
        fileName = view.findViewById(R.id.tvFileName);
        fileSize = view.findViewById(R.id.tvFileSize);
        fileExtension = view.findViewById(R.id.tvFileExtension);

        stickerMessage = view.findViewById(R.id.iv_stickerMessage);

        whiteboardMessage = view.findViewById(R.id.whiteboard_vw);
        whiteBoardTxt = view.findViewById(R.id.whiteboard_message);
        joinWhiteBoard = view.findViewById(R.id.join_whiteboard);
        writeboardMessage = view.findViewById(R.id.writeboard_vw);
        writeBoardTxt = view.findViewById(R.id.writeboard_message);
        joinWriteBoard = view.findViewById(R.id.join_whiteboard);

        pollMessage = view.findViewById(R.id.poll_message);
        pollQuestionTv = view.findViewById(R.id.tv_question);
        pollOptionsLL = view.findViewById(R.id.options_group);
        totalCount = view.findViewById(R.id.total_votes);

        if (messageType.equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
            imageMessage.setVisibility(View.VISIBLE);
            Glide.with(context).load(message).into(imageMessage);
        } else if (messageType.equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
            videoMessage.setVisibility(VISIBLE);
            MediaController mediacontroller = new MediaController(getContext());
            mediacontroller.setAnchorView(videoMessage);
            videoMessage.setMediaController(mediacontroller);
            videoMessage.setVideoURI(Uri.parse(message));
        } else if (messageType.equals(CometChatConstants.MESSAGE_TYPE_FILE) ||
                messageType.equals(CometChatConstants.MESSAGE_TYPE_AUDIO)) {
            fileMessage.setVisibility(VISIBLE);
            if (messageFileName!=null)
                fileName.setText(messageFileName);
            if (messageExtension!=null)
                fileExtension.setText(messageExtension);

            fileSize.setText(Utils.getFileSize(messageSize));
        } else if (messageType.equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
            textMessage.setVisibility(View.VISIBLE);
            textMessage.setText(message);
        } else if (messageType.equals(UIKitConstants.IntentStrings.STICKERS)) {
            ivForwardMessage.setVisibility(GONE);
            stickerMessage.setVisibility(View.VISIBLE);
            Glide.with(context).load(message).into(stickerMessage);
        } else if (messageType.equals(UIKitConstants.IntentStrings.WHITEBOARD)) {
            ivForwardMessage.setVisibility(GONE);
            whiteboardMessage.setVisibility(View.VISIBLE);
            if (name.equals(loggedInUser.getName()))
                whiteBoardTxt.setText(getString(R.string.you_created_whiteboard));
            else
                whiteBoardTxt.setText(name+" "+getString(R.string.has_shared_whiteboard));
            joinWhiteBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String boardUrl = message;
                    Intent intent = new Intent(context, CometChatWebViewActivity.class);
                    intent.putExtra(UIKitConstants.IntentStrings.URL, boardUrl);
                    startActivity(intent);
                }
            });
        } else if (messageType.equals(UIKitConstants.IntentStrings.WRITEBOARD)) {
            ivForwardMessage.setVisibility(GONE);
            writeboardMessage.setVisibility(View.VISIBLE);
            if (name.equals(loggedInUser.getName()))
                writeBoardTxt.setText(getString(R.string.you_created_document));
            else
                writeBoardTxt.setText(name+" "+getString(R.string.has_shared_document));
            joinWriteBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String boardUrl = message;
                    Intent intent = new Intent(context, CometChatWebViewActivity.class);
                    intent.putExtra(UIKitConstants.IntentStrings.URL, boardUrl);
                    startActivity(intent);
                }
            });
        }  else if (messageType.equals(UIKitConstants.IntentStrings.LOCATION)) {
            initLocation();
            locationMessage.setVisibility(VISIBLE);
            addressView.setText(Utils.getAddress(context, parentMessageLatitude, parentMessageLongitude));
            String mapUrl = UIKitConstants.MapUrl.MAPS_URL +parentMessageLatitude+","+parentMessageLongitude+"&key="+ UIKitConstants.MapUrl.MAP_ACCESS_KEY;
            Glide.with(context)
                    .load(mapUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(mapView);
        } else if (messageType.equals(UIKitConstants.IntentStrings.POLLS)) {
            ivForwardMessage.setVisibility(GONE);
            pollMessage.setVisibility(VISIBLE);
            totalCount.setText(voteCount+" Votes");
            pollQuestionTv.setText(pollQuestion);
            try {
                JSONObject options = new JSONObject(pollOptions);
                ArrayList<String> voterInfo = pollResult;
                for (int k = 0; k < options.length(); k++) {
                    LinearLayout linearLayout = new LinearLayout(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout
                            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    linearLayout.setPadding(8,8,8,8);
                    linearLayout.setBackground(context.getResources()
                            .getDrawable(R.drawable.cc_message_bubble_right));
                    linearLayout.setBackgroundTintList(ColorStateList.valueOf(context.getResources()
                            .getColor(R.color.textColorWhite)));
                    layoutParams.bottomMargin = (int) Utils.dpToPx(context, 8);
                    linearLayout.setLayoutParams(layoutParams);

                    TextView textViewPercentage = new TextView(context);
                    TextView textViewOption = new TextView(context);
                    textViewPercentage.setPadding(16, 4, 0, 4);
                    textViewOption.setPadding(16, 4, 0, 4);
                    textViewOption.setTextAppearance(context, R.style.TextAppearance_AppCompat_Medium);
                    textViewPercentage.setTextAppearance(context, R.style.TextAppearance_AppCompat_Medium);

                    textViewPercentage.setTextColor(context.getResources().getColor(R.color.primaryTextColor));
                    textViewOption.setTextColor(context.getResources().getColor(R.color.primaryTextColor));

                    String optionStr = options.getString(String.valueOf(k + 1));
                    if (voteCount>0) {
                        int percentage = Math.round((Integer.parseInt(voterInfo.get(k)) * 100) /
                                voteCount);
                        if (percentage > 0)
                            textViewPercentage.setText(percentage + "% ");
                    }
                    textViewOption.setText(optionStr);
                    int finalK = k;
                    if (pollOptionsLL.getChildCount()!=options.length()) {
                        linearLayout.addView(textViewPercentage);
                        linearLayout.addView(textViewOption);
                        pollOptionsLL.addView(linearLayout);
                    }
                    textViewOption.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("vote",finalK+1);
                                jsonObject.put("id",baseMessage.getId());
                                CometChat.callExtension("polls", "POST", "/v1/vote",
                                        jsonObject,new CometChat.CallbackListener<JSONObject>() {
                                            @Override
                                            public void onSuccess(JSONObject jsonObject) {
                                                // Voted successfully
                                                Log.e(TAG, "onSuccess: "+jsonObject.toString());
                                                CometChatSnackBar.show(context,rvChatListView,
                                                        context.getString(R.string.voted_success), CometChatSnackBar.SUCCESS);
                                            }

                                            @Override
                                            public void onError(CometChatException e) {
                                                // Some error occured
                                                CometChatSnackBar.show(context,rvChatListView,
                                                        CometChatError.Extension
                                                                .localized(e,"polls"),
                                                        CometChatSnackBar.ERROR);
                                            }
                                        });
                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onError: "+e.getMessage());
                            }
                        }
                    });
                 }
            } catch (Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "setPollsData: "+e.getMessage());
            }
        }
        addReaction = view.findViewById(R.id.add_reaction);
        reactionLayout = view.findViewById(R.id.reactions_layout);
        if (reactionInfo.size()>0)
            reactionLayout.setVisibility(VISIBLE);
        setReactionForParentMessage();
        addReaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CometChatReactionDialog reactionDialog = new CometChatReactionDialog();
                reactionDialog.setOnEmojiClick(new OnReactionClickListener() {
                    @Override
                    public void onEmojiClicked(Reaction emojicon) {
                        JSONObject body=new JSONObject();
                        try {
                            body.put("msgId", parentId);
                            body.put("emoji", emojicon.getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        CometChat.callExtension("reactions", "POST", "/v1/react", body,
                                new CometChat.CallbackListener<JSONObject>() {
                                    @Override
                                    public void onSuccess(JSONObject responseObject) {
                                        reactionLayout.setVisibility(VISIBLE);
                                        reactionDialog.dismiss();
                                        Log.e(TAG, "onSuccess: "+responseObject);
                                        // ReactionModel added successfully.
                                    }

                                    @Override
                                    public void onError(CometChatException e) {
                                        CometChatSnackBar.show(context,rvChatListView,
                                                CometChatError.Extension
                                                        .localized(e,"reactions"),
                                                CometChatSnackBar.ERROR);
                                        // Some error occured.
                                    }
                                });
                    }
                });
                reactionDialog.show(getFragmentManager(),"ReactionThreadDialog");
            }
        });

        bottomLayout = view.findViewById(R.id.bottom_layout);
        composeBox = view.findViewById(R.id.message_box);
        messageShimmer = view.findViewById(R.id.shimmer_layout);
        composeBox = view.findViewById(R.id.message_box);
        composeBox.usedIn(CometChatThreadMessageListActivity.class.getName());
        composeBox.hidePollOption(true);
        composeBox.hideStickerOption(true);
        composeBox.hideWriteBoardOption(true);
        composeBox.hideWhiteBoardOption(true);
        composeBox.hideGroupCallOption(true);
        composeBox.hideRecordOption(true);
        composeBox.hideSendButton(false);

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

        setComposeBoxListener();

        rvSmartReply = view.findViewById(R.id.rv_smartReply);


        editMessageLayout = view.findViewById(R.id.editMessageLayout);
        tvMessageTitle = view.findViewById(R.id.tv_message_layout_title);
        tvMessageSubTitle = view.findViewById(R.id.tv_message_layout_subtitle);
        ImageView ivMessageClose = view.findViewById(R.id.iv_message_close);
        ivMessageClose.setOnClickListener(this);

        replyMessageLayout = view.findViewById(R.id.replyMessageLayout);
        replyTitle = view.findViewById(R.id.tv_reply_layout_title);
        replyMessage = view.findViewById(R.id.tv_reply_layout_subtitle);
        replyMedia = view.findViewById(R.id.iv_reply_media);
        replyClose = view.findViewById(R.id.iv_reply_close);
        replyClose.setOnClickListener(this);

        senderAvatar = view.findViewById(R.id.av_sender);
        setAvatar();
        senderName = view.findViewById(R.id.tv_sender_name);
        senderName.setText(name);
        sentAt = view.findViewById(R.id.tv_message_time);
        sentAt.setText(String.format(getString(R.string.sentattxt), Utils.getLastMessageDate(context,messageSentAt)));
        tvReplyCount = view.findViewById(R.id.thread_reply_count);
        rvChatListView = view.findViewById(R.id.rv_message_list);
        if (parentMessageCategory.equals(CometChatConstants.CATEGORY_CUSTOM)) {
            ivMoreOption.setVisibility(GONE);
        }
        if (replyCount>0) {
            tvReplyCount.setText(replyCount +" "+ getString(R.string.replies));
            noReplyMessages.setVisibility(GONE);
        }
        else {
            noReplyMessages.setVisibility(VISIBLE);
        }

        MaterialButton unblockUserBtn = view.findViewById(R.id.btn_unblock_user);
        unblockUserBtn.setOnClickListener(this);
        blockedUserName = view.findViewById(R.id.tv_blocked_user_name);
        blockUserLayout = view.findViewById(R.id.blocked_user_layout);
        tvName = view.findViewById(R.id.tv_name);
        tvTypingIndicator = view.findViewById(R.id.tv_typing);
        toolbar = view.findViewById(R.id.chatList_toolbar);
        toolbar.setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        tvName.setTypeface(fontUtils.getTypeFace(FontUtils.robotoMedium));
        tvName.setText(String.format(getString(R.string.thread_in_name),conversationName));
        setAvatar();
        rvChatListView.setLayoutManager(linearLayoutManager);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Utils.isDarkMode(context)) {
            ivMoreOption.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            ivForwardMessage.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            bottomLayout.setBackgroundColor(getResources().getColor(R.color.darkModeBackground));
            toolbar.setBackgroundColor(getResources().getColor(R.color.grey));
            editMessageLayout.setBackground(getResources().getDrawable(R.drawable.left_border_dark));
            replyMessageLayout.setBackground(getResources().getDrawable(R.drawable.left_border_dark));
            composeBox.setBackgroundColor(getResources().getColor(R.color.darkModeBackground));
            rvChatListView.setBackgroundColor(getResources().getColor(R.color.darkModeBackground));
            tvName.setTextColor(getResources().getColor(R.color.textColorWhite));
        } else {
            ivMoreOption.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryTextColor)));
            ivForwardMessage.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryTextColor)));
            bottomLayout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            toolbar.setBackgroundColor(getResources().getColor(R.color.textColorWhite));
            editMessageLayout.setBackground(getResources().getDrawable(R.drawable.left_border));
            replyMessageLayout.setBackground(getResources().getDrawable(R.drawable.left_border));
            composeBox.setBackgroundColor(getResources().getColor(R.color.textColorWhite));
            rvChatListView.setBackgroundColor(getResources().getColor(R.color.textColorWhite));
            tvName.setTextColor(getResources().getColor(R.color.primaryTextColor));
        }

        KeyBoardUtils.setKeyboardVisibilityListener(getActivity(),(View) rvChatListView.getParent(), keyboardVisible -> {
            if (keyboardVisible) {
                scrollToBottom();
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

        //Check Ongoing Call
        onGoingCallView = view.findViewById(R.id.ongoing_call_view);
        onGoingCallClose = view.findViewById(R.id.close_ongoing_view);
        onGoingCallTxt = view.findViewById(R.id.ongoing_call);
        checkOnGoingCall();
    }

    private void setReactionForParentMessage() {
        for (String key : reactionInfo.keySet()) {
            Chip chip = new Chip(context);
            chip.setChipStrokeWidth(2f);
            chip.setChipBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(android.R.color.transparent)));
            chip.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor(FeatureRestriction.getColor())));
            chip.setText(key + " " + reactionInfo.get(key));
            reactionLayout.addView(chip);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject body=new JSONObject();
                    try {
                        body.put("msgId", parentId);
                        body.put("emoji", key);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    CometChat.callExtension("reactions", "POST", "/v1/react", body,
                            new CometChat.CallbackListener<JSONObject>() {
                                @Override
                                public void onSuccess(JSONObject responseObject) {
                                    Log.e(TAG, "onSuccess: "+responseObject);
                                    // ReactionModel added successfully.
                                }

                                @Override
                                public void onError(CometChatException e) {
                                    CometChatSnackBar.show(context,rvChatListView,
                                            CometChatError.Extension
                                                    .localized(e,"reactions"),
                                            CometChatSnackBar.ERROR);
                                    // Some error occured.
                                }
                            });
                }
            });
        }

        fetchSettings();
    }

    private void checkOnGoingCall() {
            if(CometChat.getActiveCall()!=null && CometChat.getActiveCall().getCallStatus().equals(CometChatConstants.CALL_STATUS_ONGOING) && CometChat.getActiveCall().getSessionId()!=null) {
                if(onGoingCallView!=null)
                    onGoingCallView.setVisibility(View.VISIBLE);
                if(onGoingCallTxt!=null) {
                    onGoingCallTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onGoingCallView.setVisibility(View.GONE);
                            CallUtils.joinOnGoingCall(getContext(), CometChat.getActiveCall());
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
                Log.e(TAG, "checkOnGoingCall: "+ CometChat.getActiveCall().toString());
            }
    }

    private void setComposeBoxListener() {

        composeBox.setComposeBoxListener(new ComposeActionListener() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0) {
                    sendTypingIndicator(false);
                } else {
                    sendTypingIndicator(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (typingTimer == null) {
                    typingTimer = new Timer();
                }
                endTypingTimer();
            }

            @Override
            public void onAudioActionClicked() {
                if (Utils.hasPermissions(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
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
                editText.setText("");
                editText.setHint(getString(R.string.message));
                if (isEdit) {
                    if (isParent)
                        editThread(message);
                    else {
                        editMessage(baseMessage, message);
                    }
                    editMessageLayout.setVisibility(GONE);
                } else if(isReply){
                    replyMessage(baseMessage,message);
                    replyMessageLayout.setVisibility(GONE);
                } else if (!message.isEmpty())
                    sendMessage(message);
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

                    boolean provider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    if (!provider) {
                        turnOnLocation();
                    }
                    else {
                        getLocation();
                    }
                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, UIKitConstants.RequestCode.LOCATION);
                }
            }
        });
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
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(mapView);

        builder.setPositiveButton(getString(R.string.share), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendCustomMessage("LOCATION", customData);
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

    private void sendCustomMessage(String customType, JSONObject customData) {
        CustomMessage customMessage;

        if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            customMessage = new CustomMessage(Id, CometChatConstants.RECEIVER_TYPE_USER, customType, customData);
        else
            customMessage = new CustomMessage(Id, CometChatConstants.RECEIVER_TYPE_GROUP, customType, customData);

        customMessage.setParentMessageId(parentId);
        CometChat.sendCustomMessage(customMessage, new CometChat.CallbackListener<CustomMessage>() {
            @Override
            public void onSuccess(CustomMessage customMessage) {
                noReplyMessages.setVisibility(GONE);
                if (messageAdapter != null) {
                    messageAdapter.addMessage(customMessage);
                    setReply();
                    scrollToBottom();
                }
            }

            @Override
            public void onError(CometChatException e) {
                if (getActivity() != null) {
                    CometChatSnackBar.show(context,rvChatListView, CometChatError.localized(e), CometChatSnackBar.ERROR);
                }
            }
        });
    }

    private void turnOnLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Turn on GPS");
        builder.setPositiveButton("ON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), UIKitConstants.RequestCode.LOCATION);
            }
        }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void initLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));
        locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
        if (Utils.hasPermissions(context,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})) {
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, UIKitConstants.RequestCode.LOCATION);
        }
    }

    private void editThread(String editMessage) {
        isEdit = false;

        TextMessage textmessage;
        if (type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            textmessage = new TextMessage(Id, editMessage, CometChatConstants.RECEIVER_TYPE_USER);
        else
            textmessage = new TextMessage(Id, editMessage, CometChatConstants.RECEIVER_TYPE_GROUP);
        sendTypingIndicator(true);
        textmessage.setId(parentId);
        CometChat.editMessage(textmessage, new CometChat.CallbackListener<BaseMessage>() {
            @Override
            public void onSuccess(BaseMessage baseMessage) {
                textMessage.setText(((TextMessage)baseMessage).getText());
                message = ((TextMessage) baseMessage).getText();
            }

            @Override
            public void onError(CometChatException e) {
                CometChatSnackBar.show(context,rvChatListView, CometChatError.localized(e), CometChatSnackBar.ERROR);
                Log.d(TAG, "onError: " + e.getMessage());
            }
        });
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
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { }
                else
                    showPermissionSnackBar(view.findViewById(R.id.message_box), getResources().getString(R.string.grant_location_permission));
                break;
        }
    }

    private void showPermissionSnackBar(View view, String message) {
        CometChatSnackBar.show(context,view,message, CometChatSnackBar.WARNING);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
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
                isBlockedByMe = false;
                messagesRequest=null;
            }

            @Override
            public void onError(CometChatException e) {
                CometChatSnackBar.show(context,rvChatListView, CometChatError.localized(e)
                        , CometChatSnackBar.ERROR);
            }
        });
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
            if (type.equals(CometChatConstants.RECEIVER_TYPE_USER))
                messagesRequest = new MessagesRequest.MessagesRequestBuilder().setLimit(LIMIT)
                        .setParentMessageId(parentId)
                        .setCategories(UIKitConstants.MessageRequest.messageCategoriesForUser)
                        .setTypes(UIKitConstants.MessageRequest.messageTypesForUser)
                        .hideDeletedMessages(hideDeleteMessages)
                    .build();
            else
                messagesRequest = new MessagesRequest.MessagesRequestBuilder().setLimit(LIMIT)
                        .setParentMessageId(parentId)
                        .setCategories(UIKitConstants.MessageRequest.messageCategoriesForGroup)
                        .setTypes(UIKitConstants.MessageRequest.messageTypesForGroup)
                        .hideDeletedMessages(hideDeleteMessages)
                        .build();
        }
        messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {

            @Override
            public void onSuccess(List<BaseMessage> baseMessages) {
                isInProgress = false;
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
                CometChatSnackBar.show(context,rvChatListView, CometChatError.localized(e), CometChatSnackBar.ERROR);
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
        scrollToBottom();
    }


    /**
     * This method is used to initialize the message adapter if it is empty else it helps
     * to update the messagelist in adapter.
     *
     * @param messageList is a list of messages which will be added.
     */
    private void initMessageAdapter(List<BaseMessage> messageList) {
        if (messageAdapter == null) {
            messageAdapter = new ThreadAdapter(getActivity(), messageList, type);
            rvChatListView.setAdapter(messageAdapter);
            messageAdapter.notifyDataSetChanged();
        } else {
            messageAdapter.updateList(messageList);

        }
        if (!isBlockedByMe && rvSmartReply.getAdapter().getItemCount()==0&&rvSmartReply.getVisibility() == GONE) {
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

        switch (requestCode) {
            case UIKitConstants.RequestCode.AUDIO:
                if (data!=null) {
                    resultIntentCode = UIKitConstants.RequestCode.AUDIO;
                    File file = MediaUtils.getRealPath(getContext(),data.getData(),false);
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
                    if (mimeType!=null && mimeType.contains("image")) {
                        if (file.exists())
                            sendMediaMessage(file, CometChatConstants.MESSAGE_TYPE_IMAGE);
                        else
                            Snackbar.make(rvChatListView, R.string.file_not_exist, Snackbar.LENGTH_LONG).show();
                    }
                    else {
                        if (file.exists())
                            sendMediaMessage(file, CometChatConstants.MESSAGE_TYPE_VIDEO);
                        else
                            Snackbar.make(rvChatListView, R.string.file_not_exist, Snackbar.LENGTH_LONG).show();
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
                    Snackbar.make(rvChatListView, R.string.file_not_exist,Snackbar.LENGTH_LONG).show();

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
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    Toast.makeText(getContext(), "Gps enabled",Toast.LENGTH_SHORT).show();
                    getLocation();
                }
                else {
                    Toast.makeText(getContext(), "Gps disabled",Toast.LENGTH_SHORT).show();
                }
        }

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

        mediaMessage.setParentMessageId(parentId);
        CometChat.sendMediaMessage(mediaMessage, new CometChat.CallbackListener<MediaMessage>() {
            @Override
            public void onSuccess(MediaMessage mediaMessage) {
                noReplyMessages.setVisibility(GONE);
                Log.d(TAG, "sendMediaMessage onSuccess: " + mediaMessage.toString());
                if (messageAdapter != null) {
                    setReply();
                    messageAdapter.updateChangedMessage(mediaMessage);
                }
            }

            @Override
            public void onError(CometChatException e) {
                if (messageAdapter != null) {
                    mediaMessage.setSentAt(-1);
                    messageAdapter.updateChangedMessage(mediaMessage);
                }
                if (getActivity() != null) {
                    CometChatSnackBar.show(context,rvChatListView, CometChatError.localized(e)
                            , CometChatSnackBar.ERROR);
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
                        isBlockedByMe = true;
                        rvSmartReply.setVisibility(GONE);
                        toolbar.setSelected(false);
                        blockedUserName.setText("You've blocked " + user.getName());
                        blockUserLayout.setVisibility(View.VISIBLE);
                    } else {
                        isBlockedByMe = false;
                        blockUserLayout.setVisibility(GONE);
                    }
                    tvName.setText(String.format(getString(R.string.thread_in_name),user.getName()));
                    Log.d(TAG, "onSuccess: " + user.toString());
                }

            }

            @Override
            public void onError(CometChatException e) {
                Log.e(TAG, "getUser():onError: "+e.getCode());
                Toast.makeText(context, CometChatError.localized(e), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAvatar() {
        if (avatarUrl != null && !avatarUrl.isEmpty())
            senderAvatar.setAvatar(avatarUrl);
        else {
            senderAvatar.setInitials(name);
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
                    loggedInUserScope = group.getScope();
                    groupOwnerId = group.getOwner();

                    tvName.setText(String.format(getString(R.string.thread_in_name),group.getName()));
                }

            }

            @Override
            public void onError(CometChatException e) {
                Log.e(TAG, "getGroup():onError: "+e.getCode());
                Toast.makeText(context, CometChatError.localized(e), Toast.LENGTH_SHORT).show();
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

        textMessage.setParentMessageId(parentId);
        sendTypingIndicator(true);

        textMessage.setCategory(CometChatConstants.CATEGORY_MESSAGE);
        textMessage.setSender(loggedInUser);
        textMessage.setMuid(System.currentTimeMillis()+"");
        if (messageAdapter != null) {
            MediaUtils.playSendSound(context, R.raw.outgoing_message);
            messageAdapter.addMessage(textMessage);
            scrollToBottom();
        }
        noReplyMessages.setVisibility(GONE);
        isSmartReplyClicked=false;
        CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
            @Override
            public void onSuccess(TextMessage textMessage) {
                if (messageAdapter!=null) {
                    setReply();
                    messageAdapter.updateChangedMessage(textMessage);
                }

            }

            @Override
            public void onError(CometChatException e) {

                if (messageAdapter != null) {
                    textMessage.setSentAt(-1);
                    messageAdapter.updateChangedMessage(textMessage);
                }
                if (getActivity() != null) {
                    CometChatSnackBar.show(context,rvChatListView,
                            CometChatError.localized(e), CometChatSnackBar.ERROR);
                }
                Log.d(TAG, "onError: " + e.getMessage());
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
                if (messageAdapter != null)
                    messageAdapter.setUpdatedMessage(baseMessage);
            }

            @Override
            public void onError(CometChatException e) {

                CometChatSnackBar.show(context,rvChatListView,
                        CometChatError.localized(e), CometChatSnackBar.ERROR);
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
        isParent = true;
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
                CometChatSnackBar.show(context,rvChatListView,
                        CometChatError.localized(e), CometChatSnackBar.ERROR);
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
            JSONObject jsonObject = new JSONObject();
            JSONObject replyObject = new JSONObject();
            if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                replyObject.put("type", CometChatConstants.MESSAGE_TYPE_TEXT);
                replyObject.put("message", ((TextMessage) baseMessage).getText());
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                replyObject.put("type", CometChatConstants.MESSAGE_TYPE_IMAGE);
                replyObject.put("message", "image");
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
                replyObject.put("type", CometChatConstants.MESSAGE_TYPE_VIDEO);
                replyObject.put("message", "video");
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_FILE)) {
                replyObject.put("type", CometChatConstants.MESSAGE_TYPE_FILE);
                replyObject.put("message", "file");
            } else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_AUDIO)) {
                replyObject.put("type", CometChatConstants.MESSAGE_TYPE_AUDIO);
                replyObject.put("message", "audio");
            }
            replyObject.put("name",baseMessage.getSender().getName());
            replyObject.put("avatar",baseMessage.getSender().getAvatar());
            jsonObject.put("reply",replyObject);
            textMessage.setParentMessageId(parentId);
            textMessage.setMetadata(jsonObject);
            sendTypingIndicator(true);
            CometChat.sendMessage(textMessage, new CometChat.CallbackListener<TextMessage>() {
                @Override
                public void onSuccess(TextMessage textMessage) {
                    if (messageAdapter != null) {
                        MediaUtils.playSendSound(context, R.raw.outgoing_message);
                        messageAdapter.addMessage(textMessage);
                        scrollToBottom();
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    CometChatSnackBar.show(context,rvChatListView,
                            CometChatError.localized(e),
                            CometChatSnackBar.ERROR);
                    Log.e(TAG, "onError: "+e.getMessage());
                }
            });
        }catch (Exception e) {
            Log.e(TAG, "replyMessage: "+e.getMessage());
        }
    }

    private void scrollToBottom() {
        if (messageAdapter != null && messageAdapter.getItemCount() > 0) {
            rvChatListView.scrollToPosition(messageAdapter.getItemCount() - 1);
            final int scrollViewHeight = nestedScrollView.getHeight();
            if (scrollViewHeight > 0) {
                final View lastView = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);
                final int lastViewBottom = lastView.getBottom() + nestedScrollView.getPaddingBottom();
                final int deltaScrollY = lastViewBottom - scrollViewHeight - nestedScrollView.getScrollY();
                /* If you want to see the scroll animation, call this. */
                nestedScrollView.smoothScrollBy(0, deltaScrollY);
            }
        }
    }

    /**
     * This method is used to mark users & group message as read.
     *
     * @param baseMessage is object of BaseMessage.class. It is message which is been marked as read.
     */
    private void markMessageAsRead(BaseMessage baseMessage) {
//        CometChat.markAsRead(baseMessage);
        if (type.equals(CometChatConstants.RECEIVER_TYPE_USER))
            CometChat.markAsRead(baseMessage.getId(), baseMessage.getSender().getUid(), baseMessage.getReceiverType());
        else
            CometChat.markAsRead(baseMessage.getId(), baseMessage.getReceiverUid(), baseMessage.getReceiverType());
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
                if (parentId != message.getId())
                    updateMessage(message);
                else {
                    reactionInfo = Extensions.getReactionsOnMessage(message);
                    reactionLayout.removeAllViews();
                    setReactionForParentMessage();
                }
            }

            @Override
            public void onMessageDeleted(BaseMessage message) {
                Log.d(TAG, "onMessageDeleted: ");
                if (messageAdapter!=null) {
                    if (hideDeleteMessages)
                        messageAdapter.remove(message);
                    else
                        updateMessage(message);
                }
            }
        });
    }

    private void setMessageReciept(MessageReceipt messageReceipt) {
        if (messageAdapter != null) {
            if (messageReceipt.getReceivertype().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                if (Id!=null && messageReceipt.getSender().getUid().equals(Id)) {
                    if (messageReceipt.getReceiptType().equals(MessageReceipt.RECEIPT_TYPE_DELIVERED))
                        messageAdapter.setDeliveryReceipts(messageReceipt);
                    else
                        messageAdapter.setReadReceipts(messageReceipt);
                }
            }
        }
    }

    private void setTypingIndicator(TypingIndicator typingIndicator, boolean isShow) {
        if (typingIndicator.getReceiverType().equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER)) {
            Log.e(TAG, "onTypingStarted: " + typingIndicator);
            if (Id != null && Id.equalsIgnoreCase(typingIndicator.getSender().getUid())) {
                if (typingIndicator.getMetadata() == null)
                    typingIndicator(typingIndicator, isShow);
            }
        } else {
            if (Id != null && Id.equalsIgnoreCase(typingIndicator.getReceiverId()))
                typingIndicator(typingIndicator, isShow);
        }
    }

    private void onMessageReceived(BaseMessage message) {
        noReplyMessages.setVisibility(GONE);
        MediaUtils.playSendSound(context, R.raw.incoming_message);
        if (message.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
            if (Id != null && Id.equalsIgnoreCase(message.getSender().getUid())) {
                if (message.getParentMessageId()==parentId)
                    setMessage(message);
            } else if(Id != null && Id.equalsIgnoreCase(message.getReceiverUid()) && message.getSender().getUid().equalsIgnoreCase(loggedInUser.getUid())) {
                if (message.getParentMessageId()==parentId)
                    setMessage(message);
            }
        } else {
            if (Id != null && Id.equalsIgnoreCase(message.getReceiverUid())) {
                if (message.getParentMessageId()==parentId)
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
       setReply();
       noReplyMessages.setVisibility(GONE);
        if (messageAdapter != null) {
            messageAdapter.addMessage(message);
            checkSmartReply(message);
            markMessageAsRead(message);
            if ((messageAdapter.getItemCount() - 1) - ((LinearLayoutManager) rvChatListView.getLayoutManager()).findLastVisibleItemPosition() < 5)
                scrollToBottom();
        } else {
            messageList.add(message);
            initMessageAdapter(messageList);
        }
    }

    private void setReply() {
        replyCount = replyCount+1;
        if (replyCount==1)
            tvReplyCount.setText(replyCount+" "+getString(R.string.reply));
        else
            tvReplyCount.setText(replyCount+" "+getString(R.string.replies));
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
                                    tvTypingIndicator.setText("is Typing...");
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
                                    tvTypingIndicator.setText(typingIndicator.getSender().getName() + " is Typing...");
                            }
                        });
                    }
                    else
                        setLiveReaction();
                }
            } else {
                if (typingIndicator.getMetadata() == null)
                    tvTypingIndicator.setVisibility(GONE);
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
        sendTypingIndicator(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (!(resultIntentCode== UIKitConstants.RequestCode.GALLERY ||
                resultIntentCode== UIKitConstants.RequestCode.CAMERA ||
                resultIntentCode== UIKitConstants.RequestCode.FILE ||
                resultIntentCode== UIKitConstants.RequestCode.AUDIO)) {
            messagesRequest = null;
            messageAdapter = null;
            isNoMoreMessages = false;
            fetchMessage();
        }
        checkOnGoingCall();
        addMessageListener();

        if (messageActionFragment!=null && messageActionFragment.getFragmentManager()!=null)
            messageActionFragment.dismiss();

        if (type != null) {
            if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                new Thread(this::getUser).start();
            } else {
                new Thread(this::getGroup).start();
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
        if (id == R.id.ic_more_option) {
            CometChatMessageActions messageActionFragment = new CometChatMessageActions();
            Bundle bundle = new Bundle();
            if (messageType.equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                bundle.putBoolean("translateVisible",true);
                bundle.putBoolean("copyVisible", true);
            }
            else
                bundle.putBoolean("copyVisible",false);

            bundle.putBoolean("forwardVisible",true);
            if (name.equals(loggedInUser.getName()) && messageType.equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                bundle.putBoolean("editVisible",true);
            } else {
                bundle.putBoolean("editVisible",false);
            }

            bundle.putString("type", CometChatThreadMessageListActivity.class.getName());
            messageActionFragment.setArguments(bundle);
            showBottomSheet(messageActionFragment);
            isParent = true;
        }
        else if (id == R.id.ic_forward_option) {
            isParent = true;
            startForwardThreadActivity();
        }
        else if (id == R.id.iv_message_close) {
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
        else if (id == R.id.chatList_toolbar) {
            if (type.equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                Intent intent = new Intent(getContext(), CometChatUserDetailScreenActivity.class);
                intent.putExtra(UIKitConstants.IntentStrings.UID, Id);
                intent.putExtra(UIKitConstants.IntentStrings.NAME, conversationName);
                intent.putExtra(UIKitConstants.IntentStrings.AVATAR, avatarUrl);
                intent.putExtra(UIKitConstants.IntentStrings.IS_BLOCKED_BY_ME, isBlockedByMe);
                intent.putExtra(UIKitConstants.IntentStrings.TYPE, type);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getContext(), CometChatGroupDetailActivity.class);
                intent.putExtra(UIKitConstants.IntentStrings.GUID, Id);
                intent.putExtra(UIKitConstants.IntentStrings.NAME, conversationName);
                intent.putExtra(UIKitConstants.IntentStrings.AVATAR, avatarUrl);
                intent.putExtra(UIKitConstants.IntentStrings.TYPE, type);
                intent.putExtra(UIKitConstants.IntentStrings.MEMBER_SCOPE, loggedInUserScope);
                intent.putExtra(UIKitConstants.IntentStrings.GROUP_OWNER, groupOwnerId);
                startActivity(intent);
            }
        }
    }

    @Override
    public void setLongMessageClick(List<BaseMessage> baseMessagesList) {
        Log.e(TAG, "setLongMessageClick: " + baseMessagesList);
        isReply = false;
        isEdit = false;
        isParent = false;
        messageActionFragment = new CometChatMessageActions();
        replyMessageLayout.setVisibility(GONE);
        editMessageLayout.setVisibility(GONE);
        fetchSettings();
        List<BaseMessage> textMessageList = new ArrayList<>();
        List<BaseMessage> mediaMessageList = new ArrayList<>();
        List<BaseMessage> locationMessageList = new ArrayList<>();
        for (BaseMessage baseMessage : baseMessagesList) {
            if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                textMessageList.add(baseMessage);
            }
            else if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE) ||
                    baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_VIDEO) ||
                    baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_FILE) ||
                    baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_AUDIO) ){
                mediaMessageList.add(baseMessage);
            }
            else {
                locationMessageList.add(baseMessage);
            }
        }
        if (textMessageList.size() == 1) {
            FeatureRestriction.isMessageTranslationEnabled(new FeatureRestriction.OnSuccessListener() {
                @Override
                public void onSuccess(Boolean booleanVal) {
                    translateVisible = booleanVal;
                }
            });
            BaseMessage basemessage = textMessageList.get(0);
            if (basemessage != null && basemessage.getSender() != null) {
                if (!(basemessage instanceof Action) && basemessage.getDeletedAt() == 0) {
                    baseMessage = basemessage;
                    threadVisible = false;
                    if (basemessage.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                        FeatureRestriction.isDeleteMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                deleteVisible = booleanVal;
                            }
                        });
                        FeatureRestriction.isEditMessageEnabled(new FeatureRestriction.OnSuccessListener() {
                            @Override
                            public void onSuccess(Boolean booleanVal) {
                                editVisible = booleanVal;
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
                }
            }
        }

        if (mediaMessageList.size() == 1) {
            translateVisible = false;
            BaseMessage basemessage = mediaMessageList.get(0);
            if (basemessage != null && basemessage.getSender() != null) {
                if (!(basemessage instanceof Action) && basemessage.getDeletedAt() == 0) {
                    baseMessage = basemessage;
                    copyVisible = false;
                    threadVisible = false;
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
            }
        }
        if (locationMessageList.size() == 1){
            translateVisible = false;
            BaseMessage basemessage = locationMessageList.get(0);
            if (basemessage != null && basemessage.getSender() != null) {
                if (!(basemessage instanceof Action) && basemessage.getDeletedAt() == 0) {
                    baseMessage = basemessage;
                    threadVisible = false;
                    copyVisible = false;
                    replyVisible = false;
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
            }
        }
        baseMessages = baseMessagesList;
        Bundle bundle = new Bundle();
        bundle.putBoolean("copyVisible",copyVisible);
        bundle.putBoolean("threadVisible",threadVisible);
        bundle.putBoolean("editVisible",editVisible);
        bundle.putBoolean("deleteVisible",deleteVisible);
        bundle.putBoolean("replyVisible",replyVisible);
        bundle.putBoolean("forwardVisible",forwardVisible);
        bundle.putBoolean("translateVisible",translateVisible);
        if(CometChat.isExtensionEnabled("reactions")) {
            bundle.putBoolean("isReactionVisible",reactionVisible);
        }

        if (!baseMessage.getSender().getUid().equalsIgnoreCase(loggedInUser.getUid())) {
            bundle.putBoolean("privateReplyVisible", true);
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
        bundle.putString("type", CometChatThreadMessageListActivity.class.getName());
        messageActionFragment.setArguments(bundle);
        if (baseMessage.getSentAt()!=0) {
            if (editVisible || copyVisible || threadVisible || deleteVisible
                    || replyVisible || forwardVisible || reactionVisible) {
                showBottomSheet(messageActionFragment);
            }
        }
    }

    private void fetchSettings() {
        FeatureRestriction.isShareCopyForwardMessageEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                shareVisible = booleanVal;
                copyVisible = booleanVal;
                forwardVisible = booleanVal;
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
        FeatureRestriction.isLiveReactionEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (booleanVal) {
                    composeBox.liveReactionBtn.setVisibility(VISIBLE);
                } else {
                    composeBox.liveReactionBtn.setVisibility(GONE);
                }
            }
        });
        FeatureRestriction.isHideDeleteMessageEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                hideDeleteMessages = booleanVal;
            }
        });
    }

    private void showBottomSheet(CometChatMessageActions messageActionFragment) {
        messageActionFragment.show(getFragmentManager(),messageActionFragment.getTag());
        messageActionFragment.setMessageActionListener(new CometChatMessageActions.MessageActionListener() {

            @Override
            public void onPrivateReplyToUser() {
                if (baseMessage!=null) {
                    User user = baseMessage.getSender();
                    Intent intent = new Intent(context, CometChatMessageListActivity.class);
                    intent.putExtra(UIKitConstants.IntentStrings.UID, user.getUid());
                    intent.putExtra(UIKitConstants.IntentStrings.AVATAR, user.getAvatar());
                    intent.putExtra(UIKitConstants.IntentStrings.STATUS, user.getStatus());
                    intent.putExtra(UIKitConstants.IntentStrings.NAME, user.getName());
                    intent.putExtra(UIKitConstants.IntentStrings.LINK,user.getLink());
                    intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER);
                    startActivity(intent);
                    if (getActivity()!=null)
                        getActivity().finish();
                }
            }

            @Override
            public void onThreadMessageClick() {

            }

            @Override
            public void onEditMessageClick() {
                if (isParent)
                    editParentMessage();
                else
                    editThreadMessage();
            }

            @Override
            public void onReplyMessageClick() {}

            @Override
            public void onReplyMessagePrivately() {}

            @Override
            public void onForwardMessageClick() {
                if (isParent)
                    startForwardThreadActivity();
                else
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
                String copyMessage = "";
                if (isParent) {
                    copyMessage = message;
                    isParent = true;
                } else {
                    for (BaseMessage bMessage : baseMessages) {
                        if (bMessage.getDeletedAt() == 0 && bMessage instanceof TextMessage) {
                            copyMessage = copyMessage + "[" + Utils.getLastMessageDate(context,bMessage.getSentAt()) + "] " + bMessage.getSender().getName() + ": " + ((TextMessage) bMessage).getText();
                        }
                    }
                    isParent = false;
                }
                Log.e(TAG, "onCopy: " + message);
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ThreadMessageAdapter", copyMessage);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, getResources().getString(R.string.text_copied), Toast.LENGTH_LONG).show();
                if (messageAdapter != null) {
                    messageAdapter.clearLongClickSelectedItem();
                    messageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onShareMessageClick() {
                shareMessage();
            }

            @Override
            public void onMessageInfoClick() {
                Intent intent = new Intent(context, CometChatMessageInfoScreenActivity.class);
                if (isParent){
                }
                else {
                    intent.putExtra(UIKitConstants.IntentStrings.ID, baseMessage.getId());
                    intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE, baseMessage.getType());
                    intent.putExtra(UIKitConstants.IntentStrings.SENTAT, baseMessage.getSentAt());
                    if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                        intent.putExtra(UIKitConstants.IntentStrings.TEXTMESSAGE,
                                Extensions.checkProfanityMessage(context,baseMessage));
                    } else if (baseMessage.getCategory().equals(CometChatConstants.CATEGORY_CUSTOM)) {
                        if (((CustomMessage)baseMessage).getCustomData()!=null)
                            intent.putExtra(UIKitConstants.IntentStrings.CUSTOM_MESSAGE,
                                    ((CustomMessage) baseMessage).getCustomData().toString());
                        if (baseMessage.getType().equals(UIKitConstants.IntentStrings.LOCATION)) {
                            intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE,
                                    UIKitConstants.IntentStrings.LOCATION);
                        }
                    } else {
                        boolean isImageNotSafe = Extensions.getImageModeration(context, baseMessage);
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL,
                                ((MediaMessage) baseMessage).getAttachment().getFileUrl());
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME,
                                ((MediaMessage) baseMessage).getAttachment().getFileName());
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE,
                                ((MediaMessage) baseMessage).getAttachment().getFileSize());
                        intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION,
                                ((MediaMessage) baseMessage).getAttachment().getFileExtension());
                        intent.putExtra(UIKitConstants.IntentStrings.IMAGE_MODERATION, isImageNotSafe);
                    }
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
                    if (isParent) {
                        body.put("msgId", parentId);
                        body.put("text", textMessage.getText().toString());
                    }
                    else {
                        body.put("msgId", baseMessage.getId());
                        body.put("text", ((TextMessage) baseMessage).getText());
                    }
                    body.put("languages", languages);
                    CometChat.callExtension("message-translation", "POST", "/v2/translate", body,
                            new CometChat.CallbackListener<JSONObject>() {
                                @Override
                                public void onSuccess(JSONObject jsonObject) {
                                    if (isParent) {
                                        if (Extensions.isMessageTranslated(jsonObject,textMessage.getText().toString())) {
                                            String translatedMessage = Extensions
                                                    .getTextFromTranslatedMessage(jsonObject,
                                                            textMessage.getText().toString());
                                            textMessage.setText(translatedMessage);
                                        } else {
                                            CometChatSnackBar.show(context,rvChatListView,
                                                    getString(R.string.no_translation_available), CometChatSnackBar.INFO);
                                        }
                                    } else {
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
                                                    getString(R.string.no_translation_available), CometChatSnackBar.INFO);
                                        }
                                    }
                                }

                                @Override
                                public void onError(CometChatException e) {
                                    CometChatSnackBar.show(context,rvChatListView,
                                            CometChatError.Extension
                                                    .localized(e,"message-translation"),
                                            CometChatSnackBar.ERROR);
                                }
                            });
                } catch (Exception e) {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onRetryClick() {

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
                        CometChatSnackBar.show(context,rvChatListView,
                                CometChatError.Extension.localized(e,"reactions"),
                                CometChatSnackBar.ERROR);
                        Log.e(TAG, "onError: " + e.getCode() + e.getMessage() + e.getDetails());
                    }
                });
    }
    private void editParentMessage() {
        if (message!=null&&messageType.equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
            isEdit = true;
            isReply = false;
            tvMessageTitle.setText(getResources().getString(R.string.edit_message));
            tvMessageSubTitle.setText(message);
            composeBox.ivSend.setVisibility(View.VISIBLE);
            editMessageLayout.setVisibility(View.VISIBLE);
            composeBox.etComposeBox.setText(message);
        }
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
        }
    }

    private void editThreadMessage() {
        if (baseMessage!=null&&baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
            isEdit = true;
            isReply = false;
            isParent = false;
            tvMessageTitle.setText(getResources().getString(R.string.edit_message));
            tvMessageSubTitle.setText(((TextMessage) baseMessage).getText());
            composeBox.ivSend.setVisibility(View.VISIBLE);
            editMessageLayout.setVisibility(View.VISIBLE);
            composeBox.etComposeBox.setText(((TextMessage) baseMessage).getText());
            if (messageAdapter != null) {
                messageAdapter.setSelectedMessage(baseMessage.getId());
                messageAdapter.notifyDataSetChanged();
            }
        }
    }

    private void startForwardThreadActivity() {
        Intent intent = new Intent(getContext(), CometChatForwardMessageActivity.class);
        if (parentMessageCategory.equals(CometChatConstants.CATEGORY_MESSAGE)) {
            intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_CATEGORY, CometChatConstants.CATEGORY_MESSAGE);
            intent.putExtra(UIKitConstants.IntentStrings.TYPE, messageType);
            if (messageType.equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                intent.putExtra(CometChatConstants.MESSAGE_TYPE_TEXT, message);
                intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.MESSAGE_TYPE_TEXT);
            } else if (messageType.equals(CometChatConstants.MESSAGE_TYPE_IMAGE) ||
                    messageType.equals(CometChatConstants.MESSAGE_TYPE_AUDIO) ||
                    messageType.equals(CometChatConstants.MESSAGE_TYPE_VIDEO) ||
                    messageType.equals(CometChatConstants.MESSAGE_TYPE_FILE)) {
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME, message);
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL, message);
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_MIME_TYPE, messageMimeType);
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION, messageExtension);
                intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE, messageSize);
            }
        } else {
            intent.putExtra(UIKitConstants.IntentStrings.MESSAGE_CATEGORY, CometChatConstants.CATEGORY_CUSTOM);
            intent.putExtra(UIKitConstants.IntentStrings.TYPE, UIKitConstants.IntentStrings.LOCATION);
            try {
                intent.putExtra(UIKitConstants.IntentStrings.LOCATION_LATITUDE,parentMessageLatitude);
                intent.putExtra(UIKitConstants.IntentStrings.LOCATION_LONGITUDE,parentMessageLongitude);
            } catch (Exception e) {
                Log.e(TAG, "startForwardMessageActivityError: "+e.getMessage());
            }
        }
        startActivity(intent);
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
        } else {
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


    private void replyMessage() {
        if (baseMessage != null) {
            isReply = true;
            replyTitle.setText(baseMessage.getSender().getName());
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
            }
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
}
