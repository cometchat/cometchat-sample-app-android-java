package com.inscripts.cometchatpulse.demo.Presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.cometchat.pro.core.BannedGroupMembersRequest;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Activity.CometChatActivity;
import com.inscripts.cometchatpulse.demo.Adapter.GroupMemberListAdapter;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.BannedMemberListContract;
import com.inscripts.cometchatpulse.demo.Utils.Logger;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.GroupMember;

import java.util.HashMap;
import java.util.List;

public class BannedMemberListPresenter extends Presenter<BannedMemberListContract.BannedMemberListView>
        implements BannedMemberListContract.BannedMemberListPresenter {

    private BannedGroupMembersRequest bannedMembersRequest;
    private Context context;

    @Override
    public void initMemberList(String groupId, int limit, Context context) {

        this.context = context;
        HashMap<String ,GroupMember> groupMemberHashMap=new HashMap<>();
        if (bannedMembersRequest == null) {
            bannedMembersRequest = new BannedGroupMembersRequest.BannedGroupMembersRequestBuilder(groupId).setLimit(limit).build();
            bannedMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>(){
                @Override
                public void onSuccess(List<GroupMember> groupMembers) {
                    if (groupMembers != null && groupMembers.size() != 0) {
                        Logger.error("OutcastMembersRequest", " " + groupMembers.size());
                        if (isViewAttached()){
                            for (GroupMember groupMember :groupMembers) {
                                groupMemberHashMap.put(groupMember.getUid(),groupMember);
                            }
                            getBaseView().setAdapter(groupMemberHashMap);
                        }
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            });
        } else {
            bannedMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
                @Override
                public void onSuccess(List<GroupMember> groupMembers) {
                    if (groupMembers != null && groupMembers.size() != 0) {
                        Logger.error("OutcastMembersRequest", " " + groupMembers.size());
                        if (isViewAttached())
                            for (GroupMember groupMember :groupMembers) {
                                groupMemberHashMap.put(groupMember.getUid(),groupMember);
                            }
                        getBaseView().setAdapter(groupMemberHashMap);
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void refresh(String GUID, int LIMIT,Context context) {
        bannedMembersRequest=null;
        initMemberList(GUID,LIMIT,context);
    }

    @Override
    public void addGroupEventListener(String listenerId, String groupId,final GroupMemberListAdapter groupMemberListAdapter) {
        CometChat.addGroupListener(listenerId, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberUnbanned(Action action, User unbannedUser, User unbannedBy, Group unbannedFrom) {
                Log.e("onGroupMemberUnbanned: ",unbannedUser.getName() );
                groupMemberListAdapter.removeMember(unbannedUser.getUid());
            }

            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group bannedFrom) {
                Log.e("OutCastedMemberList", "onGroupMemberbanned: "+bannedUser.getUid() );
                if (bannedUser.getUid().equals(CometChat.getLoggedInUser().getUid())) {
                    context.startActivity(new Intent(context, CometChatActivity.class));
                } else {
                    groupMemberListAdapter.addMember(bannedUser.getUid(), UserToGroupMember(bannedUser, action));
                }
            }
        });
    }
    public GroupMember UserToGroupMember(User joinedUser,Action action)
    {
        GroupMember groupMember = new GroupMember(joinedUser.getUid(), action.getOldScope());
        groupMember.setAvatar(joinedUser.getAvatar());
        groupMember.setName(joinedUser.getName());
        groupMember.setStatus(joinedUser.getStatus());
        return groupMember;
    }

}