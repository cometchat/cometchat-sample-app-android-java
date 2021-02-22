package com.cometchat.pro.androiduikit.ComponentFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.uikit.ui_components.shared.cometchatUserPresence.CometChatUserPresence;

public class StatusIndicatorFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status_indicator, container, false);
        CometChatUserPresence statusIndicator = view.findViewById(R.id.statusIndicator);
        RadioGroup statusChangeGroup = view.findViewById(R.id.statusChange);
        statusIndicator.setUserStatus("online");
        statusChangeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.online)
                {
                    statusIndicator.setUserStatus("online");
                }
                else
                {
                    statusIndicator.setUserStatus("offline");
                }
            }
        });
        return view;
    }
}
