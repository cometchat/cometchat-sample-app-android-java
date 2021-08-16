package com.cometchat.pro.uikit.ui_components.chats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.groups.group_list.CometChatGroupList;
import com.cometchat.pro.uikit.ui_components.shared.cometchatSharedMedia.adapter.TabAdapter;
import com.cometchat.pro.uikit.ui_components.users.user_list.CometChatUserList;
import com.cometchat.pro.uikit.ui_settings.UIKitSettings;
import com.cometchat.pro.uikit.ui_settings.enums.ConversationMode;
import com.google.android.material.tabs.TabLayout;

public class CometChatStartConversation extends AppCompatActivity {

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private TabAdapter adapter;

    private TextView title;

    private ImageView backIcon;

    private String conversationType = UIKitSettings.getConversationsMode().toString();

    public static void launch(Context context) {
        context.startActivity(new Intent(context,CometChatStartConversation.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comet_chat_start_conversation);
        title = findViewById(R.id.title);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CometChatUserList cometChatUserList = new CometChatUserList();
        cometChatUserList.setTitleVisible(false);
        CometChatGroupList cometChatGroupList = new CometChatGroupList();
        cometChatGroupList.setTitleVisible(false);
        cometChatGroupList.setGroupCreateVisible(false);
        if (conversationType.equalsIgnoreCase(ConversationMode.ALL_CHATS.toString())) {
            adapter.addFragment(cometChatUserList, getString(R.string.users));
            adapter.addFragment(cometChatGroupList, getString(R.string.groups));
        } else if (conversationType.equalsIgnoreCase(ConversationMode.GROUP.toString())) {
            title.setText(getString(R.string.select_group));
            tabLayout.setVisibility(View.GONE);
            adapter.addFragment(cometChatGroupList, getString(R.string.groups));
        } else {
            title.setText(getString(R.string.select_user));
            tabLayout.setVisibility(View.GONE);
            adapter.addFragment(cometChatUserList, getString(R.string.users));
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0) {
                    title.setText(getString(R.string.select_user));
                } else {
                    title.setText(getString(R.string.select_group));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}