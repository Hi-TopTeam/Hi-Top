package ui.activity.SystemManagement;

import java.util.HashMap;

import webservice.WebServiceDelegate;
import webservice.WebServiceUtils;

import com.DreamTeam.HiTop.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import app.MainActivity;

public class LoginActivity extends Activity implements WebServiceDelegate {
	private Button bt_login;
	private TextView tv_register;
	private TextView tv_forget;
	private EditText et_username;
	private EditText et_password;
	private WebServiceUtils webservice;
	private final String SERVICE_NS = "http://SystemManager.szp.com/";
	private final String SERVICE_URL = "http://110.84.129.43:8009/szpWebService/SystemManagement";
	private SharedPreferences sp;
	private SharedPreferences sp2;
	private ProgressBar mProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		sp = getSharedPreferences("Login_status", MODE_PRIVATE);
		sp2 = getSharedPreferences("login_user", MODE_PRIVATE);
		if (sp.getBoolean("login_in", false)) {
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			this.finish();
		}
		bt_login = (Button) findViewById(R.id.info);
		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_forget = (TextView) findViewById(R.id.tv_forget);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		mProgress = (ProgressBar) findViewById(R.id.progressBar_login);
		mProgress.setIndeterminate(true);
		webservice = new WebServiceUtils(SERVICE_NS, SERVICE_URL, this);
		bt_login.setOnClickListener(new LoginListener());
		tv_forget.setOnClickListener(new ForgotListener());
		tv_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
				LoginActivity.this.finish();
			}
		});
	}

	class LoginListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String username = et_username.getText().toString();
			String password = et_password.getText().toString();
			if (username.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+"
					+ "(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$")) {
				Editor editor = sp2.edit();
				editor.putString("user", username);
				editor.commit();
				HashMap<String, Object> args = new HashMap<String, Object>();
				args.put("username", username);
				args.put("password", password);
				mProgress.setVisibility(View.VISIBLE);
				webservice.callWebService("login", args, boolean.class);
			} else {
				Toast toast = Toast.makeText(LoginActivity.this, "请输入正确的邮箱账号",
						Toast.LENGTH_SHORT);
				toast.show();
			}
		}
	}
	class ForgotListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(LoginActivity.this,ForgotPWDActivity.class);
			startActivity(intent);
		}
		
	}
	@Override
	public void handleException(Object ex) {
		Toast toast = Toast.makeText(LoginActivity.this, "请检查网络连接",
				Toast.LENGTH_SHORT);
		toast.show();
	}

	@Override
	public void handleResultOfWebService(String methodName, Object result) {
		boolean flag = (Boolean) result;
		if (flag == true) {
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			Editor editor = sp.edit();
			editor.putBoolean("login_in", true);
			editor.commit();
			mProgress.setVisibility(View.GONE);
			startActivity(intent);
			LoginActivity.this.finish();
		} else {
			Toast toast = Toast.makeText(LoginActivity.this, "用户名或密码错误",
					Toast.LENGTH_SHORT);
			toast.show();
		}

	}

}
