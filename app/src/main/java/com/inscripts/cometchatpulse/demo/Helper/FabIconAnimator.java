package com.inscripts.cometchatpulse.demo.Helper;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inscripts.cometchatpulse.demo.R;

import java.util.concurrent.atomic.AtomicBoolean;

public class FabIconAnimator {

    private static final String ROTATION_Y_PROPERTY = "rotationY";

    private static final float TWITCH_END = 20F;
    private static final float TWITCH_START = 0F;
    private static final int DURATION = 200;


    private Drawable currentIcon;
    @StringRes
    private  static int currentText;
    private boolean isAnimating;

    private final ImageView imageView;
    private final TextView textView;
    private final ConstraintLayout container;
    private final Transition.TransitionListener listener = new Transition.TransitionListener() {
        public void onTransitionStart(Transition transition) { isAnimating = true; }

        public void onTransitionEnd(Transition transition) { isAnimating = false; }

        public void onTransitionCancel(Transition transition) { isAnimating = false; }

        public void onTransitionPause(Transition transition) { }

        public void onTransitionResume(Transition transition) { }
    };

    public FabIconAnimator(ConstraintLayout container) {
        this.container = container;
        this.imageView = container.findViewById(R.id.image);
        this.textView=container.findViewById(R.id.card_textView);
    }

    public void update( Drawable icon, @StringRes int text) {
        boolean isSame = currentIcon == icon && currentText == text;
        currentIcon = icon;
        currentText = text;
        animateChange(icon, text, isSame);
    }

    public void setExtended(boolean extended) {
        setExtended(extended, false);
    }

    public void setOnClickListener(@Nullable final View.OnClickListener clickListener) {
        if (clickListener == null) {
            container.setOnClickListener(null);
            return;
        }
        final AtomicBoolean flag = new AtomicBoolean(true);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag.getAndSet(false)) return;
                clickListener.onClick(v);
                container.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flag.set(true);
                    }
                },2000);
            }
        });

    }

    private boolean isExtended() {
        return textView.getLayoutParams().width != 0;
    }

    private void animateChange( Drawable icon, @StringRes int text, boolean isSame) {
        boolean extended = isExtended();
        textView.setText(text);
        imageView.setImageDrawable(icon);
        setExtended(extended, !isSame);
        if (!extended) twitch();
    }

    private void setExtended(boolean extended, boolean force) {
        if (isAnimating || (extended && isExtended() && !force)) return;

        ConstraintSet set = new ConstraintSet();
        set.clone(container.getContext(), extended ? R.layout.expanded : R.layout.normal);

        TransitionManager.beginDelayedTransition(container, new AutoTransition()
                .addListener(listener).setDuration(150));

        if (extended) {
            textView.setText(currentText);
        }
        else {
            textView.setText("");
        }

        set.applyTo(container);
    }

    private void twitch() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator twitchA = animateProperty(ROTATION_Y_PROPERTY, TWITCH_START, TWITCH_END);
        ObjectAnimator twitchB = animateProperty(ROTATION_Y_PROPERTY, TWITCH_END, TWITCH_START);

        set.play(twitchB).after(twitchA);
        set.start();
    }

    @NonNull
    private ObjectAnimator animateProperty(String property, float start, float end) {
        return ObjectAnimator.ofFloat(container, property, start, end).setDuration(DURATION);
    }
}