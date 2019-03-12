package com.inscripts.cometchatpulse.demo.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;


public class BlurTransformation extends BitmapTransformation {

    private RenderScript rs;

    public BlurTransformation(Context context) {
        rs=RenderScript.create(context);
    }


    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {

        // Allocate memory for Renderscript to work with

        Bitmap outputBitmap=Bitmap.createBitmap(toTransform);

        Allocation input = Allocation.createFromBitmap(rs, toTransform);
        Allocation output = Allocation.createFromBitmap(rs, outputBitmap);

        // Load up an instance of the specific script that we want to use.
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);
        // Set the blur radius
        script.setRadius(25f);

        // Start the ScriptIntrinisicBlur
        script.forEach(output);

        // Copy the output to the blurred bitmap
        output.copyTo(outputBitmap);


        return outputBitmap;


    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}
