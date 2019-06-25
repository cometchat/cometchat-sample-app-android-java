package com.inscripts.cometchatpulse.demo.Presenters;

import android.util.Log;

import com.cometchat.pro.constants.CometChatConstants;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.ContactsContract;
import com.inscripts.cometchatpulse.demo.Utils.Logger;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;

import java.util.HashMap;
import java.util.List;


public class ContactsListPresenter extends Presenter<ContactsContract.ContactView>
        implements ContactsContract.ContactPresenter{

    private UsersRequest usersRequest;
    HashMap<String,User> userHashMap=new HashMap<>();

    private static final String TAG = "ContactsListPresenter";

    @Override
    public void fetchUsers() {


        if (usersRequest==null) {

            usersRequest  = new UsersRequest.UsersRequestBuilder().setLimit(30).build();

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
                        Logger.error("old user list request obj");
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
    public void addPresenceListener(String presenceListener) {
        CometChat.addUserListener(presenceListener, new CometChat.UserListener() {
            @Override
            public void onUserOnline(User user) {
                   getBaseView().updatePresence(user);
            }

            @Override
            public void onUserOffline(User user) {
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
                    Log.d(TAG, "usersRequest onSuccess: "+user.toString());
                     hashMap.put(user.getUid(),user);
                }
               getBaseView().setFilterList(hashMap);

           }

           @Override
           public void onError(CometChatException e) {
               Log.d(TAG, "onError: fetchNext "+e.getMessage());
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
