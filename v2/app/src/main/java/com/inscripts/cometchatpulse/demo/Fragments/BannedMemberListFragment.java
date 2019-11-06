package com.inscripts.cometchatpulse.demo.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Adapter.GroupMemberListAdapter;
import com.inscripts.cometchatpulse.demo.Contracts.BannedMemberListContract;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.Helper.RecyclerTouchListener;
import com.inscripts.cometchatpulse.demo.Presenters.BannedMemberListPresenter;
import com.inscripts.cometchatpulse.demo.Utils.CommonUtils;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class BannedMemberListFragment extends Fragment implements BannedMemberListContract.BannedMemberListView {

    private RecyclerView rvMembers;

    private static final int LIMIT = 30;

    private LinearLayoutManager linearLayoutManager;

    private User user;

    private String groupId;

    private GroupMemberListAdapter groupMemberListAdapter;

    private BannedMemberListContract.BannedMemberListPresenter bannedMemberListPresenter;

    private IntentFilter intentFilter;

    private String scope;

    public BannedMemberListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outcast_member_list, container, false);

        bannedMemberListPresenter = new BannedMemberListPresenter();
        bannedMemberListPresenter.attach(this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvMembers = view.findViewById(R.id.rv_member);
        rvMembers.setLayoutManager(linearLayoutManager);

        registerForContextMenu(rvMembers);

        intentFilter = new IntentFilter();
        intentFilter.addAction("refresh");

        if (getArguments().containsKey(StringContract.IntentStrings.INTENT_GROUP_ID)) {
            groupId = getArguments().getString(StringContract.IntentStrings.INTENT_GROUP_ID);
            bannedMemberListPresenter.initMemberList(groupId, LIMIT, getContext());
        }

        if (getArguments().containsKey(StringContract.IntentStrings.INTENT_SCOPE)) {
            scope = getArguments().getString(StringContract.IntentStrings.INTENT_SCOPE);
        }

        rvMembers.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvMembers, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View var1, int var2) {

                if (scope.equals(CometChatConstants.SCOPE_MODERATOR) || scope.equals(CometChatConstants.SCOPE_ADMIN)) {
                    registerForContextMenu(rvMembers);
                    user = (User) var1.getTag(R.string.user);
                    getActivity().openContextMenu(var1);
                }
            }

            @Override
            public void onLongClick(View var1, int var2) {

            }
        }));

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_group_action, menu);
        MenuItem kickItem = menu.findItem(R.id.menu_item_kick);
        MenuItem outcastItem = menu.findItem(R.id.menu_item_outcast);
        menu.findItem(R.id.menu_view_profile).setVisible(false);
        menu.findItem(R.id.menu_item_updateScope).setVisible(false);
        kickItem.setVisible(false);
        outcastItem.setVisible(false);
        menu.setHeaderTitle(CommonUtils.setTitle("Select Action", getContext()));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_item_Reinstate:
                bannedMemberListPresenter.reinstateUser(user.getUid(), groupId, groupMemberListAdapter);
                showToast("unbanned");
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver refreshBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("refresh")) {
                if (groupMemberListAdapter != null) {
                    groupMemberListAdapter.resetAdapter();
                }
                bannedMemberListPresenter.refresh(groupId, LIMIT, getContext());
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        try {
            getActivity().registerReceiver(refreshBroadcast, intentFilter);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        bannedMemberListPresenter.detach();
        getActivity().unregisterReceiver(refreshBroadcast);
    }

    @Override
    public void setAdapter(HashMap<String, GroupMember> list) {

        if (groupMemberListAdapter == null) {
            groupMemberListAdapter = new GroupMemberListAdapter(list, getContext(), null);
            bannedMemberListPresenter.addGroupEventListener("BannedMemberListFragment",groupId,groupMemberListAdapter);
            rvMembers.setAdapter(groupMemberListAdapter);
        } else {
            groupMemberListAdapter.refreshList(list);
        }
    }
}