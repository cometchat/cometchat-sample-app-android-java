package com.cometchat.pro.javasampleapp.fragments.shared.views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.views.CometChatReceipt.CometChatReceipt;
import com.cometchat.chatuikit.shared.views.CometChatReceipt.Receipt;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.javasampleapp.R;


public class MessageReceiptFragment extends Fragment {
    CometChatReceipt messageReceiptRead, messageReceiptDeliver, messageReceiptSent, messageReceiptProgress, messageReceiptError;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_receipt, container, false);
        messageReceiptRead = view.findViewById(R.id.receiptRead);
        messageReceiptDeliver = view.findViewById(R.id.receiptDeliver);
        messageReceiptSent = view.findViewById(R.id.receiptSent);
        messageReceiptProgress = view.findViewById(R.id.receiptProgress);
        messageReceiptError = view.findViewById(R.id.receiptError);
        setReceipts();
        return view;
    }

    private void setReceipts() {
        //Initializing BaseMessage
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setSender(CometChatUIKit.getLoggedInUser());
        baseMessage.setReceiverType(CometChatConstants.RECEIVER_TYPE_USER);

        //setting ReadReceipt
        baseMessage.setReadAt(System.currentTimeMillis());
        messageReceiptRead.setReceipt(Receipt.READ);

        //setting DeliverReceipt
        baseMessage.setReadAt(0);
        baseMessage.setDeliveredAt(System.currentTimeMillis());
        messageReceiptDeliver.setReceipt(Receipt.DELIVERED);

        //setting SentReceipt
        baseMessage.setReadAt(0);
        baseMessage.setDeliveredAt(0);
        baseMessage.setSentAt(System.currentTimeMillis());
        messageReceiptSent.setReceipt(Receipt.SENT);

        //setting progressReceipt
        baseMessage.setReadAt(0);
        baseMessage.setDeliveredAt(0);
        baseMessage.setSentAt(0);
        messageReceiptProgress.setReceipt(Receipt.IN_PROGRESS);

        //setting errorReceipt
        baseMessage.setReadAt(0);
        baseMessage.setDeliveredAt(0);
        baseMessage.setSentAt(-1);
        messageReceiptError.setReceipt(Receipt.ERROR);

    }
}