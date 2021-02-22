package com.cometchat.pro.androiduikit.ComponentFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;

import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.androiduikit.databinding.FragmentUserListBinding;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_resources.utils.item_clickListener.OnItemClickListener;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;


public class UserListViewFragment extends Fragment {

    UsersRequest usersRequest;
    FragmentUserListBinding userListBinding;
    ObservableArrayList<User> observableArrayList = new ObservableArrayList<User>();
    ShimmerFrameLayout shimmerList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userListBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_list,container,false);
        getUsers();
        userListBinding.setUserList(observableArrayList);
        userListBinding.cometchatUserList.setItemClickListener(new OnItemClickListener<User>()
        {
            @Override
            public void OnItemClick(User user, int position) {
                Intent intent = new Intent(getContext(), CometChatMessageListActivity.class);
                intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER);
                intent.putExtra(UIKitConstants.IntentStrings.NAME,user.getName());
                intent.putExtra(UIKitConstants.IntentStrings.UID,user.getUid());
                intent.putExtra(UIKitConstants.IntentStrings.AVATAR,user.getAvatar());
                intent.putExtra(UIKitConstants.IntentStrings.STATUS,user.getStatus());
                startActivity(intent);
            }

            @Override
            public void OnItemLongClick(User var, int position) {
                super.OnItemLongClick(var, position);
            }
        });
        return userListBinding.getRoot();
    }

    private void getUsers() {
        if (usersRequest == null)
        {
            usersRequest = new UsersRequest.UsersRequestBuilder().setLimit(30).build();
        }
        usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                userListBinding.contactShimmer.stopShimmer();
                userListBinding.contactShimmer.setVisibility(View.GONE);
                observableArrayList.addAll(users);
            }

            @Override
            public void onError(CometChatException e) {

            }
        });
    }
}
