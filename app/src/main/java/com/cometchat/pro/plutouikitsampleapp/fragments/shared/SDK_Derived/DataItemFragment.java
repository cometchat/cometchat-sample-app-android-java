package com.cometchat.pro.plutouikitsampleapp.fragments.shared.SDK_Derived;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.plutouikitsampleapp.R;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatDataItem.CometChatDataItem;
import com.cometchatworkspace.resources.constants.UIKitConstants;


public class DataItemFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_data_item, container, false);
        CometChatDataItem groupsDataItem = view.findViewById(R.id.groupsDataItem);
        Group group =new Group();
        group.setName("Avengers");
        group.setGroupType(UIKitConstants.GroupTypeConstants.PASSWORD);
        group.setMembersCount(8);
        groupsDataItem.group(group);

        //setting dataItem for User
        CometChatDataItem users_dataItem = view.findViewById(R.id.usersDataItem);
        if (CometChat.getLoggedInUser() != null) {
            users_dataItem.user(CometChat.getLoggedInUser());
        }
        return view;
    }
}