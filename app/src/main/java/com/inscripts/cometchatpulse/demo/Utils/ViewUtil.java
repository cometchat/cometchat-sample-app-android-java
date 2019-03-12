package com.inscripts.cometchatpulse.demo.Utils;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

public class ViewUtil {

    public static <T extends View> T findById(@NonNull View parent, @IdRes int resId) {
        return (T) parent.findViewById(resId);
    }
}
