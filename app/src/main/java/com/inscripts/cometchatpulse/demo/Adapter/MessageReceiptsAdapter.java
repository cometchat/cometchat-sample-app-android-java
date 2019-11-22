package com.inscripts.cometchatpulse.demo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cometchat.pro.models.MessageReceipt;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Utils.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageReceiptsAdapter extends RecyclerView.Adapter<MessageReceiptsAdapter.ReceiptsHolder> {

    private Context context;

    private HashMap<String,MessageReceipt> messageReceiptList;

    public MessageReceiptsAdapter(Context context, HashMap<String,MessageReceipt> messageReceiptList) {
        this.context = context;
        this.messageReceiptList = messageReceiptList;
    }


    @NonNull
    @Override
    public ReceiptsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ReceiptsHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.receipts_view,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptsHolder receiptsHolder, int i) {
         MessageReceipt messageReceipt= new ArrayList<MessageReceipt>(messageReceiptList.values()).get(i);
         if (messageReceipt.getReadAt()>0) {
             receiptsHolder.tvRead.setVisibility(View.VISIBLE);
             receiptsHolder.tvRead.setText("Read At:" + DateUtils.getTimeStringFromTimestamp(messageReceipt.getReadAt(), "hh:mm a"));
         }
         else
         {
             receiptsHolder.tvRead.setVisibility(View.GONE);
         }
         if (messageReceipt.getDeliveredAt()>0) {
             receiptsHolder.tvDelivery.setVisibility(View.VISIBLE);
             receiptsHolder.tvDelivery.setText("Delivered At:" + DateUtils.getTimeStringFromTimestamp(messageReceipt.getDeliveredAt(), "hh:mm a"));
         }
         else
         {
             receiptsHolder.tvDelivery.setVisibility(View.GONE);
         }
         Glide.with(context).load(messageReceipt.getSender().getAvatar()).into(receiptsHolder.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return messageReceiptList.size();
    }

    public void updateReciept(MessageReceipt messageReceipt) {
            messageReceiptList.put(messageReceipt.getSender().getUid(),messageReceipt);
            notifyDataSetChanged();
    }

    class ReceiptsHolder extends RecyclerView.ViewHolder {

        TextView tvDelivery;

        TextView tvRead;

         CircleImageView ivAvatar;

         ReceiptsHolder(@NonNull View itemView) {
            super(itemView);

            tvDelivery=itemView.findViewById(R.id.tvDelivery);
            tvRead=itemView.findViewById(R.id.tvRead);
            ivAvatar=itemView.findViewById(R.id.ivAvatar);
        }
    }
}
