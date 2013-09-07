package ui.activity.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.ksoap2.serialization.SoapObject;

import com.DreamTeam.HiTop.R;

import domain.businessEntity.Community.FriendsData;
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
import android.view.Window;
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
import ui.activity.community.MyGroupsActivity.processWebservice2;
import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;
import webservice.HiTopWebPara;
import webservice.WebServiceDelegate;
import webservice.WebServiceUtils;
import ui.activity.community.MyListView.OnRefreshListener;

public class MyFriendsActivity extends ActivityOfAF4Ad implements
		WebServiceDelegate {
	private MyListView listView;
	// private ListView listView;
	private WebServiceUtils webService;
	private List<FriendsData> list_f;
	private ICommunityDataService communityDataService;
	public List<Map<String, Object>> mdata;
	private SharedPreferences sp;
	private String username;
	private ImageButton iv_addFriend;
//	private ProgressDialog mpDialog;
	private SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.activity_community_friends);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.addfriend_titlebar);

	}

	@Override
	protected void initControlsAndRegEvent() {
		listView = (MyListView) findViewById(R.id.lv_myjoin);
		iv_addFriend = (ImageButton) findViewById(R.id.iv_addfriend);
		sp = getSharedPreferences("login_user", MODE_PRIVATE);
		username = sp.getString("user", null);
		communityDataService = new CommunityDataService();
		//实例化mpDialog
//		mpDialog = new ProgressDialog(getParent());
//		mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		mpDialog.setIndeterminate(true);
//		mpDialog.setTitle("数据加载中");
//		mpDialog.show();
		/**
		 * 初始化朋友列表数据库取数据
		 */
		list_f = new ArrayList<FriendsData>();
		list_f = communityDataService.getAllFriends();
		mdata = getData();
		adapter = new SimpleAdapter(this, getData(),
				R.layout.activity_friend_item,
				new String[] { "img", "nickname" }, new int[] {
						R.id.img_friend, R.id.f_nickname });
		listView.setAdapter(adapter);
		/**
		 * Web服务调用
		 */
		webService = new WebServiceUtils(HiTopWebPara.CM_NAMESPACE,
				HiTopWebPara.CM_URL, this);
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("username", username);
		webService.callWebService("getMyFriends", args, List.class);
		listView.setOnItemLongClickListener(new ItemLongClickListener());
		iv_addFriend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyFriendsActivity.this,
						SearchFriendsActivity.class);
				startActivity(intent);
			}
		});
		listView.setOnItemClickListener(new ItemClickListener());
		listView.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				RefreshTask rTask = new RefreshTask();
				rTask.execute(1000);
			}
		});

	}

	class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(MyFriendsActivity.this,
					BrowseFriendshareActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("friendusername",
					mdata.get(arg2 - 1).get("username").toString());
			intent.putExtras(bundle);
			startActivity(intent);
		}

	}

	class ItemLongClickListener implements OnItemLongClickListener {
		int postion;
		Builder builder;

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			list_f = new ArrayList<FriendsData>();
			list_f = communityDataService.getAllFriends();
			builder = new AlertDialog.Builder(getParent());
			postion = arg2 - 1;
			builder.setTitle("移除好友");
			builder.setPositiveButton("确认", new OnClickListener() {
				/**
				 * 无法删除待测试
				 */
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					webService = new WebServiceUtils(HiTopWebPara.CM_NAMESPACE,
							HiTopWebPara.CM_URL, new processWebservice2());
					HashMap<String, Object> args = new HashMap<String, Object>();
					args.put("username", username);
					args.put("friendnickname",
							mdata.get(postion).get("nickname").toString());
					FriendsData f = list_f.get(postion);
					communityDataService.deleteFriend(f);
					webService.callWebService("removeFriend", args,
							boolean.class);
					initControlsAndRegEvent();
				}
			});
			builder.setNegativeButton("取消", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

				}

			});
			builder.create().show();
			return false;
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

			HashMap<String, Object> args = new HashMap<String, Object>();
			args.put("username", username);
			webService.callWebService("getMyFriends", args, List.class);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			listView.onRefreshComplete();
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

	}

	@Override
	public void handleException(Object ex) {
//		mpDialog.dismiss();
		Toast toast = Toast.makeText(this, "更新失败,请检查网络连接", Toast.LENGTH_SHORT);
		toast.show();

	}

	class processWebservice2 implements WebServiceDelegate {

		@Override
		public void handleException(Object ex) {
			Toast toast = Toast.makeText(MyFriendsActivity.this, "请检查网络连接",
					Toast.LENGTH_SHORT);
			toast.show();

		}

		@Override
		public void handleResultOfWebService(String methodName, Object result) {
			boolean flag = (Boolean) result;
			if (flag == true) {
				Toast toast = Toast.makeText(MyFriendsActivity.this, "删除成功",
						Toast.LENGTH_SHORT);
				toast.show();

			} else {
				Toast toast = Toast.makeText(MyFriendsActivity.this, "无法删除",
						Toast.LENGTH_SHORT);
				toast.show();
			}

		}

	}

	/**
	 * 朋友列表更新Web服务返回结果处理
	 */
	@Override
	public void handleResultOfWebService(String methodName, Object result) {

		List<SoapObject> list = new ArrayList<SoapObject>();
		list_f = new ArrayList<FriendsData>();
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
				SoapObject sObject = (SoapObject) it.next();
				FriendsData fData = new FriendsData();
				fData.setUsername(sObject.getProperty("username").toString());
				fData.setNickname(sObject.getProperty("nickname").toString());
				if (communityDataService.getFriendsByUsername(fData
						.getUsername()) == null) {
					communityDataService.addFriendsData(fData);
				}
				list_f.add(fData);
			}
			mdata = getData();
			SimpleAdapter adapter = new SimpleAdapter(this, getData(),
					R.layout.activity_friend_item, new String[] { "img",
							"nickname" }, new int[] { R.id.img_friend,
							R.id.f_nickname });
			listView.setAdapter(adapter);
			
		}
//		mpDialog.dismiss();
	}

	/**
	 * 
	 * ListView 数据映射
	 */
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Iterator it = list_f.iterator();
		while (it.hasNext()) {
			FriendsData g = new FriendsData();
			Map<String, Object> map = new HashMap<String, Object>();
			g = (FriendsData) it.next();
			map.put("username", g.getUsername());
			map.put("img", R.drawable.ic_hitop);
			map.put("nickname", g.getNickname());
			list.add(map);
		}
		return list;
	}

}
