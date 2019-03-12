package com.inscripts.cometchatpulse.demo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Activity.VideoViewActivity;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.CustomView.StickyHeaderAdapter;
import com.inscripts.cometchatpulse.demo.Utils.DateUtils;
import com.inscripts.cometchatpulse.demo.Utils.FontUtils;
import com.inscripts.cometchatpulse.demo.Utils.Logger;
import com.inscripts.cometchatpulse.demo.ViewHolders.LeftAudioViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.LeftFileViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.LeftImageVideoViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.LeftMessageViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.LeftReplyViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.RightAudioViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.RightFileViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.RightImageVideoViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.RightMessageViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.RightReplyViewHolder;

import org.json.JSONException;

import java.util.Date;
import java.util.List;
import static com.inscripts.cometchatpulse.demo.Utils.FileUtils.getFileExtension;
import static com.inscripts.cometchatpulse.demo.Utils.FileUtils.getFileName;


public class GroupMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        StickyHeaderAdapter<GroupMessageAdapter.DateItemHolder> {

    private static final int RIGHT_TEXT_MESSAGE = 940;

    private static final int LEFT_TEXT_MESSAGE = 489;

    private static final int ACTION_MESSAGE = 123;

    private static final int LEFT_IMAGE_MESSAGE = 510;

    private static final int RIGHT_IMAGE_MESSAGE = 380;

    private static final int LEFT_AUDIO_MESSAGE = 230;

    private static final int RIGHT_AUDIO_MESSAGE = 393;

    private static final int LEFT_VIDEO_MESSAGE = 580;

    private static final int RIGHT_VIDEO_MESSAGE = 797;

    private static final int LEFT_FILE_MESSAGE = 24;

    private static final int RIGHT_FILE_MESSAGE = 55;

    private static final int CALL_MESSAGE =964 ;

    private static final int RIGHT_MEDIA_REPLY_MESSAGE = 345;

    private static final int RIGHT_TEXT_REPLY_MESSAGE = 346;

    private static final int LEFT_MEDIA_REPLY_MESSAGE = 756;

    private static final int LEFT_TEXT_REPLY_MESSAGE = 748;

    private final Drawable drawableAvatar;


    private List<BaseMessage> messageList;

    private Context context;

    private String groupId;

    private String ownerId;

    private BaseMessage baseMessage;

    private int position;

    private String currentSenderId;

    private String previousSenderId;

    private String senderUid;

    private String currentData;

    private String previousDate;

    private int currentViewType;

    private int previousViewType;

    private MediaPlayer player;

    private long currentlyPlayingId = 0l;

    private String currentPlayingSong;

    private Runnable timerRunnable;

    private Handler seekHandler = new Handler(Looper.getMainLooper());

    private static final String TAG = "GroupMessageAdapter";

