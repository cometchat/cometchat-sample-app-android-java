package com.inscripts.cometchatpulse.demo.Helper;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.inscripts.cometchatpulse.demo.R;
import com.inscripts.cometchatpulse.demo.Utils.Logger;

import java.io.IOException;

public class OutgoingAudioHelper {

    private static final String TAG = "OutgoingAudioHelper";

    private Type type;

    public enum Type {
        IN_COMMUNICATION,
        RINGING,
    }

    private final Context context;

    private android.os.Handler handler=new android.os.Handler();

    private MediaPlayer mediaPlayer;


    public OutgoingAudioHelper(@NonNull Context context) {
        this.context = context;

    }

    public void start(final Type type) {
        int soundId;
        this.type=type;
        if (type == Type.IN_COMMUNICATION || type == Type.RINGING) soundId = R.raw.ring;
        else throw new IllegalArgumentException("Not a valid sound type");

        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        mediaPlayer.setLooping(true);
        String packageName = context.getPackageName();
        Uri dataUri = Uri.parse("android.resource://" + packageName + "/" + soundId);

        try {
            mediaPlayer.setDataSource(context, dataUri);
            mediaPlayer.prepare();

            mediaPlayer.start();

        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            Logger.error(TAG, e.getMessage());
        }

    }

    public void stop() {
        if (mediaPlayer == null) return;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
