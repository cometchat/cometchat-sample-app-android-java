package com.cometchat.pro.uikit.ui_components.calls.call_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CallManager;
import com.cometchat.pro.core.CallSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.rtc.model.AudioMode;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.calls.call_manager.ongoing_call.OngoingCallService;
import com.cometchat.pro.uikit.ui_components.shared.CometChatSnackBar;
import com.cometchat.pro.uikit.ui_resources.utils.CometChatError;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;

import java.util.List;

/**
 * CometChatStartCallActivity is activity class which is used to start a call. It takes sessionID
 * as a parameter and start call for particular sessionID.
 *
 * Created On - 22nd August 2020
 *
 * Modified On -  07th October 2020
 *
 */
public class CometChatStartCallActivity extends AppCompatActivity {

    public static CometChatStartCallActivity activity;

    private RelativeLayout mainView;

    private String sessionID;

    private String type;

    private CallSettings callSettings;

    private LinearLayout connectingLayout;

    private OngoingCallService ongoingCallService;

    private Intent mServiceIntent;

    private boolean isDefaultCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_cometchat_start_call);

        ongoingCallService = new OngoingCallService();
        mServiceIntent = new Intent(this,ongoingCallService.getClass());
        isDefaultCall = getIntent().getBooleanExtra(UIKitConstants.IntentStrings.IS_DEFAULT_CALL,false);
        if (isDefaultCall && !isMyServiceRunning(ongoingCallService.getClass())) {
            startService(mServiceIntent);
        }

        mainView = findViewById(R.id.call_view);
        connectingLayout = findViewById(R.id.connecting_to_call);
        sessionID = getIntent().getStringExtra(UIKitConstants.IntentStrings.SESSION_ID);
        type = getIntent().getStringExtra(UIKitConstants.IntentStrings.TYPE);
        if (type!=null && type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            callSettings = new CallSettings.CallSettingsBuilder(this,mainView)
                    .setSessionId(sessionID)
                    .setMode(CallSettings.MODE_SINGLE)
                    .build();
        else
            callSettings = new CallSettings.CallSettingsBuilder(this,mainView)
                    .setSessionId(sessionID)
                    .build();

        CometChatError.init(this);
        Log.e( "startCallActivity: ",sessionID+" "+type);
        CometChat.startCall(callSettings, new CometChat.OngoingCallListener() {
            @Override
            public void onAudioModesUpdated(List<AudioMode> list) {
                Log.e( "onAudioModesUpdated: ",list.toString() );
            }

            @Override
            public void onUserListUpdated(List<User> list) {
                Log.e( "onUserListUpdated: ",list.toString() );
            }

            @Override
            public void onUserJoined(User user) {
                connectingLayout.setVisibility(View.GONE);
                CometChatSnackBar.show(CometChatStartCallActivity.this,
                        mainView, getString(R.string.user_joined)+":"+ user.getName(),
                        CometChatSnackBar.INFO);
                Log.e("onUserJoined: ", user.getUid());
            }

            @Override
            public void onUserLeft(User user) {
                if (user!=null) {
                    CometChatSnackBar.show(CometChatStartCallActivity.this,
                            mainView, getString(R.string.user_left)+":"+ user.getName(),
                            CometChatSnackBar.INFO);
                    Log.e("onUserLeft: ", user.getUid());
                    if (callSettings.isDefaultLayout()) {
                        endCall();
                    }
                } else {
                    Log.e( "onUserLeft: ","triggered" );
                }
            }

            @Override
            public void onError(CometChatException e) {
                stopService(mServiceIntent);
                Log.e("onstartcallError: ", e.getMessage());
                CometChatSnackBar.show(CometChatStartCallActivity.this,
                        mainView,CometChatError.localized(e), CometChatSnackBar.ERROR);
            }

            @Override
            public void onCallEnded(Call call) {
                stopService(mServiceIntent);
                Log.e("TAG", "onCallEnded: ");
                finish();
            }
        });
    }

    private boolean isMyServiceRunning(Class<? extends OngoingCallService> serviceClass) {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                Log.i( "isMyServiceRunning: ","Running");
                return true;
            }
        }
        Log.i("isMyServiceRunning: ","Not Running");
        return false;
    }

    @Override
    public void onBackPressed() {
        if (type!=null && type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_GROUP)) {
            endCall();
        } else {
            super.onBackPressed();
        }
    }

    private void endCall() {
        CometChat.endCall(sessionID, new CometChat.CallbackListener<Call>() {
            @Override
            public void onSuccess(Call call) {
                finish();
            }

            @Override
            public void onError(CometChatException e) {
                CometChatSnackBar.show(CometChatStartCallActivity.this,
                        mainView, CometChatError.localized(e), CometChatSnackBar.ERROR);
            }
        });
    }
}