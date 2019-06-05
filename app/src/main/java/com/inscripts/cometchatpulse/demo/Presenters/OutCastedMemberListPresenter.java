package com.inscripts.cometchatpulse.demo.Presenters;

import android.content.Context;
import android.widget.Toast;
import com.cometchat.pro.core.BannedGroupMembersRequest;
import com.inscripts.cometchatpulse.demo.Adapter.GroupMemberListAdapter;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.OutCastedMemberListContract;
import com.inscripts.cometchatpulse.demo.Utils.Logger;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.GroupMember;

import java.util.List;

public class OutCastedMemberListPresenter extends Presenter<OutCastedMemberListContract.OutCastedMemberListView>
        implements OutCastedMemberListContract.OutCastedMemberListPresenter {

    private BannedGroupMembersRequest bannedMembersRequest;
    private Context context;

    @Override
    public void initMemberList(String groupId, int limit, Context context) {

        this.context = context;

        if (bannedMembersRequest == null) {
            bannedMembersRequest = new BannedGroupMembersRequest.BannedGroupMembersRequestBuilder(groupId).setLimit(limit).build();
            bannedMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>(){
                @Override
                public void onSuccess(List<GroupMember> groupMembers) {
                    if (groupMembers != null && groupMembers.size() != 0) {
                        Logger.error("OutcastMembersRequest", " " + groupMembers.size());
                        if (isViewAttached())
                            getBaseView().setAdapter(groupMembers);
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    e.printStackTrace();
                }


            });
        } else {
            bannedMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
                @Override
                public void onSuccess(List<GroupMember> groupMembers) {
                    if (groupMembers != null && groupMembers.size() != 0) {
                        Logger.error("OutcastMembersRequest", " " + groupMembers.size());
                        if (isViewAttached())
                            getBaseView().setAdapter(groupMembers);
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    e.printStackTrace();
                }


            });
        }

    }

    @Override
    public void reinstateUser(final String uid, String groupId, final GroupMemberListAdapter groupMemberListAdapter) {
        CometChat.unbanGroupMember(uid, groupId, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                groupMemberListAdapter.removeMember(uid);
                Logger.error("User ReinstatedListener", "Success");
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        });

    }



}
