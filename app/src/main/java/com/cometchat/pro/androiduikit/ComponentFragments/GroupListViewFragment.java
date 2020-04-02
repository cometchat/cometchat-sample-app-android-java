package com.cometchat.pro.androiduikit.ComponentFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;

import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.androiduikit.databinding.FragmentGroupListBinding;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;

import java.util.List;

import constant.StringContract;
import listeners.OnItemClickListener;
import screen.messagelist.CometChatMessageListActivity;

public class GroupListViewFragment extends Fragment {

    FragmentGroupListBinding groupBinding;
    ObservableArrayList<Group> grouplist = new ObservableArrayList<>();
    GroupsRequest groupsRequest;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       groupBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_group_list,container,false);
        getGroups();
        groupBinding.setGroupList(grouplist);
        groupBinding.cometchatGroupList.setItemClickListener(new OnItemClickListener<Group>()
        {
            @Override
            public void OnItemClick(Group group, int position) {
                Intent intent = new Intent(getContext(), CometChatMessageListActivity.class);
                intent.putExtra(StringContract.IntentStrings.NAME,group.getName());
                intent.putExtra(StringContract.IntentStrings.GROUP_OWNER,group.getOwner());
                intent.putExtra(StringContract.IntentStrings.GUID,group.getGuid());
                intent.putExtra(StringContract.IntentStrings.AVATAR,group.getIcon());
                intent.putExtra(StringContract.IntentStrings.TYPE,CometChatConstants.RECEIVER_TYPE_GROUP);
                startActivity(intent);
            }

            @Override
            public void OnItemLongClick(Group var, int position) {
                super.OnItemLongClick(var, position);
            }
        });
        return groupBinding.getRoot();
    }

    private void getGroups() {
        if (groupsRequest==null) {
            groupsRequest = new GroupsRequest.GroupsRequestBuilder().setLimit(30).build();
        }
        groupsRequest.fetchNext(new CometChat.CallbackListener<List<Group>>() {
            @Override
            public void onSuccess(List<Group> groups) {
                groupBinding.contactShimmer.stopShimmer();
                groupBinding.contactShimmer.setVisibility(View.GONE);
                grouplist.addAll(groups);
            }

            @Override
            public void onError(CometChatException e) {
                groupBinding.contactShimmer.stopShimmer();
                groupBinding.contactShimmer.setVisibility(View.GONE);
                Log.e( "onError: ",e.getMessage());
            }
        });
    }
}
