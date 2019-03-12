package com.inscripts.cometchatpulse.demo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Contracts.LoginActivityContract;
import com.inscripts.cometchatpulse.demo.Presenters.LoginAcitivityPresenter;

public class LoginActivity extends AppCompatActivity implements LoginActivityContract.LoginActivityView {

    private TextInputLayout textInputLayoutUid;

    private TextInputEditText textInputEditTextUid;

    private Button login;

    private LoginActivityContract.LoginActivityPresenter loginActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginActivityPresenter = new LoginAcitivityPresenter();
        loginActivityPresenter.attach(this);
        loginActivityPresenter.loginCheck();
        initComponentView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginActivityPresenter.detach();
    }

    private void initComponentView() {
        textInputLayoutUid = findViewById(R.id.input_layout_uid);
        textInputEditTextUid = findViewById(R.id.guid);
        login = findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = textInputEditTextUid.getText().toString().trim();

                if (!uid.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.wait), Toast.LENGTH_SHORT).show();
                    loginActivityPresenter.Login(uid);
                } else {

                    Toast.makeText(LoginActivity.this, getString(R.string.enter_uid_toast), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void startCometChatActivity() {

        startActivity(new Intent(LoginActivity.this, CometChatActivity.class));
        finish();

    }
}
