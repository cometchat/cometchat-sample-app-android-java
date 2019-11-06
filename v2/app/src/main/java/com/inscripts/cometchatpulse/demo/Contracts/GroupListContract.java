package com.inscripts.cometchatpulse.demo.Contracts;

import android.app.ProgressDialog;
import android.content.Context;

import com.inscripts.cometchatpulse.demo.Adapter.GroupListAdapter;
import com.inscripts.cometchatpulse.demo.Base.BasePresenter;
import com.inscripts.cometchatpulse.demo.Base.BaseView;
import com.cometchat.pro.models.Group;

import java.util.HashMap;
import java.util.List;

public interface GroupListContract {


    interface GroupView extends BaseView {

        void setGroupAdapter(HashMap<String,Group> groupList);

        void groupjoinCallback(Group group);

        void setFilterGroup(HashMap<String,Group> groups);
    }

    interface GroupPresenter extends BasePresenter<GroupView>
    {
        void initGroupView();

        void joinGroup(Context context, Group group, ProgressDialog progressDialog, GroupListAdapter groupListAdapter);

        void refresh();

        void searchGroup(String s);

        void deleteGroup(Context context,String guid,GroupListAdapter groupListAdapter);
    }
}
