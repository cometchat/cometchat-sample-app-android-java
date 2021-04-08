package com.cometchat.pro.uikit.ui_components.shared.cometchatReaction;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.cometchat.pro.uikit.R;

import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.fragment.FragmentReaction;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.fragment.FragmentReactionNature;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.fragment.FragmentReactionObject;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.fragment.FragmentReactionPeople;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.fragment.FragmentReactionPlaces;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.listener.OnReactionClickListener;
import com.cometchat.pro.uikit.ui_components.shared.cometchatReaction.model.Reaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CometChatReactionDialog extends DialogFragment implements OnReactionClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    private FrameLayout frameLayout;

    private OnReactionClickListener emojiClickListener;

    // CONSTRUCTOR
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.getDialog().setCanceledOnTouchOutside(true);
        return inflater.inflate(R.layout.cometchat_reaction_window, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        frameLayout = view.findViewById(R.id.frame);
        bottomNavigationView = view.findViewById(R.id.reaction_bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        FragmentReaction fragmentEmoji = new FragmentReactionPeople();
        fragmentEmoji.addEmojiconClickListener(emojiClickListener);
        loadFragment(fragmentEmoji);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
//        d.getWindow().getAttributes().windowAnimations = R.style.SlideUpDialogAnimation;
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setCanceledOnTouchOutside(false);
        return d;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getChildFragmentManager().beginTransaction().replace(frameLayout.getId(), fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public void onEmojiClicked(Reaction emojicon) {
        emojiClickListener.onEmojiClicked(emojicon);
    }

    public void setOnEmojiClick(OnReactionClickListener emojiClickListener) {
        this.emojiClickListener = emojiClickListener;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        FragmentReaction fragment = null;
        if (itemId == R.id.menu_people) {
            fragment = new FragmentReactionPeople();
        } else if (itemId == R.id.menu_places) {
            fragment = new FragmentReactionPlaces();
        } else if (itemId == R.id.menu_nature) {
            fragment = new FragmentReactionNature();
        } else if (itemId == R.id.menu_object) {
            fragment = new FragmentReactionObject();
        }
        fragment.addEmojiconClickListener(emojiClickListener);
        return loadFragment(fragment);
    }
}
