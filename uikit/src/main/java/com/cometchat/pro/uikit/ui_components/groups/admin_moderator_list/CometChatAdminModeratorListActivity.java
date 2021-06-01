package com.cometchat.pro.uikit.ui_components.groups.admin_moderator_list;

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

public class CometChatAdminModeratorListActivity extends AppCompatActivity {


    private String guid;

    private String ownerId;

    private boolean showModerator;

    private String loggedInUserScope;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        handleIntent();
        if (FeatureRestriction.getColor()!=null)
            getWindow().setStatusBarColor(Color.parseColor(FeatureRestriction.getColor()));
    }

    private void handleIntent() {
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.MEMBER_SCOPE)){
            loggedInUserScope=getIntent().getStringExtra(UIKitConstants.IntentStrings.MEMBER_SCOPE);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.GUID)) {
            guid = getIntent().getStringExtra(UIKitConstants.IntentStrings.GUID);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.GROUP_OWNER)) {
            ownerId = getIntent().getStringExtra(UIKitConstants.IntentStrings.GROUP_OWNER);
        }
        if (getIntent().hasExtra(UIKitConstants.IntentStrings.SHOW_MODERATORLIST)) {
            showModerator = getIntent().getBooleanExtra(UIKitConstants.IntentStrings.SHOW_MODERATORLIST,false);
        }
        Fragment fragment = new CometChatAdminModeratorList();
        Bundle bundle = new Bundle();
        bundle.putString(UIKitConstants.IntentStrings.GUID,guid);
        bundle.putString(UIKitConstants.IntentStrings.GROUP_OWNER,ownerId);
        bundle.putString(UIKitConstants.IntentStrings.MEMBER_SCOPE,loggedInUserScope);
        bundle.putBoolean(UIKitConstants.IntentStrings.SHOW_MODERATORLIST,showModerator);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment,fragment).commit();

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
