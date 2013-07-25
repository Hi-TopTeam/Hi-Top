package domain.businessEntity.gps;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "T_ClimbData")
public class ClimbData {
	
	public ClimbData(){}
	//ID号 设置主键
	@DatabaseField(generatedId = true)
	private int climbID;
	
	//设置登山记录名称
	@DatabaseField(canBeNull = false)
	private String climbName;
	
	//开始高度
	@DatabaseField(canBeNull = true)
	private int startAltitude;
	
	//结束高度
	@DatabaseField(canBeNull = true)
	private int stopAltitude;
	

	//开始时间
	@DatabaseField(canBeNull = true)
	private Date startTime;
	
	//结束时间
	@DatabaseField(canBeNull = true)
	private Date stopTime;
	
	//经度
	@DatabaseField(canBeNull = true)
	private Double longitude;
	
	//纬度
	@DatabaseField(canBeNull = true)
	private Double latitude;
	
	
	public int getStartAltitude() {
		return startAltitude;
	}
	public void setStartAltitude(int startAltitude) {
		this.startAltitude = startAltitude;
	}
	public int getStopAltitude() {
		return stopAltitude;
	}
	public void setStopAltitude(int stopAltitude) {
		this.stopAltitude = stopAltitude;
	}
	public int getClimbID() {
		return climbID;
	}
	public void setClimbID(int climbID) {
		this.climbID = climbID;
	}
	public String getClimbName() {
		return climbName;
	}
	public void setClimbName(String climbName) {
		this.climbName = climbName;
	}

	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getStopTime() {
		return stopTime;
	}
	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

}
