package com.cometchat.javasampleapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.Group;
import com.cometchat.chat.models.TextMessage;
import com.cometchat.chat.models.User;

public class AppUtils {
    private static Group group;
    private static User user;

    public static void fetchDefaultObjects() {
        CometChat.getGroup("supergroup", new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group_) {
                group = group_;
            }

            @Override
            public void onError(CometChatException e) {

            }
        });
        CometChat.getUser("superhero5", new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user_) {
                user = user_;
            }

            @Override
            public void onError(CometChatException e) {

            }
        });
    }

    public static Group getDefaultGroup() {
        return group;
    }

    public static User getDefaultUser() {
        return user;
    }

    public static void switchLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static void switchDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public static boolean isNightMode(Context context) {
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static void changeIconTintToWhite(Context context, ImageView imageView) {
        imageView.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
    }

    public static void changeIconTintToBlack(Context context, ImageView imageView) {
        imageView.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black)));
    }

    public static void changeTextColorToWhite(Context context, TextView textView) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
    }

    public static void changeTextColorToBlack(Context context, TextView textView) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.black));
    }

}
