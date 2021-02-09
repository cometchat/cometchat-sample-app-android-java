package com.cometchat.pro.uikit.ui_resources.utils.custom_alertDialog;

import android.view.View;

import androidx.appcompat.app.AlertDialog;

public interface OnAlertDialogButtonClickListener {
	void onButtonClick(AlertDialog alertDialog, View v, int which, int popupId);
}