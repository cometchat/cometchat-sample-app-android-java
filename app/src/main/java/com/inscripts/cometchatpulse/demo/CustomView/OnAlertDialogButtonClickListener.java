/*

cc
Copyright (c) 2016 Inscripts
License: https://www.cometchat.com/legal/license

*/
package com.inscripts.cometchatpulse.demo.CustomView;

import android.app.AlertDialog;
import android.view.View;

public interface OnAlertDialogButtonClickListener {
	public void onButtonClick(AlertDialog alertDialog, View v, int which, int popupId);
}
