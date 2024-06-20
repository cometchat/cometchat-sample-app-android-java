package com.cometchat.pro.androiduikit;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.cometchat.pro.core.AppSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.androiduikit.constants.AppConfig;
import com.cometchat.pro.uikit.ui_components.calls.call_manager.listener.CometChatCallListener;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.cometchat.pro.uikit.ui_settings.UIKitSettings;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private MaterialCardView user1;

    private MaterialCardView user2;

    private MaterialCardView user3;

    private MaterialCardView user4;

    private AppCompatImageView ivLogo;
    private ProgressBar progressBar;
    private AppCompatTextView tvCometChat;
    private LinearLayout gridLayoutContainer;
    private TextView stateMessage;
    private LinearLayout stateLayout;
    private TextView user1Name, user2Name, user3Name, user4Name;
    private ImageView user1Avatar, user2Avatar, user3Avatar, user4Avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        stateMessage = findViewById(R.id.state_message);
        stateLayout = findViewById(R.id.state_layout);
        gridLayoutContainer = findViewById(R.id.grid_layout_container);
        user1 = findViewById(R.id.user1);
        user2 = findViewById(R.id.user2);
        user3 = findViewById(R.id.user3);
        user4 = findViewById(R.id.user4);
        ivLogo = findViewById(R.id.ivLogo);
        tvCometChat = findViewById(R.id.tvComet);
        user1Name = findViewById(R.id.user1_name);
        user2Name = findViewById(R.id.user2_name);
        user3Name = findViewById(R.id.user3_name);
        user4Name = findViewById(R.id.user4_name);
        user1Avatar = findViewById(R.id.user1_avatar_image);
        user2Avatar = findViewById(R.id.user2_avatar_image);
        user3Avatar = findViewById(R.id.user3_avatar_image);
        user4Avatar = findViewById(R.id.user4_avatar_image);

        user1.setVisibility(View.GONE);
        user2.setVisibility(View.GONE);
        user3.setVisibility(View.GONE);
        user4.setVisibility(View.GONE);

        gridLayoutContainer.setVisibility(View.INVISIBLE);
        stateMessage.setText(R.string.please_wait);
        progressBar.setVisibility(View.VISIBLE);

        AppUtils.fetchSampleUsers(new CometChat.CallbackListener<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                if (!users.isEmpty()) {
                    setUsers(users);
                } else {
                    stateLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    stateMessage.setText(R.string.no_sample_users_available);
                }
            }

            @Override
            public void onError(CometChatException e) {
                setUsers(AppUtils.processSampleUserList(AppUtils.loadJSONFromAsset(MainActivity.this)));
            }
        });

        user1.setOnClickListener(view -> {
            findViewById(R.id.user1Progressbar).setVisibility(View.VISIBLE);
            login(user1.getTag().toString());
        });
        user2.setOnClickListener(view -> {
            findViewById(R.id.user2Progressbar).setVisibility(View.VISIBLE);
            login(user2.getTag().toString());
        });
        user3.setOnClickListener(view -> {
            findViewById(R.id.user3Progressbar).setVisibility(View.VISIBLE);
            login(user3.getTag().toString());
        });
        user4.setOnClickListener(view -> {
            findViewById(R.id.user4Progressbar).setVisibility(View.VISIBLE);
            login(user4.getTag().toString());
        });

        findViewById(R.id.login).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));

        if(Utils.isDarkMode(this)) {
            ivLogo.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            tvCometChat.setTextColor(getResources().getColor(R.color.textColorWhite));
        }
        else {
            ivLogo.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryTextColor)));
            tvCometChat.setTextColor(getResources().getColor(R.color.primaryTextColor));
        }
    }

    private void setUsers(List<User> users) {
        progressBar.setVisibility(View.GONE);
        stateLayout.setVisibility(View.GONE);
        gridLayoutContainer.setVisibility(View.VISIBLE);
        for (int i = 0; i < users.size(); i++) {
            if (i == 0) {
                user1Name.setText(users.get(i).getName());
                Glide.with(this).load(users.get(i).getAvatar()).error(R.drawable.ironman).into(user1Avatar);
                user1.setTag(users.get(i).getUid());
                user1.setVisibility(View.VISIBLE);
            } else if (i == 1) {
                user2Name.setText(users.get(i).getName());
                Glide.with(this).load(users.get(i).getAvatar()).error(R.drawable.captainamerica).into(user2Avatar);
                user2.setTag(users.get(i).getUid());
                user2.setVisibility(View.VISIBLE);
            } else if (i == 2) {
                user3Name.setText(users.get(i).getName());
                Glide.with(this).load(users.get(i).getAvatar()).error(R.drawable.spiderman).into(user3Avatar);
                user3.setTag(users.get(i).getUid());
                user3.setVisibility(View.VISIBLE);
            } else if (i == 3) {
                user4Name.setText(users.get(i).getName());
                Glide.with(this).load(users.get(i).getAvatar()).error(R.drawable.wolverine).into(user4Avatar);
                user4.setTag(users.get(i).getUid());
                user4.setVisibility(View.VISIBLE);
            }
        }
    }


    private void login(String uid) {
        CometChat.login(uid, AppConfig.AppDetails.AUTH_KEY, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                startActivity(new Intent(MainActivity.this, SelectActivity.class));
                finish();
            }

            @Override
            public void onError(CometChatException e) {
                String str = uid+"_progressbar";
                int id = getResources().getIdentifier(str,"id",getPackageName());
                findViewById(id).setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createUser(View view) {
        startActivity(new Intent(this, CreateUserActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
