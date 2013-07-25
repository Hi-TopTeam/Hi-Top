package domain.businessService.gps;

import java.util.Date;
import java.util.List;

import domain.businessEntity.gps.LatLngData;

public interface ILatLngDataService {
	public boolean addLatLngData(LatLngData data);
	public boolean deleteAll();
	public boolean deleteByDate(String time);
	public List<LatLngData> getLatLngDataByTime(String time);
	
	

}
