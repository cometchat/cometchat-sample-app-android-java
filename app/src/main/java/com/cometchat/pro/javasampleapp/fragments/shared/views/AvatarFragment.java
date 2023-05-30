package com.cometchat.pro.javasampleapp.fragments.shared.views;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.CometChatAvatar;
import com.cometchat.pro.core.CometChat;

import com.cometchat.pro.javasampleapp.R;
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
        if (CometChatUIKit.getLoggedInUser() != null) {
            loggedInUserName.setText("Name : " + CometChatUIKit.getLoggedInUser().getName());
            avatar.setImage(CometChatUIKit.getLoggedInUser().getAvatar(),CometChatUIKit.getLoggedInUser().getName()); // set Logged in user image to Avatar
        }
        avatar.setBorderWidth(10); // set Avatar Border Width
        avatar.setCornerRadius(0); // set Avatar Corner Radius
        avatar.setBackgroundColor(getResources().getColor(R.color.white)); // set Avatar background Color
        avatar.setElevation(0);
        cornerRadiusLayout = view.findViewById(R.id.borderRadiusLayout);

        RadioGroup radioGroup = view.findViewById(R.id.toggle);
        radioGroup.setOnCheckedChangeListener((radio, i) -> {
            if (i == R.id.image) {
                avatar.setImage(CometChatUIKit.getLoggedInUser().getAvatar());
            } else if (i == R.id.name) {
                avatar.setTextColor(getResources().getColor(R.color.black));
                avatar.setTextAppearance(androidx.databinding.library.baseAdapters.R.style.Base_TextAppearance_AppCompat_Large);
                avatar.setName(CometChatUIKit.getLoggedInUser().getName());
            }

        });
        TextInputEditText cornerRadius = view.findViewById(R.id.borderRadius);
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
