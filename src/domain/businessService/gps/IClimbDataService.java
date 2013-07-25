package domain.businessService.gps;

import java.util.List;

import domain.businessEntity.gps.ClimbData;

public interface IClimbDataService {
	//添加登山记录
	public boolean addClimbData(ClimbData data);
	
	//删除登山记录
	public boolean deleteCilmbDataByID(int climbId);
	
	//删除全部记录
	public boolean deleteAll();
	
	//获取登山记录
	public List<ClimbData> getClimbData();
	
	//根据ID获取登山记录
	public ClimbData getClimbDataById(int climbId);
	
	
}
