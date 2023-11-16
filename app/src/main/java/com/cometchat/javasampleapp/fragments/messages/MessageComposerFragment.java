package com.cometchat.javasampleapp.fragments.messages;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.messagecomposer.CometChatMessageComposer;
import com.cometchat.javasampleapp.R;
import com.cometchat.javasampleapp.AppUtils;


public class MessageComposerFragment extends Fragment {
    private RelativeLayout parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_composer, container, false);
        parentView = view.findViewById(R.id.parent_view);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        CometChatMessageComposer cometChatMessageComposer = view.findViewById(R.id.composer);
        cometChatMessageComposer.setUser(AppUtils.getDefaultUser());
        setUpUI(view);
        return view;
    }

    private void setUpUI(View view) {
        if (AppUtils.isNightMode(getContext())) {
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.app_background_dark)));
        } else {
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
        }
    }
}