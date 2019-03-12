package com.inscripts.cometchatpulse.demo.Contracts;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.inscripts.cometchatpulse.demo.Activity.GroupDetailActivity;
import com.inscripts.cometchatpulse.demo.Base.BasePresenter;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;

public interface GroupDetailActivityContract {

    interface GroupDetailView{

        void setGroupName(String groupName);

        void setGroupId(String groupId);

        void setOwnerDetail(User user);

        void setGroupOwnerName(String owner);

        void setGroupIcon(String icon);

        void setGroupDescription(String description);
    }

    interface GroupDetailPresenter extends BasePresenter<GroupDetailView> {

        void handleIntent(Intent data, Context context);

        void leaveGroup(String gUid);

        void clearConversation(String gUid);

        void setIcon(GroupDetailActivity groupDetailActivity, String icon, ImageView groupImage);


    }
}
