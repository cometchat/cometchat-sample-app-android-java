package com.inscripts.cometchatpulse.demo.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Contracts.CallActivityContract;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.Presenters.CallActivityPresenter;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Utils.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class CallActivity extends AppCompatActivity implements CallActivityContract.CallActivityView {


    private RelativeLayout callView;

    private String sessionId;

    private static final String TAG = "CallActivity";

    private CallActivityContract.CallActivityPresenter callActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        callActivityPresenter=new CallActivityPresenter();
        callActivityPresenter.attach(this);

        callView = findViewById(R.id.call_view);

        if (getIntent().hasExtra(StringContract.IntentStrings.SESSION_ID)) {
            sessionId = getIntent().getStringExtra(StringContract.IntentStrings.SESSION_ID);
            Logger.error(TAG, " sessionId " + sessionId);

            CometChat.startCall(CallActivity.this, sessionId, callView, new CometChat.OngoingCallListener() {
                @Override
                public void onUserJoined(User user) {
                    Logger.error(TAG, " Name " + user.getName());
                }

                @Override
                public void onUserLeft(User user) {
                    Log.d(TAG, "onUserLeft: "+user.getName());
                }

                @Override
                public void onError(CometChatException e) {

                    Log.d(TAG, "onError: "+e.getMessage());
                }

                @Override
                public void onCallEnded(Call call) {
                    Log.d(TAG, "onCallEnded: "+call.toString());
                    finish();
                }
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
         callActivityPresenter.addCallListener(this,TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        callActivityPresenter.removeCallListener(TAG);
    }
}
