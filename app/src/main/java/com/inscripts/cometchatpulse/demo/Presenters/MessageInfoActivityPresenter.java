package com.inscripts.cometchatpulse.demo.Presenters;

import android.content.Context;
import android.content.Intent;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.MessageReceipt;
import com.inscripts.cometchatpulse.demo.Base.Presenter;
import com.inscripts.cometchatpulse.demo.Contracts.MessageInfoActivityContract;

import java.util.List;

public class MessageInfoActivityPresenter extends Presenter<MessageInfoActivityContract.MessageInfoActivityView>
        implements MessageInfoActivityContract.MessageInfoActivityPresenter {

    @Override
    public void getIntent(Context context,Intent intent) {

        if (intent.hasExtra("type")){

            switch (intent.getStringExtra("type")) {
                case CometChatConstants.MESSAGE_TYPE_IMAGE:
                case CometChatConstants.MESSAGE_TYPE_VIDEO:
                case CometChatConstants.MESSAGE_TYPE_AUDIO:
                    getBaseView().setType(intent.getStringExtra("url"), intent.getStringExtra("type"));
                    break;
                case CometChatConstants.MESSAGE_TYPE_TEXT:
                    getBaseView().setType(intent.getStringExtra("text"), intent.getStringExtra("type"));
                    break;
            }
        }

        if (intent.hasExtra("senderUID")){
            getBaseView().setSenderUID(intent.getStringExtra("senderUID"));
        }

        if (intent.hasExtra("receiverUID")){
            getBaseView().receiverUID(intent.getStringExtra("receiverUID"));
        }

        if (intent.hasExtra("id")){
            CometChat.getMessageReceipts(intent.getIntExtra("id", 0), new CometChat.CallbackListener<List<MessageReceipt>>() {
                @Override
                public void onSuccess(List<MessageReceipt> messageReceipts) {
                     getBaseView().setReceiptsAdapter(messageReceipts);
                }

                @Override
                public void onError(CometChatException e) {

                }
            });
        }
    }
}
