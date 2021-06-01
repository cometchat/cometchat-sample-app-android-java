package com.cometchat.pro.uikit.ui_components.groups.create_group;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cometchat.pro.uikit.R;

import com.cometchat.pro.uikit.ui_settings.FeatureRestriction;

public class CometChatCreateGroupActivity extends AppCompatActivity {

    private Fragment fragment;

    private String guid;

    private String loggedInUserScope;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        Fragment fragment = new CometChatCreateGroup();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment,fragment).commit();
        if (FeatureRestriction.getColor()!=null)
            getWindow().setStatusBarColor(Color.parseColor(FeatureRestriction.getColor()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
