package com.inscripts.cometchatpulse.demo.Contracts;

import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;

import com.cometchat.pro.models.MessageReceipt;
import com.cometchat.pro.models.TypingIndicator;
import com.inscripts.cometchatpulse.demo.Activity.OneToOneChatActivity;
import com.inscripts.cometchatpulse.demo.Base.BasePresenter;
import com.inscripts.cometchatpulse.demo.Base.BaseView;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import  com.cometchat.pro.models.BaseMessage;
import  com.cometchat.pro.models.User;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public interface OneToOneActivityContract {



    interface OneToOneView extends BaseView {

        void setAdapter(List<BaseMessage> messageArrayList);

        void addMessage(BaseMessage newMessage);

        void setOwnerDetail(User user);

        void setTitle(String name);

        void addSendMessage(BaseMessage baseMessage );

        void setContactUid(String stringExtra);

        void setAvatar(String stringExtra);

        void setPresence(User user);

        void setTyping();

        void endTyping();

        void setMessageDelivered(MessageReceipt messageReceipt);
    }

    interface OneToOnePresenter extends BasePresenter<OneToOneView> {

        void sendMessage(String message,String uId);

        void setContext(Context context);

        void handleIntent(Intent intent);

        void addMessageReceiveListener(String contactUid);

        void sendMediaMessage(File filepath, String receiverUid, String type);

        void fetchPreviousMessage(String contactUid,int limit);

        void getOwnerDetail();

        void removeMessageLisenter(String listenerId);

        void setContactPic(OneToOneChatActivity oneToOneChatActivity, String avatar, CircleImageView circleImageView);

        void addPresenceListener(String presenceListener);

        void sendCallRequest(Context context,String contactUid, String receiverTypeUser, String callType);

        void addCallEventListener(String callEventListener);

        void removePresenceListener(String listenerId);

        void removeCallListener(String listenerId);

        void sendTypingIndicator(String receiverId);

        void endTypingIndicator(String receiverId);
    }
}
