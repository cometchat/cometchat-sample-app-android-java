package com.inscripts.cometchatpulse.demo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.inscripts.cometchatpulse.demo.Activity.VideoViewActivity;
import com.inscripts.cometchatpulse.demo.AsyncTask.DownloadFile;
import com.inscripts.cometchatpulse.demo.CometApplication;
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
import com.inscripts.cometchatpulse.demo.ViewHolders.LeftReplyViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.RightAudioViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.RightFileViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.RightImageVideoViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.RightReplyViewHolder;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.Date;
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

    private final String ownerUid;

    private List<BaseMessage> messageArrayList;

    private String friendUid;

    private Context context;

    private int position;

    private MediaPlayer player;
    private long currentlyPlayingId = 0l;

    private static LongSparseArray<Integer>audioDurations;

    private DownloadFile downloadFile;

    private String currentPlayingSong;
    private Runnable timerRunnable;
    private Handler seekHandler = new Handler(Looper.getMainLooper());


    public OneToOneAdapter(Context context, List<BaseMessage> messageArrayList, String friendUid, String ownerUid) {
        this.messageArrayList = messageArrayList;
        this.friendUid = friendUid;
        this.ownerUid = ownerUid;
        this.context = context;


        audioDurations=new LongSparseArray<>();

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

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        position = holder.getAdapterPosition();


        BaseMessage baseMessage = messageArrayList.get(i);
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
                break;

            case RIGHT_TEXT_MESSAGE:

                RightMessageViewHolder rightMessageViewHolder = (RightMessageViewHolder) holder;
                rightMessageViewHolder.textMessage.setTypeface(FontUtils.openSansRegular);
                rightMessageViewHolder.textMessage.setText(message);
                rightMessageViewHolder.messageTimeStamp.setText(timeStampString);
                rightMessageViewHolder.messageStatus.setImageResource(R.drawable.ic_check_white_24dp);
                break;

            case LEFT_IMAGE_MESSAGE:
                LeftImageVideoViewHolder leftImageViewHolder = (LeftImageVideoViewHolder) holder;
                leftImageViewHolder.senderName.setVisibility(View.GONE);
                leftImageViewHolder.messageTimeStamp.setText(timeStampString);
                leftImageViewHolder.btnPlayVideo.setVisibility(View.GONE);
                leftImageViewHolder.avatar.setVisibility(View.GONE);
                leftImageViewHolder.imageTitle.setVisibility(View.GONE);
                leftImageViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {

                    RequestOptions requestOptions = new RequestOptions().centerCrop()
                            .placeholder(R.drawable.ic_broken_image_black).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

                    Glide.with(context).load(imageUrl).apply(requestOptions).into(leftImageViewHolder.imageMessage);
                    String finalImageUrl1 = imageUrl;
                    leftImageViewHolder.imageMessage.setOnClickListener(view->startIntent(finalImageUrl1,false));
                }
                break;

            case RIGHT_IMAGE_MESSAGE:
                RightImageVideoViewHolder rightImageVideoViewHolder = (RightImageVideoViewHolder) holder;
                rightImageVideoViewHolder.messageTimeStamp.setText(timeStampString);
                rightImageVideoViewHolder.btnPlayVideo.setVisibility(View.GONE);
                rightImageVideoViewHolder.imageTitle.setVisibility(View.GONE);

                rightImageVideoViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);

                if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                    String url = imageUrl.replace("/", "");
                    Logger.error("Image", url);
                    RequestOptions requestOptions = new RequestOptions().centerCrop()
                            .placeholder(R.drawable.ic_broken_image).diskCacheStrategy(DiskCacheStrategy.ALL);

                    Glide.with(context).load(imageUrl).apply(requestOptions).into(rightImageVideoViewHolder.imageMessage);
                    String finalImageUrl = imageUrl;
                    rightImageVideoViewHolder.imageMessage.setOnClickListener(view->startIntent(finalImageUrl,false));
                }
                break;


            case LEFT_VIDEO_MESSAGE:
                final LeftImageVideoViewHolder leftVideoViewHolder = (LeftImageVideoViewHolder) holder;
                leftVideoViewHolder.messageTimeStamp.setText(timeStampString);
                leftVideoViewHolder.btnPlayVideo.setVisibility(View.VISIBLE);
                leftVideoViewHolder.imageTitle.setVisibility(View.GONE);
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
                leftVideoViewHolder.btnPlayVideo.setOnClickListener(view -> startIntent(finalMediaFile3,true));

                break;

            case RIGHT_VIDEO_MESSAGE:
                final RightImageVideoViewHolder rightVideoViewHolder = (RightImageVideoViewHolder) holder;
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
                rightVideoViewHolder.btnPlayVideo.setOnClickListener(view -> startIntent(finalMediaFile4,true));
                break;

            case RIGHT_AUDIO_MESSAGE:
                final RightAudioViewHolder rightAudioViewHolder = (RightAudioViewHolder) holder;
                rightAudioViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                rightAudioViewHolder.messageTimeStamp.setText(timeStampString);
                rightAudioViewHolder.audioSeekBar.getProgressDrawable().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                rightAudioViewHolder.audioSeekBar.getThumb().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                if (!player.isPlaying()) {
                    rightAudioViewHolder.playAudio.setImageResource(R.drawable.ic_play_arrow);
                }
                rightAudioViewHolder.audioSeekBar.setProgress(0);
                String rightAudioPath=null;
                File rightAudioFile=null;
                try {
                    if (mediaMessage.getMetadata() != null) {
                        try {
                            rightAudioPath = mediaMessage.getMetadata().getString("path");
                            rightAudioFile=new File(rightAudioPath);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (rightAudioFile.exists()){
                        try {

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
                                int duration = player.getDuration();
                                audioDurations.put(timeStampLong, duration);
                                rightAudioViewHolder.audioLength.setText(DateUtils.convertTimeStampToDurationTime(duration));

                            }

                        } catch ( Exception e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


                final String tempMediaFile = mediaFile;
                final String tempPath = rightAudioPath;
                final File tempFile= rightAudioFile;
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
                                    playAudio(tempFile.exists()?tempPath:tempMediaFile, timeStampLong, player, rightAudioViewHolder.playAudio,
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

                final LeftAudioViewHolder leftAudioViewHolder = (LeftAudioViewHolder) holder;
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

                  String audioPath=FileUtils.getPath(context, CometChatConstants.MESSAGE_TYPE_AUDIO) +
                          FileUtils.getFileName(mediaFile);

                File audioFile=new File(audioPath);

                if (audioFile.exists()){
                       leftAudioViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                       leftAudioViewHolder.download.setVisibility(View.GONE);
                       leftAudioViewHolder.playAudio.setVisibility(View.VISIBLE);

                    try {

                        if (audioDurations.get(timeStampLong) == null) {
                            player.reset();
                            try {
                                player.setDataSource(audioPath);
                                player.prepare();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            int duration = player.getDuration();
                            audioDurations.put(timeStampLong, duration);
                            leftAudioViewHolder.audioLength.setText(DateUtils.convertTimeStampToDurationTime(duration));

                        } else {
                            int duration = player.getDuration();
                            audioDurations.put(timeStampLong, duration);
                            leftAudioViewHolder.audioLength.setText(DateUtils.convertTimeStampToDurationTime(duration));

                        }

                    } catch ( Exception e) {
                        e.printStackTrace();
                    }

                }else {
                    leftAudioViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                    leftAudioViewHolder.download.setVisibility(View.VISIBLE);
                    leftAudioViewHolder.playAudio.setVisibility(View.GONE);
                }

                String tempUrl=mediaFile;
                leftAudioViewHolder.download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (downloadFile!=null&&downloadFile.getStatus()== AsyncTask.Status.RUNNING){

                            downloadFile.cancel(true);
                            leftAudioViewHolder.fileLoadingProgressBar.setVisibility(View.GONE);
                            leftAudioViewHolder.playAudio.setVisibility(View.GONE);
                            leftAudioViewHolder.download.setImageResource(R.drawable.ic_file_download);
                              try {
                                 if (audioFile.exists()){
                                     audioFile.delete();
                                 }
                              }catch (Exception e){
                                  e.printStackTrace();
                              }
                        }else {
                            if (CCPermissionHelper.hasPermissions(context, CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE)){
                                    if (FileUtils.checkDirExistence(context,CometChatConstants.MESSAGE_TYPE_AUDIO)){
                                        downloadFile=new DownloadFile(context,CometChatConstants.MESSAGE_TYPE_AUDIO,tempUrl,leftAudioViewHolder);
                                        downloadFile.execute();
                                    }
                            }else {
                                CCPermissionHelper.requestPermissions((Activity)context, new String[]{CCPermissionHelper.REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE},
                                        StringContract.RequestCode.FILE_WRITE);

                            }
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
//                                        int audioDuration = player.getDuration();

                                } else {
                                    leftAudioViewHolder.playAudio.setImageResource(R.drawable.ic_pause);
                                    playAudio(audioFile.exists()?audioPath:finalMediaFile1, timeStampLong, player, leftAudioViewHolder.playAudio,
                                            leftAudioViewHolder.audioLength, leftAudioViewHolder.audioSeekBar);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

//
                    }
                });
                break;

            case RIGHT_FILE_MESSAGE:
                try {

                    RightFileViewHolder rightFileViewHolder = (RightFileViewHolder) holder;
                    Logger.error("OneToOne", mediaFile);
                    Logger.error("OneToOne", mediaFile.substring(mediaFile.lastIndexOf("/")) + 1);
                    rightFileViewHolder.fileType.setTypeface(FontUtils.robotoRegular);
                    rightFileViewHolder.fileName.setTypeface(FontUtils.robotoRegular);
//                    rightFileViewHolder.fileName.setText(getFileName(mediaFile));
                     rightFileViewHolder.fileName.setText(mediaMessage.getAttachment().getFileName());
                    rightFileViewHolder.messageTimeStamp.setText(timeStampString);
                    rightFileViewHolder.fileType.setText(mediaMessage.getAttachment().getFileExtension());

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

                    LeftFileViewHolder leftFileViewHolder = (LeftFileViewHolder) holder;
                    leftFileViewHolder.avatar.setVisibility(View.GONE);
                    leftFileViewHolder.senderName.setVisibility(View.GONE);
                    leftFileViewHolder.fileType.setTypeface(FontUtils.robotoRegular);
                    leftFileViewHolder.fileName.setTypeface(FontUtils.robotoRegular);
                    leftFileViewHolder.fileName.setText(mediaMessage.getAttachment().getFileName());
                    leftFileViewHolder.messageTimeStamp.setText(timeStampString);
                    leftFileViewHolder.fileType.setText(mediaMessage.getAttachment().getFileExtension());
                    final String finalMediaFile2 = mediaFile;
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

            case CALL_MESSAGE:
                DateItemHolder dateItemHolder = (DateItemHolder) holder;
                dateItemHolder.txtMessageDate.setText(message);
                break;


            case RIGHT_TEXT_REPLY_MESSAGE:
                RightReplyViewHolder rightTextReplyViewHolder = (RightReplyViewHolder) holder;
                rightTextReplyViewHolder.tvTimeStamp.setText(timeStampString);
                try {
                    if (textMessage != null) {
                        if (textMessage.getMetadata().has("senderUid")) {

                            if (textMessage.getMetadata().getString("senderUid").equals(ownerUid)) {
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


                                      if (textMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_IMAGE)){
                                          rightTextReplyViewHolder.tvReplyTextMessage.setText("Photo");
                                      }
                                     else  if (textMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_VIDEO)){
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

                LeftReplyViewHolder leftTextReplyViewHolder = (LeftReplyViewHolder) holder;
                leftTextReplyViewHolder.tvTimeStamp.setText(timeStampString);
                leftTextReplyViewHolder.ivContactImage.setVisibility(View.GONE);
                try {
                    if (textMessage != null) {
                        if (textMessage.getMetadata().has("senderUid")) {

                            if (textMessage.getMetadata().getString("senderUid").equals(ownerUid)) {
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

                                    leftTextReplyViewHolder.ivReplyImage.setVisibility(View.VISIBLE);
                                    Glide.with(context).load(textMessage.getMetadata().getString("url"))
                                            .into(leftTextReplyViewHolder.ivReplyImage);

                                    if (textMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_IMAGE)){
                                        leftTextReplyViewHolder.tvReplyTextMessage.setText("Photo");
                                    }
                                    else  if (textMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_VIDEO)){
                                        leftTextReplyViewHolder.tvReplyTextMessage.setText("Video");
                                    }
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
                RightReplyViewHolder rightMediaReplyViewHolder = (RightReplyViewHolder) holder;

                rightMediaReplyViewHolder.tvTimeStamp.setText(timeStampString);

                try {
                    if (mediaMessage != null) {
                        if (mediaMessage.getMetadata().has("senderUid")) {

                            if (mediaMessage.getMetadata().getString("senderUid").equals(ownerUid)) {
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
                                rightMediaReplyViewHolder.ivPlayVideoButton.setOnClickListener(v -> startIntent(finalMediaFile5,true));
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

                LeftReplyViewHolder leftMediaReplyViewHolder = (LeftReplyViewHolder) holder;

                leftMediaReplyViewHolder.tvTimeStamp.setText(timeStampString);

                leftMediaReplyViewHolder.ivContactImage.setVisibility(View.GONE);

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

                                    if (mediaMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_IMAGE)){
                                        leftMediaReplyViewHolder.tvReplyTextMessage.setText("Photo");
                                    }
                                    else  if (mediaMessage.getMetadata().getString("type").equals(CometChatConstants.MESSAGE_TYPE_VIDEO)){
                                        leftMediaReplyViewHolder.tvReplyTextMessage.setText("Video");
                                    }

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
                                leftMediaReplyViewHolder.ivPlayVideoButton.setOnClickListener(v -> startIntent(finalMediaFile5,true));
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

    }



    private void startIntent(String url,boolean isVideo) {
        Intent videoIntent = new Intent(context, VideoViewActivity.class);
        videoIntent.putExtra(StringContract.IntentStrings.MEDIA_URL, url);
        videoIntent.putExtra(StringContract.IntentStrings.ISVIDEO,isVideo);
        context.startActivity(videoIntent);
    }

    public void playAudio(String message, long sentTimeStamp, final MediaPlayer player,
                          final ImageView playButton, final TextView audioLength, final SeekBar audioSeekBar) {
        try {
            currentPlayingSong = message;
            currentlyPlayingId = sentTimeStamp;
            if (timerRunnable != null) {
                seekHandler.removeCallbacks(timerRunnable);
                timerRunnable = null;
            }

            player.reset();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(currentPlayingSong);
            player.prepare();
            player.start();


            final int duration = player.getDuration();
            audioSeekBar.setMax(duration);
            timerRunnable = new Runnable() {
                @Override
                public void run() {

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
                }

            };
            seekHandler.postDelayed(timerRunnable, 100);
            notifyDataSetChanged();

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    currentPlayingSong = "";
                    currentlyPlayingId = 0l;
//                    setBtnColor(viewtype, playBtn, true);
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
            playButton.setImageResource(R.drawable.ic_play_arrow);

        }
    }


    public void stopPlayer(){
        try {
            if (player != null) {
                player.stop();
                player.reset();
            }

        } catch ( Exception e) {
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

        if (messageArrayList.get(position).getCategory().equals(CometChatConstants.CATEGORY_MESSAGE)) {


            if (ownerUid.equalsIgnoreCase(messageArrayList.get(position).getSender().getUid())) {

                if (messageArrayList.get(position) instanceof TextMessage &&
                        ((TextMessage) messageArrayList.get(position)).getMetadata() != null
                        && ((TextMessage) messageArrayList.get(position)).getMetadata().has("reply")) {


                    return RIGHT_TEXT_REPLY_MESSAGE;

                } else if (messageArrayList.get(position) instanceof MediaMessage &&
                        ((MediaMessage) messageArrayList.get(position)).getMetadata() != null
                        && ((MediaMessage) messageArrayList.get(position)).getMetadata().has("reply")) {

                    return RIGHT_MEDIA_REPLY_MESSAGE;

                } else {

                    switch (messageArrayList.get(position).getType()) {
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

                if (messageArrayList.get(position) instanceof TextMessage &&
                        ((TextMessage) messageArrayList.get(position)).getMetadata() != null
                        && ((TextMessage) messageArrayList.get(position)).getMetadata().has("reply")) {


                    return LEFT_TEXT_REPLY_MESSAGE;

                } else if (messageArrayList.get(position) instanceof MediaMessage &&
                        ((MediaMessage) messageArrayList.get(position)).getMetadata() != null
                        && ((MediaMessage) messageArrayList.get(position)).getMetadata().has("reply")) {

                    return LEFT_MEDIA_REPLY_MESSAGE;

                } else {

                    switch (messageArrayList.get(position).getType()) {

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
        } else if (messageArrayList.get(position).getCategory().equals(CometChatConstants.CATEGORY_CALL)) {
            return CALL_MESSAGE;
        }

        return super.getItemViewType(position);

    }



    @Override
    public long getItemId(int position) {

        return messageArrayList.get(position).getId();
    }


    public void refreshData(List<BaseMessage> userArrayList) {
        messageArrayList.addAll(0, userArrayList);
        notifyItemRangeInserted(0, userArrayList.size());
        notifyItemChanged(userArrayList.size());

    }


    @Override
    public long getHeaderId(int var1) {

        return Long.parseLong(DateUtils.getDateId(messageArrayList.get(var1).getSentAt() * 1000));
    }

    @Override
    public DateItemHolder onCreateHeaderViewHolder(ViewGroup var1) {
        View view = LayoutInflater.from(var1.getContext()).inflate(R.layout.cc_message_list_header,
                var1, false);

        return new DateItemHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(DateItemHolder var1, int var2, long var3) {

        Date date = new Date(messageArrayList.get(var2).getSentAt() * 1000);
        String formattedDate = DateUtils.getCustomizeDate(date.getTime());
        var1.txtMessageDate.setBackground(context.getResources().getDrawable(R.drawable.cc_rounded_date_button));
        var1.txtMessageDate.setTypeface(FontUtils.robotoMedium);
        var1.txtMessageDate.setText(formattedDate);
    }


    public void addMessage(BaseMessage baseMessage) {
        messageArrayList.add(baseMessage);
        notifyDataSetChanged();
    }

    public class LeftMessageViewHolder extends RecyclerView.ViewHolder {

        public TextView textMessage;
        public TextView messageTimeStamp;
        public TextView senderName;
        public CircleImageView avatar;

        public LeftMessageViewHolder(View leftTextMessageView) {

            super(leftTextMessageView);
            textMessage = leftTextMessageView.findViewById(R.id.textViewMessage);
            messageTimeStamp = leftTextMessageView.findViewById(R.id.timeStamp);
            avatar = leftTextMessageView.findViewById(R.id.imgAvatar);
            senderName = leftTextMessageView.findViewById(R.id.senderName);
        }
    }


    public class RightMessageViewHolder extends RecyclerView.ViewHolder {
        public TextView textMessage;
        public TextView messageTimeStamp;
        public CircleImageView messageStatus;

        public RightMessageViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textViewMessage);
            messageStatus = itemView.findViewById(R.id.img_message_status);
            messageTimeStamp = itemView.findViewById(R.id.timestamp);
        }

    }


    public class DateItemHolder extends RecyclerView.ViewHolder {

        public TextView txtMessageDate;

        public DateItemHolder(@NonNull View itemView) {
            super(itemView);

            txtMessageDate = (TextView) itemView.findViewById(R.id.txt_message_date);

        }
    }

}
