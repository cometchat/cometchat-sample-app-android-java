package com.cometchat.pro.androiduikit.ComponentFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;

import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.androiduikit.databinding.FragmentConversationListBinding;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.ConversationsRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_resources.utils.item_clickListener.OnItemClickListener;

import java.util.List;


public class ConversationListViewFragment extends Fragment {

    FragmentConversationListBinding conversationBinding;
    ObservableArrayList<Conversation> conversationlist = new ObservableArrayList<>();
    ConversationsRequest conversationsRequest;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        conversationBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_conversation_list,container,false);
        getConversations();
        conversationBinding.setConversationList(conversationlist);
        conversationBinding.cometchatConversationList.setItemClickListener(new OnItemClickListener<Conversation>()
        {
            @Override
            public void OnItemClick(Conversation conversation, int position) {
                Intent intent = new Intent(getContext(), CometChatMessageListActivity.class);
                intent.putExtra(UIKitConstants.IntentStrings.TYPE,conversation.getConversationType());
                if (conversation.getConversationType().equals(CometChatConstants.CONVERSATION_TYPE_GROUP))
                {
                    intent.putExtra(UIKitConstants.IntentStrings.NAME,((Group)conversation.getConversationWith()).getName());
                    intent.putExtra(UIKitConstants.IntentStrings.GUID,((Group)conversation.getConversationWith()).getGuid());
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_OWNER,((Group)conversation.getConversationWith()).getOwner());
                    intent.putExtra(UIKitConstants.IntentStrings.AVATAR,((Group)conversation.getConversationWith()).getIcon());
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_TYPE,((Group)conversation.getConversationWith()).getGroupType());
                    intent.putExtra(UIKitConstants.IntentStrings.MEMBER_COUNT,((Group)conversation.getConversationWith()).getMembersCount());
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_DESC,((Group)conversation.getConversationWith()).getDescription());
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_PASSWORD,((Group)conversation.getConversationWith()).getPassword());
                }
                else
                {
                    intent.putExtra(UIKitConstants.IntentStrings.NAME,((User)conversation.getConversationWith()).getName());
                    intent.putExtra(UIKitConstants.IntentStrings.UID,((User)conversation.getConversationWith()).getUid());
                    intent.putExtra(UIKitConstants.IntentStrings.AVATAR,((User)conversation.getConversationWith()).getAvatar());
                    intent.putExtra(UIKitConstants.IntentStrings.STATUS,((User)conversation.getConversationWith()).getStatus());
                }
                startActivity(intent);
            }

            @Override
            public void OnItemLongClick(Conversation var, int position) {
                super.OnItemLongClick(var, position);
            }
        });
        return conversationBinding.getRoot();
    }

    private void getConversations() {
        if (conversationsRequest==null)
        {
            conversationsRequest = new ConversationsRequest.ConversationsRequestBuilder().setLimit(30).build();
        }
        conversationsRequest.fetchNext(new CometChat.CallbackListener<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                conversationBinding.contactShimmer.stopShimmer();
                conversationBinding.contactShimmer.setVisibility(View.GONE);
                conversationlist.addAll(conversations);
            }

            @Override
            public void onError(CometChatException e) {
                Log.e( "onError: ",e.getMessage());
            }
        });
    }
}
