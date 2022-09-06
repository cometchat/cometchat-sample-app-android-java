package com.cometchat.pro.javasampleapp.fragments.users;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.javasampleapp.R;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatDataItem.CometChatDataItem;

public class UserListDataItemFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_list_data_item, container, false);
        CometChatDataItem dataItem = view.findViewById(R.id.usersDataItem);

        if (CometChat.getLoggedInUser() != null) {
            dataItem.user(CometChat.getLoggedInUser());
        }

        return view;
    }
}