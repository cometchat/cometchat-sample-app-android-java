package com.inscripts.cometchatpulse.demo.Contracts;

import android.content.Context;

import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Base.BasePresenter;

import java.util.HashMap;
import java.util.List;


public interface RecentsContract {

    interface RecentsView{

        void setRecentAdapter(List<Conversation> conversationList);

        void updateUnreadCount(Conversation conversation);

        void setLastMessage(Conversation conversation);

        void refreshConversation(BaseMessage message);

        void clearConversations();
    }

    interface RecentsPresenter extends BasePresenter<RecentsView> {

          void fetchConversations(Context context);

          void addMessageListener(String presenceListener);

          void removeMessageListener(String string);

//          void searchConversation(String s);

          void updateConversation();

        void refreshConversations(Context context);
    }
}
