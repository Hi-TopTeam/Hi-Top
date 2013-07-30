package ui.activity.weather;

import java.util.List;

import com.DreamTeam.HiTop.R;

import foundation.webservice.WeatherService;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

import android.widget.SearchView;
import android.widget.Spinner;
import app.MainActivity;

public class OptionActivity extends Activity {
	private RadioButton rb_location;//单选框定位
	private RadioButton rb_select;//单选框选择
	private RadioGroup radioGroup;
	private Spinner sp_province;//省份下拉列表
	private Spinner sp_city;//城市下拉列表
	private static int select;//定位选择 0代表自动定位，1代表选择城市
	private static String province="";
	private static String city=null;
	private Button bt_ok;//确认按钮
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_option);
		Bundle bundle = getIntent().getExtras();
		select = bundle.getInt("select");
		rb_location = (RadioButton) findViewById(R.id.rb_location);
		rb_select = (RadioButton) findViewById(R.id.rb_select);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		sp_province = (Spinner) findViewById(R.id.sp_province);
		sp_city = (Spinner) findViewById(R.id.sp_city);
		bt_ok = (Button) findViewById(R.id.ok);
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(select==0){
			rb_location.setChecked(true);
		}else if(select==1){
			rb_select.setChecked(true);
		}
		select();
		bt_ok.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(select==0){
					DataDeliver.setSelect(select);
					OptionActivity.this.finish();
				}else if(select==1){
					DataDeliver.setSelect(select);
					DataDeliver.setCity(city);
					OptionActivity.this.finish();
				}
				
			}
		});
	}

	private void select(){
		if(select==1){
			sp_province.setEnabled(true);
			select=1;
			ArrayAdapter adapter_province;
			List<String> list_province;
			list_province = WeatherService.getProviceList();
			adapter_province = new ArrayAdapter(OptionActivity.this,R.layout.option_item,R.id.option_item,list_province);
			sp_province.setAdapter(adapter_province);
			sp_province.setOnItemSelectedListener(new spinner_provinceListen());
		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==rb_location.getId()){
					select=0;
					sp_province.setEnabled(false);
					sp_city.setEnabled(false);
				}else if(checkedId==rb_select.getId()){
					sp_province.setEnabled(true);
					select=1;
					ArrayAdapter adapter_province;
					List<String> list_province;
					list_province = WeatherService.getProviceList();
					adapter_province = new ArrayAdapter(OptionActivity.this,R.layout.option_item,list_province);
					adapter_province.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					sp_province.setAdapter(adapter_province);
					sp_province.setOnItemSelectedListener(new spinner_provinceListen());					
				}				
			}
		});
	}
	//spinner 省份选择监听
	class spinner_provinceListen implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			province = arg0.getItemAtPosition(arg2).toString();
			sp_city.setEnabled(true);	
			List<String> list_city;
			ArrayAdapter adapter_city;
			System.out.println(province);
			list_city = WeatherService.getCityListByProvince(province);
			adapter_city = new ArrayAdapter(OptionActivity.this,R.layout.option_item,list_city);
			adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			sp_city.setAdapter(adapter_city);
			sp_city.setOnItemSelectedListener(new spinner_cityListen());
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			Toast toast = Toast.makeText(OptionActivity.this, "未选择", Toast.LENGTH_SHORT);
			toast.show();
			
		}
		
	}
	//spinner 城市选择监听
	class spinner_cityListen implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			city = arg0.getItemAtPosition(arg2).toString();					
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			Toast toast = Toast.makeText(OptionActivity.this, "未选择", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
