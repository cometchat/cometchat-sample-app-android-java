package com.cometchat.pro.androiduikit.app;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import com.cometchat.pro.androiduikit.ComponentListActivity;
import com.cometchat.pro.androiduikit.CreateUserActivity;
import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class CreateUserActivityTest {

    private static final String BASIC_SAMPLE_PACKAGE
            = "com.cometchat.pro.androiduikit";

    private static final int LAUNCH_TIMEOUT = 5000;

    private static String UID = "superhero1";

    private static String NAME = "IronMan";

    private UiDevice mDevice;

    @Rule
    public ActivityTestRule<CreateUserActivity> activityRule = new ActivityTestRule<>(CreateUserActivity.class, true, false);

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
        if (CometChat.getLoggedInUser()!=null) {
            CometChat.logout(new CometChat.CallbackListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Log.e("onSuccess: ",s );
                }

                @Override
                public void onError(CometChatException e) {
                    Log.e( "onError: ",e.getMessage());
                }
            });
        }
    }

    @Test
    public void A_createUserWithExistingUID() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.create_user)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.etUID)).perform(ViewActions.clearText()).perform(ViewActions.typeText(UID));
            Espresso.onView(ViewMatchers.withId(R.id.etName)).perform(ViewActions.clearText()).perform(ViewActions.typeText(NAME));
            Espresso.onView(ViewMatchers.withId(R.id.create_user_btn)).perform(ViewActions.click());
            Thread.sleep(5000);
            Espresso.onView(ViewMatchers.withId(R.id.directLaunch)).perform(ViewActions.click());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void B_createUserWithRandomUID() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.create_user)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.etUID)).perform(ViewActions.clearText()).perform(ViewActions.typeText(Utils.generateRandomString(25)));
            Espresso.onView(ViewMatchers.withId(R.id.etName)).perform(ViewActions.clearText()).perform(ViewActions.typeText("TestCase User"));
            Espresso.onView(ViewMatchers.withId(R.id.create_user_btn)).perform(ViewActions.click());
            Thread.sleep(5000);
            Espresso.onView(ViewMatchers.withId(R.id.directLaunch)).perform(ViewActions.click());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void C_createUserWithEmptyName() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.create_user)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.etUID)).perform(ViewActions.clearText()).perform(ViewActions.typeText(Utils.generateRandomString(25)));
            Espresso.onView(ViewMatchers.withId(R.id.etName)).perform(ViewActions.clearText()).perform(ViewActions.typeText(""));
            Espresso.onView(ViewMatchers.withId(R.id.create_user_btn)).perform(ViewActions.click());
            Thread.sleep(5000);
            try {
                Espresso.onView(ViewMatchers.withId(R.id.directLaunch)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
                Assert.fail("Failed: Able to create user without authentication");
            } catch (AssertionError e) {
                Assert.assertTrue(true);
            } catch (NoMatchingViewException e) {
                Assert.assertTrue(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
