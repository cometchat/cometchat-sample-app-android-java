package com.cometchat.pro.javasampleapp.fragments.groups;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.pro.models.Group;
import com.cometchat.pro.javasampleapp.R;
import com.cometchatworkspace.components.shared.sdkDerivedComponents.cometchatDataItem.CometChatDataItem;
import com.cometchatworkspace.resources.constants.UIKitConstants;

public class GroupListDataItemFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups_list_data_item, container, false);

        CometChatDataItem dataItemPublic = view.findViewById(R.id.publicGroup);
        CometChatDataItem dataItemProtected = view.findViewById(R.id.protectedGroup);
        CometChatDataItem dataItemPrivate = view.findViewById(R.id.privateGroup);

        Group group =new Group();
        group.setName("Avengers");

        //setting group type to Public
        group.setGroupType(UIKitConstants.GroupTypeConstants.PUBLIC);
        group.setMembersCount(8);
        dataItemPublic.group(group);

        //setting group type to Password / Protected
        group.setGroupType(UIKitConstants.GroupTypeConstants.PASSWORD);
        dataItemProtected.group(group);

        //setting group type to Private
        group.setGroupType(UIKitConstants.GroupTypeConstants.PRIVATE);
        dataItemPrivate.group(group);

        return view;
    }
}