package com.cometchat.pro.androiduikit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.cometchat.pro.androiduikit.constants.AppConfig;
import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.uikit.ui_settings.UIKitSettings;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppSettings appSettings = new AppSettings.AppSettingsBuilder().
                subscribePresenceForAllUsers().setRegion(AppConfig.AppDetails.REGION).build();
        CometChat.init(this, AppConfig.AppDetails.APP_ID, appSettings,
                new CometChat.CallbackListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (CometChat.getLoggedInUser()!=null)
                                    startActivity(new Intent(SplashActivity.this,SelectActivity.class));
                                else
                                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                                overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);
                            }
                        },2000);
                        UIKitSettings.setAppID(AppConfig.AppDetails.APP_ID);
                        UIKitSettings.setAuthKey(AppConfig.AppDetails.AUTH_KEY);
                        CometChat.setSource("ui-kit","android","java");
                        UIKitApplication.initListener(SplashActivity.this);
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}