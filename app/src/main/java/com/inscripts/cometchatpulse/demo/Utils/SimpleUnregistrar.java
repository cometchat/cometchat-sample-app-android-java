package com.inscripts.cometchatpulse.demo.Utils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import java.lang.ref.WeakReference;

public class SimpleUnregistrar implements Unregistrar {
    private WeakReference<Activity> mActivityWeakReference;
    private WeakReference<ViewTreeObserver.OnGlobalLayoutListener> mOnGlobalLayoutListenerWeakReference;

    public SimpleUnregistrar(Activity var1, ViewTreeObserver.OnGlobalLayoutListener var2) {
        this.mActivityWeakReference = new WeakReference(var1);
        this.mOnGlobalLayoutListenerWeakReference = new WeakReference(var2);
    }

    public void unregister() {
        Activity var1 = (Activity)this.mActivityWeakReference.get();
        ViewTreeObserver.OnGlobalLayoutListener var2 = (ViewTreeObserver.OnGlobalLayoutListener)this.mOnGlobalLayoutListenerWeakReference.get();
        if (null != var1 && null != var2) {
            View var3 = KeyboardVisibilityEvent.getActivityRoot(var1);
            if (Build.VERSION.SDK_INT >= 16) {
                var3.getViewTreeObserver().removeOnGlobalLayoutListener(var2);
            } else {
                var3.getViewTreeObserver().removeGlobalOnLayoutListener(var2);
            }
        }

        this.mActivityWeakReference.clear();
        this.mOnGlobalLayoutListenerWeakReference.clear();
    }
}
