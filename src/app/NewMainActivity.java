package app;


import java.util.List;

import ui.activity.gps.GpsObtainFragment.onLocateWeatherListener;
import ui.activity.weather.WeatherFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.DreamTeam.HiTop.R;
import com.viewpagerindicator.TabPageIndicator;

import domain.businessEntity.gps.ClimbData;
import domain.businessService.gps.ClimbDataService;
import domain.businessService.gps.IClimbDataService;

public class NewMainActivity extends FragmentActivity implements onLocateWeatherListener{
	public FragmentStatePagerAdapter adapter;
	public ViewPager pager;
	public TabPageIndicator indicator;
	private Thread mThread;
	private IClimbDataService dateService;
	List<ClimbData> list=null;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_newtab);
		adapter=new FragmentAdapter(getSupportFragmentManager());
		
		ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);
        
        indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
	}
	
	public void updateRecord()
	{
		
		adapter.notifyDataSetChanged();
		indicator.notifyDataSetChanged();
		
	}

	@Override
	public void onLocateLatAndLng(double Lat, double Lng) {
		// TODO Auto-generated method stub
		WeatherFragment weather=
				(WeatherFragment)getSupportFragmentManager().findFragmentById(R.id.weatherfragment);
		if(weather!=null)
		{
			weather.getLatAndLng(Lat, Lng);
		}
		else{
			WeatherFragment newFragment=new WeatherFragment();
			Bundle bundle=new Bundle();
			bundle.putDouble("Lat", Lat);
			bundle.putDouble("Lng", Lng);
			newFragment.setArguments(bundle);
			
		}
		
		
	}
	
	
	


}
