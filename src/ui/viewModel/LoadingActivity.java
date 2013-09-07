package ui.viewModel;



import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.DreamTeam.HiTop.R;

public class LoadingActivity extends Activity{

	public static Activity loadingActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.loading);
		
		loadingActivity = this;
   }
}