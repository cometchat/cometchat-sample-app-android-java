package com.cometchat.pro.uikit.ui_components.calls.call_list;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.MessagesRequest;
import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.ui_components.shared.cometchatCalls.CometChatCalls;
import com.cometchat.pro.uikit.ui_resources.utils.CometChatError;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import com.cometchat.pro.uikit.ui_resources.utils.item_clickListener.OnItemClickListener;

import com.cometchat.pro.uikit.ui_settings.FeatureRestriction;

import com.cometchat.pro.uikit.ui_resources.utils.Utils;

/**
 * * Purpose - CometChatCallList class is a activity used to display list of calls recieved to user and perform certain action on click of item.
 *              It also consist of two tabs <b>All</b> and <b>Missed Call</b>.
 *
 *   Created on - 23rd March 2020
 *
 *   Modified on  - 24th March 2020
 *
**/

public class CometChatCallList extends Fragment {

    private CometChatCalls rvCallList;

    private MessagesRequest messageRequest;    //Uses to fetch Conversations.

    private static OnItemClickListener events;

    private TextView tvTitle;

    private ShimmerFrameLayout conversationShimmer;

    private static final String TAG = "CallList";

    private View view;

    private List<Call> callList = new ArrayList<>();

    private TabAdapter tabAdapter;

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private ImageView phoneAddIv;

    private boolean oneOnoneCallEnabled;

    private boolean oneOnoneVideoCallEnabled;

    public CometChatCallList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cometchat_calls, container, false);
        tvTitle = view.findViewById(R.id.tv_title);
        fetchSettings();
        CometChatError.init(getContext());
        phoneAddIv = view.findViewById(R.id.add_phone_iv);
        if (oneOnoneCallEnabled || oneOnoneVideoCallEnabled)
            phoneAddIv.setVisibility(View.VISIBLE);
        else
            phoneAddIv.setVisibility(View.GONE);

        phoneAddIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserListScreen();
            }
        });
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        if (getActivity() != null) {
            tabAdapter = new TabAdapter(getActivity().getSupportFragmentManager());
            tabAdapter.addFragment(new AllCall(), getContext().getResources().getString(R.string.all));
            tabAdapter.addFragment(new MissedCall(), getContext().getResources().getString(R.string.missed));
            viewPager.setAdapter(tabAdapter);
        }
        tabLayout.setupWithViewPager(viewPager);
        if (FeatureRestriction.getColor()!=null) {
            phoneAddIv.setImageTintList(ColorStateList.valueOf(Color.parseColor(FeatureRestriction.getColor())));
            Drawable wrappedDrawable = DrawableCompat.wrap(getResources().
                    getDrawable(R.drawable.tab_layout_background_active));
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(FeatureRestriction.getColor()));
            tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).view.setBackground(wrappedDrawable);
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(FeatureRestriction.getColor()));
        } else {
            tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (FeatureRestriction.getColor()!=null) {
                    Drawable wrappedDrawable = DrawableCompat.wrap(getResources().
                            getDrawable(R.drawable.tab_layout_background_active));
                    DrawableCompat.setTint(wrappedDrawable, Color.parseColor(FeatureRestriction.getColor()));
                    tab.view.setBackground(wrappedDrawable);
                }
                else
                    tab.view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        checkDarkMode();
        return view;
    }

    private void fetchSettings() {
        FeatureRestriction.isOneOnOneAudioCallEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                oneOnoneCallEnabled = booleanVal;
            }
        });
        FeatureRestriction.isOneOnOneVideoCallEnabled(new FeatureRestriction.OnSuccessListener() {
            @Override
            public void onSuccess(Boolean booleanVal) {
                oneOnoneVideoCallEnabled = booleanVal;
            }
        });
    }

    private void checkDarkMode() {
        if(Utils.isDarkMode(getContext())) {
            tvTitle.setTextColor(getResources().getColor(R.color.textColorWhite));
            tabLayout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
            tabLayout.setTabTextColors(getResources().getColor(R.color.textColorWhite),getResources().getColor(R.color.light_grey));
        } else {
            tvTitle.setTextColor(getResources().getColor(R.color.primaryTextColor));
            tabLayout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
            tabLayout.setTabTextColors(getResources().getColor(R.color.primaryTextColor),getResources().getColor(R.color.textColorWhite));
        }

    }

    private void openUserListScreen() {
        Intent intent = new Intent(getContext(), CometChatNewCallList.class);
        startActivity(intent);
    }

}
