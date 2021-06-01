package com.cometchat.pro.uikit.ui_components.groups.group_members;

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

public class CometChatGroupMemberListActivity extends AppCompatActivity {

    private String guid;

    private boolean transferOwnerShip;

    private boolean showModerators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        if (getIntent().hasExtra(UIKitConstants.IntentStrings.GUID))
            guid = getIntent().getStringExtra(UIKitConstants.IntentStrings.GUID);
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.SHOW_MODERATORLIST))
            showModerators = getIntent().getBooleanExtra(UIKitConstants.IntentStrings.SHOW_MODERATORLIST,false);
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.TRANSFER_OWNERSHIP))
            transferOwnerShip = getIntent()
                    .getBooleanExtra(UIKitConstants.IntentStrings.TRANSFER_OWNERSHIP,false);

        Fragment fragment = new CometChatGroupMemberList();
        Bundle bundle = new Bundle();
        bundle.putString(UIKitConstants.IntentStrings.GUID,guid);
        bundle.putBoolean(UIKitConstants.IntentStrings.SHOW_MODERATORLIST,showModerators);
        bundle.putBoolean(UIKitConstants.IntentStrings.TRANSFER_OWNERSHIP,transferOwnerShip);
        fragment.setArguments(bundle);
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

           if(item.getItemId()==android.R.id.home){
               onBackPressed();
           }

        return super.onOptionsItemSelected(item);
    }


}
