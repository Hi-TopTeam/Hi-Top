package domain.businessEntity.gps;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "T_AltitudeData")
public class AltitudeData {
	
	@DatabaseField(generatedId = true)
	private int dataID;
	
	@DatabaseField(canBeNull = false)
	private int Altitude;
	
	@DatabaseField(canBeNull = false)
	private String time;
	
	public AltitudeData(){
	}

	public int getDateID() {
		return dataID;
	}

	public void setDateID(int dataID) {
		this.dataID = dataID;
	}

	public int getAltitude() {
		return Altitude;
	}

	public void setAltitude(int altitude) {
		Altitude = altitude;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}