    public GroupMessageAdapter(List<BaseMessage> groupList, Context context, String groupId, String ownerId) {
        this.messageList = groupList;
        this.context = context;
        this.groupId = groupId;
        this.ownerId = ownerId;
        drawableAvatar = context.getResources().getDrawable(R.drawable.default_avatar);
        if (player == null) {
            player = new MediaPlayer();
        }

        new FontUtils(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        RecyclerView.ViewHolder groupHolder = null;

        switch (i) {
            case LEFT_TEXT_MESSAGE:
                View leftTextMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_text_layout_left, viewGroup, false);
                groupHolder = new LeftMessageViewHolder(leftTextMessage);
                break;
            case RIGHT_TEXT_MESSAGE:
                View rightTextMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_text_layout_right, viewGroup, false);
                groupHolder = new RightMessageViewHolder(rightTextMessage);
                break;

            case LEFT_IMAGE_MESSAGE:
                View leftImageMessageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_image_video_layout_left, viewGroup, false);
                groupHolder = new LeftImageVideoViewHolder(context, leftImageMessageView);
                break;
            case RIGHT_IMAGE_MESSAGE:
                View rightImageMessageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_image_video_layout_right, viewGroup, false);
                groupHolder = new RightImageVideoViewHolder(context, rightImageMessageView);
                break;

            case ACTION_MESSAGE:
                View actionMessageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_message_list_header, viewGroup, false);
                groupHolder = new DateItemHolder(actionMessageView);
                break;


            case CALL_MESSAGE:
                View callMessage=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_message_list_header,viewGroup,false);
                groupHolder=new DateItemHolder(callMessage);
                break;


            case RIGHT_VIDEO_MESSAGE:
                View rightVideoMessageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_image_video_layout_right, viewGroup, false);
                groupHolder = new RightImageVideoViewHolder(context, rightVideoMessageView);
                break;

            case LEFT_VIDEO_MESSAGE:
                View leftVideoMessageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_image_video_layout_left, viewGroup, false);
                groupHolder = new LeftImageVideoViewHolder(context, leftVideoMessageView);
                break;

            case LEFT_AUDIO_MESSAGE:
                View leftAudioMessageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_audionote_layout_left, viewGroup, false);
                groupHolder = new LeftAudioViewHolder(context, leftAudioMessageView);
                break;

            case RIGHT_AUDIO_MESSAGE:
                View rightAudioMessageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_audionote_layout_right, viewGroup, false);
                groupHolder = new RightAudioViewHolder(context, rightAudioMessageView);
                break;

            case RIGHT_FILE_MESSAGE:
                View rightFileMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.right_file_layout, viewGroup, false);
                groupHolder = new RightFileViewHolder(context, rightFileMessage);
                break;

            case LEFT_FILE_MESSAGE:
                View leftFileMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.left_file_layout, viewGroup, false);
                groupHolder = new LeftFileViewHolder(context, leftFileMessage);
                break;

            case RIGHT_TEXT_REPLY_MESSAGE:
                View rightTextReplyMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.right_reply, viewGroup, false);
                groupHolder= new RightReplyViewHolder(rightTextReplyMessage);
                break;

            case LEFT_TEXT_REPLY_MESSAGE:
                View leftTextReplyMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.left_reply, viewGroup, false);
                groupHolder= new LeftReplyViewHolder(leftTextReplyMessage);
                break;

            case LEFT_MEDIA_REPLY_MESSAGE:
                View leftMediaReplyMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.left_reply, viewGroup, false);
                groupHolder= new LeftReplyViewHolder(leftMediaReplyMessage);
                break;

            case RIGHT_MEDIA_REPLY_MESSAGE:
                View rightMediaReplyMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.right_reply, viewGroup, false);
                groupHolder= new RightReplyViewHolder(rightMediaReplyMessage);
                break;


            default:
                break;
        }
        return groupHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        baseMessage = messageList.get(i);
        String message = null;
        String mediaFile = null;
        String avatar = baseMessage.getSender().getAvatar();
        String senderName = baseMessage.getSender().getName();
        senderUid = baseMessage.getSender().getUid();
        TextMessage textMessage=null;
        MediaMessage mediaMessage=null;

        position = viewHolder.getAdapterPosition();

        currentViewType = getItemViewType(position);

        currentSenderId = baseMessage.getSender().getUid();
        currentData = new java.text.SimpleDateFormat("yyyy/MM/dd")
                .format(new java.util.Date(messageList.get(position).getSentAt() * 1000));

        if (position > 0) {
            previousViewType = getItemViewType(position - 1);
            previousSenderId = messageList.get(position - 1).getSender().getUid();
            previousDate = new java.text.SimpleDateFormat("yyyy/MM/dd")
                    .format(new java.util.Date(messageList.get(position - 1).getSentAt() * 1000));
        }

        if (baseMessage instanceof TextMessage) {
            message = ((TextMessage) baseMessage).getText();
            textMessage = ((TextMessage) baseMessage);
        }
        if (baseMessage instanceof MediaMessage) {
            mediaFile = ((MediaMessage) baseMessage).getUrl();
            mediaMessage = ((MediaMessage) baseMessage);

        }
        if (baseMessage instanceof Action) {

            message = ((Action) baseMessage).getMessage();
        }

        if (baseMessage instanceof Call) {

            message=((Call)baseMessage).getCallStatus();
        }

        String timeStampString = DateUtils.getTimeStringFromTimestamp(baseMessage.getSentAt(),
                "hh:mm a");
        final long timeStampLong = baseMessage.getSentAt();
        viewHolder.itemView.setTag(R.string.message, baseMessage);

        switch (viewHolder.getItemViewType()) {
            case LEFT_TEXT_MESSAGE:
                LeftMessageViewHolder leftMessageViewHolder = (LeftMessageViewHolder) viewHolder;
                leftMessageViewHolder.textMessage.setText(message);
                leftMessageViewHolder.messageTimeStamp.setText(timeStampString);
                leftMessageViewHolder.avatar.setVisibility(View.VISIBLE);
                leftMessageViewHolder.textMessage.setTypeface(FontUtils.openSansRegular);
                leftMessageViewHolder.senderName.setVisibility(View.VISIBLE);
                leftMessageViewHolder.senderName.setText(senderName);
                leftMessageViewHolder.senderName.setTypeface(FontUtils.robotoMedium);

                setAvatar(leftMessageViewHolder.avatar,avatar);

                break;
            case RIGHT_TEXT_MESSAGE:
                RightMessageViewHolder rightMessageViewHolder = (RightMessageViewHolder) viewHolder;
                rightMessageViewHolder.textMessage.setText(message);
                rightMessageViewHolder.messageTimeStamp.setText(timeStampString);
                rightMessageViewHolder.messageStatus.setVisibility(View.VISIBLE);
                rightMessageViewHolder.textMessage.setTypeface(FontUtils.openSansRegular);
                rightMessageViewHolder.messageStatus.setImageResource(R.drawable.ic_check_white_24dp);
                break;
            case LEFT_IMAGE_MESSAGE:
                LeftImageVideoViewHolder leftImageViewHolder = (LeftImageVideoViewHolder) viewHolder;
                leftImageViewHolder.senderName.setVisibility(View.VISIBLE);
                leftImageViewHolder.senderName.setText(senderName);
                leftImageViewHolder.senderName.setTypeface(FontUtils.robotoMedium);
                leftImageViewHolder.messageTimeStamp.setText(timeStampString);
                leftImageViewHolder.btnPlayVideo.setVisibility(View.GONE);
                leftImageViewHolder.avatar.setVisibility(View.VISIBLE);
                leftImageViewHolder.imageTitle.setVisibility(View.GONE);
                leftImageViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                RequestOptions requestOptions = new RequestOptions().centerCrop()
                        .placeholder(R.drawable.ic_broken_image_black);

                setAvatar(leftImageViewHolder.avatar,avatar);

                if (mediaFile != null) {

                    Glide.with(context).load(mediaFile).apply(requestOptions).into(leftImageViewHolder.imageMessage);
                }

                break;
            case RIGHT_IMAGE_MESSAGE:
                RightImageVideoViewHolder rightImageVideoViewHolder = (RightImageVideoViewHolder) viewHolder;
                rightImageVideoViewHolder.messageTimeStamp.setText(timeStampString);
                rightImageVideoViewHolder.btnPlayVideo.setVisibility(View.GONE);
                rightImageVideoViewHolder.imageTitle.setVisibility(View.GONE);
                rightImageVideoViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);

                if (mediaFile != null) {

                    RequestOptions RightrequestOptions = new RequestOptions().centerCrop()
                            .placeholder(R.drawable.ic_broken_image);

                    Glide.with(context).load(mediaFile).apply(RightrequestOptions).into(rightImageVideoViewHolder.imageMessage);
                }
                break;
            case ACTION_MESSAGE:
                DateItemHolder actionHolder = (DateItemHolder) viewHolder;
                actionHolder.txtMessageDate.setText(message);
                actionHolder.txtMessageDate.setTypeface(FontUtils.robotoMedium);
                actionHolder.txtMessageDate.setTextColor(context.getResources().getColor(R.color.primaryTextColor));
                break;

            case CALL_MESSAGE:
                DateItemHolder callHolder = (DateItemHolder) viewHolder;
                callHolder.txtMessageDate.setText(message);
                callHolder.txtMessageDate.setTypeface(FontUtils.robotoMedium);
                callHolder.txtMessageDate.setTextColor(context.getResources().getColor(R.color.primaryTextColor));
                break;

            case LEFT_VIDEO_MESSAGE:
                LeftImageVideoViewHolder leftVideoViewHolder = (LeftImageVideoViewHolder) viewHolder;
                leftVideoViewHolder.messageTimeStamp.setText(timeStampString);
                leftVideoViewHolder.btnPlayVideo.setVisibility(View.VISIBLE);
                leftVideoViewHolder.imageTitle.setVisibility(View.GONE);
                leftVideoViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);

                setAvatar(leftVideoViewHolder.avatar,avatar);

                RequestOptions videoOption = new RequestOptions().fitCenter()
                        .placeholder(R.drawable.ic_broken_image_black);
                Glide.with(context)
                        .load(mediaFile)
                        .apply(videoOption)
                        .into(leftVideoViewHolder.imageMessage);

                final String finalMediaFile3 = mediaFile;
                leftVideoViewHolder.btnPlayVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startIntent(finalMediaFile3);
                    }
                });
                break;

            case RIGHT_VIDEO_MESSAGE:
                RightImageVideoViewHolder rightVideoViewHolder = (RightImageVideoViewHolder) viewHolder;
                rightVideoViewHolder.messageTimeStamp.setText(timeStampString);
                rightVideoViewHolder.btnPlayVideo.setVisibility(View.VISIBLE);
                rightVideoViewHolder.imageTitle.setVisibility(View.GONE);
                rightVideoViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);

                RequestOptions requestOptions2 = new RequestOptions().fitCenter()
                        .placeholder(R.drawable.ic_broken_image);
                Glide.with(context)
                        .load(mediaFile)
                        .apply(requestOptions2)
                        .into(rightVideoViewHolder.imageMessage);

                final String finalMediaFile4 = mediaFile;
                rightVideoViewHolder.btnPlayVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startIntent(finalMediaFile4);
                    }
                });
                break;

            case RIGHT_AUDIO_MESSAGE:
                final RightAudioViewHolder rightAudioViewHolder = (RightAudioViewHolder) viewHolder;
                rightAudioViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                rightAudioViewHolder.messageTimeStamp.setText(timeStampString);
                rightAudioViewHolder.audioSeekBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                rightAudioViewHolder.audioSeekBar.getThumb().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                if (!player.isPlaying()) {
                    rightAudioViewHolder.playAudio.setImageResource(R.drawable.ic_play_arrow);
                }
                rightAudioViewHolder.audioSeekBar.setProgress(0);

                final String tempMediaFile = mediaFile;
                rightAudioViewHolder.playAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (!TextUtils.isEmpty(tempMediaFile)) {
                            try {
                                if (timeStampLong == currentlyPlayingId) {
                                    Logger.error(TAG, "onClick: currently playing");
                                    currentPlayingSong = "";
//                                        currentlyPlayingId = 0l;
//                                        setBtnColor(holder.viewType, playBtn, true);
                                    try {
                                        if (player.isPlaying()) {
                                            player.pause();
                                            Logger.error(TAG, "onClick: paused");
                                            rightAudioViewHolder.playAudio.setImageResource(R.drawable.ic_play_arrow);
                                        } else {
//                                                player.setDataSource(message);
//                                                player.prepare();
                                            player.seekTo(player.getCurrentPosition());
                                            rightAudioViewHolder.audioSeekBar.setProgress(player.getCurrentPosition());
                                            rightAudioViewHolder.audioLength.setText(DateUtils.convertTimeStampToDurationTime(player.getDuration()));
                                            rightAudioViewHolder.audioSeekBar.setMax(player.getDuration());
                                            rightAudioViewHolder.playAudio.setImageResource(R.drawable.ic_pause);
                                            timerRunnable = new Runnable() {
                                                @Override
                                                public void run() {

                                                    int pos = player.getCurrentPosition();
                                                    rightAudioViewHolder.audioSeekBar.setProgress(pos);

                                                    if (player.isPlaying() && pos < player.getDuration()) {
                                                        rightAudioViewHolder.audioLength.setText(DateUtils.convertTimeStampToDurationTime(player.getCurrentPosition()));
                                                        seekHandler.postDelayed(this, 250);
                                                    } else {
                                                        seekHandler
                                                                .removeCallbacks(timerRunnable);
                                                        timerRunnable = null;
                                                    }
                                                }

                                            };
                                            seekHandler.postDelayed(timerRunnable, 100);
                                            notifyDataSetChanged();
                                            player.start();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
//                                        int audioDuration = player.getDuration();

                                } else {
                                    rightAudioViewHolder.playAudio.setImageResource(R.drawable.ic_pause);
                                    playAudio(tempMediaFile, timeStampLong, player, rightAudioViewHolder.playAudio,
                                            rightAudioViewHolder.audioLength, rightAudioViewHolder.audioSeekBar);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });


                break;

            case LEFT_AUDIO_MESSAGE:

                final LeftAudioViewHolder leftAudioViewHolder = (LeftAudioViewHolder) viewHolder;
                leftAudioViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                leftAudioViewHolder.messageTimeStamp.setText(timeStampString);
                leftAudioViewHolder.senderName.setVisibility(View.GONE);
                leftAudioViewHolder.audioSeekBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                leftAudioViewHolder.audioSeekBar.getThumb().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                if (!player.isPlaying()) {
                    leftAudioViewHolder.playAudio.setImageResource(R.drawable.ic_play_arrow);
                }

                setAvatar(leftAudioViewHolder.avatar,avatar);

                leftAudioViewHolder.audioSeekBar.setProgress(0);
                final String finalMediaFile1 = mediaFile;
                leftAudioViewHolder.playAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (!player.isPlaying()) {

                        if (!TextUtils.isEmpty(finalMediaFile1)) {
                            try {
                                if (timeStampLong == currentlyPlayingId) {
                                    Logger.error(TAG, "onClick: currently playing");
                                    currentPlayingSong = "";
//                                        currentlyPlayingId = 0l;
//                                        setBtnColor(holder.viewType, playBtn, true);
                                    try {
                                        if (player.isPlaying()) {
                                            player.pause();
                                            Logger.error(TAG, "onClick: paused");
                                            leftAudioViewHolder.playAudio.setImageResource(R.drawable.ic_play_arrow);
                                        } else {
//                                                player.setDataSource(message);
//                                                player.prepare();
                                            player.seekTo(player.getCurrentPosition());
                                            leftAudioViewHolder.audioSeekBar.setProgress(player.getCurrentPosition());
                                            leftAudioViewHolder.audioLength.setText(DateUtils.convertTimeStampToDurationTime(player.getDuration()));
                                            leftAudioViewHolder.audioSeekBar.setMax(player.getDuration());
                                            leftAudioViewHolder.playAudio.setImageResource(R.drawable.ic_pause);
                                            timerRunnable = new Runnable() {
                                                @Override
                                                public void run() {

                                                    int pos = player.getCurrentPosition();
                                                    leftAudioViewHolder.audioSeekBar.setProgress(pos);

                                                    if (player.isPlaying() && pos < player.getDuration()) {
                                                        leftAudioViewHolder.audioLength.setText(DateUtils.convertTimeStampToDurationTime(player.getCurrentPosition()));
                                                        seekHandler.postDelayed(this, 250);
                                                    } else {
                                                        seekHandler.removeCallbacks(timerRunnable);
                                                        timerRunnable = null;
                                                    }
                                                }

                                            };
                                            seekHandler.postDelayed(timerRunnable, 100);
                                            notifyDataSetChanged();
                                            player.start();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    leftAudioViewHolder.playAudio.setImageResource(R.drawable.ic_pause);
                                    playAudio(finalMediaFile1, timeStampLong, player, leftAudioViewHolder.playAudio, leftAudioViewHolder.audioLength, leftAudioViewHolder.audioSeekBar);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });
                break;

            case RIGHT_FILE_MESSAGE:
                try {

                    RightFileViewHolder rightFileViewHolder = (RightFileViewHolder) viewHolder;
                    Logger.error("OneToOne", mediaFile);
                    Logger.error("OneToOne", mediaFile.substring(mediaFile.lastIndexOf("/")) + 1);

                    rightFileViewHolder.fileType.setTypeface(FontUtils.robotoRegular);
                    rightFileViewHolder.fileName.setTypeface(FontUtils.robotoRegular);
                    rightFileViewHolder.fileName.setText(getFileName(mediaFile));
                    rightFileViewHolder.messageTimeStamp.setText(timeStampString);
                    rightFileViewHolder.fileType.setText(getFileExtension(mediaFile));

                    final String finalMediaFile = mediaFile;
                    rightFileViewHolder.fileName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(finalMediaFile)));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case LEFT_FILE_MESSAGE:
                try {

                    LeftFileViewHolder leftFileViewHolder = (LeftFileViewHolder) viewHolder;
                    leftFileViewHolder.avatar.setVisibility(View.GONE);
                    leftFileViewHolder.senderName.setVisibility(View.GONE);
                    leftFileViewHolder.fileType.setTypeface(FontUtils.robotoRegular);
                    leftFileViewHolder.fileName.setTypeface(FontUtils.robotoRegular);
                    leftFileViewHolder.fileName.setText(getFileName(mediaFile));
                    leftFileViewHolder.messageTimeStamp.setText(timeStampString);
                    leftFileViewHolder.fileType.setText(getFileExtension(mediaFile));
                    final String finalMediaFile2 = mediaFile;

                    setAvatar(leftFileViewHolder.avatar,avatar);
                    leftFileViewHolder.fileName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(finalMediaFile2)));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case RIGHT_TEXT_REPLY_MESSAGE:
                RightReplyViewHolder rightTextReplyViewHolder = (RightReplyViewHolder) viewHolder;
                rightTextReplyViewHolder.tvTimeStamp.setText(timeStampString);
                try {
                    if (textMessage != null) {
                        if (textMessage.getMetadata().has("senderUid")) {

                            if (textMessage.getMetadata().getString("senderUid").equals(ownerId)) {
                                rightTextReplyViewHolder.tvNameReply.setText("You");
                            } else {
                                rightTextReplyViewHolder.tvNameReply.setText(textMessage.getMetadata().getString("senderName"));
                            }
                        }

                        rightTextReplyViewHolder.tvNewMessage.setVisibility(View.VISIBLE);
                        rightTextReplyViewHolder.tvNewMessage.setText(textMessage.getText());

                        if (textMessage.getMetadata().has("type")) {

                            switch (textMessage.getMetadata().getString("type")) {

                                case CometChatConstants.MESSAGE_TYPE_IMAGE:

                                case CometChatConstants.MESSAGE_TYPE_VIDEO:

                                    rightTextReplyViewHolder.ivReplyImage.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(textMessage.getMetadata().getString("url"))
                                            .into(rightTextReplyViewHolder.ivReplyImage);

                                      rightTextReplyViewHolder.tvReplyTextMessage.setVisibility(View.VISIBLE);
                                    if (textMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_IMAGE)){
                                        rightTextReplyViewHolder.tvReplyTextMessage.setText("Photo");
                                    }
                                    else {
                                        rightTextReplyViewHolder.tvReplyTextMessage.setText("Video");
                                    }
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_FILE:
                                    rightTextReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    rightTextReplyViewHolder.tvReplyTextMessage.setText("File Message");
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_TEXT:
                                    rightTextReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    rightTextReplyViewHolder.tvReplyTextMessage.setText(textMessage.getMetadata().getString("text"));
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_AUDIO:
                                    rightTextReplyViewHolder.tvReplyTextMessage.setText("Audio Message");
                                    rightTextReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    break;
                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case LEFT_TEXT_REPLY_MESSAGE:

                LeftReplyViewHolder leftTextReplyViewHolder = (LeftReplyViewHolder) viewHolder;
                leftTextReplyViewHolder.tvTimeStamp.setText(timeStampString);
                leftTextReplyViewHolder.ivContactImage.setVisibility(View.GONE);
                setAvatar(leftTextReplyViewHolder.ivContactImage,avatar);
                leftTextReplyViewHolder.tvReplyTextMessage.setVisibility(View.VISIBLE);
                leftTextReplyViewHolder.tvNameReply.setVisibility(View.VISIBLE);
                try {
                    if (textMessage != null) {
                        if (textMessage.getMetadata().has("senderUid")) {

                            if (textMessage.getMetadata().getString("senderUid").equals(ownerId)) {
                                leftTextReplyViewHolder.tvNameReply.setText("You");
                            } else {
                                leftTextReplyViewHolder.tvNameReply.setText(textMessage.getMetadata().getString("senderName"));
                            }
                        }

                        leftTextReplyViewHolder.tvNewMessage.setVisibility(View.VISIBLE);
                        leftTextReplyViewHolder.tvNewMessage.setText(textMessage.getText());

                        if (textMessage.getMetadata().has("type")) {

                            switch (textMessage.getMetadata().getString("type")) {

                                case CometChatConstants.MESSAGE_TYPE_IMAGE:

                                case CometChatConstants.MESSAGE_TYPE_VIDEO:

                                    leftTextReplyViewHolder.tvReplyTextMessage.setVisibility(View.VISIBLE);
                                    if (textMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_IMAGE)){
                                        leftTextReplyViewHolder.tvReplyTextMessage.setText("Photo");
                                    }
                                    else {
                                        leftTextReplyViewHolder.tvReplyTextMessage.setText("Video");
                                    }
                                    leftTextReplyViewHolder.ivReplyImage.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(textMessage.getMetadata().getString("url"))
                                            .into(leftTextReplyViewHolder.ivReplyImage);
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_FILE:
                                    leftTextReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    leftTextReplyViewHolder.tvReplyTextMessage.setText("File Message");
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_TEXT:
                                    leftTextReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    leftTextReplyViewHolder.tvReplyTextMessage.setText(textMessage.getMetadata().getString("text"));
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_AUDIO:
                                    leftTextReplyViewHolder.tvReplyTextMessage.setText("Audio Message");
                                    leftTextReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    break;
                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;

            case RIGHT_MEDIA_REPLY_MESSAGE:
                RightReplyViewHolder rightMediaReplyViewHolder = (RightReplyViewHolder) viewHolder;

                rightMediaReplyViewHolder.tvTimeStamp.setText(timeStampString);

                try {
                    if (mediaMessage != null) {
                        if (mediaMessage.getMetadata().has("senderUid")) {

                            if (mediaMessage.getMetadata().getString("senderUid").equals(ownerId)) {
                                rightMediaReplyViewHolder.tvNameReply.setText("You");
                            } else {
                                rightMediaReplyViewHolder.tvNameReply.setText(mediaMessage.getMetadata().getString("senderName"));
                            }
                        }

                        if (mediaMessage.getMetadata().has("type")) {

                            switch (mediaMessage.getMetadata().getString("type")) {

                                case CometChatConstants.MESSAGE_TYPE_IMAGE:

                                case CometChatConstants.MESSAGE_TYPE_VIDEO:

                                    rightMediaReplyViewHolder.ivReplyImage.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(mediaMessage.getMetadata().getString("url"))
                                            .into(rightMediaReplyViewHolder.ivReplyImage);
                                    rightMediaReplyViewHolder.tvReplyTextMessage.setVisibility(View.VISIBLE);

                                    if (mediaMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_IMAGE)){
                                        rightMediaReplyViewHolder.tvReplyTextMessage.setText("Photo");
                                    }
                                    else  if (mediaMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_VIDEO)){
                                        rightMediaReplyViewHolder.tvReplyTextMessage.setText("Video");
                                    }
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_FILE:
                                    rightMediaReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    rightMediaReplyViewHolder.tvReplyTextMessage.setText("File Message");
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_TEXT:
                                    rightMediaReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    rightMediaReplyViewHolder.tvReplyTextMessage.setText(mediaMessage.getMetadata().getString("text"));
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_AUDIO:
                                    rightMediaReplyViewHolder.tvReplyTextMessage.setText("Audio Message");
                                    rightMediaReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    break;
                            }
                        }

                        switch (mediaMessage.getType()) {

                            case CometChatConstants.MESSAGE_TYPE_IMAGE:
                                rightMediaReplyViewHolder.rlImageContainer.setVisibility(View.VISIBLE);

                                Glide.with(context).load(mediaMessage.getUrl()).into(rightMediaReplyViewHolder.ivNewImage);
                                break;

                            case CometChatConstants.MESSAGE_TYPE_VIDEO:

                                rightMediaReplyViewHolder.rlImageContainer.setVisibility(View.VISIBLE);
                                rightMediaReplyViewHolder.ivPlayVideoButton.setVisibility(View.VISIBLE);
                                rightMediaReplyViewHolder.tvReplyTextMessage.setVisibility(View.VISIBLE);
                                String finalMediaFile5 = mediaMessage.getUrl();
                                rightMediaReplyViewHolder.ivPlayVideoButton.setOnClickListener(v -> startIntent(finalMediaFile5));
                                Glide.with(context).load(mediaMessage.getUrl()).into(rightMediaReplyViewHolder.ivNewImage);

                                break;

                            case CometChatConstants.MESSAGE_TYPE_FILE:
                                rightMediaReplyViewHolder.rlFileContainer.setVisibility(View.VISIBLE);
                                rightMediaReplyViewHolder.tvFileName.setText(getFileName(mediaMessage.getUrl()));
                                rightMediaReplyViewHolder.tvFileType.setText(getFileExtension(mediaMessage.getUrl()));
                                String media_File = mediaMessage.getUrl();
                                rightMediaReplyViewHolder.tvFileName.setOnClickListener(v -> context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(media_File))));
                                break;

                            case CometChatConstants.MESSAGE_TYPE_AUDIO:
                                rightMediaReplyViewHolder.rlAudioConatiner.setVisibility(View.VISIBLE);

                                if (!player.isPlaying()) {
                                    rightMediaReplyViewHolder.ivPlayButton.setImageResource(R.drawable.ic_play_arrow);
                                }
                                rightMediaReplyViewHolder.sbAudioSeekBar.setProgress(0);
                                final String replyMedia = mediaFile;
                                rightMediaReplyViewHolder.ivPlayButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        if (!TextUtils.isEmpty(replyMedia)) {
                                            try {
                                                if (timeStampLong == currentlyPlayingId) {
                                                    Logger.error(TAG, "onClick: currently playing");
                                                    currentPlayingSong = "";
//                                        currentlyPlayingId = 0l;
//                                        setBtnColor(holder.viewType, playBtn, true);
                                                    try {
                                                        if (player.isPlaying()) {
                                                            player.pause();
                                                            Logger.error(TAG, "onClick: paused");
                                                            rightMediaReplyViewHolder.ivPlayButton.setImageResource(R.drawable.ic_play_arrow);
                                                        } else {
//                                                player.setDataSource(message);
//                                                player.prepare();
                                                            player.seekTo(player.getCurrentPosition());
                                                            rightMediaReplyViewHolder.sbAudioSeekBar.setProgress(player.getCurrentPosition());
                                                            rightMediaReplyViewHolder.tvAudioLength.setText(DateUtils.convertTimeStampToDurationTime(player.getDuration()));
                                                            rightMediaReplyViewHolder.sbAudioSeekBar.setMax(player.getDuration());
                                                            rightMediaReplyViewHolder.ivPlayButton.setImageResource(R.drawable.ic_pause);
                                                            timerRunnable = new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    int pos = player.getCurrentPosition();
                                                                    rightMediaReplyViewHolder.sbAudioSeekBar.setProgress(pos);

                                                                    if (player.isPlaying() && pos < player.getDuration()) {
                                                                        rightMediaReplyViewHolder.tvAudioLength.setText(DateUtils.convertTimeStampToDurationTime(player.getCurrentPosition()));
                                                                        seekHandler.postDelayed(this, 250);
                                                                    } else {
                                                                        seekHandler
                                                                                .removeCallbacks(timerRunnable);
                                                                        timerRunnable = null;
                                                                    }
                                                                }

                                                            };
                                                            seekHandler.postDelayed(timerRunnable, 100);
                                                            notifyDataSetChanged();
                                                            player.start();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
//                                        int audioDuration = player.getDuration();

                                                } else {
                                                    rightMediaReplyViewHolder.ivPlayButton.setImageResource(R.drawable.ic_pause);
                                                    playAudio(replyMedia, timeStampLong, player, rightMediaReplyViewHolder.ivPlayButton,
                                                            rightMediaReplyViewHolder.tvAudioLength, rightMediaReplyViewHolder.sbAudioSeekBar);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }


                                    }
                                });

                                break;

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;

            case LEFT_MEDIA_REPLY_MESSAGE:

                LeftReplyViewHolder leftMediaReplyViewHolder = (LeftReplyViewHolder) viewHolder;

                leftMediaReplyViewHolder.tvTimeStamp.setText(timeStampString);

                setAvatar(leftMediaReplyViewHolder.ivContactImage,avatar);

                try {
                    if (mediaMessage != null) {
                        if (mediaMessage.getMetadata().has("senderUid")) {

                            if (mediaMessage.getMetadata().getString("senderUid").equals(ownerId)) {
                                leftMediaReplyViewHolder.tvNameReply.setText("You");
                            } else {
                                leftMediaReplyViewHolder.tvNameReply.setText(mediaMessage.getMetadata().getString("senderUid"));
                            }
                        }
                        if (mediaMessage.getMetadata().has("type")) {

                            switch (mediaMessage.getMetadata().getString("type")) {

                                case CometChatConstants.MESSAGE_TYPE_IMAGE:

                                case CometChatConstants.MESSAGE_TYPE_VIDEO:

                                    leftMediaReplyViewHolder.ivReplyImage.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(mediaMessage.getMetadata().getString("url"))
                                            .into(leftMediaReplyViewHolder.ivReplyImage);
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_FILE:
                                    leftMediaReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    leftMediaReplyViewHolder.tvReplyTextMessage.setText("File Message");
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_TEXT:
                                    leftMediaReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    leftMediaReplyViewHolder.tvReplyTextMessage.setText(mediaMessage.getMetadata().getString("text"));
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_AUDIO:
                                    leftMediaReplyViewHolder.tvReplyTextMessage.setText("Audio Message");
                                    leftMediaReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    break;
                            }
                        }

                        switch (mediaMessage.getType()) {

                            case CometChatConstants.MESSAGE_TYPE_IMAGE:
                                leftMediaReplyViewHolder.rlImageContainer.setVisibility(View.VISIBLE);

                                Glide.with(context).load(mediaMessage.getUrl()).into(leftMediaReplyViewHolder.ivNewImage);
                                break;

                            case CometChatConstants.MESSAGE_TYPE_VIDEO:

                                leftMediaReplyViewHolder.rlImageContainer.setVisibility(View.VISIBLE);
                                leftMediaReplyViewHolder.ivPlayVideoButton.setVisibility(View.VISIBLE);
                                leftMediaReplyViewHolder.tvReplyTextMessage.setVisibility(View.VISIBLE);
                                String finalMediaFile5 = mediaMessage.getUrl();
                                leftMediaReplyViewHolder.ivPlayVideoButton.setOnClickListener(v -> startIntent(finalMediaFile5));
                                Glide.with(context).load(mediaMessage.getUrl()).into(leftMediaReplyViewHolder.ivNewImage);

                                break;

                            case CometChatConstants.MESSAGE_TYPE_FILE:
                                leftMediaReplyViewHolder.rlFileContainer.setVisibility(View.VISIBLE);
                                leftMediaReplyViewHolder.tvFileName.setText(getFileName(mediaMessage.getUrl()));
                                leftMediaReplyViewHolder.tvFileType.setText(getFileExtension(mediaMessage.getUrl()));
                                String media_File = mediaMessage.getUrl();
                                leftMediaReplyViewHolder.tvFileName.setOnClickListener(v -> context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(media_File))));
                                break;

                            case CometChatConstants.MESSAGE_TYPE_AUDIO:
                                leftMediaReplyViewHolder.rlAudioConatiner.setVisibility(View.VISIBLE);

                                if (!player.isPlaying()) {
                                    leftMediaReplyViewHolder.ivPlayButton.setImageResource(R.drawable.ic_play_arrow);
                                }
                                leftMediaReplyViewHolder.sbAudioSeekBar.setProgress(0);
                                final String replyMedia = mediaFile;
                                leftMediaReplyViewHolder.ivPlayButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {


                                        if (!TextUtils.isEmpty(replyMedia)) {
                                            try {
                                                if (timeStampLong == currentlyPlayingId) {
                                                    Logger.error(TAG, "onClick: currently playing");
                                                    currentPlayingSong = "";
//                                        currentlyPlayingId = 0l;
//                                        setBtnColor(holder.viewType, playBtn, true);
                                                    try {
                                                        if (player.isPlaying()) {
                                                            player.pause();
                                                            Logger.error(TAG, "onClick: paused");
                                                            leftMediaReplyViewHolder.ivPlayButton.setImageResource(R.drawable.ic_play_arrow);
                                                        } else {
//                                                player.setDataSource(message);
//                                                player.prepare();
                                                            player.seekTo(player.getCurrentPosition());
                                                            leftMediaReplyViewHolder.sbAudioSeekBar.setProgress(player.getCurrentPosition());
                                                            leftMediaReplyViewHolder.tvAudioLength.setText(DateUtils.convertTimeStampToDurationTime(player.getDuration()));
                                                            leftMediaReplyViewHolder.sbAudioSeekBar.setMax(player.getDuration());
                                                            leftMediaReplyViewHolder.ivPlayButton.setImageResource(R.drawable.ic_pause);
                                                            timerRunnable = new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    int pos = player.getCurrentPosition();
                                                                    leftMediaReplyViewHolder.sbAudioSeekBar.setProgress(pos);

                                                                    if (player.isPlaying() && pos < player.getDuration()) {
                                                                        leftMediaReplyViewHolder.tvAudioLength.setText(DateUtils.convertTimeStampToDurationTime(player.getCurrentPosition()));
                                                                        seekHandler.postDelayed(this, 250);
                                                                    } else {
                                                                        seekHandler
                                                                                .removeCallbacks(timerRunnable);
                                                                        timerRunnable = null;
                                                                    }
                                                                }

                                                            };
                                                            seekHandler.postDelayed(timerRunnable, 100);
                                                            notifyDataSetChanged();
                                                            player.start();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    leftMediaReplyViewHolder.ivPlayButton.setImageResource(R.drawable.ic_pause);
                                                    playAudio(replyMedia, timeStampLong, player, leftMediaReplyViewHolder.ivPlayButton,
                                                            leftMediaReplyViewHolder.tvAudioLength, leftMediaReplyViewHolder.sbAudioSeekBar);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                });

                                break;
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }


        setAvatarAndName(viewHolder, position, avatar);

    }

    private void startIntent(String url) {
        Intent videoIntent=new Intent(context,VideoViewActivity.class);
        videoIntent.putExtra(StringContract.IntentStrings.MEDIA_URL,url);
        context.startActivity(videoIntent);
    }

    private void playAudio(String message, long sentTimeStamp, final MediaPlayer player, final ImageView playButton, final TextView audioLength, final SeekBar audioSeekBar) {
        try {
            currentPlayingSong = message;
            currentlyPlayingId = sentTimeStamp;
            if (timerRunnable != null) {
                seekHandler.removeCallbacks(timerRunnable);
                timerRunnable = null;
            }

            try {
                player.reset();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(currentPlayingSong);
                player.prepare();
                player.start();

            } catch (IllegalStateException ie) {
                ie.printStackTrace();
            }

            final int duration = player.getDuration();
            audioSeekBar.setMax(duration);
            timerRunnable = new Runnable() {
                @Override
                public void run() {

                    try {
                        int pos = player.getCurrentPosition();
                        audioSeekBar.setProgress(pos);

                        if (player.isPlaying() && pos < duration) {
                            audioLength.setText(DateUtils.convertTimeStampToDurationTime(player.getCurrentPosition()));
                            seekHandler.postDelayed(this, 250);
                        } else {
                            seekHandler
                                    .removeCallbacks(timerRunnable);
                            timerRunnable = null;
                        }
                    } catch (IllegalStateException ie) {
                        ie.printStackTrace();
                    }

                }

            };
            seekHandler.postDelayed(timerRunnable, 100);
            notifyDataSetChanged();

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    currentPlayingSong = "";
                    currentlyPlayingId = 0l;
                    seekHandler
                            .removeCallbacks(timerRunnable);
                    timerRunnable = null;
                    mp.stop();
                    audioLength.setText(DateUtils.convertTimeStampToDurationTime(duration));
                    audioSeekBar.setProgress(0);
                    playButton.setImageResource(R.drawable.ic_play_arrow);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAvatar(CircleImageView circleImageView,String url){

        if (url!=null){
            Glide.with(context).load(url).into(circleImageView);
        }
        else {
           circleImageView.setCircleBackgroundColor(context.getResources().
                    getColor(R.color.secondaryDarkColor));

            circleImageView.setImageDrawable(drawableAvatar);
        }
    }


    private <P> void setAvatarAndName(P holder, int position, String avatar) {

        try {

            if (getItemViewType(position) == LEFT_IMAGE_MESSAGE || getItemViewType(position) == LEFT_TEXT_MESSAGE
                    || getItemViewType(position) == LEFT_VIDEO_MESSAGE || getItemViewType(position) == LEFT_AUDIO_MESSAGE) {
                if (currentSenderId.equals(previousSenderId) && currentData.equals(previousDate) &&
                        currentViewType != ACTION_MESSAGE && previousViewType != ACTION_MESSAGE) {
                    if (holder instanceof LeftMessageViewHolder) {
                        ((LeftMessageViewHolder) holder).senderName.setVisibility(View.GONE);
                        ((LeftMessageViewHolder) holder).avatar.setVisibility(View.INVISIBLE);
                    } else if (holder instanceof LeftImageVideoViewHolder) {
                        ((LeftImageVideoViewHolder) holder).avatar.setVisibility(View.INVISIBLE);
                        ((LeftImageVideoViewHolder) holder).senderName.setVisibility(View.GONE);
                    } else if (holder instanceof LeftAudioViewHolder) {
                        ((LeftAudioViewHolder) holder).avatar.setVisibility(View.INVISIBLE);
                        ((LeftAudioViewHolder) holder).senderName.setVisibility(View.GONE);
                    } else if (holder instanceof LeftFileViewHolder) {
                        ((LeftFileViewHolder) holder).avatar.setVisibility(View.INVISIBLE);
                        ((LeftFileViewHolder) holder).senderName.setVisibility(View.GONE);
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemViewType(int position) {

        switch (messageList.get(position).getCategory()) {

            case CometChatConstants.CATEGORY_MESSAGE:

                if (ownerId.equalsIgnoreCase(messageList.get(position).getSender().getUid())) {

                    if (messageList.get(position) instanceof TextMessage &&
                            ((TextMessage) messageList.get(position)).getMetadata() != null
                            && ((TextMessage) messageList.get(position)).getMetadata().has("reply")) {


                        return RIGHT_TEXT_REPLY_MESSAGE;

                    } else if (messageList.get(position) instanceof MediaMessage &&
                            ((MediaMessage) messageList.get(position)).getMetadata() != null
                            && ((MediaMessage) messageList.get(position)).getMetadata().has("reply")) {

                        return RIGHT_MEDIA_REPLY_MESSAGE;

                    } else {

                        switch (messageList.get(position).getType()) {
                            case CometChatConstants.MESSAGE_TYPE_TEXT:

                                return RIGHT_TEXT_MESSAGE;

                            case CometChatConstants.MESSAGE_TYPE_IMAGE:

                                return RIGHT_IMAGE_MESSAGE;

                            case CometChatConstants.MESSAGE_TYPE_VIDEO:

                                return RIGHT_VIDEO_MESSAGE;

                            case CometChatConstants.MESSAGE_TYPE_AUDIO:

                                return RIGHT_AUDIO_MESSAGE;

                            case CometChatConstants.MESSAGE_TYPE_FILE:
                                return RIGHT_FILE_MESSAGE;
                        }
                    }
                } else {

                    if (messageList.get(position) instanceof TextMessage &&
                            ((TextMessage) messageList.get(position)).getMetadata() != null
                            && ((TextMessage) messageList.get(position)).getMetadata().has("reply")) {


                        return LEFT_TEXT_REPLY_MESSAGE;

                    } else if (messageList.get(position) instanceof MediaMessage &&
                            ((MediaMessage) messageList.get(position)).getMetadata() != null
                            && ((MediaMessage) messageList.get(position)).getMetadata().has("reply")) {

                        return LEFT_MEDIA_REPLY_MESSAGE;

                    } else {

                        switch (messageList.get(position).getType()) {

                            case CometChatConstants.MESSAGE_TYPE_TEXT:

                                return LEFT_TEXT_MESSAGE;

                            case CometChatConstants.MESSAGE_TYPE_IMAGE:

                                return LEFT_IMAGE_MESSAGE;

                            case CometChatConstants.MESSAGE_TYPE_VIDEO:

                                return LEFT_VIDEO_MESSAGE;

                            case CometChatConstants.MESSAGE_TYPE_AUDIO:

                                return LEFT_AUDIO_MESSAGE;

                            case CometChatConstants.MESSAGE_TYPE_FILE:
                                return LEFT_FILE_MESSAGE;
                        }
                    }
                }
                break;
            case CometChatConstants.CATEGORY_CALL:
                return CALL_MESSAGE;

            case CometChatConstants.CATEGORY_ACTION:
                return ACTION_MESSAGE;
        }
        return 0;
    }

    @Override
    public int getItemCount() {

        if (messageList != null) {

            return messageList.size();
        } else {
            return 0;
        }
    }

    @Override
    public long getHeaderId(int var1) {

        return Long.parseLong(DateUtils.getDateId(messageList.get(var1).getSentAt() * 1000));
    }

    @Override
    public GroupMessageAdapter.DateItemHolder onCreateHeaderViewHolder(ViewGroup var1) {

        View view = LayoutInflater.from(var1.getContext()).inflate(R.layout.cc_message_list_header,
                var1, false);
        return new DateItemHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(GroupMessageAdapter.DateItemHolder var1, int var2, long var3) {

        Date date = new Date(messageList.get(var2).getSentAt() * 1000);
        String formattedDate = DateUtils.getCustomizeDate(date.getTime());
        var1.txtMessageDate.setBackground(context.getResources().getDrawable(R.drawable.cc_rounded_date_button));
        var1.txtMessageDate.setTypeface(FontUtils.robotoMedium);
        var1.txtMessageDate.setText(formattedDate);
    }

    public void add(BaseMessage baseMessage) {
        messageList.add(baseMessage);
        notifyDataSetChanged();
    }

    public void refreshData(List<BaseMessage> messageList) {
        this.messageList.addAll(0, messageList);
        notifyItemRangeInserted(0, messageList.size());
        notifyItemChanged(messageList.size());
    }

    class DateItemHolder extends RecyclerView.ViewHolder {

        TextView txtMessageDate;

        DateItemHolder(@NonNull View itemView) {
            super(itemView);

            txtMessageDate = (TextView) itemView.findViewById(R.id.txt_message_date);
        }
    }

}
