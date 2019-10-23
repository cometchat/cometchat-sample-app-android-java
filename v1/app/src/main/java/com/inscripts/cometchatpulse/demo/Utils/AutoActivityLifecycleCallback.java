package com.inscripts.cometchatpulse.demo.Utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public abstract class AutoActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    private final Activity mTargetActivity;

    public AutoActivityLifecycleCallback(Activity var1) {
        this.mTargetActivity = var1;
    }

    public void onActivityCreated(Activity var1, Bundle var2) {
    }

    public void onActivityStarted(Activity var1) {
    }

    public void onActivityResumed(Activity var1) {
    }

    public void onActivityPaused(Activity var1) {
    }

    public void onActivityStopped(Activity var1) {
    }

    public void onActivitySaveInstanceState(Activity var1, Bundle var2) {
    }

    public void onActivityDestroyed(Activity var1) {
        if (var1 == this.mTargetActivity) {
            this.mTargetActivity.getApplication().unregisterActivityLifecycleCallbacks(this);
            this.onTargetActivityDestroyed();
        }

    }

    protected abstract void onTargetActivityDestroyed();
}
