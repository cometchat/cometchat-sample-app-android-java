package com.inscripts.cometchatpulse.demo.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.inscripts.cometchatpulse.demo.R;

public class PreferenceHelper {


    private static Context context;

    private static SharedPreferences sharedPreferences;

    private static SharedPreferences.Editor editor;

    public static void init(Context ctx){
        context=ctx;
        sharedPreferences=context.getSharedPreferences(context.getResources().getString(R.string.app_name),Context.MODE_PRIVATE);
         editor=sharedPreferences.edit();
    }


    public static void saveString(String key,String value){
        editor.putString(key,value);
        editor.apply();
    }


    public static String getString(String key){
        if (sharedPreferences.getString(key,null)!=null){

            return sharedPreferences.getString(key,null);
        }
        return null;
    }


}
