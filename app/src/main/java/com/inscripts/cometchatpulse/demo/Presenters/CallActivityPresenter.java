package com.inscripts.cometchatpulse.demo.Presenters;

import android.content.Context;
import android.util.Log;

import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.inscripts.cometchatpulse.demo.Activity.CallActivity;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.CallActivityContract;

public class CallActivityPresenter extends Presenter<CallActivityContract.CallActivityView> implements
        CallActivityContract.CallActivityPresenter {

    private static final String TAG = "CallActivityPresenter";

    @Override
    public void removeCallListener(String listener) {
        CometChat.removeCallListener(listener);
    }

    @Override
    public void addCallListener(Context context, String listener) {

        CometChat.addCallListener(listener,new CometChat.CallListener(){

            @Override
            public void onIncomingCallReceived(Call call) {
                Log.d(TAG, "onIncomingCallReceived: "+call.toString());
            }

            @Override
            public void onOutgoingCallAccepted(Call call) {
                Log.d(TAG, "onOutgoingCallAccepted: "+call.toString());
            }

            @Override
            public void onOutgoingCallRejected(Call call) {
                Log.d(TAG, "onOutgoingCallRejected: "+call.toString());

                  ((CallActivity) context).finish();
            }

            @Override
            public void onIncomingCallCancelled(Call call) {
                Log.d(TAG, "onIncomingCallCancelled: "+call.toString());

            }
        });
    }

}
