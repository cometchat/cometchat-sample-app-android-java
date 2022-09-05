package com.cometchat.pro.plutouikitsampleapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.plutouikitsampleapp.R;
import com.cometchat.pro.plutouikitsampleapp.constants.StringConstants;
import com.cometchatworkspace.resources.utils.Utils;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Utils.setStatusBarColor(this, getResources().getColor(R.color.app_background));

        findViewById(R.id.chats).setOnClickListener(view -> handleIntent(StringConstants.CONVERSATIONS));
        findViewById(R.id.users).setOnClickListener(view -> handleIntent(StringConstants.USERS));
        findViewById(R.id.groups).setOnClickListener(view -> handleIntent(StringConstants.GROUPS));
        findViewById(R.id.messages).setOnClickListener(view -> handleIntent(StringConstants.MESSAGES));
        findViewById(R.id.shared).setOnClickListener(view -> handleIntent(StringConstants.SHARED));

        findViewById(R.id.logout).setOnClickListener(view -> CometChat.logout(new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finishAffinity();
            }

            @Override
            public void onError(CometChatException e) {

            }
        }));
    }

    private void handleIntent(String module) {
        Intent intent = new Intent(this, ComponentListActivity.class);
        intent.putExtra(StringConstants.MODULE, module);
        startActivity(intent);
    }
}