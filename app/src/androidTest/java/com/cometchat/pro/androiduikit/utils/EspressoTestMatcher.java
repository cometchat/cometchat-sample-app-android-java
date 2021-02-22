package com.cometchat.pro.androiduikit.utils;

import android.content.Context;
import android.view.View;

import org.hamcrest.Matcher;

public class EspressoTestMatcher {
    public static Matcher<View> withDrawable(Context context, final int borderWidth, final int resourceId) {
        return new AvatarBorderMatcher(context,borderWidth,resourceId);
    }
}
