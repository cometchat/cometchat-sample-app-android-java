package com.cometchat.pro.androiduikit.utils;

import android.util.Log;

import org.junit.Assert;

public class AssertHelper {
    static String TAG = "AssertHelper";
    public static void assertTrue(String desc,boolean condition) {
        try {
            Assert.assertTrue(desc,condition);
            Log.e(TAG, desc+" Success");
        } catch (Exception e) {
            Log.e(TAG, "Failed"+e.getMessage());
            Assert.fail(desc);
        }
    }

    public static void assertFailed(String desc) {
        try {
            Assert.fail(desc);
            Log.e(TAG, desc+" Failed");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
