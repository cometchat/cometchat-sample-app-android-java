package com.cometchat.pro.uikit.ui_settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.widget.Toast;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.Group;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.R;

import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_settings.enums.ConversationMode;
import com.cometchat.pro.uikit.ui_settings.enums.GroupMode;
import com.cometchat.pro.uikit.ui_settings.enums.UserMode;

import java.util.Locale;

public class UIKitSettings {

    //style
    protected static String UIcolor = "#03A9F4";
    //BottomBar
    private static boolean users = true;
    private static boolean groups = true;
    private static boolean conversations = true;
    private static boolean calls = true;
    private static boolean userSettings = true;

    private static boolean sendMessageInOneOneOne = true;
    private static boolean sendMessageInGroup = true;

    private static boolean userVideoCall = true;
    private static boolean groupVideoCall = true;

    private static boolean userAudioCall = true;
    private static boolean groupAudioCall = true;

    private static boolean banMember = true;
    private static boolean kickMember = true;

    private static boolean publicGroup = true;
    private static boolean passwordGroup = true;
    private static boolean privateGroup = true;

    private static boolean messageHistory = true;
    private static boolean searchUser = true;
    private static boolean searchGroup = true;
    private static boolean searchMessage = true;

    private static boolean callRecording = true;
    private static boolean callLiveStreaming = true;
    private static boolean callTranscription = true;


    private static boolean hideDeleteMessage = false;

    private static boolean linkPreview = true;
    private static boolean pinMessage = true;
    private static boolean saveMessage = true;
    private static boolean smartReplies = true;
    private static boolean voiceTranscription = true;
    private static boolean mentions = true;
    private static boolean profanityFilter = true;
    private static boolean malwareScanner = true;
    private static boolean dataMasking = true;
    private static boolean sentimentAnalysis = true;
    private static boolean imageModeration = true;
    private static boolean thumbnailGeneration = true;
    private static boolean richMedia = true;

    private static boolean unreadCount = true;

    private static boolean showReadDeliveryReceipts = true;
    private static boolean sendEmojisLargeSize = true;
    private static boolean sendEmojis = true;
    private static boolean sendGifs = true;
    private static boolean sendVoiceNotes = true;
    private static boolean sendFiles = true;
    private static boolean sendPolls = true;
    private static boolean sendPhotosVideo = true;
    private static boolean threadedChats = true;
    private static boolean replyingToMessage = true;
    private static boolean shareCopyForwardMessage = true;
    private static boolean deleteMessage = true;
    private static boolean editMessage = true;
    private static boolean shareLocation = true;
    private static boolean sendStickers = true;
    private static boolean collaborativeWhiteboard = true;
    private static boolean collaborativeDocument = true;
    private static boolean blockUser = true;
    private static boolean sendTypingIndicators = true;
    private static boolean viewSharedMedia = true;
    private static boolean showUserPresence = true;
    private static boolean allowPromoteDemoteMembers = true;
    private static boolean allowAddMembers = true;
    private static boolean allowModeratorToDeleteMemberMessages;
    private static boolean allowDeleteGroups = true;
    private static boolean viewGroupMember = true;
    private static boolean joinOrLeaveGroup = true;
    private static boolean groupCreate = true;
    private static boolean hideJoinLeaveNotifications = false;
    private static boolean enableSoundForMessages = true;
    private static boolean enableSoundForCalls = true;
    private static boolean callNotification = false;
    private static boolean sendLiveReaction = true;
    private static boolean sendMessageReaction = true;
    private static boolean allowMessageTranslation = true;
    private static boolean showReplyPrivately = true;

    protected static int emailColor = R.color.primaryTextColor;
    protected static int phoneColor = R.color.purple;
    protected static int urlColor = R.color.dark_blue;



    protected static ConversationMode conversationInMode = ConversationMode.ALL_CHATS;
    protected static GroupMode groupInMode = GroupMode.ALL_GROUP;
    protected static UserMode userInMode = UserMode.ALL_USER;

    public Context context;

    public UIKitSettings(Context context) {
        this.context = context;
    }

