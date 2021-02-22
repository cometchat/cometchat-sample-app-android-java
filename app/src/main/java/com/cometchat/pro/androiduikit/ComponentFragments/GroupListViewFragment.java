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
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_resources.utils.item_clickListener.OnItemClickListener;

import java.util.List;

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
                intent.putExtra(UIKitConstants.IntentStrings.NAME,group.getName());
                intent.putExtra(UIKitConstants.IntentStrings.GROUP_OWNER,group.getOwner());
                intent.putExtra(UIKitConstants.IntentStrings.GUID,group.getGuid());
                intent.putExtra(UIKitConstants.IntentStrings.AVATAR,group.getIcon());
                intent.putExtra(UIKitConstants.IntentStrings.GROUP_TYPE,group.getGroupType());
                intent.putExtra(UIKitConstants.IntentStrings.TYPE,CometChatConstants.RECEIVER_TYPE_GROUP);
                intent.putExtra(UIKitConstants.IntentStrings.MEMBER_COUNT,group.getMembersCount());
                intent.putExtra(UIKitConstants.IntentStrings.GROUP_DESC,group.getDescription());
                intent.putExtra(UIKitConstants.IntentStrings.GROUP_PASSWORD,group.getPassword());
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
