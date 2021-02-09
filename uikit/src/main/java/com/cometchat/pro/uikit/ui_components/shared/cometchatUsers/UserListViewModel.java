package com.cometchat.pro.uikit.ui_components.shared.cometchatUsers;

import android.content.Context;

import com.cometchat.pro.models.User;

import java.util.List;

import com.cometchat.pro.uikit.ui_resources.utils.sticker_header.StickyHeaderDecoration;

public class UserListViewModel {

    private static final String TAG = "UserListViewModel";

    private  Context context;

    private CometChatUsersAdapter userListAdapter;

    private CometChatUsers userListView;



    public UserListViewModel(Context context, CometChatUsers cometChatUserList, boolean showHeader){
        this.userListView=cometChatUserList;
        this.context=context;
        setUserListAdapter(cometChatUserList,showHeader);
    }

    private UserListViewModel(){

    }

    private CometChatUsersAdapter getAdapter() {
        if (userListAdapter==null){
            userListAdapter=new CometChatUsersAdapter(context);
        }
        return userListAdapter;
    }

    public void add(User user){
        if (userListAdapter!=null)
            userListAdapter.add(user);

    }
    public void add(int index,User user){
        if (userListAdapter!=null)
            userListAdapter.add(index,user);

    }

    public void update(User user){
        if (userListAdapter!=null)
            userListAdapter.updateUser(user);

    }

    public void remove(User user){
        if (userListAdapter!=null)
            userListAdapter.removeUser(user);

    }
    public void remove(int index){
        if (userListAdapter!=null)
            userListAdapter.removeUser(index);
    }

    public void clear()
    {
        if (userListAdapter!=null)
            userListAdapter.clear();
    }
    private void setUserListAdapter(CometChatUsers cometChatUserList, boolean showHeader){
        userListAdapter=new CometChatUsersAdapter(context);
        if(showHeader) {
            StickyHeaderDecoration stickyHeaderDecoration = new StickyHeaderDecoration(userListAdapter);
            cometChatUserList.addItemDecoration(stickyHeaderDecoration, 0);
        }
        cometChatUserList.setAdapter(userListAdapter);
    }

    public void setUsersList(List<User> usersList){
          getAdapter().updateList(usersList);
    }

    public void update(int index, User user) {
        if (userListAdapter!=null)
            userListAdapter.updateUser(index,user);
    }

    public void searchUserList(List<User> userList) {
        if (userListAdapter!=null)
            userListAdapter.searchUser(userList);
    }
}

