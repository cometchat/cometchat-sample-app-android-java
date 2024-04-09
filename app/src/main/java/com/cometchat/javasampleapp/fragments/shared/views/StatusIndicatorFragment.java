package com.cometchat.javasampleapp.fragments.shared.views;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.shared.views.CometChatStatusIndicator.CometChatStatusIndicator;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;
import com.google.android.material.textfield.TextInputLayout;

public class StatusIndicatorFragment extends Fragment {
    private TextInputLayout borderWidthLayout, cornerRadiusLayout;
    private LinearLayout parentView;
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
        parentView= view.findViewById(R.id.parent_view);
        statusIndicator.setBackgroundColor(getResources().getColor(com.cometchat.chatuikit.R.color.cometchat_online_green));
        RadioGroup statusChangeGroup = view.findViewById(R.id.toggle);
        statusIndicator.setBorderWidth(0);
        statusChangeGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.online) {
                statusIndicator.setBackgroundColor(getResources().getColor(com.cometchat.chatuikit.R.color.cometchat_online_green));
            } else if (i == R.id.offline) {
                statusIndicator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
            }
        });
        setUpUI(view);
        return view;
    }

    private void setUpUI(View view) {
        if (AppUtils.isNightMode(getContext())) {
            AppUtils.changeTextColorToWhite(getContext(), view.findViewById(R.id.status_indicator_text));
            AppUtils.changeTextColorToWhite(getContext(), view.findViewById(R.id.status_desc));
            AppUtils.changeTextColorToWhite(getContext(), view.findViewById(R.id.status_title));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.app_background_dark)));
        } else {
            AppUtils.changeTextColorToBlack(getContext(), view.findViewById(R.id.status_indicator_text));
            AppUtils.changeTextColorToBlack(getContext(), view.findViewById(R.id.status_title));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
        }
    }

}
