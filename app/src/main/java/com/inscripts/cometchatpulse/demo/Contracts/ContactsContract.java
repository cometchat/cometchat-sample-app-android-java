package com.inscripts.cometchatpulse.demo.Contracts;

import android.content.Context;

import com.inscripts.cometchatpulse.demo.Base.BasePresenter;
import com.cometchat.pro.models.User;
import com.cometchat.pro.models.User;

import java.util.HashMap;
import java.util.List;


public interface ContactsContract {

    interface ContactView{

        void setContactAdapter(HashMap<String,User> userHashMap);

        void updatePresence(User user);

        void setLoggedInUser(User user);
    }

    interface ContactPresenter extends BasePresenter<ContactView> {

          void fetchUsers();

          void addPresenceListener(String presenceListener);

          void removePresenceListener(String string);

          void getLoggedInUser();

    }
}
