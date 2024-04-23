package com.cometchat.javasampleapp;

import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.cometchat.chat.core.Call;
import com.cometchat.chatuikit.calls.CometChatCallActivity;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chat.core.CometChat;


public class Application extends android.app.Application {
    private static String LISTENER_ID;

    @Override
    public void onCreate() {
        super.onCreate();
        addCallListener();
        if (AppUtils.isNightMode(this)) {
            Palette.getInstance().mode(CometChatTheme.MODE.DARK);
        }
    }

    private void addCallListener() {
        LISTENER_ID = System.currentTimeMillis() + "";
        CometChat.addCallListener(LISTENER_ID, new CometChat.CallListener() {
            @Override
            public void onIncomingCallReceived(Call call) {
                CometChatCallActivity.launchIncomingCallScreen(getApplicationContext(), call, null);
            }

            @Override
            public void onOutgoingCallAccepted(Call call) {

            }

            @Override
            public void onOutgoingCallRejected(Call call) {

            }

            @Override
            public void onIncomingCallCancelled(Call call) {

            }
        });
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setTheme();
    }

    private void setTheme() {
        if (AppUtils.isNightMode(this)) {
            Palette.getInstance().mode(CometChatTheme.MODE.DARK);
            AppUtils.switchDarkMode();
        } else {
            Palette.getInstance().mode(CometChatTheme.MODE.LIGHT);
            AppUtils.switchLightMode();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
