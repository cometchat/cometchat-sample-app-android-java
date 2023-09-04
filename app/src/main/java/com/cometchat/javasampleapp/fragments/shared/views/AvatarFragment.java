package com.cometchat.javasampleapp.fragments.shared.views;


import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.views.CometChatAvatar.CometChatAvatar;
import com.cometchat.chat.core.CometChat;

import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AvatarFragment extends Fragment {

    private TextInputLayout cornerRadiusLayout;
    private LinearLayout parentView;
    CometChatAvatar avatar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_avatar, container, false);
        avatar = view.findViewById(R.id.avataricon);
        parentView = view.findViewById(R.id.parent_view);
        TextView loggedInUserName = view.findViewById(R.id.loggedInUserName);
        if (CometChatUIKit.getLoggedInUser() != null) {
            loggedInUserName.setText("Name : " + CometChatUIKit.getLoggedInUser().getName());
            avatar.setImage(CometChatUIKit.getLoggedInUser().getAvatar(),CometChatUIKit.getLoggedInUser().getName()); // set Logged in user image to Avatar
        }
        avatar.setBorderWidth(10); // set Avatar Border Width
        avatar.setCornerRadius(0); // set Avatar Corner Radius
        avatar.setElevation(0);
        avatar.setTextAppearance(androidx.appcompat.R.style.Base_TextAppearance_AppCompat_Large);
        cornerRadiusLayout = view.findViewById(R.id.borderRadiusLayout);
        RadioGroup radioGroup = view.findViewById(R.id.toggle);
        radioGroup.setOnCheckedChangeListener((radio, i) -> {
            if (i == R.id.image) {
                avatar.setImage(CometChatUIKit.getLoggedInUser().getAvatar());
            } else if (i == R.id.name) {
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
                    avatar.setRadius(radius);
                } catch (Exception e) {
                    avatar.setRadius(0);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setUpUI(view);
        return view;
    }
    private void setUpUI(View view) {
        if (AppUtils.isNightMode(getContext())) {
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.avatar_text));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.avatar_text_description));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.avatar_text_toggle));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.loggedInUserName));
            avatar.setTextColor(ContextCompat.getColor(getContext(),R.color.app_background_dark));
            cornerRadiusLayout.setBoxStrokeColorStateList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            cornerRadiusLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            cornerRadiusLayout.getEditText().setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.app_background_dark)));
        } else {
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.avatar_text));
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.avatar_text_toggle));
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.loggedInUserName));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
        }
    }

}
