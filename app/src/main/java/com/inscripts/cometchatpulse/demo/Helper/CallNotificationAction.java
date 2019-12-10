package com.inscripts.cometchatpulse.demo.Helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.inscripts.cometchatpulse.demo.Activity.CallActivity;
import com.inscripts.cometchatpulse.demo.Activity.IncomingCallActivity;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;

public class CallNotificationAction extends BroadcastReceiver {

    String TAG = "CallNotificationAction";
    @Override
    public void onReceive(Context context, Intent intent) {
        String sessionID = intent.getStringExtra(StringContract.IntentStrings.SESSION_ID);
        Log.e(TAG, "onReceive: " + intent.getStringExtra(StringContract.IntentStrings.SESSION_ID));
        if (intent.getAction().equals("Answers"))
        {
            CometChat.acceptCall(sessionID, new CometChat.CallbackListener<Call>() {
                @Override
                public void onSuccess(Call call) {
                    Intent acceptIntent = new Intent(context, CallActivity.class);
                    acceptIntent.putExtra(StringContract.IntentStrings.SESSION_ID,call.getSessionId());
                    acceptIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(acceptIntent);
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.cancel(05);
                }

                @Override
                public void onError(CometChatException e) {
                    Toast.makeText(context,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.cancel(05);
                }
            });
        }
        else {
            CometChat.rejectCall(sessionID, CometChatConstants.CALL_STATUS_REJECTED, new CometChat.CallbackListener<Call>() {
                @Override
                public void onSuccess(Call call) {
                    Log.e(TAG, "onSuccess: " + call.getCallStatus());
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.cancel(05);
                }

                @Override
                public void onError(CometChatException e) {
                    Log.e(TAG, "onError: " + e.getCode() + e.getDetails());
                }
            });
        }
    }
}
