package com.cometchat.javasampleapp.fragments.shared.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.AudioPlayer;
import com.cometchat.chatuikit.shared.views.CometChatAudioBubble.AudioBubbleStyle;
import com.cometchat.chatuikit.shared.views.CometChatAudioBubble.CometChatAudioBubble;
import com.cometchat.javasampleapp.R;

public class AudioBubbleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio_bubble_fragement, container, false);
        CometChatAudioBubble cometChatAudioBubble = view.findViewById(R.id.audio_bubble);
        cometChatAudioBubble.setAudioUrl("https://data-us.cometchat.io/2379614bd4db65dd/media/1682517916_1406731591_130612180fb2e657699814eb52817574.mp3", "SoundHelix", "Song");
        cometChatAudioBubble.setStyle(new AudioBubbleStyle().setBackground(CometChatTheme.getInstance().getPalette().getAccent100(getContext())).setCornerRadius(18));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AudioPlayer.getAudioPlayer().stopPlayer();
    }
}