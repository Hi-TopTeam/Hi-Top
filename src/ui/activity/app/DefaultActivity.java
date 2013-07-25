package ui.activity.app;

import com.DreamTeam.HiTop.R;
import com.DreamTeam.HiTop.R.layout;
import com.DreamTeam.HiTop.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

//该Activity为测试用，可刪去
public class DefaultActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_default);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_default, menu);
		return true;
	}

}
