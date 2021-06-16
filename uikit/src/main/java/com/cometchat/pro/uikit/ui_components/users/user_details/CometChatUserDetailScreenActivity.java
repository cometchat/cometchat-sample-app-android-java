package com.cometchat.pro.uikit.ui_components.users.user_details;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;

import com.cometchat.pro.uikit.ui_components.messages.extensions.Collaborative.CometChatWebViewActivity;
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_components.shared.CometChatSnackBar;
import com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.shared.cometchatSharedMedia.CometChatSharedMedia;
import com.cometchat.pro.uikit.ui_resources.utils.CometChatError;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.cometchat.pro.uikit.ui_components.users.user_details.callHistroy.CallHistoryAdapter;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_resources.utils.CallUtils;
import com.cometchat.pro.uikit.ui_resources.utils.FontUtils;
import com.cometchat.pro.uikit.ui_settings.FeatureRestriction;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;

public class CometChatUserDetailScreenActivity extends AppCompatActivity {
    private CometChatAvatar userAvatar;

    private TextView userStatus, userName, addBtn;

    private String name;

    private String TAG = "CometChatUserDetailScreenActivity";

    private String avatar;

    private String uid;

    private String guid;

    private String groupName;

    private boolean isAddMember;

    private boolean isAlreadyAdded;

    private LinearLayout blockUserLayout;

    private TextView tvBlockUser;

    private MaterialToolbar toolbar;

    private boolean isBlocked;

    private FontUtils fontUtils;

    private LinearLayout historyView;

    private RecyclerView historyRv;

    private CallHistoryAdapter callHistoryAdapter;

    private MessagesRequest messageRequest;

    private CometChatSharedMedia sharedMediaView;

    private LinearLayout sharedMediaLayout;

    private TextView viewProfile;

    private String profileLink = "";

    private boolean inProgress;

    private boolean fromCallList;

    private View divider1,divider2,divider3;

    private LinearLayout preferenceLayout;

