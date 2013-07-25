package domain.businessService.gps;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import android.util.Log;

import domain.businessEntity.gps.LatLngData;
import foundation.data.DataContext;
import foundation.data.IDataContext;

public class LatLngDataService implements ILatLngDataService{
	private static String tag="LatLngDataService";
	private IDataContext ctx = null;
	public LatLngDataService(){
		ctx = new DataContext();
	}

	@Override
	public boolean addLatLngData(LatLngData data) {
		try {
			ctx.add(data, LatLngData.class, Integer.class);
			return true;
		} catch (SQLException e) {
			
			Log.e(tag, e.toString());
		}
		return false;
	}

	@Override
	public boolean deleteAll() {
		try {
			ctx.deleteAll(LatLngData.class, Integer.class);
			return true;
		} catch (SQLException e) {
			Log.e(tag,e.toString());
		}
		return false;
	}
	

	@Override
	public boolean deleteByDate(String time) {
		DeleteBuilder<LatLngData, Integer> db;
		try {
			db = ctx.getDeleteBuilder(LatLngData.class, int.class);
			db.where().eq("time", time);
			ctx.delete(LatLngData.class, int.class, db.prepare());
		} catch (SQLException e) {
			Log.e(tag, e.toString());
		}
		
		
		return false;
	}

	@Override
	public List<LatLngData> getLatLngDataByTime(String time) {
		try {
			QueryBuilder<LatLngData,Integer> qb = ctx.getQueryBuilder(LatLngData.class, int.class);
			qb.where().eq("time", time);
			List<LatLngData> qbDataList = ctx.query(LatLngData.class, int.class, qb.prepare());
			return qbDataList;
		} catch (SQLException e) {
			Log.e(tag,e.toString());
		}
		return null;
	}

}
