package com.cometchat.pro.androiduikit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import listeners.CometChatCallListener;
import screen.unified.CometChatUnified;
import utils.Utils;;

public class SelectActivity extends AppCompatActivity {

    private RadioGroup screenGroup,callGroup;

    private RadioButton userRb,conversationRb,groupRb,moreInfoRb,audioCallRb,videoCallRb,callsRb;

    private MaterialButton logout;

    private MaterialButton unifiedLaunch;

    private MaterialButton screenLaunch;

    private MaterialButton componentLaunch;

    private CardView directIntentFront,directIntentBack,usingScreenFront,usingScreenBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        logout = findViewById(R.id.logout);
        unifiedLaunch = findViewById(R.id.directLaunch);
        screenLaunch = findViewById(R.id.fragmentlaunch);
        componentLaunch = findViewById(R.id.componentLaunch);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser(v);
            }
        });
        unifiedLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectActivity.this, CometChatUnified.class));
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
        componentLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectActivity.this,ComponentListActivity.class));
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
        screenGroup = (RadioGroup)findViewById(R.id.screen_selector);
        callGroup = findViewById(R.id.call_selector);
        audioCallRb = findViewById(R.id.audioCall);
        videoCallRb = findViewById(R.id.videoCall);
        callsRb = findViewById(R.id.calls);
        userRb = (RadioButton)findViewById(R.id.users);
        groupRb = (RadioButton)findViewById(R.id.groups);
        conversationRb = (RadioButton)findViewById(R.id.conversations);
        moreInfoRb = (RadioButton)findViewById(R.id.moreinfo);
        screenGroup.setOnCheckedChangeListener(screenListner);
        callGroup.setOnCheckedChangeListener(callListener);
        screenLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id1 = screenGroup.getCheckedRadioButtonId();
                int id2 = callGroup.getCheckedRadioButtonId();
                if (id1<0 && id2<0)
                {
                    Snackbar.make(view,"Select any one screen.",Snackbar.LENGTH_LONG).show();
                } else if(id2<0) {
                    Intent intent = new Intent(SelectActivity.this, ComponentLoadActivity.class);
                    intent.putExtra("screen",id1);
                    startActivity(intent);
                    overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                } else if(id1<0) {
                    String type;
                    if (audioCallRb.isChecked()) {
                        type = CometChatConstants.CALL_TYPE_AUDIO;
                    } else {
                        type = CometChatConstants.CALL_TYPE_VIDEO;
                    }
                    CometChatCallListener.makeCall(SelectActivity.this, "superhero5", CometChatConstants.RECEIVER_TYPE_USER,type);
                }
            }
        });

    }

    private RadioGroup.OnCheckedChangeListener callListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId!=-1)
            {
                screenGroup.setOnCheckedChangeListener(null);
                screenGroup.clearCheck();
                screenGroup.setOnCheckedChangeListener(screenListner);
                screenLaunch.setText("Make Call");
            }
            if (audioCallRb.isChecked()) {
                audioCallRb.setBackground(getResources().getDrawable(R.drawable.radiobuttonbackground));
                conversationRb.setBackground(null);
                videoCallRb.setBackground(null);
                userRb.setBackground(null);
                callsRb.setBackground(null);
                groupRb.setBackground(null);
                moreInfoRb.setBackground(null);
            } else if (videoCallRb.isChecked()) {
                videoCallRb.setBackground(getResources().getDrawable(R.drawable.radiobuttonbackground));
                conversationRb.setBackground(null);
                audioCallRb.setBackground(null);
                userRb.setBackground(null);
                callsRb.setBackground(null);
                groupRb.setBackground(null);
                moreInfoRb.setBackground(null);
            }
        }
    };
    private RadioGroup.OnCheckedChangeListener screenListner = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId!=-1)
            {
                callGroup.setOnCheckedChangeListener(null);
                callGroup.clearCheck();
                callGroup.setOnCheckedChangeListener(callListener);
                screenLaunch.setText("Navigate");

            }
            if (userRb.isChecked()) {
                userRb.setBackground(getResources().getDrawable(R.drawable.radiobuttonbackground));
                groupRb.setBackground(null);
                callsRb.setBackground(null);
                conversationRb.setBackground(null);
                moreInfoRb.setBackground(null);
                audioCallRb.setBackground(null);
                videoCallRb.setBackground(null);
            } else if (callsRb.isChecked()) {
                callsRb.setBackground(getResources().getDrawable(R.drawable.radiobuttonbackground));
                userRb.setBackground(null);
                groupRb.setBackground(null);
                conversationRb.setBackground(null);
                moreInfoRb.setBackground(null);
                audioCallRb.setBackground(null);
                videoCallRb.setBackground(null);
            } else if (conversationRb.isChecked()) {
                conversationRb.setBackground(getResources().getDrawable(R.drawable.radiobuttonbackground));
                userRb.setBackground(null);
                callsRb.setBackground(null);
                groupRb.setBackground(null);
                moreInfoRb.setBackground(null);
                audioCallRb.setBackground(null);
                videoCallRb.setBackground(null);
            } else if (groupRb.isChecked()) {
                groupRb.setBackground(getResources().getDrawable(R.drawable.radiobuttonbackground));
                userRb.setBackground(null);
                conversationRb.setBackground(null);
                moreInfoRb.setBackground(null);
                audioCallRb.setBackground(null);
                videoCallRb.setBackground(null);
                callsRb.setBackground(null);
            } else {
                moreInfoRb.setBackground(getResources().getDrawable(R.drawable.radiobuttonbackground));
                userRb.setBackground(null);
                groupRb.setBackground(null);
                conversationRb.setBackground(null);
                audioCallRb.setBackground(null);
                videoCallRb.setBackground(null);
                callsRb.setBackground(null);
            }
        }
    };

    private void logoutUser(View view) {
        CometChat.logout(new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                startActivity(new Intent(SelectActivity.this,MainActivity.class));
            }

            @Override
            public void onError(CometChatException e) {
                Snackbar.make(view,"Login Error:"+e.getCode(),Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CometChat.getLoggedInUser()==null)
        {
            startActivity(new Intent(SelectActivity.this,MainActivity.class));
        }
    }
}
