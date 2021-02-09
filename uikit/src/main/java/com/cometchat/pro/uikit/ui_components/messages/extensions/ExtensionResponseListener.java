package com.cometchat.pro.uikit.ui_components.messages.extensions;

import com.cometchat.pro.exceptions.CometChatException;

public abstract class ExtensionResponseListener<T> {

    public abstract void OnResponseSuccess(T var);

    public abstract void OnResponseFailed(CometChatException e);
}
