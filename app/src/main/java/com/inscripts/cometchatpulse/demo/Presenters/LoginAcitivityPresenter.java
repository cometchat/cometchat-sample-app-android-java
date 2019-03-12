package com.inscripts.cometchatpulse.demo.Presenters;

import com.cometchat.pro.models.Group;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.LoginActivityContract;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;

public class LoginAcitivityPresenter extends Presenter<LoginActivityContract.LoginActivityView> implements
LoginActivityContract.LoginActivityPresenter{


    @Override
    public void Login(String uid) {


        CometChat.login(uid, StringContract.AppDetails.API_KEY, new CometChat.CallbackListener<User>() {

            @Override
            public void onSuccess(User user) {
                if (isViewAttached())
                    getBaseView().startCometChatActivity();
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }

        });
    }
    @Override
    public void loginCheck() {

        try {
            if (CometChat.getLoggedInUser()!=null)
            {    if (isViewAttached())
               getBaseView().startCometChatActivity();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
