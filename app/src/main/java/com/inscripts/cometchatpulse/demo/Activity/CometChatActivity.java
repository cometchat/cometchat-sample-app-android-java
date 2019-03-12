package com.inscripts.cometchatpulse.demo.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.models.User;
import com.inscripts.cometchatpulse.demo.Adapter.ViewPagerAdapter;
import com.inscripts.cometchatpulse.demo.Contracts.CometChatActivityContract;
import com.inscripts.cometchatpulse.demo.Contracts.IncomingCallActivityContract;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.Fragments.ContactsFragment;
import com.inscripts.cometchatpulse.demo.Fragments.GroupListFragment;
import com.inscripts.cometchatpulse.demo.Helper.FabIconAnimator;
import com.inscripts.cometchatpulse.demo.Helper.ScrollHelper;
import com.inscripts.cometchatpulse.demo.Presenters.CometChatActivityPresenter;
import com.inscripts.cometchatpulse.demo.Presenters.IncomingCallActivityPresenter;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Utils.FontUtils;

public class CometChatActivity extends AppCompatActivity implements
        ScrollHelper, CometChatActivityContract.CometChatActivityView {

    private ViewPager mViewPager; //view pager

    private Toolbar toolbar;

    private AppBarLayout appBarLayout;

    private TabLayout tabs;

    private Context context;

    private FabIconAnimator fabIconAnimator;

    private ConstraintLayout container;

    private CoordinatorLayout mainContent;

    private CometChatActivityContract.CometChatActivityPresenter cometChatActivityPresenter;

    private IncomingCallActivityContract.IncomingCallActivityPresenter incomingCallActivityPresenter;

    private ViewPagerAdapter adapter;

    private static final String TAG = "CometChatActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comet_chat);
        context = this;
        initViewComponents();
        cometChatActivityPresenter = new CometChatActivityPresenter();
        incomingCallActivityPresenter = new IncomingCallActivityPresenter();
        cometChatActivityPresenter.attach(this);

    }



    private void initViewComponents() {

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        new FontUtils(this);

        appBarLayout = findViewById(R.id.appbar);

        getSupportActionBar().setTitle("");

        Drawable groupDrawable = getResources().getDrawable(R.drawable.ic_group_add_white_24dp);

        container = findViewById(R.id.constraint_container);
        mainContent = findViewById(R.id.main_content);


        fabIconAnimator = new FabIconAnimator(container);
        fabIconAnimator.update(groupDrawable, R.string.group);
        fabIconAnimator.setExtended(true);

        tabs = findViewById(R.id.tabs);

        fabIconAnimator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CreateGroupActivity.class));
            }
        });


        mViewPager = findViewById(R.id.container);

        mViewPager.setOffscreenPageLimit(2);

        setViewPager();

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();

    }




    @Override
    protected void onResume() {
        super.onResume();
        cometChatActivityPresenter.addCallEventListener(context, TAG);
        Log.d(TAG, "onResume: ");
        cometChatActivityPresenter.addMessageListener(CometChatActivity.this,TAG);

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");

        cometChatActivityPresenter.removeMessageListener(TAG);
        cometChatActivityPresenter.removeCallEventListener(TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        new GroupListFragment().onActivityResult(requestCode, resultCode, data);
        mViewPager.setCurrentItem(2);
    }

    private void setViewPager() {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ContactsFragment(), getString(R.string.contacts));
        adapter.addFragment(new GroupListFragment(), getString(R.string.group));
        mViewPager.setAdapter(adapter);
        tabs.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_settings:
                User user = CometChat.getLoggedInUser();
                Intent intent = new Intent(context, UsersProfileViewActivity.class);
                intent.putExtra(StringContract.IntentStrings.PROFILE_VIEW, true);
                intent.putExtra(StringContract.IntentStrings.USER_ID, user.getUid());
                intent.putExtra(StringContract.IntentStrings.USER_AVATAR, user.getAvatar());
                intent.putExtra(StringContract.IntentStrings.USER_NAME, user.getName());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void setFab(boolean isExtended) {
        fabIconAnimator.setExtended(isExtended);
    }


}
