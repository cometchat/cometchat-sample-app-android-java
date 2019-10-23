package com.inscripts.cometchatpulse.demo;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.helpers.Logger;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;

import timber.log.Timber;


public class CometApplication extends Application {

    private static final String TAG = "CometApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        AppSettings appSettings=new AppSettings.AppSettingsBuilder().subscribePresenceForAllUsers().setRegion(StringContract.AppDetails.REGION).build();
        CometChat.init(this,StringContract.AppDetails.APP_ID,appSettings,new CometChat.CallbackListener<String>() {

            @Override
            public void onSuccess(String s) {
                Toast.makeText(CometApplication.this, "SetUp Complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(CometApplication.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Timber.d("onError: %s", e.getMessage());
            }

        });

    }
}
