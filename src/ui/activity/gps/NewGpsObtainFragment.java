package ui.activity.gps;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ui.activity.GoogleMap.GpsToMapReceiver;
import ui.activity.weather.NewLatLngReceiver;

import com.DreamTeam.HiTop.R;

import domain.businessEntity.gps.ClimbData;
import domain.businessService.gps.ClimbDataService;
import domain.businessService.gps.IClimbDataService;
import foundation.webservice.GeocodeService;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import app.NewMainActivity;

public class NewGpsObtainFragment extends Fragment {
	private TextView tv_altitude;// 高度显示控件
	private TextView tv_direction;// 方向显示控件
	private TextView tv_speed;// 速度显示控件
	private TextView tv_longitude;// 经度显示控件
	private TextView tv_latitude;// 纬度显示控件
	private ImageButton bt_startAndStop;// 开始结束按钮
	private Builder builder;
	private EditText editor;// 对话框文本输入控件
	
	
	
	Location currentLocation=null;
	
	boolean locationFlag = true;
	
	
	private String cliName=null;// 行程名称
	private Chronometer timer;// 定义计时器
	private Date startTime;// 记录开始时间
	private Date stopTime;// 记录结束时间
	private int startAltitude;// 开始海拔
	private int stopAltitude;// 结束海拔
	boolean flag = false;// 设置开始结束按钮标志
	private int currentAltitude;// 获取当前高度
	private SensorManager mSensorManager;// 定义SensorManager对象
	
	private double stopLon;// 结束时经度
	private double stopLat;// 结束时纬度
	private double currentLon;// 当前经度
	private double currentLat;// 当前纬度
	private IClimbDataService climbDataService;// 定义登山数据服务对象
	
	
	private ImageView iv_compass;
	private float predegree = 0f;
	private ImageView compassNeedle;//指南针
	private String city;
	private boolean isExit = false;
	private String test;
	
	private View layoutView;
	
	
	
	public static NewGpsObtainFragment newInstance()
	{
		NewGpsObtainFragment fragment=new NewGpsObtainFragment();
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setRetainInstance(true);
		
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layoutView=inflater.inflate(R.layout.activity_gps, container, false);
		if(TempData.isEmpty==false)
		{
			cliName=TempData.getCliName();
			flag=TempData.getFlag();
			startAltitude=TempData.getStartAltitude();
			startTime=TempData.getStartTime();
			TempData.clean();
		}
		initControlsAndRegEvent();
		return layoutView;
		
	}
	
