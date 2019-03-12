package com.inscripts.cometchatpulse.demo.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Helper.OnRecordClickListener;
import com.inscripts.cometchatpulse.demo.Utils.AnimUtil;

public class RecordMicButton extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener, View.OnClickListener {

    private View scaleView;

    private boolean listenForRecord=true;

    private OnRecordClickListener onRecordClickListener;

    private RecordAudio recordAudio;

    private Context context;

    public void setRecordAudio(RecordAudio recordAudio) {
        this.recordAudio = recordAudio;

    }

    public RecordMicButton(Context context) {
        super(context);
        initViewComponent(context, null);

    }

    public RecordMicButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViewComponent(context, attrs);

    }

    public RecordMicButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViewComponent(context, attrs);

    }


    private void initViewComponent(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordMic);

            int imageResource = typedArray.getResourceId(R.styleable.RecordMic_mic_icon, -1);


            if (imageResource != -1) {
                setImage(imageResource);
            }

            scaleView = this;


            typedArray.recycle();
        }

        this.setOnTouchListener(this);
        this.setOnClickListener(this);

    }


    private void setImage(int imageResource) {
        Drawable image = AppCompatResources.getDrawable(getContext(), imageResource);
        setImageDrawable(image);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setClip(this);
    }

    public void setClip(View v) {
        if (v.getParent() == null) {
            return;
        }

        if (v instanceof ViewGroup) {
            ((ViewGroup) v).setClipChildren(false);
            ((ViewGroup) v).setClipToPadding(false);
        }

        if (v.getParent() instanceof View) {
            setClip((View) v.getParent());
        }
    }

    public void startScale() {
        AnimUtil.start(scaleView);
    }

    public void stopScale() {
        AnimUtil.stop(scaleView);
    }

    public void setListenForRecord(boolean listenForRecord,Context context) {
        this.listenForRecord = listenForRecord;
        this.context=context;
    }

    public boolean isRecord() {
        return listenForRecord;
    }

    public void setOnRecordClickListener(OnRecordClickListener onRecordClickListener) {
        this.onRecordClickListener = onRecordClickListener;
    }


    @Override
    public void onClick(View view) {
        if (onRecordClickListener != null)
            onRecordClickListener.onClick(view);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (isRecord()) {
            switch (motionEvent.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    recordAudio.onActionDown((RecordMicButton) view,context);
                    break;

                case MotionEvent.ACTION_MOVE:
                    recordAudio.onActionMove((RecordMicButton) view, motionEvent);
                    break;

                case MotionEvent.ACTION_UP:
                    recordAudio.onActionUp((RecordMicButton) view);
                    break;

            }

        }
        return isRecord();
    }
}
