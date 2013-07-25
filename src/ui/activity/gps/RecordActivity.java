package ui.activity.gps;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ui.activity.ActivityOfAF4Ad;
import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.DreamTeam.HiTop.R;

import domain.businessEntity.gps.ClimbData;
import domain.businessService.gps.ClimbDataService;
import domain.businessService.gps.IClimbDataService;

/**
 * 
 * @author DreamTeam 郑宇 
 */
public class RecordActivity extends ActivityOfAF4Ad implements  OnTouchListener,OnGestureListener  {
	private IClimbDataService dateService;
	
	private ListView recList;
	private TextView tv_id;
	private TextView tv_name;
	List<Map<String,String>> data;
	List<ClimbData> list;
	Date date=new Date();
	GestureDetector mGestureDetector=null;  //定义手势监听对象
	private int verticalMinDistance = 10;   //最小触摸滑动距离
	private int minVelocity         = 0;   //最小水平移动速度
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		dateService=new ClimbDataService();
		list=dateService.getClimbData();
		data=convertDateToMap(list);
		recList=(ListView)findViewById(R.id.recList);
		mGestureDetector=new GestureDetector((OnGestureListener)this);
		RelativeLayout recordlayout=(RelativeLayout)findViewById(R.id.record_layout);
		recordlayout.setOnTouchListener(this);
		recordlayout.setLongClickable(true);
		recList.setOnItemClickListener(new OnItemClickListener() {

			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {				
				Intent intent=new Intent(RecordActivity.this, RecDetailsActivity.class);
				Bundle bundle=new Bundle();
				bundle.putInt("id", list.get(arg2).getClimbID());
				bundle.putInt("count", list.size());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
		
		SimpleAdapter adapter = new SimpleAdapter(this, data, 
				R.layout.activity_reclist2, new String[] {"name","date"}, new int[]{R.id.recName3,R.id.recDate3});
		recList.setAdapter(adapter);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_record, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.finish();
	}
	
	@Override
	protected void initControlsAndRegEvent() {

	}

	private List<Map<String, String>> convertDateToMap(List<ClimbData> climbData) {
		// TODO Auto-generated method stub
		List<Map<String,String>> mapList=new ArrayList<Map<String, String>>();;
		if(climbData!=null)
		{
			int len=climbData.size(),i=0;
			for(;i<len;i++)
			{
				Map<String,String> map=new HashMap<String, String>();
				//map.put("id",climbData.get(i).getClimbID()+"");
				map.put("name", climbData.get(i).getClimbName());
				Date date=climbData.get(i).getStartTime();
				map.put("date", DateFormat.getDateInstance().format(date));
				mapList.add(map);
			}
			
		}
		return mapList;
		
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
		if(arg1.getX()-arg0.getX()>verticalMinDistance && Math.abs(arg2)>minVelocity)
		{
			//Intent intent=new Intent(RecordActivity.this, GpsObtainActivity.class);
			//startActivity(intent);
			RecordActivity.this.finish();
			overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
		
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
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);  
	}
	
	

}
