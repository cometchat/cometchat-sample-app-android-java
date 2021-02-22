package com.cometchat.pro.androiduikit.ComponentFragments;


import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cometchat.pro.androiduikit.ColorPickerDialog;
import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Random;

public class AvatarFragment extends Fragment {

    private Context context;

    private TextInputLayout borderWidthLayout;

    public AvatarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_avatar, container, false);
        CometChatAvatar avatar = view.findViewById(R.id.avataricon);
        avatar.setBorderColor(getResources().getColor(R.color.colorPrimaryDark));
        avatar.setAvatar(CometChat.getLoggedInUser().getAvatar());
        TextInputEditText borderWidth = view.findViewById(R.id.borderWidth);
        borderWidthLayout = view.findViewById(R.id.borderWidth_layout);
        borderWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0)
                    avatar.setBorderWidth(Integer.parseInt(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        RadioGroup shapegroup = view.findViewById(R.id.shapeGroup);
        shapegroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.circle)
                {
                    avatar.setShape("circle");
                    refreshAvatar(avatar);
                }
                else
                {
                    avatar.setShape("rectangle");
                    refreshAvatar(avatar);
                }
            }
        });
        view.findViewById(R.id.red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatar.setBorderColor(getResources().getColor(R.color.red));
                refreshAvatar(avatar);
            }
        });
        view.findViewById(R.id.yellow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatar.setBorderColor(getResources().getColor(R.color.yellow));
                refreshAvatar(avatar);
            }
        });
        view.findViewById(R.id.purple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatar.setBorderColor(getResources().getColor(R.color.purple));
                refreshAvatar(avatar);
            }
        });
        view.findViewById(R.id.green).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatar.setBorderColor(getResources().getColor(R.color.green));
                refreshAvatar(avatar);
            }
        });
        view.findViewById(R.id.blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatar.setBorderColor(getResources().getColor(R.color.blue));
                refreshAvatar(avatar);
            }
        });
        view.findViewById(R.id.violet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatar.setBorderColor(getResources().getColor(R.color.violet));
                refreshAvatar(avatar);
            }
        });
        checkDarkMode();
        return view;
    }

    private void checkDarkMode() {
        if(Utils.isDarkMode(getContext())) {
            borderWidthLayout.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            borderWidthLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            borderWidthLayout.setBoxStrokeColor(getResources().getColor(R.color.textColorWhite));
        } else {
            borderWidthLayout.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.primaryTextColor)));
            borderWidthLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.primaryTextColor)));
            borderWidthLayout.setBoxStrokeColor(getResources().getColor(R.color.primaryTextColor));
        }
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public void refreshAvatar(CometChatAvatar avatar)
    {
        avatar.setAvatar(CometChat.getLoggedInUser());
    }
}
