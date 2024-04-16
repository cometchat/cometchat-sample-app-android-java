package com.cometchat.javasampleapp.fragments.shared.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.views.CometChatTextBubble.CometChatTextBubble;
import com.cometchat.chatuikit.shared.views.CometChatTextBubble.TextBubbleStyle;
import com.cometchat.javasampleapp.R;


public class TextBubbleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_bubble, container, false);
        CometChatTheme cometChatTheme = CometChatTheme.getInstance();
        CometChatTextBubble receiverBubble = view.findViewById(R.id.receiver_bubble);
        receiverBubble.setText("Hi John, How are you?");
        receiverBubble.setStyle(new TextBubbleStyle().setBackground(cometChatTheme.getPalette().getAccent100(getContext())).setTextColor(cometChatTheme.getPalette().getAccent(getContext())).setCornerRadius(18));

        CometChatTextBubble senderBubble = view.findViewById(R.id.sender_bubble);
        senderBubble.setText("Hey Jack,I am fine. How about you?");
        senderBubble.setStyle(new TextBubbleStyle().setBackground(cometChatTheme.getPalette().getPrimary(getContext())).setTextColor(getResources().getColor(R.color.white)).setCornerRadius(18));

        return view;
    }
}