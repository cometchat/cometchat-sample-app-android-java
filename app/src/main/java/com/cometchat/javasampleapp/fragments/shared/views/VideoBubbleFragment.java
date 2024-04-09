package com.cometchat.javasampleapp.fragments.shared.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.shared.views.CometChatVideoBubble.CometChatVideoBubble;
import com.cometchat.chatuikit.shared.views.CometChatVideoBubble.VideoBubbleStyle;
import com.cometchat.javasampleapp.R;


public class VideoBubbleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_bubble, container, false);

        CometChatVideoBubble videoBubble = view.findViewById(R.id.video_bubble);
        videoBubble.setStyle(new VideoBubbleStyle().setCornerRadius(18).setBackground(getResources().getColor(com.cometchat.chatuikit.R.color.cometchat_accent100)));
        videoBubble.setVideoUrl("https://data-us.cometchat.io/2379614bd4db65dd/media/1682517886_527585446_3e8e02fc506fa535eecfe0965e1a2024.mp4", R.drawable.ic_launcher_background);
        return view;
    }
}