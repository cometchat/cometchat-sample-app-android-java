package com.inscripts.cometchatpulse.demo.CustomView;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Activity.OneToOneChatActivity;
import com.inscripts.cometchatpulse.demo.Helper.CCPermissionHelper;
import com.inscripts.cometchatpulse.demo.Helper.RecordListener;
import com.inscripts.cometchatpulse.demo.Utils.AnimUtil;
import com.inscripts.cometchatpulse.demo.Utils.CommonUtils;

import java.io.IOException;

public class RecordAudio extends RelativeLayout {

    public static final int DEFAULT_CANCEL_OFFSET = 8;

    private ImageView recordMic;

    private Chronometer counterTime;

    private TextView slideToCancel;

    private ImageView arrow;

    private float initialX, difX = 0;

    private float cancelOffset = DEFAULT_CANCEL_OFFSET;

    private long startTime, elapsedTime = 0;

    private Context context;

    private RecordListener recordListener;

    private boolean isSwiped, isLessThanSecondAllowed = false;

    private boolean isSoundEnabled = true;

    private int RECORD_START = R.raw.record_start;

    private int RECORD_FINISHED = R.raw.record_finished;

    private int RECORD_ERROR = R.raw.record_error;

    private MediaPlayer player;

    private AnimUtil animUtil;

    private FrameLayout slideToCancelLayout;


    public RecordAudio(Context context) {
        super(context);
        this.context = context;
        initViewComponent(context, null, -1, -1);
    }


    public RecordAudio(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViewComponent(context, attrs, -1, -1);
    }

    public RecordAudio(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViewComponent(context, attrs, defStyleAttr, -1);
    }

    private void initViewComponent(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        View view = View.inflate(context, R.layout.record_audio, null);
        addView(view);

        ViewGroup viewGroup = (ViewGroup) view.getParent();
        viewGroup.setClipChildren(false);

        arrow = view.findViewById(R.id.left_arrow);
        slideToCancel = view.findViewById(R.id.slide_to_cancel_text);
        counterTime = view.findViewById(R.id.record_time);
        recordMic=view.findViewById(R.id.record_mic);
        slideToCancelLayout=view.findViewById(R.id.slide_to_cancel_Layout);

        hideViews(true);


        if (attrs != null && defStyleAttr == -1 && defStyleRes == -1) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordAudio,
                    defStyleAttr, defStyleRes);


            int slideArrowResource = typedArray.getResourceId(R.styleable.RecordAudio_slide_to_cancel_arrow, -1);
            String slideToCancelText = typedArray.getString(R.styleable.RecordAudio_slide_to_cancel_text);
            int slideMarginRight = (int) typedArray.getDimension(R.styleable.RecordAudio_slide_to_cancel_margin_right, 30);
            int counterTimeColor = typedArray.getColor(R.styleable.RecordAudio_counter_time_color, -1);
            int arrowColor = typedArray.getColor(R.styleable.RecordAudio_slide_to_cancel_arrow_color, -1);


            int cancelBounds = typedArray.getDimensionPixelSize(R.styleable.RecordAudio_slide_to_cancel_bounds, -1);

            if (cancelBounds != -1)
                setCancelOffset(cancelBounds, false);


            if (slideArrowResource != -1) {
                Drawable slideArrow = AppCompatResources.getDrawable(getContext(), slideArrowResource);
                arrow.setImageDrawable(slideArrow);
            }

            if (slideToCancelText != null)
                slideToCancel.setText(slideToCancelText);

            if (counterTimeColor != -1)
                setCounterTimeColor(counterTimeColor);


            if (arrowColor != -1)
                setSlideToCancelArrowColor(arrowColor);



            setMarginRight(slideMarginRight, true);

