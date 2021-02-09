package com.cometchat.pro.uikit.ui_components.messages.message_information.Message_Receipts;

import android.content.Context;

import com.cometchat.pro.models.MessageReceipt;

import java.util.List;

public class CometChatReceiptsViewModel {

    private static final String TAG = "ReceiptListViewModel";

    private  Context context;

    private CometChatReceiptsAdapter receiptListAdapter;

    private CometChatReceiptsList receiptsList;



    public CometChatReceiptsViewModel(Context context, CometChatReceiptsList receiptsList,
                                      boolean showDelivery, boolean showRead){
        this.receiptsList = receiptsList;
        this.context=context;
        setReceiptListAdapter(receiptsList,showDelivery,showRead);
    }

    private CometChatReceiptsViewModel(){

    }

    private CometChatReceiptsAdapter getAdapter() {
        if (receiptListAdapter==null){
            receiptListAdapter=new CometChatReceiptsAdapter(context);
        }
        return receiptListAdapter;
    }

    public void add(MessageReceipt messageReceipt){
        if (receiptListAdapter!=null)
            receiptListAdapter.add(messageReceipt);

    }
    public void add(int index,MessageReceipt messageReceipt){
        if (receiptListAdapter!=null)
            receiptListAdapter.addAtIndex(index,messageReceipt);

    }

    public void update(MessageReceipt messageReceipt){
        if (receiptListAdapter!=null)
            receiptListAdapter.updateReceipts(messageReceipt);

    }

    public void clear()
    {
        if (receiptListAdapter!=null)
            receiptListAdapter.clear();
    }
    private void setReceiptListAdapter(CometChatReceiptsList cometChatReceiptsList,
                                       boolean showDelivery,boolean showRead){
        receiptListAdapter=new CometChatReceiptsAdapter(context);
        cometChatReceiptsList.showDelivery(showDelivery);
        cometChatReceiptsList.showRead(showRead);
        cometChatReceiptsList.setAdapter(receiptListAdapter);
    }

    public void setReceiptList(List<MessageReceipt> messageReceiptsList){
          getAdapter().updateList(messageReceiptsList);
    }

    public void update(int index, MessageReceipt messageReceipt) {
        if (receiptListAdapter!=null)
            receiptListAdapter.updateReceiptsAtIndex(index,messageReceipt);
    }
}

