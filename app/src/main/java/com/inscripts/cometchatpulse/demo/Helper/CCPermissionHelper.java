package com.inscripts.cometchatpulse.demo.Helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.inscripts.cometchatpulse.demo.Utils.Logger;



public class CCPermissionHelper {

    private static final String TAG = CCPermissionHelper.class.getSimpleName();

    // request codes for requested permissions
    public static final int CODE_IMAGE_UPLOAD = 11;
    public static final int CODE_VIDEO_UPLOAD = 12;
    public static final int CODE_AUDIO_UPLOAD = 13;
    public static final int CODE_CAPTURE_MEDIA = 14;
    public static final int CODE_AUDIO_CALL = 15;
    public static final int CODE_VIDEO_CALL = 16;
    public static final int CODE_AV_BROADCAST = 17;
    public static final int CODE_GROUP_AV_CONFERENCE = 18;
    public static final int CODE_GROUP_AUDIO_CONFERENCE = 19;
    public static final int CODE_AVBROADCAST = 20;
    public static final int CODE_ACCEPT_CALL = 21;
    public static final int CODE_PHONE_STATE = 22;
    public static final int CODE_PHONE_STORAGE = 23;

    // requested permissions
    public static final String REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String REQUEST_PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String REQUEST_PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String REQUEST_PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String REQUEST_PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS;
    public static final String REQUEST_PERMISSION_READ_CONTACTS = Manifest.permission.READ_CONTACTS;

    /**
     * method to check whether the permissions has been granted to the app
     * @param context the context_menu of the requesting component
     * @param permissions list of permissions requested
     * @return boolean whether app has permission or not
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null
                && permissions != null) {
            for (String permission : permissions) {
                Logger.error(TAG, " hasPermissions() : Permission : " + permission
                        + "checkSelfPermission : " + ActivityCompat.checkSelfPermission(context, permission));
                if (ActivityCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * method to request permissions
     * @param activity the activity requesting permissions
     * @param permission list of the requested permissions
     * @param requestCode int request code
     */
    public static void requestPermissions(Activity activity, String[] permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, permission, requestCode);
    }

}
