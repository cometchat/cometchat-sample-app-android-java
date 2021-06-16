package com.cometchat.pro.uikit.ui_resources.utils;

import android.content.Context;

import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.uikit.R;

public class CometChatError {


    private static final String ERR_EXTENSION_NOT_FOUND = "ERR_EXTENSION_NOT_FOUND";
    private static final String ERR_EXTENSION_NOT_ENABLED = "ERR_EXTENSION_NOT_ENABLED";
    private static final String ERR_INTERNAL_SERVER_ERROR = "ERR_INTERNAL_SERVER_ERROR";
    private static final String ERR_NO_QUESTION = "ERR_NO_QUESTION";
    private static final String ERR_NO_RECEIVER = "ERR_NO_RECEIVER";
    private static final String ERR_NO_RECEIVER_TYPE = "ERR_NO_RECEIVER_TYPE";
    private static final String ERR_NOT_POLL_CREATOR = "ERR_NOT_POLL_CREATOR";
    private static final String ERR_NO_POLL_ID = "ERR_NO_POLL_ID";
    private static final String ERR_NO_OPTIONS = "ERR_NO_OPTIONS";
    private static final String ERR_POLL_NOT_FOUND = "ERR_POLL_NOT_FOUND";

    private static final String ERROR_PASSWORD_MISSING_MESSAGE = "ERROR_PASSWORD_MISSING_MESSAGE";
    private static final String ERROR_INVALID_GUID_MESSAGE = "ERROR_INVALID_GUID_MESSAGE";
    private static final String ERROR_INVALID_UID_MESSAGE = "ERROR_INVALID_UID_MESSAGE";
    private static final String ERROR_BLANK_UID_MESSAGE = "ERROR_BLANK_UID_MESSAGE";
    private static final String ERROR_UID_WITH_SPACE_MESSAGE = "ERROR_UID_WITH_SPACE_MESSAGE";
    private static final String ERROR_DEFAULT_MESSAGE = "ERROR_DEFAULT_MESSAGE";
    private static final String ERROR_UID_GUID_NOT_SPECIFIED_MESSAGE = "ERROR_UID_GUID_NOT_SPECIFIED_MESSAGE";
    private static final String ERROR_INTERNET_UNAVAILABLE_MESSAGE = "ERROR_INTERNET_UNAVAILABLE_MESSAGE";
    private static final String ERROR_REQUEST_IN_PROGRESS_MESSAGE = "ERROR_REQUEST_IN_PROGRESS_MESSAGE";
    private static final String ERROR_EMPTY_GROUP_NAME_MESSAGE = "ERROR_EMPTY_GROUP_NAME_MESSAGE";

    private static Context errorContext;
    public static void init(Context context) {
        errorContext = context;
    }

    public static String localized(CometChatException e) {
        if (e.getCode().equalsIgnoreCase(ERROR_PASSWORD_MISSING_MESSAGE)) {
            return new CometChatException(ERROR_PASSWORD_MISSING_MESSAGE,
                    errorContext.getString(R.string.err_password_missing_message)).getMessage();
        }
        else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_GUID_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_GUID_MESSAGE,
                    errorContext.getString(R.string.err_invalid_guid_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_UID_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_UID_MESSAGE,
                    errorContext.getString(R.string.err_invalid_uid_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_BLANK_UID_MESSAGE)) {
            return new CometChatException(ERROR_BLANK_UID_MESSAGE,
                    errorContext.getString(R.string.err_blank_uid_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_UID_WITH_SPACE_MESSAGE)) {
            return new CometChatException(ERROR_UID_WITH_SPACE_MESSAGE,
                    errorContext.getString(R.string.err_uid_with_space_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INTERNET_UNAVAILABLE_MESSAGE)) {
            return new CometChatException(ERROR_INTERNET_UNAVAILABLE_MESSAGE,
                    errorContext.getString(R.string.err_internet_unavailable)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_EMPTY_GROUP_NAME_MESSAGE)) {
            return new CometChatException(ERROR_EMPTY_GROUP_NAME_MESSAGE,
                    errorContext.getString(R.string.err_empty_group_name_message)).getMessage();
        }
        else {
            return new CometChatException(ERROR_DEFAULT_MESSAGE,
                    errorContext.getString(R.string.err_default_message)).getMessage();
        }
    }
    public static class Extension {
        public static String localized(CometChatException e,String extensionId) {
            if (e.getCode().equalsIgnoreCase(ERR_EXTENSION_NOT_FOUND)) {
                return new CometChatException(ERR_EXTENSION_NOT_FOUND,
                       extensionId+":"+errorContext.getString(R.string.extension_not_found)).getMessage();
            } else if (e.getCode().equalsIgnoreCase(ERR_EXTENSION_NOT_ENABLED)) {
                return new CometChatException(ERR_EXTENSION_NOT_ENABLED,
                        errorContext.getString(R.string.extension_is_not_enabled)).getMessage();
            } else if (e.getCode().equalsIgnoreCase(ERR_INTERNAL_SERVER_ERROR)) {
                return new CometChatException(ERR_INTERNAL_SERVER_ERROR,
                        errorContext.getString(R.string.server_error)).getMessage();
            } else if (e.getCode().equalsIgnoreCase(ERR_NO_QUESTION)) {
                return new CometChatException(ERR_NO_QUESTION,
                        errorContext.getString(R.string.err_no_question)).getMessage();
            } else if (e.getCode().equalsIgnoreCase(ERR_NO_RECEIVER)) {
                return new CometChatException(ERR_NO_RECEIVER,
                        errorContext.getString(R.string.err_no_receiver)).getMessage();
            } else if (e.getCode().equalsIgnoreCase(ERR_NO_RECEIVER_TYPE)) {
                return new CometChatException(ERR_NO_RECEIVER_TYPE,
                        errorContext.getString(R.string.err_no_receiver_type)).getMessage();
            } else if (e.getCode().equalsIgnoreCase(ERR_NO_OPTIONS)) {
                return new CometChatException(ERR_NO_OPTIONS,
                        errorContext.getString(R.string.err_no_options)).getMessage();
            } else if (e.getCode().equalsIgnoreCase(ERR_POLL_NOT_FOUND)) {
                return new CometChatException(ERR_POLL_NOT_FOUND,
                        errorContext.getString(R.string.err_poll_not_found)).getMessage();
            } else if (e.getCode().equalsIgnoreCase(ERR_NO_POLL_ID)) {
                return new CometChatException(ERR_NO_POLL_ID,
                        errorContext.getString(R.string.err_no_poll_id)).getMessage();
            } else if (e.getCode().equalsIgnoreCase(ERR_NOT_POLL_CREATOR)) {
                return new CometChatException(ERR_NOT_POLL_CREATOR,
                        errorContext.getString(R.string.err_not_poll_creator)).getMessage();
            } else {
                return e.getMessage();
            }
        }
    }
}
