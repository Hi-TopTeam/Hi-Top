
package tool.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import domain.businessEntity.gps.ClimbData;



public class ClimbDataUtil {

	/**
	 * @param args
	 */
	
	
	private static final String startAltitude = null;
	private static final String stopAltitude = null;
	private static final String startTime = null;
	private static final String stopTime = null;
	
	private static final String longitude =null;
	private static final String latitude =null;
	private static Date date1;
	private static Date date2;
	
	public static Bundle writeCardtoBundle(ClimbData climbdat){
		
		
		Bundle bundle = new Bundle();
		
		bundle.putInt("startAltitude", climbdat.getStartAltitude());
		bundle.putInt("stopAltitude", climbdat.getStopAltitude());
		bundle.putString("startTime", climbdat.getStartTime().toString());
		bundle.putString("stopTime", climbdat.getStopTime().toString());
		bundle.putDouble("longitude", climbdat.getLongitude());
		bundle.putDouble("latitude", climbdat.getLatitude());
		
		return bundle;
	}
	
	public static ClimbData readCardFromBundle(Bundle bundle){
		
		ClimbData climbdat = new ClimbData();
		climbdat.setStartAltitude(bundle.getInt("startAltitude"));
		climbdat.setStopAltitude(bundle.getInt("stopAltitude"));
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:MI:SS");
		try {
			 date1 = sdf.parse(bundle.getString("startTime"));
			 date2 = sdf.parse(bundle.getString("stopTime"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		          
		climbdat.setStartTime(date1);
		
		climbdat.setStopTime(date2);
		climbdat.setLongitude(bundle.getDouble("longitude"));
		climbdat.setLatitude(bundle.getDouble("latitude"));
	
		return climbdat;
	}

	
	
	
}