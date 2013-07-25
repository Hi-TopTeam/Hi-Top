package domain.businessService.gps;

import java.sql.SQLException;
import java.util.List;

import ui.viewModel.gps.RecDetailViewModel;

import com.j256.ormlite.stmt.QueryBuilder;


import android.util.Log;

import domain.businessEntity.gps.ClimbData;
import foundation.data.DataContext;
import foundation.data.IDataContext;

public class ClimbDataService implements IClimbDataService {
	
	private static String Tag="GpsDataService";
	
	private  RecDetailViewModel    viewModel;
	private IDataContext ctx=null;
	public ClimbDataService(){
		ctx= new DataContext();
	}
	
	//添加数据
	@Override
	public boolean addClimbData(ClimbData data) {
		try {
			ctx.add(data, ClimbData.class, int.class);
			return true;
		} catch (SQLException e) {
			Log.e(Tag, e.toString());
		}
		return false;
	}
	//根据ID删除数据
	@Override
	public boolean deleteCilmbDataByID(int climbId) {
		// TODO Auto-generated method stub
		try {
			ctx.deleteById(climbId, ClimbData.class, int.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.e(Tag, e.toString());
		}
		return false;
	}
	
	//删除所有数据
	@Override
	public boolean deleteAll() {
		try {
			ctx.deleteAll(ClimbData.class, int.class);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.e(Tag, e.toString());
		}
		return false;
	}
	
	//获取数据
	@Override
	public List<ClimbData> getClimbData() {
		// TODO Auto-generated method stub
		try {
			List<ClimbData> dateList=ctx.queryForAll(ClimbData.class, int.class);
			return dateList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.e(Tag, e.toString());
		}
		return null;
	}
	
	//根据ID获取数据
	
	public ClimbData getClimbDataById(int climbId) {
		// TODO Auto-generated method stub
		ClimbData climbdata = new ClimbData();
		try{
			 climbdata = ctx.queryById(ClimbData.class, int.class, climbId);
			 return climbdata;
			 
		}catch (Exception e) {
			// TODO: handle exception
			Log.e(Tag, e.toString());
		}
		return null;	
	}
}
