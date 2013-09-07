package ui.activity.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.ksoap2.serialization.SoapObject;

import com.DreamTeam.HiTop.R;

import domain.businessEntity.Helper.GroupHelper;
import domain.businessEntity.Helper.MemberHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.MainActivity;
import ui.activity.ActivityOfAF4Ad;
import ui.activity.SystemManagement.LoginActivity;
import ui.activity.community.CommunityActivity.ViewHolder;
import ui.viewModel.LoadingActivity;
import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;
import webservice.HiTopWebPara;
import webservice.WebServiceDelegate;
import webservice.WebServiceUtils;

public class GroupMemberActivity extends ActivityOfAF4Ad implements
		WebServiceDelegate {
	private ListView listView;
	private WebServiceUtils webService;
	private WebServiceUtils webService2;
	private List<Map<String, Object>> mdata;
	private List<MemberHelper> list_Helpers;
	private SharedPreferences sp;
	private String username;
//	private ProgressDialog mpDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_members);
	}

	@Override
	protected void initControlsAndRegEvent() {
		listView = (ListView) findViewById(R.id.lv_members);
		sp = getSharedPreferences("login_user", MODE_PRIVATE);
		username = sp.getString("user", null);
		webService = new WebServiceUtils(HiTopWebPara.CM_NAMESPACE,
				HiTopWebPara.CM_URL, this);
		HashMap<String, Object> args = new HashMap<String, Object>();
		String gid = SaveGid.getGid();
		args.put("gid", gid);
		Intent intent = new Intent();  
        intent.setClass(this,ui.viewModel.LoadingActivity.class);//跳转到加载界面  
        startActivity(intent);
		webService.callWebService("getGroupMember", args, List.class);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Iterator it = list_Helpers.iterator();

		while (it.hasNext()) {
			MemberHelper g = new MemberHelper();
			Map<String, Object> map = new HashMap<String, Object>();
			g = (MemberHelper) it.next();
			map.put("nickname", g.getNickname());
			map.put("img", R.drawable.ic_hitop);
			list.add(map);
		}
		return list;

	}

	/**
	 * 自定义 ListView适配器
	 */
	public final class ViewHolder {
		public ImageView img;
		public TextView nickname;
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
			return null;
		}

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
				convertView = mInflater.inflate(R.layout.activity_members_item,
						null);
				holder.img = (ImageView) convertView
						.findViewById(R.id.img_member);
				holder.nickname = (TextView) convertView
						.findViewById(R.id.nickname);
				holder.add = (TextView) convertView
						.findViewById(R.id.add_friend);
				convertView.setTag(holder);
			} else {

				holder = (ViewHolder) convertView.getTag();
			}
			holder.img.setBackgroundResource((Integer) mdata.get(position).get(
					"img"));
			holder.nickname.setText((String) mdata.get(position)
					.get("nickname"));
			holder.add.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					addMember((String) mdata.get(position).get("nickname"));
				}
			});
			return convertView;
		}
	}

	public void addMember(String nickname) {
		webService2 = new WebServiceUtils(HiTopWebPara.CM_NAMESPACE,
				HiTopWebPara.CM_URL, new ProcessWebservice2());
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("username", username);
		args.put("nickname", nickname);
		webService2.callWebService("addFriend", args, boolean.class);

	}

	/**
	 * 
	 * 添加朋友web服务返回结果处理
	 * 
	 */
	class ProcessWebservice2 implements WebServiceDelegate {

		@Override
		public void handleException(Object ex) {
			Toast toast = Toast.makeText(GroupMemberActivity.this, "请检查网络连接",
					Toast.LENGTH_SHORT);
			toast.show();

		}

		@Override
		public void handleResultOfWebService(String methodName, Object result) {
			boolean flag = (Boolean) result;
			if (flag == true) {
				Toast toast = Toast.makeText(GroupMemberActivity.this,
						"添加朋友成功", Toast.LENGTH_SHORT);
				toast.show();
			} else {
				Toast toast = Toast.makeText(GroupMemberActivity.this,
						"添加朋友失败", Toast.LENGTH_SHORT);
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
		list_Helpers = new ArrayList<MemberHelper>();
		List<SoapObject> list = new ArrayList<SoapObject>();
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
				MemberHelper mHelper = new MemberHelper();
				mHelper.setUsername(sObject.getProperty("username").toString());
				mHelper.setNickname(sObject.getProperty("nickname").toString());
				list_Helpers.add(mHelper);
			}
			mdata = getData();
			MyAdapter adapter = new MyAdapter(this);
			listView.setAdapter(adapter);
		}
		LoadingActivity loadingActivity = new LoadingActivity();
		loadingActivity.loadingActivity.finish();
	}
}
