package app;

import ui.viewModel.ViewModel;

import foundation.data.DataHelper;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * 
 * @author 福建师范大学软件学院 倪友聪、赵康
 * 
 */
public class MyApplication extends Application {

	// 数据库助手
	public static DataHelper DATAHELPER;

	// 数据库名
	public static String DATAFILENAME = "myApp.db";

	// 登录用户

	// Activity跳转广播接收器
	public static ViewFwdRcv VWFWDRCV;

	@Override
	public void onCreate() {

		super.onCreate();
		// 初始化全局变量
		DATAHELPER = new DataHelper(getApplicationContext(), DATAFILENAME);
		VWFWDRCV = new ViewFwdRcv();
	}

	// Activity跳转广播接收器类的定义
	public class ViewFwdRcv extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {

			// 获取源源Activity和目标源Activity的名字
			String frmActClsNm = arg1.getStringExtra("frmActClsNm");
			String toActClsNm = arg1.getStringExtra("toActClsNm");

			if (frmActClsNm.equals(toActClsNm) || frmActClsNm == null
					|| toActClsNm == null) {
				return;
			}

			// 源Activity的ViewModel对象
			ViewModel ObjOfFrmActVM = null;

			// 目标Activity的ViewModel对象
			ViewModel ObjOfToActVM = null;

			// 源Activity中ViewModel对象的打包对象
			Bundle bdlOfFrmAct = arg1.getExtras();

			// 目标Activity中ViewModel对象的打包对象
			Bundle bdlOfToAct = null;

			if (bdlOfFrmAct != null) {
				// 获取源Activity的ViewModel对象
				ObjOfFrmActVM = ViewModel.readFromBundle(bdlOfFrmAct);
			}
			// 依据源Activity的ViewModel对象，变换生成目标Activity的ViewModel对象
			ObjOfToActVM = ViewModelTansformation(frmActClsNm, ObjOfFrmActVM,
					toActClsNm);

			// 打包目标Activity的ViewModel对象
			if (ObjOfToActVM != null) {
				bdlOfToAct = ObjOfToActVM.writeToBundle();
			}

			Class<?> clsOfToAct = null;
			try {
				clsOfToAct = Class.forName(toActClsNm);

				Intent toActIntent = new Intent(MyApplication.this, clsOfToAct);
				if (bdlOfToAct != null) {
					toActIntent.putExtras(bdlOfToAct);
				}
				toActIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(toActIntent);
			} catch (ClassNotFoundException e) {
				Log.e(toActClsNm, "目标Activity的类找不到");
			}

		}
	}

	protected ViewModel ViewModelTansformation(String frmActClsNm,
			ViewModel objOfFrmActVM, String toActClsNm) {

		ViewModel resultVMofToAct = null;

		/*
		 * if (frmActClsNm.equals(Activity1.class.getName()) &&
		 * toActClsNm.equals(Aoivtity2.class.getName())){ // 强制类型转换 if
		 * (objOfFrmActVM != null) { ViewModelX objOfVMX = (ViewModelX)
		 * objOfFrmActVM; ViewModelY objOfVMY = new ViewModelY();
		 * 
		 * //视图模型变换 objOfVMY.setAge2(objOfVMX.getAge() + 1); resultVMofToAct =
		 * objOfVMY; } }
		 */

		return resultVMofToAct;
	}
}
