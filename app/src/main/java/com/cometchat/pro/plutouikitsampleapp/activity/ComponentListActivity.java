package com.cometchat.pro.plutouikitsampleapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cometchat.pro.plutouikitsampleapp.R;
import com.cometchat.pro.plutouikitsampleapp.constants.StringConstants;
import com.cometchatworkspace.resources.utils.Utils;

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
            }
        }
        //back
        findViewById(R.id.backIcon).setOnClickListener(view -> onBackPressed());

        //chats
        findViewById(R.id.conversationWithMessages).setOnClickListener(view -> handleIntent(R.id.conversationWithMessages));
        findViewById(R.id.conversations).setOnClickListener(view -> handleIntent(R.id.conversations));
        findViewById(R.id.conversationList).setOnClickListener(view -> handleIntent(R.id.conversationList));
        findViewById(R.id.conversationListItem).setOnClickListener(view -> handleIntent(R.id.conversationListItem));

        //users
        findViewById(R.id.userWithMessages).setOnClickListener(view -> handleIntent(R.id.userWithMessages));
        findViewById(R.id.users).setOnClickListener(view -> handleIntent(R.id.users));
        findViewById(R.id.userList).setOnClickListener(view -> handleIntent(R.id.userList));
        findViewById(R.id.usersDataItem).setOnClickListener(view -> handleIntent(R.id.usersDataItem));

        //groups
        findViewById(R.id.groupWithMessages).setOnClickListener(view -> handleIntent(R.id.groupWithMessages));
        findViewById(R.id.groups).setOnClickListener(view -> handleIntent(R.id.groups));
        findViewById(R.id.groupList).setOnClickListener(view -> handleIntent(R.id.groupList));
        findViewById(R.id.groupsDataItem).setOnClickListener(view -> handleIntent(R.id.groupsDataItem));

        //messages
        findViewById(R.id.messages).setOnClickListener(view -> handleIntent(R.id.messages));
        findViewById(R.id.messageList).setOnClickListener(view -> handleIntent(R.id.messageList));
        findViewById(R.id.messageHeader).setOnClickListener(view -> handleIntent(R.id.messageHeader));
        findViewById(R.id.messageComposer).setOnClickListener(view -> handleIntent(R.id.messageComposer));

        //shared

        //secondary
        findViewById(R.id.avatar).setOnClickListener(view -> handleIntent(R.id.avatar));
        findViewById(R.id.badgeCount).setOnClickListener(view -> handleIntent(R.id.badgeCount));
        findViewById(R.id.messageReceipt).setOnClickListener(view -> handleIntent(R.id.messageReceipt));
        findViewById(R.id.statusIndicator).setOnClickListener(view -> handleIntent(R.id.statusIndicator));

        //SDK derived
        findViewById(R.id.derivedConversationListItem).setOnClickListener(view -> handleIntent(R.id.derivedConversationListItem));
        findViewById(R.id.derivedDataItem).setOnClickListener(view -> handleIntent(R.id.derivedDataItem));

        //primary
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