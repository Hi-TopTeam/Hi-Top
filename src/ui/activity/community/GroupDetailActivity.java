package ui.activity.community;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.DreamTeam.HiTop.R;

import domain.businessEntity.Community.GroupsData;
import domain.businessService.Community.CommunityDataService;
import domain.businessService.Community.ICommunityDataService;

import ui.activity.ActivityOfAF4Ad;
import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;

public class GroupDetailActivity extends ActivityOfAF4Ad {
	private TextView groupname;
	private TextView info;
	private TextView moreinfo;
	private ImageView img;
	private Button check_members;
	private ICommunityDataService communityDataService;
	private GroupsData groupsData;
	private String gid;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_detail);
		Bundle bundle = getIntent().getExtras();
		gid = bundle.getString("gid");
	}

	@Override
	protected void initControlsAndRegEvent() {
		groupname = (TextView) findViewById(R.id.detail_name);
		info = (TextView) findViewById(R.id.detail_info);
		moreinfo = (TextView) findViewById(R.id.detail_moreInfo);
		img = (ImageView) findViewById(R.id.detail_img);
		check_members = (Button) findViewById(R.id.bt_checkmember);
		communityDataService = new CommunityDataService();
		groupsData = communityDataService.getByGid(gid);
		groupname.setText(groupsData.getGroupname());
		info.setText(groupsData.getTime()+"  "+groupsData.getPlace());
		moreinfo.setText(groupsData.getInfo());
		img.setBackgroundResource(R.drawable.ic_hitop);
		check_members.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GroupDetailActivity.this,
						GroupMemberActivity.class);
				SaveGid.setGid(gid);
				startActivity(intent);

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

}
