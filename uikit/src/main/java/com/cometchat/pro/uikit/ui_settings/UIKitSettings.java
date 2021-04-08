package com.cometchat.pro.uikit.ui_settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.R;

import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_components.shared.CometChatSnackBar;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;

public class UIKitSettings {

    public static String PUBLIC_GROUP = "public_groups";
    public static String PASSWORD_GROUP = "password_protected_groups";
    public static String ALL_GROUP = "public_and_password_protected_groups";
    public static String ALL_USER = "all_users";
    public static String FRIENDS = "friends";
    public Context context;

    public UIKitSettings(Context context) {
        this.context = context;
    }

    public static void setMessagingSoundEnable(boolean isEnable) {
        UISettings.enableMessageSounds = isEnable;
    }

    public static void setAppID(String appID) {
        UIKitConstants.AppInfo.APP_ID = appID;
    }

    public static void setMapAccessKey(String mapsKey) { UIKitConstants.MapUrl.MAP_ACCESS_KEY = mapsKey; }

    public static void setAuthKey(String authKey) {
        UIKitConstants.AppInfo.AUTH_KEY = authKey;
    }
    public static void setCallSoundEnable(boolean isEnable) {
        UISettings.enableCallSounds = isEnable;
    }

    public static void enableLiveReaction(boolean isEnable) {
        UISettings.liveReaction = isEnable;
    }

    public static void setHyperLinkEmailColor(int color) {
        UISettings.emailColor = color;
    }

    public static void setHyperLinkPhoneColor(int color) {
        UISettings.phoneColor = color;
    }

    public static void setHyperLinkUrlColor(int color) {
        UISettings.urlColor = color;
    }

