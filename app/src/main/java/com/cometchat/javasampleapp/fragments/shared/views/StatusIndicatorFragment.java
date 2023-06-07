package com.cometchat.javasampleapp.fragments.shared.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.CometChatStatusIndicator;
import com.cometchat.javasampleapp.R;
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
        statusIndicator.setBackgroundColor(getResources().getColor(com.cometchat.chatuikit.R.color.online_green));
        RadioGroup statusChangeGroup = view.findViewById(R.id.toggle);
        statusIndicator.setBorderWidth(0);
        statusChangeGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.online) {
                statusIndicator.setBackgroundColor(getResources().getColor(com.cometchat.chatuikit.R.color.online_green));
            } else if (i == R.id.offline) {
                statusIndicator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        });

        return view;
    }

}
