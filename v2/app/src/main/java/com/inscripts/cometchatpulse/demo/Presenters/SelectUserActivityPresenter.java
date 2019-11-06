package com.inscripts.cometchatpulse.demo.Presenters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Activity.SelectUserActivity;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.SelectUserActivityContract;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.Utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SelectUserActivityPresenter extends Presenter<SelectUserActivityContract.SelectUserActivityView> implements
        SelectUserActivityContract.SelectUserActivityPresenter {

    private UsersRequest usersRequest;
    HashMap<String, User> userHashMap=new HashMap<>();

    private static final String TAG = "SelectUserActivityPrese";

    @Override
    public void getIntent(Intent intent) {

        if (intent.hasExtra(StringContract.IntentStrings.INTENT_GROUP_ID)){
            getBaseView().setGUID(intent.getStringExtra(StringContract.IntentStrings.INTENT_GROUP_ID));
        }

        if (intent.hasExtra(StringContract.IntentStrings.INTENT_SCOPE)){
            getBaseView().setScope(intent.getStringExtra(StringContract.IntentStrings.INTENT_SCOPE));
        }
    }

    @Override
    public void getUserList(int i) {


        if (usersRequest==null) {

            usersRequest  = new UsersRequest.UsersRequestBuilder().setLimit(i).build();

            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    Logger.error(TAG," "+users.size());

                    for (int i = 0; i < users.size(); i++) {
                        userHashMap.put(users.get(i).getUid(), users.get(i));
                    }
                    getBaseView().setContactAdapter(userHashMap);

                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "fetchNext onError: ");
                }
            });
        }
        else {
            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    if (users != null) {
                        Logger.error(TAG," "+users.size());

                        for (int i = 0; i < users.size(); i++) {
                            userHashMap.put(users.get(i).getUid(), users.get(i));
                        }
                        getBaseView().setContactAdapter(userHashMap);
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d(TAG, "fetchNext old onError: ");
                }

            });
        }

    }

    @Override
    public void addMemberToGroup(String guid, SelectUserActivity selectUserActivity, Set<String> keySet) {

        List<GroupMember> userList=new ArrayList<>();
        Iterator value=keySet.iterator();
        while (value.hasNext()){
            userList.add(new GroupMember((String) value.next(), CometChatConstants.SCOPE_PARTICIPANT));
        }

        CometChat.addMembersToGroup(guid, userList, null,
                new CometChat.CallbackListener<HashMap<String, String>>() {
            @Override
            public void onSuccess(HashMap<String, String> stringStringHashMap) {
                selectUserActivity.finish();
            }

            @Override
            public void onError(CometChatException e) {
                Toast.makeText(selectUserActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}