package com.cometchat.pro.plutouikitsampleapp.fragments.shared.secondary;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cometchat.pro.plutouikitsampleapp.R;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatStatusIndicator.CometChatStatusIndicator;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
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
