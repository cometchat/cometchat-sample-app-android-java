package com.inscripts.cometchatpulse.demo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.inscripts.cometchatpulse.demo.Adapter.AutoCompleteAdapter;
import com.inscripts.cometchatpulse.demo.Fcm.MyFirebaseService;
import com.inscripts.cometchatpulse.demo.Pojo.Option;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Contracts.LoginActivityContract;
import com.inscripts.cometchatpulse.demo.Presenters.LoginAcitivityPresenter;

import java.util.ArrayList;
import java.util.List;

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

        AutoCompleteAdapter autoCompleteAdapter=new
                AutoCompleteAdapter(this,R.layout.sample_user,getSampleUserList());

        GridView gridView=findViewById(R.id.sample_list);

        gridView.setAdapter(autoCompleteAdapter);

         gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 textInputEditTextUid.setText(getSampleUserList().get(position).getId());
                 login();
             }
         });


        login.setOnClickListener(view -> login());
    }

    private void login(){
        String uid = textInputEditTextUid.getText().toString().trim();

        if (!uid.isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.wait), Toast.LENGTH_SHORT).show();
            loginActivityPresenter.Login(uid);
            MyFirebaseService.subscribeUser(uid);
        } else {

            Toast.makeText(LoginActivity.this, getString(R.string.enter_uid_toast), Toast.LENGTH_SHORT).show();
        }
    }


    private List<Option> getSampleUserList(){

        List<Option> sampleList=new ArrayList<>();

        sampleList.add(new Option("Iron Man",getResources().getDrawable(R.drawable.ironman),"superhero1"));
        sampleList.add(new Option("Captain America",getResources().getDrawable(R.drawable.captainamerica),"superhero2"));
        sampleList.add(new Option("SpiderMan",getResources().getDrawable(R.drawable.spiderman),"superhero3"));
        sampleList.add(new Option("Wolverine",getResources().getDrawable(R.drawable.wolverine),"superhero4"));

        return sampleList;
    }


    @Override
    public void startCometChatActivity() {

        startActivity(new Intent(LoginActivity.this, CometChatActivity.class));
        finish();

    }
}
