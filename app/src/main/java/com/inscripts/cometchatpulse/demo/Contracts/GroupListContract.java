package com.inscripts.cometchatpulse.demo.Contracts;

import android.app.ProgressDialog;
import android.content.Context;

import com.inscripts.cometchatpulse.demo.Adapter.GroupListAdapter;
import com.inscripts.cometchatpulse.demo.Base.BasePresenter;
import com.inscripts.cometchatpulse.demo.Base.BaseView;
import com.cometchat.pro.models.Group;

import java.util.List;

public interface GroupListContract {


    interface GroupView extends BaseView {

        void setGroupAdapter(List<Group> groupList);


        void groupjoinCallback(Group group);
    }

    interface GroupPresenter extends BasePresenter<GroupView>
    {
        void initGroupView();

        void joinGroup(Context context, Group group, ProgressDialog progressDialog, GroupListAdapter groupListAdapter);

        void refresh();
    }
}
