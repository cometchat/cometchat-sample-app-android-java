package com.inscripts.cometchatpulse.demo.Activity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.models.Group;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Contracts.CreateGroupActivityContract;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.Presenters.CreateGroupActivityPresenter;
import com.inscripts.cometchatpulse.demo.Utils.AnimUtil;
import com.inscripts.cometchatpulse.demo.Utils.FontUtils;


public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener,
        CreateGroupActivityContract.CreateGroupView, AdapterView.OnItemSelectedListener {

    private TextInputEditText groupIdField;

    private TextInputEditText groupPasswordField;

    private TextInputEditText groupDescriptionField;

    private TextInputEditText groupNameField;

    private String groupId, groupType, groupPassword, groupDescription, groupName;

    private TextInputLayout textInputLayoutName, textInputLayoutGroupId,
            textInputLayoutPassword, textInputLayoutGroupDescription;

    private Toolbar toolbar;

    private CreateGroupActivityContract.CreateGroupPresenter createGroupPresenter;

    private Spinner spinner;

    private Context context;

    private LinearLayout rootView;

    private int revealX;

    private int revealY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);
        context = this;

        createGroupPresenter = new CreateGroupActivityPresenter();
        createGroupPresenter.attach(this);
        initComponents();
        new FontUtils(this);

        rootView = findViewById(R.id.root_view);

    }


    private void initComponents() {

        toolbar = findViewById(R.id.create_group_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(10);


        textInputLayoutGroupDescription = findViewById(R.id.input_layout_description);
        textInputLayoutGroupId = findViewById(R.id.input_layout_group_id);
        textInputLayoutPassword = findViewById(R.id.input_layout_password);
        groupIdField = findViewById(R.id.input_group_id);
        groupPasswordField = findViewById(R.id.input_password);
        groupDescriptionField = findViewById(R.id.input_decription);
        groupNameField = findViewById(R.id.input_name);
        textInputLayoutName = findViewById(R.id.input_layout_name);

        //input text typeface
        groupPasswordField.setTypeface(FontUtils.openSansRegular);
        groupDescriptionField.setTypeface(FontUtils.openSansRegular);
        groupIdField.setTypeface(FontUtils.openSansRegular);
        groupNameField.setTypeface(FontUtils.openSansRegular);

        //label text typeface
        textInputLayoutPassword.setTypeface(FontUtils.robotoCondenseRegular);
        textInputLayoutGroupDescription.setTypeface(FontUtils.robotoCondenseRegular);
        textInputLayoutGroupId.setTypeface(FontUtils.robotoCondenseRegular);
        textInputLayoutName.setTypeface(FontUtils.robotoCondenseRegular);

        groupPasswordField.addTextChangedListener(passwordWatcher);
        groupIdField.addTextChangedListener(idWatcher);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.group_type, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


    }



    TextWatcher idWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            textInputLayoutGroupId.setHelperTextEnabled(true);
            textInputLayoutGroupId.setErrorEnabled(false);
            textInputLayoutGroupId.setHelperText(getString(R.string.required));
            textInputLayoutGroupId.setCounterEnabled(true);
            textInputLayoutGroupId.setError(null);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    TextWatcher passwordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            textInputLayoutPassword.setErrorEnabled(false);
            textInputLayoutPassword.setError(null);
            textInputLayoutPassword.setHelperTextEnabled(true);
            textInputLayoutPassword.setHelperText(getString(R.string.required));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        createGroupPresenter.detach();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View view) {


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        if (id == R.id.menu_create_group) {
            groupId = groupIdField.getText().toString().trim();
            groupPassword = groupPasswordField.getText().toString().trim();
            groupDescription = groupDescriptionField.getText().toString().trim();
            groupName=groupNameField.getText().toString().trim();


             if (groupId.isEmpty()) {
                textInputLayoutGroupId.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.error)));
            } if (groupType.isEmpty()) {

                showToast(getString(R.string.valid_group));
            }
            if (groupName.isEmpty())
            {

                textInputLayoutName.setHelperTextColor(ColorStateList.valueOf(getResources().getColor(R.color.error)));

            }

            if (groupType.equalsIgnoreCase(CometChatConstants.GROUP_TYPE_PASSWORD) && groupPassword.isEmpty()) {
                groupPasswordField.setText("");
                textInputLayoutPassword.setHelperTextColor(ColorStateList.valueOf(getResources()
                        .getColor(R.color.error)));
            }

            if (!groupId.isEmpty() && !groupType.isEmpty()&& !groupName.isEmpty()) {

                if (groupType.equalsIgnoreCase(CometChatConstants.GROUP_TYPE_PASSWORD)) {

                    if (!groupPassword.isEmpty()) {
                        Group group = new Group(groupId, groupName, groupType.toLowerCase(), groupPassword, null, groupDescription);
                        createGroupPresenter.createGroup(context, group);
                    } /*else {
                            chatroomPasswordField.setText("");
                            chatroomPasswordField.setError(chatroomLang.get26());
                        }*/
                } else {
                    Group group = new Group(groupId, groupName, groupType.toLowerCase(), null, null, groupDescription);
                    createGroupPresenter.createGroup(context, group);
                }

            }
            else {
               showToast("Fill all required fields");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_create_group, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void setTitle(String title) {

    }

    private void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        groupType = (String) adapterView.getSelectedItem();

        if (groupType.equalsIgnoreCase(CometChatConstants.GROUP_TYPE_PASSWORD)) {
            textInputLayoutPassword.setVisibility(View.VISIBLE);
        } else {
            textInputLayoutPassword.setVisibility(View.GONE);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }
}
