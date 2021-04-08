package com.cometchat.pro.androiduikit.app;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.ComponentNameMatchers;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import com.cometchat.pro.androiduikit.MainActivity;
import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.androiduikit.UIKitApplication;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;

import junit.framework.AssertionFailedError;

import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

import static androidx.test.InstrumentationRegistry.getTargetContext;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.init;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasClassName;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityTest {

    private static final String BASIC_SAMPLE_PACKAGE
                = "com.cometchat.pro.androiduikit";

    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String STRING_TO_BE_TYPED = "superhero5";

    private UiDevice mDevice;

    @Rule
    public GrantPermissionRule internetRule = GrantPermissionRule.grant(
                Manifest.permission.INTERNET,Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE);

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, false);


    @After
    public void tearDown() {
        Intents.release();
    }

    @Before
    public void startMainActivityFromHomeScreen() {
        Intents.init();
        CometChat.logout(new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e("onSuccess: ",s );
            }

            @Override
            public void onError(CometChatException e) {
                Log.e("Error: ",e.getMessage());
            }
        });
//        new UIKitApplication().setTestCasesRun(true);
        ArrayList<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        //add here your other permissions

        for (int i = 0; i < permissions.size(); i++) {
            String command = String.format("pm grant %s %s", getInstrumentation().getContext().getPackageName(), permissions.get(i));
            getInstrumentation().getUiAutomation().executeShellCommand(command);
            // wait a bit until the command is finished
            SystemClock.sleep(1000);
        }
//    }
//         Initialize UiDevice instance
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
    public void A_sameActivity3Sec() {
        if (CometChat.getLoggedInUser()==null) {
            ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
            // Type text and then press the button.
            Espresso.onView(ViewMatchers.withId(R.id.superhero1)).perform(ViewActions.click());
            try {
                Thread.sleep(3000);
                Espresso.onView(ViewMatchers.withId(R.id.logout)).perform(scrollTo());
                Espresso.onView(ViewMatchers.withId(R.id.logout)).perform(ViewActions.click());
                Thread.sleep(3000);
            } catch (Exception e) {
                Assert.fail("login_sameActivity:Failed in 3 Sec- "+e.getMessage());
                B_sameActivity7Sec();
            }
        } else {
            Log.e( "login_sameActivity: ","User already logged IN" );
        }
    }

    public void B_sameActivity7Sec() {
        if (CometChat.getLoggedInUser()==null) {
            ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
            // Type text and then press the button.
            Espresso.onView(ViewMatchers.withId(R.id.superhero2)).perform(ViewActions.click());
            try {
                Thread.sleep(7000);
                Espresso.onView(ViewMatchers.withId(R.id.logout)).perform(scrollTo());
                Espresso.onView(ViewMatchers.withId(R.id.logout)).perform(ViewActions.click());
                Thread.sleep(3000);
            } catch (Exception e) {
                Assert.fail("login_sameActivity:Failed in 7 Sec - "+e.getMessage());
                C_sameActivity15Sec();
            }
        } else {
            Log.e( "login_sameActivity: ","User already logged IN" );
        }
    }

    public void C_sameActivity15Sec() {
        if (CometChat.getLoggedInUser()==null) {
            ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
            activityScenario.moveToState(Lifecycle.State.RESUMED);
            // Type text and then press the button.
            Espresso.onView(ViewMatchers.withId(R.id.superhero3)).perform(ViewActions.click());
            try {
                Thread.sleep(15000);
                Espresso.onView(ViewMatchers.withId(R.id.logout)).perform(scrollTo());
                Espresso.onView(ViewMatchers.withId(R.id.logout)).perform(ViewActions.click());
                Thread.sleep(3000);
            } catch (Exception e) {
                Assert.fail("login_sameActivity:Failed in 15 Sec- "+e.getMessage());
            }
        } else {
            Log.e( "login_sameActivity: ","User already logged IN" );
        }
    }

    @Test
    public void D_goToLoginActivity() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        if (CometChat.getLoggedInUser()==null) {
            Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.etUID))
                    .perform(ViewActions.typeText("superhero4"))
                    .perform(ViewActions.pressImeActionButton());
        }
        Log.e( "goToLoginActivity: ","User already loggedIn" );
    }

    @Test
    public void E_failLoginActivity() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        if (CometChat.getLoggedInUser()==null) {
            Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.etUID))
                    .perform(ViewActions.typeText(""))
                    .perform(ViewActions.pressImeActionButton());
            try {
                Espresso.onView(ViewMatchers.withId(R.id.logout)).check(matches(isDisplayed()));
                Assert.fail("Failed: Enter to Select Activity without login");
            } catch (AssertionFailedError e) {
                Assert.assertTrue(true);
            } catch (NoMatchingViewException e) {
                Assert.assertTrue(true);
            }
        } else {
            Assert.assertTrue(true);
            Log.e("goToLoginActivity: ", "User already loggedIn");
        }
    }


    @Test
    public void F_createUserWithExistingUID() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.create_user)).perform(ViewActions.click());
            Espresso.onView(ViewMatchers.withId(R.id.etUID)).perform(ViewActions.clearText()).perform(ViewActions.typeText("superhero1"));
            Espresso.onView(ViewMatchers.withId(R.id.etName)).perform(ViewActions.clearText()).perform(ViewActions.typeText("IronMan"));
            Espresso.onView(ViewMatchers.withId(R.id.create_user_btn)).perform(ViewActions.click());
            Thread.sleep(5000);
            Espresso.onView(ViewMatchers.withId(R.id.directLaunch)).perform(ViewActions.click());
        } catch (Exception e) {
            Log.e( "A_createUserWithExistingUID: ",e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void G_createUserWithEmptyName() {
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

    @Test
    public void H_createUserWithRandomUID() {
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

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = ApplicationProvider.getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }


    private static ViewAction setProgressBarVisibitity(final boolean value) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                    return isAssignableFrom(ProgressBar.class);
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.setVisibility(value ? View.VISIBLE : View.GONE);
            }

            @Override
            public String getDescription() {
                return "Show / Hide View";
            }
        };
    }
}
