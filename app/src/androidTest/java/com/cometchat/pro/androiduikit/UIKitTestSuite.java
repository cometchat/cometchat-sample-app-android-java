package com.cometchat.pro.androiduikit;

import com.cometchat.pro.androiduikit.app.ComponentActivityTest;
import com.cometchat.pro.androiduikit.app.MainActivityTest;
import com.cometchat.pro.androiduikit.app.SelectActivityTest;
import com.cometchat.pro.androiduikit.uikit.CometChatUITest;
import com.cometchat.pro.androiduikit.uikit.ConversationListTest;
import com.cometchat.pro.androiduikit.uikit.GroupListTest;
import com.cometchat.pro.androiduikit.uikit.UsersListTest;
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
                CometChatUITest.class,
                ConversationListTest.class,
                UsersListTest.class,
                GroupListTest.class
        })

public class UIKitTestSuite {}
