package com.cometchat.pro.uikit.ui_components.messages.thread_message_list;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import androidx.fragment.app.Fragment;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.uikit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_components.messages.message_actions.listener.MessageActionCloseListener;
import com.cometchat.pro.uikit.ui_components.messages.message_actions.listener.OnMessageLongClick;

import com.cometchat.pro.uikit.ui_settings.FeatureRestriction;

/**

* Purpose - CometChatMessageListActivity.class is a Activity used to display messages using CometChatMessageScreen.class. It takes
            parameter like TYPE to differentiate between User MessageScreen & Group MessageScreen.

            It passes parameters like UID (userID) ,AVATAR (userAvatar) ,NAME (userName) ,STATUS (userStatus) to CometChatMessageScreen.class
            if TYPE is CometChatConstant.RECEIVER_TYPE_USER

            It passes parameters like GUID (groupID) ,AVATAR (groupIcon) ,NAME (groupName) ,GROUP_OWNER (groupOwner) to CometChatMessageScreen.class
            if TYPE is CometChatConstant.RECEIVER_TYPE_GROUP

            @see CometChatConstants
            @see CometChatThreadMessageList


*/

public class CometChatThreadMessageListActivity extends AppCompatActivity implements ThreadAdapter.OnMessageLongClick {

    private static final String TAG = "CometChatMessageListAct";

    private OnMessageLongClick messageLongClick;

    Fragment fragment = new CometChatThreadMessageList();

    private String avatar;

    private String name;

    private String uid;

    private String messageType;

    private String message;

    private String messagefileName;

    private String mediaUrl;

    private String mediaExtension;

    private int messageId;

    private int mediaSize;

    private String mediaMime;

    private String type;

    private String Id;

    private long sentAt;

    private String messageCategory;

    private double latitude;

    private double longitude;

    private int replyCount;

    private String conversationName;

    private String baseMessage;

    private String pollQuestion;

    private String pollOptions;

    private ArrayList<String> pollResult;

    private int voteCount;

