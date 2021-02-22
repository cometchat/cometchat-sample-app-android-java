package com.cometchat.pro.androiduikit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.cometchat.pro.androiduikit.constants.AppConfig;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout inputLayout;
    private ProgressBar progressBar;
    private TextInputEditText uid;
    private TextView title;
    private TextView des1,des2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        title = findViewById(R.id.tvTitle);
        des1 = findViewById(R.id.tvDes1);
        des2 = findViewById(R.id.tvDes2);
        uid = findViewById(R.id.etUID);
        progressBar = findViewById(R.id.loginProgress);
        inputLayout = findViewById(R.id.inputUID);
        uid.setOnEditorActionListener((textView, i, keyEvent) -> {
             if (i== EditorInfo.IME_ACTION_DONE){
                 if (uid.getText().toString().isEmpty()) {
                     Toast.makeText(LoginActivity.this, "Fill Username field", Toast.LENGTH_LONG).show();
                 }
                 else {
                     progressBar.setVisibility(View.VISIBLE);
                     inputLayout.setEndIconVisible(false);
                     login(uid.getText().toString());
                 }
             }
            return true;
        });

        inputLayout.setEndIconOnClickListener(view -> {
            if (uid.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "Fill Username field", Toast.LENGTH_LONG).show();
            }
            else {
                findViewById(R.id.loginProgress).setVisibility(View.VISIBLE);
                inputLayout.setEndIconVisible(false);
                login(uid.getText().toString());
            }

        });
        checkDarkMode();
    }

    private void checkDarkMode() {
        if(Utils.isDarkMode(this)) {
            title.setTextColor(getResources().getColor(R.color.textColorWhite));
            des1.setTextColor(getResources().getColor(R.color.textColorWhite));
            des2.setTextColor(getResources().getColor(R.color.textColorWhite));
            uid.setTextColor(getResources().getColor(R.color.textColorWhite));
            inputLayout.setBoxStrokeColor(getResources().getColor(R.color.textColorWhite));
            inputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            inputLayout.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            uid.setHintTextColor(getResources().getColor(R.color.textColorWhite));
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
        } else {
            title.setTextColor(getResources().getColor(R.color.primaryTextColor));
            des1.setTextColor(getResources().getColor(R.color.primaryTextColor));
            des2.setTextColor(getResources().getColor(R.color.primaryTextColor));
            uid.setTextColor(getResources().getColor(R.color.primaryTextColor));
            inputLayout.setBoxStrokeColor(getResources().getColor(R.color.primaryTextColor));
            uid.setHint("");
            inputLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.secondaryTextColor)));
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryTextColor)));
        }
    }
    private void login(String uid) {


        CometChat.login(uid, AppConfig.AppDetails.AUTH_KEY, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                startActivity(new Intent(LoginActivity.this, SelectActivity.class));
                finish();
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
