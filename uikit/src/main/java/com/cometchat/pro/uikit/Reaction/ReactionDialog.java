package com.cometchat.pro.uikit.Reaction;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
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
import com.cometchat.pro.uikit.Reaction.fragment.FragmentEmojiNature;
import com.cometchat.pro.uikit.Reaction.fragment.FragmentEmojiObject;
import com.cometchat.pro.uikit.Reaction.fragment.FragmentEmojiPeople;
import com.cometchat.pro.uikit.Reaction.fragment.FragmentEmojiPlaces;
import com.cometchat.pro.uikit.Reaction.model.Reaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ReactionDialog extends DialogFragment implements OnEmojiClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    private FrameLayout frameLayout;

    private OnEmojiClickListener emojiClickListener;

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
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        FragmentEmoji fragmentEmoji = new FragmentEmojiPeople();
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

    public void setOnEmojiClick(OnEmojiClickListener emojiClickListener) {
        this.emojiClickListener = emojiClickListener;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        FragmentEmoji fragment = null;
        if (itemId == R.id.menu_people) {
            fragment = new FragmentEmojiPeople();
        } else if (itemId == R.id.menu_places) {
            fragment = new FragmentEmojiPlaces();
        } else if (itemId == R.id.menu_nature) {
            fragment = new FragmentEmojiNature();
        } else if (itemId == R.id.menu_object) {
            fragment = new FragmentEmojiObject();
        }
        fragment.addEmojiconClickListener(emojiClickListener);
        return loadFragment(fragment);
    }
}
