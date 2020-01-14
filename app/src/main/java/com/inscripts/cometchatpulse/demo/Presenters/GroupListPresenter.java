package com.inscripts.cometchatpulse.demo.Presenters;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Adapter.GroupListAdapter;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.GroupListContract;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.helpers.Logger;
import com.cometchat.pro.models.Group;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class GroupListPresenter extends Presenter<GroupListContract.GroupView> implements
        GroupListContract.GroupPresenter {

    private GroupsRequest groupsRequest;

    private HashMap<String, Group> groupHashMap = new HashMap<>();

    private static final String TAG = "GroupListPresenter";

    @Override
    public void initGroupView() {

        if (groupsRequest == null) {
            groupsRequest = new GroupsRequest.GroupsRequestBuilder().setLimit(50).build();
        }
        setGroupsRequest(groupsRequest);
    }

    private void setGroupsRequest(GroupsRequest groupsRequest) {

        groupsRequest.fetchNext(new CometChat.CallbackListener<List<Group>>() {
            @Override
            public void onSuccess(List<Group> groups) {
                if (isViewAttached()) {
                    Timber.d("onSuccess: %s", groups.toString());
                    for (Group group : groups) {
                        groupHashMap.put(group.getGuid(), group);
                    }
                    getBaseView().setGroupAdapter(groupHashMap);

                }

            }

            @Override
            public void onError(CometChatException e) {
                Timber.d("onError: %s", e.getMessage());

            }

        });
    }

    @Override
    public void joinGroup(final Context context, final Group group, final ProgressDialog progressDialog,
                          final GroupListAdapter groupListAdapter) {

        CometChat.joinGroup(group.getGuid(), group.getGroupType(), group.getPassword(), new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group) {
                progressDialog.dismiss();
                group.setHasJoined(true);
                groupListAdapter.notifyDataSetChanged();
                if (isViewAttached())
                    getBaseView().groupjoinCallback(group);
            }

            @Override
            public void onError(CometChatException e) {
                Log.d("joinGroup", "onError: " + e.getMessage());
                if (e.getCode().equals("ERR_ALREADY_JOINED")){
                    if (isViewAttached())
                        getBaseView().groupjoinCallback(group);
                }
               progressDialog.dismiss();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });

    }

    @Override
    public void refresh() {
        groupsRequest = null;
        initGroupView();
    }

    @Override
    public void searchGroup(String s) {

        HashMap<String ,Group> filterMap=new HashMap<>();
        GroupsRequest groupsRequest = new GroupsRequest.GroupsRequestBuilder().setSearchKeyWord(s).setLimit(100).build();

        groupsRequest.fetchNext(new CometChat.CallbackListener<List<Group>>() {
            @Override
            public void onSuccess(List<Group> groups) {

                for (Group group : groups) {
                    filterMap.put(group.getGuid(), group);
                }
                getBaseView().setFilterGroup(filterMap);
            }

            @Override
            public void onError(CometChatException e) {

            }
        });


    }

    @Override
    public void deleteGroup(Context context,String guid,GroupListAdapter groupListAdapter) {
        CometChat.deleteGroup(guid, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                groupListAdapter.removeGroup(guid);
                Timber.d("onSuccess: %s" , s);
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
