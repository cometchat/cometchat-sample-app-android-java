package com.cometchat.javasampleapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.javasampleapp.R;
import com.cometchat.javasampleapp.constants.StringConstants;

public class ComponentListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_list);
        Utils.setStatusBarColor(this, getResources().getColor(R.color.app_background));
        TextView title = findViewById(R.id.title);
        if (getIntent() != null) {
            title.setText(getIntent().getStringExtra(StringConstants.MODULE));
            if (getIntent().getStringExtra(StringConstants.MODULE).equalsIgnoreCase(StringConstants.CONVERSATIONS)) {
                findViewById(R.id.moduleChats).setVisibility(View.VISIBLE);
            } else if (getIntent().getStringExtra(StringConstants.MODULE).equalsIgnoreCase(StringConstants.USERS)) {
                findViewById(R.id.moduleUsers).setVisibility(View.VISIBLE);
            } else if (getIntent().getStringExtra(StringConstants.MODULE).equalsIgnoreCase(StringConstants.GROUPS)) {
                findViewById(R.id.noduleGroups).setVisibility(View.VISIBLE);
            } else if (getIntent().getStringExtra(StringConstants.MODULE).equalsIgnoreCase(StringConstants.MESSAGES)) {
                findViewById(R.id.moduleMessages).setVisibility(View.VISIBLE);
            } else if (getIntent().getStringExtra(StringConstants.MODULE).equalsIgnoreCase(StringConstants.SHARED)) {
                findViewById(R.id.shared).setVisibility(View.VISIBLE);
            } else if (getIntent().getStringExtra(StringConstants.MODULE).equalsIgnoreCase(StringConstants.CALLS)) {
                findViewById(R.id.module_calls).setVisibility(View.VISIBLE);
            }
        }
        //back
        findViewById(R.id.backIcon).setOnClickListener(view -> onBackPressed());

        //chats
        findViewById(R.id.conversationWithMessages).setOnClickListener(view -> handleIntent(R.id.conversationWithMessages));
        findViewById(R.id.conversations).setOnClickListener(view -> handleIntent(R.id.conversations));

        //users
        findViewById(R.id.userWithMessages).setOnClickListener(view -> handleIntent(R.id.userWithMessages));
        findViewById(R.id.users).setOnClickListener(view -> handleIntent(R.id.users));
        findViewById(R.id.user_details).setOnClickListener(view -> handleIntent(R.id.user_details));

        //groups
        findViewById(R.id.groupWithMessages).setOnClickListener(view -> handleIntent(R.id.groupWithMessages));
        findViewById(R.id.groups).setOnClickListener(view -> handleIntent(R.id.groups));
        findViewById(R.id.create_group).setOnClickListener(view -> handleIntent(R.id.create_group));
        findViewById(R.id.join_protected_group).setOnClickListener(view -> handleIntent(R.id.join_protected_group));
        findViewById(R.id.group_member).setOnClickListener(view -> handleIntent(R.id.group_member));
        findViewById(R.id.add_member).setOnClickListener(view -> handleIntent(R.id.add_member));
        findViewById(R.id.transfer_ownership).setOnClickListener(view -> handleIntent(R.id.transfer_ownership));
        findViewById(R.id.banned_members).setOnClickListener(view -> handleIntent(R.id.banned_members));
        findViewById(R.id.group_details).setOnClickListener(view -> handleIntent(R.id.group_details));

        //messages
        findViewById(R.id.messages).setOnClickListener(view -> handleIntent(R.id.messages));
        findViewById(R.id.messageList).setOnClickListener(view -> handleIntent(R.id.messageList));
        findViewById(R.id.messageHeader).setOnClickListener(view -> handleIntent(R.id.messageHeader));
        findViewById(R.id.messageComposer).setOnClickListener(view -> handleIntent(R.id.messageComposer));

        //calls
        findViewById(R.id.call_button).setOnClickListener(view -> handleIntent(R.id.call_button));

        //shared

        //views
        findViewById(R.id.avatar).setOnClickListener(view -> handleIntent(R.id.avatar));
        findViewById(R.id.badgeCount).setOnClickListener(view -> handleIntent(R.id.badgeCount));
        findViewById(R.id.messageReceipt).setOnClickListener(view -> handleIntent(R.id.messageReceipt));
        findViewById(R.id.statusIndicator).setOnClickListener(view -> handleIntent(R.id.statusIndicator));
        findViewById(R.id.list_item).setOnClickListener(view -> handleIntent(R.id.list_item));
        findViewById(R.id.text_bubble).setOnClickListener(view -> handleIntent(R.id.text_bubble));
        findViewById(R.id.image_bubble).setOnClickListener(view -> handleIntent(R.id.image_bubble));
        findViewById(R.id.video_bubble).setOnClickListener(view -> handleIntent(R.id.video_bubble));
        findViewById(R.id.audio_bubble).setOnClickListener(view -> handleIntent(R.id.audio_bubble));
        findViewById(R.id.files_bubble).setOnClickListener(view -> handleIntent(R.id.files_bubble));

        //resources
        findViewById(R.id.soundManager).setOnClickListener(view -> handleIntent(R.id.soundManager));
        findViewById(R.id.theme).setOnClickListener(view -> handleIntent(R.id.theme));
        findViewById(R.id.localize).setOnClickListener(view -> handleIntent(R.id.localize));


    }

    private void handleIntent(int id) {
        Intent intent = new Intent(this, ComponentLaunchActivity.class);
        intent.putExtra("component", id);
        startActivity(intent);
    }

}