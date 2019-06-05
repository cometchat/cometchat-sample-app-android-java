package com.inscripts.cometchatpulse.demo.CustomView;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Utils.AnimUtil;
import com.inscripts.cometchatpulse.demo.Utils.KeyboardVisibilityEvent;
import com.inscripts.cometchatpulse.demo.Utils.MediaUtils;
import com.inscripts.cometchatpulse.demo.Utils.ViewUtil;

import static com.inscripts.cometchatpulse.demo.Contracts.StringContract.RequestCode.ADD_DOCUMENT;
import static com.inscripts.cometchatpulse.demo.Contracts.StringContract.RequestCode.ADD_GALLERY;
import static com.inscripts.cometchatpulse.demo.Contracts.StringContract.RequestCode.ADD_SOUND;
import static com.inscripts.cometchatpulse.demo.Contracts.StringContract.RequestCode.TAKE_PHOTO;
import static com.inscripts.cometchatpulse.demo.Contracts.StringContract.RequestCode.TAKE_VIDEO;

public class AttachmentTypeSelector extends PopupWindow {


    private static final int ANIMATION_DURATION = 300;

    @SuppressWarnings("unused")
    private static final String TAG = AttachmentTypeSelector.class.getSimpleName();

    private final @NonNull
    ImageView imageButton;
    private final @NonNull
    ImageView audioButton;
    private final @NonNull
    ImageView documentButton;
    private final @NonNull
    ImageView cameraButton;
    private final @NonNull
    ImageView closeButton;
    private final @NonNull
    ImageView videoButton;


    private @Nullable
    View currentAnchor;
    private @Nullable
    AttachmentClickedListener listener;
    private Rect rect;
    private int winHeight;

