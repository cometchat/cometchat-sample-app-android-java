package com.cometchat.pro.androiduikit.uikit;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import com.cometchat.pro.androiduikit.MainActivity;
import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.androiduikit.utils.RecyclerViewAssertion;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.uikit.databinding.ActivityCometchatUnifiedBinding;
import com.cometchat.pro.uikit.ui_components.calls.call_list.CometChatCallList;
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList;
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI;
import com.cometchat.pro.uikit.ui_components.groups.group_list.CometChatGroupList;
import com.cometchat.pro.uikit.ui_components.userprofile.CometChatUserProfile;
import com.cometchat.pro.uikit.ui_components.users.user_list.CometChatUserList;
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
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.cometchat.pro.uikit.ui_components.calls.call_manager.CometChatStartCallActivity.activity;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CometChatUITest {
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.cometchat.pro.androiduikit";

    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String STRING_TO_BE_TYPED = "superhero5";

    private UiDevice mDevice;

    private BottomNavigationView bottomNavigation;

    @Rule
    public ActivityTestRule<CometChatUI> activityRule = new ActivityTestRule<>(CometChatUI.class, true, false);


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
    }

    @Test
    public void A_checkConversations() {
        try {
            Thread.sleep(3000);
            BottomNavigationView.OnNavigationItemSelectedListener mockedListener =
                    mock(BottomNavigationView.OnNavigationItemSelectedListener.class);
            bottomNavigation.setOnNavigationItemSelectedListener(mockedListener);

            // Make the listener return true to allow selecting the item.
            when(mockedListener.onNavigationItemSelected(any(MenuItem.class))).thenReturn(true);
            Espresso.onView(
                    Matchers.allOf(
                            ViewMatchers.withId(R.id.menu_conversation),
                            isDescendantOfA(withId(R.id.bottom_navigation)),
                            isDisplayed()))
                    .perform(click());
            // Verify our listener has been notified of the click
            verify(mockedListener, times(1))
                    .onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_conversation));
            // Verify the item is now selected
            assertTrue(bottomNavigation.getMenu().findItem(R.id.menu_conversation).isChecked());
            Fragment conversationList = new CometChatConversationList();
            CometChatUI.getCometChatUIActivity()
                    .getSupportFragmentManager().beginTransaction()
                    .replace(CometChatUI.getBinding().frame.getId(),conversationList).commit();
        }catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void B_checkCalls() {
        try {
            Thread.sleep(3000);
            BottomNavigationView.OnNavigationItemSelectedListener mockedListener =
                    mock(BottomNavigationView.OnNavigationItemSelectedListener.class);
            bottomNavigation.setOnNavigationItemSelectedListener(mockedListener);

            // Make the listener return true to allow selecting the item.
            when(mockedListener.onNavigationItemSelected(any(MenuItem.class))).thenReturn(true);
            Espresso.onView(
                    Matchers.allOf(
                            ViewMatchers.withId(R.id.menu_call),
                            isDescendantOfA(withId(R.id.bottom_navigation)),
                            isDisplayed()))
                    .perform(click());
            // Verify our listener has been notified of the click
            verify(mockedListener, times(1))
                    .onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_call));
            // Verify the item is now selected
            assertTrue(bottomNavigation.getMenu().findItem(R.id.menu_call).isChecked());
            Fragment callList = new CometChatCallList();
            CometChatUI.getCometChatUIActivity()
                    .getSupportFragmentManager().beginTransaction()
                    .replace(CometChatUI.getBinding().frame.getId(),callList).commit();
            Thread.sleep(2500);

        }catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void C_checkUsers() {
        try {
            Thread.sleep(3000);
            BottomNavigationView.OnNavigationItemSelectedListener mockedListener =
                    mock(BottomNavigationView.OnNavigationItemSelectedListener.class);
            bottomNavigation.setOnNavigationItemSelectedListener(mockedListener);

            // Make the listener return true to allow selecting the item.
            when(mockedListener.onNavigationItemSelected(any(MenuItem.class))).thenReturn(true);
            Espresso.onView(
                    Matchers.allOf(
                            ViewMatchers.withId(R.id.menu_users),
                            isDescendantOfA(withId(R.id.bottom_navigation)),
                            isDisplayed()))
                    .perform(click());
            // Verify our listener has been notified of the click
            verify(mockedListener, times(1))
                    .onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_users));
            // Verify the item is now selected
            assertTrue(bottomNavigation.getMenu().findItem(R.id.menu_users).isChecked());

            Fragment usersList = new CometChatUserList();
            CometChatUI.getCometChatUIActivity()
                    .getSupportFragmentManager().beginTransaction()
                    .replace(CometChatUI.getBinding().frame.getId(),usersList).commit();
            Thread.sleep(2500);

        }catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void D_checkGroups() {
        try {
            Thread.sleep(5000);
            BottomNavigationView.OnNavigationItemSelectedListener mockedListener =
                    mock(BottomNavigationView.OnNavigationItemSelectedListener.class);
            bottomNavigation.setOnNavigationItemSelectedListener(mockedListener);

            // Make the listener return true to allow selecting the item.
            when(mockedListener.onNavigationItemSelected(any(MenuItem.class))).thenReturn(true);
            Espresso.onView(
                    Matchers.allOf(
                            ViewMatchers.withId(R.id.menu_group),
                            isDescendantOfA(withId(R.id.bottom_navigation)),
                            isDisplayed()))
                    .perform(click());
            // Verify our listener has been notified of the click
            verify(mockedListener, times(1))
                    .onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_group));
            // Verify the item is now selected
            assertTrue(bottomNavigation.getMenu().findItem(R.id.menu_group).isChecked());

            Fragment groupList = new CometChatGroupList();
            CometChatUI.getCometChatUIActivity()
                    .getSupportFragmentManager().beginTransaction()
                    .replace(CometChatUI.getBinding().frame.getId(),groupList).commit();
            Thread.sleep(2500);

        }catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void E_checkUserProfile() {
        try {
            Thread.sleep(5000);
            BottomNavigationView.OnNavigationItemSelectedListener mockedListener =
                    mock(BottomNavigationView.OnNavigationItemSelectedListener.class);
            bottomNavigation.setOnNavigationItemSelectedListener(mockedListener);

            // Make the listener return true to allow selecting the item.
            when(mockedListener.onNavigationItemSelected(any(MenuItem.class))).thenReturn(true);
            Espresso.onView(
                    Matchers.allOf(
                            ViewMatchers.withId(R.id.menu_more),
                            isDescendantOfA(withId(R.id.bottom_navigation)),
                            isDisplayed()))
                    .perform(click());
            // Verify our listener has been notified of the click
            verify(mockedListener, times(1))
                    .onNavigationItemSelected(bottomNavigation.getMenu().findItem(R.id.menu_more));
            // Verify the item is now selected
            assertTrue(bottomNavigation.getMenu().findItem(R.id.menu_more).isChecked());

            Fragment profileScreen = new CometChatUserProfile();
            CometChatUI.getCometChatUIActivity()
                    .getSupportFragmentManager().beginTransaction()
                    .replace(CometChatUI.getBinding().frame.getId(),profileScreen).commit();
            Thread.sleep(2500);

        }catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }



}
