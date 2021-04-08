package com.cometchat.pro.androiduikit.uikit;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList;
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConversationListTest {
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.cometchat.pro.androiduikit";

    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String STRING_TO_BE_TYPED = "superhero5";

    private UiDevice mDevice;

    private BottomNavigationView bottomNavigation;

    @Rule
    public ActivityTestRule<CometChatUI> activityRule = new ActivityTestRule<>(CometChatUI.class, true, false);

    private View view;

    @Before
    public void setup() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Context context = getInstrumentation().getContext();
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Clear out any previous instances
        context.startActivity(intent);
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
        if (CometChat.getLoggedInUser()==null) {
            // Type text and then press the button.
            Espresso.onView(withId(R.id.superhero1)).perform(ViewActions.click());
        }
        Espresso.onView(withId(R.id.directLaunch)).perform(click());
        bottomNavigation = CometChatUI.getBinding().bottomNavigation;
        Fragment conversationList = new CometChatConversationList();
        CometChatUI.getCometChatUIActivity()
                .getSupportFragmentManager().beginTransaction()
                .replace(CometChatUI.getBinding().frame.getId(),conversationList).commit();
        try {
            Thread.sleep(3000);
            view = conversationList.getView();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void A_checkConversationListNotEmpty() {
        RecyclerView conversationList = (RecyclerView)view.findViewById(R.id.rv_conversation_list);
        RecyclerView.Adapter adapter = conversationList.getAdapter();
        if (adapter.getItemCount()>0) {
            Espresso.onView(Matchers.allOf(
                    withId(R.id.rv_conversation_list),
                    isDescendantOfA(withId(view.getId())),
                    isDisplayed()))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
            Assert.assertTrue("Not Empty",true);
        } else {
            Assert.fail("ConversationList is Empty");
        }
    }

    @Test
    public void B_checkConversationListLastItem() {
        RecyclerView conversationList = (RecyclerView)view.findViewById(R.id.rv_conversation_list);
        RecyclerView.Adapter adapter = conversationList.getAdapter();
        if (adapter.getItemCount()>0) {
            Espresso.onView(Matchers.allOf(
                    withId(R.id.rv_conversation_list),
                    isDescendantOfA(withId(view.getId())),
                    isDisplayed()))
                    .perform(RecyclerViewActions.scrollToPosition(adapter.getItemCount()-1))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(adapter.getItemCount()-1,click()));
        } else {
            Assert.fail("ConversationList is Empty");
        }
    }
}
