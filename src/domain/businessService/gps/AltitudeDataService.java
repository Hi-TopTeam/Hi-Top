package domain.businessService.gps;

import java.sql.SQLException;
import java.util.List;

import android.util.Log;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import domain.businessEntity.gps.AltitudeData;
import domain.businessEntity.gps.LatLngData;
import foundation.data.DataContext;
import foundation.data.IDataContext;

public class AltitudeDataService implements IAltitudeDataService {

	private static String tag="AltitudeDataService";
	private IDataContext ctx = null;
	public AltitudeDataService(){
		ctx = new DataContext();
	}
	@Override
	public boolean addAltitudeData(AltitudeData data) {
		try {
			ctx.add(data, AltitudeData.class, Integer.class);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteAltitudeData(String time) {
		DeleteBuilder<AltitudeData, Integer> db;
		try {
			db = ctx.getDeleteBuilder(AltitudeData.class, int.class);
			db.where().eq("time", time);
			ctx.delete(AltitudeData.class, int.class, db.prepare());
		} catch (SQLException e) {
			Log.e(tag, e.toString());
		}
		return false;
	}

	@Override
	public List<AltitudeData> getAltitudeData(String time) {
		try {
			QueryBuilder<AltitudeData,Integer> qb = ctx.getQueryBuilder(AltitudeData.class, int.class);
			qb.where().eq("time", time);
			List<AltitudeData> qbDataList = ctx.query(AltitudeData.class, int.class, qb.prepare());
			return qbDataList;
		} catch (SQLException e) {
			Log.e(tag,e.toString());
		}
		return null;
	}

}
