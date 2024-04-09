package com.cometchat.javasampleapp.fragments.shared.resources;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;

import com.cometchat.chatuikit.shared.resources.theme.CometChatTheme;
import com.cometchat.chatuikit.shared.resources.theme.Palette;
import com.cometchat.chatuikit.shared.resources.theme.Typography;
import com.cometchat.javasampleapp.AppUtils;
import com.cometchat.javasampleapp.R;
import com.cometchat.javasampleapp.activity.ComponentLaunchActivity;


public class ThemeFragment extends Fragment {
    Palette palette;
    @ColorInt
    int accent, background, primary;
    @StyleRes
    int heading, name, text1, text2;

    private LinearLayout parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
        palette = Palette.getInstance(getContext());
        parentView = view.findViewById(R.id.parent_view);
        setUpUI(view);
        setTheme(true);
        RadioGroup radioGroup = view.findViewById(R.id.toggle);
        radioGroup.setOnCheckedChangeListener((radio, i) -> {
            if (i == R.id.defaultTheme) {
                setTheme(true);
            } else if (i == R.id.customTheme) {
                setTheme(false);
            }

        });

        view.findViewById(R.id.button).setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ComponentLaunchActivity.class);
            intent.putExtra("component", R.id.userWithMessages);
            startActivity(intent);
        });

        return view;

    }

    private void setTheme(boolean isDefault) {

        if (isDefault) {
            accent = getResources().getColor(com.cometchat.chatuikit.R.color.cometchat_accent);
            background = getResources().getColor(R.color.white);
            primary = getResources().getColor(com.cometchat.chatuikit.R.color.cometchat_accent);
            heading = com.cometchat.chatuikit.R.style.Heading;
            name = R.style.Name;
            text1 = R.style.Text1;
            text2 = R.style.Text2;
        } else {
            accent = getResources().getColor(com.cometchat.chatuikit.R.color.cometchat_online_green);
            background = Color.parseColor("#021E20");
            primary = getResources().getColor(android.R.color.black);
            heading = R.style.AppHeading;
            name = R.style.AppName;
            text1 = R.style.AppText1;
            text2 = R.style.AppText2;
        }
        palette.background(background);
        palette.accent(accent);
        palette.primary(primary);
        palette.accent50(ColorUtils.setAlphaComponent(accent, 10));
        palette.accent100(ColorUtils.setAlphaComponent(accent, 20));
        palette.accent200(ColorUtils.setAlphaComponent(accent, 38));
        palette.accent300(ColorUtils.setAlphaComponent(accent, 61));
        palette.accent400(ColorUtils.setAlphaComponent(accent, 84));
        palette.accent500(ColorUtils.setAlphaComponent(accent, 117));
        palette.accent600(ColorUtils.setAlphaComponent(accent, 148));
        palette.accent700(ColorUtils.setAlphaComponent(accent, 176));
        palette.accent800(ColorUtils.setAlphaComponent(accent, 209));
        palette.secondary(getResources().getColor(com.cometchat.chatuikit.R.color.cometchat_secondary));
        palette.accent900(ColorUtils.setAlphaComponent(accent, 255));

        Typography typography = Typography.getInstance();
        typography.setHeading(heading);
        typography.setName(name);
        typography.setText1(text1);
        typography.setText2(text2);
        CometChatTheme cometChatTheme = CometChatTheme.getInstance(getContext());
        cometChatTheme.setPalette(palette);
        cometChatTheme.setTypography(typography);

    }

    private void setUpUI(View view) {
        if (AppUtils.isNightMode(getContext())) {
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.theme_text));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.theme_text_2));
            AppUtils.changeTextColorToWhite(getContext(),view.findViewById(R.id.theme_text_description));
            parentView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.app_background_dark)));
        } else {
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.theme_text));
            AppUtils.changeTextColorToBlack(getContext(),view.findViewById(R.id.theme_text_2));
            parentView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_background)));
        }
    }

}


