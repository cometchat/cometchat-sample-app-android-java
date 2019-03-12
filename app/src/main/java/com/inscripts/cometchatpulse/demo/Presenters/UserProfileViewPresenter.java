package com.inscripts.cometchatpulse.demo.Presenters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.Contracts.UserProfileViewActivityContract;
import com.inscripts.cometchatpulse.demo.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;


public class UserProfileViewPresenter extends Presenter<UserProfileViewActivityContract.UserProfileActivityView> implements
        UserProfileViewActivityContract.UserProfileActivityPresenter {


    private MessagesRequest messagesRequest=null;

    private List<MediaMessage> messageList=new ArrayList<>();

    @Override
    public void handleIntent(Intent data) {

        if (data.hasExtra(StringContract.IntentStrings.USER_ID)) {
            String uid = data.getStringExtra(StringContract.IntentStrings.USER_ID);
            getBaseView().setUserId(uid);

        }
        if (data.hasExtra(StringContract.IntentStrings.USER_NAME)) {
            String name = data.getStringExtra(StringContract.IntentStrings.USER_NAME);
            getBaseView().setTitle(name);
        }
        if (data.hasExtra(StringContract.IntentStrings.USER_AVATAR)) {
            getBaseView().setUserImage(data.getStringExtra(StringContract.IntentStrings.USER_AVATAR));
        }


    }

    @SuppressLint("CheckResult")
    @Override
    public void setContactAvatar(Context context, String avatar, ImageView userAvatar) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(context.getResources().getDrawable(R.drawable.ic_broken_image));
        Glide.with(context).load(avatar).apply(requestOptions).into(userAvatar);

    }


    @Override
    public void addUserPresenceListener(String presenceListener) {

        CometChat.addUserListener(presenceListener, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user) {
                if (isViewAttached())
                    getBaseView().setStatus(user);
            }

            @Override
            public void onUserOffline(User user) {
                if (isViewAttached())
                    getBaseView().setStatus(user);
            }
        });
    }

    @Override
    public void removeUserPresenceListener(String presenceListener) {
        CometChat.removeUserListener(presenceListener);
    }

    @Override
    public void sendCallRequest(Context context, String contactUid, String receiverType, String callTyp) {

        Call call=new Call(contactUid,receiverType,callTyp);
        CometChat.initiateCall(call, new CometChat.CallbackListener<Call>() {
            @Override
            public void onSuccess(Call call) {
                CommonUtils.startCallIntent(context,((User) call.getCallReceiver()),call.getType(),true,call.getSessionId());
            }

            @Override
            public void onError(CometChatException e) {

            }
        });
    }

    @Override
    public void getMediaMessage(String contactUid,int limit) {

        if (messagesRequest==null)
        {
            messagesRequest=new MessagesRequest.MessagesRequestBuilder().setUID(contactUid).setLimit(limit).build();

            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> baseMessages) {

                    for (BaseMessage message:baseMessages){

                        if (message instanceof MediaMessage){
                              if (message.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                                  messageList.add((MediaMessage) message);
                              }
                        }
                    }

                    getBaseView().setAdapter(messageList);
                }

                @Override
                public void onError(CometChatException e) {

                }
            });
        }
        else {
            messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
                @Override
                public void onSuccess(List<BaseMessage> baseMessages) {

                    for (BaseMessage message:baseMessages){

                        if (message instanceof MediaMessage){
                            if (message.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                                messageList.add((MediaMessage) message);
                            }
                        }
                    }

                    getBaseView().setAdapter(messageList);
                }

                @Override
                public void onError(CometChatException e) {

                }
            });
        }
    }
}
