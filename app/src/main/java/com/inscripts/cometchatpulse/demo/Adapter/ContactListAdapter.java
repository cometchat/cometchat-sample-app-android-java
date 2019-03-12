package com.inscripts.cometchatpulse.demo.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.Utils.ColorUtils;
import com.inscripts.cometchatpulse.demo.Utils.FontUtils;
import com.inscripts.cometchatpulse.demo.Utils.MediaUtils;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.User;

import java.util.ArrayList;
import java.util.List;


public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {

    private final Context context;

    private List<com.cometchat.pro.models.User> userArrayList;

    private int resId;

    private List<String> uidList;

    public ContactListAdapter(String ownerId,List<User> userArrayList, Context context, int resId) {
        this.userArrayList = userArrayList;
        this.context = context;
        this.resId = resId;
        new FontUtils(context);

        uidList = new ArrayList<>();

        for (int i = 0; i < this.userArrayList.size(); i++) {
            uidList.add(userArrayList.get(i).getUid());
        }
    }


    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(resId, viewGroup, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder contactViewHolder, final int i) {

        final com.cometchat.pro.models.User user = userArrayList.get(i);
        Drawable statusDrawable;
        contactViewHolder.userName.setText(user.getName());
        contactViewHolder.userName.setTypeface(FontUtils.robotoRegular);
        contactViewHolder.userStatus.setTypeface(FontUtils.robotoRegular);
        contactViewHolder.userStatus.setText(user.getStatus());

        if (user.getStatus().equals(CometChatConstants.USER_STATUS_ONLINE)) {
            statusDrawable = context.getResources().getDrawable(R.drawable.cc_status_available);
        } else {
            statusDrawable = context.getResources().getDrawable(R.drawable.cc_status_offline);
        }

        contactViewHolder.statusImage.setImageDrawable(statusDrawable);

        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            Glide.with(context).load(user.getAvatar()).into(contactViewHolder.avatar);
        } else {
            Drawable drawable = context.getResources().getDrawable(R.drawable.default_avatar);
            try {
                contactViewHolder.avatar.setCircleBackgroundColor(ColorUtils.getMaterialColor(context));
                contactViewHolder.avatar.setImageBitmap(MediaUtils.getPlaceholderImage(context, drawable));
            } catch (Exception e) {
                contactViewHolder.avatar.setCircleBackgroundColor(context.getResources().getColor(R.color.secondaryDarkColor));
                contactViewHolder.avatar.setImageDrawable(drawable);
            }
        }

        contactViewHolder.view.setTag(R.string.user_avatar, user.getAvatar());
        contactViewHolder.view.setTag(R.string.contact_id, user.getUid());
        contactViewHolder.view.setTag(R.string.contact_name, user.getName());
        contactViewHolder.view.setTag(R.string.user, user);
        contactViewHolder.view.setTag(R.string.userHolder, contactViewHolder);

    }


    public void refreshData(List<com.cometchat.pro.models.User> userArrayList) {
        this.userArrayList.addAll(userArrayList);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        if (userArrayList != null) {
            return userArrayList.size();
        } else {
            return 0;
        }
    }

    public void updateUserPresence(User user) {
//
        try {
            int index = uidList.indexOf(user.getUid());
            userArrayList.get(index).setStatus(user.getStatus());
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView userName;
        public TextView userStatus;
        public TextView unreadCount;
        public CircleImageView avatar;
        public ImageView statusImage;
        public View view;

        ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            avatar = view.findViewById(R.id.imageViewUserAvatar);
            userName = (TextView) view.findViewById(R.id.textviewUserName);
            userStatus = (TextView) view.findViewById(R.id.textviewUserStatus);
            statusImage = (ImageView) view.findViewById(R.id.imageViewStatusIcon);
            unreadCount = (TextView) view.findViewById(R.id.textviewSingleChatUnreadCount);


        }
    }
}
