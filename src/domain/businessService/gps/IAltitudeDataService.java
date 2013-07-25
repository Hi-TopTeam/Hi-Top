package domain.businessService.gps;

import java.util.List;

import domain.businessEntity.gps.AltitudeData;

public interface IAltitudeDataService {
	public boolean addAltitudeData(AltitudeData data);
	
	public boolean deleteAltitudeData(String time);
	
	public List<AltitudeData> getAltitudeData(String time);
	

}
