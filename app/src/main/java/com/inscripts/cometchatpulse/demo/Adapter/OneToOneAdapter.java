package com.inscripts.cometchatpulse.demo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TextMessage;
import com.inscripts.cometchatpulse.demo.Activity.VideoViewActivity;
import com.inscripts.cometchatpulse.demo.AsyncTask.DownloadFile;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.CustomView.StickyHeaderAdapter;
import com.inscripts.cometchatpulse.demo.Helper.CCPermissionHelper;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Utils.DateUtils;
import com.inscripts.cometchatpulse.demo.Utils.FileUtils;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.inscripts.cometchatpulse.demo.Utils.FileUtils.getFileExtension;
import static com.inscripts.cometchatpulse.demo.Utils.FileUtils.getFileName;


public class OneToOneAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements StickyHeaderAdapter<OneToOneAdapter.DateItemHolder> {

    private static final String TAG = "OneToOneAdapter";

    private static final int RIGHT_TEXT_MESSAGE = 334;

    private static final int CALL_MESSAGE = 123;

    private static final int LEFT_TEXT_MESSAGE = 734;

    private static final int LEFT_IMAGE_MESSAGE = 528;

    private static final int RIGHT_IMAGE_MESSAGE = 834;

    private static final int LEFT_VIDEO_MESSAGE = 580;

    private static final int RIGHT_VIDEO_MESSAGE = 797;

    private static final int RIGHT_AUDIO_MESSAGE = 70;

    private static final int LEFT_AUDIO_MESSAGE = 79;

    private static final int LEFT_FILE_MESSAGE = 24;

    private static final int RIGHT_FILE_MESSAGE = 55;

    private static final int RIGHT_MEDIA_REPLY_MESSAGE = 345;

    private static final int RIGHT_TEXT_REPLY_MESSAGE = 346;

    private static final int LEFT_MEDIA_REPLY_MESSAGE = 756;

    private static final int LEFT_TEXT_REPLY_MESSAGE = 748;

    private static final int ACTION_MESSAGE = 432;

    private final String ownerUid;

    private LongSparseArray<BaseMessage> messageArrayList=new LongSparseArray<>();

    private Context context;

    private MediaPlayer player;
    private long currentlyPlayingId = 0l;

    private static LongSparseArray<Integer> audioDurations;

    private DownloadFile downloadFile;

    private String currentPlayingSong;

    private static int currentPlayingPosition;

    private Runnable timerRunnable;

    private Handler seekHandler = new Handler(Looper.getMainLooper());

    private RecyclerView.ViewHolder holder;


    public OneToOneAdapter(Context context,List<BaseMessage> messageArrayList, String ownerUid) {
        this.ownerUid = ownerUid;
        this.context = context;

         setList(messageArrayList);

         audioDurations = new LongSparseArray<>();

        if (null == player) {
            player = new MediaPlayer();
        }

        new FontUtils(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        switch (i) {

            case RIGHT_TEXT_MESSAGE:
                View rightTextMessageView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.cc_text_layout_right, viewGroup, false);
                return new RightMessageViewHolder(rightTextMessageView);


            case LEFT_TEXT_MESSAGE:
                View leftTextMessageView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.cc_text_layout_left, viewGroup, false);
                return new LeftMessageViewHolder(leftTextMessageView);

            case LEFT_IMAGE_MESSAGE:
                View leftImageMessageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_image_video_layout_left, viewGroup, false);
                return new LeftImageVideoViewHolder(context, leftImageMessageView);

