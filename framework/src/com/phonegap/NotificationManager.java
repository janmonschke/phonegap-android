package com.phonegap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class NotificationManager {
	Context ctx;
	
	public NotificationManager(Context ctx){
		this.ctx = ctx;
	}
	
	/**
	 * Builds and shows a native Android alert with given Strings
	 * @param message The message the alert should display
	 * @param title The title of the alert
	 * @param buttonLabel The label of the button 
	 */
	public void alert(String message,String title,String buttonLabel){
		AlertDialog.Builder dlg = new AlertDialog.Builder(this.ctx);
        dlg.setMessage(message);
        dlg.setTitle(title);
        dlg.setCancelable(false);
        dlg.setPositiveButton(buttonLabel,
        	new AlertDialog.OnClickListener() {
            	public void onClick(DialogInterface dialog, int which) {
            		dialog.dismiss();
            	}
        	});
        dlg.create();
        dlg.show();
	}
}
