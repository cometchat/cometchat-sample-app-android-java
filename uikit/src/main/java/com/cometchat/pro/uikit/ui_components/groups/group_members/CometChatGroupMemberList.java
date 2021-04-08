package com.cometchat.pro.uikit.ui_components.groups.group_members;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.GroupMembersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.shared.CometChatSnackBar;
import com.cometchat.pro.uikit.ui_resources.utils.CometChatError;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_resources.utils.recycler_touch.ClickListener;
import com.cometchat.pro.uikit.ui_resources.utils.recycler_touch.RecyclerTouchListener;
import com.cometchat.pro.uikit.ui_resources.utils.FontUtils;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;

/**
 * Purpose - CometChatGroupMemberListScreen.class is used to make another admin to other group members.
 * It fetches the list of group member and on click on any group member it changes its scope to admin.
 *
 * Created on - 20th December 2019
 *
 * Modified on  - 16th January 2020
 *
 */

public class CometChatGroupMemberList extends Fragment {
    private static final String TAG = "CometChatGroupMember";

    private GroupMemberAdapter groupMemberListAdapter;

    private GroupMembersRequest groupMembersRequest;

    private boolean showModerators;

    private RecyclerView rvGroupMemberList;

    private EditText etSearch;

    private ImageView clearSearch;

    private String guid;

    private Context context;

