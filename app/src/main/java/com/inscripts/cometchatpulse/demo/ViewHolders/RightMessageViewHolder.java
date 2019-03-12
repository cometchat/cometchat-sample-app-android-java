package com.inscripts.cometchatpulse.demo.ViewHolders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.inscripts.cometchatpulse.demo.R;

public class RightMessageViewHolder extends RecyclerView.ViewHolder{
    public TextView textMessage;
    public TextView messageTimeStamp;
    public ImageView rightArrow,messageStatus;

    public RightMessageViewHolder(View itemView) {
        super(itemView);
        textMessage = itemView.findViewById(R.id.textViewMessage);
        messageStatus = itemView.findViewById(R.id.img_message_status);
        messageTimeStamp = itemView.findViewById(R.id.timestamp);
    }

}
