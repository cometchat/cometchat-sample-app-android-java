package com.cometchat.pro.uikit.ui_settings.enums;

public enum GroupMode {
    PUBLIC_GROUP("public_groups"),
    PASSWORD_GROUP("password_protected_groups"),
    ALL_GROUP("public_and_password_protected_groups");

    private String label;
    GroupMode(String label) {
        this.label = label;
    }
}
