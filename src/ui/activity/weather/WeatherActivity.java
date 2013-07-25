package ui.activity.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.DreamTeam.HiTop.R;

import foundation.webservice.GeocodeService;
import foundation.webservice.WeatherService;

import tool.SunriseSunset.SunriseSunset;
import ui.activity.ActivityOfAF4Ad;
import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;

public class WeatherActivity extends ActivityOfAF4Ad {

	private ImageView todayWhIcon = null;
	private TextView tv_todaydate = null;
	private TextView tv_currentTemperature = null;
	private TextView tv_today_Temp = null;
	private TextView tv_today_windspeed = null;
	private ImageView tomorrowWhIcon = null;
	private TextView tv_tomorrowdate = null;
	private TextView tv_tomorrow_Temp = null;
	private TextView tv_tomorrow_windspeed = null;
	private TextView today_sunrisetime = null;//
	private TextView today_sunsettime = null;//

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		SunriseAndSetAsyncTask calculate = new SunriseAndSetAsyncTask();
		
		
		
		RequireWeatherAsyncTask require = new RequireWeatherAsyncTask();
		require.execute();
		calculate.execute();
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_weather, menu);
		return true;
	}

	@Override
	protected void initControlsAndRegEvent() {
		todayWhIcon = (ImageView) findViewById(R.id.todayWhIcon);
		tv_todaydate = (TextView) findViewById(R.id.tv_todaydate);
		tv_currentTemperature = (TextView) findViewById(R.id.tv_currentTemperature);
		tv_today_Temp = (TextView) findViewById(R.id.tv_today_Temp);
		tv_today_windspeed = (TextView) findViewById(R.id.tv_today_windspeed);
		tomorrowWhIcon = (ImageView) findViewById(R.id.tomorrowWhIcon);
		tv_tomorrowdate = (TextView) findViewById(R.id.tv_tomorrowdate);
		// tv_currentTemperature = (TextView)
		// findViewById(R.id.tv_currentTemperature);
		// tv_tomorrow_Temp = (TextView) findViewById(R.id.tv_tomorrow_Temp);
		tv_tomorrow_windspeed = (TextView) findViewById(R.id.tv_tomorrow_windspeed);
		tv_tomorrow_Temp = (TextView) findViewById(R.id.tv_tomorrow_Temp);

		today_sunrisetime = (TextView) findViewById(R.id.today_sunrisetime);
		today_sunsettime = (TextView) findViewById(R.id.today_sunsettime);
		
		
		

	}

	@Override
	protected ViewModel initModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void upDateView(ViewModel aVM) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processViewModelErrorMsg(List<ModelErrorInfo> errsOfVM,
			String errMsg) {
		// TODO Auto-generated method stub

	}

	// 多线程实现天气查询
	class RequireWeatherAsyncTask extends AsyncTask<Void, Void, SoapObject> {

		@Override
		protected SoapObject doInBackground(Void... arg0) {
			String city;
			city = GeocodeService.getAddressByLatLng(LatLngReceiver.getLat(),
					LatLngReceiver.getLng(), 2);
			return WeatherService.getWeatherByCity(city);
		}

		@Override
		protected void onPostExecute(SoapObject detail) {
			// TODO Auto-generated method stub
			showWeather(detail);
		}
	}

	private void showWeather(SoapObject detail) {
		if (detail == null) {
			initModel();
			return;
		}
		String weatherToday = null;
		String weatherTomorrow = null;
		String weatherCurrent = null;
		int iconToday;
		int iconTomorrow;
		// int iconAfterday[] = new int[2];
		// 获取天气实况
		weatherCurrent = detail.getProperty(4).toString().substring(10, 13);
		tv_currentTemperature.setText(weatherCurrent);
		// 解析今天的天气情况
		String date = detail.getProperty(7).toString();
		weatherToday = "今天:" + date.split(" ")[0];
		weatherToday = weatherToday + "  " + date.split(" ")[1];
		tv_todaydate.setText(weatherToday);
		weatherToday = detail.getProperty(8).toString();// 今日气温范围
		tv_today_Temp.setText(weatherToday);
		weatherToday = detail.getProperty(9).toString();
		tv_today_windspeed.setText(weatherToday);
		iconToday = parseIcon(detail.getProperty(10).toString());
		todayWhIcon.setImageResource(iconToday);
		// iconToday[1] = parseIcon(detail.getProperty(11).toString());
		// 解析明天的天气情况
		date = detail.getProperty(12).toString();
		weatherTomorrow = "明天:" + date.split(" ")[0];
		weatherTomorrow = weatherTomorrow + "  " + date.split(" ")[1];
		tv_tomorrowdate.setText(weatherTomorrow);
		weatherTomorrow = detail.getProperty(13).toString();
		tv_tomorrow_Temp.setText(weatherTomorrow);
		weatherTomorrow = detail.getProperty(14).toString();
		tv_tomorrow_windspeed.setText(weatherTomorrow);
		iconTomorrow = parseIcon(detail.getProperty(15).toString());
		tomorrowWhIcon.setImageResource(iconTomorrow);
		// iconTomorrow[1] = parseIcon(detail.getProperty(16).toString());

	}

	// 工具方法，该方法负责把返回的天气图标字符串。转换为程序的图片资源ID
	private int parseIcon(String strIcon) {
		// TODO 自动生成的方法存根
		// 根据字符串解析天气图标的代码
		if (strIcon == null)
			return -1;
		if ("0.gif".equals(strIcon))
			return R.drawable.a_0;
		if ("1.gif".equals(strIcon))
			return R.drawable.a_1;
		if ("2.gif".equals(strIcon))
			return R.drawable.a_2;
		if ("3.gif".equals(strIcon))
			return R.drawable.a_3;
		if ("4.gif".equals(strIcon))
			return R.drawable.a_4;
		if ("5.gif".equals(strIcon))
			return R.drawable.a_5;
		if ("6.gif".equals(strIcon))
			return R.drawable.a_6;
		if ("7.gif".equals(strIcon))
			return R.drawable.a_7;
		if ("8.gif".equals(strIcon))
			return R.drawable.a_8;
		if ("9.gif".equals(strIcon))
			return R.drawable.a_9;
		if ("10.gif".equals(strIcon))
			return R.drawable.a_10;
		if ("11.gif".equals(strIcon))
			return R.drawable.a_11;
		if ("12.gif".equals(strIcon))
			return R.drawable.a_12;
		if ("13.gif".equals(strIcon))
			return R.drawable.a_13;
		if ("14.gif".equals(strIcon))
			return R.drawable.a_14;
		if ("15.gif".equals(strIcon))
			return R.drawable.a_15;
		if ("16.gif".equals(strIcon))
			return R.drawable.a_16;
		if ("17.gif".equals(strIcon))
			return R.drawable.a_17;
		if ("18.gif".equals(strIcon))
			return R.drawable.a_18;
		if ("19.gif".equals(strIcon))
			return R.drawable.a_19;
		if ("20.gif".equals(strIcon))
			return R.drawable.a_20;
		if ("21.gif".equals(strIcon))
			return R.drawable.a_21;
		if ("22.gif".equals(strIcon))
			return R.drawable.a_22;
		if ("23.gif".equals(strIcon))
			return R.drawable.a_23;
		if ("24.gif".equals(strIcon))
			return R.drawable.a_24;
		if ("25.gif".equals(strIcon))
			return R.drawable.a_25;
		if ("26.gif".equals(strIcon))
			return R.drawable.a_26;
		if ("27.gif".equals(strIcon))
			return R.drawable.a_27;
		if ("28.gif".equals(strIcon))
			return R.drawable.a_28;
		if ("29.gif".equals(strIcon))
			return R.drawable.a_29;
		if ("30.gif".equals(strIcon))
			return R.drawable.a_30;
		if ("31.gif".equals(strIcon))
			return R.drawable.a_31;
		return 0;
	}
	
	class SunriseAndSetAsyncTask extends AsyncTask<Void,Void,Date[]>{
		private double lat;
		private double lng;
		@Override
		protected Date[] doInBackground(Void... params) {
			lat = LatLngReceiver.getLat();
			lng = LatLngReceiver.getLng();
			Date now = new Date();
			Date[] riseSet = new Date[2];
			SunriseSunset sunriseSunset = new SunriseSunset(lat, lng, now, 0);
			riseSet[0]=sunriseSunset.getSunrise();
			riseSet[1]=sunriseSunset.getSunset();		
			return riseSet;
		}

		@Override
		protected void onPostExecute(Date[] result) {
			SimpleDateFormat sf = new SimpleDateFormat("hh:mm:ss");
			today_sunrisetime.setText(sf.format(result[0]));
			today_sunsettime.setText(sf.format(result[1]));
		}
		
		
	}
	
}
