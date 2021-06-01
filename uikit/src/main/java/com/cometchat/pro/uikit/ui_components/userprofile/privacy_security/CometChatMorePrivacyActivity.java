package com.cometchat.pro.uikit.ui_components.userprofile.privacy_security;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cometchat.pro.core.BlockedUsersRequest;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.shared.CometChatSnackBar;
import com.cometchat.pro.uikit.ui_resources.utils.CometChatError;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import com.cometchat.pro.uikit.ui_components.users.block_users.CometChatBlockUserListActivity;
import com.cometchat.pro.uikit.ui_resources.utils.FontUtils;
import com.cometchat.pro.uikit.ui_settings.FeatureRestriction;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;

public class CometChatMorePrivacyActivity extends AppCompatActivity {

    private TextView tvBlockUserCount;

    private BlockedUsersRequest blockedUsersRequest;

    private TextView blockUserTv;

    private View divider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cometchat_more_privacy);

        CometChatError.init(this);
        blockUserTv = findViewById(R.id.blocked_user_tv);
        tvBlockUserCount = findViewById(R.id.tv_blocked_user_count);
        MaterialToolbar toolbar = findViewById(R.id.privacy_toolbar);
        divider = findViewById(R.id.divider);
        setSupportActionBar(toolbar);

         if (getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (FeatureRestriction.getColor()!=null)
            getWindow().setStatusBarColor(
                    Color.parseColor(FeatureRestriction.getColor()));
         if (Utils.changeToolbarFont(toolbar)!=null){
             Utils.changeToolbarFont(toolbar).setTypeface(FontUtils.getInstance(this).getTypeFace(FontUtils.robotoMedium));
         }
         if(Utils.isDarkMode(this)) {
             divider.setBackgroundColor(getResources().getColor(R.color.grey));
             blockUserTv.setTextColor(getResources().getColor(R.color.textColorWhite));
         } else {
             divider.setBackgroundColor(getResources().getColor(R.color.light_grey));
             blockUserTv.setTextColor(getResources().getColor(R.color.primaryTextColor));
         }
         getBlockCount();
    }

    public void blockUserList(View view) {
        startActivity(new Intent(this, CometChatBlockUserListActivity.class));
    }

    public void getBlockCount() {

         blockedUsersRequest = new BlockedUsersRequest.BlockedUsersRequestBuilder().setDirection(BlockedUsersRequest.DIRECTION_BLOCKED_BY_ME).setLimit(100).build();
         blockedUsersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {

                if (users.size() == 0) {
                    tvBlockUserCount.setText("");
                } else if (users.size() < 2) {
                    tvBlockUserCount.setText(users.size() +" "+getResources().getString(R.string.user));
                } else {
                    tvBlockUserCount.setText(users.size() + " "+getResources().getString(R.string.users));
                }

            }

            @Override
            public void onError(CometChatException e) {
               CometChatSnackBar.show(CometChatMorePrivacyActivity.this,
                        tvBlockUserCount,getResources().getString(R.string.block_user_list_error)+", "
                               + CometChatError.localized(e), CometChatSnackBar.ERROR);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        blockedUsersRequest=null;
        getBlockCount();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

         if (item.getItemId()==android.R.id.home)
             onBackPressed();

        return super.onOptionsItemSelected(item);
    }
}
