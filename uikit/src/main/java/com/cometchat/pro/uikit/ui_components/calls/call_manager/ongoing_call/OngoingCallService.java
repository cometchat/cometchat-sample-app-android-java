package com.cometchat.pro.uikit.ui_components.calls.call_manager.ongoing_call;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;

import java.util.Timer;
import java.util.TimerTask;

public class OngoingCallService extends Service {

    private static final int REQUEST_CODE = 888;
    private int counter = 0;

    NotificationManager notificationManager;
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            this.startForeground(1,new Notification());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startMyOwnForeground() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(2);
        PendingIntent pendingIntent;
        if (CometChat.getActiveCall()!=null) {
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),REQUEST_CODE,
                    getCallIntent("Ongoing"),PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE,
                    getCallIntent("Ongoing"), PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"2");
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.cc)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle(getResources().getString(R.string.tap_to_join_call))
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentIntent(pendingIntent)
                .setCategory(Notification.CATEGORY_CALL)
                .build();
        startForeground(1,notification);
    }

    private Intent getCallIntent(String title) {
        Intent callIntent;
        if (CometChat.getActiveCall()!=null) {
            callIntent = new Intent(getApplicationContext(),OngoingCallBroadcast.class);
            callIntent.putExtra(UIKitConstants.IntentStrings.SESSION_ID, CometChat.getActiveCall().getSessionId());
            callIntent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChat.getActiveCall().getType());
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            callIntent.setAction(title);
            return callIntent;
        } else {
            callIntent = new Intent(getApplicationContext(), CometChatUI.class);
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            notificationManager.cancel(2);
            notificationManager.cancel(1);
            return callIntent;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();

//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("restartservice");
//        this.sendBroadcast(broadcastIntent);
    }

    @Override
    public boolean stopService(Intent name) {
        stopTimer();
        return super.stopService(name);
    }

    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("AppInBackground: ",""+ counter++ );
            }
        };
        timer.schedule(timerTask, 1000, 1000);
    }

    public void stopTimer() {
        if (timer!=null) {
            timer.cancel();
            timer = null;
        }
    }
}
