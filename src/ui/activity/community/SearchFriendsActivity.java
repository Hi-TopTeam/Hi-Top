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
import domain.businessEntity.Helper.GroupHelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import ui.activity.ActivityOfAF4Ad;
import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;
import webservice.HiTopWebPara;
import webservice.WebServiceDelegate;
import webservice.WebServiceUtils;

public class SearchFriendsActivity extends ActivityOfAF4Ad implements
		WebServiceDelegate {
	private ListView listView;
	private ImageView search;
	private EditText et_nickname;
	private SharedPreferences sp;
	private String username;
	private WebServiceUtils webService;
	private List<FriendsData> list_f;
	public List<Map<String, Object>> mdata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addfriends);
	}

	@Override
	protected void initControlsAndRegEvent() {
		listView = (ListView) findViewById(R.id.sf_listview);
		search = (ImageView) findViewById(R.id.search);
		et_nickname = (EditText) findViewById(R.id.sf_nickname);
		sp = getSharedPreferences("login_user", MODE_PRIVATE);
		username = sp.getString("user", null);
		webService = new WebServiceUtils(HiTopWebPara.CM_NAMESPACE,
				HiTopWebPara.CM_URL, this);
		search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String nickname = et_nickname.getText().toString();
				HashMap<String, Object> args = new HashMap<String, Object>();
				args.put("nickname",nickname);
				webService.callWebService("findFriendsByNickname", args, List.class);
			}
		});
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
		Toast toast = Toast.makeText(SearchFriendsActivity.this, "请检查网络连接",
				Toast.LENGTH_SHORT);
		toast.show();

	}

	@Override
	public void handleResultOfWebService(String methodName, Object result) {
		List<SoapObject> list = new ArrayList<SoapObject>();
		list_f = new ArrayList<FriendsData>();
		if (result instanceof Vector) {
			Vector vector = (Vector) result;
			for (int i = 0; i < vector.size(); i++)
				list.add((SoapObject) vector.get(i));
		} else {
			SoapObject soapObject = (SoapObject) result;
			list.add(soapObject);
		}
		Iterator it = list.iterator();
		while(it.hasNext()){
			SoapObject sObject = (SoapObject) it.next();
			FriendsData fData = new FriendsData();
			fData.setUsername(sObject.getProperty("username").toString());
			fData.setNickname(sObject.getProperty("nickname").toString());
			list_f.add(fData);
		}
		mdata = getData();
		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.activity_friend_item,
                new String[]{"img","nickname"},
                new int[]{R.id.img_friend,R.id.f_nickname});
		listView.setAdapter(adapter);
	}
	/**
	 * 
	 * ListView Map映射
	 */
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Iterator it = list_f.iterator();
		while (it.hasNext()) {
			FriendsData g = new FriendsData();
			Map<String, Object> map = new HashMap<String, Object>();
			g = (FriendsData) it.next();
			map.put("username",g.getUsername());
			map.put("img", R.drawable.ic_hitop);
			map.put("nickname", g.getNickname());
			list.add(map);
		}
		return list;
	}

}
