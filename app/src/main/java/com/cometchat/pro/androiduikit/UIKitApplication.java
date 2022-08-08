package com.cometchat.pro.androiduikit;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.uikit.ui_components.calls.call_manager.listener.CometChatCallListener;
import com.cometchat.pro.uikit.ui_settings.UIKitSettings;


public class UIKitApplication extends Application {

    private static final String TAG = "UIKitApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    public static void initListener(Context context) {
        UIKitSettings uiKitSettings = new UIKitSettings(context);
        uiKitSettings.addConnectionListener(TAG);
        CometChatCallListener.addCallListener(TAG,context);
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
        CometChat.removeConnectionListener(TAG);
        CometChatCallListener.removeCallListener(TAG);
    }
}
