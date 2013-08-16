package ui.activity.GoogleMap;

/**
 * @author DreamTeam 郑运春
 */

import java.util.List;


import ui.activity.GoogleMap.NewGMapFragment.ItemClickEventGmap;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;



public class MyDefinedMenu extends PopupWindow { 
	
	private LinearLayout layout;	 
	private GridView gv_title;		 
	private GridView gv_body;		 
	private BodyAdatper[] bodyAdapter;	 
	private TitleAdatper titleAdapter;	 
	private Context context;			 
	private int titleIndex;				 
	public int currentState;		
	
	
	public MyDefinedMenu(Context context, List<String> titles, 
			List<List<String>> item_names, List<List<Integer>> item_images,
			ItemClickEventGmap itemClickEvent) {
		
		super(context);
		this.context = context;
		currentState = 1;
		
	
		layout = new LinearLayout(context);		
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
	
	    titleIndex = 0;
		gv_title = new GridView(context);
		titleAdapter = new TitleAdatper(context, titles);
		gv_title.setAdapter(titleAdapter);
		gv_title.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		gv_title.setNumColumns(titles.size());	 
		//gv_title.setBackgroundColor(Color.WHITE);
		gv_title.setBackgroundColor(Color.GRAY);
		
		bodyAdapter = new BodyAdatper[item_names.size()];	 
		for (int i = 0; i < item_names.size(); i++) {
			bodyAdapter[i] = new BodyAdatper(context, item_names.get(i), item_images.get(i));
		}
		gv_body = new GridView(context);
		gv_body.setNumColumns(4);	 
		//设置背景为半透明
		//gv_body.setBackgroundColor(Color.TRANSPARENT);
		gv_body.setBackgroundColor(Color.WHITE);
		gv_body.setAdapter(bodyAdapter[0]);	 
		
		gv_title.setOnItemClickListener(new OnItemClickListener() {

			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				titleIndex = arg2;	 
				titleAdapter.setFocus(arg2);
				gv_body.setAdapter(bodyAdapter[arg2]); 

			}
		});
		
		gv_body.setOnItemClickListener(itemClickEvent);
		
		layout.addView(gv_title);
		layout.addView(gv_body);
		
		this.setContentView(layout);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
	}
	


	public int getTitleIndex() {
		
		return titleIndex;
	}
	
}
