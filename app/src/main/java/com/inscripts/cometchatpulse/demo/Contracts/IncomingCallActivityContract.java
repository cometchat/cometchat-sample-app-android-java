package com.inscripts.cometchatpulse.demo.Contracts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cometchat.pro.core.Call;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Base.BasePresenter;

public interface IncomingCallActivityContract {

    interface IncomingCallActivityView{

        void setUserImage(String stringExtra);

        void setUserName(String userName);

        void setCallType(boolean isVideoCall, boolean inComing);

        void setContactUserId(String userId);

        void setSessionId(String stringExtra);

        void handleCall(Call call);

    }

    interface IncomingCallActivityPresenter extends BasePresenter<IncomingCallActivityView> {

        void handleIntent(Intent intent, Context context);

        void setImage(String stringExtra, ImageView ivUserPic, ImageView ivUserBackground);

        void rejectCall(Context context, String sessionId,String callStatus);

        void answerCall(Camera camera, RelativeLayout callView, Context context, String sessionId);

        void removerMessageListener(Context context, String listenerId);

        void addMessageListener(RelativeLayout view, Context context, String listenerId);

        void addCallEventListener(String listenerId);

        void removeCallListener(String listenerId);
    }
}
