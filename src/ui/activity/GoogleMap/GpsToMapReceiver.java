package ui.activity.GoogleMap;

public class GpsToMapReceiver {
	private static boolean status=false;
	private static String startTime=null;
	public static void setStatus(boolean sta)
	{
		status=sta;
	}
	public static void setTime(String stTime)
	{
		startTime=stTime;
	}
	public static boolean getStatus()
	{
		return status;
	}
	public static String getStartTime()
	{
		return startTime;
	}
}
