package com.inscripts.cometchatpulse.demo;

import android.app.Instrumentation;
import android.content.Context;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Test
    public void login(){

        Context appContext = InstrumentationRegistry.getTargetContext();
        CometChat.init(appContext, "337beda0759d2", new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                System.out.println(s);

            }

            @Override
            public void onError(CometChatException e) {
                System.out.println(e.getMessage());
            }
        });

    }

    @Test
    public void login1(){

        CometChat.login("superhero2", "083e6894e7cd4b75348a607f254166b1f41462ef", new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                System.out.println(user.toString());
            }

            @Override
            public void onError(CometChatException e) {
                System.out.println(e.getMessage());
            }
        });
    }




}
