package ui.activity.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 
 * @author szp
 *
 */
public class LatLngReceiver extends BroadcastReceiver {
	private static double lat;
	private static double lng;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		lat = intent.getExtras().getDouble("Lat");
		lng = intent.getExtras().getDouble("Lng");
	}
	public static double getLat(){
		return lat;
	}
	public static double getLng(){
		return lng;
	}

}
