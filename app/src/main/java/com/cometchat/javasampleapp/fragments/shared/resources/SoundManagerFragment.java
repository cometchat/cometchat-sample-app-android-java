package com.cometchat.javasampleapp.fragments.shared.resources;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cometchat.chatuikit.shared.resources.soundManager.CometChatSoundManager;
import com.cometchat.chatuikit.shared.resources.soundManager.Sound;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;


public class SoundManagerFragment extends Fragment {

    private LinearLayout parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sound_manager, container, false);
        parentView = view.findViewById(R.id.parent_view);
        setUpUI(view);
        CometChatSoundManager soundManager = new CometChatSoundManager(getActivity());
        view.findViewById(R.id.playIncoming).setOnClickListener(view1 -> {
            soundManager.play(Sound.incomingMessage); //To play incoming Messages sound
        });
        view.findViewById(R.id.playOutgoing).setOnClickListener(view12 -> {
            soundManager.play(Sound.outgoingMessage);//To play outgoing Messages sound
        });
        return view;
    }

    private void setUpUI(View view) {
        if (AppUtils.isNightMode(getContext())) {
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.soundManager_title));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.soundManager_description));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.incoming_message_text));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.outgoing_message_text));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.app_background_dark)));
        } else {
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.soundManager_title));
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.soundManager_description));
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.incoming_message_text));
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.outgoing_message_text));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
        }
    }


}