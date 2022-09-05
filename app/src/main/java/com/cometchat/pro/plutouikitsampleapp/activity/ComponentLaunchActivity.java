package com.cometchat.pro.plutouikitsampleapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.plutouikitsampleapp.R;
import com.cometchat.pro.plutouikitsampleapp.fragments.chats.ConversationListFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.chats.ConversationListItemFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.chats.ConversationsWithMessagesFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.chats.ConversationsFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.groups.GroupListFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.groups.GroupListDataItemFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.groups.GroupsFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.groups.GroupsWithMessagesFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.messages.MessageComposerFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.messages.MessageHeaderFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.messages.MessageListFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.shared.SDK_Derived.DataItemFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.shared.primary.LocalizeFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.shared.primary.SoundManagerFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.shared.primary.ThemeFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.shared.secondary.AvatarFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.shared.secondary.BadgeCountFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.shared.secondary.MessageReceiptFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.shared.secondary.StatusIndicatorFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.users.UserListFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.users.UserListDataItemFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.users.UsersFragment;
import com.cometchat.pro.plutouikitsampleapp.fragments.users.UsersWithMessagesFragment;
import com.cometchatworkspace.components.messages.message_list.CometChatMessages;
import com.cometchatworkspace.resources.utils.Utils;

public class ComponentLaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_launch);

        Utils.setStatusBarColor(this,getResources().getColor(R.color.app_background));
        int id = getIntent().getIntExtra("component", 0);

        if (id == R.id.conversationWithMessages) {
            loadFragment(new ConversationsWithMessagesFragment());
        } else if (id == R.id.conversations) {
            loadFragment(new ConversationsFragment());
        } else if (id == R.id.conversationList) {
            loadFragment(new ConversationListFragment());
        } else if (id == R.id.conversationListItem) {
            loadFragment(new ConversationListItemFragment());
        } else if (id == R.id.userWithMessages) {
            loadFragment(new UsersWithMessagesFragment());
        } else if (id == R.id.users) {
            loadFragment(new UsersFragment());
        } else if (id == R.id.userList) {
            loadFragment(new UserListFragment());
        } else if (id == R.id.usersDataItem) {
            loadFragment(new UserListDataItemFragment());
        } else if (id == R.id.groupWithMessages) {
            loadFragment(new GroupsWithMessagesFragment());
        } else if (id == R.id.groups) {
            loadFragment(new GroupsFragment());
        } else if (id == R.id.groupList) {
            loadFragment(new GroupListFragment());
        } else if (id == R.id.groupsDataItem) {
            loadFragment(new GroupListDataItemFragment());
        } else if (id == R.id.messages) {
            CometChat.getUser("superhero5", new CometChat.CallbackListener<User>() {
                @Override
                public void onSuccess(User user) {
                    CometChatMessages messagesFragment = new CometChatMessages();
                    messagesFragment.setUser(user);
                    loadFragment(messagesFragment);
                }

                @Override
                public void onError(CometChatException e) {

                }
            });
        } else if (id == R.id.messageList) {
            loadFragment(new MessageListFragment());
        } else if (id == R.id.messageHeader) {
            loadFragment(new MessageHeaderFragment());
        } else if (id == R.id.messageComposer) {
            loadFragment(new MessageComposerFragment());
        } else if (id == R.id.avatar) {
            loadFragment(new AvatarFragment());
        } else if (id == R.id.badgeCount) {
            loadFragment(new BadgeCountFragment());
        } else if (id == R.id.messageReceipt) {
            loadFragment(new MessageReceiptFragment());
        } else if (id == R.id.statusIndicator) {
            loadFragment(new StatusIndicatorFragment());
        } else if (id == R.id.soundManager) {
            loadFragment(new SoundManagerFragment());
        } else if (id == R.id.theme) {
            loadFragment(new ThemeFragment());
        } else if (id == R.id.localize) {
            loadFragment(new LocalizeFragment());
        } else if (id == R.id.derivedConversationListItem) {
            loadFragment(new ConversationListItemFragment());
        } else if (id == R.id.derivedDataItem) {
            loadFragment(new DataItemFragment());
        }

    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

}