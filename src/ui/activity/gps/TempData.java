package ui.activity.gps;



public class TempData {
	public static boolean isEmpty;
	public static boolean flag;
	public static String cliName;
	public static int startAltitude;
	public static java.util.Date startTime;
	public static void clean()
	{
		isEmpty=true;
	}
	public static void load()
	{
		isEmpty=false;
	}
	public static boolean getEmpty()
	{
		return isEmpty;
	}
	public static boolean getFlag(){
		return flag;
	}
	public static String getCliName()
	{
		return cliName;
	}
	public static int getStartAltitude()
	{
		return startAltitude;
	}
	public static java.util.Date getStartTime()
	{
		return startTime;
	}
	
	public static void setFlag(boolean Flag){
		flag=Flag;
	}
	public static void setCliName(String CliName)
	{
		cliName=CliName;
	}
	public static void setStartAltitude(int StartAltitude)
	{
		startAltitude=StartAltitude;
	}
	public static void setStartTime(java.util.Date StartTime)
	{
		startTime=StartTime;
	}
}
