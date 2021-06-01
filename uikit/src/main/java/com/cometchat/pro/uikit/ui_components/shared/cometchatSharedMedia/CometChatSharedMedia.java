package com.cometchat.pro.uikit.ui_components.shared.cometchatSharedMedia;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.cometchat.pro.uikit.R;

import com.cometchat.pro.uikit.ui_components.shared.cometchatSharedMedia.adapter.TabAdapter;
import com.cometchat.pro.uikit.ui_components.shared.cometchatSharedMedia.fragments.CometChatSharedFiles;
import com.cometchat.pro.uikit.ui_components.shared.cometchatSharedMedia.fragments.CometChatSharedImages;
import com.cometchat.pro.uikit.ui_components.shared.cometchatSharedMedia.fragments.CometChatSharedVideos;
import com.google.android.material.tabs.TabLayout;

import com.cometchat.pro.uikit.ui_settings.FeatureRestriction;
import com.cometchat.pro.uikit.ui_resources.utils.Utils;


public class CometChatSharedMedia extends RelativeLayout {

    private Context context;

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private String Id;

    private String type;

    private AttributeSet attrs;

    private TabAdapter adapter;

    public CometChatSharedMedia(Context context) {
        super(context);
        this.context = context;
        initViewComponent(context,null,-1,-1);
    }

    public CometChatSharedMedia(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        this.context = context;
        initViewComponent(context,attrs,-1,-1);
    }

    public CometChatSharedMedia(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        initViewComponent(context,attrs,defStyleAttr,-1);
    }


    private void initViewComponent(Context context,AttributeSet attributeSet,int defStyleAttr,int defStyleRes){

        View view =View.inflate(context, R.layout.cometchat_shared_media,null);

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.SharedMediaView, 0, 0);
        addView(view);

        Bundle bundle = new Bundle();
        bundle.putString("Id",Id);
        bundle.putString("type",type);
        if (type!=null) {
            viewPager = this.findViewById(R.id.viewPager);
            tabLayout = view.findViewById(R.id.tabLayout);
            adapter = new TabAdapter(((FragmentActivity)context).getSupportFragmentManager());
            CometChatSharedImages images = new CometChatSharedImages();
            images.setArguments(bundle);
            adapter.addFragment(images, getResources().getString(R.string.images));
            CometChatSharedVideos videos = new CometChatSharedVideos();
            videos.setArguments(bundle);
            adapter.addFragment(videos, getResources().getString(R.string.videos));
            CometChatSharedFiles files = new CometChatSharedFiles();
            files.setArguments(bundle);
            adapter.addFragment(files, getResources().getString(R.string.files));
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(3);
            tabLayout.setupWithViewPager(viewPager);

            if (FeatureRestriction.getColor()!=null) {
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

            if(Utils.isDarkMode(context)) {
                tabLayout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.grey)));
                tabLayout.setTabTextColors(getResources().getColor(R.color.light_grey),getResources().getColor(R.color.textColorWhite));
            } else {
                tabLayout.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textColorWhite)));
                tabLayout.setTabTextColors(getResources().getColor(R.color.primaryTextColor),getResources().getColor(R.color.textColorWhite));
            }

        }
    }

    public void setRecieverId(String uid) {
        this.Id = uid;
    }

    public void setRecieverType(String receiverTypeUser) {
        this.type = receiverTypeUser;
    }
    public void reload() {
        initViewComponent(context,null,-1,-1);
    }
}
