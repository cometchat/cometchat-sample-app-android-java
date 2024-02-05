package com.cometchat.javasampleapp.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;
import com.cometchat.javasampleapp.fragments.calls.CallButtonFragment;
import com.cometchat.javasampleapp.fragments.calls.CallLogDetailsFragment;
import com.cometchat.javasampleapp.fragments.calls.CallLogHistoryFragment;
import com.cometchat.javasampleapp.fragments.calls.CallLogParticipantsFragment;
import com.cometchat.javasampleapp.fragments.calls.CallLogRecordingFragment;
import com.cometchat.javasampleapp.fragments.calls.CallLogWithDetailsFragment;
import com.cometchat.javasampleapp.fragments.calls.CallLogsFragment;
import com.cometchat.javasampleapp.fragments.conversations.ContactsFragment;
import com.cometchat.javasampleapp.fragments.conversations.ConversationsFragment;
import com.cometchat.javasampleapp.fragments.conversations.ConversationsWithMessagesFragment;
import com.cometchat.javasampleapp.fragments.groups.AddMemberFragment;
import com.cometchat.javasampleapp.fragments.groups.BannedMembersFragment;
import com.cometchat.javasampleapp.fragments.groups.CreateGroupFragment;
import com.cometchat.javasampleapp.fragments.groups.GroupDetailsFragment;
import com.cometchat.javasampleapp.fragments.groups.GroupMembersFragment;
import com.cometchat.javasampleapp.fragments.groups.GroupsFragment;
import com.cometchat.javasampleapp.fragments.groups.GroupsWithMessagesFragment;
import com.cometchat.javasampleapp.fragments.groups.JoinProtectedGroupFragment;
import com.cometchat.javasampleapp.fragments.groups.TransferOwnershipFragment;
import com.cometchat.javasampleapp.fragments.messages.MessageComposerFragment;
import com.cometchat.javasampleapp.fragments.messages.MessageHeaderFragment;
import com.cometchat.javasampleapp.fragments.messages.MessageInformationFragment;
import com.cometchat.javasampleapp.fragments.messages.MessageListFragment;
import com.cometchat.javasampleapp.fragments.messages.MessagesFragment;
import com.cometchat.javasampleapp.fragments.shared.resources.LocalizeFragment;
import com.cometchat.javasampleapp.fragments.shared.resources.SoundManagerFragment;
import com.cometchat.javasampleapp.fragments.shared.resources.ThemeFragment;
import com.cometchat.javasampleapp.fragments.shared.views.AudioBubbleFragment;
import com.cometchat.javasampleapp.fragments.shared.views.AvatarFragment;
import com.cometchat.javasampleapp.fragments.shared.views.BadgeCountFragment;
import com.cometchat.javasampleapp.fragments.shared.views.CardBubbleFragment;
import com.cometchat.javasampleapp.fragments.shared.views.FileBubbleFragment;
import com.cometchat.javasampleapp.fragments.shared.views.FormBubbleFragment;
import com.cometchat.javasampleapp.fragments.shared.views.ImageBubbleFragment;
import com.cometchat.javasampleapp.fragments.shared.views.ListItemFragment;
import com.cometchat.javasampleapp.fragments.shared.views.MediaRecorderFragment;
import com.cometchat.javasampleapp.fragments.shared.views.MessageReceiptFragment;
import com.cometchat.javasampleapp.fragments.shared.views.SchedulerBubbleFragment;
import com.cometchat.javasampleapp.fragments.shared.views.StatusIndicatorFragment;
import com.cometchat.javasampleapp.fragments.shared.views.TextBubbleFragment;
import com.cometchat.javasampleapp.fragments.shared.views.VideoBubbleFragment;
import com.cometchat.javasampleapp.fragments.users.UsersDetailsFragment;
import com.cometchat.javasampleapp.fragments.users.UsersFragment;
import com.cometchat.javasampleapp.fragments.users.UsersWithMessagesFragment;

