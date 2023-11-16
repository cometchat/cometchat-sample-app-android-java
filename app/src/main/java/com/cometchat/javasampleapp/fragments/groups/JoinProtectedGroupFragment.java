package com.cometchat.javasampleapp.fragments.groups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.joinprotectedgroup.CometChatJoinProtectedGroup;
import com.cometchat.javasampleapp.R;
import com.cometchat.javasampleapp.AppUtils;

public class JoinProtectedGroupFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join_protected_group, container, false);
        CometChatJoinProtectedGroup joinProtectedGroup = view.findViewById(R.id.join_group);
        joinProtectedGroup.setGroup(AppUtils.getDefaultGroup());

        return view;
    }
}