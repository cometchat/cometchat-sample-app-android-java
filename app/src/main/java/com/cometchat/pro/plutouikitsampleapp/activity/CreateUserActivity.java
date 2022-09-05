package com.cometchat.pro.plutouikitsampleapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.plutouikitsampleapp.R;
import com.cometchat.pro.plutouikitsampleapp.constants.AppConfig;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreateUserActivity extends AppCompatActivity {
    private TextInputEditText uid;
    private TextInputEditText name;
    private AppCompatButton createUserBtn;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        progressBar = findViewById(R.id.createUserPb);
        uid = findViewById(R.id.etUID);
        name = findViewById(R.id.etName);
        createUserBtn = findViewById(R.id.createUserBtn);
        createUserBtn.setTextColor(getResources().getColor(R.color.white));
        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid.getText().toString().isEmpty())
                    uid.setError(getResources().getString(R.string.fill_this_field));
                else if (name.getText().toString().isEmpty())
                    name.setError(getResources().getString(R.string.fill_this_field));
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    createUserBtn.setClickable(false);
                    User user = new User();
                    user.setUid(uid.getText().toString());
                    user.setName(name.getText().toString());
                    CometChat.createUser(user, AppConfig.AppDetails.AUTH_KEY, new CometChat.CallbackListener<User>() {
                        @Override
                        public void onSuccess(User user) {
                            login(user);
                        }

                        @Override
                        public void onError(CometChatException e) {
                            createUserBtn.setClickable(true);
                            Toast.makeText(CreateUserActivity.this, "Failed to create user", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void login(User user) {
        CometChat.login(user.getUid(), AppConfig.AppDetails.AUTH_KEY, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                startActivity(new Intent(CreateUserActivity.this, HomeActivity.class));
                finishAffinity();
            }

            @Override
            public void onError(CometChatException e) {
                if (uid != null)
                    Snackbar.make(uid.getRootView(), "Unable to login", Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(CreateUserActivity.this, LoginActivity.class));
                        }
                    }).show();
            }
        });
    }
}