package ui.viewModel.gps;

import java.util.Date;
import java.util.List;

import domain.businessEntity.gps.ClimbData;

import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;

public class RecDetailViewModel extends ViewModel {
	private String ClimbName;
	private int startAltitude;
	private int stopAltitude;
	private Date startTime;
	private Date stopTime;
	private String longitude;
	private String latitude;
	private ClimbData climbdata;
	
	
	public ClimbData getClimbdata() {
		return climbdata;
	}
	public void setClimbdata(ClimbData climbdata) {
		this.climbdata = climbdata;
	}
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
	public String getClimbName() {
		return ClimbName;
	}
	public void setClimbName(String climbName) {
		ClimbName = climbName;
	}
	
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	@Override
	public List<ModelErrorInfo> verifyModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
