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

import java.util.HashMap;
import java.util.List;

public class OutCastedMemberListPresenter extends Presenter<OutCastedMemberListContract.OutCastedMemberListView>
        implements OutCastedMemberListContract.OutCastedMemberListPresenter {

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


}
