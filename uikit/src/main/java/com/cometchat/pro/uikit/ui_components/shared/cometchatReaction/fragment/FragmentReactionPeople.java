package com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.cometchat.pro.uikit.R;

import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.model.Reaction;

import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.adapter.EmojiAdapter;
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.ReactionUtils;

public class FragmentReactionPeople extends FragmentReaction {

    public static final String TAG = "FragmentEmojiPeople";

    private View mRootView;
    private Reaction[] mData;
    private boolean mUseSystemDefault = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_emoji_objects, container, false);
        return this.mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        GridView gridView = (GridView) view.findViewById(R.id.Emoji_GridView);
        Bundle bundle = getArguments();

        if (bundle == null) {
            this.mData = ReactionUtils.getPeopleList();
            this.mUseSystemDefault = false;
        } else {
            Parcelable[] parcels = bundle.getParcelableArray(UIKitConstants.Emoji.EMOJI_KEY);
            this.mData = new Reaction[parcels.length];

            for (int i = 0; i < parcels.length; i++) {
                this.mData[i] = (Reaction) parcels[i];
            }

            this.mUseSystemDefault = bundle.getBoolean(UIKitConstants.Emoji.USE_SYSTEM_DEFAULT_KEY);
        }
        gridView.setAdapter(new EmojiAdapter(view.getContext(), this.mData));
        gridView.setOnItemClickListener(this);
    }
}