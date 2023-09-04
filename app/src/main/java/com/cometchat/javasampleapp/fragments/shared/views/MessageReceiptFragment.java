package com.cometchat.javasampleapp.fragments.shared.views;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cometchat.chatuikit.shared.cometchatuikit.CometChatUIKit;
import com.cometchat.chatuikit.shared.views.CometChatReceipt.CometChatReceipt;
import com.cometchat.chatuikit.shared.views.CometChatReceipt.Receipt;
import com.cometchat.chat.constants.CometChatConstants;
import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.models.BaseMessage;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;


public class MessageReceiptFragment extends Fragment {
    private CometChatReceipt messageReceiptRead, messageReceiptDeliver, messageReceiptSent, messageReceiptProgress, messageReceiptError;
    private LinearLayout parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message_receipt, container, false);
        messageReceiptRead = view.findViewById(R.id.receiptRead);
        parentView = view.findViewById(R.id.parent_view);
        messageReceiptDeliver = view.findViewById(R.id.receiptDeliver);
        messageReceiptSent = view.findViewById(R.id.receiptSent);
        messageReceiptProgress = view.findViewById(R.id.receiptProgress);
        messageReceiptError = view.findViewById(R.id.receiptError);
        setReceipts();
        setUpUI(view);
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

    private void setUpUI(View view) {
        if (AppUtils.isNightMode(getContext())) {
            AppUtils.changeTextColorToWhite(getContext(), view.findViewById(R.id.message_receipt_text));
            AppUtils.changeTextColorToWhite(getContext(), view.findViewById(R.id.message_receipt_text_desc));
            AppUtils.changeTextColorToWhite(getContext(), view.findViewById(R.id.progress_text));
            AppUtils.changeTextColorToWhite(getContext(), view.findViewById(R.id.sent_text));
            AppUtils.changeTextColorToWhite(getContext(), view.findViewById(R.id.deliver_text));
            AppUtils.changeTextColorToWhite(getContext(), view.findViewById(R.id.read_text));
            AppUtils.changeTextColorToWhite(getContext(), view.findViewById(R.id.error_text));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.app_background_dark)));
        } else {
            AppUtils.changeTextColorToBlack(getContext(), view.findViewById(R.id.message_receipt_text));
            AppUtils.changeTextColorToBlack(getContext(), view.findViewById(R.id.message_receipt_text_desc));
            AppUtils.changeTextColorToBlack(getContext(), view.findViewById(R.id.progress_text));
            AppUtils.changeTextColorToBlack(getContext(), view.findViewById(R.id.sent_text));
            AppUtils.changeTextColorToBlack(getContext(), view.findViewById(R.id.deliver_text));
            AppUtils.changeTextColorToBlack(getContext(), view.findViewById(R.id.read_text));
            AppUtils.changeTextColorToBlack(getContext(), view.findViewById(R.id.error_text));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
        }
    }
}