package com.cometchat.pro.javasampleapp.fragments.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.messagecomposer.CometChatMessageComposer;
import com.cometchat.pro.javasampleapp.R;
import com.cometchat.pro.javasampleapp.AppUtils;


public class MessageComposerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_composer, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        CometChatMessageComposer cometChatMessageComposer = view.findViewById(R.id.composer);
        cometChatMessageComposer.setUser(AppUtils.getDefaultUser());

        return view;
    }
}