package com.cometchat.pro.uikit.ui_components.groups.banned_members;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cometchat.pro.uikit.R;
import com.google.android.material.appbar.MaterialToolbar;

import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_settings.FeatureRestriction;

public class CometChatBanMembersActivity extends AppCompatActivity {

    private String guid,gName;
    private String loggedInUserScope;
    private MaterialToolbar banToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cometchat_ban_members);
        banToolbar = findViewById(R.id.banToolbar);
        banToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        handleIntent();
        if (FeatureRestriction.getColor()!=null)
            getWindow().setStatusBarColor(Color.parseColor(FeatureRestriction.getColor()));
        CometChatBanMembers banFragment = new CometChatBanMembers();
        Bundle bundle = new Bundle();
        bundle.putString(UIKitConstants.IntentStrings.GUID,guid);
        bundle.putString(UIKitConstants.IntentStrings.GROUP_NAME,gName);
        bundle.putString(UIKitConstants.IntentStrings.MEMBER_SCOPE,loggedInUserScope);
        banFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.ban_member_frame,banFragment).commit();
    }

    public void handleIntent() {
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.GUID)) {
            guid = getIntent().getStringExtra(UIKitConstants.IntentStrings.GUID);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.GROUP_NAME)) {
            gName = getIntent().getStringExtra(UIKitConstants.IntentStrings.GROUP_NAME);
            banToolbar.setTitle(String.format(getResources().getString(R.string.ban_member_of_group),gName));
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.MEMBER_SCOPE)) {
            loggedInUserScope = getIntent().getStringExtra(UIKitConstants.IntentStrings.MEMBER_SCOPE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
