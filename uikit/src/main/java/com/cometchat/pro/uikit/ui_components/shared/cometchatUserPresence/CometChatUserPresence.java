package com.cometchat.pro.uikit.ui_components.shared.cometchatUserPresence;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.StringDef;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.google.android.material.card.MaterialCardView;

/**
 * Purpose - StatusIndicator is a subclass of View and it is used as component to display status
 * indicator of user. If helps to know whether user is online or offline.
 *
 * Created on - 20th December 2019
 *
 * Modified on  - 16th January 2020
 *
 */

@BindingMethods(value ={@BindingMethod(type = CometChatUserPresence.class, attribute = "app:user_status", method = "setUserStatus")})
public class CometChatUserPresence extends MaterialCardView {

    @Presence
    String status;

    /*
     * Constants to define shape
     * */
    @StringDef({Presence.ONLINE, Presence.OFFLINE})
    public @interface Presence {
        String OFFLINE = CometChatConstants.USER_STATUS_OFFLINE;
        String ONLINE = CometChatConstants.USER_STATUS_ONLINE;
    }

    public CometChatUserPresence(Context context) {
        super(context);
        init();
    }

    public CometChatUserPresence(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(attrs);
        init();
    }

    public CometChatUserPresence(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributes(attrs);
        init();
    }

    public void setUserStatus(@Presence String userStatus) {
        status = userStatus;
        setValues();
    }

    private void setValues() {
        if (status == Presence.ONLINE)
            setCardBackgroundColor(getResources().getColor(R.color.online_green));
        else {
            setCardBackgroundColor(getResources().getColor(R.color.offline));
        }
        if (Utils.isDarkMode(getContext())) {
            setStrokeColor(getResources().getColor(R.color.darkModeBackground));
        } else {
            setStrokeColor(getResources().getColor(R.color.textColorWhite));
        }
        setStrokeWidth(2);
        invalidate();
    }


    private void getAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.StatusIndicator, 0, 0);

        String userStatus = a.getString(R.styleable.StatusIndicator_user_status);
        if (userStatus == null) {
            status = Presence.OFFLINE;
        } else {
            if (getContext().getString(R.string.online).equalsIgnoreCase(userStatus)) {
                status = Presence.ONLINE;
            } else {
                status = Presence.OFFLINE;
            }
        }
    }

    protected void init() {
        if (status == Presence.ONLINE) {
            setVisibility(View.VISIBLE);
            setCardBackgroundColor(Color.parseColor("#3BDF2F"));
        } else {
            setVisibility(View.GONE);
            setCardBackgroundColor(Color.parseColor("#C4C4C4"));
        }
    }
}