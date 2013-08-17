package ui.activity.GoogleMap;

public class DetailToMap {
	private static String Marker=null;
	private static String strTime=null;
	public static void setMarker(String marker)
	{
		Marker=marker;
	}
	public static void setStrTime(String StrTime)
	{
		strTime=StrTime;
	}
	public static String getMarker()
	{
		return Marker;
	}
	public static String getStrTime()
	{
		return strTime;
	}
	
}
