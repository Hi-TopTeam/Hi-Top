package ui.activity.community;

import com.DreamTeam.HiTop.R;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

public class Communitymain extends ActivityGroup  implements OnTabChangeListener{

	
	TabHost myTabHost;
	TabWidget tabWidget;  
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.communiti_subtab);
		
		myTabHost = (TabHost)findViewById(R.id.community_sub_tab);
		myTabHost.setup(this.getLocalActivityManager());

		
		LayoutInflater inflater = LayoutInflater.from(this);
		
		inflater.inflate(R.layout.activity_community_main,myTabHost.getTabContentView());
		inflater.inflate(R.layout.activity_community_friends,myTabHost.getTabContentView());
		inflater.inflate(R.layout.activity_community_myjoin,myTabHost.getTabContentView());
	
		myTabHost.addTab(myTabHost.newTabSpec("好友列表").setIndicator("好友列表").setContent(new Intent(this,MyFriendsActivity.class)));
		myTabHost.addTab(myTabHost.newTabSpec("活动公告").setIndicator("活动公告").setContent(new Intent(this,CommunityActivity.class)));
		myTabHost.addTab(myTabHost.newTabSpec("我的参与").setIndicator("我的参与").setContent(new Intent(this,MyGroupsActivity.class)));
		myTabHost.setCurrentTab(1);
		
		myTabHost.setOnTabChangedListener(this);
		
		setAppear(1);
	}

	public void setAppear(int flag){
		tabWidget = (TabWidget) findViewById(android.R.id.tabs);  

		tabWidget.setBackgroundResource(R.drawable.black_bg);  
	      
	      for (int i = 0; i < tabWidget.getChildCount(); i++) {  
	          final TextView tv = (TextView) tabWidget.getChildAt(i)  
	                  .findViewById(android.R.id.title);  
	          tabWidget.getChildAt(i).getLayoutParams().height = 60;  
	          if (i == flag) {  
	              tabWidget.getChildAt(i).setBackgroundResource(  
	                      R.drawable.tab_bankground);//设置背景  
	              tv.setTextColor(Color.rgb(202, 151, 0));  
	          } else {  
	              tabWidget.getChildAt(i)  
	                      .setBackgroundResource(R.drawable.tab_bankground2);  
	              tv.setTextColor(Color.WHITE);  
	              tabWidget.getChildAt(i).setClickable(true);  
	          }  
	          tv.setTextSize(15);  
	      }
	    
	}
	
	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		
		int flag = myTabHost.getCurrentTab();
		setAppear(flag);
	}
	
	
	
}
