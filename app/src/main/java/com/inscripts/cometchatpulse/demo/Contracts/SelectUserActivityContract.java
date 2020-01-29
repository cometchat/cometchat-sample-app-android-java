package com.inscripts.cometchatpulse.demo.Contracts;

import android.content.Intent;

import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Activity.SelectUserActivity;
import com.inscripts.cometchatpulse.demo.Base.BasePresenter;

import java.util.HashMap;
import java.util.Set;

public interface SelectUserActivityContract {


    interface SelectUserActivityView{

        void setScope(String scope);

        void setGUID(String guid);

        void setContactAdapter(HashMap<String, User> userHashMap);
    }

    interface SelectUserActivityPresenter extends BasePresenter<SelectUserActivityView> {

        void getIntent(Intent intent);

        void getUserList(int i);


        void addMemberToGroup(String guid, SelectUserActivity selectUserActivity, Set<String> keySet);
    }
}