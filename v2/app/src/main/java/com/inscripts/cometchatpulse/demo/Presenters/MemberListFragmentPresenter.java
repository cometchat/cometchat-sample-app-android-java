package com.inscripts.cometchatpulse.demo.Presenters;

import android.content.Context;
import android.widget.Toast;

import com.inscripts.cometchatpulse.demo.Adapter.GroupMemberListAdapter;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.MemberListFragmentContract;
import com.inscripts.cometchatpulse.demo.Utils.Logger;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupMembersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.GroupMember;

import java.util.HashMap;
import java.util.List;

public class MemberListFragmentPresenter extends Presenter<MemberListFragmentContract.MemberListFragmentView>
        implements MemberListFragmentContract.MemberListFragmentPresenter {

    private GroupMembersRequest groupMembersRequest;

    private Context context;

    HashMap<String ,GroupMember> groupMemberHashMap=new HashMap<>();

    @Override
    public void initMemberList(String guid, int LIMIT,Context context) {

        this.context=context;


        if (groupMembersRequest==null)
        {
            groupMembersRequest=new GroupMembersRequest.GroupMembersRequestBuilder(guid).setLimit(LIMIT).build();
        }

        setGroupMembersRequest(groupMembersRequest);

    }

    private void setGroupMembersRequest(GroupMembersRequest groupMembersRequest){
        groupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> groupMembers) {
                Logger.error("groupMembersRequest"," "+groupMembers.size());
                if (isViewAttached())
                    for (GroupMember groupMember :groupMembers) {
                        groupMemberHashMap.put(groupMember.getUid(),groupMember);
                    }
                getBaseView().setAdapter(groupMemberHashMap);
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        });
    }

    @Override
    public void outCastUser(final String uid, String groupGuid, final GroupMemberListAdapter groupMemberListAdapter) {

        CometChat.banGroupMember(uid, groupGuid, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                getBaseView().removeMember(uid);
                Logger.error("UserOutcastedListener", "Success");
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void kickUser(final String uid, String groupId, final GroupMemberListAdapter groupMemberListAdapter) {

        CometChat.kickGroupMember(uid, groupId, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                groupMemberListAdapter.removeMember(uid);
                Logger.error("UserKickedListener","Success");
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void updateScope(String uid, String groupId, GroupMemberListAdapter groupMemberListAdapter, String scope) {
        CometChat.updateGroupMemberScope(uid, groupId, scope, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                groupMemberListAdapter.updateMember(uid,scope);
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void refresh(String GUID, int LIMIT,Context context) {
        groupMembersRequest=null;
        initMemberList(GUID,LIMIT,context);
    }

}