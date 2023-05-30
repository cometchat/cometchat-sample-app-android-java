package com.cometchat.pro.javasampleapp;

import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.pro.core.CometChat;


public class Application extends android.app.Application {

    private static final String TAG = "UIKitApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setTheme();
    }

    private void setTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        CometChat.removeConnectionListener(TAG);
    }
}
