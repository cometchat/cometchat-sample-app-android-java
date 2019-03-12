package com.inscripts.cometchatpulse.demo.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.MediaMessage;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Adapter.MediaAdapter;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.Contracts.UserProfileViewActivityContract;
import com.inscripts.cometchatpulse.demo.Presenters.UserProfileViewPresenter;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Utils.DateUtils;
import com.inscripts.cometchatpulse.demo.Utils.FontUtils;
import com.inscripts.cometchatpulse.demo.Utils.MediaUtils;

import java.util.List;

public class UsersProfileViewActivity extends AppCompatActivity implements
        UserProfileViewActivityContract.UserProfileActivityView, View.OnClickListener {

    private Toolbar toolbar;

    private MediaAdapter mediaAdapter;

    private ImageView userAvatar, ivStatusIcon;

    private TextView tvStatus, tvVoiceCall, tvVideoCall;

    private Context context;

    private CollapsingToolbarLayout collapsingToolbar;

    private UserProfileViewActivityContract.UserProfileActivityPresenter userProfileActivityPresenter;

    private String contactUid;

    private RecyclerView rvMedia;

    private boolean isProfileView = false;

    private TextView tvUid;

    private String userStatus;

    private Drawable statusDrawable;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        new FontUtils(this);
        context = this;
        userProfileActivityPresenter = new UserProfileViewPresenter();
        userProfileActivityPresenter.attach(this);

        initViewComponent();

    }

    private void initViewComponent() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(10);

        TextView shared = findViewById(R.id.tvshared);
        TextView tvSeparator=findViewById(R.id.tvSeparator);
        userAvatar = findViewById(R.id.ivUserImage);
        ivStatusIcon = findViewById(R.id.imageViewProfileStatus);
        tvStatus = findViewById(R.id.textViewProfileStatusMessage);

        tvUid = findViewById(R.id.tvUid);
        rvMedia = findViewById(R.id.rv_media);

        tvVideoCall = findViewById(R.id.video_call);
        tvVoiceCall = findViewById(R.id.voice_call);

        tvVoiceCall.setOnClickListener(this);
        tvVideoCall.setOnClickListener(this);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.primaryLightColor));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.primaryTextColor));
        collapsingToolbar.setExpandedTitleGravity(Gravity.START | Gravity.BOTTOM);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.primaryLightColor));

        collapsingToolbar.setExpandedTitleTypeface(FontUtils.robotoRegular);
        collapsingToolbar.setCollapsedTitleTypeface(FontUtils.robotoMedium);

        tvVideoCall.setTypeface(FontUtils.robotoMedium);
        tvVoiceCall.setTypeface(FontUtils.robotoMedium);
        shared.setTypeface(FontUtils.robotoMedium);
        tvStatus.setTypeface(FontUtils.robotoRegular);
        tvUid.setTypeface(FontUtils.robotoRegular);

        userProfileActivityPresenter.handleIntent(getIntent());

        linearLayoutManager = new LinearLayoutManager(this);
        rvMedia.setLayoutManager(linearLayoutManager);

        if (getIntent().hasExtra(StringContract.IntentStrings.PROFILE_VIEW)) {

            if (getIntent().getBooleanExtra(StringContract.IntentStrings.PROFILE_VIEW, false)) ;
            {
                isProfileView = true;
            }

        }


        if (isProfileView) {
            tvVideoCall.setVisibility(View.GONE);
            tvVoiceCall.setVisibility(View.GONE);
            rvMedia.setVisibility(View.GONE);
            shared.setVisibility(View.GONE);
            tvSeparator.setVisibility(View.GONE);
        } else {
            userProfileActivityPresenter.getMediaMessage(contactUid, 10);
        }


        rvMedia.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {

                    userProfileActivityPresenter.getMediaMessage(contactUid,10);

                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userProfileActivityPresenter.removeUserPresenceListener(getString(R.string.presenceListener));
        userProfileActivityPresenter.detach();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setTitle(String name) {
        collapsingToolbar.setTitle(name);
    }

    @Override
    public void setStatus(User user) {

          if (user!=null&&user.getUid().equals(contactUid)) {
              if (user.getStatus().equals(CometChatConstants.USER_STATUS_OFFLINE)) {
                  statusDrawable = context.getResources().getDrawable(R.drawable.cc_status_offline);
                  userStatus = DateUtils.getLastSeenDate(user.getLastActiveAt(), this);
              } else if (user.getStatus().equals(CometChatConstants.USER_STATUS_ONLINE)) {
                  statusDrawable = context.getResources().getDrawable(R.drawable.cc_status_available);
                  userStatus = user.getStatus();
              }
          }

        tvStatus.setText(userStatus);
        ivStatusIcon.setImageDrawable(statusDrawable);
    }

    @Override
    public void setUserImage(String avatar) {

        if (avatar != null) {

            userProfileActivityPresenter.setContactAvatar(context, avatar, userAvatar);

        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.default_avatar);

            try {
                Bitmap bitmap = MediaUtils.getPlaceholderImage(this, drawable);
                userAvatar.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        userProfileActivityPresenter.addUserPresenceListener(getString(R.string.presenceListener));
    }

    @Override
    public void setUserId(String uid) {
        contactUid = uid;

        if (uid != null) {
            tvUid.setText("Uid:" + contactUid);
        }
    }

    @Override
    public void setAdapter(List<MediaMessage> messageList) {

        if (mediaAdapter == null) {
            mediaAdapter = new MediaAdapter(messageList, context);
            rvMedia.setAdapter(mediaAdapter);
        } else {
            if (messageList.size() != 0) {
                mediaAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.voice_call:
                userProfileActivityPresenter.sendCallRequest(this, contactUid, CometChatConstants.RECEIVER_TYPE_USER, CometChatConstants.CALL_TYPE_AUDIO);
                break;

            case R.id.video_call:
                userProfileActivityPresenter.sendCallRequest(this, contactUid, CometChatConstants.RECEIVER_TYPE_USER, CometChatConstants.CALL_TYPE_VIDEO);
                break;
        }
    }
}
