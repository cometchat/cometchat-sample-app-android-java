package com.cometchat.javasampleapp.fragments.calls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.calls.callbutton.CometChatCallButtons;
import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.chatuikit.shared.views.button.ButtonStyle;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;

public class CallButtonFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_button, container, false);

        CometChatCallButtons cometChatCallButton = view.findViewById(R.id.call_button);
        cometChatCallButton.getVideoCallButton().hideButtonBackground(true);
        cometChatCallButton.getVoiceCallButton().hideButtonBackground(true);
        cometChatCallButton.setVideoCallIcon(com.cometchat.chatuikit.R.drawable.cometchat_video_icon);
        cometChatCallButton.setVoiceCallIcon(com.cometchat.chatuikit.R.drawable.cometchat_call_icon);
        cometChatCallButton.hideButtonText(false);
        cometChatCallButton.setVideoButtonText("video call");
        cometChatCallButton.setVoiceButtonText("voice call");
        cometChatCallButton.setMarginForButtons(Utils.convertDpToPx(getContext(), 1));
        cometChatCallButton.setButtonStyle(new ButtonStyle().setButtonSize(Utils.convertDpToPx(getContext(), 25), Utils.convertDpToPx(getContext(), 25)).setButtonIconTint(CometChatTheme.getInstance().getPalette().getPrimary(getContext())));
        cometChatCallButton.setUser(AppUtils.getDefaultUser());
        return view;
    }
}