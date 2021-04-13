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

    private static final String ERROR_INIT_NOT_CALLED = "ERROR_INIT_NOT_CALLED";
    private static final String ERROR_PASSWORD_MISSING_MESSAGE = "ERROR_PASSWORD_MISSING_MESSAGE";
    private static final String ERROR_LIMIT_EXCEEDED_MESSAGE = "ERROR_LIMIT_EXCEEDED_MESSAGE";
    private static final String ERROR_USER_NOT_LOGGED_IN_MESSAGE = "ERROR_USER_NOT_LOGGED_IN_MESSAGE";
    private static final String ERROR_INVALID_GUID_MESSAGE = "ERROR_INVALID_GUID_MESSAGE";
    private static final String ERROR_INVALID_UID_MESSAGE = "ERROR_INVALID_UID_MESSAGE";
    private static final String ERROR_BLANK_UID_MESSAGE = "ERROR_BLANK_UID_MESSAGE";
    private static final String ERROR_UID_WITH_SPACE_MESSAGE = "ERROR_UID_WITH_SPACE_MESSAGE";
    private static final String ERROR_DEFAULT_MESSAGE = "ERROR_DEFAULT_MESSAGE";
    private static final String ERROR_CALL_NOT_INITIATED_MESSAGE = "ERROR_CALL_NOT_INITIATED_MESSAGE";
    private static final String ERROR_CALL_MESSAGE = "ERROR_CALL_MESSAGE";
    private static final String ERROR_CALL_SESSION_MISMATCH_MESSAGE = "ERROR_CALL_SESSION_MISMATCH_MESSAGE";
    private static final String ERROR_INIT_NOT_CALLED_MESSAGE = "ERROR_INIT_NOT_CALLED_MESSAGE";
    private static final String ERROR_UID_GUID_NOT_SPECIFIED_MESSAGE = "ERROR_UID_GUID_NOT_SPECIFIED_MESSAGE";
    private static final String ERROR_INTERNET_UNAVAILABLE_MESSAGE = "ERROR_INTERNET_UNAVAILABLE_MESSAGE";
    private static final String ERROR_REQUEST_IN_PROGRESS_MESSAGE = "ERROR_REQUEST_IN_PROGRESS_MESSAGE";
    private static final String ERROR_FILTERS_MISSING_MESSAGE = "ERROR_FILTERS_MISSING_MESSAGE";
    private static final String ERROR_BLANK_AUTHTOKEN_MESSAGE = "ERROR_BLANK_AUTHTOKEN_MESSAGE";
    private static final String ERROR_EXTENSION_DISABLED_MESSAGE = "ERROR_EXTENSION_DISABLED_MESSAGE";
    private static final String ERROR_INVALID_MESSAGEID_MESSAGE = "ERROR_INVALID_MESSAGEID_MESSAGE";
    private static final String ERROR_INVALID_MESSAGE_TYPE_MESSAGE = "ERROR_INVALID_MESSAGE_TYPE_MESSAGE";
    private static final String ERROR_LIST_EMPTY_MESSAGE = "ERROR_LIST_EMPTY_MESSAGE";
    private static final String ERROR_UPDATESONLY_WITHOUT_UPDATEDAFTER_MESSAGE = "ERROR_UPDATESONLY_WITHOUT_UPDATEDAFTER_MESSAGE";
    private static final String ERROR_JSON_MESSAGE = "ERROR_JSON_MESSAGE";
    private static final String ERROR_LOGOUT_FAIL_MESSAGE = "ERROR_LOGOUT_FAIL_MESSAGE";
    private static final String ERROR_EMPTY_APPID_MESSAGE = "ERROR_EMPTY_APPID_MESSAGE";
    private static final String ERROR_NON_POSITIVE_LIMIT_MESSSAGE = "ERROR_NON_POSITIVE_LIMIT_MESSSAGE";
    private static final String ERROR_REGION_MISSING_MESSAGE = "ERROR_REGION_MISSING_MESSAGE";
    private static final String ERROR_APP_SETTING_NULL_MESSAGE = "ERROR_APP_SETTING_NULL_MESSAGE";
    private static final String ERROR_API_KEY_NOT_FOUND_MESSAGE = "ERROR_API_KEY_NOT_FOUND_MESSAGE";
    private static final String ERROR_INVALID_SENDING_MESSAGE_TYPE_MESSAGE = "ERROR_INVALID_SENDING_MESSAGE_TYPE_MESSAGE";
    private static final String ERROR_MESSAGE_TEXT_EMPTY_MESSAGE = "ERROR_MESSAGE_TEXT_EMPTY_MESSAGE";
    private static final String ERROR_FILE_OBJECT_INVALID_MESSAGE = "ERROR_MESSAGE_TEXT_EMPTY_MESSAGE";
    private static final String ERROR_FILE_URL_EMPTY_MESSAGE = "ERROR_FILE_URL_EMPTY_MESSAGE";
    private static final String ERROR_EMPTY_CUSTOM_DATA_MESSAGE = "ERROR_EMPTY_CUSTOM_DATA_MESSAGE";
    private static final String ERROR_EMPTY_GROUP_NAME_MESSAGE = "ERROR_EMPTY_GROUP_NAME_MESSAGE";
    private static final String ERROR_EMPTY_GROUP_TYPE_MESSAGE = "ERROR_EMPTY_GROUP_TYPE_MESSAGE";
    private static final String ERROR_LOGIN_IN_PROGRESS_MESSAGE = "ERROR_LOGIN_IN_PROGRESS_MESSAGE";
    private static final String ERROR_INVALID_MESSAGE_MESSAGE = "ERROR_INVALID_MESSAGE_MESSAGE";
    private static final String ERROR_INVALID_GROUP_MESSAGE = "ERROR_INVALID_GROUP_MESSAGE";
    private static final String ERROR_INVALID_CALL_MESSAGE = "ERROR_INVALID_CALL_MESSAGE";
    private static final String ERROR_INVALID_CALL_TYPE_MESSAGE = "ERROR_INVALID_CALL_TYPE_MESSAGE";
    private static final String ERROR_INVALID_RECEIVER_TYPE_MESSAGE = "ERROR_INVALID_RECEIVER_TYPE_MESSAGE";
    private static final String ERROR_INVALID_SESSION_ID_MESSAGE = "ERROR_INVALID_SESSION_ID_MESSAGE";
    private static final String ERROR_ACTIVITY_NULL_MESSAGE = "ERROR_ACTIVITY_NULL_MESSAGE";
    private static final String ERROR_VIEW_NULL_MESSAGE = "ERROR_VIEW_NULL_MESSAGE";
    private static final String ERROR_INVALID_FCM_TOKEN_MESSAGE = "ERROR_INVALID_FCM_TOKEN_MESSAGE";
    private static final String ERROR_INVALID_GROUP_TYPE_MESSAGE = "ERROR_INVALID_FCM_TOKEN_MESSAGE";
    private static final String ERROR_CALL_IN_PROGRESS_MESSAGE = "ERROR_CALL_IN_PROGRESS_MESSAGE";
    private static final String ERROR_INCORRECT_INITIATOR_MESSAGE = "ERROR_INCORRECT_INITIATOR_MESSAGE";
    private static final String ERROR_INVALID_USER_NAME_MESSAGE = "ERROR_INVALID_USER_NAME_MESSAGE";
    private static final String ERROR_INVALID_USER_MESSAGE = "ERROR_INVALID_USER_MESSAGE";
    private static final String ERROR_INVALID_GROUP_NAME_MESSAGE = "ERROR_INVALID_GROUP_NAME_MESSAGE";
    private static final String ERROR_INVALID_TIMESTAMP_MESSAGE = "ERROR_INVALID_TIMESTAMP_MESSAGE";
    private static final String ERROR_INVALID_CATEGORY_MESSAGE = "ERROR_INVALID_CATEGORY_MESSAGE";
    private static final String ERROR_EMPTY_ICON_MESSAGE = "ERROR_EMPTY_ICON_MESSAGE";
    private static final String ERROR_EMPTY_DESCRIPTION_MESSAGE = "ERROR_EMPTY_DESCRIPTION_MESSAGE";
    private static final String ERROR_EMPTY_METADATA_MESSAGE = "ERROR_EMPTY_METADATA_MESSAGE";
    private static final String ERROR_EMPTY_SCOPE_MESSAGE = "ERROR_EMPTY_SCOPE_MESSAGE";
    private static final String ERROR_INVALID_SCOPE_MESSAGE = "ERROR_INVALID_SCOPE_MESSAGE";
    private static final String ERROR_INVALID_CONVERSATION_WITH_MESSAGE = "ERROR_INVALID_CONVERSATION_WITH_MESSAGE";
    private static final String ERROR_INVALID_CONVERSATION_TYPE_MESSAGE = "ERROR_INVALID_CONVERSATION_TYPE_MESSAGE";
    private static final String ERROR_INVALID_MEDIA_MESSAGE_MESSAGE = "ERROR_INVALID_MEDIA_MESSAGE_MESSAGE";
    private static final String ERROR_INVALID_ATTACHMENT_MESSAGE = "ERROR_INVALID_ATTACHMENT_MESSAGE";
    private static final String ERROR_INVALID_FILE_NAME_MESSAGE = "ERROR_INVALID_FILE_NAME_MESSAGE";
    private static final String ERROR_INVALID_FILE_EXTENSION_MESSAGE = "ERROR_INVALID_FILE_EXTENSION_MESSAGE";
    private static final String ERROR_INVALID_FILE_MIME_TYPE_MESSAGE = "ERROR_INVALID_FILE_MIME_TYPE_MESSAGE";
    private static final String ERROR_INVALID_FILE_URL_MESSAGE = "ERROR_INVALID_FILE_URL_MESSAGE";
    private static final String ERROR_CONVERSATION_NOT_FOUND_MESSAGE = "ERROR_CONVERSATION_NOT_FOUND_MESSAGE";
    private static final String ERROR_SETTINGS_NOT_FOUND_MESSAGE = "ERROR_SETTINGS_NOT_FOUND_MESSAGE";
    private static final String ERROR_INVALID_FEATURE_MESSAGE = "ERROR_INVALID_FEATURE_MESSAGE";
    private static final String ERROR_INVALID_EXTENSION_MESSAGE = "ERROR_INVALID_EXTENSION_MESSAGE";
    private static final String ERROR_FEATURE_NOT_FOUND_MESSAGE = "ERROR_FEATURE_NOT_FOUND_MESSAGE";
    private static final String ERROR_EXTENSION_NOT_FOUND_MESSAGE = "ERROR_EXTENSION_NOT_FOUND_MESSAGE";

    private static Context errorContext;
    public static void init(Context context) {
        errorContext = context;
    }

    public static String localized(CometChatException e) {
        if (e.getCode().equalsIgnoreCase(ERROR_INIT_NOT_CALLED) ||
                e.getCode().equalsIgnoreCase(ERROR_INIT_NOT_CALLED_MESSAGE)) {
            return new CometChatException(ERROR_INIT_NOT_CALLED,
                    errorContext.getString(R.string.err_init_not_called)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_EMPTY_APPID_MESSAGE)) {
            return new CometChatException(ERROR_EMPTY_APPID_MESSAGE,
                    errorContext.getString(R.string.err_empty_appid_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_NON_POSITIVE_LIMIT_MESSSAGE)) {
            return new CometChatException(ERROR_NON_POSITIVE_LIMIT_MESSSAGE,
                    errorContext.getString(R.string.err_non_positive_limit_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_REGION_MISSING_MESSAGE)) {
            return new CometChatException(ERROR_REGION_MISSING_MESSAGE,
                    errorContext.getString(R.string.err_region_missing_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_APP_SETTING_NULL_MESSAGE)) {
            return new CometChatException(ERROR_APP_SETTING_NULL_MESSAGE,
                    errorContext.getString(R.string.err_app_settings_null_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_API_KEY_NOT_FOUND_MESSAGE)) {
            return new CometChatException(ERROR_API_KEY_NOT_FOUND_MESSAGE,
                    errorContext.getString(R.string.err_api_key_not_found_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_LOGIN_IN_PROGRESS_MESSAGE)) {
            return new CometChatException(ERROR_LOGIN_IN_PROGRESS_MESSAGE,
                    errorContext.getString(R.string.err_login_in_progress_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_PASSWORD_MISSING_MESSAGE)) {
            return new CometChatException(ERROR_PASSWORD_MISSING_MESSAGE,
                    errorContext.getString(R.string.err_password_missing_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_LIMIT_EXCEEDED_MESSAGE)) {
            return new CometChatException(ERROR_LIMIT_EXCEEDED_MESSAGE,
                    errorContext.getString(R.string.err_limit_exceed_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_USER_NOT_LOGGED_IN_MESSAGE)) {
           return new CometChatException(ERROR_USER_NOT_LOGGED_IN_MESSAGE,
                   errorContext.getString(R.string.err_user_not_logged_in_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_GUID_MESSAGE)) {
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
        } else if (e.getCode().equalsIgnoreCase(ERROR_DEFAULT_MESSAGE)) {
            return new CometChatException(ERROR_DEFAULT_MESSAGE,
                    errorContext.getString(R.string.err_default_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_CALL_NOT_INITIATED_MESSAGE)) {
            return new CometChatException(ERROR_CALL_NOT_INITIATED_MESSAGE,
                    errorContext.getString(R.string.err_call_not_initiated_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_CALL_MESSAGE)) {
            return new CometChatException(ERROR_CALL_MESSAGE,
                    errorContext.getString(R.string.err_call_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_CALL_SESSION_MISMATCH_MESSAGE)) {
            return new CometChatException(ERROR_CALL_SESSION_MISMATCH_MESSAGE,
                    errorContext.getString(R.string.err_call_session_mismatch_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_UID_GUID_NOT_SPECIFIED_MESSAGE)) {
            return new CometChatException(ERROR_UID_GUID_NOT_SPECIFIED_MESSAGE,
                    errorContext.getString(R.string.err_uid_guid_not_specified_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INTERNET_UNAVAILABLE_MESSAGE)) {
            return new CometChatException(ERROR_INTERNET_UNAVAILABLE_MESSAGE,
                    errorContext.getString(R.string.err_internet_unavailable)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_REQUEST_IN_PROGRESS_MESSAGE)) {
            return new CometChatException(ERROR_REQUEST_IN_PROGRESS_MESSAGE,
                    errorContext.getString(R.string.err_request_in_progress_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_FILTERS_MISSING_MESSAGE)) {
            return new CometChatException(ERROR_FILTERS_MISSING_MESSAGE,
                    errorContext.getString(R.string.err_filters_missing_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_BLANK_AUTHTOKEN_MESSAGE)) {
            return new CometChatException(ERROR_BLANK_AUTHTOKEN_MESSAGE,
                    errorContext.getString(R.string.err_blank_auth_token_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_EXTENSION_DISABLED_MESSAGE)) {
            return new CometChatException(ERROR_EXTENSION_DISABLED_MESSAGE,
                    errorContext.getString(R.string.extension_is_not_enabled)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_MESSAGEID_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_MESSAGEID_MESSAGE,
                    errorContext.getString(R.string.err_invalid_messageid_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_MESSAGE_TYPE_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_MESSAGE_TYPE_MESSAGE,
                    errorContext.getString(R.string.err_invalid_message_type_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_LIST_EMPTY_MESSAGE)) {
            return new CometChatException(ERROR_LIST_EMPTY_MESSAGE,
                    errorContext.getString(R.string.err_list_empty_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_UPDATESONLY_WITHOUT_UPDATEDAFTER_MESSAGE)) {
            return new CometChatException(ERROR_UPDATESONLY_WITHOUT_UPDATEDAFTER_MESSAGE,
                    errorContext.getString(R.string.err_updatesonly_without_updatedafter_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_JSON_MESSAGE)) {
            return new CometChatException(ERROR_JSON_MESSAGE,
                    errorContext.getString(R.string.err_json_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_LOGOUT_FAIL_MESSAGE)) {
            return new CometChatException(ERROR_LOGOUT_FAIL_MESSAGE,
                    errorContext.getString(R.string.err_logout_fail_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_SENDING_MESSAGE_TYPE_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_SENDING_MESSAGE_TYPE_MESSAGE,
                    errorContext.getString(R.string.err_invalid_sending_message_type_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_MESSAGE_TEXT_EMPTY_MESSAGE)) {
            return new CometChatException(ERROR_MESSAGE_TEXT_EMPTY_MESSAGE,
                    errorContext.getString(R.string.err_message_text_empty_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_FILE_OBJECT_INVALID_MESSAGE)) {
            return new CometChatException(ERROR_FILE_OBJECT_INVALID_MESSAGE,
                    errorContext.getString(R.string.err_file_object_invalid_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_FILE_URL_EMPTY_MESSAGE)) {
            return new CometChatException(ERROR_FILE_URL_EMPTY_MESSAGE,
                    errorContext.getString(R.string.err_file_url_empty_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_EMPTY_CUSTOM_DATA_MESSAGE)) {
            return new CometChatException(ERROR_EMPTY_CUSTOM_DATA_MESSAGE,
                    errorContext.getString(R.string.err_empty_custom_data_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_EMPTY_GROUP_NAME_MESSAGE)) {
            return new CometChatException(ERROR_EMPTY_GROUP_NAME_MESSAGE,
                    errorContext.getString(R.string.err_empty_group_name_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_EMPTY_GROUP_TYPE_MESSAGE)) {
            return new CometChatException(ERROR_EMPTY_GROUP_TYPE_MESSAGE,
                    errorContext.getString(R.string.err_empty_group_type_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_MESSAGE_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_MESSAGE_MESSAGE,
                    errorContext.getString(R.string.err_invalid_message_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_GROUP_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_GROUP_MESSAGE,
                    errorContext.getString(R.string.err_invalid_group_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_CALL_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_CALL_MESSAGE,
                    errorContext.getString(R.string.err_invalid_call_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_CALL_TYPE_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_CALL_TYPE_MESSAGE,
                    errorContext.getString(R.string.err_invalid_call_type_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_RECEIVER_TYPE_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_RECEIVER_TYPE_MESSAGE,
                    errorContext.getString(R.string.err_invalid_receiver_type_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_SESSION_ID_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_SESSION_ID_MESSAGE,
                    errorContext.getString(R.string.err_invalid_session_id_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_ACTIVITY_NULL_MESSAGE)) {
            return new CometChatException(ERROR_ACTIVITY_NULL_MESSAGE,
                    errorContext.getString(R.string.err_activity_null_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_VIEW_NULL_MESSAGE)) {
            return new CometChatException(ERROR_VIEW_NULL_MESSAGE,
                    errorContext.getString(R.string.err_view_null_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_FCM_TOKEN_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_FCM_TOKEN_MESSAGE,
                    errorContext.getString(R.string.err_invalid_fcm_token_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_GROUP_TYPE_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_GROUP_TYPE_MESSAGE,
                    errorContext.getString(R.string.err_invalid_group_type_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_CALL_IN_PROGRESS_MESSAGE)) {
            return new CometChatException(ERROR_CALL_IN_PROGRESS_MESSAGE,
                    errorContext.getString(R.string.err_call_in_progress_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INCORRECT_INITIATOR_MESSAGE)) {
            return new CometChatException(ERROR_INCORRECT_INITIATOR_MESSAGE,
                    errorContext.getString(R.string.err_incorrect_initiator_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_USER_NAME_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_USER_NAME_MESSAGE,
                    errorContext.getString(R.string.err_invalid_user_name_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_USER_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_USER_MESSAGE,
                    errorContext.getString(R.string.err_invalid_user_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_GROUP_NAME_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_GROUP_NAME_MESSAGE,
                    errorContext.getString(R.string.err_invalid_group_name_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_TIMESTAMP_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_TIMESTAMP_MESSAGE,
                    errorContext.getString(R.string.err_invalid_timestamp_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_CATEGORY_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_CATEGORY_MESSAGE,
                    errorContext.getString(R.string.err_invalid_category_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_EMPTY_ICON_MESSAGE)) {
            return new CometChatException(ERROR_EMPTY_ICON_MESSAGE,
                    errorContext.getString(R.string.err_empty_icon_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_EMPTY_DESCRIPTION_MESSAGE)) {
            return new CometChatException(ERROR_EMPTY_DESCRIPTION_MESSAGE,
                    errorContext.getString(R.string.err_empty_description_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_EMPTY_METADATA_MESSAGE)) {
            return new CometChatException(ERROR_EMPTY_METADATA_MESSAGE,
                    errorContext.getString(R.string.err_empty_metadata_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_EMPTY_SCOPE_MESSAGE)) {
            return new CometChatException(ERROR_EMPTY_SCOPE_MESSAGE,
                    errorContext.getString(R.string.err_empty_scope_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_SCOPE_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_SCOPE_MESSAGE,
                    errorContext.getString(R.string.err_invalid_scope_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_CONVERSATION_WITH_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_CONVERSATION_WITH_MESSAGE,
                    errorContext.getString(R.string.err_invalid_conversation_with_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_CONVERSATION_TYPE_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_CONVERSATION_TYPE_MESSAGE,
                    errorContext.getString(R.string.err_invalid_conversation_type_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_MEDIA_MESSAGE_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_MEDIA_MESSAGE_MESSAGE,
                    errorContext.getString(R.string.err_invalid_media_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_ATTACHMENT_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_ATTACHMENT_MESSAGE,
                    errorContext.getString(R.string.err_invalid_attachment_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_FILE_NAME_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_FILE_NAME_MESSAGE,
                    errorContext.getString(R.string.err_invalid_file_name_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_FILE_EXTENSION_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_FILE_EXTENSION_MESSAGE,
                    errorContext.getString(R.string.err_invalid_file_extension_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_FILE_MIME_TYPE_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_FILE_MIME_TYPE_MESSAGE,
                    errorContext.getString(R.string.err_invalid_file_mime_type_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_FILE_URL_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_FILE_URL_MESSAGE,
                    errorContext.getString(R.string.err_invalid_file_url_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_CONVERSATION_NOT_FOUND_MESSAGE)) {
            return new CometChatException(ERROR_CONVERSATION_NOT_FOUND_MESSAGE,
                    errorContext.getString(R.string.err_conversation_not_found_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_SETTINGS_NOT_FOUND_MESSAGE)) {
            return new CometChatException(ERROR_SETTINGS_NOT_FOUND_MESSAGE,
                    errorContext.getString(R.string.err_settings_not_found_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_FEATURE_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_FEATURE_MESSAGE,
                    errorContext.getString(R.string.err_invalid_feature_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_INVALID_EXTENSION_MESSAGE)) {
            return new CometChatException(ERROR_INVALID_EXTENSION_MESSAGE,
                    errorContext.getString(R.string.err_invalid_extension_message)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_FEATURE_NOT_FOUND_MESSAGE)) {
            return new CometChatException(ERROR_FEATURE_NOT_FOUND_MESSAGE,
                    errorContext.getString(R.string.err_feature_not_found)).getMessage();
        } else if (e.getCode().equalsIgnoreCase(ERROR_EXTENSION_NOT_FOUND_MESSAGE)) {
            return new CometChatException(ERROR_EXTENSION_NOT_FOUND_MESSAGE,
                    errorContext.getString(R.string.extension_not_found)).getMessage();
        }
//        else if (e.getCode().equalsIgnoreCase())
        else {
            return e.getMessage();
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
