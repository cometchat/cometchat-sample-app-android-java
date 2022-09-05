package com.cometchat.pro.plutouikitsampleapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.BaseMessage;
import com.cometchat.pro.models.Conversation;
import com.cometchat.pro.models.User;
import com.cometchat.pro.plutouikitsampleapp.R;
import com.cometchat.pro.plutouikitsampleapp.constants.AppConfig;
import com.cometchatworkspace.resources.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout inputLayout;
    private ProgressBar progressBar;
    private TextInputEditText uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        uid = findViewById(R.id.etUID);
        progressBar = findViewById(R.id.loginProgress);
        inputLayout = findViewById(R.id.inputUID);

        uid.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (uid.getText().toString().isEmpty()) {
                    uid.setError("Enter UID");
                } else {
                    uid.setError(null);
                    progressBar.setVisibility(View.VISIBLE);
                    inputLayout.setEndIconVisible(false);
                    login(uid.getText().toString());
                }
            }
            return true;
        });


        findViewById(R.id.tvSignIn).setOnClickListener(view -> {
            if (uid.getText().toString().isEmpty()) {
                uid.setError("Enter UID");
            } else {
                findViewById(R.id.loginProgress).setVisibility(View.VISIBLE);
                inputLayout.setEndIconVisible(false);
                login(uid.getText().toString());
            }

        });

    }

    private void login(String uid) {
        CometChat.login(uid, AppConfig.AppDetails.AUTH_KEY, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finishAffinity();
            }
            @Override
            public void onError(CometChatException e) {
                inputLayout.setEndIconVisible(true);
                findViewById(R.id.loginProgress).setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void createUser(View view) {
        startActivity(new Intent(LoginActivity.this,CreateUserActivity.class));
    }
}