package com.cometchat.javasampleapp.fragments.shared.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.views.mediarecorder.CometChatMediaRecorder;
import com.cometchat.chatuikit.shared.views.mediarecorder.MediaRecorderStyle;
import com.cometchat.javasampleapp.R;

public class MediaRecorderFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media_recorder, container, false);
        CometChatMediaRecorder mediaRecorder = view.findViewById(R.id.recorder);
        CometChatTheme cometChatTheme = CometChatTheme.getInstance(getContext());
        mediaRecorder.setStyle(new MediaRecorderStyle()
                .setBackground(cometChatTheme.getPalette().getBackground())
                .setBackground(cometChatTheme.getPalette().getBackground())
                .setRecordedContainerColor(cometChatTheme.getPalette().getAccent100())
                .setPlayIconTint(cometChatTheme.getPalette().getAccent())
                .setPauseIconTint(cometChatTheme.getPalette().getAccent())
                .setStopIconTint(cometChatTheme.getPalette().getError())
                .setVoiceRecordingIconTint(cometChatTheme.getPalette().getError())
                .setRecordingChunkColor(cometChatTheme.getPalette().getPrimary())
                .setTimerTextColor(cometChatTheme.getPalette().getAccent())
                .setTimerTextAppearance(cometChatTheme.getTypography().getText1()));
        mediaRecorder.setCardElevation(10);
        mediaRecorder.setRadius(16);
        return view;
    }
}