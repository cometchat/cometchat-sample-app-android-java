/*

cc
Copyright (c) 2016 Inscripts
License: https://www.cometchat.com/legal/license

*/
package com.inscripts.cometchatpulse.demo.Utils;

import android.util.Log;

public class Logger {

    public static boolean isDev(){
        return true;
    }
	public static void error(String message) {

        if(Logger.isDev())
		Log.e("CC^LOGS", message);
	}

	public static void debug(String message) {
		Log.d("CC^LOGS", message);
	}

	public static void info(String message) {
		Log.i("CC^LOGS", message);
	}

    public static void error(String TAG , String message) {
        if(Logger.isDev())
        Log.e(TAG, message);
    }

    public static void debug(String TAG , String message) {
        Log.d(TAG, message);
    }

    public static void info(String TAG , String message) {
        Log.i(TAG, message);
    }

    public static void errorLong(String TAG , String message){
        int maxLogSize = 1000;
        for(int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            Logger.error(TAG, message.substring(start, end));
        }
    }
}
