
package ui.activity.GoogleMap;

/**
 * @author DreamTeam 郑运春
 */


import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.DreamTeam.HiTop.R;

import domain.businessEntity.gps.LatLngData;
import domain.businessService.gps.ILatLngDataService;
import domain.businessService.gps.LatLngDataService;



public class GMapActivity extends FragmentActivity 
implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener{

    private Polyline mMutablePolyline;
    //经纬度
    private double latitude;
    private double longitude;
    
	private int flag = 0;
    private LatLngData data;
    private String strTime;
    private LocationClient mLocationClient;
    
	private List<String> titles;	     //标题栏
	private List<List<String>> item_names;	//选项名称
	private List<List<Integer>> item_images;	//选项图标
	private MyDefinedMenu myMenu;	//弹出菜单
    
    private ILatLngDataService latLngDataService = null;
    
    private List<LatLng> points = new ArrayList<LatLng>();
    
    private List<LatLngData> dataList = new ArrayList<LatLngData>();
    
    
    private boolean status;//监听第一个界面广播过来的开始或结束的状态 true为开始，false为停止
    private boolean traffic_status = false;
    private GoogleMap mMap;
    private UiSettings mUiSettings;

  
    private static final LocationRequest REQUEST = LocationRequest.create()
        .setInterval(50000)         // 50 seconds
        .setFastestInterval(20)    // 16ms = 60fps
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap); 
      
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapGoogle);
        
        if (savedInstanceState == null) {
            // 第一次得到该  activity.
            mapFragment.setRetainInstance(true);
        } else {
            // 当重启该Activity 时候，不需要要在重新实例化 就可以得到之前的activity
            mMap = mapFragment.getMap();
        }
            
        latLngDataService = new LatLngDataService();
        setUpMapIfNeeded();
        
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(29,113),2));
        
        setRecRoute();
        initPopupWindow();
       
        
    }
    
    public void initPopupWindow(){
        //弹出菜单标题栏
        titles = addItems(new String[]{"图层菜单"});
        //选项图标
        item_images = new ArrayList<List<Integer>>();
        item_images.add(addItems(new Integer[]{R.drawable.pop_normalmap,
        	R.drawable.pop_earth,R.drawable.pop_terrin, R.drawable.pop_traffic}));
        
          //选项名称
        item_names = new ArrayList<List<String>>();
        item_names.add(addItems(new String[]{"平面地图", "卫星地图", "地形", "实时路况"}));

        //创建弹出菜单对象
		myMenu = new MyDefinedMenu(this, titles, item_names,item_images, new ItemClickEventGmap());
    }
    
	//菜单选项点击事件
	public class ItemClickEventGmap implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			if(arg2 == 0)
				 mMap.setMapType(MAP_TYPE_NORMAL);
				
			if(arg2 == 1)
	              mMap.setMapType(MAP_TYPE_SATELLITE);
			if(arg2 == 2)
				 mMap.setMapType(MAP_TYPE_TERRAIN);
			if(arg3 == 3){
				 mMap.setTrafficEnabled(traffic_status);
				setUi();
				 if(traffic_status)
					 traffic_status = false;
				 else
					 traffic_status = true;
			}
			myMenu.dismiss();	//菜单消失
			myMenu.currentState = 1;	//标记状态，已消失
		}
	}	
	// 转换为List<String>
    private List<String> addItems(String[] values) {
    	
    	List<String> list = new ArrayList<String>();
    	for (String var : values) {
			list.add(var);
		}
    	return list;
    }
 
   //转换为List<Integer>
    private List<Integer> addItems(Integer[] values) {
    	
    	List<Integer> list = new ArrayList<Integer>();
    	for (Integer var : values) {
			list.add(var);
		}
    	return list;
    }
    
    public boolean onCreateOptionsMenu(){
		return false;
    	
    }
    
	
    public boolean onCreateOptionsMenu(Menu menu){

		if(0 == myMenu.currentState && myMenu.isShowing()) {	
			myMenu.dismiss();			//对话框消失
			myMenu.currentState = 1;	//标记状态，已消失
		} else {
			myMenu.showAtLocation(findViewById(R.id.mapGoogle), Gravity.BOTTOM, 0,0);
			myMenu.currentState = 0;	//标记状态，显示中
		}
		return false;	// true--显示系统自带菜单；false--不显示。
    }
    
	
	@Override
	public void closeOptionsMenu() {
		// TODO Auto-generated method stub
		super.closeOptionsMenu();
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onOptionsMenuClosed(menu);
	}

    public void setRecRoute(){
        //获取由RecDetailsActivity传过来的位置信息
		Bundle bundle = getIntent().getExtras();
		if(bundle != null)
		{
			strTime = bundle.getString("time");
			dataList = latLngDataService.getLatLngDataByTime(strTime);
			
	    	Iterator<LatLngData> it = dataList.iterator();
	    	if(!it.hasNext()){
	    		Toast toast = Toast.makeText(this, "当前记录无轨迹！", Toast.LENGTH_SHORT);
	    		toast.show();
	    		return;
	    	}
	    	 
	    	PolylineOptions RecOptions = new PolylineOptions();
	    	
	    	 mMap.addMarker(new MarkerOptions()
             .position(new LatLng(it.next().getLat(),it.next().getLng()))
             .title("开始"));
	    	
	    	//LatLngData data;
	    	while(it.hasNext()){
	    		data = it.next();
	    		RecOptions.add(new LatLng(data.getLat(),data.getLng()));
	    	}
	    	
	    	mMap.addMarker(new MarkerOptions()
             .position(new LatLng(data.getLat(),data.getLng()))
             .title("结束"));
	    	
	    	
	        mMutablePolyline = mMap.addPolyline(RecOptions
	                .color(Color.GREEN)
	                .width(2)
	                .geodesic(true));
		}
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        
        if (mMap != null) {
        		setUi();
              mMap.setMapType(MAP_TYPE_SATELLITE);
            }
        status = BroadcastDataReceiver.getStatus();
        
        setUpLocationClientIfNeeded();
        mLocationClient.connect();
    }
    
    @Override
    public void onPause() {
      super.onPause();
      if (mLocationClient != null) {
        mLocationClient.disconnect();
      }
    }
    
    private void setUpLocationClientIfNeeded() {
        if (mLocationClient == null) {
          mLocationClient = new LocationClient(
              getApplicationContext(),
              this,  // ConnectionCallbacks
              this); // OnConnectionFailedListener
        }
      }

    //添加经纬度点
    private void addLatLngPoint(double longitude,double latitude){
    	double lat = latitude;
    	double lon = longitude;
		
    	points.add(new LatLng(lon,lat));
    }

    private void setUi(){
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setTiltGesturesEnabled(true);
        mUiSettings.setRotateGesturesEnabled(true);
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapGoogle))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        mUiSettings = mMap.getUiSettings();
    }

    private void drawRouteOnMap(){
        if(points.size() >= 2)
        {
	        	PolylineOptions options = new PolylineOptions();
			    			    
			       options.addAll(points);
			        
			        mMutablePolyline = mMap.addPolyline(options
			                .color(Color.RED)
			                .width(2)
			                .geodesic(true));
        }
    }

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if(status){
				
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				
				if(flag == 0 ){
					 mMap.addMarker(new MarkerOptions()
		             .position(new LatLng(latitude,longitude))
		             .title("开始"));
					 flag = 1;
				}
				
				addLatLngPoint(latitude,longitude);
				
				//划线
				drawRouteOnMap();
				
				//将记录下的点存入数据库
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String time = sf.format(new java.util.Date());
				
				LatLngData data = new LatLngData();		
				data.setLat(latitude);
				data.setLng(longitude);
				data.setStartTime(time);
			
				latLngDataService.addLatLngData(data);
		}else{
			if(flag == 1){
				mMap.addMarker(new MarkerOptions()
	             .position(new LatLng(latitude,longitude))
	             .title("结束"));
				 flag = 2;
			}
		}
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub0
	}
	
	  @Override
	  public void onConnected(Bundle connectionHint) {
	    mLocationClient.requestLocationUpdates(
	        REQUEST,
	        this);  // LocationListener
	  }
	  
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
	}
}