public class ComponentLaunchActivity extends AppCompatActivity {
    private LinearLayout parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_launch);
        int id = getIntent().getIntExtra("component", 0);
        parentView = findViewById(R.id.container);
        setUpUI();
        if (id == R.id.conversationWithMessages) {
            loadFragment(new ConversationsWithMessagesFragment());
        } else if (id == R.id.conversations) {
            loadFragment(new ConversationsFragment());
        } else if (id == R.id.userWithMessages) {
            loadFragment(new UsersWithMessagesFragment());
        } else if (id == R.id.users) {
            loadFragment(new UsersFragment());
        } else if (id == R.id.user_details) {
            loadFragment(new UsersDetailsFragment());
        } else if (id == R.id.groupWithMessages) {
            loadFragment(new GroupsWithMessagesFragment());
        } else if (id == R.id.call_button) {
            loadFragment(new CallButtonFragment());
        } else if (id == R.id.groups) {
            loadFragment(new GroupsFragment());
        } else if (id == R.id.create_group) {
            loadFragment(new CreateGroupFragment());
        } else if (id == R.id.join_protected_group) {
            loadFragment(new JoinProtectedGroupFragment());
        } else if (id == R.id.group_member) {
            loadFragment(new GroupMembersFragment());
        } else if (id == R.id.add_member) {
            loadFragment(new AddMemberFragment());
        } else if (id == R.id.transfer_ownership) {
            loadFragment(new TransferOwnershipFragment());
        } else if (id == R.id.banned_members) {
            loadFragment(new BannedMembersFragment());
        } else if (id == R.id.group_details) {
            loadFragment(new GroupDetailsFragment());
        } else if (id == R.id.messages) {
            loadFragment(new MessagesFragment());
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
        } else if (id == R.id.list_item) {
            loadFragment(new ListItemFragment());
        } else if (id == R.id.text_bubble) {
            loadFragment(new TextBubbleFragment());
        } else if (id == R.id.image_bubble) {
            loadFragment(new ImageBubbleFragment());
        } else if (id == R.id.video_bubble) {
            loadFragment(new VideoBubbleFragment());
        } else if (id == R.id.audio_bubble) {
            loadFragment(new AudioBubbleFragment());
        } else if (id == R.id.files_bubble) {
            loadFragment(new FileBubbleFragment());
        } else if (id == R.id.form_bubble) {
            loadFragment(new FormBubbleFragment());
        } else if (id == R.id.card_bubble) {
            loadFragment(new CardBubbleFragment());
        } else if (id == R.id.scheduler_bubble) {
            loadFragment(new SchedulerBubbleFragment());
        } else if (id == R.id.media_recorder) {
            loadFragment(new MediaRecorderFragment());
        } else if (id == R.id.contacts) {
            loadFragment(new ContactsFragment());
        } else if (id == R.id.messageInformation) {
            loadFragment(new MessageInformationFragment());
        } else if (id == R.id.call_logs) {
            loadFragment(new CallLogsFragment());
        } else if (id == R.id.call_logs_details) {
            loadFragment(new CallLogDetailsFragment());
        } else if (id == R.id.call_logs_with_details) {
            loadFragment(new CallLogWithDetailsFragment());
        } else if (id == R.id.call_log_participants) {
            loadFragment(new CallLogParticipantsFragment());
        } else if (id == R.id.call_log_recording) {
            loadFragment(new CallLogRecordingFragment());
        } else if (id == R.id.call_log_history) {
            loadFragment(new CallLogHistoryFragment());
        }
    }

    private void setUpUI() {
        if (AppUtils.isNightMode(this)) {
            Utils.setStatusBarColor(this, ContextCompat.getColor(this, R.color.app_background_dark));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.app_background_dark)));
        } else {
            Utils.setStatusBarColor(this, getResources().getColor(R.color.app_background));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
        }
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
    }

}