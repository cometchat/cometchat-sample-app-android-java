package com.cometchat.pro.androiduikit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cometchat.pro.androiduikit.constants.AppConfig;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class CreateUserActivity extends AppCompatActivity {

    private TextInputLayout inputUid,inputName;
    private TextInputEditText uid;
    private TextInputEditText name;
    private MaterialButton createUserBtn;
    private ProgressBar progressBar;
    private TextView title;
    private TextView des1,des2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        inputUid = findViewById(R.id.inputUID);
        inputName = findViewById(R.id.inputName);
        title = findViewById(R.id.tvTitle);
        des1 = findViewById(R.id.tvDes1);
        des2 = findViewById(R.id.tvDes2);
        progressBar = findViewById(R.id.createUser_pb);
        uid = findViewById(R.id.etUID);
        name = findViewById(R.id.etName);
        createUserBtn = findViewById(R.id.create_user_btn);
        createUserBtn.setTextColor(getResources().getColor(R.color.textColorWhite));
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
                            Toast.makeText(CreateUserActivity.this,"Failed to create user",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        checkDarkMode();
    }

    private void checkDarkMode() {
        if(Utils.isDarkMode(this)) {
            title.setTextColor(getResources().getColor(R.color.textColorWhite));
            des2.setTextColor(getResources().getColor(R.color.textColorWhite));
            uid.setTextColor(getResources().getColor(R.color.textColorWhite));
            name.setTextColor(getResources().getColor(R.color.textColorWhite));
            inputUid.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            inputUid.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            inputUid.setBoxStrokeColor(getResources().getColor(R.color.textColorWhite));
            inputName.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            inputName.setBoxStrokeColor(getResources().getColor(R.color.textColorWhite));
            inputName.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
        } else {
            title.setTextColor(getResources().getColor(R.color.primaryTextColor));
            des2.setTextColor(getResources().getColor(R.color.primaryTextColor));
            uid.setTextColor(getResources().getColor(R.color.primaryTextColor));
            inputUid.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.secondaryTextColor)));
            inputUid.setBoxStrokeColor(getResources().getColor(R.color.primaryTextColor));
            inputName.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.secondaryTextColor)));
            inputName.setBoxStrokeColor(getResources().getColor(R.color.primaryTextColor));
            name.setTextColor(getResources().getColor(R.color.primaryTextColor));
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryTextColor)));
        }
    }

    private void login(User user) {
        CometChat.login(user.getUid(), AppConfig.AppDetails.AUTH_KEY, new CometChat.CallbackListener<User>() {
            @Override
            public void onSuccess(User user) {
                startActivity(new Intent(CreateUserActivity.this,SelectActivity.class));
            }

            @Override
            public void onError(CometChatException e) {
                if (uid!=null)
                    Snackbar.make(uid.getRootView(),"Unable to login",Snackbar.LENGTH_INDEFINITE).setAction("Try Again", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(CreateUserActivity.this,LoginActivity.class));
                        }
                    }).show();
            }
        });
    }
}
