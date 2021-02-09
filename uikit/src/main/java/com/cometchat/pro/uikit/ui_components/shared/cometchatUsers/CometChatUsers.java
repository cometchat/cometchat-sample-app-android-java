package com.cometchat.pro.uikit.ui_components.shared.cometchatUsers;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingMethod;
import androidx.databinding.BindingMethods;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.R;

import java.util.List;

import com.cometchat.pro.uikit.ui_resources.utils.recycler_touch.ClickListener;
import com.cometchat.pro.uikit.ui_resources.utils.item_clickListener.OnItemClickListener;
import com.cometchat.pro.uikit.ui_resources.utils.recycler_touch.RecyclerTouchListener;

/**
 * Purpose - CometChatUserList class is a subclass of recyclerview and used as component by
 * developer to display list of users. Developer just need to fetchUsers at their end
 * and pass it to this component to display list of Users. It helps user to create conversation
 * list easily and saves their time.
 * @see User
 *
 * Created on - 20th December 2019
 *
 * Modified on  - 23rd March 2020
 *
 */

@BindingMethods(value = {@BindingMethod(type = CometChatUsers.class, attribute = "app:userlist", method = "setUserList")})
public class CometChatUsers extends RecyclerView {

    private Context context;

    private UserListViewModel userListViewModel;

    private boolean showHeader;

    public CometChatUsers(@NonNull Context context) {
        super(context);
        this.context = context;
        setViewModel();
    }

    public CometChatUsers(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        getAttributes(attrs);
        setViewModel();
    }

    public CometChatUsers(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        getAttributes(attrs);
        setViewModel();
    }

    private void getAttributes(AttributeSet attributeSet) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.CometChatUserList, 0, 0);
        showHeader = a.getBoolean(R.styleable.CometChatUserList_headers,false);
    }

    /**
     *  This methods sets the list of users provided by the developer
     *
     * @param userList list of users
     *
     */
    public void setUserList(List<User> userList) {
        if (userListViewModel != null)
            userListViewModel.setUsersList(userList);
    }

    private void setViewModel() {
        if (userListViewModel == null) {
            userListViewModel = new UserListViewModel(context,this,showHeader);
        }
    }

    public void add(int index,User user){
        if (userListViewModel!=null)
            userListViewModel.add(index,user);
    }

    /**
     * Method helps in adding the user to list
     *
     * @param user to be added in the list
     */
    public void add(User user){
        if (userListViewModel!=null)
            userListViewModel.add(user);
    }

    /**
     *  This methods updates the particular user provided by the developer
     *
     * @param user object of the user to be updated
     *
     */
    public void update(User user){
        if (userListViewModel!=null)
            userListViewModel.update(user);

    }

    public void update(int index,User user){
        if (userListViewModel!=null)
            userListViewModel.update(index,user);
    }

    public void remove(int index){
        if (userListViewModel!=null)
            userListViewModel.remove(index);
    }

    /**
     *   Removes user from the list based on user provided
     *
     * @param user of the user to be removed
     *
     */
    public void remove(User user){
        if (userListViewModel!=null){
            userListViewModel.remove(user);
        }
    }

    /**
     *   This method provides click event callback to the developer.
     *
     * @param onItemClickListener object of <code>OnItemClickListener<User><code/> class
     */
    public void setItemClickListener(OnItemClickListener<User> onItemClickListener){

        this.addOnItemTouchListener(new RecyclerTouchListener(context, this, new ClickListener() {

            @Override
            public void onClick(View var1, int var2) {
                User user=(User)var1.getTag(R.string.user);
                if (onItemClickListener!=null)
                      onItemClickListener.OnItemClick(user,var2);
                else
                    throw new NullPointerException("OnItemClickListener<User> is null");
            }

            @Override
            public void onLongClick(View var1, int var2) {
                User user=(User)var1.getTag(R.string.user);
                if (onItemClickListener!=null)
                     onItemClickListener.OnItemLongClick(user,var2);
                else
                  throw new NullPointerException("OnItemClickListener<User> is null");
            }
        }));
    }


    /**
     * This method is used to set list of searched user in CometChatUserList Component.
     * @param userList is object of List<User>. It is list of searched users.
     */
    public void searchUserList(List<User> userList)
    {
        userListViewModel.searchUserList(userList);
    }

    /**
     * This method is used to clear a userlist of CometChatUserList Component.
     */
    public void clear() {
        userListViewModel.clear();
    }
}
