package com.cometchat.javasampleapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.cometchatuikit.UIKitSettings;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.javasampleapp.AppConstants;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.BuildConfig;
import com.cometchat.javasampleapp.R;
import com.cometchat.chat.models.User;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MaterialCardView user1;

    private MaterialCardView user2;

    private MaterialCardView user3;

    private MaterialCardView user4;

    private AppCompatImageView ivLogo;
    private ProgressBar progressBar;
    private AppCompatTextView tvCometChat;
    private LinearLayout parentView;
    private LinearLayout gridLayoutContainer;
    private TextView stateMessage;
    private LinearLayout stateLayout;
    private TextView user1Name, user2Name, user3Name, user4Name;
    private ImageView user1Avatar, user2Avatar, user3Avatar, user4Avatar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentView = findViewById(R.id.parent_view);
        progressBar = findViewById(R.id.progress_bar);
        stateMessage = findViewById(R.id.state_message);
        stateLayout = findViewById(R.id.state_layout);
        gridLayoutContainer = findViewById(R.id.grid_layout_container);
        user1 = findViewById(R.id.user1);
        user2 = findViewById(R.id.user2);
        user3 = findViewById(R.id.user3);
        user4 = findViewById(R.id.user4);
        ivLogo = findViewById(R.id.ivLogo);
        tvCometChat = findViewById(R.id.tvComet);
        user1Name = findViewById(R.id.user1_name);
        user2Name = findViewById(R.id.user2_name);
        user3Name = findViewById(R.id.user3_name);
        user4Name = findViewById(R.id.user4_name);
        user1Avatar = findViewById(R.id.user1_avatar_image);
        user2Avatar = findViewById(R.id.user2_avatar_image);
        user3Avatar = findViewById(R.id.user3_avatar_image);
        user4Avatar = findViewById(R.id.user4_avatar_image);

        user1.setVisibility(View.GONE);
        user2.setVisibility(View.GONE);
        user3.setVisibility(View.GONE);
        user4.setVisibility(View.GONE);

        gridLayoutContainer.setVisibility(View.INVISIBLE);
        stateMessage.setText(R.string.please_wait);
        progressBar.setVisibility(View.VISIBLE);
        Utils.setStatusBarColor(this, getResources().getColor(android.R.color.white));
        UIKitSettings uiKitSettings = new UIKitSettings.UIKitSettingsBuilder().setRegion(AppConstants.REGION).setAppId(AppConstants.APP_ID).setAuthKey(AppConstants.AUTH_KEY).subscribePresenceForAllUsers().build();
        CometChatUIKit.init(this, uiKitSettings, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                CometChat.setDemoMetaInfo(getAppMetadata());
                if (CometChatUIKit.getLoggedInUser() != null) {
                    AppUtils.fetchDefaultObjects();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                } else {
                    AppUtils.fetchSampleUsers(new CometChat.CallbackListener<List<User>>() {
                        @Override
                        public void onSuccess(List<User> users) {
                            if (!users.isEmpty()) {
                                setUsers(users);
                            } else {
                                stateLayout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                stateMessage.setText(R.string.no_sample_users_available);
                            }
                        }

                        @Override
                        public void onError(CometChatException e) {
                            setUsers(AppUtils.processSampleUserList(AppUtils.loadJSONFromAsset(MainActivity.this)));
                        }
                    });
                }
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.login).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));

        user1.setOnClickListener(view -> {
            findViewById(R.id.user1Progressbar).setVisibility(View.VISIBLE);
            login(user1.getTag().toString());
        });
        user2.setOnClickListener(view -> {
            findViewById(R.id.user2Progressbar).setVisibility(View.VISIBLE);
            login(user2.getTag().toString());
        });
        user3.setOnClickListener(view -> {
            findViewById(R.id.user3Progressbar).setVisibility(View.VISIBLE);
            login(user3.getTag().toString());
        });
        user4.setOnClickListener(view -> {
            findViewById(R.id.user4Progressbar).setVisibility(View.VISIBLE);
            login(user4.getTag().toString());
        });

        if (Utils.isDarkMode(this)) {
            ivLogo.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        } else {
            ivLogo.setImageTintList(ColorStateList.valueOf(getResources().getColor(com.cometchat.chatuikit.R.color.cometchat_primary_text_color)));
        }
        setUpUI();
    }

    private void setUsers(List<User> users) {
        progressBar.setVisibility(View.GONE);
        stateLayout.setVisibility(View.GONE);
        gridLayoutContainer.setVisibility(View.VISIBLE);
        for (int i = 0; i < users.size(); i++) {
            if (i == 0) {
                if (isDestroyed() || isFinishing()) break;
                user1Name.setText(users.get(i).getName());
                Glide.with(getApplicationContext()).load(users.get(i).getAvatar()).error(R.drawable.ironman).into(user1Avatar);
                user1.setTag(users.get(i).getUid());
                user1.setVisibility(View.VISIBLE);
            } else if (i == 1) {
                if (isDestroyed() || isFinishing()) break;
                user2Name.setText(users.get(i).getName());
                Glide.with(getApplicationContext()).load(users.get(i).getAvatar()).error(R.drawable.captainamerica).into(user2Avatar);
                user2.setTag(users.get(i).getUid());
                user2.setVisibility(View.VISIBLE);
            } else if (i == 2) {
                if (isDestroyed() || isFinishing()) break;
                user3Name.setText(users.get(i).getName());
                Glide.with(getApplicationContext()).load(users.get(i).getAvatar()).error(R.drawable.spiderman).into(user3Avatar);
                user3.setTag(users.get(i).getUid());
                user3.setVisibility(View.VISIBLE);
            } else if (i == 3) {
                if (isDestroyed() || isFinishing()) break;
                user4Name.setText(users.get(i).getName());
                Glide.with(getApplicationContext()).load(users.get(i).getAvatar()).error(R.drawable.wolverine).into(user4Avatar);
                user4.setTag(users.get(i).getUid());
                user4.setVisibility(View.VISIBLE);
            }
        }
    }

    private void login(String uid) {
        CometChatUIKit.login(uid, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
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
        if (AppUtils.isNightMode(this)) {
            Utils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.app_background_dark));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.app_background_dark)));
            tvCometChat.setTextColor(getResources().getColor(R.color.app_background));
        } else {
            Utils.setStatusBarColor(this, getResources().getColor(R.color.app_background));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
            tvCometChat.setTextColor(getResources().getColor(R.color.app_background_dark));
        }
    }

    private JSONObject getAppMetadata() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", getResources().getString(R.string.app_name));
            jsonObject.put("type", "sample");
            jsonObject.put("version", BuildConfig.VERSION_NAME);
            jsonObject.put("bundle", BuildConfig.APPLICATION_ID);
            jsonObject.put("platform", "android");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void createUser(View view) {
        startActivity(new Intent(this, CreateUserActivity.class));
    }
}