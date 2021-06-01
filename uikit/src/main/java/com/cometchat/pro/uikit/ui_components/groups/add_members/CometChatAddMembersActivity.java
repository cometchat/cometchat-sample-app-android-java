package com.cometchat.pro.uikit.ui_components.groups.add_members;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cometchat.pro.uikit.R;

import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_settings.FeatureRestriction;

public class CometChatAddMembersActivity extends AppCompatActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        handleIntent();
        if (FeatureRestriction.getColor()!=null)
            getWindow().setStatusBarColor(Color.parseColor(FeatureRestriction.getColor()));
    }

    private void handleIntent() {
        if (getIntent()!=null) {
            Bundle bundle = new Bundle();
            fragment=new CometChatAddMembers();
            if (getIntent().hasExtra(UIKitConstants.IntentStrings.GUID))
                bundle.putString(UIKitConstants.IntentStrings.GUID, getIntent()
                        .getStringExtra(UIKitConstants.IntentStrings.GUID));
            if (getIntent().hasExtra(UIKitConstants.IntentStrings.GROUP_NAME))
                bundle.putString(UIKitConstants.IntentStrings.GROUP_NAME, getIntent()
                        .getStringExtra(UIKitConstants.IntentStrings.GROUP_NAME));
            if (getIntent().hasExtra(UIKitConstants.IntentStrings.GROUP_MEMBER))
                bundle.putStringArrayList(UIKitConstants.IntentStrings.GROUP_MEMBER,getIntent()
                        .getStringArrayListExtra(UIKitConstants.IntentStrings.GROUP_MEMBER));

            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment,fragment).commit();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

           if(item.getItemId()==android.R.id.home){
               onBackPressed();
           }

        return super.onOptionsItemSelected(item);
    }


}
