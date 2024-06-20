package com.cometchat.pro.androiduikit;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.cometchat.pro.androiduikit.constants.StringConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;

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

    private static List<User> userList = new ArrayList<>();

    public static void fetchSampleUsers(CometChat.CallbackListener<List<User>> listener) {
        if (userList.isEmpty()) {
            Request request = new Request.Builder().url(StringConstants.SAMPLE_APP_USERS_URL).method("GET", null).build();
            OkHttpClient client = new OkHttpClient();
            client.newCall(request).enqueue(new Callback() {
                public void onFailure(Call call, IOException e) {
                    runOnMainThread(() -> listener.onError(new CometChatException("11", e.getMessage())));
                }

                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            userList = processSampleUserList(response.body().string());
                        } catch (IOException e) {
                            runOnMainThread(() -> listener.onError(new CometChatException("10", e.getMessage())));
                        }
                        runOnMainThread(() -> listener.onSuccess(userList));
                    } else {
                        runOnMainThread(() -> listener.onError(new CometChatException("Unexpected code ", String.valueOf(response.code()))));
                    }
                }
            });
        } else {
            runOnMainThread(() -> listener.onSuccess(userList));
        }
    }

    public static void runOnMainThread(Runnable runnable) {
        Handler mainThread = new Handler(Looper.getMainLooper());
        mainThread.post(runnable);
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
    public static void changeTextColorToWhite(Context context, TextView textView) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.textColorWhite));
    }
    public static void changeTextColorToBlack(Context context, TextView textView) {
        textView.setTextColor(ContextCompat.getColor(context, R.color.black));
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

}
