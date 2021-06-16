package com.cometchat.pro.uikit.ui_settings;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;

public class FeatureRestriction {
    public static boolean isGroupActionMessagesEnabled() {
        return UIKitSettings.isGroupNotifications();
    }

    public static boolean isCallActionMessageEnabled() {
        return UIKitSettings.isCallNotification();
    }

    public static String getColor() {
        return UIKitSettings.UIcolor;
    }


    public static void isUserListEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_users_list_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.users(false);
                onSuccessListener.onSuccess(UIKitSettings.isUsers());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }


    public static void isGroupListEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isGroups());
    }

    public static void isConversationListEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isConversations());
    }

    public static void isCallListEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isCalls());
    }

    public static void isUserSettingsEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isUserSettings());
    }

    public static void isOneOnOneChatEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_one_on_one_enabled,
                new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.sendMessageInOneOneOne(false);
                onSuccessListener.onSuccess(UIKitSettings.isSendMessageInOneOneOne());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isGroupChatEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_groups_enabled,
                new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.sendMessageInGroup(false);
                onSuccessListener.onSuccess(UIKitSettings.isSendMessageInGroup());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isDeliveryReceiptsEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_messages_receipts_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.showReadDeliveryReceipts(false);
                onSuccessListener.onSuccess(UIKitSettings.isShowReadDeliveryReceipts());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isGifsEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isSendGifs());
    }
    public static void isLargeSizeEmojisEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isSendEmojisLargeSize());
    }

    public static void isEmojisEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.emojis_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.sendEmojis(false);
                onSuccessListener.onSuccess(UIKitSettings.isSendEmojis());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isVoiceNotesEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_voice_notes_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.sendVoiceNotes(false);
                onSuccessListener.onSuccess(UIKitSettings.isSendVoiceNotes());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isFilesEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_messages_media_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.sendFiles(false);
                onSuccessListener.onSuccess(UIKitSettings.isSendFiles());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isPollsEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.polls_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.polls))
                    UIKitSettings.sendPolls(false);
                else {
                   UIKitSettings.sendPolls(true);
                }
                onSuccessListener.onSuccess(UIKitSettings.isSendPolls());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isStickersEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.stickers_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.stickers))
                    UIKitSettings.sendStickers(false);
                else {
                    UIKitSettings.sendStickers(true);
                }
                onSuccessListener.onSuccess(UIKitSettings.isSendStickers());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isHideDeleteMessageEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isHideDeleteMessage());
    }
    public static void isPhotosVideoEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_messages_media_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.sendPhotosVideo(false);
                onSuccessListener.onSuccess(UIKitSettings.isSendPhotosVideo());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isThreadedMessagesEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_messages_replies_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.threadedChats(false);
                onSuccessListener.onSuccess(UIKitSettings.isThreadedChats());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isMessageRepliesEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_messages_replies_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.replyingToMessage(false);
                onSuccessListener.onSuccess(UIKitSettings.isReplyingToMessage());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isShareCopyForwardMessageEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isShareCopyForwardMessage());
    }

    public static void isDeleteMessageEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isDeleteMessage());
    }

    public static void isEditMessageEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isEditMessage());
    }

    public static void isLocationSharingEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_messages_custom_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.shareLocation(false);
                onSuccessListener.onSuccess(UIKitSettings.isShareLocation());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isBlockUserEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.moderation_users_block_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.blockUser(false);
                onSuccessListener.onSuccess(UIKitSettings.isBlockUser());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isTypingIndicatorsEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_typing_indicator_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.sendTypingIndicators(false);
                onSuccessListener.onSuccess(UIKitSettings.isSendTypingIndicators());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isSharedMediaEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isViewSharedMedia());
    }

    public static void isUserPresenceEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_users_presence_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.showUserPresence(false);
                onSuccessListener.onSuccess(UIKitSettings.isShowUserPresence());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isChangingMemberScopeEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isAllowPromoteDemoteMembers());
    }

