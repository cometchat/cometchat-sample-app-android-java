package com.inscripts.cometchatpulse.demo.Presenters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.cometchat.pro.constants.CometChatConstants;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.CometApplication;
import com.inscripts.cometchatpulse.demo.Contracts.ContactsContract;
import com.inscripts.cometchatpulse.demo.Utils.Logger;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;

import java.util.HashMap;
import java.util.List;

import timber.log.Timber;


public class ContactsListPresenter extends Presenter<ContactsContract.ContactView>
        implements ContactsContract.ContactPresenter{

    private UsersRequest usersRequest;

    HashMap<String,User> userHashMap=new HashMap<>();

    private static final String TAG = "ContactsListPresenter";

    @Override
    public void fetchUsers(Context context) {


        if (usersRequest==null) {

            usersRequest  = new UsersRequest.UsersRequestBuilder().setLimit(100).build();

            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    Logger.error(TAG," "+users.size());

                        for (int i = 0; i < users.size(); i++) {
                            Timber.d("fetchNext onSuccess: %s", users.get(i).toString());
                            userHashMap.put(users.get(i).getUid(), users.get(i));
                        }
                        getBaseView().setContactAdapter(userHashMap);
                }

                @Override
                public void onError(CometChatException e) {
                    Timber.d("fetchNext onError: %s", e.getMessage());
                }
            });
        }
        else {
            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    if (users != null) {
                            for (int i = 0; i < users.size(); i++) {

                                Timber.d("fetchNext onSuccess: %s", users.toString());

                                userHashMap.put(users.get(i).getUid(), users.get(i));
                            }
                            getBaseView().setContactAdapter(userHashMap);
                    }
                }

                @Override
                public void onError(CometChatException e) {
                    Timber.d("fetchNext old onError: ");
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
        }

    }

    @Override
    public void addPresenceListener(String presenceListener) {
        CometChat.addUserListener(presenceListener, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user) {
                Timber.d("onUserOnline: %s", user.toString());
                   getBaseView().updatePresence(user);
            }

            @Override
            public void onUserOffline(User user) {
                Timber.d("onUserOffline: %s", user.toString());
                  getBaseView().updatePresence(user);
            }
        });
    }

    @Override
    public void removePresenceListener(String presenceListener) {
        CometChat.removeUserListener(presenceListener);
    }

    @Override
    public void getLoggedInUser() {
        User user=CometChat.getLoggedInUser();
        if (isViewAttached())
        getBaseView().setLoggedInUser(user);
    }

    @Override
    public void searchUser(String s) {

       UsersRequest usersRequest= new UsersRequest.UsersRequestBuilder().setSearchKeyword(s).setLimit(100).build();
       HashMap<String ,User> hashMap=new HashMap<>();
       usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
           @Override
           public void onSuccess(List<User> users) {
                for (User user:users){
                    Timber.d("usersRequest onSuccess: %s", user.toString());
                     hashMap.put(user.getUid(),user);
                }
               getBaseView().setFilterList(hashMap);
           }
           @Override
           public void onError(CometChatException e) {
               Timber.d("onError: fetchNext %s", e.getMessage());
           }
       });

    }

    public void fetchCount() {

        CometChat.getUnreadMessageCountForAllUsers(new CometChat.CallbackListener<HashMap<String, Integer>>() {
            @Override
            public void onSuccess(HashMap<String, Integer> stringIntegerHashMap) {
                getBaseView().setUnreadMap(stringIntegerHashMap);
            }

            @Override
            public void onError(CometChatException e) {
            }
        });
    }

}