    public static void enableSoundForMessages(boolean isEnable) {
        enableSoundForMessages = isEnable;
    }

    public static void setAppID(String appID) {
        UIKitConstants.AppInfo.APP_ID = appID;
    }

    public static void setMapAccessKey(String mapsKey) { UIKitConstants.MapUrl.MAP_ACCESS_KEY = mapsKey; }

    public static void setAuthKey(String authKey) {
        UIKitConstants.AppInfo.AUTH_KEY = authKey;
    }

    public static void enableSoundForCalls(boolean isEnable) {
        enableSoundForCalls = isEnable;
    }

    public static void sendLiveReaction(boolean isEnable) {
        sendLiveReaction = isEnable;
    }

    public static void setHyperLinkEmailColor(int color) {
        emailColor = color;
    }

    public static void setHyperLinkPhoneColor(int color) {
        phoneColor = color;
    }

    public static void setHyperLinkUrlColor(int color) {
        urlColor = color;
    }

    public static ConversationMode getConversationsMode() {
        return conversationInMode;
    }
    public static GroupMode getGroupsMode() {
        return groupInMode;
    }
    public static UserMode getUsersMode() {
        return userInMode;
    }

    public static void showReplyPrivately(boolean isEnable) {
        showReplyPrivately = isEnable;
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
    public static void callNotification(boolean isVisible) {
        callNotification = isVisible;
        if (!isVisible) {
            UIKitConstants.MessageRequest.messageCategoriesForGroup
                    .remove(CometChatConstants.CATEGORY_CALL);
            UIKitConstants.MessageRequest.messageCategoriesForUser
                    .remove(CometChatConstants.CATEGORY_CALL);

        } else {
            if (!UIKitConstants.MessageRequest.messageCategoriesForGroup
                    .contains(CometChatConstants.CATEGORY_CALL)) {
                UIKitConstants.MessageRequest.messageCategoriesForGroup
                        .add(CometChatConstants.CATEGORY_CALL);
            }
            if (!UIKitConstants.MessageRequest.messageCategoriesForUser
                    .contains(CometChatConstants.CATEGORY_CALL)) {
                UIKitConstants.MessageRequest.messageCategoriesForUser
                        .add(CometChatConstants.CATEGORY_CALL);
            }

        }
    }

    @Deprecated
    public static void hideCallActions(boolean isHidden) {
        if (isHidden) {
            UIKitConstants.MessageRequest.messageCategoriesForGroup
                    .remove(CometChatConstants.CATEGORY_CALL);
            UIKitConstants.MessageRequest.messageCategoriesForUser
                    .remove(CometChatConstants.CATEGORY_CALL);

        } else {
            if (!UIKitConstants.MessageRequest.messageCategoriesForGroup
                    .contains(CometChatConstants.CATEGORY_CALL)) {
                UIKitConstants.MessageRequest.messageCategoriesForGroup
                        .add(CometChatConstants.CATEGORY_CALL);
            }
            if (!UIKitConstants.MessageRequest.messageCategoriesForUser
                    .contains(CometChatConstants.CATEGORY_CALL)) {
                UIKitConstants.MessageRequest.messageCategoriesForUser
                        .add(CometChatConstants.CATEGORY_CALL);
            }
        }
    }

    public static void groupNotifications(boolean isVisible) {
        hideJoinLeaveNotifications = isVisible;
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

    @Deprecated
    public static void hideGroupActions(boolean isHidden) {
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
                intent.putExtra(UIKitConstants.IntentStrings.LINK,user.getLink());
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
        UIcolor = color;
    }

    public static void users(boolean showUsers) {
        users = showUsers;
    }

    public static void groups(boolean showGroups) {
        groups = showGroups;
    }

    public static void conversations(boolean showChats) {
        conversations = showChats;
    }

    public static void calls(boolean showCalls) {
        calls = showCalls;
    }

    public static void userSettings(boolean showUserSettings) {
        userSettings = showUserSettings;
    }

    public static void setConversationsMode(ConversationMode conversationMode) {
        conversationInMode = conversationMode;
    }

    public static void setGroupsMode(GroupMode groupListing) {
        groupInMode = groupListing;
    }

    public static void publicGroup(boolean isEnable) { publicGroup = isEnable; }
    public static void passwordGroup(boolean isEnable) { passwordGroup = isEnable; }
    public static void privateGroup(boolean isEnable) { privateGroup = isEnable; }

    public static void setUsersMode(UserMode userListing) {
        userInMode = userListing;
    }

    public static void sendMessageInOneOneOne(boolean isEnable) {
        sendMessageInOneOneOne = isEnable;
    }

    public static void hideDeleteMessage(boolean isEnable) {
        hideDeleteMessage = isEnable;
    }

    public static void sendMessageInGroup(boolean isEnable) {
        sendMessageInGroup = isEnable;
    }

    public static void showReadDeliveryReceipts(boolean isEnable) {
        showReadDeliveryReceipts = isEnable;
    }

    public static void sendGifs(boolean isEnable) { sendGifs = isEnable; }

    public static void sendEmojisInLargeSize(boolean isEnable) {
        sendEmojisLargeSize = isEnable;
    }

    public static void sendEmojis(boolean isEnable) {
        sendEmojis = isEnable;
    }

    public static void smartReplies(boolean isEnable) {
        smartReplies = isEnable;
    }

    public static void linkPreview(boolean isEnable) {
        linkPreview = isEnable;
    }

    public static void dataMasking(boolean isEnable) {
        dataMasking = isEnable;
    }

    public static void pinMessage(boolean isEnable) {
        pinMessage = isEnable;
    }

    public static void saveMessage(boolean isEnable) {
        saveMessage = isEnable;
    }

    public static void sentimentAnalysis(boolean isEnable) {
        sentimentAnalysis = isEnable;
    }

    public static void profanityFilter(boolean isEnable) {
        profanityFilter = isEnable;
    }

    public static void voiceTranscription(boolean isEnable) {
        voiceTranscription = isEnable;
    }

    public static void malwareScanner(boolean isEnable) {
        malwareScanner = isEnable;
    }

    public static void mentions(boolean isEnable) {
        mentions = isEnable;
    }

    public static void imageModeration(boolean isEnable) {
        imageModeration = isEnable;
    }

    public static void thumbnailGeneration(boolean isEnable) {
        thumbnailGeneration = isEnable;
    }

    public static void richMedia(boolean isEnable) {
        richMedia = isEnable;
    }


    public static void sendStickers(boolean isEnable) {
         sendStickers = isEnable;
    }

    public static void unreadCount(boolean isEnable) { unreadCount = isEnable; }

    public static void sendVoiceNotes(boolean isEnable) {
        sendVoiceNotes = isEnable;
    }


    public static void sendFiles(boolean isEnable) {
        sendFiles = isEnable;
    }

    public static void sendPolls(boolean isEnable) {
        sendPolls = isEnable;
    }

    public static void sendPhotosVideo(boolean isEnable) {
        sendPhotosVideo = isEnable;
    }

    public static void threadedChats(boolean isEnable) {
        threadedChats = isEnable;
    }

    public static void replyingToMessage(boolean isEnable) {
        replyingToMessage = isEnable;
    }

    public static void shareCopyForwardMessage(boolean isEnable) {
        shareCopyForwardMessage = isEnable;
    }

    public static void deleteMessage(boolean isEnable) {
        deleteMessage = isEnable;
    }

    public static void editMessage(boolean isEnable) {
        editMessage = isEnable;
    }

    public static void shareLocation(boolean isEnable) {
        shareLocation = isEnable;
    }

    public static void blockUser(boolean isEnable) {
        blockUser = isEnable;
    }

    public static void sendTypingIndicators(boolean isEnable) {
        sendTypingIndicators = isEnable;
    }

    public static void viewSharedMedia(boolean isEnable) {
        viewSharedMedia = isEnable;
    }

    public static void showUserPresence(boolean isEnable) {
        showUserPresence = isEnable;
    }

    public static void allowPromoteDemoteMembers(boolean isEnable) {
        allowPromoteDemoteMembers = isEnable;
    }

    public static void kickMember(boolean isEnable) {
        kickMember = isEnable;
    }

    public static void banMember(boolean isEnable) {
        banMember = isEnable;
    }

    public static void allowAddMembers(boolean isEnable) {
        allowAddMembers = isEnable;
    }

    public static void allowModeratorToDeleteMemberMessages(boolean isEnable) {
        allowModeratorToDeleteMemberMessages = isEnable;
    }

    public static void allowDeleteGroups(boolean isEnable) {
        allowDeleteGroups = isEnable;
    }

    public static void viewGroupMembers(boolean isEnable) {
        viewGroupMember = isEnable;
    }

    public static void joinOrLeaveGroup(boolean isEnable) {
        joinOrLeaveGroup = isEnable;
    }


    public static void groupCreate(boolean isEnable) {
        groupCreate = isEnable;
    }

    public static void userVideoCall(boolean isEnable) {
        userVideoCall = isEnable;
    }

    public static void userAudioCall(boolean isEnable) {
        userAudioCall = isEnable;
    }

    public static void groupVideoCall(boolean isEnable) {
        groupVideoCall = isEnable;
    }

    public static void groupAudioCall(boolean isEnable) {
        groupAudioCall = isEnable;
    }

    public static void callRecording(boolean isEnable) {
        callRecording = isEnable;
    }

    public static void callLiveStreaming(boolean isEnable) {
        callLiveStreaming = isEnable;
    }

    public static void callTranscription(boolean isEnable) {
        callTranscription = isEnable;
    }

    public static void messageHistory(boolean isEnable) {
        messageHistory = isEnable;
    }

    public static void searchUser(boolean isEnable) {
        searchUser = isEnable;
    }

    public static void searchGroup(boolean isEnable) {
        searchGroup = isEnable;
    }

    public static void searchMessage(boolean isEnable) {
        searchMessage = isEnable;
    }

    public static void sendMessageReaction(boolean isEnable) {
        sendMessageReaction = isEnable;
    }

    public static void collaborativeWhiteBoard(boolean isEnable) {
        collaborativeWhiteboard = isEnable;
    }

    public static void collaborativeDocument(boolean isEnable) {
        collaborativeDocument = isEnable;
    }

    public static void allowMessageTranslation(boolean isEnable) {
        allowMessageTranslation = isEnable;
    }

    public static boolean isUsers() {
        return users;
    }

    public static boolean isGroups() {
        return groups;
    }

    public static boolean isConversations() {
        return conversations;
    }

    public static boolean isCalls() {
        return calls;
    }

    public static boolean isUserSettings() {
        return userSettings;
    }

    public static boolean isSendMessageInOneOneOne() {
        return sendMessageInOneOneOne;
    }

    public static boolean isSendMessageInGroup() {
        return sendMessageInGroup;
    }

    public static boolean isUserVideoCall() {
        return userVideoCall;
    }

    public static boolean isGroupVideoCall() {
        return groupVideoCall;
    }

    public static boolean isUserAudioCall() {
        return userAudioCall;
    }

    public static boolean isGroupAudioCall() {
        return groupAudioCall;
    }

    public static boolean isBanMember() {
        return banMember;
    }

    public static boolean isKickMember() {
        return kickMember;
    }

    public static boolean isPublicGroup() {
        return publicGroup;
    }

    public static boolean isPasswordGroup() {
        return passwordGroup;
    }

    public static boolean isPrivateGroup() {
        return privateGroup;
    }

    public static boolean isMessageHistory() {
        return messageHistory;
    }

    public static boolean isSearchUser() {
        return searchUser;
    }

    public static boolean isSearchGroup() {
        return searchGroup;
    }

    public static boolean isSearchMessage() {
        return searchMessage;
    }

    public static boolean isCallRecording() {
        return callRecording;
    }

    public static boolean isCallLiveStreaming() {
        return callLiveStreaming;
    }

    public static boolean isCallTranscription() {
        return callTranscription;
    }

    public static boolean isLinkPreview() {
        return linkPreview;
    }

    public static boolean isPinMessage() {
        return pinMessage;
    }

    public static boolean isSaveMessage() {
        return saveMessage;
    }

    public static boolean isSmartReplies() {
        return smartReplies;
    }

    public static boolean isVoiceTranscription() {
        return voiceTranscription;
    }

    public static boolean isMentions() {
        return mentions;
    }

    public static boolean isProfanityFilter() {
        return profanityFilter;
    }

    public static boolean isMalwareScanner() {
        return malwareScanner;
    }

    public static boolean isDataMasking() {
        return dataMasking;
    }

    public static boolean isSentimentAnalysis() {
        return sentimentAnalysis;
    }

    public static boolean isImageModeration() {
        return imageModeration;
    }

    public static boolean isThumbnailGeneration() {
        return thumbnailGeneration;
    }

    public static boolean isRichMedia() {
        return richMedia;
    }

    public static boolean isUnreadCount() {
        return unreadCount;
    }

    public static boolean isShowReadDeliveryReceipts() {
        return showReadDeliveryReceipts;
    }

    public static boolean isSendEmojisLargeSize() {
        return sendEmojisLargeSize;
    }

    public static boolean isSendEmojis() {
        return sendEmojis;
    }

    public static boolean isSendGifs() {
        return sendGifs;
    }

    public static boolean isSendVoiceNotes() {
        return sendVoiceNotes;
    }

    public static boolean isSendFiles() {
        return sendFiles;
    }

    public static boolean isSendPolls() {
        return sendPolls;
    }

    public static boolean isSendPhotosVideo() {
        return sendPhotosVideo;
    }

    public static boolean isThreadedChats() {
        return threadedChats;
    }

    public static boolean isReplyingToMessage() {
        return replyingToMessage;
    }

    public static boolean isShareCopyForwardMessage() {
        return shareCopyForwardMessage;
    }

    public static boolean isDeleteMessage() {
        return deleteMessage;
    }

    public static boolean isEditMessage() {
        return editMessage;
    }

    public static boolean isShareLocation() {
        return shareLocation;
    }

    public static boolean isSendStickers() {
        return sendStickers;
    }

    public static boolean isCollaborativeWhiteboard() {
        return collaborativeWhiteboard;
    }

    public static boolean isCollaborativeDocument() {
        return collaborativeDocument;
    }

    public static boolean isBlockUser() {
        return blockUser;
    }

    public static boolean isSendTypingIndicators() {
        return sendTypingIndicators;
    }

    public static boolean isViewSharedMedia() {
        return viewSharedMedia;
    }

    public static boolean isShowUserPresence() {
        return showUserPresence;
    }

    public static boolean isAllowPromoteDemoteMembers() {
        return allowPromoteDemoteMembers;
    }

    public static boolean isAllowAddMembers() {
        return allowAddMembers;
    }

    public static boolean isAllowModeratorToDeleteMemberMessages() {
        return allowModeratorToDeleteMemberMessages;
    }

    public static boolean isAllowDeleteGroups() {
        return allowDeleteGroups;
    }

    public static boolean isViewGroupMember() {
        return viewGroupMember;
    }

    public static boolean isJoinOrLeaveGroup() {
        return joinOrLeaveGroup;
    }

    public static boolean isGroupCreate() {
        return groupCreate;
    }

    public static boolean isHideJoinLeaveNotifications() {
        return hideJoinLeaveNotifications;
    }

    public static boolean isEnableSoundForMessages() {
        return enableSoundForMessages;
    }

    public static boolean isEnableSoundForCalls() {
        return enableSoundForCalls;
    }

    public static boolean isGroupNotifications() {
        return hideJoinLeaveNotifications;
    }

    public static boolean isCallNotification() {
        return callNotification;
    }

    public static boolean isSendLiveReaction() {
        return sendLiveReaction;
    }

    public static boolean isSendMessageReaction() {
        return sendMessageReaction;
    }

    public static boolean isAllowMessageTranslation() {
        return allowMessageTranslation;
    }

    public static boolean isShowReplyPrivately() {
        return showReplyPrivately;
    }

    public static boolean isHideDeleteMessage() {
        return hideDeleteMessage;
    }

}
