package com.cometchat.pro.uikit.Reaction;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;

import com.cometchat.pro.uikit.R;
import com.cometchat.pro.uikit.Reaction.model.Reaction;

public class FragmentEmoji extends Fragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "FragmentEmoji";

    private OnEmojiClickListener mOnEmojiconClickedListener;
    private View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_emoji_objects, container, false);
        return this.mRootView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Reaction clickedReaction = (Reaction) parent.getItemAtPosition(position);
        if (this.mOnEmojiconClickedListener != null) {
            this.mOnEmojiconClickedListener.onEmojiClicked(clickedReaction);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void addEmojiconClickListener(OnEmojiClickListener listener) {
        this.mOnEmojiconClickedListener = listener;
    }
}