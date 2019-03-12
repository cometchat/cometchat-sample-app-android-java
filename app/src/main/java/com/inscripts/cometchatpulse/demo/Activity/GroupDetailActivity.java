package com.inscripts.cometchatpulse.demo.Activity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Adapter.ViewPagerAdapter;
import com.inscripts.cometchatpulse.demo.Contracts.GroupDetailActivityContract;
import com.inscripts.cometchatpulse.demo.Contracts.StringContract;
import com.inscripts.cometchatpulse.demo.Fragments.MemberListFragment;
import com.inscripts.cometchatpulse.demo.Fragments.OutcastMemberListFragment;
import com.inscripts.cometchatpulse.demo.Presenters.GroupDetailActivityPresenter;
import com.inscripts.cometchatpulse.demo.Utils.FontUtils;
import com.inscripts.cometchatpulse.demo.Utils.MediaUtils;
import com.cometchat.pro.models.User;

public class GroupDetailActivity extends AppCompatActivity implements View.OnClickListener,
        GroupDetailActivityContract.GroupDetailView {

    private TextView toolbarSubtitle;

    private ImageView groupImage;

    private GroupDetailActivityContract.GroupDetailPresenter groupDetailPresenter;

    private CollapsingToolbarLayout collapsingToolbar;

    private String groupId;

    private String groupName;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private Bundle bundle;

    private String ownerUid;

    private TextView tvGroupDescriptionLabel;

    private TextView tvGroupDescription;

    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_detail);
        new FontUtils(this);
        groupDetailPresenter = new GroupDetailActivityPresenter();
        groupDetailPresenter.attach(this);
        initViewComponent();
    }

    private void initViewComponent() {
        Toolbar toolbar = findViewById(R.id.toolbar);
//
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(10);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bundle = new Bundle();
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.primaryLightColor));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.primaryTextColor));
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.primaryLightColor));
        collapsingToolbar.setCollapsedTitleTypeface(FontUtils.robotoMedium);
        collapsingToolbar.setExpandedTitleTypeface(FontUtils.robotoRegular);
        collapsingToolbar.setExpandedTitleGravity(Gravity.START | Gravity.BOTTOM);

        tvGroupDescription = findViewById(R.id.tv_group_description);
        tvGroupDescription.setTypeface(FontUtils.robotoRegular);

        tvGroupDescriptionLabel = findViewById(R.id.group_description_labal);
        tvGroupDescriptionLabel.setTypeface(FontUtils.robotoMedium);

        toolbarSubtitle = findViewById(R.id.toolbarSubTitle);
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.container);
        groupImage = findViewById(R.id.ivGroupImage);
        groupDetailPresenter.handleIntent(getIntent(), this);


    }

    private void setViewPager() {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        MemberListFragment memberListFragment = new MemberListFragment();
        OutcastMemberListFragment outcastMemberListFragment = new OutcastMemberListFragment();

        bundle.putString(StringContract.IntentStrings.INTENT_GROUP_ID, groupId);
        bundle.putString(StringContract.IntentStrings.USER_ID, ownerUid);

        memberListFragment.setArguments(bundle);
        outcastMemberListFragment.setArguments(bundle);

        adapter.addFragment(memberListFragment, getString(R.string.member));
        adapter.addFragment(outcastMemberListFragment, "Ban Member");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        groupDetailPresenter.detach();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setGroupName(String groupName) {
        this.groupName = groupName;
//        tvGroupName.setText(this.groupName);
        collapsingToolbar.setTitle(this.groupName);

    }

    @Override
    public void setGroupId(String groupId) {
        this.groupId = groupId;
        setViewPager();
    }

    @Override
    public void setOwnerDetail(User user) {
        ownerUid = user.getUid();
    }


    @Override
    public void setGroupOwnerName(String owner) {
        toolbarSubtitle.setText(getString(R.string.created_by)+" " + owner);
    }

    @Override
    public void setGroupIcon(String icon) {

        if (icon == null || icon.isEmpty()) {
            Drawable drawable = getResources().getDrawable(R.drawable.cc_ic_group);
            Bitmap bitmap = null;
            try {
                bitmap = MediaUtils.getPlaceholderImage(this, drawable);
                groupImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            groupDetailPresenter.setIcon(this, icon, groupImage);
        }
    }

    @Override
    public void setGroupDescription(String description) {
        if (description != null) {
            tvGroupDescriptionLabel.setVisibility(View.VISIBLE);
            tvGroupDescription.setVisibility(View.VISIBLE);
            tvGroupDescription.setText(description);
        }
    }
}
