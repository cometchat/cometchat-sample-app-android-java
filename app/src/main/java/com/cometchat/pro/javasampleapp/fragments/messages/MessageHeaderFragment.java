package com.cometchat.pro.javasampleapp.fragments.messages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.javasampleapp.R;
import com.cometchatworkspace.components.messages.header.CometChatMessagesHeader;

public class MessageHeaderFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_message_header, container, false);
        CometChatMessagesHeader messagesHeader=view.findViewById(R.id.messageHeader);

        //set user Object to the MessageHeader
        messagesHeader.user(CometChat.getLoggedInUser());

        //handle back press
        messagesHeader.addListener("MessageHeaderFragment", new CometChatMessagesHeader.OnEventListener() {
            @Override
            public void onBackPressed() {
                getActivity().onBackPressed();
            }
        });
        return view;
    }
}