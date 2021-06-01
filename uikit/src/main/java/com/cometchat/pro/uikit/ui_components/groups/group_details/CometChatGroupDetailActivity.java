package com.cometchat.pro.uikit.ui_components.groups.group_details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.BannedGroupMembersRequest;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupMembersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;

import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI;
import com.cometchat.pro.uikit.ui_components.shared.CometChatSnackBar;
import com.cometchat.pro.uikit.ui_components.shared.cometchatAvatar.CometChatAvatar;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.shared.cometchatSharedMedia.CometChatSharedMedia;
import com.cometchat.pro.uikit.ui_resources.utils.CometChatError;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cometchat.pro.uikit.ui_components.groups.group_members.GroupMemberAdapter;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_resources.utils.recycler_touch.ClickListener;
import com.cometchat.pro.uikit.ui_resources.utils.recycler_touch.RecyclerTouchListener;
import com.cometchat.pro.uikit.ui_components.groups.add_members.CometChatAddMembersActivity;
import com.cometchat.pro.uikit.ui_components.groups.admin_moderator_list.CometChatAdminModeratorListActivity;
import com.cometchat.pro.uikit.ui_components.groups.banned_members.CometChatBanMembersActivity;
import com.cometchat.pro.uikit.ui_components.groups.group_members.CometChatGroupMemberListActivity;
import com.cometchat.pro.uikit.ui_resources.utils.CallUtils;
import com.cometchat.pro.uikit.ui_resources.utils.FontUtils;
import com.cometchat.pro.uikit.ui_settings.FeatureRestriction;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;

import static com.cometchat.pro.uikit.ui_resources.utils.Utils.UserToGroupMember;


public class CometChatGroupDetailActivity extends AppCompatActivity {

    private String TAG = "CometChatGroupDetail";

    private CometChatAvatar groupIcon;

    private String groupType;

    private String ownerId;

    private TextView tvGroupName;

    private TextView tvGroupDesc;

    private TextView tvAdminCount;

    private TextView tvModeratorCount;

    private TextView tvBanMemberCount;

    private ArrayList<String> groupMemberUids = new ArrayList<>();

    private RecyclerView rvMemberList;

    private String guid, gName,gDesc,gPassword;

    private GroupMembersRequest groupMembersRequest;

    private GroupMemberAdapter groupMemberAdapter;

    private int adminCount;

    private int moderatorCount;

    String[] s = new String[0];

    private RelativeLayout rlAddMemberView;

    private RelativeLayout rlAdminListView;

    private RelativeLayout rlModeratorView;

    private RelativeLayout rlBanMembers;

    private String loggedInUserScope;

    private GroupMember groupMember;

    private TextView tvDelete;

    private TextView tvLoadMore;

    private List<GroupMember> groupMembers = new ArrayList<>();

    private AlertDialog.Builder dialog;

    private TextView tvMemberCount;

    private int groupMemberCount=0;

    private static int LIMIT = 30;

    private User loggedInUser = CometChat.getLoggedInUser();

    private FontUtils fontUtils;

    private CometChatSharedMedia sharedMediaView;

    private LinearLayout sharedMediaLayout;

    private TextView dividerAdmin,dividerBan,dividerModerator,divider2;

    private BannedGroupMembersRequest banMemberRequest;

