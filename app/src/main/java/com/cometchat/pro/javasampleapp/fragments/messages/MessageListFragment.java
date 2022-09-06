package com.cometchat.pro.javasampleapp.fragments.messages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.javasampleapp.R;
import com.cometchatworkspace.components.messages.message_list.CometChatMessageList;

public class MessageListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_message_list, container, false);
        CometChatMessageList messageList=view.findViewById(R.id.messageList);
        messageList.uid("superhero5");
        messageList.type(CometChatConstants.RECEIVER_TYPE_USER);
        return view;
    }
}