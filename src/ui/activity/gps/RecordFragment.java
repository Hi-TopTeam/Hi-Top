package ui.activity.gps;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.DreamTeam.HiTop.R;

import domain.businessEntity.gps.ClimbData;
import domain.businessService.gps.ClimbDataService;
import domain.businessService.gps.IClimbDataService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import app.NewMainActivity;

public class RecordFragment extends Fragment {
	
private IClimbDataService dateService;
	
	private ListView recList;
	private TextView tv_id;
	private TextView tv_name;
	List<Map<String,String>> data;
	List<ClimbData> list;
	Date date=new Date();
	private View layoutView;
	
	
	public static RecordFragment newInstance()
	{
		RecordFragment fragment=new RecordFragment();
		
		return fragment;
	}
	public void setDate(List<ClimbData> list)
	{
		this.list=list;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		layoutView=inflater.inflate(R.layout.activity_record, container, false);
		//dateService=new ClimbDataService();
		//list=dateService.getClimbData();
		data=convertDateToMap(list);
		recList=(ListView)layoutView.findViewById(R.id.recList);
		recList.setOnItemClickListener(new OnItemClickListener() {
			
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {				
				Toast.makeText(getActivity(), "Changing to ID " +  list.get(arg2).getClimbID(), Toast.LENGTH_SHORT).show();
				FragmentTransaction ft=getFragmentManager().beginTransaction();
				ft.hide(getActivity().getSupportFragmentManager().findFragmentById(getId()));
				RecDetailsFragment recDetail=RecDetailsFragment.newInstance();
				Bundle bundle=new Bundle();
				bundle.putInt("id", list.get(arg2).getClimbID());
				recDetail.setArguments(bundle);
				ft.add(R.id.newtab,recDetail,"详细信息");
				ft.addToBackStack("详细记录");
				ft.commit();
			}
		});
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, 
				R.layout.activity_reclist2, new String[] {"name","date"}, new int[]{R.id.recName3,R.id.recDate3});
		recList.setAdapter(adapter);
		
		
		return layoutView;
	}
	@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			
		}
	
	@Override
		public void onSaveInstanceState(Bundle outState) {
			// TODO Auto-generated method stub
			super.onSaveInstanceState(outState);
			
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

}
