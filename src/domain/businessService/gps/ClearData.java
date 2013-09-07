package domain.businessService.gps;

import java.sql.SQLException;

import android.R.integer;
import domain.businessEntity.Community.FriendsData;
import domain.businessEntity.Community.GroupsData;
import domain.businessEntity.gps.AltitudeData;
import domain.businessEntity.gps.ClimbData;
import domain.businessEntity.gps.LatLngData;
import foundation.data.DataContext;
import foundation.data.IDataContext;

public class ClearData{
	private IDataContext ctx=null;
	public ClearData(){
		ctx= new DataContext();
	}

	public boolean clearAllData() {
		try {
			ctx.deleteAll(AltitudeData.class,Integer.class);
			ctx.deleteAll(ClimbData.class,Integer.class);
			ctx.deleteAll(LatLngData.class, Integer.class);
			ctx.deleteAll(FriendsData.class,Integer.class);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}	
	}

}
