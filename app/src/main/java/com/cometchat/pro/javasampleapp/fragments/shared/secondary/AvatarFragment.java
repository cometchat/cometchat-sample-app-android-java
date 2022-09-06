package com.cometchat.pro.javasampleapp.fragments.shared.secondary;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cometchat.pro.core.CometChat;

import com.cometchat.pro.javasampleapp.R;
import com.cometchatworkspace.components.shared.secondaryComponents.cometchatAvatar.CometChatAvatar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AvatarFragment extends Fragment {

    private TextInputLayout cornerRadiusLayout;

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
        TextView loggedInUserName = view.findViewById(R.id.loggedInUserName);
        if (CometChat.getLoggedInUser() != null) {
            loggedInUserName.setText("Name : " + CometChat.getLoggedInUser().getName());
            avatar.setAvatar(CometChat.getLoggedInUser()); // set Logged in user image to Avatar
        }

        avatar.setBorderColor(getResources().getColor(R.color.colorPrimaryDark)); //set Avatar Border Color
        avatar.setBorderWidth(10); // set Avatar Border Width
        avatar.setCornerRadius(10); // set Avatar Corner Radius
        avatar.setBackgroundColor(getResources().getColor(R.color.white)); // set Avatar background Color

        cornerRadiusLayout = view.findViewById(R.id.borderRadiusLayout);

        RadioGroup radioGroup = view.findViewById(R.id.toggle);
        radioGroup.setOnCheckedChangeListener((radio, i) -> {
            if (i == R.id.image) {
                avatar.setAvatar(CometChat.getLoggedInUser());
            } else if (i == R.id.name) {
                avatar.setTextColor(getResources().getColor(R.color.black));
                avatar.setTextAppearance(androidx.databinding.library.baseAdapters.R.style.Base_TextAppearance_AppCompat_Large);
                avatar.setInitials(CometChat.getLoggedInUser().getName());
            }

        });
        TextInputEditText cornerRadius = view.findViewById(R.id.borderRadius);
        cornerRadius.setText(String.valueOf(10));
        cornerRadiusLayout = view.findViewById(R.id.borderRadiusLayout);
        cornerRadius.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    int radius = Integer.parseInt(charSequence.toString());
                    avatar.setCornerRadius(radius);
                } catch (Exception e) {
                    cornerRadius.setText("0");
                    avatar.setCornerRadius(0);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return view;
    }



}