    private MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cometchat_group_detail);
        fontUtils= FontUtils.getInstance(this);
        initComponent();

        CometChatError.init(this);
    }

    private void initComponent() {

        dividerAdmin = findViewById(R.id.tv_seperator_admin);
        dividerModerator = findViewById(R.id.tv_seperator_moderator);
        dividerBan = findViewById(R.id.tv_seperator_ban);
        divider2 = findViewById(R.id.tv_seperator_1);
        groupIcon = findViewById(R.id.iv_group);
        tvGroupName = findViewById(R.id.tv_group_name);
        tvGroupDesc = findViewById(R.id.group_description);
        tvGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGroupDialog();
            }
        });
        tvMemberCount = findViewById(R.id.tv_members);
        tvAdminCount = findViewById(R.id.tv_admin_count);
        tvModeratorCount = findViewById(R.id.tv_moderator_count);
        tvBanMemberCount = findViewById(R.id.tv_ban_count);
        rvMemberList = findViewById(R.id.member_list);
        tvLoadMore = findViewById(R.id.tv_load_more);
        tvLoadMore.setText(String.format(getResources().getString(R.string.load_more_members),LIMIT));
        TextView tvAddMember = findViewById(R.id.tv_add_member);
        rlBanMembers = findViewById(R.id.rlBanView);
        rlBanMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBanMemberListScreen();
            }
        });
        rlAddMemberView = findViewById(R.id.rl_add_member);
        rlAddMemberView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMembers();
            }
        });
        rlAdminListView = findViewById(R.id.rlAdminView);
        rlAdminListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminListScreen(false);
            }
        });
        rlModeratorView = findViewById(R.id.rlModeratorView);
        rlModeratorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openAdminListScreen(true); }
        });
        tvDelete = findViewById(R.id.tv_delete);
        TextView tvExit = findViewById(R.id.tv_exit);
        toolbar = findViewById(R.id.groupDetailToolbar);

        tvDelete.setTypeface(fontUtils.getTypeFace(FontUtils.robotoMedium));
        tvExit.setTypeface(fontUtils.getTypeFace(FontUtils.robotoMedium));
        tvAddMember.setTypeface(fontUtils.getTypeFace(FontUtils.robotoRegular));

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMemberList.setLayoutManager(linearLayoutManager);
//        rvMemberList.setNestedScrollingEnabled(false);

        handleIntent();
        checkDarkMode();

        sharedMediaLayout = findViewById(R.id.shared_media_layout);
        sharedMediaView = findViewById(R.id.shared_media_view);
        sharedMediaView.setRecieverId(guid);
        sharedMediaView.setRecieverType(CometChatConstants.RECEIVER_TYPE_GROUP);
        sharedMediaView.reload();

        rvMemberList.addOnItemTouchListener(new RecyclerTouchListener(this, rvMemberList, new ClickListener() {
            @Override
            public void onClick(View var1, int var2) {
                GroupMember user = (GroupMember) var1.getTag(R.string.user);
                if (loggedInUserScope != null&&(loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN)||loggedInUserScope.equals(CometChatConstants.SCOPE_MODERATOR))) {
                    groupMember = user;
                    boolean isAdmin =user.getScope().equals(CometChatConstants.SCOPE_ADMIN) ;
                    boolean isSelf = loggedInUser.getUid().equals(user.getUid());
                    boolean isOwner = loggedInUser.getUid().equals(ownerId);
                    if (!isSelf) {
                        if (!isAdmin||isOwner) {
                            registerForContextMenu(rvMemberList);
                            openContextMenu(var1);
                        }
                    }
                }
            }

            @Override
            public void onLongClick(View var1, int var2) {

            }
        }));

        tvLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGroupMembers();
            }
        });

        tvExit.setOnClickListener(view -> createDialog(getResources().getString(R.string.leave_group), getResources().getString(R.string.leave_group_message),
                getResources().getString(R.string.leave_group), getResources().getString(R.string.cancel), R.drawable.ic_exit_to_app));


        tvDelete.setOnClickListener(view -> createDialog(getResources().getString(R.string.delete_group), getResources().getString(R.string.delete_group_message),
                getResources().getString(R.string.delete_group), getResources().getString(R.string.cancel), R.drawable.ic_delete));


        //
        FeatureRestriction.isJoinOrLeaveGroupEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (!booleanVal)
                    tvExit.setVisibility(View.GONE);
            }
        });

        FeatureRestriction.isGroupDeletionEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (!booleanVal)
                    tvDelete.setVisibility(View.GONE);
            }
        });

        FeatureRestriction.isSharedMediaEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (!booleanVal) {
                    sharedMediaLayout.setVisibility(View.GONE);
                }
            }
        });

        FeatureRestriction.isViewGroupMemberInGroupDetails(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (!booleanVal) {
                    rvMemberList.setVisibility(View.GONE);
                    tvLoadMore.setVisibility(View.GONE);
                    rlAddMemberView.setVisibility(View.GONE);
                    tvMemberCount.setVisibility(View.GONE);
                    divider2.setVisibility(View.GONE);
                }
            }
        });
        FeatureRestriction.isChangingMemberScopeEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (!booleanVal) {
                    rlModeratorView.setVisibility(View.GONE);
                    rlAdminListView.setVisibility(View.GONE);
                    dividerAdmin.setVisibility(View.GONE);
                    dividerModerator.setVisibility(View.GONE);
                }
            }
        });

        FeatureRestriction.isBanningGroupMembersEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (!booleanVal)
                    rlBanMembers.setVisibility(View.GONE);
            }
        });
    }

    private void checkDarkMode() {
        if(Utils.isDarkMode(this)) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.textColorWhite));
            tvGroupName.setTextColor(getResources().getColor(R.color.textColorWhite));
            dividerAdmin.setBackgroundColor(getResources().getColor(R.color.grey));
            dividerModerator.setBackgroundColor(getResources().getColor(R.color.grey));
            dividerBan.setBackgroundColor(getResources().getColor(R.color.grey));
            divider2.setBackgroundColor(getResources().getColor(R.color.grey));
        } else {
            toolbar.setTitleTextColor(getResources().getColor(R.color.primaryTextColor));
            tvGroupName.setTextColor(getResources().getColor(R.color.primaryTextColor));
            dividerAdmin.setBackgroundColor(getResources().getColor(R.color.light_grey));
            dividerModerator.setBackgroundColor(getResources().getColor(R.color.light_grey));
            dividerBan.setBackgroundColor(getResources().getColor(R.color.light_grey));
            divider2.setBackgroundColor(getResources().getColor(R.color.light_grey));
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.group_action_menu, menu);

        menu.findItem(R.id.item_make_admin).setVisible(false);

        menu.setHeaderTitle(getString(R.string.group_alert));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == R.id.item_remove) {
            kickMember();
        } else if(item.getItemId() == R.id.item_ban) {
            banMember();
        }

        return super.onContextItemSelected(item);
    }


    /**
     * This method is used to create dialog box on click of events like <b>Delete Group</b> and <b>Exit Group</b>
     * @param title
     * @param message
     * @param positiveText
     * @param negativeText
     * @param drawableRes
     */
    private void createDialog(String title, String message, String positiveText, String negativeText, int drawableRes) {

        MaterialAlertDialogBuilder alert_dialog = new MaterialAlertDialogBuilder(CometChatGroupDetailActivity.this,
                R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered);
        alert_dialog.setTitle(title);
        alert_dialog.setMessage(message);
        alert_dialog.setPositiveButton(positiveText, (dialogInterface, i) -> {

            if (positiveText.equalsIgnoreCase(getResources().getString(R.string.leave_group))) {
                if (loggedInUser.getUid().equals(ownerId))
                    showTansferOwnerShipDialog();
                else
                    leaveGroup();

            }
            else if (positiveText.equalsIgnoreCase(getResources().getString(R.string.delete_group))
                    && loggedInUserScope.equalsIgnoreCase(CometChatConstants.SCOPE_ADMIN))
                deleteGroup();

        });

        alert_dialog.setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert_dialog.create();
        alert_dialog.show();

    }

    private void showTansferOwnerShipDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.group_alert));
        dialog.setMessage(getString(R.string.transfer_ownership_message));
        dialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(CometChatGroupDetailActivity.this, CometChatGroupMemberListActivity.class);
                intent.putExtra(UIKitConstants.IntentStrings.GUID,guid);
                intent.putExtra(UIKitConstants.IntentStrings.SHOW_MODERATORLIST,false);
                intent.putExtra(UIKitConstants.IntentStrings.TRANSFER_OWNERSHIP,true);
                finish();
                startActivity(intent);
            }
        });
        dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }

    /**
     * This method is used to handle the intent passed to this activity.
     */
    private void handleIntent() {
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.GUID)) {
            guid = getIntent().getStringExtra(UIKitConstants.IntentStrings.GUID);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.MEMBER_SCOPE)) {
            loggedInUserScope = getIntent().getStringExtra(UIKitConstants.IntentStrings.MEMBER_SCOPE);
            if (loggedInUserScope != null && loggedInUserScope.equals(CometChatConstants.SCOPE_ADMIN)) {
                rlAddMemberView.setVisibility(View.VISIBLE);
                rlBanMembers.setVisibility(View.VISIBLE);
                rlModeratorView.setVisibility(View.VISIBLE);
                tvDelete.setVisibility(View.VISIBLE);
            } else if(loggedInUserScope!=null && loggedInUserScope.equals(CometChatConstants.SCOPE_MODERATOR)) {
                rlAddMemberView.setVisibility(View.GONE);
                rlBanMembers.setVisibility(View.VISIBLE);
                rlModeratorView.setVisibility(View.VISIBLE);
                rlAdminListView.setVisibility(View.VISIBLE);
            } else {
                dividerModerator.setVisibility(View.GONE);
                dividerAdmin.setVisibility(View.GONE);
                rlAdminListView.setVisibility(View.GONE);
                rlModeratorView.setVisibility(View.GONE);
                rlBanMembers.setVisibility(View.GONE);
                rlAddMemberView.setVisibility(View.GONE);
            }

        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.NAME)) {
            gName = getIntent().getStringExtra(UIKitConstants.IntentStrings.NAME);
            tvGroupName.setText(gName);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.AVATAR)) {
            String avatar = getIntent().getStringExtra(UIKitConstants.IntentStrings.AVATAR);
            if (avatar != null && !avatar.isEmpty())
                groupIcon.setAvatar(avatar);
            else
                groupIcon.setInitials(gName);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.GROUP_DESC)) {
            gDesc = getIntent().getStringExtra(UIKitConstants.IntentStrings.GROUP_DESC);
            tvGroupDesc.setText(gDesc);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.GROUP_PASSWORD)) {
            gPassword = getIntent().getStringExtra(UIKitConstants.IntentStrings.GROUP_PASSWORD);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.GROUP_OWNER)) {
            ownerId = getIntent().getStringExtra(UIKitConstants.IntentStrings.GROUP_OWNER);
        }
        if(getIntent().hasExtra(UIKitConstants.IntentStrings.MEMBER_COUNT)) {
            tvMemberCount.setVisibility(View.VISIBLE);
            groupMemberCount = getIntent().getIntExtra(UIKitConstants.IntentStrings.MEMBER_COUNT,0);
            tvMemberCount.setText((groupMemberCount)+" "+getString(R.string.members));
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.GROUP_TYPE)) {
            groupType = getIntent().getStringExtra(UIKitConstants.IntentStrings.GROUP_TYPE);
        }
    }



    /**
     * This method is used whenever user click <b>Banned Members</b>. It takes user to
     * <code>CometChatBanMemberScreenActivity.class</code>
     *
     * @see CometChatBanMembersActivity
     */
    private void openBanMemberListScreen() {
        Intent intent = new Intent(this, CometChatBanMembersActivity.class);
        intent.putExtra(UIKitConstants.IntentStrings.GUID,guid);
        intent.putExtra(UIKitConstants.IntentStrings.GROUP_NAME,gName);
        intent.putExtra(UIKitConstants.IntentStrings.MEMBER_SCOPE,loggedInUserScope);
        startActivity(intent);
    }


    /**
     * This method is used whenever user click <b>Administrator</b>. It takes user to
     * <code>CometChatAdminListScreenActivity.class</code>
     *
     * @see CometChatAdminModeratorListActivity
     */
    public void openAdminListScreen(boolean showModerators) {
        Intent intent = new Intent(this, CometChatAdminModeratorListActivity.class);
        intent.putExtra(UIKitConstants.IntentStrings.GUID, guid);
        intent.putExtra(UIKitConstants.IntentStrings.SHOW_MODERATORLIST,showModerators);
        intent.putExtra(UIKitConstants.IntentStrings.GROUP_OWNER, ownerId);
        intent.putExtra(UIKitConstants.IntentStrings.MEMBER_SCOPE, loggedInUserScope);
        startActivity(intent);
    }

    /**
     * This method is used whenever user click <b>Add Member</b>. It takes user to
     * <code>CometChatAddMemberScreenActivity.class</code>
     *
     * @see CometChatAddMembersActivity
     */
    public void addMembers() {
        FeatureRestriction.isAddingGroupMembersEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (booleanVal) {
                    Intent intent = new Intent(CometChatGroupDetailActivity.this, CometChatAddMembersActivity.class);
                    intent.putExtra(UIKitConstants.IntentStrings.GUID, guid);
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_MEMBER, groupMemberUids);
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_NAME, gName);
                    intent.putExtra(UIKitConstants.IntentStrings.MEMBER_SCOPE, loggedInUserScope);
                    intent.putExtra(UIKitConstants.IntentStrings.IS_ADD_MEMBER, true);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * This method is used to delete Group. It is used only if loggedIn user is admin.
     */
    private void deleteGroup() {
        FeatureRestriction.isGroupDeletionEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
             if (booleanVal) {
                    CometChat.deleteGroup(guid, new CometChat.CallbackListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            launchUnified();
                        }

                        @Override
                        public void onError(CometChatException e) {
                            CometChatSnackBar.show(CometChatGroupDetailActivity.this,
                                    rvMemberList, CometChatError.localized(e),CometChatSnackBar.ERROR);
                            Log.e(TAG, "onError: " + e.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void launchUnified() {
        Intent intent = new Intent(CometChatGroupDetailActivity.this, CometChatUI.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    /**
     * This method is used to kick group member from the group. It is used only if loggedIn user is admin.
     *
     * @see CometChat#kickGroupMember(String, String, CometChat.CallbackListener)
     */
    private void kickMember() {
        ProgressDialog progressDialog = ProgressDialog.show(CometChatGroupDetailActivity.this,
                null,
                String.format(getString(R.string.user_removed_from_group),groupMember.getName(),gName));
        CometChat.kickGroupMember(groupMember.getUid(), guid, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                progressDialog.dismiss();
                Log.e(TAG, "onSuccess: " + s);
                tvMemberCount.setText((groupMemberCount-1)+" "+getString(R.string.members));
                groupMemberUids.remove(groupMember.getUid());
                groupMemberAdapter.removeGroupMember(groupMember);
            }

            @Override
            public void onError(CometChatException e) {
                CometChatSnackBar.show(CometChatGroupDetailActivity.this,
                        rvMemberList, String.format(getResources().getString(R.string.cannot_remove_member),groupMember.getName())
                                +","+CometChatError.localized(e), CometChatSnackBar.ERROR);
                Log.e(TAG, "onError: " + e.getMessage());
            }
        });
    }

    /**
     * This method is used to ban group member from the group. It is used only if loggedIn user is admin.
     *
     * @see CometChat#banGroupMember(String, String, CometChat.CallbackListener)
     */
    private void banMember() {
        ProgressDialog progressDialog = ProgressDialog.show(CometChatGroupDetailActivity.this,
                null,
                groupMember.getName()+" "+getString(R.string.banned_successfully));
        CometChat.banGroupMember(groupMember.getUid(), guid, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                progressDialog.dismiss();
                Log.e(TAG, "onSuccess: " + s);
                tvMemberCount.setText((groupMemberCount-1)+" "+getString(R.string.members));
//                int count = Integer.parseInt(tvBanMemberCount.getText().toString());
//                tvBanMemberCount.setText(String.valueOf(++count));
                groupMemberUids.remove(groupMember.getUid());
                groupMemberAdapter.removeGroupMember(groupMember);
            }

            @Override
            public void onError(CometChatException e) {
                CometChatSnackBar.show(CometChatGroupDetailActivity.this,
                        rvMemberList, String.format(getResources().getString(R.string.cannot_remove_member),groupMember.getName())
                                +","+CometChatError.localized(e),CometChatSnackBar.ERROR);
                Log.e(TAG, "onError: " + e.getMessage());
            }
        });
    }

    /**
     * This method is used to get list of group members. It also helps to update other things like
     * Admin count.
     *
     * @see GroupMembersRequest#fetchNext(CometChat.CallbackListener)
     * @see GroupMember
     */
    private void getGroupMembers() {
        List<String> groupMemeberScopes = new ArrayList<>();
        groupMemeberScopes.add(CometChatConstants.SCOPE_ADMIN);
        groupMemeberScopes.add(CometChatConstants.SCOPE_PARTICIPANT);
        if (groupMembersRequest == null) {
            groupMembersRequest = new GroupMembersRequest.GroupMembersRequestBuilder(guid)
                    .setScopes(Arrays.asList(CometChatConstants.SCOPE_ADMIN, CometChatConstants.SCOPE_PARTICIPANT, CometChatConstants.SCOPE_MODERATOR))
                    .setLimit(LIMIT).build();
        }
        groupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> groupMembers) {
                Log.e(TAG, "onSuccess: " + groupMembers.size());
                if (groupMembers != null && groupMembers.size() != 0) {
                    adminCount = 0;
                    moderatorCount = 0;
                    groupMemberUids.clear();
                    s = new String[groupMembers.size()];
                    for (int j = 0; j < groupMembers.size(); j++) {
                        groupMemberUids.add(groupMembers.get(j).getUid());
                        if (groupMembers.get(j).getScope().equals(CometChatConstants.SCOPE_ADMIN)) {
                            adminCount++;
                        }
                        if (groupMembers.get(j).getScope().equals(CometChatConstants.SCOPE_MODERATOR)) {
                            moderatorCount++;
                        }
                        s[j] = groupMembers.get(j).getName();
                    }
//                    tvAdminCount.setText(adminCount+"");
//                    tvModeratorCount.setText(moderatorCount+"");
                    if (groupMemberAdapter == null) {
                        groupMemberAdapter = new GroupMemberAdapter(CometChatGroupDetailActivity.this, groupMembers, ownerId);
                        rvMemberList.setAdapter(groupMemberAdapter);
                    } else {
                        groupMemberAdapter.addAll(groupMembers);
                    }
                    if (groupMembers.size()<LIMIT)
                    {
                        tvLoadMore.setVisibility(View.GONE);
                    }
                }
                else
                {
                    tvLoadMore.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(CometChatException e) {
                CometChatSnackBar.show(CometChatGroupDetailActivity.this,
                        rvMemberList, CometChatError.localized(e),CometChatSnackBar.ERROR);
                Log.e(TAG, "onError: " + e.getMessage());
            }
        });
    }

    private void getUserStatus() {
        CometChat.addUserListener(TAG, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user) {
                groupMemberAdapter.updateMemberByStatus(user);
            }

            @Override
            public void onUserOffline(User user) {
                groupMemberAdapter.updateMemberByStatus(user);
            }
        });
    }

