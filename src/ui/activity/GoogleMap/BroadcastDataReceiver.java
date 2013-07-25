package ui.activity.GoogleMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastDataReceiver extends BroadcastReceiver {
	private static boolean status;
	private static String startTime;
	@Override
	public void onReceive(Context context, Intent intent) {
		status = intent.getExtras().getBoolean("status");
		startTime = intent.getExtras().getString("startTime");
	}
	public static boolean getStatus(){
		return status;
	}
	public static String getStartTime(){
		return startTime;
	}

}
