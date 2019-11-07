package com.inscripts.cometchatpulse.demo.Presenters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.inscripts.cometchatpulse.demo.Activity.CallActivity;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.CallActivityContract;

import timber.log.Timber;

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
                Timber.d("onIncomingCallReceived: %s", call.toString());
                Toast.makeText(context, "onIncomingCallReceived", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOutgoingCallAccepted(Call call) {
                Timber.d("onOutgoingCallAccepted: %s", call.toString());
                Toast.makeText(context, "onOutgoingCallAccepted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOutgoingCallRejected(Call call) {
                Timber.d("onOutgoingCallRejected: %s", call.toString());
                Toast.makeText(context, "onOutgoingCallRejected", Toast.LENGTH_SHORT).show();
                ((CallActivity) context).finish();
            }

            @Override
            public void onIncomingCallCancelled(Call call) {
                Timber.d("onIncomingCallCancelled: %s", call.toString());
                Toast.makeText(context, "onIncomingCallCancelled", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
