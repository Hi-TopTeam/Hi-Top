package app;




import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.DreamTeam.HiTop.R;
/**
 * 
 * @author GreamTeam 沈志鹏  郑运春
 *
 */
public class MainActivity extends TabActivity  implements OnCheckedChangeListener{

	public static final String TAB_GPS = "tabGps";
	public static final String TAB_MAP = "tabMap";
	public static final String TAB_WEATHER = "tabWeather";
	public static final String TAB_COMMUNITY = "tabCommunity";
	
	private RadioGroup radioderGroup;	
	private TabHost tabHost;

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tab);
		tabHost=this.getTabHost();

		
        //添加选项卡
		tabHost.addTab(tabHost.newTabSpec("TAB_GPS").setIndicator("TAB_GPS")
        			.setContent(new Intent(this, ui.activity.gps.GpsObtainActivity.class)));
//		tabHost.addTab(tabHost.newTabSpec("TAB_MAP").setIndicator("TAB_MAP")
 //       		.setContent(new Intent(this,ui.activity.GoogleMap.GMapActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("TAB_WEATHER").setIndicator("TAB_WEATHER")
        		.setContent(new Intent(this,ui.activity.weather.WeatherActivity.class))); 
//		tabHost.addTab(tabHost.newTabSpec("TAB_COMMUNITY").setIndicator("TAB_COMMUNITY")
//        		.setContent(new Intent(this,ui.activity.community.CommunityActivity.class))); 
//		tabHost.addTab(tabHost.newTabSpec("TAB_COMMUNITY").setIndicator("TAB_COMMUNITY")
//        		.setContent(new Intent(this,ui.activity.community.Communitymain.class))); 
		
        radioderGroup = (RadioGroup) findViewById(R.id.main_radio);
		radioderGroup.setOnCheckedChangeListener(this);

		this.tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch(checkedId){
		case R.id.radio_button0:
			tabHost.setCurrentTabByTag("TAB_GPS");
			break;
		case R.id.radio_button1:
			tabHost.setCurrentTabByTag("TAB_MAP");
			break;
		case R.id.radio_button2:
			tabHost.setCurrentTabByTag("TAB_WEATHER");
			break;
		case R.id.radio_button3:
			tabHost.setCurrentTabByTag("TAB_COMMUNITY");
			break;
		}		
	}
}
   
