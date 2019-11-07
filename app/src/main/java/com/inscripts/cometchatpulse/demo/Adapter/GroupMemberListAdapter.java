package com.inscripts.cometchatpulse.demo.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.Utils.ColorUtils;
import com.inscripts.cometchatpulse.demo.Utils.MediaUtils;
import com.cometchat.pro.models.GroupMember;
import com.cometchat.pro.models.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupMemberListAdapter extends RecyclerView.Adapter<GroupMemberListAdapter.GroupMemberHolder> {

    private final String ownerId;
    private HashMap<String ,GroupMember> groupMemberList;

    private Context context;

    public GroupMemberListAdapter(HashMap<String ,GroupMember> groupMemberList, Context context, String ownerId) {
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

        GroupMember groupMember = new ArrayList<>(groupMemberList.values()).get(i);
        User user = groupMember;
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
        try {
            groupMemberList.remove(uid);
            notifyDataSetChanged();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public void addMember(String uid,GroupMember user) {
        try {
            groupMemberList.put(uid,user);
            notifyDataSetChanged();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public int getItemCount() {

        if (groupMemberList != null) {
            return groupMemberList.size();
        } else {
            return 0;
        }


    }

    public void updateMember(String uid, String scope) {
        GroupMember groupMember = groupMemberList.get(uid);
        if (groupMember != null)
            groupMember.setScope(scope);

        this.groupMemberList.put(uid, groupMember);
        notifyDataSetChanged();
    }

    public void resetAdapter() {
        groupMemberList.clear();
    }

    public void refreshList(HashMap<String ,GroupMember> groupMemberList) {
        this.groupMemberList.putAll(groupMemberList);
        notifyDataSetChanged();
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