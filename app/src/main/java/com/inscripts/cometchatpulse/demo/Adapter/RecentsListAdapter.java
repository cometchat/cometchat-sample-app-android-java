package com.inscripts.cometchatpulse.demo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.Action;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.CustomMessage;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.TextMessage;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Activity.GroupChatActivity;
import com.inscripts.cometchatpulse.demo.Activity.OneToOneChatActivity;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.CustomView.CircleImageView;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Utils.ColorUtils;
import com.inscripts.cometchatpulse.demo.Utils.DateUtils;
import com.inscripts.cometchatpulse.demo.Utils.FontUtils;
import com.inscripts.cometchatpulse.demo.Utils.MediaUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static java.security.AccessController.getContext;


public class RecentsListAdapter extends RecyclerView.Adapter<RecentsListAdapter.RecentsViewHolder> {

    private final Context context;

    private List<Conversation> conversationList;

    private boolean isBlockedList;

    private int resId;

    private String guid,icon,groupName,str="";

    private String uid,avatar,username;

    private HashMap<String, Integer> unreadCountMap;

    public RecentsListAdapter( List<Conversation> conversationList, Context context, int resId, boolean isBlockedList) {
        this.conversationList = conversationList;
        this.context = context;
        this.isBlockedList = isBlockedList;
        this.resId = resId;
        new FontUtils(context);

    }

