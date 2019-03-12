package com.inscripts.cometchatpulse.demo.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.provider.MediaStore;

import com.inscripts.cometchatpulse.demo.ViewHolders.LeftImageVideoViewHolder;
import com.inscripts.cometchatpulse.demo.ViewHolders.RightImageVideoViewHolder;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class MediaUtils {


    private static Bitmap getBitmapFromDrawable(Context context,Drawable drawable)
    {
        Bitmap bitmap;

        bitmap=Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);

        Canvas canvas=new Canvas(bitmap);
        drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap getPlaceholderImage(Context context,Drawable drawable)
    {
        Bitmap bitmap=getBitmapFromDrawable(context,drawable);

        Bitmap outputImage=Bitmap.createBitmap(bitmap.getWidth()+80,bitmap.getHeight()+80,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(outputImage);
        canvas.drawARGB(0,0,0,0);
        canvas.drawBitmap(bitmap,40,40,null);
        bitmap=outputImage;

        return bitmap;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String ImagePath(Uri uri, Context context) {
        String path = "";
        if (context.getContentResolver() != null) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public static String getVideoPath(Uri uri, Context context) {
        String path = "";
        if (context.getContentResolver() != null) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    public  static  <P> void getVideoThumbnail(P view, String url) throws Throwable {

        Bitmap bitmap = null;
        Bitmap thumbNail=MediaUtils.getVideoThumbnail(url);
        try {
            if ( thumbNail!= null) {
                if (view instanceof RightImageVideoViewHolder) {
                    bitmap = Bitmap.createScaledBitmap(thumbNail, ((RightImageVideoViewHolder) view).imageMessage.getWidth(),
                            ((RightImageVideoViewHolder) view).imageMessage.getHeight(), false);
                    ((RightImageVideoViewHolder) view).imageMessage.setImageBitmap(bitmap);
                }
                if (view instanceof LeftImageVideoViewHolder) {
                    bitmap = Bitmap.createScaledBitmap(thumbNail, ((LeftImageVideoViewHolder) view).imageMessage.getWidth(),
                            ((LeftImageVideoViewHolder) view).imageMessage.getHeight(), false);
                    ((LeftImageVideoViewHolder) view).imageMessage.setImageBitmap(bitmap);
                }
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    public static Camera openFrontCam()
    {
        int camCount=0;
        Camera camera=null;
        Camera.CameraInfo cameraInfo=new Camera.CameraInfo();
        camCount=Camera.getNumberOfCameras();
        for (int i = 0; i < camCount; i++) {
            Camera.getCameraInfo(i,cameraInfo);
            if (cameraInfo.facing==Camera.CameraInfo.CAMERA_FACING_FRONT)
            {
                try {
                    camera=Camera.open(i);
                    camera.setDisplayOrientation(90);
                }catch (RuntimeException re)
                {

                }
            }
        }
        return camera;
    }

    public static void vibrate(Context context)
    {
        Vibrator vibrator= (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);

    }


    public static void playSendSound(Context context ,int ringId) {
        MediaPlayer mMediaPlayer = MediaPlayer.create(context, ringId);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });

    }

    public static Bitmap getVideoThumbnail(String mediaFile) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(mediaFile, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(mediaFile);

            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in getVideoThumbnail(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;

    }
}
