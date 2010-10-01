package com.phonegap;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.webkit.WebView;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

/**
 * This class provides access to notifications on the device.
 */
public class Notification implements Plugin {
	
    WebView webView;					// WebView object
    DroidGap ctx;						// DroidGap object
	
	/**
	 * Constructor.
	 */
	public Notification() {
	}

	/**
	 * Sets the context of the Command. This can then be used to do things like
	 * get file paths associated with the Activity.
	 * 
	 * @param ctx The context of the main Activity.
	 */
	public void setContext(DroidGap ctx) {
		this.ctx = ctx;
	}

	/**
	 * Sets the main View of the application, this is the WebView within which 
	 * a PhoneGap app runs.
	 * 
	 * @param webView The PhoneGap WebView
	 */
	public void setView(WebView webView) {
		this.webView = webView;
	}

	/**
	 * Executes the request and returns CommandResult.
	 * 
	 * @param action The command to execute.
	 * @param args JSONArry of arguments for the command.
	 * @return A CommandResult object with a status and message.
	 */
	public PluginResult execute(String action, JSONArray args) {
		PluginResult.Status status = PluginResult.Status.OK;
		String result = "";		
		
		try {
			if (action.equals("beep")) {
				this.beep(args.getLong(0));
			}
			else if (action.equals("vibrate")) {
				this.vibrate(args.getLong(0));
			}
			else if (action.equals("alert")) {
				this.alert(args.getString(0),args.getString(1),args.getString(2));
			}
			return new PluginResult(status, result);
		} catch (JSONException e) {
			return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
		}
	}

	/**
	 * Identifies if action to be executed returns a value and should be run synchronously.
	 * 
	 * @param action	The action to execute
	 * @return			T=returns value
	 */
	public boolean isSynch(String action) {
		if(action.equals("alert"))
			return true;
		else
			return false;
	}

	/**
     * Called when the system is about to start resuming a previous activity. 
     */
    public void onPause() {
    }

    /**
     * Called when the activity will start interacting with the user. 
     */
    public void onResume() {
    }
    
    /**
     * Called by AccelBroker when listener is to be shut down.
     * Stop listener.
     */
    public void onDestroy() { 	
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it. 
     * 
     * @param requestCode		The request code originally supplied to startActivityForResult(), 
     * 							allowing you to identify who this result came from.
     * @param resultCode		The integer result code returned by the child activity through its setResult().
     * @param data				An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    }

    //--------------------------------------------------------------------------
    // LOCAL METHODS
    //--------------------------------------------------------------------------

	/**
	 * Beep plays the default notification ringtone.
	 * 
	 * @param count			Number of times to play notification
	 */
	public void beep(long count) {
		Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone notification = RingtoneManager.getRingtone(this.ctx, ringtone);
		
		// If phone is not set to silent mode
		if (notification != null) {
			for (long i = 0; i < count; ++i) {
				notification.play();
				long timeout = 5000;
				while (notification.isPlaying() && (timeout > 0)) {
					timeout = timeout - 100;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}
	
	/**
	 * Vibrates the device for the specified amount of time.
	 * 
	 * @param time			Time to vibrate in ms.
	 */
	public void vibrate(long time){
        // Start the vibration, 0 defaults to half a second.
		if (time == 0) {
			time = 500;
		}
        Vibrator vibrator = (Vibrator) this.ctx.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(time);
	}
	
	/**
	 * Builds and shows a native Android alert with given Strings
	 * @param message The message the alert should display
	 * @param title The title of the alert
	 * @param buttonLabel The label of the button 
	 */
	public synchronized void alert(String message,String title,String buttonLabel){
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