    @NonNull
    @Override
    public RecentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resId, viewGroup, false);
        return new RecentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentsViewHolder contactViewHolder, final int i) {

        Conversation conversation =conversationList.get(contactViewHolder.getAdapterPosition());
        Drawable statusDrawable;
//        contactViewHolder.messageTime.setText(DateUtils.getLastMessageDate(conversation.getLastMessage().getSentAt()));
        if (conversation.getConversationType().equals(CometChatConstants.CONVERSATION_TYPE_GROUP))
        {

//            str = conversation.getLastMessage().getSender().getName()+": ";
            guid = ((Group)conversation.getConversationWith()).getGuid();
            groupName = ((Group)conversation.getConversationWith()).getName();
            contactViewHolder.userName.setText(groupName);
            setIcon(((Group) conversation.getConversationWith()).getIcon(),R.drawable.cc_ic_group,contactViewHolder.avatar);
        }
        else
        {
            str = "";
            uid = ((User)conversation.getConversationWith()).getUid();
            username = ((User)conversation.getConversationWith()).getName();
            contactViewHolder.userName.setText(username);
             setIcon(((User) conversation.getConversationWith()).getAvatar(),R.drawable.default_avatar,contactViewHolder.avatar);
        }
        contactViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (conversation.getConversationType().equals(CometChatConstants.CONVERSATION_TYPE_GROUP)) {
                    conversation.setUnreadMessageCount(0);
                    notifyDataSetChanged();
                    Intent intent = new Intent(context, GroupChatActivity.class);
                    intent.putExtra(StringContract.IntentStrings.INTENT_GROUP_ID, ((Group)conversation.getConversationWith()).getGuid());
                    intent.putExtra(StringContract.IntentStrings.INTENT_GROUP_NAME, ((Group)conversation.getConversationWith()).getName());
                    context.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(context, OneToOneChatActivity.class);
                    intent.putExtra(StringContract.IntentStrings.USER_ID, ((User)conversation.getConversationWith()).getUid());
                    conversation.setUnreadMessageCount(0);
                    notifyDataSetChanged();
                    intent.putExtra(StringContract.IntentStrings.USER_AVATAR, ((User)conversation.getConversationWith()).getAvatar());
                    intent.putExtra(StringContract.IntentStrings.USER_NAME, ((User)conversation.getConversationWith()).getName());
                    context.startActivity(intent);
                }
            }
        });
        contactViewHolder.userName.setTypeface(FontUtils.robotoRegular);
        contactViewHolder.lastMessage.setTypeface(FontUtils.robotoRegular);
        if (conversation.getLastMessage()!=null) {
            Log.e("LastMessage: ",conversation.getLastMessage().toString());
            if (conversation.getLastMessage().getType().equals(CometChatConstants.MESSAGE_TYPE_TEXT)) {
                contactViewHolder.lastMessage.setText(((TextMessage) conversation.getLastMessage()).getText());
            } else if (conversation.getLastMessage().getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                contactViewHolder.lastMessage.setText("Image File");
            } else if (conversation.getLastMessage().getType().equals(CometChatConstants.MESSAGE_TYPE_AUDIO) && conversation.getLastMessage().getCategory().equals(CometChatConstants.CATEGORY_MESSAGE)) {
                contactViewHolder.lastMessage.setText("Audio File");
            } else if (conversation.getLastMessage().getType().equals(CometChatConstants.MESSAGE_TYPE_VIDEO)) {
                contactViewHolder.lastMessage.setText("Video File");
            } else if (conversation.getLastMessage().getType().equals(CometChatConstants.MESSAGE_TYPE_CUSTOM)) {
                contactViewHolder.lastMessage.setText(((CustomMessage) conversation.getLastMessage()).getSubType());
            } else if (conversation.getLastMessage().getType().equals(CometChatConstants.MESSAGE_TYPE_FILE)) {
                contactViewHolder.lastMessage.setText("File");
            } else if (conversation.getLastMessage().getCategory().equals(CometChatConstants.CATEGORY_ACTION)) {
                contactViewHolder.lastMessage.setText(((Action) conversation.getLastMessage()).getMessage());
            } else if (conversation.getLastMessage().getCategory().equals(CometChatConstants.CATEGORY_CALL)) {
                contactViewHolder.lastMessage.setText("Call " + ((Call) conversation.getLastMessage()).getCallStatus());
            }
            else
            {
                contactViewHolder.lastMessage.setText("Custom Message");
            }
        }
        if (isBlockedList) {
            contactViewHolder.lastMessage.setVisibility(View.INVISIBLE);
        }
        if (conversation.getUnreadMessageCount()>0)
        {
            contactViewHolder.unreadCount.setVisibility(View.VISIBLE);
        }
        else
        {
            contactViewHolder.unreadCount.setVisibility(View.GONE);
        }
        contactViewHolder.view.setTag(R.string.message, conversation.getConversationId());
        contactViewHolder.unreadCount.setText(conversation.getUnreadMessageCount()+"");
    }

    private void setIcon(String url,int drawableRes,CircleImageView circleImageView){

        if (url!= null && !url.trim().isEmpty()) {
            Glide.with(context).load(url).into(circleImageView);
            circleImageView.setCircleBackgroundColor(context.getResources().getColor(android.R.color.white));
        } else {
            Drawable drawable = context.getResources().getDrawable(drawableRes);
            circleImageView.setCircleBackgroundColor(context.getResources().getColor(R.color.secondaryColor));
            circleImageView.setImageBitmap(MediaUtils.getPlaceholderImage(context, drawable));

        }
    }


    public void refreshData(List<Conversation> conversationsList) {
        if (conversationsList!=null) {
            for (Conversation conversation : conversationsList)
            {
                if (this.conversationList.contains(conversation))
                {
                    this.conversationList.remove(conversation);
                }
            }
            this.conversationList.addAll(conversationsList);
            notifyDataSetChanged();
        }
    }

    public void setFilterList(List<Conversation> hashMap) {
        conversationList=hashMap;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }



    public void updateConversation(Conversation newConversation) {
        if (conversationList.contains(newConversation)){
            Conversation oldConversation=conversationList.get(conversationList.indexOf(newConversation));
            conversationList.remove(oldConversation);
            newConversation.setUnreadMessageCount(oldConversation.getUnreadMessageCount()+1);
            Log.e( "updateConversation: ",oldConversation.toString()+"\n"+newConversation.toString());
            conversationList.add(0,newConversation);
            notifyDataSetChanged();
        }else {
            conversationList.add(0,newConversation);
            notifyItemInserted(0);
        }
    }

    public void clear() {
        conversationList.clear();
        notifyDataSetChanged();
    }


    public class RecentsViewHolder extends RecyclerView.ViewHolder {

        public TextView userName;
        public TextView lastMessage;
        public TextView unreadCount;
        public TextView messageTime;
        public CircleImageView avatar;
        public View view;

        RecentsViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            avatar = view.findViewById(R.id.imageViewUserAvatar);
            userName = (TextView) view.findViewById(R.id.textviewUserName);
            lastMessage = (TextView) view.findViewById(R.id.textviewLastMessage);
            unreadCount = (TextView) view.findViewById(R.id.textviewSingleChatUnreadCount);
            messageTime = (TextView)view.findViewById(R.id.textviewSingleChatTime);

        }
    }
}