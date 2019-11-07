package com.inscripts.cometchatpulse.demo.Contracts;

import android.content.Context;

import com.inscripts.cometchatpulse.demo.Base.BasePresenter;
import com.inscripts.cometchatpulse.demo.Base.BaseView;
import com.cometchat.pro.models.Group;


public interface CreateGroupActivityContract {

    interface CreateGroupView extends BaseView {

    }

    interface CreateGroupPresenter extends BasePresenter<CreateGroupView> {

        void createGroup(Context context, Group group);

    }
}
