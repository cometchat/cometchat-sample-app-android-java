package com.cometchat.pro.javasampleapp.fragments.shared.secondary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.cometchat.pro.javasampleapp.R;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStatusIndicator.CometChatStatusIndicator;
import com.google.android.material.textfield.TextInputLayout;

public class StatusIndicatorFragment extends Fragment {
    private TextInputLayout borderWidthLayout, cornerRadiusLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status_indicator, container, false);
        CometChatStatusIndicator statusIndicator = view.findViewById(R.id.statusIndicator);
        RadioGroup statusChangeGroup = view.findViewById(R.id.toggle);
        statusIndicator.status(CometChatStatusIndicator.STATUS.ONLINE);
        statusChangeGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.online) {
                statusIndicator.status(CometChatStatusIndicator.STATUS.ONLINE);
            } else if (i == R.id.offline) {
                statusIndicator.status(CometChatStatusIndicator.STATUS.OFFLINE);
                statusIndicator.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

}
