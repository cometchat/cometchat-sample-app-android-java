package com.cometchat.pro.androiduikit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;

public class ComponentListActivity extends AppCompatActivity {

    MaterialCardView cometchatAvatar;
    MaterialCardView cometchatStatusIndicator;
    MaterialCardView cometchatBadgeCount;
    MaterialCardView cometchatUserList;
    MaterialCardView cometchatGroupList;
    MaterialCardView cometchatConversationList;
    MaterialCardView cometchatCallList;
    ImageView backIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_component_list);
        backIcon = findViewById(R.id.backIcon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cometchatAvatar = findViewById(R.id.cometchat_avatar);
        cometchatAvatar.setOnClickListener(view -> {
            Intent intent = new Intent(ComponentListActivity.this, ComponentLoadActivity.class);
            intent.putExtra("screen",R.id.cometchat_avatar);
            startActivity(intent);
        });
        cometchatStatusIndicator = findViewById(R.id.cometchat_status_indicator);
        cometchatStatusIndicator.setOnClickListener(view -> {
            Intent intent = new Intent(ComponentListActivity.this, ComponentLoadActivity.class);
            intent.putExtra("screen",R.id.cometchat_status_indicator);
            startActivity(intent);
        });
        cometchatBadgeCount = findViewById(R.id.cometchat_badge_count);
        cometchatBadgeCount.setOnClickListener(view -> {
            Intent intent = new Intent(ComponentListActivity.this, ComponentLoadActivity.class);
            intent.putExtra("screen",R.id.cometchat_badge_count);
            startActivity(intent);
        });
        cometchatUserList = findViewById(R.id.cometchat_user_view);
        cometchatUserList.setOnClickListener(view -> {
            Intent intent = new Intent(ComponentListActivity.this, ComponentLoadActivity.class);
            intent.putExtra("screen",R.id.cometchat_user_view);
            startActivity(intent);
        });
        cometchatGroupList = findViewById(R.id.cometchat_group_view);
        cometchatGroupList.setOnClickListener(view -> {
            Intent intent = new Intent(ComponentListActivity.this, ComponentLoadActivity.class);
            intent.putExtra("screen",R.id.cometchat_group_view);
            startActivity(intent);
        });
        cometchatConversationList = findViewById(R.id.cometchat_conversation_view);
        cometchatConversationList.setOnClickListener(view -> {
            Intent intent = new Intent(ComponentListActivity.this, ComponentLoadActivity.class);
            intent.putExtra("screen",R.id.cometchat_conversation_view);
            startActivity(intent);
        });
        cometchatCallList = findViewById(R.id.cometchat_callList);
        cometchatCallList.setOnClickListener(view -> {
            Intent intent = new Intent(ComponentListActivity.this,ComponentLoadActivity.class);
            intent.putExtra("screen",R.id.cometchat_callList);
            startActivity(intent);
        });
    }
}
