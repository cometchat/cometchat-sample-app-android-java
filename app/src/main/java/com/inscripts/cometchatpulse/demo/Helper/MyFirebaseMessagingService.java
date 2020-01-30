package com.inscripts.cometchatpulse.demo.Helper;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.helpers.CometChatHelper;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.inscripts.cometchatpulse.demo.Activity.GroupChatActivity;
import com.inscripts.cometchatpulse.demo.Activity.OneToOneChatActivity;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import static org.webrtc.ContextUtils.getApplicationContext;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseService";
    private JSONObject json;
    private Intent intent;
    private int count=0;
    private Call call;
    private static final int REQUEST_CODE = 12;

    private boolean isCall;


    public static void subscribeUser(String UID) {
        FirebaseMessaging.getInstance().subscribeToTopic(StringContract.AppDetails.APP_ID + "_"+ CometChatConstants.RECEIVER_TYPE_USER +"_" +
                UID);
    }

    public static void unsubscribeUser(String UID) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(StringContract.AppDetails.APP_ID + "_"+ CometChatConstants.RECEIVER_TYPE_USER +"_" +
                UID);
    }

    public static void subscribeGroup(String GUID) {
        FirebaseMessaging.getInstance().subscribeToTopic(StringContract.AppDetails.APP_ID + "_"+ CometChatConstants.RECEIVER_TYPE_GROUP +"_" +
                GUID);
    }

    public static void unsubscribeGroup(String GUID) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(StringContract.AppDetails.APP_ID + "_"+ CometChatConstants.RECEIVER_TYPE_GROUP +"_" +
                GUID);
    }

    @Override
    public void onNewToken(String s) {
        Log.d(TAG, "onNewToken: "+s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            count++;
            json = new JSONObject(remoteMessage.getData());
            Log.d(TAG, "JSONObject: "+json.toString());
            JSONObject messageData = new JSONObject(json.getString("message"));
            BaseMessage baseMessage = CometChatHelper.processMessage(new JSONObject(remoteMessage.getData().get("message")));
            if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER))
            {
                intent = new Intent(getApplicationContext(), OneToOneChatActivity.class);
                intent.putExtra(StringContract.IntentStrings.USER_ID,baseMessage.getSender().getUid());
                intent.putExtra(StringContract.IntentStrings.AVATAR,baseMessage.getSender().getAvatar());
                intent.putExtra(StringContract.IntentStrings.USER_NAME,baseMessage.getSender().getName());
            }
            else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)){
                intent = new Intent(getApplicationContext(), GroupChatActivity.class);
                intent.putExtra(StringContract.IntentStrings.INTENT_GROUP_ID,baseMessage.getReceiverUid());
                intent.putExtra(StringContract.IntentStrings.INTENT_GROUP_NAME,((Group)baseMessage.getReceiver()).getName());
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (baseMessage instanceof Call){
                call = (Call)baseMessage;
                isCall=true;
            }
            showNotifcation(pendingIntent,baseMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showNotifcation(PendingIntent pendingIntent,BaseMessage baseMessage) {

        try {
            int m = (int) ((new Date().getTime()));
            String GROUP_ID = "group_id";

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"2")
                    .setSmallIcon(R.drawable.cc)
                    .setContentTitle(json.getString("title"))
                    .setContentText(json.getString("alert"))
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setColor(getResources().getColor(R.color.primaryColor))
                    .setLargeIcon(getBitmapFromURL(baseMessage.getSender().getAvatar()))
                    .setGroup(GROUP_ID)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

            NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(this,"2")
                    .setContentTitle("CometChat")
                    .setContentText(count+" messages")
                    .setSmallIcon(R.drawable.cc)
                    .setGroup(GROUP_ID)
                    .setGroupSummary(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            if (isCall){
                builder.setGroup(GROUP_ID+"Call");
                if (json.getString("alert").equals("Incoming audio call") || json.getString("alert").equals("Incoming video call")) {
                    builder.setOngoing(true);
                    builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                    builder.addAction(0, "Answers", PendingIntent.getBroadcast(getApplicationContext(), REQUEST_CODE, getCallIntent("Answers"), PendingIntent.FLAG_UPDATE_CURRENT));
                    builder.addAction(0, "Decline", PendingIntent.getBroadcast(getApplicationContext(), 1, getCallIntent("Decline"), PendingIntent.FLAG_UPDATE_CURRENT));
                }
                notificationManager.notify(05,builder.build());
            }
            else {
                notificationManager.notify(baseMessage.getId(), builder.build());
                notificationManager.notify(0, summaryBuilder.build());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Intent getCallIntent(String title){
        Intent callIntent = new Intent(getApplicationContext(), CallNotificationAction.class);
        callIntent.putExtra(StringContract.IntentStrings.SESSION_ID,call.getSessionId());
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setAction(title);
        return callIntent;
    }

}
