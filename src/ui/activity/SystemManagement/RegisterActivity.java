package ui.activity.SystemManagement;

import java.util.HashMap;

import webservice.WebServiceDelegate;
import webservice.WebServiceUtils;

import com.DreamTeam.HiTop.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import app.MainActivity;

public class RegisterActivity extends Activity implements WebServiceDelegate {

	private Button bt_ok;
	private Button bt_cancel;
	private EditText et_username;
	private EditText et_password;
	private EditText et_passwordConfirm;
	private EditText et_nickname;
	private WebServiceUtils webService;
	private final String SERVICE_NS = "http://SystemManager.szp.com/";
	private final String SERVICE_URL = "http://110.84.129.43:8009/szpWebService/SystemManagement";
	private ProgressBar mProgress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		webService = new WebServiceUtils(SERVICE_NS, SERVICE_URL, this);
		bt_ok = (Button) findViewById(R.id.detail_info);
		bt_cancel = (Button) findViewById(R.id.name);
		et_username = (EditText) findViewById(R.id.et_rgusername);
		et_password = (EditText) findViewById(R.id.et_rgpassword);
		et_passwordConfirm = (EditText) findViewById(R.id.et_repasswordconfirm);
		et_nickname = (EditText) findViewById(R.id.et_rgnickname);
		mProgress = (ProgressBar) findViewById(R.id.progressBar_register);
		mProgress.setIndeterminate(true);
		bt_ok.getBackground().setAlpha(100);
		bt_cancel.getBackground().setAlpha(100);
		bt_ok.setOnClickListener(new registerListener());
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
				startActivity(intent);
				RegisterActivity.this.finish();

			}
		});
	}

	class registerListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			String username = et_username.getText().toString();
			String password = et_password.getText().toString();
			String passwordConfirm = et_passwordConfirm.getText().toString();
			String nickname = et_nickname.getText().toString();
			if (username.equals("") || password.equals("")
					|| passwordConfirm.equals("") || nickname.equals("")) {
				Toast toast = Toast.makeText(RegisterActivity.this, "请填写完整",
						Toast.LENGTH_SHORT);
				toast.show();
			} else if (username
					.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+"
							+ "(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$")) {
				if (!password.equals(passwordConfirm)) {
					Toast toast = Toast.makeText(RegisterActivity.this,
							"两次次密码不匹配", Toast.LENGTH_SHORT);
					toast.show();
				} else {
					HashMap<String, Object> args = new HashMap<String, Object>();
					args.put("username", username);
					args.put("password", password);
					args.put("nickname", nickname);
					mProgress.setVisibility(View.VISIBLE);
					webService.callWebService("register", args, boolean.class);
				}
			} else {
				Toast toast = Toast.makeText(RegisterActivity.this, "请输入正确的邮箱账号",
						Toast.LENGTH_SHORT);
				toast.show();
			}

		}

	}

	@Override
	public void handleException(Object ex) {
		Toast toast = Toast.makeText(RegisterActivity.this, "请检查网络连接",
				Toast.LENGTH_SHORT);
		toast.show();

	}

	@Override
	public void handleResultOfWebService(String methodName, Object result) {
		boolean flag = (Boolean) result;		
		if (flag == true) {
			Toast toast = Toast.makeText(RegisterActivity.this, "注册成功",
					Toast.LENGTH_SHORT);
			toast.show();
			Intent intent = new Intent(RegisterActivity.this,
					MainActivity.class);
			mProgress.setVisibility(View.GONE);
			startActivity(intent);
			RegisterActivity.this.finish();
		} else {
			Toast toast = Toast.makeText(RegisterActivity.this, "注册失败,用户名已存在",
					Toast.LENGTH_SHORT);
			toast.show();
		}

	}

}
