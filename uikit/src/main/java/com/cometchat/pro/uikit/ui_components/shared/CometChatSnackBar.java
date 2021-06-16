package com.cometchat.pro.uikit.ui_components.shared;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cometchat.pro.uikit.R;
import com.google.android.material.snackbar.Snackbar;

public class CometChatSnackBar {
    public static final String ERROR = "error";
    public static final String INFO = "info";
    public static final String WARNING = "warning";
    public static final String SUCCESS = "success";

    public static void show(Context context, View parentLayout, String message, String type) {
        customSnackBar(context,parentLayout,message,type);
    }
    private static void customSnackBar(Context context,View parentLayout,String message, String type) {
        Snackbar snackbar = Snackbar.make(parentLayout, "", Snackbar.LENGTH_INDEFINITE);
        // Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        snackbar.getView().setBackgroundColor(Color.TRANSPARENT);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.cometchat_dialog_layout, null, false);
//        builder.setView(dialogView);
        TextView messageTv = dialogView.findViewById(R.id.tv_message);
        messageTv.setText(message);
//        Dialog alertDialog = builder.create();
//        alertDialog.getWindow().setWindowAnimations(R.style.AppTheme_DialogAnimation);
        ImageView closeImage = dialogView.findViewById(R.id.iv_close);
        ImageView iconImage = dialogView.findViewById(R.id.iv_icon);
        if (type.equalsIgnoreCase(ERROR)) {
            iconImage.setImageResource(R.drawable.ic_baseline_error_24);
            iconImage.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textColorWhite)));
            closeImage.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textColorWhite)));
            messageTv.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.textColorWhite)));
            dialogView.setBackgroundColor(context.getResources().getColor(R.color.red));
        } else if (type.equalsIgnoreCase(INFO)) {
            snackbar.setDuration(Snackbar.LENGTH_LONG);
            iconImage.setImageResource(R.drawable.ic_info);
            iconImage.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textColorWhite)));
            closeImage.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textColorWhite)));
            messageTv.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.textColorWhite)));
            dialogView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else if (type.equalsIgnoreCase(WARNING)) {
            snackbar.setDuration(Snackbar.LENGTH_LONG);
            iconImage.setImageResource(R.drawable.ic_warning_image);
            iconImage.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textColorWhite)));
            closeImage.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textColorWhite)));
            messageTv.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.textColorWhite)));
            dialogView.setBackgroundColor(context.getResources().getColor(R.color.orange));
        } else {
            snackbar.setDuration(Snackbar.LENGTH_LONG);
            iconImage.setImageResource(R.drawable.ic_baseline_check_circle_24);
            iconImage.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textColorWhite)));
            closeImage.setImageTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.textColorWhite)));
            dialogView.setBackgroundColor(context.getResources().getColor(R.color.green_600));
            messageTv.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.textColorWhite)));
        }
        closeImage.setOnClickListener(v-> {
            snackbar.dismiss();
        });
        layout.addView(dialogView);
        View view = snackbar.getView();
        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snackbar.show();
    }
}
