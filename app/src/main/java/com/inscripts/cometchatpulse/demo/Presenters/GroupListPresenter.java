package com.inscripts.cometchatpulse.demo.Presenters;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.inscripts.cometchatpulse.demo.Adapter.GroupListAdapter;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.GroupListContract;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.helpers.Logger;
import com.cometchat.pro.models.Group;

import java.util.List;

public class GroupListPresenter extends Presenter<GroupListContract.GroupView> implements
        GroupListContract.GroupPresenter  {

    private GroupsRequest groupsRequest;

    @Override
    public void initGroupView() {

        if (groupsRequest == null) {

            groupsRequest = new GroupsRequest.GroupsRequestBuilder().setLimit(50).build();

            groupsRequest.fetchNext(new CometChat.CallbackListener<List<Group>>() {
                @Override
                public void onSuccess(List<Group> groups) {
                    if (isViewAttached())
                        Logger.error("Groups List Received : " + groups);
                        getBaseView().setGroupAdapter(groups);

                }

                @Override
                public void onError(CometChatException e) {

                }

            });
        } else {
            groupsRequest.fetchNext(new CometChat.CallbackListener<List<Group>>() {
                @Override
                public void onSuccess(List<Group> groups) {
                    Logger.error("Groups List Received : " + groups);
                    if (isViewAttached()&&groups.size()!=0)
                        getBaseView().setGroupAdapter(groups);

                }

                @Override
                public void onError(CometChatException e) {

                }

            });
        }
    }


    @Override
    public void joinGroup(final Context context, final Group group, final ProgressDialog progressDialog,
                          final GroupListAdapter groupListAdapter) {

        CometChat.joinGroup(group.getGuid(), group.getGroupType(), group.getPassword(), new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group) {
                showToast(context, "yes");
                progressDialog.dismiss();
                group.setHasJoined(true);
                groupListAdapter.notifyDataSetChanged();
                if (isViewAttached())
                    getBaseView().groupjoinCallback(group);
            }

            @Override
            public void onError(CometChatException e) {
                progressDialog.dismiss();
            }


        });

    }

    @Override
    public void refresh() {
           groupsRequest=null;
           initGroupView();
    }


    private void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
