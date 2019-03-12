package com.inscripts.cometchatpulse.demo.Presenters;

import android.content.Context;
import android.util.Log;

import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Activity.CometChatActivity;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.CometChatActivityContract;
import com.inscripts.cometchatpulse.demo.Utils.CommonUtils;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.BaseMessage;

public class CometChatActivityPresenter extends Presenter<CometChatActivityContract.CometChatActivityView>
implements CometChatActivityContract.CometChatActivityPresenter {


    private static final String TAG = "CometChatActivityPresen";

    @Override
    public void addMessageListener(final Context context, String listnerId) {

        CometChat.addMessageListener(listnerId, new CometChat.MessageListener() {

            @Override
            public void onTextMessageReceived(TextMessage message) {


            }

            @Override
            public void onMediaMessageReceived(MediaMessage message) {


            }
        });
    }

    @Override
    public void removeMessageListener(String listenerId) {
        CometChat.removeMessageListener(listenerId);
    }

    @Override
    public void addCallEventListener(Context context,String listenerId) {
        CometChat.addCallListener(listenerId, new CometChat.CallListener() {
            @Override
            public void onIncomingCallReceived(Call call) {

                Log.d(TAG, "onIncomingCallReceived: "+call.toString());

                        if (call.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {

                            CommonUtils.startCallIntent(context, (User) call.getCallInitiator(), call.getType(),
                                    false, call.getSessionId());
                        } else if (call.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {

                            CommonUtils.startCallIntent(context, (Group) call.getCallReceiver(), call.getType(),
                                    false, call.getSessionId());
                        }
            }

            @Override
            public void onOutgoingCallAccepted(Call call) {

                Log.d(TAG, "onOutgoingCallAccepted: "+call.toString());
            }

            @Override
            public void onOutgoingCallRejected(Call call) {

                Log.d(TAG, "onOutgoingCallRejected: "+call.toString());
            }

            @Override
            public void onIncomingCallCancelled(Call call) {

                Log.d(TAG, "onIncomingCallCancelled: "+call.toString());
            }

        });
    }

    @Override
    public void removeCallEventListener(String tag) {
        CometChat.removeCallListener(tag);
    }
}
