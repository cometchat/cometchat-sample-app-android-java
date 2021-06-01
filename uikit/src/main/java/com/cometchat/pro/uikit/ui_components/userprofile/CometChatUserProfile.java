package com.cometchat.pro.uikit.ui_components.userprofile;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.databinding.FragmentCometchatUserProfileBinding;
import com.cometchat.pro.uikit.ui_components.shared.CometChatSnackBar;
import com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar;
import com.cometchat.pro.uikit.ui_resources.utils.CometChatError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_resources.utils.FontUtils;
import com.cometchat.pro.uikit.ui_settings.FeatureRestriction;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.cometchat.pro.uikit.ui_components.userprofile.privacy_security.CometChatMorePrivacyActivity;

public class CometChatUserProfile extends Fragment {

    private CometChatAvatar notificationIv;
    private AlertDialog.Builder dialog;
    FragmentCometchatUserProfileBinding moreInfoScreenBinding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        CometChatError.init(getContext());
        moreInfoScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cometchat_user_profile, container, false);
        moreInfoScreenBinding.setUser(CometChat.getLoggedInUser());
        moreInfoScreenBinding.ivUser.setAvatar(CometChat.getLoggedInUser());

        moreInfoScreenBinding.tvTitle.setTypeface(FontUtils.getInstance(getActivity()).getTypeFace(FontUtils.robotoMedium));
        Log.e("onCreateView: ", CometChat.getLoggedInUser().toString());
        moreInfoScreenBinding.privacyAndSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), CometChatMorePrivacyActivity.class));
            }
        });

        if (FeatureRestriction.getColor()!=null) {
            int widgetColor = Color.parseColor(FeatureRestriction.getColor());
            Drawable wrappedDrawable = DrawableCompat.wrap(getResources().
                    getDrawable(R.drawable.ic_security_24dp));
            wrappedDrawable.setTint(widgetColor);
            DrawableCompat.setTint(wrappedDrawable, widgetColor);
            moreInfoScreenBinding.ivSecurity.setImageDrawable(wrappedDrawable);
        }
        if(Utils.isDarkMode(getContext())) {
            moreInfoScreenBinding.tvTitle.setTextColor(getResources().getColor(R.color.textColorWhite));
            moreInfoScreenBinding.tvSeperator.setBackgroundColor(getResources().getColor(R.color.grey));
            moreInfoScreenBinding.tvSeperator1.setBackgroundColor(getResources().getColor(R.color.grey));
        } else {
            moreInfoScreenBinding.tvTitle.setTextColor(getResources().getColor(R.color.primaryTextColor));
            moreInfoScreenBinding.tvSeperator.setBackgroundColor(getResources().getColor(R.color.light_grey));
            moreInfoScreenBinding.tvSeperator1.setBackgroundColor(getResources().getColor(R.color.light_grey));
        }

        moreInfoScreenBinding.userContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserDialog();
            }
        });
        return moreInfoScreenBinding.getRoot();
    }

    private void updateUserDialog() {
        dialog = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.cometchat_update_user_dialog,null);
        CometChatAvatar avatar = view.findViewById(R.id.user_avatar);
        avatar.setAvatar(CometChat.getLoggedInUser());
        TextInputEditText avatar_url = view.findViewById(R.id.avatar_url_edt);
        avatar_url.setText(CometChat.getLoggedInUser().getAvatar());
        TextInputEditText username = view.findViewById(R.id.username_edt);
        username.setText(CometChat.getLoggedInUser().getName());
        MaterialButton updateUserBtn = view.findViewById(R.id.updateUserBtn);
        MaterialButton cancelBtn = view.findViewById(R.id.cancelBtn);

        if(CometChat.getLoggedInUser().getAvatar()==null) {
            avatar.setVisibility(View.GONE);
            avatar_url.setVisibility(View.GONE);
        }
        else {
            avatar.setVisibility(View.VISIBLE);
            avatar_url.setVisibility(View.GONE);
        }
        avatar_url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty())
                {
                    avatar.setVisibility(View.VISIBLE);
                    Glide.with(getContext()).load(s.toString()).into(avatar);
                } else
                    avatar.setVisibility(View.GONE);
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.setView(view);
        updateUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                if (username.getText().toString().isEmpty())
                    username.setError(getString(R.string.fill_this_field));
                else {
                    user.setName(username.getText().toString());
                    user.setUid(CometChat.getLoggedInUser().getUid());
                    user.setAvatar(avatar_url.getText().toString());
                    updateUser(user);
                    alertDialog.dismiss();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void updateUser(User user) {
        CometChat.updateUser(user, UIKitConstants.AppInfo.AUTH_KEY, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                if (getContext()!=null)
                    CometChatSnackBar.show(getContext(),moreInfoScreenBinding.getRoot(),
                            getString(R.string.updated_user_successfully),CometChatSnackBar.SUCCESS);
                moreInfoScreenBinding.setUser(user);
            }

            @Override
            public void onError(CometChatException e) {
                if (getContext()!=null)
                   CometChatSnackBar.show(getContext(),moreInfoScreenBinding.tvTitle, CometChatError.localized(e), CometChatSnackBar.ERROR);
            }
        });
    }
}
