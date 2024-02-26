package com.cometchat.javasampleapp.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;
import com.cometchat.javasampleapp.constants.StringConstants;

public class ComponentListActivity extends AppCompatActivity {
    LinearLayout parentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_list);
        parentView = findViewById(R.id.parent_view);
        TextView title = findViewById(R.id.title);
        setUpUI();
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
        findViewById(R.id.contacts).setOnClickListener(view -> handleIntent(R.id.contacts));

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
        findViewById(R.id.messageInformation).setOnClickListener(view -> handleIntent(R.id.messageInformation));

        //calls
        findViewById(R.id.call_button).setOnClickListener(view -> handleIntent(R.id.call_button));
        findViewById(R.id.call_logs).setOnClickListener(view -> handleIntent(R.id.call_logs));
        findViewById(R.id.call_logs_details).setOnClickListener(view -> handleIntent(R.id.call_logs_details));
        findViewById(R.id.call_logs_with_details).setOnClickListener(view -> handleIntent(R.id.call_logs_with_details));
        findViewById(R.id.call_log_participants).setOnClickListener(view -> handleIntent(R.id.call_log_participants));
        findViewById(R.id.call_log_recording).setOnClickListener(view -> handleIntent(R.id.call_log_recording));
        findViewById(R.id.call_log_history).setOnClickListener(view -> handleIntent(R.id.call_log_history));

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
        findViewById(R.id.form_bubble).setOnClickListener(view -> handleIntent(R.id.form_bubble));
        findViewById(R.id.card_bubble).setOnClickListener(view -> handleIntent(R.id.card_bubble));
        findViewById(R.id.scheduler_bubble).setOnClickListener(view -> handleIntent(R.id.scheduler_bubble));
        findViewById(R.id.media_recorder).setOnClickListener(view -> handleIntent(R.id.media_recorder));

        //resources
        findViewById(R.id.soundManager).setOnClickListener(view -> handleIntent(R.id.soundManager));
        findViewById(R.id.theme).setOnClickListener(view -> handleIntent(R.id.theme));
        findViewById(R.id.localize).setOnClickListener(view -> handleIntent(R.id.localize));
    }

    private void setUpUI() {
        if(AppUtils.isNightMode(this)){
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.backIcon));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_cwm));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_c));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_contacts));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_uwm));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_u));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_ud));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_gwm));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_g));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_cg));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_jp));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_gm));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_ad));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_to));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_bm));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_gd));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_message));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_message_header));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_message_list));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_message_composer));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_message_information));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_call_button));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_audio));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_translate));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_avatar));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_badge_count));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_message_receipt));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_status_indicator));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_text_bubble));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_image_bubble));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_video_bubble));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_audio_bubble));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_file_bubble));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_form_bubble));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_card_bubble));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_scheduler_bubble));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_mic));
            AppUtils.changeIconTintToWhite(this,findViewById(R.id.image_list_item));
            Utils.setStatusBarColor(this, ContextCompat.getColor(this,R.color.app_background_dark));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this,R.color.app_background_dark)));
        }else {
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.backIcon));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_cwm));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_c));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_contacts));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_uwm));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_u));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_ud));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_gwm));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_g));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_cg));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_jp));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_gm));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_ad));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_to));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_bm));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_gd));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_message));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_message_header));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_message_list));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_message_composer));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_message_information));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_call_button));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_audio));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_translate));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_avatar));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_badge_count));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_message_receipt));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_status_indicator));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_text_bubble));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_image_bubble));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_video_bubble));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_audio_bubble));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_file_bubble));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_form_bubble));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_card_bubble));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_scheduler_bubble));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_mic));
            AppUtils.changeIconTintToBlack(this,findViewById(R.id.image_list_item));
            Utils.setStatusBarColor(this, getResources().getColor(R.color.app_background));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
        }
    }
    private void handleIntent(int id) {
        Intent intent = new Intent(this, ComponentLaunchActivity.class);
        intent.putExtra("component", id);
        startActivity(intent);
    }

}