package ui.activity.community;

import java.util.HashMap;
import java.util.List;

import com.DreamTeam.HiTop.R;
import com.google.android.gms.internal.m;

import domain.businessEntity.Helper.GroupHelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ui.activity.ActivityOfAF4Ad;
import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;
import webservice.HiTopWebPara;
import webservice.WebServiceDelegate;
import webservice.WebServiceUtils;

public class CreateGroupActivity extends ActivityOfAF4Ad implements WebServiceDelegate{
	private WebServiceUtils webService;
	private Button bt_create;
	private TextView tv_name;
	private TextView tv_place;
	private TextView tv_time;
	private TextView tv_info;
	private GroupHelper gHelper;
	private SharedPreferences sp;
	private String username;
	private ProgressBar mProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
	}

	@Override
	protected void initControlsAndRegEvent() {
		sp = getSharedPreferences("login_user", MODE_PRIVATE);
		bt_create = (Button) findViewById(R.id.bt_create);
		tv_name = (TextView) findViewById(R.id.cg_name);
		tv_place = (TextView) findViewById(R.id.cg_place);
		tv_time = (TextView) findViewById(R.id.cg_time);
		tv_info = (TextView) findViewById(R.id.cg_info);
		username = sp.getString("user",null);
		mProgress = (ProgressBar) findViewById(R.id.progressBar_creat);
		bt_create.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				gHelper = new GroupHelper();
				gHelper.setGroupname(tv_name.getText().toString());
				gHelper.setPlace(tv_place.getText().toString());
				gHelper.setTime(tv_time.getText().toString());
				gHelper.setInfo(tv_info.getText().toString());
				gHelper.setSponsor(username);
				createGroup(gHelper);				
			}
		});
		
		
	}
	/**
	 * 创建活动组
	 */
	public void createGroup(GroupHelper gHelper){		
		webService = new WebServiceUtils(HiTopWebPara.CM_NAMESPACE, HiTopWebPara.CM_URL, this);
		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("GroupHelper", gHelper);
		mProgress.setIndeterminate(true);
		mProgress.setVisibility(View.VISIBLE);
		webService.callWebService("createGroup", args, boolean.class);
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
		Toast toast = Toast.makeText(CreateGroupActivity.this, "请检查网络连接",
				Toast.LENGTH_SHORT);
		toast.show();
		
	}

	@Override
	public void handleResultOfWebService(String methodName, Object result) {
		boolean flag = (Boolean) result;
		mProgress.setVisibility(View.GONE);
		if (flag == true) {
			Toast toast = Toast.makeText(CreateGroupActivity.this, "创建成功",
					Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Toast toast = Toast.makeText(CreateGroupActivity.this, "创建失败",
					Toast.LENGTH_SHORT);
			toast.show();
		}
		
	}

}
