package screen.groupmemberlist;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cometchat.pro.uikit.R;

import constant.StringContract;
import com.cometchat.pro.uikit.Settings.UISettings;

public class CometChatGroupMemberListScreenActivity extends AppCompatActivity {

    private String guid;

    private boolean transferOwnerShip;

    private boolean showModerators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_screen);

        if (getIntent().hasExtra(StringContract.IntentStrings.GUID))
            guid = getIntent().getStringExtra(StringContract.IntentStrings.GUID);
        if (getIntent().hasExtra(StringContract.IntentStrings.SHOW_MODERATORLIST))
            showModerators = getIntent().getBooleanExtra(StringContract.IntentStrings.SHOW_MODERATORLIST,false);
        if (getIntent().hasExtra(StringContract.IntentStrings.TRANSFER_OWNERSHIP))
            transferOwnerShip = getIntent()
                    .getBooleanExtra(StringContract.IntentStrings.TRANSFER_OWNERSHIP,false);

        Fragment fragment = new CometChatGroupMemberListScreen();
        Bundle bundle = new Bundle();
        bundle.putString(StringContract.IntentStrings.GUID,guid);
        bundle.putBoolean(StringContract.IntentStrings.SHOW_MODERATORLIST,showModerators);
        bundle.putBoolean(StringContract.IntentStrings.TRANSFER_OWNERSHIP,transferOwnerShip);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment,fragment).commit();
        if (UISettings.getColor()!=null)
            getWindow().setStatusBarColor(Color.parseColor(UISettings.getColor()));
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