            typedArray.recycle();
        }

    }


    private void hideViews(boolean hideSmallMic) {
        slideToCancelLayout.setVisibility(GONE);
        counterTime.setVisibility(GONE);
        if (hideSmallMic)
            recordMic.setVisibility(GONE);
    }

    private void showViews() {
        slideToCancelLayout.setVisibility(VISIBLE);
        recordMic.setVisibility(VISIBLE);
        counterTime.setVisibility(VISIBLE);
    }


    private boolean isLessThanOneSecond(long time) {
        return time <= 1000;
    }


    private void playSound(int soundRes) {

        if (isSoundEnabled) {
            if (soundRes == 0)
                return;

            try {
                player = new MediaPlayer();
                AssetFileDescriptor afd = context.getResources().openRawResourceFd(soundRes);
                if (afd == null) return;
                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                player.prepare();
                player.start();
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }

                });
                player.setLooping(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    public void onActionDown(RecordMicButton recordMicButton,Context context) {

        if (recordListener != null) {
            if (CCPermissionHelper.hasPermissions(context, OneToOneChatActivity.RECORD_PERMISSION)) {
                recordListener.onStart();
            } else {
                CCPermissionHelper.requestPermissions(((Activity) context), OneToOneChatActivity.RECORD_PERMISSION,
                        OneToOneChatActivity.RECORD_CODE);
            }
        }

        AnimUtil.setStartRecorded(true);
        AnimUtil.resetSmallMic(recordMic);


        recordMicButton.startScale();

        initialX = recordMicButton.getX();

        playSound(RECORD_START);

        showViews();

        AnimUtil.animateMic(recordMic);

        counterTime.setBase(SystemClock.elapsedRealtime());

        startTime = System.currentTimeMillis();

        counterTime.start();
        isSwiped = false;

    }


    protected void onActionMove(RecordMicButton recordMicButton, MotionEvent motionEvent) {


        long time = System.currentTimeMillis() - startTime;

        if (!isSwiped) {

            //Swipe To Cancel
              float slideX=slideToCancelLayout.getX();
              int right=counterTime.getRight();

            if (slideX != 0 && slideX <= right + cancelOffset) {

                //if the time was less than one second then do not start basket animation
                if (isLessThanOneSecond(time)) {
                    hideViews(true);
                    AnimUtil.clearAlphaAnimation(false,recordMic);

                } else {
                    hideViews(true);
                    AnimUtil.clearAlphaAnimation(true,recordMic);
                }

                AnimUtil.moveSlideToCancel(recordMicButton,slideToCancelLayout, initialX, difX);
                counterTime.stop();
                isSwiped = true;

                AnimUtil.setStartRecorded(false);
                if (recordListener != null) {

                    recordListener.onCancel();
                }


            } else {

                if (motionEvent.getRawX() < initialX) {
                    recordMicButton.animate()
                            .x(motionEvent.getRawX())
                            .setDuration(0)
                            .start();

                    if (difX == 0)
                        difX = (initialX - slideToCancelLayout.getX());


                    slideToCancelLayout.animate()
                            .x(motionEvent.getRawX() - difX)
                            .setDuration(0)
                            .start();

                }


            }

        }
    }

    protected void onActionUp(RecordMicButton recordMicButton) {

        elapsedTime = System.currentTimeMillis() - startTime;

        if (!isLessThanSecondAllowed && isLessThanOneSecond(elapsedTime) && !isSwiped) {
            if (recordListener != null)
                recordListener.onLessTime();
            AnimUtil.setStartRecorded(false);
            playSound(RECORD_ERROR);


        } else {
            if (recordListener != null && !isSwiped)
                recordListener.onFinish(elapsedTime);

            AnimUtil.setStartRecorded(false);

            if (!isSwiped)
                playSound(RECORD_FINISHED);

        }


        //if user has swiped then do not hide SmallMic since it will be hidden after swipe Animation
        hideViews(!isSwiped);


        if (!isSwiped)
            AnimUtil.clearAlphaAnimation(true,recordMic);

        AnimUtil.moveSlideToCancel(recordMicButton,slideToCancelLayout, initialX, difX);
        counterTime.stop();



    }


    private void setMarginRight(int marginRight, boolean convertToDp) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) slideToCancelLayout.getLayoutParams();
        if (convertToDp) {
            layoutParams.rightMargin = (int) CommonUtils.dpToPx(context,marginRight);
        } else
            layoutParams.rightMargin = marginRight;

        slideToCancelLayout.setLayoutParams(layoutParams);
    }


    public void setOnRecordListener(RecordListener recrodListener) {
        this.recordListener = recrodListener;
    }



    public void setSoundEnabled(boolean isEnabled) {
        isSoundEnabled = isEnabled;
    }

    public void setLessThanSecondAllowed(boolean isAllowed) {
        isLessThanSecondAllowed = isAllowed;
    }

    public void setSlideToCancelText(String text) {
        slideToCancel.setText(text);
    }

    public void setSlideToCancelTextColor(int color) {
        slideToCancel.setTextColor(color);
    }

    public void setSmallMicColor(int color) {
        recordMic.setColorFilter(color);
    }

    public void setSmallMicIcon(int icon) {
        recordMic.setImageResource(icon);
    }

    public void setSlideMarginRight(int marginRight) {
        setMarginRight(marginRight, true);
    }


    public void setCustomSounds(int startSound, int finishedSound, int errorSound) {
        //0 means do not play sound
        RECORD_START = startSound;
        RECORD_FINISHED = finishedSound;
        RECORD_ERROR = errorSound;
    }

    public float getCancelOffset() {
        return cancelOffset;
    }

    public void setCancelOffset(float cancelBounds) {
        setCancelOffset(cancelBounds, true);
    }

    //set Chronometer color
    public void setCounterTimeColor(int color) {
        counterTime.setTextColor(color);
    }

    public void setSlideToCancelArrowColor(int color){
        arrow.setColorFilter(color);
    }


    private void setCancelOffset(float cancelBounds, boolean DpToPixel) {
        float bounds = DpToPixel ? CommonUtils.dpToPx( context,cancelBounds) : cancelBounds;
        this.cancelOffset = bounds;
    }


}
