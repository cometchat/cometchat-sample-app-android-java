package com.cometchat.pro.uikit.ui_settings.enums;

public enum ConversationMode {
    ALL_CHATS("all_chats"),
    GROUP("groups"),
    USER("users");

    private String label;
    ConversationMode(String label) {
        this.label = label;
    }
}
