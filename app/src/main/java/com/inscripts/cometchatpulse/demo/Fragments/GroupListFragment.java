package com.inscripts.cometchatpulse.demo.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Contracts.GroupListContract;
import com.inscripts.cometchatpulse.demo.Adapter.GroupListAdapter;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.CustomView.StickyHeaderDecoration;
import com.inscripts.cometchatpulse.demo.Helper.CustomAlertDialogHelper;
import com.inscripts.cometchatpulse.demo.Helper.OnAlertDialogButtonClickListener;
import com.inscripts.cometchatpulse.demo.Helper.RecyclerTouchListener;
import com.inscripts.cometchatpulse.demo.Helper.ScrollHelper;
import com.inscripts.cometchatpulse.demo.Presenters.GroupListPresenter;
import com.inscripts.cometchatpulse.demo.Utils.CommonUtils;
import com.inscripts.cometchatpulse.demo.Utils.Logger;
import com.cometchat.pro.constants.CometChatConstants;

import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupListFragment extends Fragment implements GroupListContract.GroupView,
       OnAlertDialogButtonClickListener {

    private RecyclerView groupsRecyclerView;

    private GroupListAdapter groupListAdapter;

    private StickyHeaderDecoration decor;

    private String  groupPassword;

    private ProgressDialog progressDialog;

    private Group grpNoGroups;

    private GroupListContract.GroupPresenter groupPresenter;

    private com.cometchat.pro.models.Group group;

    private ScrollHelper scrollHelper;

    private LinearLayoutManager linearLayoutManager;

    public GroupListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups, container, false);

        initViewComponent(view);

        groupPresenter = new GroupListPresenter();

        groupPresenter.attach(this);

        groupPresenter.initGroupView();

        return view;
    }

    private void initViewComponent(View view) {

        groupsRecyclerView = view.findViewById(R.id.groups_recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        groupsRecyclerView.setLayoutManager(linearLayoutManager);
        grpNoGroups = view.findViewById(R.id.grpNoGroups);

        groupsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    groupPresenter.initGroupView();
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy < 0) {
                    scrollHelper.setFab(true);
                } else
                    scrollHelper.setFab(false);
            }
        });


        groupsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), groupsRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View var1, int var2) {

                group = (com.cometchat.pro.models.Group) var1.getTag(R.string.group_id);

                GroupListAdapter.GroupHolder groupHolder= (GroupListAdapter.GroupHolder)
                        var1.getTag(R.string.groupHolder);

                Pair<View,String> p1=Pair.create(((View)groupHolder.imageViewGroupAvatar),"groupImage");
                Pair<View,String> p2=Pair.create(((View)groupHolder.groupNameField),"groupName");

                initJoinGroup(group,p1,p2);

            }

            @Override
            public void onLongClick(View var1, int var2) {


            }
        }));
    }

    private void initJoinGroup(com.cometchat.pro.models.Group group,Pair<View,String>... pairs) {


        if (CommonUtils.isConnected(getActivity())) {

            switch (group.getGroupType()) {
                case CometChatConstants.GROUP_TYPE_PUBLIC:
                    if (group.isJoined()) {
                        CommonUtils.startActivityIntent(group, getActivity(), false,pairs);
                    } else {
                        progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.joining));
                        progressDialog.setCancelable(false);
                        groupPresenter.joinGroup(getActivity(), group, progressDialog, groupListAdapter);
                    }

                    break;
                case CometChatConstants.GROUP_TYPE_PRIVATE:

                    if (group.isJoined()) {
                        CommonUtils.startActivityIntent(group, getActivity(), false,pairs);
                    } else {
                        progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.joining));
                        progressDialog.setCancelable(false);
                        groupPresenter.joinGroup(getActivity(), group, progressDialog, groupListAdapter);
                    }
                    break;
                case CometChatConstants.GROUP_TYPE_PASSWORD:

                    if (group.isJoined()) {
                        CommonUtils.startActivityIntent(group, getActivity(), false,pairs);
                    } else {
                        View dialogview = getActivity().getLayoutInflater().inflate(R.layout.cc_custom_dialog, null);
                        TextView tvTitle = (TextView) dialogview.findViewById(R.id.textViewDialogueTitle);
                        tvTitle.setText("");
                        new CustomAlertDialogHelper(getContext(), getString(R.string.group_password), dialogview, getString(R.string.accept),
                                "", getString(R.string.cancel),  this,1, false);
                    }
                    break;
            }
        } else {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(getActivity(), getString(R.string.warning), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==StringContract.RequestCode.LEFT)
        {
            if (groupListAdapter != null) {
                groupPresenter.refresh();
                groupListAdapter=null;
                groupsRecyclerView.removeItemDecoration(decor);
                decor=null;
                Logger.error("onResume", "onResume");
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        groupPresenter.refresh();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        scrollHelper = (ScrollHelper) context;
    }

    @Override
    public void setGroupAdapter(List<com.cometchat.pro.models.Group> groupList) {

        if (groupListAdapter == null) {
            groupListAdapter = new GroupListAdapter(groupList, getActivity());
            groupsRecyclerView.setAdapter(groupListAdapter);
            decor = new StickyHeaderDecoration(groupListAdapter);
            groupsRecyclerView.addItemDecoration(decor, 0);
        } else {
            groupListAdapter.refreshData(groupList);
        }
        if (groupList.size() == 0) {
            grpNoGroups.setVisibility(View.VISIBLE);
        } else {
            grpNoGroups.setVisibility(View.GONE);
        }
    }

    @Override
    public void groupjoinCallback(com.cometchat.pro.models.Group group) {

        CommonUtils.startActivityIntent(group, getContext(), false);
    }

    @Override
    public void setFilterGroup(List<com.cometchat.pro.models.Group> groups) {
        if (groupListAdapter!=null){
             groupListAdapter.setFilterList(groups);
        }
    }


    @Override
    public void setTitle(String title) {

    }

    @Override
    public void onButtonClick(AlertDialog alertDialog, View v, int which, int popupId) {

        EditText groupPasswordInput = (EditText) v.findViewById(R.id.edittextDialogueInput);
        if (which == DialogInterface.BUTTON_NEGATIVE) { // Cancel

            alertDialog.dismiss();
        } else if (which == DialogInterface.BUTTON_POSITIVE) { // Join
            try {
                progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.joining));
                progressDialog.setCancelable(false);
                groupPassword = groupPasswordInput.getText().toString();
                if (groupPassword.length() == 0) {
                    groupPasswordInput.setText("");
                    groupPasswordInput.setError(getString(R.string.incorrect_password));

                } else {
                    try {
                        alertDialog.dismiss();
                        group.setPassword(groupPassword);

                        groupPresenter.joinGroup(getActivity(), group, progressDialog, groupListAdapter);


                    } catch (Exception e) {
                        Logger.error("Error at SHA1:UnsupportedEncodingException FOR PASSWORD "
                                + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                Logger.error("chatroomFragment.java onButtonClick() : Exception=" + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

    public void search(String s) {
       groupPresenter.searchGroup(s);
    }
}
