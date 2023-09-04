package com.cometchat.javasampleapp;

import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chat.core.CometChat;


public class Application extends android.app.Application {
    private static final String TAG = "UIKitApplication";
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        CometChat.removeConnectionListener(TAG);
    }
}
