package app;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.SupportMapFragment;

import domain.businessEntity.gps.ClimbData;
import domain.businessService.gps.ClimbDataService;
import domain.businessService.gps.IClimbDataService;
import ui.activity.GoogleMap.NewGMapFragment;
import ui.activity.gps.GpsObtainFragment;
import ui.activity.gps.RecordFragment;
import ui.activity.weather.WeatherFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

public class FragmentAdapter extends FragmentStatePagerAdapter {
	private static final String[] CONTENT = new String[] { "GPS", "历史记录", "天气", "地图"};//,"社区","更多"
	private int mCount=CONTENT.length;
	private IClimbDataService dateService;
	private ArrayList<Fragment> fragments; 
	private FragmentManager fm;
	List<ClimbData> list;
	

	public FragmentAdapter(FragmentManager fm) {
		
		super(fm);
		
		
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		if(super.instantiateItem(container, position).getClass()==RecordFragment.class)
		{
			RecordFragment rf= (RecordFragment) super.instantiateItem(container, position);
			dateService=new ClimbDataService();
			list=dateService.getClimbData();
			rf.setDate(list);
			
			return rf;
		}
		
		return super.instantiateItem(container, position);
		
		
	}
	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0%CONTENT.length){
		case 0:
			return GpsObtainFragment.newInstance();
		case 1: 
			return RecordFragment.newInstance();
		case 2:
			return WeatherFragment.newInstance();
		case 3:
			return NewGMapFragment.newInstance();
			
			
		}
		return null;
		
		
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return CONTENT.length;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return CONTENT[position%CONTENT.length];
	}
	
	public void setmCount(int mCount) {
		if(mCount>0&&mCount<10)
		{
			this.mCount = mCount;
			notifyDataSetChanged();
		}
	}
	
	
	
	

}
