package com.cometchat.javasampleapp.fragments.messages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.chat.models.TextMessage;
import com.cometchat.chatuikit.messageinformation.CometChatMessageInformation;
import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.constants.UIKitConstants;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;

public class MessageInformationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_information, container, false);

        CometChatMessageInformation messageInformation = view.findViewById(R.id.message_information);
        //It is necessary to set message object and Template before loading the component

        TextMessage textMessage=new TextMessage(CometChatUIKit.getLoggedInUser().getUid(),"Hey Jack,I am fine. How about you?", UIKitConstants.ReceiverType.USER);
        textMessage.setReadAt(System.currentTimeMillis()/100);
        textMessage.setReceiver(AppUtils.getDefaultUser());
        textMessage.setSender(CometChatUIKit.getLoggedInUser());
        messageInformation.setMessage(textMessage);
        messageInformation.setTemplate(CometChatUIKit.getDataSource().getTextTemplate());
        return view;
    }
}