    public AttachmentTypeSelector(@NonNull Context context, @Nullable AttachmentClickedListener listener) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.attachment_type_selector, null, true);

        this.listener = listener;
        this.imageButton = ViewUtil.findById(layout, R.id.gallery_button);
        this.audioButton = ViewUtil.findById(layout, R.id.audio_button);
        this.documentButton = ViewUtil.findById(layout, R.id.document_button);
        this.cameraButton = ViewUtil.findById(layout, R.id.camera_button);
        this.closeButton = ViewUtil.findById(layout, R.id.close_button);
        this.videoButton = ViewUtil.findById(layout, R.id.video);


        Drawable imageDrawable = context.getResources().getDrawable(R.drawable.ic_image_black_24dp);

        Drawable caemraDrawable = context.getResources().getDrawable(R.drawable.ic_camera_white_24dp);

        Drawable audioDrawable = context.getResources().getDrawable(R.drawable.ic_mic_white_24dp);

        Drawable whiteBoardDrawable = context.getResources().getDrawable(R.drawable.ic_video_library_black_24dp);

        Drawable documentDrawable = context.getResources().getDrawable(R.drawable.ic_insert_drive_file_white_24dp);

        Drawable closeDrawable = context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp);

        this.imageButton.setImageBitmap(MediaUtils.getPlaceholderImage(context, imageDrawable));
        this.audioButton.setImageBitmap(MediaUtils.getPlaceholderImage(context, audioDrawable));
        this.documentButton.setImageBitmap(MediaUtils.getPlaceholderImage(context, documentDrawable));

        this.closeButton.setImageBitmap(MediaUtils.getPlaceholderImage(context, closeDrawable));
        this.cameraButton.setImageBitmap(MediaUtils.getPlaceholderImage(context, caemraDrawable));
        this.videoButton.setImageBitmap(MediaUtils.getPlaceholderImage(context, whiteBoardDrawable));


        this.imageButton.setOnClickListener(new PropagatingClickListener(ADD_GALLERY));
        this.audioButton.setOnClickListener(new PropagatingClickListener(ADD_SOUND));
        this.documentButton.setOnClickListener(new PropagatingClickListener(ADD_DOCUMENT));

        this.cameraButton.setOnClickListener(new PropagatingClickListener(TAKE_PHOTO));
        this.videoButton.setOnClickListener(new PropagatingClickListener(TAKE_VIDEO));
        this.closeButton.setOnClickListener(new CloseClickListener());


        setContentView(layout);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(0);
        setClippingEnabled(false);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        setFocusable(true);
        setTouchable(true);


    }


    public void show(@NonNull Activity activity, final @NonNull View anchor) {

        int y;
        if (KeyboardVisibilityEvent.isKeyboardVisible(activity)) {
            y = 0;
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } else {
            rect = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            winHeight = activity.getWindow().getDecorView().getHeight();
            y=winHeight-rect.bottom;
        }


        this.currentAnchor = anchor;


        showAtLocation(anchor, Gravity.BOTTOM, 0, y);


        getContentView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getContentView().getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    animateWindowInCircular(anchor, getContentView());
                } else {
                    animateWindowInTranslate(getContentView());
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateButtonIn(imageButton, ANIMATION_DURATION / 2);
            animateButtonIn(cameraButton, ANIMATION_DURATION / 2);
            animateButtonIn(audioButton, ANIMATION_DURATION / 3);
            animateButtonIn(videoButton, ANIMATION_DURATION / 3);
            animateButtonIn(documentButton, ANIMATION_DURATION / 4);
            animateButtonIn(closeButton, 0);
        }
    }

    @Override
    public void dismiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animateWindowOutCircular(currentAnchor, getContentView());
        } else {
            animateWindowOutTranslate(getContentView());
        }
    }

    public void setListener(@Nullable AttachmentClickedListener listener) {
        this.listener = listener;
    }

    private void animateButtonIn(View button, int delay) {
        AnimationSet animation = new AnimationSet(true);
        Animation scale = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.0f);

        animation.addAnimation(scale);
        animation.setInterpolator(new OvershootInterpolator(1));
        animation.setDuration(ANIMATION_DURATION);
        animation.setStartOffset(delay);
        button.startAnimation(animation);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateWindowInCircular(@Nullable View anchor, @NonNull View contentView) {
        Pair<Integer, Integer> coordinates = AnimUtil.getClickOrigin(anchor, contentView);
        Animator animator = ViewAnimationUtils.createCircularReveal(contentView,
                coordinates.first,
                coordinates.second,
                0,
                Math.max(contentView.getWidth(), contentView.getHeight()));
        animator.setDuration(ANIMATION_DURATION);
        animator.start();
    }

    private void animateWindowInTranslate(@NonNull View contentView) {
        Animation animation = new TranslateAnimation(0, 0, contentView.getHeight(), 0);
        animation.setDuration(ANIMATION_DURATION);

        getContentView().startAnimation(animation);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animateWindowOutCircular(@Nullable View anchor, @NonNull View contentView) {
        Pair<Integer, Integer> coordinates = AnimUtil.getClickOrigin(anchor, contentView);
        Animator animator = ViewAnimationUtils.createCircularReveal(getContentView(),
                coordinates.first,
                coordinates.second,
                Math.max(getContentView().getWidth(), getContentView().getHeight()),
                0);

        animator.setDuration(ANIMATION_DURATION);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AttachmentTypeSelector.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animator.start();
    }

    private void animateWindowOutTranslate(@NonNull View contentView) {
        Animation animation = new TranslateAnimation(0, 0, 0, contentView.getTop() + contentView.getHeight());
        animation.setDuration(ANIMATION_DURATION);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                AttachmentTypeSelector.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        getContentView().startAnimation(animation);
    }



    private class PropagatingClickListener implements View.OnClickListener {

        private final int type;

        private PropagatingClickListener(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            animateWindowOutTranslate(getContentView());

            if (listener != null) listener.onClick(type);
        }

    }

    private class CloseClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

    public interface AttachmentClickedListener {
        void onClick(int type);

    }

}