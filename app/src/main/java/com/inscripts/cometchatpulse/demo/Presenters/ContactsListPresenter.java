package com.inscripts.cometchatpulse.demo.Presenters;

import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.ContactsContract;
import com.inscripts.cometchatpulse.demo.Utils.Logger;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.core.UsersRequest;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import java.util.List;


public class ContactsListPresenter extends Presenter<ContactsContract.ContactView>
        implements ContactsContract.ContactPresenter{

    private UsersRequest usersRequest ;


    @Override
    public void fecthUsers() {


        if (usersRequest==null) {

            usersRequest  = new UsersRequest.UsersRequestBuilder().setLimit(30).build();
            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    Logger.error("userList"," "+users.size());
                    if (isViewAttached())
                        getBaseView().setContactAdapter(users);
                }

                @Override
                public void onError(CometChatException e) {

                }
            });
        }
        else {
            usersRequest.fetchNext(new CometChat.CallbackListener<List<User>>() {
                @Override
                public void onSuccess(List<User> users) {
                    if (users != null && users.size() != 0) {
                        Logger.error("old user list resquest obj");
                        Logger.error("userList"," "+users.size());
                        if (isViewAttached())
                            getBaseView().setContactAdapter(users);
                    }
                }

                @Override
                public void onError(CometChatException e) {

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


}
