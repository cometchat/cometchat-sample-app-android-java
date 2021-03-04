package com.cometchat.pro.androiduikit.ComponentFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_components.calls.call_list.CometChatCallList;
import com.cometchat.pro.uikit.ui_components.groups.group_details.CometChatGroupDetailActivity;
import com.cometchat.pro.uikit.ui_components.shared.cometchatCalls.CometChatCalls;
import com.cometchat.pro.uikit.ui_components.users.user_details.CometChatUserDetailScreenActivity;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_resources.utils.CallUtils;
import com.cometchat.pro.uikit.ui_resources.utils.item_clickListener.OnItemClickListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CallListViewFragment extends Fragment {

    private CometChatCalls rvCallList;

    private LinearLayout noCallView;

    private ShimmerFrameLayout shimmerFrameLayout;

    private MessagesRequest messagesRequest;

    private LinearLayoutManager linearLayoutManager;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_list, container, false);
        rvCallList =  view.findViewById(R.id.callList_rv);
        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false
        );
        rvCallList.setLayoutManager(linearLayoutManager);
        shimmerFrameLayout = view.findViewById(R.id.contact_shimmer);
        noCallView = view.findViewById(com.cometchat.pro.uikit.R.id.no_call_vw);
        rvCallList.setItemClickListener(new OnItemClickListener<Call>() {
            @Override
            public void OnItemClick(Call var, int position) {
                if (var.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                    User user;
                    if (var.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                        user =  ((User)var.getCallReceiver());
                    }
                    else
                    {
                        user = var.getSender();
                    }
                    Intent intent = new Intent(getContext(), CometChatUserDetailScreenActivity.class);
                    intent.putExtra(UIKitConstants.IntentStrings.UID, user.getUid());
                    intent.putExtra(UIKitConstants.IntentStrings.NAME, user.getName());
                    intent.putExtra(UIKitConstants.IntentStrings.AVATAR, user.getAvatar());
                    intent.putExtra(UIKitConstants.IntentStrings.STATUS, user.getStatus());
                    intent.putExtra(UIKitConstants.IntentStrings.IS_BLOCKED_BY_ME, user.isBlockedByMe());
                    intent.putExtra(UIKitConstants.IntentStrings.FROM_CALL_LIST,true);
                    startActivity(intent);
                }
                else {
                    Group group;
                    group = ((Group)var.getCallReceiver());
                    Intent intent = new Intent(getContext(), CometChatGroupDetailActivity.class);
                    intent.putExtra(UIKitConstants.IntentStrings.GUID, group.getGuid());
                    intent.putExtra(UIKitConstants.IntentStrings.NAME, group.getName());
                    intent.putExtra(UIKitConstants.IntentStrings.AVATAR, group.getIcon());
                    intent.putExtra(UIKitConstants.IntentStrings.MEMBER_SCOPE, group.getScope());
                    intent.putExtra(UIKitConstants.IntentStrings.MEMBER_COUNT,group.getMembersCount());
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_OWNER, group.getOwner());
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_DESC,group.getDescription());
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_PASSWORD,group.getPassword());
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_TYPE,group.getGroupType());
                    startActivity(intent);
                }
            }
        });
        rvCallList.setItemCallClickListener(new OnItemClickListener<Call>() {
            @Override
            public void OnItemClick(Call var, int position) {
                CometChat.initiateCall(var, new CometChat.CallbackListener<Call>() {
                    @Override
                    public void onSuccess(Call call) {
                        Log.e( "onSuccess: ",call.toString());
                        if (var.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {
                            User user;
                            if (var.getSender().getUid().equals(CometChat.getLoggedInUser().getUid())) {
                                user = ((User) var.getCallReceiver());
                            } else {
                                user = var.getSender();
                            }
                            CallUtils.startCallIntent(getContext(), user, CometChatConstants.CALL_TYPE_AUDIO, true, call.getSessionId());
                        } else
                            CallUtils.startGroupCallIntent(getContext(),((Group)call.getCallReceiver()),CometChatConstants.CALL_TYPE_AUDIO,true,call.getSessionId());
                    }

                    @Override
                    public void onError(CometChatException e) {

                    }
                });
            }
        });
        rvCallList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!rvCallList.canScrollVertically(1))
                {
                    getCallList();
                }
            }
        });
        getCallList();
        return view;
    }

    /**
     * This method is used to get the fetch the call List.
     */
    private void getCallList() {
        if (messagesRequest == null)
        {
            messagesRequest = new MessagesRequest.MessagesRequestBuilder().setCategories(Arrays.asList(CometChatConstants.CATEGORY_CALL)).setLimit(30).build();
        }

        messagesRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
            @Override
            public void onSuccess(List<BaseMessage> baseMessages) {
                Collections.reverse(baseMessages);
                rvCallList.setCallList(baseMessages);
                if (rvCallList.size()!=0) {
                    hideShimmer();
                    noCallView.setVisibility(View.GONE);
                }
                else
                    noCallView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(CometChatException e) {
                Log.e( "onError: ",e.getMessage() );
                if (rvCallList!=null)
                    Snackbar.make(rvCallList, com.cometchat.pro.uikit.R.string.call_list_error,Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void hideShimmer() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
    }

}
