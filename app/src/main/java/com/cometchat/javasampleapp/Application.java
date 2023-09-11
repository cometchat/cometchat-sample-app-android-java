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
        LISTENER_ID = System.currentTimeMillis() + "";
        if(AppUtils.isNightMode(this)){
            Palette.getInstance(this).mode(CometChatTheme.MODE.DARK);
        }
    }

    public static void addCallListener(Context context) {
        CometChat.addCallListener(LISTENER_ID, new CometChat.CallListener() {
            @Override
            public void onIncomingCallReceived(Call call) {
                CometChatCallActivity.launchIncomingCallScreen(context, call, null);
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
        if(AppUtils.isNightMode(this)){
            Palette.getInstance(this).mode(CometChatTheme.MODE.DARK);
        }else{
            Palette.getInstance(this).mode(CometChatTheme.MODE.LIGHT);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