	protected void initControlsAndRegEvent()
	{
		bt_startAndStop = (ImageButton) layoutView.findViewById(R.id.bt_startAndStop);
		tv_altitude = (TextView) layoutView.findViewById(R.id.tv_altitude);
		tv_direction = (TextView) layoutView.findViewById(R.id.tv_direction);
		tv_speed = (TextView) layoutView.findViewById(R.id.tv_speed);
		tv_longitude = (TextView) layoutView.findViewById(R.id.tv_longitude);
		tv_latitude = (TextView) layoutView.findViewById(R.id.tv_latitude);
		timer = (Chronometer) layoutView.findViewById(R.id.timer);		
		compassNeedle=(ImageView)layoutView.findViewById(R.id.iv_compassNeedle);
		climbDataService = new ClimbDataService();
		
		// 构建对话框输入控件对象
		editor = new EditText(getActivity());
		builder = new AlertDialog.Builder(getActivity());
		
		// 设置计时器显示格式
		timer.setFormat("%s");
		
		//设置开始记录键的图标
		if (flag == true) {
			bt_startAndStop.setImageDrawable(getResources().getDrawable(
					R.drawable.pause));
		} else
			bt_startAndStop.setImageDrawable(getResources().getDrawable(
					R.drawable.play));
		if(cliName!=null)
		{
			Date currentTime=new java.util.Date();
			long betweenTime=currentTime.getTime()-startTime.getTime();
			// 计时器重置
			timer.setBase(SystemClock.elapsedRealtime()-betweenTime);
			
			// 开始计时
			timer.start();
		}
		
		NewMainActivity activity=(NewMainActivity) getActivity();
		currentLocation=activity.gps.getLocation();
		updateGpsView(currentLocation);
		// 获取方向传感器服务
		mSensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
		// 开始和停止按钮监听事件
		bt_startAndStop.setOnClickListener(new View.OnClickListener() {
		@Override
			public void onClick(View v) {
			if (flag == false) {
					builder.setTitle("请输入行程名称");
					builder.setView(editor);
					RequireAddressAsyncTask asyncTask = new RequireAddressAsyncTask(
					editor);
					asyncTask.execute();
					// 点击自动EditText自动全选
					editor.setOnTouchListener(new OnTouchListener() {
					@Override
						public boolean onTouch(View v, MotionEvent event) {
							editor.selectAll();
							((InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE))
						   	.showSoftInput(v, 0);
							return true;
						}
					});

					builder.setNegativeButton("取消", new OnClickListener() {
					@Override
						public void onClick(DialogInterface dialog, int which) {
						initControlsAndRegEvent();
						}
					});
					// 设置对话框
					builder.setPositiveButton("确认", new OnClickListener() {
					@Override
						public void onClick(DialogInterface dialog, int which) {
							// 获取输入
							cliName = editor.getText().toString();
							// 计时器重置
							if(startTime==null)
							{
								timer.setBase(SystemClock.elapsedRealtime());
							}							
							// 开始计时
							timer.start();
							// 获取开始时间
							startTime = new java.util.Date();
							// 记录开始高度值
							startAltitude = currentAltitude;
							flag = true;
							sendStartStatusToGMap();
							initControlsAndRegEvent();
						}
					});
					builder.create().show();
					} else {
						flag = false;
						timer.stop();
						// 记录结束是时间
						stopTime = new java.util.Date();
						// 记录结束时高度
						stopAltitude = currentAltitude;
						// 记录结束是经度
						stopLon = currentLon;
						// 记录结束是纬度
						stopLat = currentLat;
						sendStopStatusToGMap();
						writeDataToSqlite();
						cliName=null;
						initControlsAndRegEvent();
						NewMainActivity activity=(NewMainActivity) getActivity();
						activity.updateRecord();
						
					}
				}

			});

		
		
	}
	// 发送开始状态给GoogleMap
	
	public void sendStartStatusToGMap() {
		String strTime;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		strTime = sf.format(startTime);
		GpsToMapReceiver.setStatus(true);
		GpsToMapReceiver.setTime(strTime);
		
	}
	
	
	// 发送结束状态给GoogleMap
	public void sendStopStatusToGMap() {
		GpsToMapReceiver.setStatus(false);
		
	}
	public void setLocation(Location location)
	{
		currentLocation=location;
	}

	//发送到天气
	public void sendBroadcastToWeather(){
		NewLatLngReceiver.setLat(currentLat);
		NewLatLngReceiver.setLng(currentLon);
	}
	
	
	
	
	   
		
		// 动态更新GPS数据
		public void updateGpsView(Location newLocation) {
			int altitude;
			float speed;
			double lat;
			double lon;
			if (newLocation != null) {
				currentAltitude = altitude = (int) newLocation.getAltitude();
				speed = newLocation.getSpeed();
				currentLat = lat = newLocation.getLatitude();
				currentLon = lon = newLocation.getLongitude();
				
				sendBroadcastToWeather();
				
				tv_altitude.setText(Integer.toString(altitude));
				
				tv_speed.setText(new DecimalFormat("#0.0").format(speed));
				
				tv_latitude.setText(Double.toString(lat));
				tv_longitude.setText(Double.toString(lon));
			} else {
				tv_altitude.setText("0");
				tv_speed.setText("0");
				tv_latitude.setText("0");
				tv_longitude.setText("0");
			}
		}
		
