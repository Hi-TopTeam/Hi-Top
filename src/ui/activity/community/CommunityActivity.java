package ui.activity.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.crypto.spec.PSource;

import org.ksoap2.serialization.SoapObject;

import ui.activity.ActivityOfAF4Ad;
import ui.viewModel.LoadingActivity;
import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;
import webservice.HiTopWebPara;
import webservice.WebServiceDelegate;
import webservice.WebServiceUtils;

import com.DreamTeam.HiTop.R;

import ui.activity.SystemManagement.LoginActivity;
import ui.activity.community.MyListView.OnRefreshListener;
import domain.businessEntity.Community.GroupsData;
import domain.businessEntity.Helper.GroupHelper;
import domain.businessService.Community.CommunityDataService;
import domain.businessService.Community.ICommunityDataService;
import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import app.MainActivity;

public class CommunityActivity extends ActivityOfAF4Ad implements
		WebServiceDelegate {
	private WebServiceUtils webService;
	private WebServiceUtils webService2;
	private List<GroupHelper> list_Helpers;
	// private ListView notice_listview;
	private MyListView notice_listview;
	public List<Map<String, Object>> mdata;
	public List<Map<String, Object>> mdata_loc;
	private ICommunityDataService communityDataService;
	private SharedPreferences sp;
	private String username;
	private MyAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_main);
	}

	@Override
	protected void initControlsAndRegEvent() {
		sp = getSharedPreferences("login_user", MODE_PRIVATE);
		communityDataService = new CommunityDataService();
		notice_listview = (MyListView) findViewById(R.id.lv_myjoin);
		list_Helpers = new ArrayList<GroupHelper>();
		username = sp.getString("user", null);
		List<GroupsData> tempDatas;
		tempDatas = communityDataService.getAllData();
		Iterator it = tempDatas.iterator();
		while (it.hasNext()) {
			GroupsData tmp = new GroupsData();
			GroupHelper tmpHelper = new GroupHelper();
			tmp = (GroupsData) it.next();
			tmpHelper.setGid(tmp.getGid());
			tmpHelper.setGroupname(tmp.getGroupname());
			tmpHelper.setInfo(tmp.getInfo());
			tmpHelper.setPlace(tmp.getPlace());
			tmpHelper.setSponsor(tmp.getSponsor());
			tmpHelper.setTime(tmp.getTime());
			tmpHelper.setIsMyJoin(tmp.getIsMyJoin());
			list_Helpers.add(tmpHelper);
		}
		mdata = getData();
		adapter = new MyAdapter(this);
		notice_listview.setAdapter(adapter);

		Intent intent = new Intent();  
        intent.setClass(this,ui.viewModel.LoadingActivity.class);//跳转到加载界面  
        startActivity(intent); 

		
		webService = new WebServiceUtils(HiTopWebPara.CM_NAMESPACE,
				HiTopWebPara.CM_URL, this);
		webService2 = new WebServiceUtils(HiTopWebPara.CM_NAMESPACE,
				HiTopWebPara.CM_URL, new processWebservice2());
		webService.callWebService("getGroups", null, List.class);

		notice_listview.setOnItemClickListener(new ItemOnclickListener());

		// 下拉刷新数据
		notice_listview.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				RefreshTask rTask = new RefreshTask();
				rTask.execute(1000);
			}
		});

	}

	class ItemOnclickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String gid = (String) mdata.get(arg2 - 1).get("gid");
			Intent intent = new Intent(CommunityActivity.this,
					GroupDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("gid", gid);
			intent.putExtras(bundle);
			startActivity(intent);
		}

	}

	class RefreshTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... params) {
			try {
				Thread.sleep(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 在data最前添加数据
			/*
			 * 下面这个函数添加刷新后的内容
			 */
			webService.callWebService("getGroups", null, List.class);

			// mdata.addFirst("刷新后的内容");
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			notice_listview.onRefreshComplete();
		}
	}

	// 将数据添加进Map以便ListView适配器调用
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Iterator it = list_Helpers.iterator();

		while (it.hasNext()) {
			GroupHelper g = new GroupHelper();
			Map<String, Object> map = new HashMap<String, Object>();
			g = (GroupHelper) it.next();
			map.put("name", g.getGroupname());
			map.put("info", g.getTime() + "  " + g.getPlace());
			map.put("img", R.drawable.ic_hitop);
			map.put("gid", g.getGid());
			list.add(map);
		}
		return list;

	}

	@Override
	public void handleException(Object ex) {
		
		LoadingActivity loadingActivity = new LoadingActivity();
		loadingActivity.loadingActivity.finish();
		
		Toast toast = Toast.makeText(this, "更新失败,请检查网络连接", Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void handleResultOfWebService(String methodName, Object result) {
		List<SoapObject> list = new ArrayList<SoapObject>();
		list_Helpers = new ArrayList<GroupHelper>();
		if (result != null) {
			if (result instanceof Vector) {
				Vector vector = (Vector) result;
				for (int i = 0; i < vector.size(); i++)
					list.add((SoapObject) vector.get(i));
			} else {
				SoapObject soapObject = (SoapObject) result;
				list.add(soapObject);
			}
			Iterator it = list.iterator();

			while (it.hasNext()) {
				GroupHelper g = new GroupHelper();
				SoapObject sobj = (SoapObject) it.next();
				g.setGid(sobj.getProperty("gid").toString());
				g.setGroupname(sobj.getProperty("groupname").toString());
				g.setInfo(sobj.getProperty("info").toString());
				g.setPlace(sobj.getProperty("place").toString());
				g.setSponsor(sobj.getProperty("sponsor").toString());
				g.setTime(sobj.getProperty("time").toString());
				g.setIsMyJoin("false");
				if (communityDataService.getByGid(g.getGid()) == null) {
					communityDataService.addGroupsData(g);
				}
				list_Helpers.add(g);
			}

			mdata = getData();
			adapter = new MyAdapter(this);
			notice_listview.setAdapter(adapter);
			
		}
	
		LoadingActivity loadingActivity = new LoadingActivity();
		loadingActivity.loadingActivity.finish();	}

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

	// 加入组
	public void addGroup(int gid) {
		Toast toast = Toast.makeText(this, "Adding", Toast.LENGTH_SHORT);
		toast.show();
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("username", username);
		args.put("gid", gid);
		webService2.callWebService("addGroup", args, boolean.class);
	}

	/**
	 * 自定义 ListView适配器
	 */
	public final class ViewHolder {
		public ImageView img;
		public TextView name;
		public TextView info;
		public TextView add;
	}

	public class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mdata.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/*
		 * public long getItemId(int position) { return position; }
		 */
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.activity_group_item,
						null);
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.info = (TextView) convertView.findViewById(R.id.info);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.add = (TextView) convertView.findViewById(R.id.add);
				convertView.setTag(holder);
			} else {

				holder = (ViewHolder) convertView.getTag();
			}
			holder.img.setBackgroundResource((Integer) mdata.get(position).get(
					"img"));
			holder.info.setText((String) mdata.get(position).get("info"));
			holder.name.setText((String) mdata.get(position).get("name"));
			holder.add.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					addGroup(Integer.parseInt((String) mdata.get(position).get(
							"gid")));
				}
			});
			return convertView;
		}
	}

	/**
	 * 
	 * 添加活动组Web服务处理
	 * 
	 */
	class processWebservice2 implements WebServiceDelegate {

		@Override
		public void handleException(Object ex) {
			Toast toast = Toast.makeText(CommunityActivity.this, "请检查网络连接",
					Toast.LENGTH_SHORT);
			toast.show();

		}

		@Override
		public void handleResultOfWebService(String methodName, Object result) {
			boolean flag = (Boolean) result;
			if (flag == true) {
				Toast toast = Toast.makeText(CommunityActivity.this, "加入成功",
						Toast.LENGTH_SHORT);
				toast.show();

			} else {
				Toast toast = Toast.makeText(CommunityActivity.this, "已是该组成员",
						Toast.LENGTH_SHORT);
				toast.show();
			}

		}

	}

}