    private HashMap<String,String> reactionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cometchat_message_list);

        if (FeatureRestriction.getColor() !=null) {
            getWindow().setStatusBarColor(Color.parseColor(FeatureRestriction.getColor()));
        }
        EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
        EmojiCompat.init(config);

         if (getIntent()!=null) {
             Bundle bundle = new Bundle();
//             if (getIntent().hasExtra(StringContract.IntentStrings.PARENT_BASEMESSAGE))
//                 baseMessage = getIntent().getStringExtra(StringContract.IntentStrings.PARENT_BASEMESSAGE);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE_CATEGORY))
                 messageCategory = getIntent().getStringExtra(UIKitConstants.IntentStrings.MESSAGE_CATEGORY);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.LOCATION_LONGITUDE))
                 longitude = getIntent().getDoubleExtra(UIKitConstants.IntentStrings.LOCATION_LONGITUDE,0);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.LOCATION_LATITUDE))
                 latitude = getIntent().getDoubleExtra(UIKitConstants.IntentStrings.LOCATION_LATITUDE,0);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.CONVERSATION_NAME))
                 conversationName = getIntent().getStringExtra(UIKitConstants.IntentStrings.CONVERSATION_NAME);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.PARENT_ID))
                 messageId = getIntent().getIntExtra(UIKitConstants.IntentStrings.PARENT_ID,0);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.REPLY_COUNT))
                 replyCount = getIntent().getIntExtra(UIKitConstants.IntentStrings.REPLY_COUNT,0);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.AVATAR))
                 avatar = getIntent().getStringExtra(UIKitConstants.IntentStrings.AVATAR);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.NAME))
                 name = getIntent().getStringExtra(UIKitConstants.IntentStrings.NAME);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE))
                 messageType = getIntent().getStringExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.UID))
                 uid = getIntent().getStringExtra(UIKitConstants.IntentStrings.UID);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.SENTAT))
                 sentAt = getIntent().getLongExtra(UIKitConstants.IntentStrings.SENTAT,0);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.TEXTMESSAGE))
                 message = getIntent().getStringExtra(UIKitConstants.IntentStrings.TEXTMESSAGE);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME))
                 messagefileName = getIntent().getStringExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE))
                 mediaSize = getIntent().getIntExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE,0);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL))
                 mediaUrl = getIntent().getStringExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION))
                 mediaExtension = getIntent().getStringExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.TYPE))
                 type = getIntent().getStringExtra(UIKitConstants.IntentStrings.TYPE);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_MIME_TYPE))
                 mediaMime = getIntent().getStringExtra(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_MIME_TYPE);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.POLL_QUESTION))
                 pollQuestion = getIntent().getStringExtra(UIKitConstants.IntentStrings.POLL_QUESTION);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.POLL_OPTION))
                 pollOptions = getIntent().getStringExtra(UIKitConstants.IntentStrings.POLL_OPTION);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.POLL_RESULT))
                 pollResult = getIntent().getStringArrayListExtra(UIKitConstants.IntentStrings.POLL_RESULT);
             if (getIntent().hasExtra(UIKitConstants.IntentStrings.POLL_VOTE_COUNT))
                 voteCount = getIntent().getIntExtra(UIKitConstants.IntentStrings.POLL_VOTE_COUNT,0);
             if (type.equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                 if (getIntent().hasExtra(UIKitConstants.IntentStrings.GUID))
                     Id = getIntent().getStringExtra(UIKitConstants.IntentStrings.GUID);
             } else{
                 if (getIntent().hasExtra(UIKitConstants.IntentStrings.UID))
                     Id = getIntent().getStringExtra(UIKitConstants.IntentStrings.UID);
             }
             bundle.putString(UIKitConstants.IntentStrings.MESSAGE_CATEGORY,messageCategory);
             bundle.putString(UIKitConstants.IntentStrings.ID,Id);
             bundle.putString(UIKitConstants.IntentStrings.CONVERSATION_NAME,conversationName);
             bundle.putString(UIKitConstants.IntentStrings.TYPE,type);
             bundle.putString(UIKitConstants.IntentStrings.AVATAR, avatar);
             bundle.putString(UIKitConstants.IntentStrings.NAME, name);
             bundle.putInt(UIKitConstants.IntentStrings.PARENT_ID,messageId);
             bundle.putInt(UIKitConstants.IntentStrings.REPLY_COUNT,replyCount);
             bundle.putString(UIKitConstants.IntentStrings.MESSAGE_TYPE,messageType);
             bundle.putString(UIKitConstants.IntentStrings.UID, uid);
             bundle.putLong(UIKitConstants.IntentStrings.SENTAT, sentAt);

             if (getIntent().hasExtra(UIKitConstants.IntentStrings.REACTION_INFO)) {
                 reactionInfo = (HashMap<String,String>) getIntent().getSerializableExtra(UIKitConstants.IntentStrings.REACTION_INFO);
                 bundle.putSerializable(UIKitConstants.IntentStrings.REACTION_INFO, reactionInfo);
             }

             if (messageType.equals(CometChatConstants.MESSAGE_TYPE_TEXT))
                  bundle.putString(UIKitConstants.IntentStrings.TEXTMESSAGE,message);
             else if (messageType.equals(UIKitConstants.IntentStrings.LOCATION)) {
                  bundle.putDouble(UIKitConstants.IntentStrings.LOCATION_LATITUDE,latitude);
                  bundle.putDouble(UIKitConstants.IntentStrings.LOCATION_LONGITUDE,longitude);
             } else if (messageType.equals(UIKitConstants.IntentStrings.POLLS)) {
                  bundle.putStringArrayList(UIKitConstants.IntentStrings.POLL_RESULT,pollResult);
                  bundle.putString(UIKitConstants.IntentStrings.POLL_QUESTION,pollQuestion);
                  bundle.putString(UIKitConstants.IntentStrings.POLL_OPTION,pollOptions);
                  bundle.putInt(UIKitConstants.IntentStrings.POLL_VOTE_COUNT,voteCount);
             } else if (messageType.equals(UIKitConstants.IntentStrings.STICKERS)) {
                  bundle.putString(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL,mediaUrl);
                  bundle.putString(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME,messagefileName);
             } else if (messageType.equals(UIKitConstants.IntentStrings.WHITEBOARD) ||
                      messageType.equals(UIKitConstants.IntentStrings.WRITEBOARD)) {
                  bundle.putString(UIKitConstants.IntentStrings.TEXTMESSAGE,message);
             } else {
                  bundle.putString(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_URL,mediaUrl);
                  bundle.putString(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_NAME,messagefileName);
                  bundle.putString(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_EXTENSION,mediaExtension);
                  bundle.putInt(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_SIZE,mediaSize);
                  bundle.putString(UIKitConstants.IntentStrings.MESSAGE_TYPE_IMAGE_MIME_TYPE,mediaMime);
             }

             fragment.setArguments(bundle);
             getSupportFragmentManager().beginTransaction().replace(R.id.chat_fragment, fragment).commit();
         }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.d(TAG, "onActivityResult: ");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: ");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setLongMessageClick(List<BaseMessage> baseMessage) {
        if (fragment!=null)
        ((OnMessageLongClick)fragment).setLongMessageClick(baseMessage);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void handleDialogClose(DialogInterface dialog) {
        ((MessageActionCloseListener)fragment).handleDialogClose(dialog);
        dialog.dismiss();
    }
}
