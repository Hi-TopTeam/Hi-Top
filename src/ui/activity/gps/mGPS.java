package ui.activity.gps;



import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;



public class mGPS {
	
	
	private LocationManager locManager;// 定义LocationManager对象
	
	
	Location currentLocation=null;
	private LocationListener gpsListener=null;
    private LocationListener networkListner=null;
	boolean locationFlag = true;
	Activity parent;
	NewGpsObtainFragment gpsfg;
	
	public  mGPS(Activity activity)
	{
		parent=activity;
		locManager = (LocationManager) parent.getSystemService(Context.LOCATION_SERVICE);
		Location location = locManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

	// Location
		location=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		//updateGpsView(location);
		
	  //networkListner=new MyLocationListner();
      //locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, networkListner);
		gpsListener=new MyLocationListner();
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, gpsListener);
	}
	public void setNewGpsObtainFragment(NewGpsObtainFragment fg)
	{
		gpsfg=fg;
		//gpsfg.setLocation(currentLocation);
	}
	public Location getLocation()
	{
		Location location = locManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

	// Location
		location=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		return location;
	}
	private class MyLocationListner implements LocationListener{
		
	    @Override
	    public void onLocationChanged(Location location) {
	        // Called when a new location is found by the location provider.
	        if(currentLocation!=null){
	            if(isBetterLocation(location, currentLocation)){
	                currentLocation=location;
	            }
	        }
	        else{
	            currentLocation=location;
	        }
	        //updateGpsView(location);
	        if(gpsfg!=null){
	        	gpsfg.setLocation(location);
	        	gpsfg.updateGpsView(location);
	        }
	        //移除基于LocationManager.NETWORK_PROVIDER的监听器
	        if(LocationManager.NETWORK_PROVIDER.equals(location.getProvider())){
	        	locManager.removeUpdates(networkListner);
	        	networkListner = null;
	        }
	    }

	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    }

	    public void onProviderEnabled(String provider) {
	    }

	    public void onProviderDisabled(String provider) {
	    }
	};
	
	private static final int CHECK_INTERVAL = 1000 * 30;
    protected boolean isBetterLocation(Location location,
            Location currentBestLocation) {
        if (currentBestLocation == null) {
            // 如果没有得到当前最好的位置 则新得到的位置就是最好的位置
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > CHECK_INTERVAL;
        boolean isSignificantlyOlder = timeDelta < -CHECK_INTERVAL;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location,
        // use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must
            // be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
                .getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate
                && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

}
