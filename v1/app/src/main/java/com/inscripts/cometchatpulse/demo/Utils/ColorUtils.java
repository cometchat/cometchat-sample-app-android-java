package com.inscripts.cometchatpulse.demo.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import com.inscripts.cometchatpulse.demo.R;

import java.util.concurrent.ThreadLocalRandom;

public class ColorUtils {


    public static int getMaterialColor(Context context) {
        int materialColor = 0;
        try {


            materialColor = Color.WHITE;

            TypedArray colors = context.getResources().obtainTypedArray(R.array.material_color);

            materialColor = colors.getColor(getRandomIndex(), Color.WHITE);
            colors.recycle();

            return materialColor;

        } catch (StringIndexOutOfBoundsException se) {

            return context.getResources().getColor(R.color.secondaryDarkColor);
        }

    }

    public static void setStatusBarColor(Activity var0, int var1) {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Window var2 = var0.getWindow();
                var2.addFlags(-2147483648);
                var2.setStatusBarColor(var1);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }


    private static int getRandomIndex() {
        return ThreadLocalRandom.current().nextInt(0, 80 + 1);
    }
}
