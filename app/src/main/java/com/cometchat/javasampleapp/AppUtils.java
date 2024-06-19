package com.cometchat.javasampleapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.cometchat.chat.core.CometChat;
import com.cometchat.chat.exceptions.CometChatException;
import com.cometchat.chat.models.Group;
import com.cometchat.chat.models.User;
import com.cometchat.chatuikit.shared.resources.utils.Utils;
import com.cometchat.javasampleapp.constants.StringConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AppUtils {
    private static Group group;
    private static User user;
    private static List<User> userList = new ArrayList<>();

    public static void fetchDefaultObjects() {
        CometChat.getGroup("supergroup", new CometChat.CallbackListener<Group>() {
            @Override
            public void onSuccess(Group group_) {
                group = group_;
            }

            @Override
            public void onError(CometChatException e) {

            }
        });
        CometChat.getUser(!userList.isEmpty() ? userList.get(userList.size() - 1).getUid() : "superhero5", new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user_) {
                user = user_;
            }

            @Override
            public void onError(CometChatException e) {

            }
        });
    }

    public static void fetchSampleUsers(CometChat.CallbackListener<List<User>> listener) {
        if (userList.isEmpty()) {
            Request request = new Request.Builder().url(StringConstants.SAMPLE_APP_USERS_URL).method("GET", null).build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                public void onFailure(Call call, IOException e) {
                    Utils.runOnMainThread(() -> listener.onError(new CometChatException("11", e.getMessage())));
                }

                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            userList = processSampleUserList(response.body().string());
                        } catch (IOException e) {
                            Utils.runOnMainThread(() -> listener.onError(new CometChatException("10", e.getMessage())));
                        }
                        Utils.runOnMainThread(() -> listener.onSuccess(userList));
                    } else {
                        Utils.runOnMainThread(() -> listener.onError(new CometChatException("Unexpected code ", String.valueOf(response.code()))));
                    }
                }
            });
        } else {
            Utils.runOnMainThread(() -> listener.onSuccess(userList));
        }
    }

    public static List<User> processSampleUserList(String jsonString) {
        List<User> users = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(StringConstants.KEY_USER);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userJson = jsonArray.getJSONObject(i);
                User user = new User();
                user.setUid(userJson.getString(StringConstants.UID));
                user.setName(userJson.getString(StringConstants.NAME));
                user.setAvatar(userJson.getString(StringConstants.AVATAR));
                users.add(user);
            }
        } catch (Exception ignore) {

        }
        return users;
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("SampleUsers.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static Group getDefaultGroup() {
        return group;
    }

    public static User getDefaultUser() {
        return user;
    }

    public static void switchLightMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    public static void switchDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public static boolean isNightMode(Context context) {
        int currentNightMode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static void changeIconTintToWhite(Context context, ImageView imageView) {
        imageView.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
    }

    public static void changeIconTintToBlack(Context context, ImageView imageView) {
        imageView.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black)));
    }

    public static void changeTextColorToWhite(Context context, TextView textView) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.white));
    }

    public static void changeTextColorToBlack(Context context, TextView textView) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.black));
    }

}
