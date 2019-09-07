package com.inscripts.cometchatpulse.demo.Contracts;

import android.content.Context;

import com.inscripts.cometchatpulse.demo.Base.BasePresenter;

public interface LoginActivityContract {

    interface LoginActivityView {

        void startCometChatActivity();
    }

    interface LoginActivityPresenter extends BasePresenter<LoginActivityView> {

        void Login(Context context,String uid);

        void loginCheck();
    }
}