		// 设置方向传感器监听类
		SensorEventListener mSersorEventListener = new SensorEventListener() {
			// 传感器值改变
			
			@Override
			public void onSensorChanged(SensorEvent event) {
				float[] values = event.values;
				if (values[0] >= 0 && values[0] < 30)
					tv_direction.setText("北");
				if (values[0] >= 30 && values[0] < 60)
					tv_direction.setText("东北");
				if (values[0] >= 60 && values[0] < 120)
					tv_direction.setText("东");
				if (values[0] >= 120 && values[0] < 150)
					tv_direction.setText("东南");
				if (values[0] >= 150 && values[0] < 210)
					tv_direction.setText("南");
				if (values[0] >= 210 && values[0] < 240)
					tv_direction.setText("西南");
				if (values[0] >= 240 && values[0] < 300)
					tv_direction.setText("西");
				if (values[0] >= 300 && values[0] < 330)
					tv_direction.setText("西北");
				if (values[0] >= 300 && values[0] <= 360)
					tv_direction.setText("北");			
				RotateAnimation animation = new RotateAnimation(predegree, -values[0], 
				Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
				animation.setDuration(200); 
				compassNeedle.startAnimation(animation); 
				predegree=-values[0]; 

			}
			// 传感器精度改变
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub

			}
		};
		
		@Override
		public void onResume() {
			super.onResume();
			// 方向传感器注册监听器
			mSensorManager.registerListener(mSersorEventListener,
					mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
					SensorManager.SENSOR_DELAY_NORMAL);
		}

		@Override
		public void onStop() {
			// 程序停止时注销监听器
			mSensorManager.unregisterListener(mSersorEventListener);
			super.onStop();
			Toast toast = Toast.makeText(getActivity(), "gps onStop()", Toast.LENGTH_SHORT);
			toast.show();
		}
		
		@Override
		public void onDestroyView() {
			// TODO Auto-generated method stub
			super.onDestroyView();
			Toast toast = Toast.makeText(getActivity(), "gps onDestroyView()", Toast.LENGTH_SHORT);
			toast.show();
		}
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			Toast toast = Toast.makeText(getActivity(), "gps onDestroy()", Toast.LENGTH_SHORT);
			toast.show();
		}
		@Override
		public void onDetach() {
			// TODO Auto-generated method stub
			super.onDetach();
			Toast toast = Toast.makeText(getActivity(), "gps onDetach()", Toast.LENGTH_SHORT);
			toast.show();
		}

		@Override
		public void onPause() {
			// 程序暂停时注销监听器
			mSensorManager.unregisterListener(mSersorEventListener);
			super.onPause();
			//super.onStop();
			Toast toast = Toast.makeText(getActivity(), "gps onPause()", Toast.LENGTH_SHORT);
			toast.show();
		}
		
		// 数据写入数据库操作
		public void writeDataToSqlite() {
			ClimbData climbData = new ClimbData();
			climbData.setClimbName(cliName);
			climbData.setLatitude(stopLat);
			climbData.setLongitude(stopLon);
			climbData.setStartAltitude(startAltitude);
			climbData.setStopAltitude(stopAltitude);
			climbData.setStartTime(startTime);
			climbData.setStopTime(stopTime);
			climbDataService.addClimbData(climbData);
		}

		
		@Override
		public void onSaveInstanceState(Bundle outState) {
			// TODO Auto-generated method stub
			super.onSaveInstanceState(outState);
			if(TempData.getEmpty()==true)
			{
				TempData.setCliName(cliName);
				TempData.setFlag(flag);
				TempData.setStartAltitude(startAltitude);
				TempData.setStartTime(startTime);
				TempData.load();
			}
			
		}
		/**
		 * 
		 * 使用多线性异步使用访问网络，进行地址反向解析查询
		 * 
		 */
		class RequireAddressAsyncTask extends AsyncTask<Void, Void, String> {

			private EditText editor;

			public RequireAddressAsyncTask(EditText editor) {
				this.editor = editor;
			}
			@Override
			protected String doInBackground(Void... params) {
				String result = null;
				result = GeocodeService.getAddressByLatLng(currentLat, currentLon,
						1);
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				editor.setText(result);
			}

		}
		
		

}
