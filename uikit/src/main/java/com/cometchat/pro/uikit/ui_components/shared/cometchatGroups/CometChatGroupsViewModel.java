package com.cometchat.pro.uikit.ui_components.shared.cometchatGroups;

import android.content.Context;

import com.cometchat.pro.core.GroupsRequest;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CometChatGroupsViewModel {

    private  Context context;

    private GroupsRequest groupsRequest;

    private CometChatGroupsAdapter groupListAdapter;

    private List<User> groupList = new ArrayList<>();

    private HashMap<String, Group> groupHashMap = new HashMap<>();

    private CometChatGroups groupListView;


    private static final String TAG = "GroupListViewModel";

    private CometChatGroupsViewModel(){

    }

    public CometChatGroupsViewModel(Context context, CometChatGroups cometChatGroupList){
        this.groupListView=cometChatGroupList;
        this.context=context;
        setGroupListAdapter(cometChatGroupList);
    }

    private CometChatGroupsAdapter getAdapter(){
        if (groupListAdapter==null){
            groupListAdapter=new CometChatGroupsAdapter(context);
        }
        return groupListAdapter;
    }

    private void setGroupListAdapter(CometChatGroups cometChatGroupList){
        groupListAdapter=new CometChatGroupsAdapter(context);
        cometChatGroupList.setAdapter(groupListAdapter);
    }

    public void setGroupList(List<Group> groupList){
         if (groupListAdapter!=null) {
              if (groupList!=null&&groupList.size()!=0)
                groupListAdapter.updateGroupList(groupList);
         }
    }


    public void remove(Group group){
         if (groupListAdapter!=null)
        groupListAdapter.removeGroup(group);
    }


    public void update(Group group) {
        if (groupListAdapter!=null)
            groupListAdapter.updateGroup(group);
    }

    public void add(Group group) {
        if (groupListAdapter!=null)
            groupListAdapter.add(group);
    }

    public void searchGroupList(List<Group> groups) {
        if (groupListAdapter!=null)
            groupListAdapter.searchGroup(groups);
    }

    public void clear() {
        if (groupListAdapter!=null)
            groupListAdapter.clear();
    }
}
