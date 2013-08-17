package ui.activity.weather;

public class NewLatLngReceiver {
	private static  double lat;
	private  static double lng;
	
	public static void setLat(double Lat)
	{
		lat=Lat;
	}
	
	public static void setLng(double Lng)
	{
		lng=Lng;
	}
	
	public static double getLat()
	{
		return lat;
	}
	public static double getLng()
	{
		return lng;
	}

}
