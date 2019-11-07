package com.inscripts.cometchatpulse.demo.Utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class KeyboardVisibilityEvent {
    private static final int KEYBOARD_VISIBLE_THRESHOLD_DP = 100;

    public KeyboardVisibilityEvent() {
    }

    public static void setEventListener(Activity var0, KeyboardVisibilityEventListener var1) {
        final Unregistrar var2 = registerEventListener(var0, var1);
        var0.getApplication().registerActivityLifecycleCallbacks(new AutoActivityLifecycleCallback(var0) {
            protected void onTargetActivityDestroyed() {
                var2.unregister();
            }
        });
    }

    public static Unregistrar registerEventListener(final Activity var0, final KeyboardVisibilityEventListener var1) {
        if (var0 == null) {
            throw new NullPointerException("Parameter:activity must not be null");
        } else {
            int var2 = var0.getWindow().getAttributes().softInputMode;
            if (var1 == null) {
                throw new NullPointerException("Parameter:listener must not be null");
            } else {
                final View var3 = getActivityRoot(var0);
                ViewTreeObserver.OnGlobalLayoutListener var4 = new ViewTreeObserver.OnGlobalLayoutListener() {
                    private final Rect r = new Rect();
                    private final int visibleThreshold = Math.round(CommonUtils.dpToPx(var0, 100.0F));
                    private boolean wasOpened = false;

                    public void onGlobalLayout() {
                        var3.getWindowVisibleDisplayFrame(this.r);
                        int var1x = var3.getRootView().getHeight() - this.r.height();
                        boolean var2 = var1x > this.visibleThreshold;
                        if (var2 != this.wasOpened) {
                            this.wasOpened = var2;
                            var1.onVisibilityChanged(var2);
                        }
                    }
                };
                var3.getViewTreeObserver().addOnGlobalLayoutListener(var4);
                return new SimpleUnregistrar(var0, var4);
            }
        }
    }

    public static boolean isKeyboardVisible(Activity var0) {
        Rect var1 = new Rect();
        View var2 = getActivityRoot(var0);
        int var3 = Math.round(CommonUtils.dpToPx(var0, 100.0F));
        var2.getWindowVisibleDisplayFrame(var1);
        int var4 = var2.getRootView().getHeight() - var1.height();
        return var4 > var3;
    }

    static View getActivityRoot(Activity var0) {
        return ((ViewGroup) var0.findViewById(16908290)).getChildAt(0);
    }
}