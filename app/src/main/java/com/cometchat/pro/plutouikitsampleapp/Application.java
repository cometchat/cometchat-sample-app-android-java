package com.cometchat.pro.plutouikitsampleapp;

import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.helpers.Logger;
import com.cometchatworkspace.components.shared.primaryComponents.theme.CometChatTheme;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Palette;
import com.cometchatworkspace.components.shared.primaryComponents.theme.Typography;
import com.cometchatworkspace.resources.utils.Utils;


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
