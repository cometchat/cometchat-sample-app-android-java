package com.cometchat.pro.plutouikitsampleapp.fragments.chats;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.pro.plutouikitsampleapp.R;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatConversationList.CometChatConversationListItem;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStatusIndicator.CometChatStatusIndicator;

public class ConversationListItemFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_conversation_list_item, container, false);
        CometChatConversationListItem conversationListItem=view.findViewById(R.id.conversationListItem);
        conversationListItem.setAvatar(getActivity().getDrawable(R.drawable.logo));
        conversationListItem.setTitle("CometChat");
        conversationListItem.setSubTitle("You got new messages");
        conversationListItem.setUnreadCount(50);
        conversationListItem.setStatusIndicator(CometChatStatusIndicator.STATUS.ONLINE);
        conversationListItem.setTime(System.currentTimeMillis()/1000);
        return view;
    }
}