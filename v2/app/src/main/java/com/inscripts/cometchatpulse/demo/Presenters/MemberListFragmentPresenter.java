package com.inscripts.cometchatpulse.demo.Presenters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Activity.CometChatActivity;
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

    private String TAG = "MemberListFragmentPresenter";
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
    public void addGroupEventListener(String listenerId, String groupId,final GroupMemberListAdapter groupMemberListAdapter) {
        CometChat.addGroupListener(listenerId, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group kickedFrom) {
                Toast.makeText(context,"Group Member:"+action.getMessage(),Toast.LENGTH_LONG).show();
                Log.e(TAG, "onGroupMemberKicked: "+kickedUser.getUid());
                if (kickedUser.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    context.startActivity(new Intent(context,CometChatActivity.class));
                } else {
                    groupMemberListAdapter.removeMember(kickedUser.getUid());
                }
            }

            @Override
            public void onGroupMemberJoined(Action action, User joinedUser, Group joinedGroup) {
                Toast.makeText(context,"Group Member:"+action.getMessage(),Toast.LENGTH_LONG).show();
                Log.e(TAG, "onGroupMemberJoined: "+joinedUser.getUid() );
                groupMemberListAdapter.addMember(joinedUser.getUid(),UserToGroupMember(joinedUser,action));
            }

            @Override
            public void onMemberAddedToGroup(Action action, User addedby, User userAdded, Group addedTo) {
                Toast.makeText(context,"Group Member:"+action.getMessage(),Toast.LENGTH_LONG).show();
                groupMemberListAdapter.addMember(userAdded.getUid(),UserToGroupMember(userAdded,action));
            }

            @Override
            public void onGroupMemberLeft(Action action, User leftUser, Group leftGroup) {
                Toast.makeText(context, "Group Member:" + action.getMessage(), Toast.LENGTH_LONG).show();
                if (leftUser.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    context.startActivity(new Intent(context, CometChatActivity.class));
                } else {
                    groupMemberListAdapter.removeMember(leftUser.getUid());
                }
            }

            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group bannedFrom) {
                Toast.makeText(context, "Group Member:" + action.getMessage(), Toast.LENGTH_LONG).show();
                if (bannedUser.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    context.startActivity(new Intent(context, CometChatActivity.class));
                } else {
                    Log.e(TAG, "onGroupMemberBanned " + bannedUser.getUid());
                    groupMemberListAdapter.removeMember(bannedUser.getUid());
                }
            }

            @Override
            public void onGroupMemberUnbanned(Action action, User unbannedUser, User unbannedBy, Group unbannedFrom) {
                Log.e(TAG, "onGroupMemberUnbanned: "+unbannedUser.getUid() );
            }
        });
    }

    @Override
    public void refresh(String GUID, int LIMIT,Context context) {
        groupMembersRequest=null;
        initMemberList(GUID,LIMIT,context);
    }
    public GroupMember UserToGroupMember(User joinedUser,Action action)
    {
        GroupMember groupMember = new GroupMember(joinedUser.getUid(), action.getOldScope());
        groupMember.setAvatar(joinedUser.getAvatar());
        groupMember.setName(joinedUser.getName());
        groupMember.setStatus(joinedUser.getStatus());
        groupMember.setScope(CometChatConstants.SCOPE_PARTICIPANT);
        return groupMember;
    }

}