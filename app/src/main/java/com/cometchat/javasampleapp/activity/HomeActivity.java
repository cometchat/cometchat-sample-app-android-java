package com.cometchat.javasampleapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;
import com.cometchat.javasampleapp.constants.StringConstants;

public class HomeActivity extends AppCompatActivity {

    private ImageView darkMode, lightMode,logout;
    private LinearLayout parentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        darkMode = findViewById(R.id.dark_mode);
        lightMode = findViewById(R.id.light_mode);
        parentView = findViewById(R.id.parent_view);
        logout = findViewById(R.id.logout);
        setUpUI();
        findViewById(R.id.chats).setOnClickListener(view -> handleIntent(StringConstants.CONVERSATIONS));
        findViewById(R.id.users).setOnClickListener(view -> handleIntent(StringConstants.USERS));
        findViewById(R.id.groups).setOnClickListener(view -> handleIntent(StringConstants.GROUPS));
        findViewById(R.id.messages).setOnClickListener(view -> handleIntent(StringConstants.MESSAGES));
        findViewById(R.id.shared).setOnClickListener(view -> handleIntent(StringConstants.SHARED));
        findViewById(R.id.calls).setOnClickListener(view -> handleIntent(StringConstants.CALLS));

        logout.setOnClickListener(view -> CometChatUIKit.logout(new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finishAffinity();
            }

            @Override
            public void onError(CometChatException e) {

            }
        }));

        darkMode.setOnClickListener(view -> toggleDarkMode());

        lightMode.setOnClickListener(view -> toggleDarkMode());
    }

    private void setUpUI() {
        if(AppUtils.isNightMode(this)){
            AppUtils.changeIconTintToWhite(this,darkMode);
            AppUtils.changeIconTintToWhite(this,lightMode);
            AppUtils.changeIconTintToWhite(this,logout);
            Utils.setStatusBarColor(this, ContextCompat.getColor(this,R.color.app_background_dark));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.app_background_dark)));
            darkMode.setVisibility(View.GONE);
            lightMode.setVisibility(View.VISIBLE);
        }else {
            AppUtils.changeIconTintToBlack(this,darkMode);
            AppUtils.changeIconTintToBlack(this,lightMode);
            AppUtils.changeIconTintToBlack(this,logout);
            Utils.setStatusBarColor(this, getResources().getColor(R.color.app_background));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
            darkMode.setVisibility(View.VISIBLE);
            lightMode.setVisibility(View.GONE);
        }
    }

    private void toggleDarkMode() {
        if (AppUtils.isNightMode(this)) {
            Palette.getInstance(this).mode(CometChatTheme.MODE.LIGHT);
            AppUtils.switchLightMode();
        } else {
            Palette.getInstance(this).mode(CometChatTheme.MODE.DARK);
            AppUtils.switchDarkMode();
        }
    }

    private void handleIntent(String module) {
        Intent intent = new Intent(this, ComponentListActivity.class);
        intent.putExtra(StringConstants.MODULE, module);
        startActivity(intent);
    }
}