    private List<BaseMessage> callList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cometchat_user_detail);
        fontUtils= FontUtils.getInstance(this);
        initComponent();

    }

    private void initComponent() {

        historyView = findViewById(R.id.history_view);
        historyRv = findViewById(R.id.history_rv);
        userAvatar = findViewById(R.id.iv_user);
        userName = findViewById(R.id.tv_name);
        userStatus = findViewById(R.id.tv_status);
        preferenceLayout = findViewById(R.id.preference_layout);
        addBtn = findViewById(R.id.btn_add);
        toolbar= findViewById(R.id.user_detail_toolbar);
        divider1 = findViewById(R.id.divider_1);
        divider2 = findViewById(R.id.divider_2);
        divider3 = findViewById(R.id.divider_3);

        CometChatError.init(this);
        viewProfile = findViewById(R.id.tv_view_profile);

        setSupportActionBar(toolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addBtn.setTypeface(fontUtils.getTypeFace(FontUtils.robotoRegular));

        blockUserLayout = findViewById(R.id.block_user_layout);

        tvBlockUser = findViewById(R.id.tv_blockUser);

        tvBlockUser.setTypeface(fontUtils.getTypeFace(FontUtils.robotoMedium));

        userName.setTypeface(fontUtils.getTypeFace(FontUtils.robotoMedium));


        handleIntent();

        sharedMediaLayout = findViewById(R.id.shared_media_layout);
        sharedMediaView = findViewById(R.id.shared_media_view);
        sharedMediaView.setRecieverId(uid);
        sharedMediaView.setRecieverType(CometChatConstants.RECEIVER_TYPE_USER);
        sharedMediaView.reload();

        if (profileLink==null || profileLink.isEmpty()) {
            preferenceLayout.setVisibility(View.GONE);
            viewProfile.setVisibility(View.GONE);
        }

        if(isAddMember)
            preferenceLayout.setVisibility(View.VISIBLE);

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile();
            }
        });

        FeatureRestriction.isSharedMediaEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (!booleanVal)
                    sharedMediaLayout.setVisibility(View.GONE);
            }
        });

        checkDarkMode();
        addBtn.setOnClickListener(view -> {

            if (guid != null) {
                if (isAddMember) {
                    if (isAlreadyAdded)
                        kickGroupMember();
                    else
                        addMember();
                }
            }
        });

        FeatureRestriction.isBlockUserEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (!booleanVal) {
                    blockUserLayout.setVisibility(View.GONE);
                }
            }
        });

        tvBlockUser.setOnClickListener(view -> {
            if (isBlocked)
               unblockUser();
            else
                blockUser();
        });
    }

    private void checkDarkMode() {
        if (Utils.isDarkMode(this)) {
            userName.setTextColor(getResources().getColor(R.color.textColorWhite));
            divider1.setBackgroundColor(getResources().getColor(R.color.grey));
            divider2.setBackgroundColor(getResources().getColor(R.color.grey));
            divider3.setBackgroundColor(getResources().getColor(R.color.grey));
        } else {
            userName.setTextColor(getResources().getColor(R.color.primaryTextColor));
            divider1.setBackgroundColor(getResources().getColor(R.color.light_grey));
            divider2.setBackgroundColor(getResources().getColor(R.color.light_grey));
            divider3.setBackgroundColor(getResources().getColor(R.color.light_grey));
        }
    }

    private void handleIntent() {

        if (getIntent().hasExtra(UIKitConstants.IntentStrings.IS_ADD_MEMBER)) {
            isAddMember = getIntent().getBooleanExtra(UIKitConstants.IntentStrings.IS_ADD_MEMBER, false);
        }

        if (getIntent().hasExtra(UIKitConstants.IntentStrings.FROM_CALL_LIST)) {
            fromCallList = getIntent().getBooleanExtra(UIKitConstants.IntentStrings.FROM_CALL_LIST,false);
        }

        if (getIntent().hasExtra(UIKitConstants.IntentStrings.IS_BLOCKED_BY_ME)){
            isBlocked=getIntent().getBooleanExtra(UIKitConstants.IntentStrings.IS_BLOCKED_BY_ME,false);
             setBlockUnblock();
        }

        if (getIntent().hasExtra(UIKitConstants.IntentStrings.GUID)) {
            guid = getIntent().getStringExtra(UIKitConstants.IntentStrings.GUID);
        }

        if (getIntent().hasExtra(UIKitConstants.IntentStrings.UID)) {
            uid = getIntent().getStringExtra(UIKitConstants.IntentStrings.UID);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.GROUP_NAME)) {
            groupName = getIntent().getStringExtra(UIKitConstants.IntentStrings.GROUP_NAME);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.NAME)) {
            name = getIntent().getStringExtra(UIKitConstants.IntentStrings.NAME);
            userName.setText(name);
        }

        if (getIntent().hasExtra(UIKitConstants.IntentStrings.LINK)) {
            profileLink = getIntent().getStringExtra(UIKitConstants.IntentStrings.LINK);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.AVATAR)) {
            avatar = getIntent().getStringExtra(UIKitConstants.IntentStrings.AVATAR);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.STATUS)) {
            String status = getIntent().getStringExtra(UIKitConstants.IntentStrings.STATUS);

            if (status != null && status.equals(CometChatConstants.USER_STATUS_ONLINE))
                userStatus.setTextColor(getResources().getColor(R.color.colorPrimary));

            userStatus.setText(status);
        }

        if (avatar != null && !avatar.isEmpty())
            userAvatar.setAvatar(avatar);
        else {
            if (name != null && !name.isEmpty())
                userAvatar.setInitials(name);
            else {
                userAvatar.setInitials("--");
            }
        }

        if (isAddMember) {
            addBtn.setText(String.format(getResources().getString(R.string.add_user_to_group),name,groupName));
            historyView.setVisibility(View.GONE);
            addBtn.setVisibility(View.VISIBLE);
        } else {
            fetchCallHistory();
            addBtn.setVisibility(View.GONE);
        }
    }

    private void fetchCallHistory() {
        if (messageRequest==null)
        {
            messageRequest = new MessagesRequest.MessagesRequestBuilder().setUID(uid)
                    .setCategories(Arrays.asList(CometChatConstants.CATEGORY_CALL))
                    .setLimit(30).build();
        }
        messageRequest.fetchPrevious(new CometChat.CallbackListener<List<BaseMessage>>() {
            @Override
            public void onSuccess(List<BaseMessage> messageList) {
                if (messageList.size()!=0) {
                    callList.addAll(messageList);
                    setCallHistoryAdapter(messageList);
                }
                if (callList.size()!=0)
                    historyView.setVisibility(View.VISIBLE);
                else
                    historyView.setVisibility(View.GONE);
            }

            @Override
            public void onError(CometChatException e) {

            }
        });
    }

    private void setCallHistoryAdapter(List<BaseMessage> messageList) {
        if (callHistoryAdapter==null)
        {
            callHistoryAdapter = new CallHistoryAdapter(CometChatUserDetailScreenActivity.this,messageList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
            historyRv.setLayoutManager(linearLayoutManager);
            historyRv.setAdapter(callHistoryAdapter);
        }
        else
            callHistoryAdapter.updateList(messageList);
    }

    private void setBlockUnblock() {
        if (isBlocked) {
            tvBlockUser.setTextColor(getResources().getColor(R.color.online_green));
            tvBlockUser.setText(getResources().getString(R.string.unblock_user));
        } else{
            tvBlockUser.setText(getResources().getString(R.string.block_user));
            tvBlockUser.setTextColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

         if (item.getItemId()==android.R.id.home){
             onBackPressed();
         }
        return super.onOptionsItemSelected(item);
    }

    private void addMember() {
        ProgressDialog progressDialog = ProgressDialog.show(this, null,
                String.format(getResources().getString(R.string.user_added_to_group),
                        userName.getText().toString(), groupName));
        List<GroupMember> userList = new ArrayList<>();
        userList.add(new GroupMember(uid, CometChatConstants.SCOPE_PARTICIPANT));
        CometChat.addMembersToGroup(guid, userList, null, new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> stringStringHashMap) {
                Log.e(TAG, "onSuccess: " + uid + "Group" + guid);
                progressDialog.dismiss();
//                if(tvBlockUser!=null) {
//                    CometChatSnackBar.show(CometChatUserDetailScreenActivity.this,
//                            tvBlockUser, String.format(getResources().getString(R.string.user_added_to_group), userName.getText().toString(), groupName),
//                            CometChatSnackBar.SUCCESS);
//                }
                addBtn.setText(String.format(getResources().getString(R.string.remove_from_group),groupName));
                isAlreadyAdded = true;
            }

            @Override
            public void onError(CometChatException e) {
                progressDialog.dismiss();
                CometChatSnackBar.show(CometChatUserDetailScreenActivity.this,
                        historyRv,CometChatError.localized(e),CometChatSnackBar.ERROR);
            }
        });
    }

    private void kickGroupMember() {
        ProgressDialog progressDialog = ProgressDialog.show(this, null,
                String.format(getResources().getString(R.string.user_removed_from_group),
                        userName.getText().toString(),groupName));
        CometChat.kickGroupMember(uid, guid, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                progressDialog.dismiss();
                addBtn.setText(String.format(getResources().getString(R.string.add_in),groupName));
                addBtn.setVisibility(View.VISIBLE);
                isAlreadyAdded = false;
            }

            @Override
            public void onError(CometChatException e) {
                progressDialog.dismiss();
                if (tvBlockUser!=null)
                    CometChatSnackBar.show(CometChatUserDetailScreenActivity.this,
                            tvBlockUser, CometChatError.localized(e),
                            CometChatSnackBar.ERROR);
            }
        });
    }


    private void unblockUser() {
        ProgressDialog progressDialog = ProgressDialog.show(CometChatUserDetailScreenActivity.this,
                null,
                userName.getText().toString()+" "+getString(R.string.unblocked_successfully));
        ArrayList<String> uids = new ArrayList<>();
        uids.add(uid);

      CometChat.unblockUsers(uids, new CometChat.CallbackListener<HashMap<String, String>>() {
          @Override
          public void onSuccess(HashMap<String, String> stringStringHashMap) {
//              if (tvBlockUser!=null)
//                  CometChatSnackBar.show(CometChatUserDetailScreenActivity.this,
//                          tvBlockUser,
//                          userName.getText().toString()+" "+getResources().getString(R.string.unblocked_successfully),CometChatSnackBar.SUCCESS);
              progressDialog.dismiss();
              isBlocked=false;
              setBlockUnblock();
          }

          @Override
          public void onError(CometChatException e) {
              Log.d(TAG, "onError: "+e.getMessage());
              if (tvBlockUser!=null)
                  CometChatSnackBar.show(CometChatUserDetailScreenActivity.this,
                          tvBlockUser,getString(R.string.unblock_user_error),CometChatSnackBar.ERROR);
          }
      });
    }


    private void blockUser() {
        ProgressDialog progressDialog = ProgressDialog.show(CometChatUserDetailScreenActivity.this,
                null,
                String.format(getResources().getString(R.string.user_is_blocked),userName.getText().toString()));
        ArrayList<String> uids = new ArrayList<>();
        uids.add(uid);
        CometChat.blockUsers(uids, new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> stringStringHashMap) {
//                if (tvBlockUser!=null)
//                    CometChatSnackBar.show(CometChatUserDetailScreenActivity.this,
//                            tvBlockUser,
//                            String.format(getResources().getString(R.string.user_is_blocked),userName.getText().toString()),
//                            CometChatSnackBar.SUCCESS);
                isBlocked=true;
                progressDialog.dismiss();
                setBlockUnblock();
            }

            @Override
            public void onError(CometChatException e) {
                if (tvBlockUser!=null)
                    CometChatSnackBar.show(CometChatUserDetailScreenActivity.this,
                            tvBlockUser,
                            String.format(getResources().getString(R.string.block_user_error),
                                    userName.getText().toString()),
                            CometChatSnackBar.ERROR);
                Log.d(TAG, "onError: "+e.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        groupListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        CometChat.removeGroupListener(TAG);
    }

    private void groupListener()
    {
        CometChat.addGroupListener(TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberJoined(Action action, User joinedUser, Group joinedGroup) {
                updateBtn(joinedUser,R.string.remove_from_group);
            }

            @Override
            public void onGroupMemberLeft(Action action, User leftUser, Group leftGroup) {
                updateBtn(leftUser,R.string.add_in);
            }

            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group kickedFrom) {
                updateBtn(kickedUser,R.string.add_in);
            }

            @Override
            public void onMemberAddedToGroup(Action action, User addedby, User userAdded, Group addedTo) {
                updateBtn(userAdded,R.string.remove_from_group);
            }
        });
    }

    private void openProfile() {
        Intent intent = new Intent(CometChatUserDetailScreenActivity.this, CometChatWebViewActivity.class);
        intent.putExtra(UIKitConstants.IntentStrings.URL,profileLink);
        startActivity(intent);
    }

    private void updateBtn(User user, int resource_string) {
        if (user.getUid().equals(uid))
            addBtn.setText(String.format(getResources().getString(resource_string), groupName ));
    }
}
