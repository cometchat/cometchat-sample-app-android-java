package com.inscripts.cometchatpulse.demo.Fcm;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Group;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.inscripts.cometchatpulse.demo.Activity.CometChatActivity;
import com.inscripts.cometchatpulse.demo.Activity.OneToOneChatActivity;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyFirebaseService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseService";

    private static final String CHANNEL_ID = "2";

    private String GROUP_ID = "group_id";

    private JSONObject json;

    private JSONObject messageData;

    public static ArrayList<String> messageList = new ArrayList<>();

    private static final int REQUEST_CODE = 12;


    public static void subscribeGroup(List<Group> groupList) {
        for (Group group : groupList) {
            FirebaseMessaging.getInstance().subscribeToTopic(StringContract.AppDetails.APP_ID + "_group_"
                    + group.getGuid());
        }

    }

    public static void subscribeUser(String UID) {
        FirebaseMessaging.getInstance().subscribeToTopic(StringContract.AppDetails.APP_ID + "_user_" +
                UID);
    }

    public static void clearNotification() {
        messageList = null;
        messageList = new ArrayList<>();
    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());

        try {
            json = new JSONObject(remoteMessage.getData());
            messageData = new JSONObject(json.getString("message"));

            Intent intent = new Intent(this, CometChatActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (!messageData.getString("sender").equals(OneToOneChatActivity.contactId) &&
                    !messageData.getString("sender").equals(CometChat.getLoggedInUser().getUid())) {
                PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                showNotifcation(pendingIntent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void showNotifcation(PendingIntent pendingIntent) {

        try {

            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.badge)
                    .setContentTitle(json.getString("title"))
                    .setContentText(json.getString("alert"))
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setGroup(GROUP_ID)
                    .setGroupSummary(true)
                    .setAutoCancel(true);

            try {
                for (int i = 0; i < messageList.size(); i++) {
                    inboxStyle.addLine(messageList.get(i));
                }
            } catch (Exception e) {

            }
            builder.setStyle(inboxStyle);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(2, builder.build());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