    public void showReplyPrivately(boolean isVisible) {
        UISettings.showReplyPrivately = isVisible;
    }
    public void addConnectionListener(String TAG) {
        CometChat.addConnectionListener(TAG, new CometChat.ConnectionListener() {
            @Override
            public void onConnected() {
                Toast.makeText(context,"OnConnected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnecting() {
                Toast.makeText(context,"OnConnecting",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDisconnected() {
                Toast.makeText(context,"OnDisConnected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFeatureThrottled() {
                Toast.makeText(context,"OnFeatureThrottled",Toast.LENGTH_LONG).show();
            }
        });
    }
    @Deprecated
    public static void showCallNotification(boolean isVisible) {
        if (!isVisible) {
            UIKitConstants.MessageRequest.messageCategoriesForGroup
                    .remove(com.cometchat.pro.constants.CometChatConstants.CATEGORY_CALL);
            UIKitConstants.MessageRequest.messageTypesForUser
                    .remove(com.cometchat.pro.constants.CometChatConstants.CATEGORY_CALL);

        } else {
            if (!UIKitConstants.MessageRequest.messageCategoriesForGroup
                    .contains(com.cometchat.pro.constants.CometChatConstants.CATEGORY_CALL)) {
                UIKitConstants.MessageRequest.messageCategoriesForGroup
                        .add(com.cometchat.pro.constants.CometChatConstants.CATEGORY_CALL);
            }
            if (!UIKitConstants.MessageRequest.messageCategoriesForUser
                    .contains(com.cometchat.pro.constants.CometChatConstants.CATEGORY_CALL)) {
                UIKitConstants.MessageRequest.messageCategoriesForUser
                        .add(com.cometchat.pro.constants.CometChatConstants.CATEGORY_CALL);
            }

        }
    }

    public static void hideCallActions(boolean isHidden) {
        UISettings.hideCallNotification = isHidden;
        if (isHidden) {
            UIKitConstants.MessageRequest.messageCategoriesForGroup
                    .remove(com.cometchat.pro.constants.CometChatConstants.CATEGORY_CALL);
            UIKitConstants.MessageRequest.messageTypesForUser
                    .remove(com.cometchat.pro.constants.CometChatConstants.CATEGORY_CALL);

        } else {
            if (!UIKitConstants.MessageRequest.messageCategoriesForGroup
                    .contains(com.cometchat.pro.constants.CometChatConstants.CATEGORY_CALL)) {
                UIKitConstants.MessageRequest.messageCategoriesForGroup
                        .add(com.cometchat.pro.constants.CometChatConstants.CATEGORY_CALL);
            }
            if (!UIKitConstants.MessageRequest.messageCategoriesForUser
                    .contains(com.cometchat.pro.constants.CometChatConstants.CATEGORY_CALL)) {
                UIKitConstants.MessageRequest.messageCategoriesForUser
                        .add(com.cometchat.pro.constants.CometChatConstants.CATEGORY_CALL);
            }

        }
    }

    @Deprecated
    public static void showGroupNotification(boolean isVisible) {
        if (!isVisible) {
            UIKitConstants.MessageRequest.messageTypesForGroup
                    .remove(com.cometchat.pro.constants.CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER);
        } else {
            if (!UIKitConstants.MessageRequest.messageTypesForGroup
                    .contains(com.cometchat.pro.constants.CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER)) {
                UIKitConstants.MessageRequest.messageTypesForGroup
                        .add(com.cometchat.pro.constants.CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER);
            }
        }
    }

    public static void hideGroupActions(boolean isHidden) {
        UISettings.hideGroupNotification = isHidden;
        if (isHidden) {
            UIKitConstants.MessageRequest.messageTypesForGroup
                    .remove(com.cometchat.pro.constants.CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER);
        } else {
            if (!UIKitConstants.MessageRequest.messageTypesForGroup
                    .contains(com.cometchat.pro.constants.CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER)) {
                UIKitConstants.MessageRequest.messageTypesForGroup
                        .add(com.cometchat.pro.constants.CometChatConstants.ActionKeys.ACTION_TYPE_GROUP_MEMBER);
            }
        }
    }

    public void chatWithUser(String uid, CometChat.CallbackListener callbackListener) {
        CometChat.getUser(uid, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                Intent intent = new Intent(context, CometChatMessageListActivity.class);
                intent.putExtra(UIKitConstants.IntentStrings.UID, user.getUid());
                intent.putExtra(UIKitConstants.IntentStrings.AVATAR, user.getAvatar());
                intent.putExtra(UIKitConstants.IntentStrings.STATUS, user.getStatus());
                intent.putExtra(UIKitConstants.IntentStrings.NAME, user.getName());
                intent.putExtra(UIKitConstants.IntentStrings.TYPE, com.cometchat.pro.constants.CometChatConstants.RECEIVER_TYPE_USER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onError(CometChatException e) {
                callbackListener.onError(e);
            }
        });
    }

    public void chatWithGroup(String guid, CometChat.CallbackListener callbackListener) {
        CometChat.getGroup(guid, new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group) {
                if (group.isJoined()) {
                    Intent intent = new Intent(context, CometChatMessageListActivity.class);
                    intent.putExtra(UIKitConstants.IntentStrings.GUID, group.getGuid());
                    intent.putExtra(UIKitConstants.IntentStrings.AVATAR, group.getIcon());
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_OWNER,group.getOwner());
                    intent.putExtra(UIKitConstants.IntentStrings.NAME, group.getName());
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_TYPE,group.getGroupType());
                    intent.putExtra(UIKitConstants.IntentStrings.TYPE, com.cometchat.pro.constants.CometChatConstants.RECEIVER_TYPE_GROUP);
                    intent.putExtra(UIKitConstants.IntentStrings.MEMBER_COUNT,group.getMembersCount());
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_DESC,group.getDescription());
                    intent.putExtra(UIKitConstants.IntentStrings.GROUP_PASSWORD,group.getPassword());
                    context.startActivity(intent);
                } else {
                    callbackListener.onError(new CometChatException("ERR_NOT_A_MEMBER",
                            String.format(context.getString(R.string.not_a_member),group.getName())));
                }
            }

            @Override
            public void onError(CometChatException e) {
                callbackListener.onError(e);
            }
        });
    }

    public static void setColor(String color) {
        UISettings.color = color;
    }

    public static void showUsersInNavigation(boolean showUsers) {
        UISettings.showUsersBB = showUsers;
    }

    public static void showGroupsInNavigation(boolean showGroups) {
        UISettings.showGroupsBB = showGroups;
    }

    public static void showChatsInNavigation(boolean showChats) {
        UISettings.showChatsBB = showChats;
    }

    public static void showCallsInNavigation(boolean showCalls) {
        UISettings.showCallsBB = showCalls;
    }

    public static void showSettingsInNavigation(boolean showUserSettings) {
        UISettings.showUserSettingsBB = showUserSettings;
    }

    public static void setGroupType(String groupListing) {
        UISettings.groupListing = groupListing;
    }

    public static void setUsersType(String userListing) {
        UISettings.userListing = userListing;
    }

    public static void allowSendingMessage(boolean enableSendingMessage) {
        UISettings.enableSendingMessage = enableSendingMessage;
    }

    public static void showReadDeliveryReceipts(boolean showReadDeliveryReceipts) {
        UISettings.showReadDeliveryReceipts = showReadDeliveryReceipts;
    }

    public static void allowEmojisInLargeSize(boolean sendEmojisLargeSize) {
        UISettings.sendEmojisLargeSize = sendEmojisLargeSize;
    }

    public static void allowSendingEmojis(boolean sendEmojis) {
        UISettings.sendEmojis = sendEmojis;
    }


    public static void allowSendingVoiceNotes(boolean sendVoiceNotes) {
        UISettings.sendVoiceNotes = sendVoiceNotes;
    }


    public static void allowSendingFiles(boolean sendFiles) {
        UISettings.sendFiles = sendFiles;
    }

    public static void allowSendingPolls(boolean sendPolls) {
        UISettings.sendPolls = sendPolls;
    }

    public static void allowSendingPhotosVideo(boolean sendPhotosVideo) {
        UISettings.sendPhotosVideo = sendPhotosVideo;
    }

    public static void enableThreadedReplies(boolean enableThreadedReplies) {
        UISettings.enableThreadedReplies = enableThreadedReplies;
    }

    public static void enableReplyToMessage(boolean enableReplyToMessage) {
        UISettings.enableReplyToMessage = enableReplyToMessage;
    }

    public static void setEnableShareCopyForward(boolean enableShareCopyForward) {
        UISettings.enableShareCopyForward = enableShareCopyForward;
    }

    public static void allowDeletingMessage(boolean enableDeleteMessage) {
        UISettings.enableDeleteMessage = enableDeleteMessage;
    }

    public static void allowEditingMessage(boolean enableEditingMessage) {
        UISettings.enableEditingMessage = enableEditingMessage;
    }

    public static void allowShareLocation(boolean shareLocation) {
        UISettings.shareLocation = shareLocation;
    }

    public static void allowUsersToBlock(boolean allowUserToblockUser) {
        UISettings.blockUser = allowUserToblockUser;
    }

    public static void showTypingIndicators(boolean showTypingIndicators) {
        UISettings.showTypingIndicators = showTypingIndicators;
    }

    public static void showSharedMedia(boolean showSharedMedia) {
        UISettings.viewSharedMedia = showSharedMedia;
    }

    public static void setShowUserPresence(boolean showUserPresence) {
        UISettings.showUserPresence = showUserPresence;
    }

    public static void allowPromoteDemoteMembers(boolean allowPromoteDemoteMembers) {
        UISettings.allowPromoteDemoteMembers = allowPromoteDemoteMembers;
    }

    public static void allowBanKickMembers(boolean allowBanKickMembers) {
        UISettings.allowBanKickMembers = allowBanKickMembers;
    }

    public static void allowAddMembersInGroup(boolean allowAddMembersInGroup) {
        UISettings.allowAddMembers = allowAddMembersInGroup;
    }

    public static void allowModeratorToDeleteMessages(boolean allowModeratorToDeleteMessages) {
        UISettings.allowModeratorToDeleteMessages = allowModeratorToDeleteMessages;
    }

    public static void allowDeleteGroups(boolean allowDeleteGroups) {
        UISettings.allowDeleteGroups = allowDeleteGroups;
    }

    public static void showGroupMembers(boolean showGroupMembers) {
        UISettings.viewGroupMember = showGroupMembers;
    }

    public static void allowJoinOrLeaveGroup(boolean joinOrLeaveGroup) {
        UISettings.joinOrLeaveGroup = joinOrLeaveGroup;
    }


    public static void showGroupCreate(boolean groupCreate) {
        UISettings.groupCreate = groupCreate;
    }

    public static void enableVideoCalling(boolean enableVideoCalling) {
        UISettings.enableVideoCalling = enableVideoCalling;
    }

    public static void enableVoiceCalling(boolean enableVoiceCalling) {
        UISettings.enableVoiceCalling = enableVoiceCalling;
    }

    public static void enableReactionsOnMessage(boolean enableReactionOnMessage) {
        UISettings.allowReactionOnMessage = enableReactionOnMessage;
    }

    public static void enableCollaborativeWhiteBoard(boolean enableWhiteBoardSharing) {
        UISettings.sendWhiteBoard = enableWhiteBoardSharing;
    }

    public static void enableCollaborativeWriteBoard(boolean enableWriteBoardSharing) {
        UISettings.sendWriteBoard = enableWriteBoardSharing;
    }

    public static void enableMessageTranslation(boolean enableMessageTranslation) {
        UISettings.allowMessageTranslation = enableMessageTranslation;
    }
}
