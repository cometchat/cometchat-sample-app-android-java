package com.cometchat.pro.androiduikit;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;


import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.androiduikit.constants.AppConfig;
import com.cometchat.pro.helpers.Logger;
import com.cometchat.pro.uikit.Settings.UIKitSettings;

import constant.StringContract;
import listeners.CometChatCallListener;
import utils.PreferenceUtil;
import utils.Utils;

public class UIKitApplication extends Application {

    private static final String TAG = "UIKitApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        AppSettings appSettings = new AppSettings.AppSettingsBuilder().
                subscribePresenceForAllUsers().setRegion(AppConfig.AppDetails.REGION).build();
        CometChat.init(this, AppConfig.AppDetails.APP_ID, appSettings,
                new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                UIKitSettings.setAppID(AppConfig.AppDetails.APP_ID);
                UIKitSettings.setAPIKey(AppConfig.AppDetails.API_KEY);
                CometChat.setSource("ui-kit","android","java");
                Log.d(TAG, "onSuccess: "+s);
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(UIKitApplication.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        CometChatCallListener.addCallListener(TAG,this);
        createNotificationChannel();
    }

    private void addConnectionListener(String tag) {
        CometChat.addConnectionListener(tag, new CometChat.ConnectionListener() {
            @Override
            public void onConnected() {
                Toast.makeText(getBaseContext(),"Connected",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onConnecting() {

            }

            @Override
            public void onDisconnected() {
                Toast.makeText(getBaseContext(),"You have been disconnected.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFeatureThrottled() {

            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("2", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        CometChatCallListener.removeCallListener(TAG);
    }
}
