package com.inscripts.cometchatpulse.demo.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.CustomView.StickyHeaderAdapter;
import com.inscripts.cometchatpulse.demo.Utils.ColorUtils;
import com.inscripts.cometchatpulse.demo.Utils.FontUtils;
import com.inscripts.cometchatpulse.demo.Utils.MediaUtils;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupHolder> implements
        StickyHeaderAdapter<GroupListAdapter.GroupHeaderItemHolder> {


    private List<Group> joined;

    private Context context;

    private List<Group> groupList = new ArrayList<>();

    private List<Group> other = new ArrayList<>();

    public GroupListAdapter(List<Group> groupList, Context context) {
        this.joined = groupList;
        this.context = context;

        for (int i = 0; i < groupList.size(); i++) {
            if (groupList.get(i).isJoined()) {
                this.groupList.add(groupList.get(i));
            } else {

                other.add(groupList.get(i));
            }
        }

        this.groupList.addAll(other);

    }

    @NonNull
    @Override
    public GroupHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.group_list_item, viewGroup, false);
        return new GroupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupHolder groupHolder, int i) {

        Group group = groupList.get(i);


//      groupHolder.usersOnline.setText(String.valueOf(group.));

        groupHolder.itemView.setTag(R.string.group_id, group);
        groupHolder.itemView.setTag(R.string.group_name, group.getName());
        groupHolder.itemView.setTag(R.string.group_owner, group.getOwner());
        groupHolder.itemView.setTag(R.string.groupHolder, groupHolder);
//      groupHolder.itemView.setTag(R.string.group_moderator,group.get);

        groupHolder.groupNameField.setText(group.getName());

        groupHolder.groupNameField.setTypeface(FontUtils.robotoRegular);
        groupHolder.usersOnline.setTypeface(FontUtils.robotoRegular);

        Drawable drawable = context.getResources().getDrawable(R.drawable.cc_ic_group);
        if (group.getIcon() != null && !group.getIcon().isEmpty()) {

           Glide.with(context).load(group.getIcon()).into(groupHolder.imageViewGroupAvatar);
        } else {
            try {
                groupHolder.imageViewGroupAvatar.setCircleBackgroundColor(ColorUtils.getMaterialColor(context));
                groupHolder.imageViewGroupAvatar.setImageBitmap(MediaUtils.getPlaceholderImage(context, drawable));
            } catch (Exception e) {
                groupHolder.imageViewGroupAvatar.setCircleBackgroundColor(context.getResources().
                        getColor(R.color.secondaryDarkColor));

                groupHolder.imageViewGroupAvatar.setImageDrawable(drawable);
            }

        }

        if (group.getGroupType().equals(CometChatConstants.GROUP_TYPE_PASSWORD)) {
            groupHolder.protectedStatus.setVisibility(View.VISIBLE);
        } else {
            groupHolder.protectedStatus.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (groupList != null) {
            return groupList.size();
        } else {
            return 0;
        }
    }


    @Override
    public long getHeaderId(int var1) {

        if (groupList.get(var1).isJoined()) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public GroupListAdapter.GroupHeaderItemHolder onCreateHeaderViewHolder(ViewGroup var1) {
        final View view = LayoutInflater.from(var1.getContext()).inflate(R.layout.group_list_header, var1, false);
        return new GroupHeaderItemHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(GroupListAdapter.GroupHeaderItemHolder var1, int var2, long var3) {

        if (var3 == 1) {
            var1.txtGroupStatus.setText(context.getResources().getText(R.string.joined_group));
        } else if (var3 == 0) {
            var1.txtGroupStatus.setText(context.getResources().getText(R.string.other_group));
        }


    }

    public void refreshData(List<Group> groupList) {
        this.groupList.addAll(groupList);
        notifyDataSetChanged();
    }


    public class GroupHolder extends RecyclerView.ViewHolder {

        public TextView groupNameField, unreadCount, usersOnline, usersOnlineMessage;
        public CircleImageView imageViewGroupAvatar;
        public ImageView protectedStatus;
        public RelativeLayout container;

        GroupHolder(@NonNull View itemView) {
            super(itemView);
            groupNameField = itemView.findViewById(R.id.textViewGroupName);
            unreadCount = itemView.findViewById(R.id.textViewGroupUnreadCount);
            usersOnline = itemView.findViewById(R.id.textViewGroupUsersOnline);
            imageViewGroupAvatar = itemView.findViewById(R.id.imageViewGroupAvatar);
            usersOnlineMessage = itemView.findViewById(R.id.textViewUsersOnlineMessage);
            protectedStatus = itemView.findViewById(R.id.imageViewGroupProtected);
            container = (RelativeLayout) itemView;
        }
    }


    class GroupHeaderItemHolder extends RecyclerView.ViewHolder {

        TextView txtGroupStatus;

        GroupHeaderItemHolder(@NonNull View itemView) {
            super(itemView);

            txtGroupStatus = itemView.findViewById(R.id.txt_group_status);
        }
    }
}