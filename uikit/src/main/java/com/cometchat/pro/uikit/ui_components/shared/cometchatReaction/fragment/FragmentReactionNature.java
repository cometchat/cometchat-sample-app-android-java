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
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.ReactionUtils;

public class FragmentReactionNature extends FragmentReaction {

    public static final String TAG = "FragmentEmojiNature";

    private View mRootView;
    private Reaction[] mData;
    private boolean mUseSystemDefault = false;

    private static final String USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults";
    private static final String EMOJI_KEY = "emojic";

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
            mData = ReactionUtils.getNatureList();
            mUseSystemDefault = false;
        } else {
            Parcelable[] parcels = bundle.getParcelableArray(EMOJI_KEY);
            mData = new Reaction[parcels.length];
            for (int i = 0; i < parcels.length; i++) {
                mData[i] = (Reaction) parcels[i];
            }
            mUseSystemDefault = bundle.getBoolean(USE_SYSTEM_DEFAULT_KEY);
        }
        gridView.setAdapter(new EmojiAdapter(view.getContext(), mData));
        gridView.setOnItemClickListener(this);
    }
}
