package ui.activity.gps;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.R.integer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.GestureDetector.OnGestureListener;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.sharesdk.BaiduShareException;
import com.baidu.sharesdk.BaiduSocialShare;
import com.baidu.sharesdk.ShareContent;
import com.baidu.sharesdk.ShareListener;
import com.baidu.sharesdk.SocialShareLogger;
import com.baidu.sharesdk.Utility;
import com.baidu.sharesdk.ui.BaiduSocialShareUserInterface;
import com.DreamTeam.HiTop.R;

import domain.businessEntity.gps.ClimbData;
import domain.businessService.gps.ClimbDataService;
import domain.businessService.gps.IClimbDataService;
import domain.businessService.gps.ILatLngDataService;
import domain.businessService.gps.LatLngDataService;

import socialShare.SocialShareConfig;
import tool.data.ClimbDataUtil;
import ui.activity.ActivityOfAF4Ad;
import ui.activity.GoogleMap.GMapActivity;
import ui.activity.GoogleMap.GoogleMapActivity;
import ui.activity.community.CommunityActivity;
import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;
import ui.viewModel.gps.RecDetailViewModel;
import webservice.HiTopWebPara;
import webservice.WebServiceDelegate;
import webservice.WebServiceUtils;

public class RecDetailsActivity extends ActivityOfAF4Ad implements
		OnTouchListener, OnGestureListener, WebServiceDelegate {
	private IClimbDataService dateService;
	private ILatLngDataService latLngService;
	private WebServiceUtils webService;
	private String username;
	private SharedPreferences sp;
	// 记录名
	private TextView tv_Name = null;
	// 当前日期
	private TextView tv_Date = null;

	// 开始时间
	private TextView tv_startTime = null;

	// 结束时间
	private TextView tv_stopTime = null;

	// 平均海拔
	private TextView tv_altitudeDiff = null;

	// 返回上一个界面
	private ImageView iv_back = null;

	private ImageView iv_delete = null;

	private ImageView iv_location;
	private int id = -1;
	private int maxId = -1;

	// 经纬度全局变量方便传值
	private double lat;
	private double lon;
	private String Name;
	private String strTime;// 全局变量方便取值
	GestureDetector mGestureDetector = null; // 定义手势监听对象
	private int verticalMinDistance = 10; // 最小触摸滑动距离
	private int minVelocity = 0; // 最小水平移动速度
	private RelativeLayout detailLayout;

	private ImageView iv_share;

	/********************** 社会化分享组件定义 ***********************************/
	private BaiduSocialShare socialShare;
	private BaiduSocialShareUserInterface socialShareUi;
	private final static String appKey = SocialShareConfig.mbApiKey;
	private final static String wxAppKey = SocialShareConfig.wxApp;
	private ShareContent picContent;
	private final Handler handler = new Handler(Looper.getMainLooper());

	/*******************************************************************/
	/*********************** 图表 *****************************************/
	private XYMultipleSeriesDataset ds;
	private XYMultipleSeriesRenderer render;
	private XYSeries series;
	private GraphicalView gv;
	private XYSeriesRenderer xyRender;

	/*******************************************************************/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		// 初始化社会化主件
		socialShare = BaiduSocialShare.getInstance(this, appKey);
		// socialShare.supportWeiBoSso(BaiduSocialShareConfig.SINA_SSO_APP_KEY);
		socialShare.supportWeixin(wxAppKey);
		socialShareUi = socialShare.getSocialShareUserInterfaceInstance();
		SocialShareLogger.on();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_rec_details, menu);
		return true;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("dataset", ds);
		outState.putSerializable("renderer", render);
		outState.putSerializable("current_series", series);
		outState.putSerializable("current_renderer", xyRender);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedState) {
		super.onRestoreInstanceState(savedState);
		ds = (XYMultipleSeriesDataset) savedState.getSerializable("dataset");
		render = (XYMultipleSeriesRenderer) savedState
				.getSerializable("renderer");
		series = (XYSeries) savedState.getSerializable("current_series");
		xyRender = (XYSeriesRenderer) savedState
				.getSerializable("current_renderer");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (ds == null)
			getDataset();
		if (render == null)
			getRenderer();
		if (gv == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
			gv = ChartFactory.getLineChartView(this, ds, render);
			layout.addView(gv, new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));
		} else {
			// 绘制图形
			gv.repaint();
		}
	}
	private XYMultipleSeriesDataset getDataset() {
        ds = new XYMultipleSeriesDataset();
        // 新建一个系列（线条）
        series = new XYSeries("高度走势");
        series.add(10,30);
        series.add(20,50);
        series.add(30,71);
        series.add(40,85);
        series.add(50,90);
          // 把添加了点的折线放入dataset
        ds.addSeries(series);
       
        return ds;
     }
    public XYMultipleSeriesRenderer getRenderer() {
    // 新建一个xymultipleseries
        render = new XYMultipleSeriesRenderer();
        render.setAxisTitleTextSize(16); // 设置坐标轴标题文本大小
        render.setChartTitleTextSize(20); // 设置图表标题文本大小
        render.setLabelsTextSize(15); // 设置轴标签文本大小
        render.setLegendTextSize(15); // 设置图例文本大小
        render.setMargins(new int[] {20, 30, 15,0}); // 设置4边留白
        render.setPanEnabled(false, false); // 设置x,y坐标轴不会因用户划动屏幕而移动
        // 设置4边留白透明
        render.setMarginsColor(Color.argb(0,0xff, 0, 0)); 
        render.setBackgroundColor(Color.TRANSPARENT); // 设置背景色透明
        render.setApplyBackgroundColor(true); // 使背景色生效
        render.setXTitle("持续时间");
        render.setYTitle("海拔高度");
        // 设置一个系列的颜色为蓝色
        xyRender = new XYSeriesRenderer();
        xyRender.setColor(Color.BLUE);
        // 往xymultiplerender中增加一个系列
        render.addSeriesRenderer(xyRender);
        return render;
      }
	@Override
	protected void initControlsAndRegEvent() {
		tv_Date = (TextView) findViewById(R.id.tv_Date);
		tv_startTime = (TextView) findViewById(R.id.tv_startTime);
		tv_stopTime = (TextView) findViewById(R.id.tv_stopTime);
		tv_altitudeDiff = (TextView) findViewById(R.id.tv_altitudeDiff);
		tv_Name = (TextView) findViewById(R.id.tv_recName);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_delete = (ImageView) findViewById(R.id.iv_delete);
		iv_location = (ImageView) findViewById(R.id.iv_loc);
		//iv_back.setOnClickListener(new BackToRecord());
	
		iv_share = (ImageView) findViewById(R.id.iv_share);
		mGestureDetector = new GestureDetector((OnGestureListener) this);
		detailLayout = (RelativeLayout) findViewById(R.id.detail_layout);
		detailLayout.setOnTouchListener(this);
		detailLayout.setLongClickable(true);
		dateService = new ClimbDataService();
		latLngService = new LatLngDataService();
		webService = new WebServiceUtils(HiTopWebPara.CM_NAMESPACE, HiTopWebPara.CM_URL, this);
		sp = getSharedPreferences("login_user", MODE_PRIVATE);
		username = sp.getString("user",null);
		Intent intent = getIntent();
		ClimbData climbdata;
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			id = bundle.getInt("id");
			maxId = bundle.getInt("count");
			climbdata = dateService.getClimbDataById(id);

		} else {
			throw new RuntimeException("查看信息出错");
		}
		showActivity(climbdata);

		// 设置分享内容
		picContent = new ShareContent();
		picContent.setContent("Hi-Top:我刚刚登上了" + climbdata.getClimbName() + "!"
				+ "这是我的行程记录");
		picContent.setTitle("Hi-Top");
		picContent.setUrl("http://weibo.com/lovelss310");
		// 设置删除键监听事件
		iv_delete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dateService.deleteCilmbDataByID(id);
				latLngService.deleteByDate(strTime);
				Intent intent = new Intent(RecDetailsActivity.this,
						RecordActivity.class);
				startActivity(intent);
				RecDetailsActivity.this.finish();
				
			}
		});
		// 设置定位键监听事件
		iv_location.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecDetailsActivity.this,
						GMapActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("time", strTime);
				//bundle.putString("Marker", Name);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		iv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				  Intent intent = new Intent();
				  intent.setClass(RecDetailsActivity.this, RecordActivity.class);
				  RecDetailsActivity.this.startActivity(intent);
				  
				  RecDetailsActivity.this.finish();

			}
		});
		// 分享按钮监听事件
		iv_share.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				printscreen_share();

			}
		});

	}

	class BackToRecord implements OnClickListener {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
//			  Intent intent = new Intent();
//			  intent.setClass(RecDetailsActivity.this, RecordActivity.class);
//			  RecDetailsActivity.this.startActivity(intent);
			  
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
		}

	}
	//
	public void showActivity(ClimbData climbdata) {
		if (climbdata != null) {
			Name = climbdata.getClimbName();
			tv_Name.setText(Name);
			Double longitude = climbdata.getLongitude();
			Double latitude = climbdata.getLatitude();
			lat = longitude;
			lon = latitude;
			// tv_lat.setText(longitude.toString());
			//
			// tv_lon.setText(latitude.toString());
			Date startTime = climbdata.getStartTime();
			Date stopTime = climbdata.getStopTime();
			tv_Date.setText(DateFormat.getDateInstance().format(startTime));
			SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			strTime = tmp.format(startTime);
			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

			String date1 = sdf.format(startTime);
			String date2 = sdf.format(stopTime);

			tv_startTime.setText(date1);
			tv_stopTime.setText(date2);
			int startAltitude = climbdata.getStartAltitude();
			int stopAltitude = climbdata.getStopAltitude();
			int altitudeDiff = stopAltitude - startAltitude;
			tv_altitudeDiff.setText(altitudeDiff + "");
		}

	}

	@Override
	protected ViewModel initModel() {

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
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e2.getX() - e1.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity && (--id) >= 1) {
			Animation translateAnimationoOut = AnimationUtils.loadAnimation(
					detailLayout.getContext(), R.anim.out_to_right);
			detailLayout.startAnimation(translateAnimationoOut);
			ClimbData climbdata = dateService.getClimbDataById(id);
			showActivity(climbdata);
			Animation translateAnimationIn = AnimationUtils.loadAnimation(
					detailLayout.getContext(), R.anim.in_from_left);
			detailLayout.startAnimation(translateAnimationIn);

		}
		if (e1.getX() - e2.getX() > verticalMinDistance
				&& Math.abs(velocityX) > minVelocity && (++id) <= maxId) {
			Toast toast = Toast.makeText(RecDetailsActivity.this, id + "",
					Toast.LENGTH_SHORT);
			toast.show();
			ClimbData climbdata = dateService.getClimbDataById(id);
			Animation translateAnimationOut = AnimationUtils.loadAnimation(
					detailLayout.getContext(), R.anim.out_to_left);
			detailLayout.startAnimation(translateAnimationOut);
			showActivity(climbdata);
			Animation translateAnimationIn = AnimationUtils.loadAnimation(
					detailLayout.getContext(), R.anim.in_from_right);
			detailLayout.startAnimation(translateAnimationIn);

		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	/*************** 截屏分享 **************/
	public void printscreen_share() {
		View view1 = getWindow().getDecorView();
		Display display = getWindowManager().getDefaultDisplay();
		view1.layout(0, 0, display.getWidth(), display.getHeight());
		view1.setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(view1.getDrawingCache());
		byte[] pic = Bitmap2Bytes(bitmap);
		picContent.setImageUrl(null);
		picContent.addImageByContent(pic);
		socialShareUi.showShareMenu(this, picContent,
				Utility.SHARE_THEME_STYLE, new ShareListener() {
					@Override
					public void onAuthComplete(Bundle values) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onApiComplete(String responses) {
						// TODO Auto-generated method stub
						handler.post(new Runnable() {
							@Override
							public void run() {
								Utility.showAlert(RecDetailsActivity.this,
										"分享成功");
							}
						});
					}

					@Override
					public void onError(BaiduShareException e) {

						handler.post(new Runnable() {
							@Override
							public void run() {
								Utility.showAlert(RecDetailsActivity.this,
										"分享失败");
							}
						});
					}

				});
		HashMap<String,Object> args = new HashMap<String, Object>();
		args.put("username", username);
		args.put("pic", pic);
		webService.callWebService("share_pic", args, boolean.class);
	}

	// 把Bitmap 转成 Byte
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if (keyCode == KeyEvent.KEYCODE_BACK )  
        {  
        	
        	Intent intent = new Intent(this, RecordActivity.class);  
            startActivity(intent);
            this.finish();
        }
		return true; 
    }

	@Override
	public void handleException(Object ex) {
		System.out.println();
		
	}

	@Override
	public void handleResultOfWebService(String methodName, Object result) {

		System.out.println();
	}
}
