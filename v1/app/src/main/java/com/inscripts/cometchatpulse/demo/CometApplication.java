package com.inscripts.cometchatpulse.demo;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;


public class CometApplication extends Application {

    private static final String TAG = "CometApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        CometChat.init(this,StringContract.AppDetails.APP_ID,new CometChat.CallbackListener<String>() {

            @Override
            public void onSuccess(String s) {
                Toast.makeText(CometApplication.this, "SetUp Complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(CometApplication.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onError: "+e.getMessage());
            }

        });

    }
}
