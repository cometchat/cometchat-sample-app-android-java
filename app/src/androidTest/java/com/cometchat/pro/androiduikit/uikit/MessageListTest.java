package com.cometchat.pro.androiduikit.uikit;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.intent.IntentCallback;
import androidx.test.runner.intent.IntentMonitorRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.Until;

import com.cometchat.pro.androiduikit.R;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.uikit.ui_components.chats.CometChatConversationList;
import com.cometchat.pro.uikit.ui_components.cometchat_ui.CometChatUI;
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageList;
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.io.OutputStream;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageListTest {
    private static final String BASIC_SAMPLE_PACKAGE
            = "com.cometchat.pro.androiduikit";

    private static final int LAUNCH_TIMEOUT = 5000;

    private static final String STRING_TO_BE_TYPED = "superhero5";

    private UiDevice mDevice;

    private BottomNavigationView bottomNavigation;

    @Rule
    public ActivityTestRule<CometChatMessageListActivity> activityTestRule = new ActivityTestRule<>(CometChatMessageListActivity.class,true,false);

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
            Thread.sleep(2000);
            view = conversationList.getView();
            Espresso.onView(allOf(
                    withId(R.id.rv_conversation_list),
                    isDescendantOfA(withId(view.getId())),
                    isDisplayed()))
                    .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void A_sendTextMessage() {
        Espresso.onView(Matchers.allOf(
                withId(R.id.etComposeBox),
                isDisplayed())).perform(ViewActions.typeText("Hi,Test Message"));
        Espresso.onView(Matchers.allOf(
                withId(R.id.ivSend),
                isDisplayed())).perform(ViewActions.click());
    }

    @Test
    public void B_sendMediaMessage() {
        Intents.init();
        Intent resultData = new Intent(Intent.ACTION_GET_CONTENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData));

        Espresso.onView(allOf(
                withId(R.id.ivArrow),
                isDisplayed())).perform(click());

        Espresso.onView(allOf(
                withId(R.id.gallery_message),
                isDisplayed())).perform(click());
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Intents.release();
    }

    @Test
    public void C_sendCameraImage() {
        Intents.init();

        intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(
                new Instrumentation.ActivityResult(Activity.RESULT_OK, null));

        IntentCallback intentCallback = new IntentCallback() {
            @Override
            public void onIntentSent(Intent intent) {
                if (intent.getAction().equals("android.media.action.IMAGE_CAPTURE")) {
                    try {
                        Uri imageUri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
                        Context context = InstrumentationRegistry.getInstrumentation().getContext();
                        Bitmap icon = BitmapFactory.decodeResource(
                                context.getResources(),
                                R.drawable.ic_menu_manage);
                        OutputStream out = InstrumentationRegistry.getInstrumentation()
                                .getContext().getContentResolver().openOutputStream(imageUri);
                        icon.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        Log.e("onIntentSent: ",e.getMessage());
                    }
                }
            }
        };
        IntentMonitorRegistry.getInstance().addIntentCallback(intentCallback);

        Espresso.onView(allOf(
                withId(R.id.ivArrow),
                isDisplayed())).perform(click());

        Espresso.onView(allOf(
                withId(R.id.camera_message),
                isDisplayed())).perform(click());
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Intents.release();
    }

    @Test
    public void D_checkAudioMessage() {
        Intents.init();
        Intent resultData = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData));

        Espresso.onView(allOf(
                withId(R.id.ivArrow),
                isDisplayed())).perform(click());

        Espresso.onView(allOf(
                withId(R.id.audio_message),
                isDisplayed())).perform(click());
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
        Intents.release();

    }

    @Test
    public void E_checkLocationMessage() {
        try {
            Espresso.onView(allOf(
                withId(R.id.ivArrow),
                isDisplayed())).perform(click());

            Espresso.onView(allOf(
                    withId(R.id.location_message),
                    isDisplayed())).perform(click());

            Thread.sleep(2000);

            Espresso.onView(allOf(
                    withText(R.string.share),
                    isDisplayed())).perform(click());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

}




