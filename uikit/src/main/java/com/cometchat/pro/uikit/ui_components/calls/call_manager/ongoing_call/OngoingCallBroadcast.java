package com.cometchat.pro.uikit.ui_components.calls.call_manager.ongoing_call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.cometchat.pro.uikit.ui_components.calls.call_manager.CometChatStartCallActivity;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;

public class OngoingCallBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String sessionID = intent.getStringExtra(UIKitConstants.IntentStrings.SESSION_ID);
        String type = intent.getStringExtra(UIKitConstants.IntentStrings.TYPE);
        Log.e( "onReceive: ",sessionID);
        if (intent.getAction().equals("Ongoing")) {
            Intent joinOngoingIntent  = new Intent(context, CometChatStartCallActivity.class);
            joinOngoingIntent.putExtra(UIKitConstants.IntentStrings.SESSION_ID,sessionID);
            joinOngoingIntent.putExtra(UIKitConstants.IntentStrings.TYPE,type);
            joinOngoingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(joinOngoingIntent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context,OngoingCallService.class));
        } else {
            context.startService(new Intent(context, OngoingCallService.class));
        }

    }
}
