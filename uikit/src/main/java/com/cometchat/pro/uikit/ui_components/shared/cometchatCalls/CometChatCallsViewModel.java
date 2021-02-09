package com.cometchat.pro.uikit.ui_components.shared.cometchatCalls;

import android.content.Context;

import com.cometchat.pro.core.Call;
import com.cometchat.pro.models.BaseMessage;

import java.util.List;

public class CometChatCallsViewModel {

    private  Context context;

    private CometChatCallsAdapter callListAdapter;

    private CometChatCalls callListView;

    private CometChatCallsViewModel(){

    }
    public CometChatCallsViewModel(Context context, CometChatCalls cometChatCallList){
        this.callListView=cometChatCallList;
        this.context=context;
        setAdapter();
    }

    private CometChatCallsAdapter getAdapter(){
       if (callListAdapter==null){
           callListAdapter=new CometChatCallsAdapter(context);
       }
       return callListAdapter;
    }

    public void add(Call call){
        if (callListAdapter!=null)
            callListAdapter.add(call);
    }

    private void setAdapter(){
        callListAdapter=new CometChatCallsAdapter(context);
        callListView.setAdapter(callListAdapter);
    }


    public void setCallList(List<BaseMessage> callList) {
        if (callListAdapter!=null)
            callListAdapter.updateList(callList);
    }


    public void update(Call call) {
        if (callListAdapter!=null)
            callListAdapter.update(call);
    }

    public void remove(Call call) {
        if (callListAdapter!=null)
            callListAdapter.remove(call);
    }

    public int size() {
        return callListAdapter.getItemCount();
    }
}