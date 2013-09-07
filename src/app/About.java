package app;

import com.DreamTeam.HiTop.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class About extends Activity {
	private boolean flag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		flag = false;
	}

	// 下面是四个按钮点击的事件响应
	public void onclick_bangbengenxin(View v) {
		Toast.makeText(getApplicationContext(), "已是最新版本", Toast.LENGTH_SHORT)
				.show();
	}

	public void onclick_gongnengjieshao(View v) {
		Toast.makeText(getApplicationContext(), "没功能，别摁啦", Toast.LENGTH_SHORT)
				.show();
	}

	public void onclick_canyurenyuan(View v) {
		// Toast.makeText(getApplicationContext(), "都说摁了，还摁！！！！",
		// Toast.LENGTH_SHORT)
		// .show();
		setContentView(R.layout.partner);
		flag = true;
	}

	public void onclick_bangzuyufangkuai(View v) {
		Toast.makeText(getApplicationContext(), "不理你了！！", Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && flag == true) {
			setContentView(R.layout.about);
			flag = false;
			return false;
		}if (keyCode == KeyEvent.KEYCODE_BACK && flag == false) {
			return super.onKeyDown(keyCode, event);
		}
		return false;
		
	}

}
