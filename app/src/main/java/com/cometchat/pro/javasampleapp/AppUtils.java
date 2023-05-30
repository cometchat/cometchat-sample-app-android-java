package com.cometchat.pro.javasampleapp;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;

public class AppUtils {
    private static Group group;
    private static User user;

    public static void fetchDefaultObjects() {
        CometChat.getGroup("supergroup", new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group_) {
                group = group_;
            }

            @Override
            public void onError(CometChatException e) {

            }
        });
        CometChat.getUser("superhero5", new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user_) {
                user = user_;
            }

            @Override
            public void onError(CometChatException e) {

            }
        });
    }

    public static Group getDefaultGroup() {
        return group;
    }

    public static User getDefaultUser() {
        return user;
    }

}
