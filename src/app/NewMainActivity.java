package app;


import java.util.List;

import ui.activity.GoogleMap.NewGMapFragment;


import ui.activity.gps.mGPS;
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

public class NewMainActivity extends FragmentActivity {
	public FragmentStatePagerAdapter adapter;
	public ViewPager pager;
	public TabPageIndicator indicator;
	List<ClimbData> list=null;
	public mGPS gps;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_newtab);
		gps=new mGPS(this);
		adapter=new FragmentAdapter(getSupportFragmentManager(),gps);//
		
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

	


}