    private boolean transferOwnerShip;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       if (getArguments()!=null) {
           guid = getArguments().getString(UIKitConstants.IntentStrings.GUID);
           showModerators = getArguments().getBoolean(UIKitConstants.IntentStrings.SHOW_MODERATORLIST);
           transferOwnerShip = getArguments().getBoolean(UIKitConstants.IntentStrings.TRANSFER_OWNERSHIP);
       }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cometchat_add_member, container, false);

        rvGroupMemberList = view.findViewById(R.id.rv_user_list);

        etSearch = view.findViewById(R.id.search_bar);

        clearSearch = view.findViewById(R.id.clear_search);

        MaterialToolbar toolbar = view.findViewById(R.id.add_member_toolbar);
        setToolbar(toolbar);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0)
                    clearSearch.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH)
            {
                searchUser(textView.getText().toString());
                clearSearch.setVisibility(View.VISIBLE);
                return true;
            }
            return false;
        });
        clearSearch.setOnClickListener(view1 -> {
            etSearch.setText("");
            clearSearch.setVisibility(View.GONE);
            searchUser(etSearch.getText().toString());
             if (getActivity()!=null) {
                 InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                 // Hide the soft keyboard
                 assert inputMethodManager != null;
                 inputMethodManager.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
             }
        });

        rvGroupMemberList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (!recyclerView.canScrollVertically(1)) {
                    fetchGroupMembers();
                }

            }
        });

        // On click of any group member item in rvUserList, It shows dialog with positive and negative button. On click of positive button it changes scope of group member
        rvGroupMemberList.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rvGroupMemberList, new ClickListener() {

            @Override
            public void onClick(View var1, int var2) {
                GroupMember groupMember = (GroupMember) var1.getTag(R.string.user);
                if (transferOwnerShip) {
                    MaterialAlertDialogBuilder alert_dialog = new MaterialAlertDialogBuilder(getActivity());
                    alert_dialog.setTitle(getResources().getString(R.string.make_owner));
                    alert_dialog.setMessage(String.format(getResources().getString(R.string.make_owner_question), groupMember.getName()));
                    alert_dialog.setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i) -> transferOwner(groupMember));
                    alert_dialog.setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
                    alert_dialog.create();
                    alert_dialog.show();
                }
                else {
                    if (showModerators) {
                        if (getActivity() != null) {
                            MaterialAlertDialogBuilder alert_dialog = new MaterialAlertDialogBuilder(getActivity());
                            alert_dialog.setTitle(getResources().getString(R.string.make_group_moderator));
                            alert_dialog.setMessage(String.format(getResources().getString(R.string.make_moderator_question), groupMember.getName()));
                            alert_dialog.setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i) -> updateAsModeratorScope(groupMember));
                            alert_dialog.setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
                            alert_dialog.create();
                            alert_dialog.show();
                        }
                    } else {
                        if (getActivity() != null) {
                            MaterialAlertDialogBuilder alert_dialog = new MaterialAlertDialogBuilder(getActivity());
                            alert_dialog.setTitle(getResources().getString(R.string.make_group_admin));
                            alert_dialog.setMessage(String.format(getResources().getString(R.string.make_admin_question), groupMember.getName()));
                            alert_dialog.setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i) -> updateAsAdminScope(groupMember));
                            alert_dialog.setNegativeButton(getResources().getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.dismiss());
                            alert_dialog.create();
                            alert_dialog.show();

                        }
                    }
                }
            }
        }));

        fetchGroupMembers();

        return view;
    }

    private void transferOwner(GroupMember groupMember) {
        CometChat.transferGroupOwnership(guid, groupMember.getUid(), new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                CometChatSnackBar.show(context,rvGroupMemberList,
                        String.format(getResources().getString(R.string.user_is_owner),groupMember.getName()), CometChatSnackBar.SUCCESS);
                if (getActivity()!=null)
                    getActivity().onBackPressed();
            }

            @Override
            public void onError(CometChatException e) {
                CometChatSnackBar.show(context,
                        rvGroupMemberList,
                        String.format(getResources().getString(R.string.update_scope_error)+e.getCode(),groupMember.getName())
                                +", "+CometChatError.localized(e),CometChatSnackBar.ERROR);
            }
        });
    }

    private void setToolbar(MaterialToolbar toolbar) {
        if (Utils.changeToolbarFont(toolbar) != null) {
            Utils.changeToolbarFont(toolbar).setTypeface(FontUtils.getInstance(getActivity()).getTypeFace(FontUtils.robotoMedium));
        }
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void updateAsAdminScope(GroupMember groupMember) {

        CometChat.updateGroupMemberScope(groupMember.getUid(), guid, CometChatConstants.SCOPE_ADMIN, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG, "onSuccess: "+s);
                groupMemberListAdapter.removeGroupMember(groupMember);
                CometChatSnackBar.show(context,rvGroupMemberList,
                        String.format(getResources().getString(R.string.is_now_admin),groupMember.getName()),CometChatSnackBar.SUCCESS);
            }

            @Override
            public void onError(CometChatException e) {
                Log.e(TAG, "onError: "+e.getMessage() );
                CometChatSnackBar.show(context, rvGroupMemberList,
                        String.format(getResources().getString(R.string.update_scope_error),groupMember.getName())+
                                ", "+CometChatError.localized(e),CometChatSnackBar.ERROR);
            }
        });
    }

    private void updateAsModeratorScope(GroupMember groupMember) {

        CometChat.updateGroupMemberScope(groupMember.getUid(), guid, CometChatConstants.SCOPE_MODERATOR, new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG, "onSuccess: "+s);
                groupMemberListAdapter.removeGroupMember(groupMember);
                if (rvGroupMemberList !=null)
                    CometChatSnackBar.show(context,rvGroupMemberList,
                            String.format(getResources().getString(R.string.is_now_moderator),groupMember.getName()),CometChatSnackBar.SUCCESS);
            }

            @Override
            public void onError(CometChatException e) {
                Log.e(TAG, "onError: "+e.getMessage() );
                CometChatSnackBar.show(context,rvGroupMemberList,
                        String.format(getResources().getString(R.string.update_scope_error),groupMember.getName())+
                                ","+CometChatError.localized(e),CometChatSnackBar.ERROR);
            }
        });
    }

    /**
     * This method is used to fetch list of group members.
     *
     * @see GroupMembersRequest
     */
    private void fetchGroupMembers() {
        if (groupMembersRequest == null) {
            if (showModerators)
                groupMembersRequest = new GroupMembersRequest.GroupMembersRequestBuilder(guid)
                        .setScopes(Arrays.asList(CometChatConstants.SCOPE_PARTICIPANT))
                        .setLimit(10).build();
            else
                groupMembersRequest = new GroupMembersRequest.GroupMembersRequestBuilder(guid)
                        .setScopes(Arrays.asList(CometChatConstants.SCOPE_PARTICIPANT,
                                CometChatConstants.SCOPE_MODERATOR))
                        .setLimit(10).build();
        }
        groupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> users) {
                if (users.size() > 0) {
                    setAdapter(users);
                }
            }
            @Override
            public void onError(CometChatException e) {
                Log.e(TAG, "onError: " + e.getMessage());
                CometChatSnackBar.show(context,rvGroupMemberList,
                        CometChatError.localized(e), CometChatSnackBar.ERROR);
            }
        });
    }

    /**
     * This method is used to perform search operation on list of group members.
     *
     * @param s is a String which is used to search group members.
     *
     * @see GroupMembersRequest
     */
    private void searchUser(String s)
    {
        GroupMembersRequest groupMembersRequest = new GroupMembersRequest.GroupMembersRequestBuilder(guid).setSearchKeyword(s).setLimit(10).build();
        groupMembersRequest.fetchNext(new CometChat.CallbackListener<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> groupMembers) {
                if (groupMemberListAdapter!=null)
                {
                    List<GroupMember> filterlist = new ArrayList<>();
                    for (GroupMember gmember : groupMembers) {
                        if (gmember.getScope().equals(CometChatConstants.SCOPE_PARTICIPANT))
                        {
                            filterlist.add(gmember);
                        }
                    }
                    groupMemberListAdapter.searchGroupMembers(filterlist);
                }
            }

            @Override
            public void onError(CometChatException e) {
                CometChatSnackBar.show(context,rvGroupMemberList, CometChatError.localized(e),CometChatSnackBar.ERROR);
                Log.e(TAG, "onError: "+e.getMessage() );
            }
        });
    }


    /**
     * This method is used to set Adapter for groupMemberList.
     * @param groupMembers
     */
    private void setAdapter(List<GroupMember> groupMembers) {
        if (groupMemberListAdapter==null){
            groupMemberListAdapter=new GroupMemberAdapter(getContext(),groupMembers,null);
            rvGroupMemberList.setAdapter(groupMemberListAdapter);
        }else {
            groupMemberListAdapter.updateGroupMembers(groupMembers);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }
}
