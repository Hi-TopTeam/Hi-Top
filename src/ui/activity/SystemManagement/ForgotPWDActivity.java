package ui.activity.SystemManagement;

import java.util.HashMap;

import webservice.WebServiceDelegate;
import webservice.WebServiceUtils;

import com.DreamTeam.HiTop.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ForgotPWDActivity extends Activity implements WebServiceDelegate {
	private Button bt_forgot;
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgotpassword);
		webService = new WebServiceUtils(SERVICE_NS, SERVICE_URL, this);
		et_username = (EditText) findViewById(R.id.et_fgusername);
		et_password = (EditText) findViewById(R.id.et_fgpwd);
		et_passwordConfirm = (EditText) findViewById(R.id.et_fgpwdconfrim);
		et_nickname = (EditText) findViewById(R.id.et_fgnickname);
		bt_forgot = (Button) findViewById(R.id.detail_name);
		mProgress = (ProgressBar) findViewById(R.id.progressBar_fogot);
		mProgress.setIndeterminate(true);
		bt_forgot.setOnClickListener(new ForgotListener());
	}
	class ForgotListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			String username = et_username.getText().toString();
			String password = et_password.getText().toString();
			String passwordConfirm = et_passwordConfirm.getText().toString();
			String nickname = et_nickname.getText().toString();
			if (username.equals("") || password.equals("")
					|| passwordConfirm.equals("") || nickname.equals("")) {
				Toast toast = Toast.makeText(ForgotPWDActivity.this, "请填写完整",
						Toast.LENGTH_SHORT);
				toast.show();
			} else if (username
					.matches("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+"
							+ "(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$")) {
				if (!password.equals(passwordConfirm)) {
					Toast toast = Toast.makeText(ForgotPWDActivity.this,
							"两次次密码不匹配", Toast.LENGTH_SHORT);
					toast.show();
				} else {
					HashMap<String, Object> args = new HashMap<String, Object>();
					args.put("username", username);
					args.put("nickname", nickname);
					args.put("newPassword", password);
					mProgress.setVisibility(View.VISIBLE);
					webService.callWebService("forgotPassword", args, boolean.class);
				}
			} else {
				Toast toast = Toast.makeText(ForgotPWDActivity.this, "请输入正确的邮箱账号",
						Toast.LENGTH_SHORT);
				toast.show();
			}

			
		}
		
	}
	@Override
	public void handleException(Object ex) {
		Toast toast = Toast.makeText(ForgotPWDActivity.this, "请检查网络连接",
				Toast.LENGTH_SHORT);
		toast.show();
		
	}
	@Override
	public void handleResultOfWebService(String methodName, Object result) {
		boolean flag = (Boolean) result;
		if(flag == true){
			mProgress.setVisibility(View.GONE);
			Toast toast = Toast.makeText(ForgotPWDActivity.this, "新密码设置成功，请重新登陆",
					Toast.LENGTH_SHORT);
			toast.show();
			ForgotPWDActivity.this.finish();
		}else{
			Toast toast = Toast.makeText(ForgotPWDActivity.this, "验证信息不匹配",
					Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
