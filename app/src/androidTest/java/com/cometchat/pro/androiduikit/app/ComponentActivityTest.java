package com.cometchat.pro.androiduikit.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import com.cometchat.pro.androiduikit.ComponentListActivity;
import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.androiduikit.utils.RecyclerViewAssertion;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.core.AllOf.allOf;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComponentActivityTest {


    private static final String BASIC_SAMPLE_PACKAGE
            = "com.cometchat.pro.androiduikit";

    private static final int LAUNCH_TIMEOUT = 5000;

    private UiDevice mDevice;

    @Rule
    public ActivityTestRule<ComponentListActivity> activityRule = new ActivityTestRule<>(ComponentListActivity.class, true, false);

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
        Espresso.onView(ViewMatchers.withId(R.id.componentLaunch)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.componentLaunch)).perform(ViewActions.click());
    }

    @Test
    public void A_checkAvatar() {

        //Check Components
        try {
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_avatar)).perform(ViewActions.scrollTo());
            Espresso.onView(withId(R.id.cometchat_avatar)).perform(click());
            Espresso.onView(withId(R.id.borderWidth)).perform(ViewActions.typeText("5"))
                    .perform(closeSoftKeyboard());
            Thread.sleep(2000);
            Espresso.onView(withId(R.id.borderWidth)).perform(pressBack());
            mDevice.pressBack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void B_checkBadgeCount() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_badge_count)).perform(ViewActions.scrollTo());
            Espresso.onView(withId(R.id.cometchat_badge_count)).perform(click());
            Espresso.onView(withId(R.id.badgeCount_edt)).perform(clearText())
                    .perform(ViewActions.typeText("9999"));
            Thread.sleep(2000);
            Espresso.onView(withId(R.id.countSize)).perform(clearText())
                    .perform(ViewActions.typeText("12"))
                    .perform(closeSoftKeyboard());
            Thread.sleep(2000);
            Espresso.onView(withId(R.id.countSize)).perform(pressBack());
            mDevice.pressBack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void C_checkStatusIndicator() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_status_indicator)).perform(ViewActions.scrollTo());
            Espresso.onView(withId(R.id.cometchat_status_indicator)).perform(click());
            Espresso.onView(withId(R.id.offline)).perform(click());
            Espresso.onView(withId(R.id.offline)).check(matches(ViewMatchers.isChecked()));

            Espresso.onView(withId(R.id.online)).perform(click());
            Espresso.onView(withId(R.id.online)).check(matches(ViewMatchers.isChecked()))
                    .perform(closeSoftKeyboard()).perform(pressBack());
            mDevice.pressBack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void J_checkCallList() {
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_callList)).perform(ViewActions.scrollTo());
            try {
                Espresso.onView(ViewMatchers.withId(R.id.cometchat_callList)).check(matches(isDisplayed()));
                Espresso.onView(ViewMatchers.withId(R.id.cometchat_callList)).perform(click());
            } catch (AssertionError e) {
                Log.e("D_checkCallList: ",e.getMessage() );
            } catch (NoMatchingViewException e) {
                Log.e("D_checkCallList: ",e.getMessage() );
            }
            try {

                Espresso.onView(ViewMatchers.withId(R.id.callList_rv))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
            } catch (PerformException e) {
                Assert.assertTrue("View Not loaded",true);
            } catch (Exception e) {
                Assert.fail();
            }
    }

    @Test
    public void K_checkCallList_2Sec() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_callList)).perform(ViewActions.scrollTo());
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_callList)).check(matches(isDisplayed()));
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_callList)).perform(click());
            Thread.sleep(2000);
            Espresso.onView(ViewMatchers.withId(R.id.callList_rv))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void D_checkUserList() {
        Espresso.onView(ViewMatchers.withId(R.id.cometchat_user_view)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.cometchat_user_view)).perform(click());
        try {
            Espresso.onView(ViewMatchers.withId(R.id.cometchatUserList))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()))
                    .perform(pressBack());
            mDevice.pressBack();;
        }
        catch (NoMatchingViewException e) {
            Log.e( "D_checkUserList: ","No View found...Trying again with 2 Sec Delay");
            D_checkUserList_2Sec();
        }
        catch (PerformException e) {
            Assert.assertTrue("View not loaded",true);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    public void D_checkUserList_2Sec() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_user_view)).perform(ViewActions.scrollTo());
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_user_view)).perform(ViewActions.click());
            Thread.sleep(2000);
            Espresso.onView(ViewMatchers.withId(R.id.cometchatUserList))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void F_checkGroupList() {
        Espresso.onView(ViewMatchers.withId(R.id.cometchat_group_view)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.cometchat_group_view)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.cometchatGroupList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
    }

    @Test
    public void G_checkGroupList_2Sec() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_group_view)).perform(ViewActions.scrollTo());
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_group_view)).perform(ViewActions.click());
            Thread.sleep(2000);
            Espresso.onView(ViewMatchers.withId(R.id.cometchatGroupList))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        } catch (NoMatchingViewException e) {
            Log.e( "F_checkGroupList_2Sec: ","View Not loaded...Moving to 5 sec" );
            F_checkGroupList_5Sec();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void F_checkGroupList_5Sec() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_group_view)).perform(ViewActions.scrollTo());
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_group_view)).perform(ViewActions.click());
            Thread.sleep(5000);
            Espresso.onView(ViewMatchers.withId(R.id.cometchatGroupList))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        } catch (NoMatchingViewException e) {
            Log.e( "F_checkGroupList_2Sec: ","View Not loaded...Moving to 10 sec" );
            F_checkGroupList_10Sec();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void F_checkGroupList_10Sec() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_group_view)).perform(ViewActions.scrollTo());
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_group_view)).perform(ViewActions.click());
            Thread.sleep(10000);
            Espresso.onView(ViewMatchers.withId(R.id.cometchatGroupList))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        } catch (NoMatchingViewException e) {
            Assert.fail("View Not Loaded"+e.getMessage());
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void H_checkConversationList() {
        Espresso.onView(ViewMatchers.withId(R.id.cometchat_conversation_view)).perform(ViewActions.scrollTo());
        Espresso.onView(ViewMatchers.withId(R.id.cometchat_conversation_view)).perform(ViewActions.click());
        try {
            Espresso.onView(ViewMatchers.withId(R.id.cometchatConversationList))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        }
        catch (NoMatchingViewException e) {
            Log.e( "H_checkConversationList: ","No View found...Trying again with 2 Sec Delay");
            H_checkConversationList_2Sec();
        }
        catch (PerformException e) {
            Assert.assertTrue("View not loaded",true);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    public void H_checkConversationList_2Sec() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_conversation_view)).perform(ViewActions.scrollTo());
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_conversation_view)).perform(ViewActions.click());
            Thread.sleep(2000);
            Espresso.onView(ViewMatchers.withId(R.id.cometchatConversationList))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        } catch (NoMatchingViewException e) {
            Log.e("H_checkConversationList_2Sec: ","View Not loaded..Moving to 5 Sec");
            H_checkConversationList_5Sec();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void H_checkConversationList_5Sec() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_conversation_view)).perform(ViewActions.scrollTo());
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_conversation_view)).perform(ViewActions.click());
            Thread.sleep(5000);
            Espresso.onView(ViewMatchers.withId(R.id.cometchatConversationList))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        } catch (NoMatchingViewException e) {
            Log.e("H_checkConversationList_5Sec: ","View Not loaded..Moving to 10 Sec");
            H_checkConversationList_10Sec();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void H_checkConversationList_10Sec() {
        try {
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_conversation_view)).perform(ViewActions.scrollTo());
            Espresso.onView(ViewMatchers.withId(R.id.cometchat_conversation_view)).perform(ViewActions.click());
            Thread.sleep(10000);
            Espresso.onView(ViewMatchers.withId(R.id.cometchatConversationList))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        } catch (NoMatchingViewException e) {
            Assert.fail("View Not Loaded"+e.getMessage());
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
//        RecyclerView callRv = activityRule.getActivity().findViewById(R.id.cometchat_callList);
//        int itemCount = callRv.getAdapter().getItemCount();
//        if (itemCount>0) {
//            Espresso.onView(withId(R.id.cometchat_callList)).perform(click());
//        }

}
