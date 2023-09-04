package com.cometchat.javasampleapp.fragments.shared.resources;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cometchat.chatuikit.shared.resources.Localise.CometChatLocalize;
import com.cometchat.chatuikit.shared.resources.Localise.Language;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;
import com.cometchat.javasampleapp.activity.ComponentLaunchActivity;


public class LocalizeFragment extends Fragment {
    LinearLayout parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_localize, container, false);
        TextView localizeIntro = view.findViewById(R.id.localizeOverview);
        TextView language = view.findViewById(R.id.language);
        AppCompatButton button = view.findViewById(R.id.button);
        RadioGroup radioGroup = view.findViewById(R.id.toggle);
        parentView = view.findViewById(R.id.parent_view);
        setUpUI(view);
        if (getContext() != null)
            CometChatLocalize.setLocale(getContext(), Language.Code.en);
        radioGroup.setOnCheckedChangeListener((radio, i) -> {
            if (getContext() != null) {
                if (i == R.id.english) {
                    localizeIntro.setText(getResources().getString(R.string.localize_english_overview));
                    language.setText(getResources().getString(R.string.language_english));
                    button.setText(getResources().getString(R.string.view_english));
                    CometChatLocalize.setLocale(getContext(), Language.Code.en); //Localize UiKit to English
                } else if (i == R.id.hindi) {
                    localizeIntro.setText(getResources().getString(R.string.localize_hindi_overview));
                    language.setText(getResources().getString(R.string.language_hindi));
                    button.setText(getResources().getString(R.string.view_hindi));
                    CometChatLocalize.setLocale(getContext(), Language.Code.hi); //Localize UiKit to Hindi
                }
            }
        });
        view.findViewById(R.id.button).setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ComponentLaunchActivity.class);
            intent.putExtra("component", R.id.conversationWithMessages);
            startActivity(intent);
        });

        return view;
    }

    private void setUpUI(View view) {
        if (AppUtils.isNightMode(getContext())) {
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.localize_text));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.localizeOverview));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.language));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.app_background_dark)));
        } else {
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.localize_text));
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.language));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
        }
    }
}