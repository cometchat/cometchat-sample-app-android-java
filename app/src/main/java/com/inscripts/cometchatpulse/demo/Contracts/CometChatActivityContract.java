package com.inscripts.cometchatpulse.demo.Contracts;

import android.content.Context;

import com.inscripts.cometchatpulse.demo.Base.BasePresenter;
import com.inscripts.cometchatpulse.demo.Base.BaseView;

public interface CometChatActivityContract {

    interface CometChatActivityView {

    }

    interface CometChatActivityPresenter extends BasePresenter<CometChatActivityView> {

        void addMessageListener(Context context,String listenerId);

        void removeMessageListener(String listenerId);

        void addCallEventListener(Context context,String listenerId);

        void removeCallEventListener(String tag);
    }
}
