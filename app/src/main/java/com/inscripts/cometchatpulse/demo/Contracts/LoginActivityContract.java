package com.inscripts.cometchatpulse.demo.Contracts;

import com.inscripts.cometchatpulse.demo.Base.BasePresenter;

public interface LoginActivityContract {

    interface LoginActivityView {

        void startCometChatActivity();
    }

    interface LoginActivityPresenter extends BasePresenter<LoginActivityView> {

        void Login(String uid);

        void loginCheck();
    }
}
