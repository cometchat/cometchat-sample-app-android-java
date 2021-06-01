package com.cometchat.pro.uikit.ui_components.shared.cometchatConversations;

import android.content.Context;
import android.util.Log;

import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.MessageReceipt;

import java.util.List;

public class CometChatConversationsViewModel {

    private  Context context;

    private CometChatConversationsAdapter conversationListAdapter;

    private CometChatConversationsViewModel(){

    }
    public CometChatConversationsViewModel(Context context, CometChatConversations cometChatConversationList){
        this.context=context;
        setAdapter(cometChatConversationList);
    }

    private CometChatConversationsAdapter getAdapter(){
       if (conversationListAdapter==null){
           conversationListAdapter=new CometChatConversationsAdapter(context);
       }
       return conversationListAdapter;
    }

    public void add(Conversation conversation){
        if (conversationListAdapter!=null)
            conversationListAdapter.add(conversation);
    }

    private void setAdapter(CometChatConversations cometChatConversationList){
        if (conversationListAdapter==null)
           conversationListAdapter=new CometChatConversationsAdapter(context);
        cometChatConversationList.setAdapter(conversationListAdapter);
    }


    public void setConversationList(List<Conversation> conversationList) {
        if (conversationListAdapter!=null) {
                conversationListAdapter.updateList(conversationList);
        }
        else
        {
            Log.e("ERROR", "setConversationList: ERROR " );
        }
    }


    public void update(Conversation conversation) {
        if (conversationListAdapter!=null)
            conversationListAdapter.update(conversation);
    }

    public void remove(Conversation conversation) {
        if (conversationListAdapter!=null)
            conversationListAdapter.remove(conversation);
    }

    public void searchConversation(String searchString) {
        if (conversationListAdapter!=null)
            conversationListAdapter.getFilter().filter(searchString);
    }

    public void setDeliveredReceipts(MessageReceipt messageReceipt) {
        if (conversationListAdapter!=null)
            conversationListAdapter.setDeliveredReceipts(messageReceipt);
    }

    public void setReadReceipts(MessageReceipt messageReceipt) {
        if (conversationListAdapter!=null)
            conversationListAdapter.setReadReceipts(messageReceipt);
    }

    public void clear() {
        if (conversationListAdapter!=null)
            conversationListAdapter.resetAdapterList();
    }

    public Conversation getConversation(int position) {
        Conversation conversation = null;
        if (conversationListAdapter!=null)
            conversation = conversationListAdapter.getItemAtPosition(position);
        return conversation;
    }
    public int size() {
        return conversationListAdapter.getItemCount();
    }
}