package com.cometchat.pro.androiduikit.app;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;


import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.core.CometChat;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SelectActivityTest {

    private static final String BASIC_SAMPLE_PACKAGE
            = "com.cometchat.pro.androiduikit";

    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String STRING_TO_BE_TYPED = "superhero5";

    private UiDevice mDevice;

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
    }

    @Test
    public void launchUnifiedTest() {
        Espresso.onView(ViewMatchers.withId(R.id.directLaunch)).perform(ViewActions.click());
    }

    @Test
    public void launchComponentTest() {
        Espresso.onView(ViewMatchers.withId(R.id.componentLaunch)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.componentLaunch)).perform(ViewActions.click());
    }
}