//    private void getBannedMemberCount() {
//        banMemberRequest = new BannedGroupMembersRequest.BannedGroupMembersRequestBuilder(guid).setLimit(100).build();
//        banMemberRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
//            @Override
//            public void onSuccess(List<GroupMember> groupMembers) {
//                if (groupMembers.size()>=99) {
//                    tvBanMemberCount.setText("99+");
//                } else {
//                    tvBanMemberCount.setText(groupMembers.size()+"");
//                }
//            }
//
//            @Override
//            public void onError(CometChatException e) {
//                Log.e(TAG, "onError: "+e.getMessage()+"\n"+e.getCode());
//            }
//        });
//    }

    /**
     * This method is used to leave the loggedIn User from respective group.
     *
     * @see CometChat#leaveGroup(String, CometChat.CallbackListener)
     */
    private void leaveGroup() {
        FeatureRestriction.isJoinOrLeaveGroupEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (booleanVal) {
                    CometChat.leaveGroup(guid, new CometChat.CallbackListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            launchUnified();
                        }

                        @Override
                        public void onError(CometChatException e) {
                            CometChatSnackBar.show(CometChatGroupDetailActivity.this,
                                    rlAddMemberView, CometChatError.localized(e), CometChatSnackBar.ERROR);
                            Log.e(TAG, "onError: " + e.getMessage());
                        }
                    });
                }
            }
        });
    }

    /**
     * This method is used to add group listener in this screen to receive real-time events.
     *
     * @see CometChat#addGroupListener(String, CometChat.GroupListener)
     */
    public void addGroupListener() {
        CometChat.addGroupListener(TAG, new CometChat.GroupListener() {
            @Override
            public void onGroupMemberJoined(Action action, User joinedUser, Group joinedGroup) {
                Log.e(TAG, "onGroupMemberJoined: " + joinedUser.getUid());
                if (joinedGroup.getGuid().equals(guid))
                    updateGroupMember(joinedUser, false, false, action);
            }

            @Override
            public void onGroupMemberLeft(Action action, User leftUser, Group leftGroup) {
                Log.d(TAG, "onGroupMemberLeft: ");
                if (leftGroup.getGuid().equals(guid))
                    updateGroupMember(leftUser, true, false, action);
            }

            @Override
            public void onGroupMemberKicked(Action action, User kickedUser, User kickedBy, Group kickedFrom) {
                Log.d(TAG, "onGroupMemberKicked: ");
                if (kickedFrom.getGuid().equals(guid))
                    updateGroupMember(kickedUser, true, false, action);
            }

            @Override
            public void onGroupMemberScopeChanged(Action action, User updatedBy, User updatedUser, String scopeChangedTo, String scopeChangedFrom, Group group) {
                Log.d(TAG, "onGroupMemberScopeChanged: ");
                if (group.getGuid().equals(guid))
                    updateGroupMember(updatedUser,false,true,action);
            }

            @Override
            public void onMemberAddedToGroup(Action action, User addedby, User userAdded, Group addedTo) {
                if (addedTo.getGuid().equals(guid))
                    updateGroupMember(userAdded, false, false, action);
            }

            @Override
            public void onGroupMemberBanned(Action action, User bannedUser, User bannedBy, Group bannedFrom) {
                if (bannedFrom.getGuid().equals(guid)) {
//                    int count = Integer.parseInt(tvBanMemberCount.getText().toString());
//                    tvBanMemberCount.setText(String.valueOf(++count));
                    updateGroupMember(bannedUser, true, false, action);
                }
            }

            @Override
            public void onGroupMemberUnbanned(Action action, User unbannedUser, User unbannedBy, Group unbannedFrom) {
                if (unbannedFrom.getGuid().equals(guid)) {
//                    int count = Integer.parseInt(tvBanMemberCount.getText().toString());
//                    tvBanMemberCount.setText(String.valueOf(--count));
                }
            }
        });
    }

    /**
     * This method is used to update group members from events recieved in real time. It updates or removes
     * group member from list based on parameters passed.
     *
     * @param user is a object of User.
     * @param isRemoved is a boolean which helps to know whether group member needs to be removed from list or not.
     * @param isScopeUpdate is a boolean which helps to know whether group member scope is updated or not.
     * @param action is object of Action.
     *
     * @see Action
     * @see GroupMember
     * @see User
     * @see com.cometchat.pro.uikit.ui_resources.utils.Utils#UserToGroupMember(User, boolean, String)
     */
    private void updateGroupMember(User user, boolean isRemoved, boolean isScopeUpdate, Action action) {
        if (groupMemberAdapter != null) {
            if (!isRemoved && !isScopeUpdate) {
                groupMemberAdapter.addGroupMember(UserToGroupMember(user, false, action.getOldScope()));
                int count = ++groupMemberCount;
                tvMemberCount.setText(count+" "+getString(R.string.members));
            } else if (isRemoved && !isScopeUpdate) {
                groupMemberAdapter.removeGroupMember(UserToGroupMember(user, false, action.getOldScope()));
                int count = --groupMemberCount;
                tvMemberCount.setText(count+" "+getString(R.string.members));
                if(action.getNewScope()!=null) {
                    if (action.getNewScope().equals(CometChatConstants.SCOPE_ADMIN)) {
                        adminCount = adminCount - 1;
//                        tvAdminCount.setText(String.valueOf(adminCount));
                    } else if (action.getNewScope().equals(CometChatConstants.SCOPE_MODERATOR)) {
                        moderatorCount = moderatorCount - 1;
//                        tvModeratorCount.setText(String.valueOf(moderatorCount));
                    }
                }
            } else if (!isRemoved) {
                groupMemberAdapter.updateMember(UserToGroupMember(user, true, action.getNewScope()));
                if (action.getNewScope().equals(CometChatConstants.SCOPE_ADMIN)) {
                    adminCount = adminCount + 1;
//                    tvAdminCount.setText(String.valueOf(adminCount));
                    if (user.getUid().equals(loggedInUser.getUid())) {
                        rlAddMemberView.setVisibility(View.VISIBLE);
                        loggedInUserScope = CometChatConstants.SCOPE_ADMIN;
                        tvDelete.setVisibility(View.VISIBLE);
                    }
                } else if (action.getNewScope().equals(CometChatConstants.SCOPE_MODERATOR)) {
                    moderatorCount = moderatorCount + 1;
//                    tvModeratorCount.setText(String.valueOf(moderatorCount));
                    if (user.getUid().equals(loggedInUser.getUid())) {
                        rlBanMembers.setVisibility(View.VISIBLE);
                        loggedInUserScope = CometChatConstants.SCOPE_MODERATOR;
                    }
                } else if (action.getOldScope().equals(CometChatConstants.SCOPE_ADMIN)) {
                    adminCount = adminCount - 1;
//                    tvAdminCount.setText(String.valueOf(adminCount));
                } else if (action.getOldScope().equals(CometChatConstants.SCOPE_MODERATOR)) {
                    moderatorCount = moderatorCount -1;
//                    tvModeratorCount.setText(String.valueOf(moderatorCount));
                }
            }
        }
    }


    private void updateGroupDialog() {
        dialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.cometchat_update_group_dialog,null);
        CometChatAvatar avatar = view.findViewById(R.id.group_icon);
        TextInputEditText avatar_url = view.findViewById(R.id.icon_url_edt);
        if (groupIcon.getAvatarUrl()!=null) {
            avatar.setVisibility(View.VISIBLE);
            avatar.setAvatar(groupIcon.getAvatarUrl());
            avatar_url.setText(groupIcon.getAvatarUrl());
        } else {
            avatar.setVisibility(View.GONE);
        }
        TextInputEditText groupName = view.findViewById(R.id.groupname_edt);
        TextInputEditText groupDesc = view.findViewById(R.id.groupdesc_edt);
        TextInputEditText groupOldPwd = view.findViewById(R.id.group_old_pwd);
        TextInputEditText groupNewPwd = view.findViewById(R.id.group_new_pwd);
        TextInputLayout groupOldPwdLayout = view.findViewById(R.id.input_group_old_pwd);
        TextInputLayout groupNewPwdLayout = view.findViewById(R.id.input_group_new_pwd);
        Spinner groupTypeSp = view.findViewById(R.id.groupTypes);
        MaterialButton updateGroupBtn = view.findViewById(R.id.updateGroupBtn);
        MaterialButton cancelBtn = view.findViewById(R.id.cancelBtn);
        groupName.setText(gName);
        groupDesc.setText(gDesc);
        if (groupType!=null && groupType.equals(CometChatConstants.GROUP_TYPE_PUBLIC)) {
            groupTypeSp.setSelection(0);
            groupOldPwdLayout.setVisibility(View.GONE);
            groupNewPwdLayout.setVisibility(View.GONE);
        } else if(groupType!=null && groupType.equals(CometChatConstants.GROUP_TYPE_PRIVATE)){
            groupTypeSp.setSelection(1);
            groupOldPwdLayout.setVisibility(View.GONE);
            groupNewPwdLayout.setVisibility(View.GONE);
        } else {
            groupTypeSp.setSelection(2);
            groupOldPwdLayout.setVisibility(View.VISIBLE);
            groupNewPwdLayout.setVisibility(View.VISIBLE);
        }

        groupTypeSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getSelectedItemPosition()==2) {
                    if (gPassword==null) {
                        groupOldPwdLayout.setVisibility(View.GONE);
                    } else
                        groupOldPwdLayout.setVisibility(View.VISIBLE);
                    groupNewPwdLayout.setVisibility(View.VISIBLE);
                } else
                {
                    groupOldPwdLayout.setVisibility(View.GONE);
                    groupNewPwdLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        avatar_url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty())
                {
                    avatar.setVisibility(View.VISIBLE);
                    Glide.with(CometChatGroupDetailActivity.this).load(s.toString()).into(avatar);
                } else
                    avatar.setVisibility(View.GONE);
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.setView(view);
        updateGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group group = new Group();
                group.setDescription(groupDesc.getText().toString().trim());
                if(groupName.getText().toString().isEmpty()) {
                    groupName.setError(getString(R.string.fill_this_field));
                } else if (groupTypeSp.getSelectedItemPosition()==2) {
                    if (gPassword != null && groupOldPwd.getText().toString().trim().isEmpty()) {
                        groupOldPwd.setError(getResources().getString(R.string.fill_this_field));
                    } else if (gPassword != null && !groupOldPwd.getText().toString().trim().equals(gPassword.trim())) {
                        groupOldPwd.setError(getResources().getString(R.string.password_not_matched));
                    } else if (groupNewPwd.getText().toString().trim().isEmpty()) {
                        groupNewPwd.setError(getResources().getString(R.string.fill_this_field));
                    } else {
                        group.setName(groupName.getText().toString());
                        group.setGuid(guid);
                        group.setGroupType(CometChatConstants.GROUP_TYPE_PASSWORD);
                        group.setPassword(groupNewPwd.getText().toString());
                        group.setIcon(avatar_url.getText().toString());
                        updateGroup(group, alertDialog);
                    }
                }
                else if (groupTypeSp.getSelectedItemPosition()==1){
                    group.setName(groupName.getText().toString());
                    group.setGuid(guid);
                    group.setGroupType(CometChatConstants.GROUP_TYPE_PRIVATE);
                    group.setIcon(avatar_url.getText().toString());
                } else {
                    group.setName(groupName.getText().toString());
                    group.setGroupType(CometChatConstants.GROUP_TYPE_PUBLIC);
                    group.setIcon(avatar_url.getText().toString());
                }
                group.setGuid(guid);
                updateGroup(group,alertDialog);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void updateGroup(Group group,AlertDialog dialog) {
        CometChat.updateGroup(group, new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group) {
                if (rvMemberList!=null) {
                    CometChatSnackBar.show(CometChatGroupDetailActivity.this,
                            rvMemberList,getResources().getString(R.string.group_updated),CometChatSnackBar.SUCCESS);
                    getGroup();
                }
                dialog.dismiss();
            }

            @Override
            public void onError(CometChatException e) {
                if (rvMemberList!=null) {
                    CometChatSnackBar.show(CometChatGroupDetailActivity.this,
                            rvMemberList,CometChatError.localized(e),
                            CometChatSnackBar.ERROR);
                }
                dialog.dismiss();
            }
        });
    }

    /**
     * This method is used to remove group listener.
     */
    public void removeGroupListener() {
        CometChat.removeGroupListener(TAG);
    }

    public void removeUserListener() {
        CometChat.removeUserListener(TAG);
    }

    /**
     * This method is used to get Group Details.
     *
     * @see CometChat#getGroup(String, CometChat.CallbackListener)
     */
    private void getGroup() {

        CometChat.getGroup(guid, new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group) {
                gName = group.getName();
                tvGroupName.setText(gName);
                groupIcon.setAvatar(group.getIcon());
                loggedInUserScope = group.getScope();
                groupMemberCount = group.getMembersCount();
                groupType = group.getGroupType();
                gDesc = group.getDescription();
                tvGroupDesc.setText(gDesc);
                tvMemberCount.setText(groupMemberCount+" "+getString(R.string.members));
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(CometChatGroupDetailActivity.this,CometChatError.localized(e),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroup();
        groupMembersRequest = null;
        if (groupMemberAdapter != null) {
            groupMemberAdapter.resetAdapter();
            groupMemberAdapter = null;

        }
//        getBannedMemberCount();
        FeatureRestriction.isUserPresenceEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (booleanVal)
                    getUserStatus();
            }
        });

        FeatureRestriction.isViewGroupMemberInGroupDetails(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                if (booleanVal)
                    getGroupMembers();
            }
        });
        addGroupListener();
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        removeGroupListener();
        removeUserListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeGroupListener();
        removeUserListener();
    }
}
