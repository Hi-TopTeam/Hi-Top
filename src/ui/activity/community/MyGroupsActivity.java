package ui.activity.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.ksoap2.serialization.SoapObject;

import com.DreamTeam.HiTop.R;

import domain.businessEntity.Community.GroupsData;
import domain.businessEntity.Helper.GroupHelper;
import domain.businessService.Community.CommunityDataService;
import domain.businessService.Community.ICommunityDataService;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import ui.activity.ActivityOfAF4Ad;
import ui.viewModel.LoadingActivity;
import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;
import webservice.HiTopWebPara;
import webservice.WebServiceDelegate;
import webservice.WebServiceUtils;
import ui.activity.community.MyListView.OnRefreshListener;

public class MyGroupsActivity extends ActivityOfAF4Ad implements
		WebServiceDelegate {

	private MyListView listView;
	// private ListView listView;
	private WebServiceUtils webService;
	private List list_Helpers;
	private List<Map<String, Object>> mdata;
	private ICommunityDataService communityDataService;
	private SharedPreferences sp;
	private String username;
	private ImageButton iv_createGroup;
    private	SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_myjoin);
	}

	@Override
	protected void initControlsAndRegEvent() {
		listView = (MyListView) findViewById(R.id.lv_myjoin);
		iv_createGroup = (ImageButton) findViewById(R.id.iv_createGroup);
		sp = getSharedPreferences("login_user", MODE_PRIVATE);
		username = sp.getString("user", null);
		//实例化mpDialog
		Intent intent = new Intent();  
        intent.setClass(this,ui.viewModel.LoadingActivity.class);//跳转到加载界面  
        startActivity(intent);
		
		
		communityDataService = new CommunityDataService();

		webService = new WebServiceUtils(HiTopWebPara.CM_NAMESPACE,
				HiTopWebPara.CM_URL, this);
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("username", username);
		webService.callWebService("getMyGroups", args, List.class);

		list_Helpers = new ArrayList<GroupHelper>();
		List<GroupsData> tempDatas;
		tempDatas = communityDataService.getMyJoinGroups();
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
		adapter = new SimpleAdapter(this, getData(),
				R.layout.activity_myjoin_item, new String[] { "img", "name",
						"info" }, new int[] { R.id.img_join, R.id.name_join,
						R.id.info_join });
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new ItemOnclickListener());
		listView.setOnItemLongClickListener(new LongClickListener());

		listView.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				RefreshTask rTask = new RefreshTask();
				rTask.execute(1000);
			}
		});

		iv_createGroup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyGroupsActivity.this,
						CreateGroupActivity.class);
				startActivity(intent);
			}
		});
	}

	class ItemOnclickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String gid = (String) mdata.get(arg2 - 1).get("gid");
			Intent intent = new Intent(MyGroupsActivity.this,
					GroupDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("gid", gid);
			intent.putExtras(bundle);
			startActivity(intent);

		}
	}

	class LongClickListener implements OnItemLongClickListener {
		int postion;
		Builder builder;

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			builder = new AlertDialog.Builder(getParent());
			postion = arg2 - 1;
			builder.setTitle("退出该活动组");
			builder.setPositiveButton("确认", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					webService = new WebServiceUtils(HiTopWebPara.CM_NAMESPACE,
							HiTopWebPara.CM_URL, new processWebservice2());
					HashMap<String, Object> args = new HashMap<String, Object>();
					args.put("username", username);
					args.put("gid", mdata.get(postion).get("gid").toString());
					GroupsData g = (GroupsData) list_Helpers.get(postion);
					g.setIsMyJoin("false");
					communityDataService.UpdateGroupsData(g);
					webService.callWebService("quitGroup", args, boolean.class);
					initControlsAndRegEvent();
				}

			});
			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}

			});
			builder.create().show();
			return true;
		}
	}

	class processWebservice2 implements WebServiceDelegate {

		@Override
		public void handleException(Object ex) {
			Toast toast = Toast.makeText(MyGroupsActivity.this, "请检查网络连接",
					Toast.LENGTH_SHORT);
			toast.show();
		}

		@Override
		public void handleResultOfWebService(String methodName, Object result) {
			boolean flag = (Boolean) result;
			if (flag == true) {
				Toast toast = Toast.makeText(MyGroupsActivity.this, "退出成功",
						Toast.LENGTH_SHORT);
				toast.show();

			} else {
				Toast toast = Toast.makeText(MyGroupsActivity.this, "无法退出",
						Toast.LENGTH_SHORT);
				toast.show();
			}

		}

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
				g.setIsMyJoin("ture");
				communityDataService.UpdateGroupsData(g);
				list_Helpers.add(g);
			}
			mdata = getData();
			adapter = new SimpleAdapter(this, getData(),
					R.layout.activity_myjoin_item, new String[] { "img",
							"name", "info" }, new int[] { R.id.img_join,
							R.id.name_join, R.id.info_join });
			listView.setAdapter(adapter);
			
		}
		LoadingActivity loadingActivity = new LoadingActivity();
		loadingActivity.loadingActivity.finish();
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

	class RefreshTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... params) {
			try {
				Thread.sleep(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			HashMap<String, Object> args = new HashMap<String, Object>();
			args.put("username", username);
			webService.callWebService("getMyGroups", args, List.class);

			// 在data最前添加数据
			// data.addFirst("刷新后的内容");
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			listView.onRefreshComplete();
		}
	}

}
