package com.cometchat.javasampleapp.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.cometchatuikit.UIKitSettings;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.javasampleapp.AppConstants;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.Application;
import com.cometchat.javasampleapp.R;
import com.cometchat.chat.models.User;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    private MaterialCardView superhero1;

    private MaterialCardView superhero2;

    private MaterialCardView superhero3;

    private MaterialCardView superhero4;

    private AppCompatImageView ivLogo;

    private LinearLayout parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentView=findViewById(R.id.parent_view);
        Utils.setStatusBarColor(this, getResources().getColor(android.R.color.white));
        UIKitSettings uiKitSettings = new UIKitSettings.UIKitSettingsBuilder().setRegion(AppConstants.REGION).setAppId(AppConstants.APP_ID).setAuthKey(AppConstants.AUTH_KEY).subscribePresenceForAllUsers().build();
        CometChatUIKit.init(this, uiKitSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (CometChatUIKit.getLoggedInUser() != null) {
                    Application.addCallListener(MainActivity.this);
                    AppUtils.fetchDefaultObjects();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                }
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        superhero1 = findViewById(R.id.superhero1);
        superhero2 = findViewById(R.id.superhero2);
        superhero3 = findViewById(R.id.superhero3);
        superhero4 = findViewById(R.id.superhero4);
        ivLogo = findViewById(R.id.ivLogo);
        findViewById(R.id.login).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));

        superhero1.setOnClickListener(view -> {
            findViewById(R.id.superhero1Progressbar).setVisibility(View.VISIBLE);
            login("superhero1");
        });
        superhero2.setOnClickListener(view -> {
            findViewById(R.id.superhero2Progressbar).setVisibility(View.VISIBLE);
            login("superhero2");
        });
        superhero3.setOnClickListener(view -> {
            findViewById(R.id.superhero3Progressbar).setVisibility(View.VISIBLE);
            login("superhero3");
        });
        superhero4.setOnClickListener(view -> {
            findViewById(R.id.superhero4Progressbar).setVisibility(View.VISIBLE);
            login("superhero4");
        });

        if (Utils.isDarkMode(this)) {
            ivLogo.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
            ivLogo.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryTextColor)));
        }
        setUpUI();
    }

    private void login(String uid) {
        CometChatUIKit.login(uid, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                Application.addCallListener(MainActivity.this);
                AppUtils.fetchDefaultObjects();
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpUI() {
        if(AppUtils.isNightMode(this)){
            Utils.setStatusBarColor(this, ContextCompat.getColor(this,R.color.app_background_dark));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.app_background_dark)));
        }else {
            Utils.setStatusBarColor(this, getResources().getColor(R.color.app_background));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
        }
    }

    public void createUser(View view) {
        startActivity(new Intent(this, CreateUserActivity.class));
    }
}