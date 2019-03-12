package com.inscripts.cometchatpulse.demo.Presenters;

import android.content.Context;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.CreateGroupActivityContract;
import com.inscripts.cometchatpulse.demo.Utils.CommonUtils;


public class CreateGroupActivityPresenter extends Presenter<CreateGroupActivityContract.CreateGroupView>
        implements CreateGroupActivityContract.CreateGroupPresenter {


    @Override
    public void createGroup(final Context context, Group group) {

        CometChat.createGroup(group, new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group) {
                CommonUtils.startActivityIntent(group, context, true, null);
                Toast.makeText(context, group.getGroupType() + " group created ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(CometChatException e) {

            }

        });

    }
}
