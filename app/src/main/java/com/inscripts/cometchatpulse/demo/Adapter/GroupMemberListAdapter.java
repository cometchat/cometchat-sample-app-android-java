package com.inscripts.cometchatpulse.demo.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.Utils.ColorUtils;
import com.inscripts.cometchatpulse.demo.Utils.MediaUtils;

import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;

import java.util.List;

public class GroupMemberListAdapter extends RecyclerView.Adapter<GroupMemberListAdapter.GroupMemberHolder> {

    private final String ownerId;
    private List<GroupMember> groupMemberList;

    private Context context;

    public GroupMemberListAdapter(List<GroupMember> groupMemberList, Context context, String ownerId) {
        this.groupMemberList = groupMemberList;
        this.context = context;
        this.ownerId=ownerId;
    }

    @NonNull
    @Override
    public GroupMemberHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GroupMemberHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.group_member_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupMemberHolder groupMemberHolder, int i) {

        GroupMember groupMember = groupMemberList.get(i);
        User user = groupMember.getUser();
        if (user.getUid().equals(ownerId))
        {
            groupMemberHolder.userName.setText(context.getString(R.string.you));
        }else {
            groupMemberHolder.userName.setText(user.getName());
        }
        groupMemberHolder.userStatus.setText(groupMember.getScope());

        if (user.getAvatar() != null) {
            Glide.with(context).load(user.getAvatar()).into(groupMemberHolder.avatar);
        } else {
            Drawable drawable = context.getResources().getDrawable(R.drawable.default_avatar);
            try {
                groupMemberHolder.avatar.setCircleBackgroundColor(ColorUtils.getMaterialColor(context));
                groupMemberHolder.avatar.setImageBitmap(MediaUtils.getPlaceholderImage(context, drawable));
            } catch (Exception e) {

                groupMemberHolder.avatar.setCircleBackgroundColor(context.getResources()
                        .getColor(R.color.secondaryDarkColor));

                groupMemberHolder.avatar.setImageDrawable(drawable);
            }
        }
        groupMemberHolder.view.setTag(R.string.user, user);
        groupMemberHolder.view.setTag(R.string.group_member, groupMember);


    }


    public void removeMember(String uid) {
//        for (Iterator<GroupMember> iterator=groupMemberList.iterator();iterator.hasNext();)
        try {
            for (GroupMember groupMember : groupMemberList) {
                if (groupMember.getUid().equals(uid)) {
                    remove(groupMember);
                    break;

                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void remove(GroupMember groupMember) {
        groupMemberList.remove(groupMember);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (groupMemberList != null) {
            return groupMemberList.size();
        } else {
            return 0;
        }


    }

    public class GroupMemberHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView userStatus;
        CircleImageView avatar;
        View view;

        public GroupMemberHolder(@NonNull View itemView) {

            super(itemView);
            view = itemView;
            avatar = view.findViewById(R.id.imageViewUserAvatar);
            userName = (TextView) view.findViewById(R.id.textviewUserName);
            userStatus = (TextView) view.findViewById(R.id.textviewUserStatus);
        }
    }
}
