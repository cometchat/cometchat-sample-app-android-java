package com.inscripts.cometchatpulse.demo.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.R;

public class LeftMessageViewHolder extends RecyclerView.ViewHolder {
    public TextView textMessage;
    public TextView messageTimeStamp;
    public TextView senderName;
    public CircleImageView avatar;

    public LeftMessageViewHolder(View leftTextMessageView) {
        super(leftTextMessageView);
        textMessage = leftTextMessageView.findViewById(R.id.textViewMessage);
        messageTimeStamp = leftTextMessageView.findViewById(R.id.timeStamp);
        avatar = leftTextMessageView.findViewById(R.id.imgAvatar);
        senderName = leftTextMessageView.findViewById(R.id.senderName);
    }
}
