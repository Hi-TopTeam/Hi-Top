package ui.activity.gps;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ui.activity.ActivityOfAF4Ad;
import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DreamTeam.HiTop.R;

import domain.businessEntity.gps.ClimbData;
import domain.businessEntity.gps.LatLngData;
import domain.businessService.gps.ClimbDataService;
import domain.businessService.gps.IClimbDataService;
import foundation.webservice.GeocodeService;

/**
 * @author DreamTeam 沈志鹏
 */
public class GpsObtainActivity extends ActivityOfAF4Ad implements
		OnTouchListener, OnGestureListener {
	private TextView tv_altitude;// 高度显示控件
	private TextView tv_direction;// 方向显示控件
	private TextView tv_speed;// 速度显示控件
	private TextView tv_longitude;// 经度显示控件
	private TextView tv_latitude;// 纬度显示控件
	private ImageButton bt_startAndStop;// 开始结束按钮
	private Builder builder;
	private EditText editor;// 对话框文本输入控件
	private LocationManager locManager;// 定义LocationManager对象
	private String cliName;// 行程名称
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
	private GestureDetector mGestureDetector = null; // 定义手势监听对象
	private int verticalMinDistance = 10; // 最小触摸滑动距离
	private int minVelocity = 0; // 最小水平移动速度
	private ImageView iv_compass;
	private float predegree = 0f;
	private ImageView compassNeedle;//指南针
	private String city;
	private boolean isExit = false;
	private String test;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		mGestureDetector = new GestureDetector((OnGestureListener) this);
		LinearLayout mainlayout = (LinearLayout) findViewById(R.id.gps_main_layout);
		mainlayout.setOnTouchListener(this);
		mainlayout.setLongClickable(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_obtain_gps, menu);
		return true;
	}

	@Override
	protected void initControlsAndRegEvent() {
		
		// 获取相应控件id
		
		bt_startAndStop = (ImageButton) findViewById(R.id.bt_startAndStop);
		tv_altitude = (TextView) findViewById(R.id.tv_altitude);
		tv_direction = (TextView) findViewById(R.id.tv_direction);
		tv_speed = (TextView) findViewById(R.id.tv_speed);
		tv_longitude = (TextView) findViewById(R.id.tv_longitude);
		tv_latitude = (TextView) findViewById(R.id.tv_latitude);
		timer = (Chronometer) findViewById(R.id.timer);		
		compassNeedle=(ImageView)findViewById(R.id.iv_compassNeedle);
		climbDataService = new ClimbDataService();
		// 构建对话框输入控件对象
		editor = new EditText(this);
		builder = new AlertDialog.Builder(this);

		// 设置计时器显示格式
		timer.setFormat("%s");

		if (flag == true) {
			bt_startAndStop.setImageDrawable(getResources().getDrawable(
					R.drawable.pause));
		} else
			bt_startAndStop.setImageDrawable(getResources().getDrawable(
					R.drawable.play));

		// 启动GPS
		startGps();

		// 获取方向传感器服务
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

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
							((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
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
							timer.setBase(SystemClock.elapsedRealtime());
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
					initControlsAndRegEvent();
				}
			}

		});

	}

	// 发送开始状态广播给GoogleMap
	public void sendStartStatusToGMap() {
		String strTime;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		strTime = sf.format(startTime);
		Intent intent = new Intent();
		intent.setAction("status");
		intent.putExtra("status", true);
		intent.putExtra("startTime", strTime);
		sendBroadcast(intent);
	}

	// 发送结束状态广播给GoogleMap
	public void sendStopStatusToGMap() {
		Intent intent = new Intent();
		intent.setAction("status");
		intent.putExtra("status", false);
		sendBroadcast(intent);
	}

	// 跳转到记录界面
	public void toRecActivity() {
		toActivity(this, RecordActivity.class);
	}

	// 开启GPS功能
	public void startGps() {
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Location location = locManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		String str = LocationManager.NETWORK_PROVIDER;

		// Location
		// location=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		updateGpsView(location);
		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000,
				8, new LocationListener() {

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {

					}

					@Override
					public void onProviderEnabled(String provider) {
						updateGpsView(locManager.getLastKnownLocation(provider));

					}

					@Override
					public void onProviderDisabled(String provider) {
						updateGpsView(null);

					}

					@Override
					public void onLocationChanged(Location location) {
						updateGpsView(location);

					}
				});
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
	protected void onResume() {
		super.onResume();
		// 方向传感器注册监听器
		mSensorManager.registerListener(mSersorEventListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		// 程序停止时注销监听器
		mSensorManager.unregisterListener(mSersorEventListener);
		super.onStop();
	}

	@Override
	protected void onPause() {
		// 程序暂停时注销监听器
		mSensorManager.unregisterListener(mSersorEventListener);
		
		super.onStop();
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

	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		if (arg0.getX() - arg1.getX() > verticalMinDistance
				&& Math.abs(arg2) > minVelocity) {
			Intent intent = new Intent(GpsObtainActivity.this,
					RecordActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(arg1);
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
	public void sendBroadcastToWeather(){
		Intent intent = new Intent();
		intent.setAction("LatLng");
		intent.putExtra("Lat", currentLat);
		intent.putExtra("Lng", currentLon);
		sendBroadcast(intent);
	}
	
}