//    public static boolean isAllowBanKickMembers() {
//        return allowBanKickMembers;
//    }

    public static void isBanningGroupMembersEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.moderation_groups_ban_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.banMember(false);
                onSuccessListener.onSuccess(UIKitSettings.isBanMember());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isKickingGroupMembersEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.moderation_groups_kick_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.kickMember(false);
                onSuccessListener.onSuccess(UIKitSettings.isKickMember());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isAddingGroupMembersEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isAllowAddMembers());
    }

    public static void isDeleteMemberMessageEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.moderation_groups_moderators_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.allowModeratorToDeleteMemberMessages(false);
                onSuccessListener.onSuccess(UIKitSettings.isAllowModeratorToDeleteMemberMessages());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isGroupDeletionEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isAllowDeleteGroups());
    }

    public static void isViewGroupMemberInGroupDetails(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isViewGroupMember());
    }

    public static void isJoinOrLeaveGroupEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isJoinOrLeaveGroup());
    }

    public static void isGroupCreationEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isGroupCreate());
    }

    public static void isGroupActionMessagesEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isHideJoinLeaveNotifications());
    }

    public static void isOneOnOneVideoCallEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.call_one_on_one_video_enabled,
                new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.userVideoCall(false);
                onSuccessListener.onSuccess(UIKitSettings.isUserVideoCall());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isOneOnOneAudioCallEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.call_one_on_one_audio_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.userAudioCall(false);
                onSuccessListener.onSuccess(UIKitSettings.isUserAudioCall());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isGroupVideoCallEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.call_groups_video_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.groupVideoCall(false);
                onSuccessListener.onSuccess(UIKitSettings.isGroupVideoCall());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isGroupAudioCallEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.call_groups_audio_enabled,
                new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.groupAudioCall(false);
                onSuccessListener.onSuccess(UIKitSettings.isGroupAudioCall());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isMessagesSoundEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isEnableSoundForMessages());
    }

    public static void isCallsSoundEnabled(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isEnableSoundForCalls());
    }

    public static int getEmailColor() {
        return UIKitSettings.emailColor;
    }

    public static int getPhoneColor() {
        return UIKitSettings.phoneColor;
    }

    public static int getUrlColor() {
        return UIKitSettings.urlColor;
    }

    public static void isLiveReactionEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.live_reactions_enabled,
                new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.sendLiveReaction(false);
                onSuccessListener.onSuccess(UIKitSettings.isSendLiveReaction());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isReactionEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.reactions_enabled,
                new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.reactions))
                    UIKitSettings.sendMessageReaction(false);
                else {
                    UIKitSettings.sendMessageReaction(true);
                }
                onSuccessListener.onSuccess(UIKitSettings.isSendMessageReaction());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isCollaborativeWhiteBoardEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.collaboration_whiteboard_enabled,
                new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.whiteboard))
                    UIKitSettings.collaborativeWhiteBoard(false);
                onSuccessListener.onSuccess(UIKitSettings.isCollaborativeWhiteboard());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isCollaborativeDocumentEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.collaboration_document_enabled,
                new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.document))
                    UIKitSettings.collaborativeDocument(false);
                else
                    UIKitSettings.collaborativeDocument(true);
                onSuccessListener.onSuccess(UIKitSettings.isCollaborativeDocument());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isMessageTranslationEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.message_translation_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.messageTranslation))
                    UIKitSettings.allowMessageTranslation(false);
                else
                    UIKitSettings.allowMessageTranslation(true);
                onSuccessListener.onSuccess(UIKitSettings.isAllowMessageTranslation());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isShowReplyPrivately(OnSuccessListener onSuccessListener) {
        onSuccessListener.onSuccess(UIKitSettings.isShowReplyPrivately());
    }

    public static void isPublicGroupEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_groups_public_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.publicGroup(false);
                onSuccessListener.onSuccess(UIKitSettings.isPublicGroup());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isPasswordGroupEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_groups_password_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.passwordGroup(false);
                onSuccessListener.onSuccess(UIKitSettings.isPasswordGroup());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isPrivateGroupEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_groups_private_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.privateGroup(false);
                onSuccessListener.onSuccess(UIKitSettings.isPrivateGroup());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isUnreadCountEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_messages_unread_count_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.unreadCount(false);
                onSuccessListener.onSuccess(UIKitSettings.isUnreadCount());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isSmartRepliesEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.smart_replies_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.smartReply))
                    UIKitSettings.smartReplies(false);
                onSuccessListener.onSuccess(UIKitSettings.isSmartReplies());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isUserSearchEnabled(OnSuccessListener onSuccessListener) {
       CometChat.isFeatureEnabled(Feature.chat_users_search_enabled, new CometChat.CallbackListener<Boolean>() {
           @Override
           public void onSuccess(Boolean aBoolean) {
               if (!aBoolean)
                   UIKitSettings.searchUser(false);
               onSuccessListener.onSuccess(UIKitSettings.isSearchUser());

           }

           @Override
           public void onError(CometChatException e) {
               e.printStackTrace();
           }
       });
    }

    public static void isGroupSearchEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_groups_search_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.searchGroup(false);
                onSuccessListener.onSuccess(UIKitSettings.isSearchGroup());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isMessageSearchEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_messages_search_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.searchMessage(false);
                onSuccessListener.onSuccess(UIKitSettings.isSearchMessage());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isCallRecordingEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.call_recording_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.callRecording(false);
                onSuccessListener.onSuccess(UIKitSettings.isCallRecording());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isCallLiveStreamingEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.call_live_streaming_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.callLiveStreaming(false);
                onSuccessListener.onSuccess(UIKitSettings.isCallLiveStreaming());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isCallTranscriptEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.call_transcript_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.callTranscription(false);
                onSuccessListener.onSuccess(UIKitSettings.isCallTranscription());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isThumbnailGenerationEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.thumbnail_generation_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.thumbnailGeneration))
                    UIKitSettings.thumbnailGeneration(false);
                else
                    UIKitSettings.thumbnailGeneration(true);
                onSuccessListener.onSuccess(UIKitSettings.isThumbnailGeneration());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isLinkPreviewEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.link_preview_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.linkPreview))
                    UIKitSettings.linkPreview(false);
                else
                    UIKitSettings.linkPreview(true);
                onSuccessListener.onSuccess(UIKitSettings.isLinkPreview());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isSaveMessagesEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.messages_saved_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.saveMessage))
                    UIKitSettings.saveMessage(false);
                else
                    UIKitSettings.saveMessage(true);
                onSuccessListener.onSuccess(UIKitSettings.isSaveMessage());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isPinMessageEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.messages_pinned_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.pinMessage))
                    UIKitSettings.pinMessage(false);
                else
                    UIKitSettings.pinMessage(true);
                onSuccessListener.onSuccess(UIKitSettings.isPinMessage());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isRichMediaPreviewEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.rich_media_preview_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.richMedia))
                    UIKitSettings.richMedia(false);
                else
                    UIKitSettings.pinMessage(true);
                onSuccessListener.onSuccess(UIKitSettings.isRichMedia());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isVoiceTranscriptionEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.voice_transcription_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.voiceTranscription))
                    UIKitSettings.voiceTranscription(false);
                else
                    UIKitSettings.voiceTranscription(true);
                onSuccessListener.onSuccess(UIKitSettings.isVoiceTranscription());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isMentionsEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.mentions_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.mentions))
                    UIKitSettings.mentions(false);
                else
                    UIKitSettings.mentions(true);
                onSuccessListener.onSuccess(UIKitSettings.isMentions());
            }

            @Override
            public void onError(CometChatException e) {
            e.printStackTrace();
            }
        });
    }

    public static void isProfanityFilterEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.moderation_profanity_filter_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.profanityFilter))
                    UIKitSettings.profanityFilter(false);
                else
                    UIKitSettings.profanityFilter(true);
                onSuccessListener.onSuccess(UIKitSettings.isProfanityFilter());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isImageModerationEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.moderation_image_moderation_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.imageModeration))
                    UIKitSettings.imageModeration(false);
                else
                    UIKitSettings.imageModeration(true);

                onSuccessListener.onSuccess(UIKitSettings.isImageModeration());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isDataMaskingEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.moderation_data_masking_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.dataMasking))
                    UIKitSettings.dataMasking(false);
                else
                    UIKitSettings.dataMasking(true);

                onSuccessListener.onSuccess(UIKitSettings.isDataMasking());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isMalwareScannerEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.moderation_malware_scanner_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.malwareScanner))
                    UIKitSettings.malwareScanner(false);
                onSuccessListener.onSuccess(UIKitSettings.isMalwareScanner());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isSentimentAnalysisEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.moderation_sentiment_analysis_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean || !CometChat.isExtensionEnabled(Feature.Extension.sentimentalAnalysis))
                    UIKitSettings.sentimentAnalysis(false);
                else
                    UIKitSettings.sentimentAnalysis(true);

                onSuccessListener.onSuccess(UIKitSettings.isSentimentAnalysis());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public static void isMessageHistoryEnabled(OnSuccessListener onSuccessListener) {
        CometChat.isFeatureEnabled(Feature.chat_messages_history_enabled, new CometChat.CallbackListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (!aBoolean)
                    UIKitSettings.messageHistory(false);
                onSuccessListener.onSuccess(UIKitSettings.isMessageHistory());
            }

            @Override
            public void onError(CometChatException e) {
                e.printStackTrace();
            }
        });
    }

    public interface OnSuccessListener {
        void onSuccess(Boolean booleanVal);
    }
}
