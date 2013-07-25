package ui.viewModel.gps;

import java.util.Date;
import java.util.List;

import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;

public class RecordViewModel extends ViewModel {
	private String ClimbName;
	private Date starTime;
	
	public String getClimbName() {
		return ClimbName;
	}
	public void setClimbName(String climbName) {
		ClimbName = climbName;
	}
	public Date getStarTime() {
		return starTime;
	}
	public void setStarTime(Date starTime) {
		this.starTime = starTime;
	}
	@Override
	public List<ModelErrorInfo> verifyModel() {
		// TODO Auto-generated method stub
		return null;
	}

}