            case RIGHT_IMAGE_MESSAGE:
                View rightImageMessageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_image_video_layout_right, viewGroup, false);
                return new RightImageVideoViewHolder(context, rightImageMessageView);

            case RIGHT_VIDEO_MESSAGE:
                View rightVideoMessageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_image_video_layout_right, viewGroup, false);
                return new RightImageVideoViewHolder(context, rightVideoMessageView);

            case LEFT_VIDEO_MESSAGE:
                View leftVideoMessageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_image_video_layout_left, viewGroup, false);
                return new LeftImageVideoViewHolder(context, leftVideoMessageView);

            case LEFT_AUDIO_MESSAGE:
                View leftAudioMessageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_audionote_layout_left, viewGroup, false);
                return new LeftAudioViewHolder(context, leftAudioMessageView);

            case RIGHT_AUDIO_MESSAGE:
                View rightAudioMessageView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_audionote_layout_right, viewGroup, false);
                return new RightAudioViewHolder(context, rightAudioMessageView);

            case RIGHT_FILE_MESSAGE:
                View rightFileMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.right_file_layout, viewGroup, false);
                return new RightFileViewHolder(context, rightFileMessage);

            case LEFT_FILE_MESSAGE:
                View leftFileMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.left_file_layout, viewGroup, false);
                return new LeftFileViewHolder(context, leftFileMessage);

            case CALL_MESSAGE:
                View callMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cc_message_list_header, viewGroup, false);
                return new DateItemHolder(callMessage);

            case RIGHT_TEXT_REPLY_MESSAGE:
                View rightTextReplyMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.right_reply, viewGroup, false);
                return new RightReplyViewHolder(rightTextReplyMessage);

            case LEFT_TEXT_REPLY_MESSAGE:
                View leftTextReplyMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.left_reply, viewGroup, false);
                return new LeftReplyViewHolder(leftTextReplyMessage);

            case LEFT_MEDIA_REPLY_MESSAGE:
                View leftMediaReplyMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.left_reply, viewGroup, false);
                return new LeftReplyViewHolder(leftMediaReplyMessage);

            case RIGHT_MEDIA_REPLY_MESSAGE:
                View rightMediaReplyMessage = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.right_reply, viewGroup, false);
                return new RightReplyViewHolder(rightMediaReplyMessage);


            default:
                return null;
        }


    }

    private void setDeliveryIcon(CircleImageView circleImageView,BaseMessage baseMessage){
        if (baseMessage.getDeliveredAt()!=0){
            circleImageView.setCircleBackgroundColor(context.getResources().getColor(R.color.secondaryColor));
            circleImageView.setImageResource(R.drawable.ic_double_tick);
        }
    }

    private void setReadIcon(CircleImageView circleImageView,BaseMessage baseMessage){
        if (baseMessage.getReadAt()!=0){

            circleImageView.setImageResource(R.drawable.ic_double_tick_blue);
            circleImageView.setCircleBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        this.holder=holder;

        BaseMessage baseMessage = messageArrayList.get(messageArrayList.keyAt(i));
        TextMessage textMessage = null;
        MediaMessage mediaMessage = null;

        String message = null;
        String mediaFile = null;
        String imageUrl = null;
        if (baseMessage instanceof TextMessage) {
            message = ((TextMessage) baseMessage).getText();
            textMessage = ((TextMessage) baseMessage);
        }
        if (baseMessage instanceof MediaMessage) {
            imageUrl = ((MediaMessage) baseMessage).getUrl();
            mediaFile = ((MediaMessage) baseMessage).getUrl();
            mediaMessage = (MediaMessage) baseMessage;

        }

        if (baseMessage instanceof Call) {

            message = ((Call) baseMessage).getCallStatus();
        }


        String timeStampString = DateUtils.getTimeStringFromTimestamp(baseMessage.getSentAt(),
                "hh:mm a");
        final long timeStampLong = baseMessage.getSentAt();


        holder.itemView.setTag(R.string.message, baseMessage);


        switch (holder.getItemViewType()) {
            case LEFT_TEXT_MESSAGE:
                LeftMessageViewHolder leftMessageViewHolder = (LeftMessageViewHolder) holder;
                leftMessageViewHolder.textMessage.setTypeface(FontUtils.openSansRegular);
                leftMessageViewHolder.textMessage.setText(message);
                leftMessageViewHolder.messageTimeStamp.setText(timeStampString);
                leftMessageViewHolder.senderName.setVisibility(View.GONE);
                leftMessageViewHolder.avatar.setVisibility(View.GONE);
                if (baseMessage.getReadAt()==0){
                    CometChat.markMessageAsRead(baseMessage);
                }
                break;

            case RIGHT_TEXT_MESSAGE:
                RightMessageViewHolder rightMessageViewHolder = (RightMessageViewHolder) holder;
                rightMessageViewHolder.textMessage.setTypeface(FontUtils.openSansRegular);
                rightMessageViewHolder.textMessage.setText(message);
                rightMessageViewHolder.messageTimeStamp.setText(timeStampString);
                setDeliveryIcon(rightMessageViewHolder.messageStatus,baseMessage);
                setReadIcon(rightMessageViewHolder.messageStatus,baseMessage);

//                  if (baseMessage.getDeletedAt()!=0){
//                      rightMessageViewHolder.messageStatus.setVisibility(View.GONE);
//                      rightMessageViewHolder.textMessage.setVisibility(View.GONE);
//                      rightMessageViewHolder.messageTimeStamp.setVisibility(View.GONE);
//                  }
                break;

            case LEFT_IMAGE_MESSAGE:
                LeftImageVideoViewHolder leftImageViewHolder = (LeftImageVideoViewHolder) holder;
                leftImageViewHolder.senderName.setVisibility(View.GONE);
                leftImageViewHolder.messageTimeStamp.setText(timeStampString);
                leftImageViewHolder.btnPlayVideo.setVisibility(View.GONE);
                leftImageViewHolder.avatar.setVisibility(View.GONE);
                leftImageViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {

                    RequestOptions requestOptions = new RequestOptions().centerCrop()
                            .placeholder(R.drawable.ic_broken_image_black).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

                    Glide.with(context).load(imageUrl).into(leftImageViewHolder.imageMessage);
                    String finalImageUrl1 = imageUrl;
                    leftImageViewHolder.imageMessage.setOnClickListener(view -> startIntent(finalImageUrl1, false));
                }
                if (baseMessage.getReadAt()==0){
                    CometChat.markMessageAsRead(baseMessage);
                }
                break;

            case RIGHT_IMAGE_MESSAGE:
                RightImageVideoViewHolder rightImageVideoViewHolder = (RightImageVideoViewHolder) holder;
                rightImageVideoViewHolder.messageTimeStamp.setText(timeStampString);
                rightImageVideoViewHolder.btnPlayVideo.setVisibility(View.GONE);
                rightImageVideoViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                setDeliveryIcon(rightImageVideoViewHolder.messageStatus,baseMessage);
                if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                    String url = imageUrl.replace("/", "");
                    Logger.error("Image", url);
                    RequestOptions requestOptions = new RequestOptions().centerCrop()
                            .placeholder(R.drawable.ic_broken_image).diskCacheStrategy(DiskCacheStrategy.ALL);

                    Glide.with(context).load(imageUrl).into(rightImageVideoViewHolder.imageMessage);
                    String finalImageUrl = imageUrl;
                    rightImageVideoViewHolder.imageMessage.setOnClickListener(view -> startIntent(finalImageUrl, false));
                }

                setReadIcon(rightImageVideoViewHolder.messageStatus,baseMessage);

//                if (baseMessage.getDeletedAt()!=0){
//                    rightImageVideoViewHolder.messageTimeStamp.setVisibility(View.GONE);
//                    rightImageVideoViewHolder.messageStatus.setVisibility(View.GONE);
//                    rightImageVideoViewHolder.imageMessage.setVisibility(View.GONE);
//                }

                break;


            case LEFT_VIDEO_MESSAGE:
                final LeftImageVideoViewHolder leftVideoViewHolder = (LeftImageVideoViewHolder) holder;
                leftVideoViewHolder.messageTimeStamp.setText(timeStampString);
                leftVideoViewHolder.btnPlayVideo.setVisibility(View.VISIBLE);
                leftVideoViewHolder.senderName.setVisibility(View.GONE);
                leftVideoViewHolder.avatar.setVisibility(View.GONE);
                leftVideoViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);

                RequestOptions requestOptions = new RequestOptions().fitCenter()
                        .placeholder(R.drawable.ic_broken_image);
                Glide.with(context)
                        .load(mediaFile)
                        .apply(requestOptions)
                        .into(leftVideoViewHolder.imageMessage);

                final String finalMediaFile3 = mediaFile;
                leftVideoViewHolder.btnPlayVideo.setOnClickListener(view -> startIntent(finalMediaFile3, true));
                if (baseMessage.getReadAt()==0){
                    CometChat.markMessageAsRead(baseMessage);
                }
                break;

            case RIGHT_VIDEO_MESSAGE:
                final RightImageVideoViewHolder rightVideoViewHolder = (RightImageVideoViewHolder) holder;
                rightVideoViewHolder.messageTimeStamp.setText(timeStampString);
                rightVideoViewHolder.btnPlayVideo.setVisibility(View.VISIBLE);
                rightVideoViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                setDeliveryIcon(rightVideoViewHolder.messageStatus,baseMessage);
                RequestOptions requestOptions2 = new RequestOptions().fitCenter()
                        .placeholder(R.drawable.ic_broken_image);
                Glide.with(context)
                        .load(mediaFile)
                        .apply(requestOptions2)
                        .into(rightVideoViewHolder.imageMessage);

                final String finalMediaFile4 = mediaFile;
                rightVideoViewHolder.btnPlayVideo.setOnClickListener(view -> startIntent(finalMediaFile4, true));

                setReadIcon(rightVideoViewHolder.messageStatus,baseMessage);

//                  if (baseMessage.getDeletedAt()!=0){
//                      rightVideoViewHolder.btnPlayVideo.setVisibility(View.GONE);
//                      rightVideoViewHolder.messageTimeStamp.setVisibility(View.GONE);
//                      rightVideoViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
//                      rightVideoViewHolder.messageStatus.setVisibility(View.GONE);
//                  }
                break;

            case RIGHT_AUDIO_MESSAGE:
                RightAudioViewHolder rightAudioViewHolder = (RightAudioViewHolder) holder;
                rightAudioViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                rightAudioViewHolder.messageTimeStamp.setText(timeStampString);
                rightAudioViewHolder.audioSeekBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                rightAudioViewHolder.audioSeekBar.getThumb().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                if (!player.isPlaying()) {
                    rightAudioViewHolder.playAudio.setImageResource(R.drawable.ic_play_arrow);
                }

//                if (baseMessage.getDeletedAt()!=0){
//                    rightAudioViewHolder.audioContainer.setVisibility(View.GONE);
//                    rightAudioViewHolder.messageTimeStamp.setVisibility(View.GONE);
//                    rightAudioViewHolder.audioSeekBar.setVisibility(View.GONE);
//                    rightAudioViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
//                    rightAudioViewHolder.messageStatus.setVisibility(View.GONE);
//                }
                setDeliveryIcon(rightAudioViewHolder.messageStatus,baseMessage);
                rightAudioViewHolder.audioSeekBar.setProgress(0);
                String rightAudioPath = null;
                File rightAudioFile = null;
                try {
                    if (mediaMessage.getMetadata() != null) {
                        try {
                            rightAudioPath = mediaMessage.getMetadata().getString("path");
                            rightAudioFile = new File(rightAudioPath);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (rightAudioFile.exists()) {
                        if (audioDurations.get(timeStampLong) == null) {
                            player.reset();
                            try {

                                player.setDataSource(rightAudioPath);
                                player.prepare();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            int duration = player.getDuration();
                            audioDurations.put(timeStampLong, duration);
                            rightAudioViewHolder.audioLength.setText(DateUtils.convertTimeStampToDurationTime(duration));

                        } else {
                            int duration = audioDurations.get(timeStampLong);
                            rightAudioViewHolder.audioLength.setText(DateUtils.convertTimeStampToDurationTime(duration));

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    player.reset();
                }


                final String tempMediaFile = mediaFile;
                final String tempPath = rightAudioPath;
                final File tempFile = rightAudioFile;
                rightAudioViewHolder.playAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (!TextUtils.isEmpty(tempPath)) {
                            try {
                                if (timeStampLong == currentlyPlayingId) {
                                    Logger.error(TAG, "onClick: currently playing");
                                    currentPlayingSong = "";
                                    try {
                                        if (player.isPlaying()) {
                                            player.pause();
                                            Logger.error(TAG, "onClick: paused");
                                            rightAudioViewHolder.playAudio.setImageResource(R.drawable.ic_play_arrow);
                                        } else {

                                            player.seekTo(player.getCurrentPosition());
                                            rightAudioViewHolder.audioSeekBar.setProgress(player.getCurrentPosition());
                                            rightAudioViewHolder.audioLength.setText(DateUtils.convertTimeStampToDurationTime(player.getDuration()));
                                            rightAudioViewHolder.audioSeekBar.setMax(player.getDuration());
                                            Log.d(TAG, "onClick: rightAudioViewHolder " + i);
                                            rightAudioViewHolder.playAudio.setImageResource(R.drawable.ic_pause);
                                            timerRunnable = new Runnable() {
                                                @Override
                                                public void run() {

                                                    int pos = player.getCurrentPosition();
                                                    rightAudioViewHolder.audioSeekBar.setProgress(pos);

                                                    if (player.isPlaying() && pos < player.getDuration()) {
                                                        rightAudioViewHolder.audioLength.setText(DateUtils.convertTimeStampToDurationTime(player.getCurrentPosition()));
                                                        seekHandler.postDelayed(this, 100);
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
                                    rightAudioViewHolder.playAudio.setImageResource(R.drawable.ic_pause);

                                    playAudio(tempFile.exists() ? tempPath : tempMediaFile, timeStampLong, player, rightAudioViewHolder.playAudio,
                                            rightAudioViewHolder.audioLength, rightAudioViewHolder.audioSeekBar, i);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });


                break;

            case LEFT_AUDIO_MESSAGE:

                LeftAudioViewHolder leftAudioViewHolder = (LeftAudioViewHolder) holder;
                leftAudioViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                leftAudioViewHolder.messageTimeStamp.setText(timeStampString);
                leftAudioViewHolder.senderName.setVisibility(View.GONE);
                leftAudioViewHolder.avatar.setVisibility(View.GONE);
                leftAudioViewHolder.audioSeekBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                leftAudioViewHolder.audioSeekBar.getThumb().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);

                if (!player.isPlaying()) {
                    leftAudioViewHolder.playAudio.setImageResource(R.drawable.ic_play_arrow);
                }
                leftAudioViewHolder.audioSeekBar.setProgress(0);
                final String finalMediaFile1 = mediaFile;

                if (baseMessage.getReadAt()==0){
                    CometChat.markMessageAsRead(baseMessage);
                }

                String audioPath = FileUtils.getPath(context, CometChatConstants.MESSAGE_TYPE_AUDIO)+
                        FileUtils.getFileName(mediaFile);

                File audioFile = new File(audioPath);
                audioFile.setReadable(true,false);

                if (audioFile.exists()) {
                    leftAudioViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                    leftAudioViewHolder.download.setVisibility(View.GONE);
                    leftAudioViewHolder.playAudio.setVisibility(View.VISIBLE);
                    try {

                        if (audioDurations.get(timeStampLong) == null) {
                            player.reset();

                            player.setDataSource(audioPath);
                            player.prepare();


                            int duration = player.getDuration();
                            audioDurations.put(timeStampLong, duration);
                            leftAudioViewHolder.audioLength.setText(DateUtils.convertTimeStampToDurationTime(duration));

                        } else {
                            int duration = audioDurations.get(timeStampLong);
                            leftAudioViewHolder.audioLength.setText(DateUtils.convertTimeStampToDurationTime(duration));

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        player.reset();

                    }

                } else {
                    leftAudioViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                    leftAudioViewHolder.download.setVisibility(View.VISIBLE);
                    leftAudioViewHolder.playAudio.setVisibility(View.GONE);
                }


                String tempUrl = mediaFile;
                leftAudioViewHolder.download.setOnClickListener(v -> {

                    if (downloadFile != null && downloadFile.getStatus() == AsyncTask.Status.RUNNING) {

                        downloadFile.cancel(true);
                        leftAudioViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                        leftAudioViewHolder.playAudio.setVisibility(View.GONE);
                        leftAudioViewHolder.download.setImageResource(R.drawable.ic_file_download);
                        try {
                            if (audioFile.exists()) {
                                audioFile.delete();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (CCPermissionHelper.hasPermissions(context, CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE)) {
                            if (FileUtils.checkDirExistence(context, CometChatConstants.MESSAGE_TYPE_AUDIO)) {
                                downloadFile = new DownloadFile(context, CometChatConstants.MESSAGE_TYPE_AUDIO, tempUrl, leftAudioViewHolder);
                                downloadFile.execute();
                            } else {
                                FileUtils.makeDirectory(context, CometChatConstants.MESSAGE_TYPE_AUDIO);
                            }
                        } else {
                            CCPermissionHelper.requestPermissions((Activity) context, new String[]{CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE},
                                    StringContract.RequestCode.FILE_WRITE);

                        }
                    }
                });

                leftAudioViewHolder.playAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


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

                                    leftAudioViewHolder.playAudio.setImageResource(R.drawable.ic_pause);
                                    playAudio(audioFile.exists() ? audioPath : finalMediaFile1, timeStampLong, player, leftAudioViewHolder.playAudio,
                                            leftAudioViewHolder.audioLength, leftAudioViewHolder.audioSeekBar, i);
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
                    RightFileViewHolder rightFileViewHolder = (RightFileViewHolder) holder;
                    Logger.error("OneToOne", mediaFile);
                    assert mediaFile != null;
                    Logger.error("OneToOne", mediaFile.substring(mediaFile.lastIndexOf("/")) + 1);
                    rightFileViewHolder.fileType.setTypeface(FontUtils.robotoRegular);
                    rightFileViewHolder.fileName.setTypeface(FontUtils.robotoRegular);
                    assert mediaMessage != null;
                    rightFileViewHolder.fileName.setText(mediaMessage.getAttachment().getFileName());
                    rightFileViewHolder.messageTimeStamp.setText(timeStampString);
                    rightFileViewHolder.fileType.setText(mediaMessage.getAttachment().getFileExtension());
                    final String finalMediaFile = mediaFile;
                    setDeliveryIcon(rightFileViewHolder.messageStatus,baseMessage);
                    setReadIcon(rightFileViewHolder.messageStatus,baseMessage);
                    rightFileViewHolder.fileName.setOnClickListener(view -> context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(finalMediaFile))));

//                      if (baseMessage.getDeletedAt()!=0){
//                          rightFileViewHolder.fileContainer.setVisibility(View.GONE);
//                          rightFileViewHolder.fileName.setVisibility(View.GONE);
//                          rightFileViewHolder.messageTimeStamp.setVisibility(View.GONE);
//                          rightFileViewHolder.fileType.setVisibility(View.GONE);
//                      }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case LEFT_FILE_MESSAGE:
                try {

                    LeftFileViewHolder leftFileViewHolder = (LeftFileViewHolder) holder;
                    leftFileViewHolder.avatar.setVisibility(View.GONE);
                    leftFileViewHolder.senderName.setVisibility(View.GONE);
                    leftFileViewHolder.fileType.setTypeface(FontUtils.robotoRegular);
                    leftFileViewHolder.fileName.setTypeface(FontUtils.robotoRegular);
                    leftFileViewHolder.fileName.setText(mediaMessage.getAttachment().getFileName());
                    leftFileViewHolder.messageTimeStamp.setText(timeStampString);
                    leftFileViewHolder.fileType.setText(mediaMessage.getAttachment().getFileExtension());
                    final String finalMediaFile2 = mediaFile;

                    if (baseMessage.getReadAt()==0){
                        CometChat.markMessageAsRead(baseMessage);
                    }
                    leftFileViewHolder.fileName.setOnClickListener(view -> context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(finalMediaFile2))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case CALL_MESSAGE:
                DateItemHolder dateItemHolder = (DateItemHolder) holder;
                dateItemHolder.txtMessageDate.setText(message);
                break;


            case RIGHT_TEXT_REPLY_MESSAGE:
                RightReplyViewHolder rightTextReplyViewHolder = (RightReplyViewHolder) holder;
                rightTextReplyViewHolder.tvTimeStamp.setText(timeStampString);
                try {

//                      if (baseMessage.getDeletedAt()!=0){
//                          rightTextReplyViewHolder.rlMain.setVisibility(View.GONE);
//                          rightTextReplyViewHolder.tvTimeStamp.setVisibility(View.GONE);
//                      }
                    if (textMessage != null) {
                        if (textMessage.getMetadata().has("senderUid")) {

                            if (textMessage.getMetadata().getString("senderUid").equals(ownerUid)) {
                                rightTextReplyViewHolder.tvNameReply.setText(context.getString(R.string.you));
                            } else {
                                rightTextReplyViewHolder.tvNameReply.setText(textMessage.getMetadata().getString("senderName"));
                            }
                        }


                        rightTextReplyViewHolder.tvNewMessage.setVisibility(View.VISIBLE);
                        rightTextReplyViewHolder.tvNewMessage.setText(textMessage.getText());
                        setDeliveryIcon(rightTextReplyViewHolder.ivMessageStatus,baseMessage);
                        setReadIcon(rightTextReplyViewHolder.ivMessageStatus,baseMessage);
                        if (textMessage.getMetadata().has("type")) {

                            switch (textMessage.getMetadata().getString("type")) {

                                case CometChatConstants.MESSAGE_TYPE_IMAGE:

                                case CometChatConstants.MESSAGE_TYPE_VIDEO:

                                    rightTextReplyViewHolder.ivReplyImage.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(textMessage.getMetadata().getString("url"))
                                            .into(rightTextReplyViewHolder.ivReplyImage);

                                    if (textMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                                        rightTextReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.photo));
                                    } else if (textMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
                                        rightTextReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.video));
                                    }
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_FILE:
                                    rightTextReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    rightTextReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.file_message));
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_TEXT:
                                    rightTextReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    rightTextReplyViewHolder.tvReplyTextMessage.setText(textMessage.getMetadata().getString("text"));
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_AUDIO:
                                    rightTextReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.audio_message));
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

                LeftReplyViewHolder leftTextReplyViewHolder = (LeftReplyViewHolder) holder;
                leftTextReplyViewHolder.tvTimeStamp.setText(timeStampString);
                leftTextReplyViewHolder.ivContactImage.setVisibility(View.GONE);
                try {
                    if (textMessage != null) {
                        if (textMessage.getMetadata().has("senderUid")) {

                            if (textMessage.getMetadata().getString("senderUid").equals(ownerUid)) {
                                leftTextReplyViewHolder.tvNameReply.setText(context.getString(R.string.you));
                            } else {
                                leftTextReplyViewHolder.tvNameReply.setText(textMessage.getMetadata().getString("senderName"));
                            }
                        }

                        if (baseMessage.getReadAt()==0){
                            CometChat.markMessageAsRead(baseMessage);
                        }
                        leftTextReplyViewHolder.tvNewMessage.setVisibility(View.VISIBLE);
                        leftTextReplyViewHolder.tvNewMessage.setText(textMessage.getText());

                        if (textMessage.getMetadata().has("type")) {

                            switch (textMessage.getMetadata().getString("type")) {

                                case CometChatConstants.MESSAGE_TYPE_IMAGE:

                                case CometChatConstants.MESSAGE_TYPE_VIDEO:

                                    leftTextReplyViewHolder.ivReplyImage.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(textMessage.getMetadata().getString("url"))
                                            .into(leftTextReplyViewHolder.ivReplyImage);

                                    if (textMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                                        leftTextReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.photo));
                                    } else if (textMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
                                        leftTextReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.video));
                                    }
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_FILE:
                                    leftTextReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    leftTextReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.file_message));
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_TEXT:
                                    leftTextReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    leftTextReplyViewHolder.tvReplyTextMessage.setText(textMessage.getMetadata().getString("text"));
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_AUDIO:
                                    leftTextReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.audio_message));
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
                RightReplyViewHolder rightMediaReplyViewHolder = (RightReplyViewHolder) holder;

                rightMediaReplyViewHolder.tvTimeStamp.setText(timeStampString);

                try {
//                    if (baseMessage.getDeletedAt()!=0){
//                        rightMediaReplyViewHolder.rlMain.setVisibility(View.GONE);
//                        rightMediaReplyViewHolder.tvTimeStamp.setVisibility(View.GONE);
//                    }
                    if (mediaMessage != null) {
                        if (mediaMessage.getMetadata().has("senderUid")) {

                            if (mediaMessage.getMetadata().getString("senderUid").equals(ownerUid)) {
                                rightMediaReplyViewHolder.tvNameReply.setText(context.getString(R.string.you));
                            } else {
                                rightMediaReplyViewHolder.tvNameReply.setText(mediaMessage.getMetadata().getString("senderName"));
                            }
                        }
                        setDeliveryIcon(rightMediaReplyViewHolder.ivMessageStatus,baseMessage);
                        setReadIcon(rightMediaReplyViewHolder.ivMessageStatus,baseMessage);
                        if (mediaMessage.getMetadata().has("type")) {

                            switch (mediaMessage.getMetadata().getString("type")) {

                                case CometChatConstants.MESSAGE_TYPE_IMAGE:

                                case CometChatConstants.MESSAGE_TYPE_VIDEO:

                                    rightMediaReplyViewHolder.ivReplyImage.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(mediaMessage.getMetadata().getString("url"))
                                            .into(rightMediaReplyViewHolder.ivReplyImage);

                                    if (mediaMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                                        rightMediaReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.photo));
                                    } else if (mediaMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
                                        rightMediaReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.video));
                                    }
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_FILE:
                                    rightMediaReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    rightMediaReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.file_message));
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_TEXT:
                                    rightMediaReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    rightMediaReplyViewHolder.tvReplyTextMessage.setText(mediaMessage.getMetadata().getString("text"));
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_AUDIO:
                                    rightMediaReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.audio_message));
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
                                rightMediaReplyViewHolder.ivPlayVideoButton.setOnClickListener(v -> startIntent(finalMediaFile5, true));
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
                                                            rightMediaReplyViewHolder.tvAudioLength, rightMediaReplyViewHolder.sbAudioSeekBar, i);
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

                LeftReplyViewHolder leftMediaReplyViewHolder = (LeftReplyViewHolder) holder;

                leftMediaReplyViewHolder.tvTimeStamp.setText(timeStampString);

                leftMediaReplyViewHolder.ivContactImage.setVisibility(View.GONE);


                if (baseMessage.getReadAt()==0){
                    CometChat.markMessageAsRead(baseMessage);
                }

                try {
                    if (mediaMessage != null) {
                        if (mediaMessage.getMetadata().has("senderUid")) {

                            if (mediaMessage.getMetadata().getString("senderUid").equals(ownerUid)) {
                                leftMediaReplyViewHolder.tvNameReply.setText("You");
                            } else {
                                leftMediaReplyViewHolder.tvNameReply.setText(mediaMessage.getMetadata().getString("senderName"));
                            }

                        }
                        if (mediaMessage.getMetadata().has("type")) {

                            switch (mediaMessage.getMetadata().getString("type")) {

                                case CometChatConstants.MESSAGE_TYPE_IMAGE:

                                case CometChatConstants.MESSAGE_TYPE_VIDEO:

                                    leftMediaReplyViewHolder.ivReplyImage.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(mediaMessage.getMetadata().getString("url"))
                                            .into(leftMediaReplyViewHolder.ivReplyImage);

                                    if (mediaMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                                        leftMediaReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.photo));
                                    } else if (mediaMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
                                        leftMediaReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.video));
                                    }

                                    break;

                                case CometChatConstants.MESSAGE_TYPE_FILE:
                                    leftMediaReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    leftMediaReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.file_message));
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_TEXT:
                                    leftMediaReplyViewHolder.ivReplyImage.setVisibility(View.GONE);
                                    leftMediaReplyViewHolder.tvReplyTextMessage.setText(mediaMessage.getMetadata().getString("text"));
                                    break;

                                case CometChatConstants.MESSAGE_TYPE_AUDIO:
                                    leftMediaReplyViewHolder.tvReplyTextMessage.setText(context.getString(R.string.audio_message));
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
                                leftMediaReplyViewHolder.ivPlayVideoButton.setOnClickListener(v -> startIntent(finalMediaFile5, true));
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
                                                    try {
                                                        if (player.isPlaying()) {
                                                            player.pause();
                                                            Logger.error(TAG, "onClick: paused");
                                                            leftMediaReplyViewHolder.ivPlayButton.setImageResource(R.drawable.ic_play_arrow);
                                                        } else {
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
                                                                        seekHandler.postDelayed(this, 100);
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
                                                            leftMediaReplyViewHolder.tvAudioLength, leftMediaReplyViewHolder.sbAudioSeekBar, i);
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

    }


    private void setList(List<BaseMessage> messageList){
        for (BaseMessage basemessage:messageList) {
            messageArrayList.put(basemessage.getId(),basemessage);
        }
    }

    private void startIntent(String url, boolean isVideo) {
        Intent videoIntent = new Intent(context, VideoViewActivity.class);
        videoIntent.putExtra(StringContract.IntentStrings.MEDIA_URL, url);
        videoIntent.putExtra(StringContract.IntentStrings.ISVIDEO, isVideo);
        context.startActivity(videoIntent);
    }

    private void playAudio(String message, long sentTimeStamp, MediaPlayer player, ImageView playButton,
                           TextView audioLength, SeekBar audioSeekBar, int i) {

        try {

            currentlyPlayingId = sentTimeStamp;

            if (timerRunnable != null) {
                seekHandler.removeCallbacks(timerRunnable);
                timerRunnable = null;
            }

            player.reset();
            currentPlayingSong = message;
            if (CCPermissionHelper.hasPermissions(context, CCPermissionHelper.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)) {
                player.setDataSource(message);
                player.prepare();
                player.start();
            } else {
                CCPermissionHelper.requestPermissions((Activity) context, new String[]{CCPermissionHelper.REQUEST_PERMISSION_READ_EXTERNAL_STORAGE},
                        StringContract.RequestCode.READ_STORAGE);
            }

            final int duration = player.getDuration();
            audioSeekBar.setMax(duration);
            timerRunnable = new Runnable() {
                @Override
                public void run() {

                    int pos = player.getCurrentPosition();
                    audioSeekBar.setProgress(pos);

                    if (player.isPlaying() && pos < duration) {
                        audioLength.setText(DateUtils.convertTimeStampToDurationTime(player.getCurrentPosition()));
                        Log.d(TAG, "run: ");
                        seekHandler.postDelayed(this, 100);
                    } else {
                        seekHandler
                                .removeCallbacks(timerRunnable);
                        timerRunnable = null;
                    }
                }

            };
            seekHandler.postDelayed(timerRunnable, 100);
            notifyDataSetChanged();
            player.setOnCompletionListener(mp -> {
                currentPlayingSong = "";
                currentlyPlayingId = 0l;
                seekHandler
                        .removeCallbacks(timerRunnable);
                timerRunnable = null;
                mp.stop();
                audioLength.setText(DateUtils.convertTimeStampToDurationTime(duration));
                audioSeekBar.setProgress(0);
                playButton.setImageResource(R.drawable.ic_play_arrow);
            });

        } catch (Exception e) {

        }
    }


    public void stopPlayer() {
        try {
            if (player != null) {
                player.stop();
                player.reset();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        if (messageArrayList != null) {
            return messageArrayList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (messageArrayList.get(messageArrayList.keyAt(position)).getCategory().equals(CometChatConstants.CATEGORY_MESSAGE)) {


            if (ownerUid.equalsIgnoreCase(messageArrayList.get(messageArrayList.keyAt(position)).getSender().getUid())) {

                if (messageArrayList.get(messageArrayList.keyAt(position)) instanceof TextMessage &&
                        messageArrayList.get(messageArrayList.keyAt(position)).getMetadata() != null
                        && messageArrayList.get(messageArrayList.keyAt(position)).getMetadata().has("reply")) {

                    return RIGHT_TEXT_REPLY_MESSAGE;

                } else if (messageArrayList.get(messageArrayList.keyAt(position)) instanceof MediaMessage &&
                        messageArrayList.get(messageArrayList.keyAt(position)).getMetadata() != null
                        && messageArrayList.get(messageArrayList.keyAt(position)).getMetadata().has("reply")) {

                    return RIGHT_MEDIA_REPLY_MESSAGE;

                } else {

                    switch (messageArrayList.get(messageArrayList.keyAt(position)).getType()) {
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

                if (messageArrayList.get(messageArrayList.keyAt(position)) instanceof TextMessage &&
                        messageArrayList.get(messageArrayList.keyAt(position)).getMetadata() != null
                        && messageArrayList.get(messageArrayList.keyAt(position)).getMetadata().has("reply")) {


                    return LEFT_TEXT_REPLY_MESSAGE;

                } else if (messageArrayList.get(messageArrayList.keyAt(position)) instanceof MediaMessage &&
                        messageArrayList.get(messageArrayList.keyAt(position)).getMetadata() != null
                        && messageArrayList.get(messageArrayList.keyAt(position)).getMetadata().has("reply")) {

                    return LEFT_MEDIA_REPLY_MESSAGE;

                } else {

                    switch (messageArrayList.get(messageArrayList.keyAt(position)).getType()) {

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
        } else if (messageArrayList.get(messageArrayList.keyAt(position)).getCategory().equals(CometChatConstants.CATEGORY_CALL)) {
            return CALL_MESSAGE;
        }
        return super.getItemViewType(position);

    }


    @Override
    public long getItemId(int position) {
        return messageArrayList.get(messageArrayList.keyAt(position)).getId();
    }


    public void refreshData(List<BaseMessage> userArrayList) {
        setList(userArrayList);
        notifyItemRangeInserted(0, userArrayList.size());
        notifyItemChanged(userArrayList.size());

    }


    @Override
    public long getHeaderId(int var1) {

        return Long.parseLong(DateUtils.getDateId(messageArrayList.get(messageArrayList.keyAt(var1)).getSentAt() * 1000));
    }

    @Override
    public DateItemHolder onCreateHeaderViewHolder(ViewGroup var1) {
        View view = LayoutInflater.from(var1.getContext()).inflate(R.layout.cc_message_list_header,
                var1, false);

        return new DateItemHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(DateItemHolder var1, int var2, long var3) {

        Date date = new Date(messageArrayList.get(messageArrayList.keyAt(var2)).getSentAt() * 1000);
        String formattedDate = DateUtils.getCustomizeDate(date.getTime());
        var1.txtMessageDate.setBackground(context.getResources().getDrawable(R.drawable.cc_rounded_date_button));
        var1.txtMessageDate.setTypeface(FontUtils.robotoMedium);
        var1.txtMessageDate.setText(formattedDate);
    }

    public void addMessage(BaseMessage baseMessage) {
        messageArrayList.put(baseMessage.getId(),baseMessage);
        notifyDataSetChanged();
    }

    public void Delivered(MessageReceipt messageReceipt) {
        BaseMessage baseMessage=messageArrayList.get(messageReceipt.getMessageId());
        if (baseMessage!=null) {
            baseMessage.setDeliveredAt(messageReceipt.getTimestamp());
            messageArrayList.put(baseMessage.getId(), baseMessage);
            notifyDataSetChanged();
        }
    }

    public void setRead(MessageReceipt messageReceipt) {
        BaseMessage baseMessage=messageArrayList.get(messageReceipt.getMessageId());
        if (baseMessage!=null) {
            baseMessage.setReadAt(messageReceipt.getTimestamp());
            messageArrayList.put(baseMessage.getId(), baseMessage);
            notifyDataSetChanged();
        }
    }

    public void updateMessage(BaseMessage baseMessage) {
        Log.d(TAG, "updateMessage: "+baseMessage.toString());
         messageArrayList.put(baseMessage.getId(),baseMessage);
         notifyDataSetChanged();

    }

    public void deleteMessage(BaseMessage baseMessage) {
        messageArrayList.remove(baseMessage.getId());
        notifyDataSetChanged();
    }

    public void setEditMessage(BaseMessage baseMessage) {
        messageArrayList.put(baseMessage.getId(),baseMessage);
        notifyDataSetChanged();
    }

    class DateItemHolder extends RecyclerView.ViewHolder {

        TextView txtMessageDate;

        DateItemHolder(@NonNull View itemView) {
            super(itemView);

            txtMessageDate = itemView.findViewById(R.id.txt_message_date);

        }
    }

}
