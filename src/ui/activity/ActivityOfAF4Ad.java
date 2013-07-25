package ui.activity;

import java.util.List;

import ui.viewModel.IOnViewModelUpated;
import ui.viewModel.ModelErrorInfo;
import ui.viewModel.ViewModel;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import app.MyApplication;
import com.DreamTeam.HiTop.*;
/**
 * 
 * @author 福建师范大学软件学院   倪友聪、赵康
 * 
 */
public abstract class ActivityOfAF4Ad extends Activity {
	// tag标记，用于输出调试信息
	static String tag = "MyApplication";

	// 视图模型
	protected ViewModel vm;

	public ViewModel getVm() {
		return vm;
	}

	public void setVm(ViewModel aVM) {
		if (this.vm != aVM) {
			this.vm = aVM;
			// 触发视图模型更新事件
			if (vm != null) {
				vm.fireOnUpdted();
			}
		}
	}

	// 视图模型更新的监听器
	private OnViewModelUpdated listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		// 加载视图
		setContentView(R.layout.activity_af4ad);
		// 初始化视图模型
		this.vm = null;

		// 考虑Activity跳转时，获取从MyApplication转发过来的ViewModel
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			this.vm = ViewModel.readFromBundle(bundle);
		}

		// 构造监听器
		setListener(new OnViewModelUpdated());
		Log.d(tag, "in OnCreate...");
	}

	@Override
	protected void onPostCreate(android.os.Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		Log.d(tag, "in onPostCreate...");

		// 获取初始视图模型
		if (vm == null) {
			vm = initModel();
		}

		Log.d(tag, "vm intialized...");

		// 获取操作控件并注册控件事件
		initControlsAndRegEvent();

		Log.d(tag, "controls obtained....");

		// 注册视图模型的监听者
		if (vm != null) {

			int position = vm.findListener(getListener());
			Log.d(tag, "finding listener...");

			// 是否已加入过
			if (position == -1) {
				// 注册视图模型的监听者
				vm.addListener(getListener());
				Log.d(tag, "listener added");
			}

		}
		// 更新视图
		if (vm != null) {
			upDateView(vm);
		}

	}

	@Override
	protected void onDestroy() {
		if (vm != null) {
			int position = vm.findListener(getListener());
			if (position > -1) {
				vm.removeListener(getListener());
			}
		}
		super.onDestroy();
	}

	// 获取操作控件并注册控件事件
	protected abstract void initControlsAndRegEvent();

	// 获取初始的视图模型
	protected abstract ViewModel initModel();

	// 更新视图
	protected abstract void upDateView(ViewModel aVM);

	/**
	 * 处理视图模型错误
	 * @param errsOfVM:第一个参数给出了出错的属性名和出错消息，
	 * @param errMsg:按每个属性出错误一行的格式给出错误消息
	 */
	protected abstract void processViewModelErrorMsg(
			List<ModelErrorInfo> errsOfVM, String errMsg);

	/**
	 * 视图模型更新的监听器类
	 **/
	/**
	 * 
	 * @author 福建师范大学软件学院   倪友聪、赵康
	 *
	 */
	private class OnViewModelUpdated implements IOnViewModelUpated {

		@Override
		// 视图模型更新后的回调方法
		public void onUpdated(ViewModel vm) {
			upDateView(vm);
		}

		@Override
		// 发现视图模型错误时的回调方法
		public void onViewModelInError(List<ModelErrorInfo> errsOfVM) {
			String errTxt = "";

			for (int i = 0; i < errsOfVM.size(); i++) {
				errTxt += errsOfVM.get(i).getErrMsg() + "\n";
			}
            //调用子类的处理视图模型错误方法
			processViewModelErrorMsg(errsOfVM, errTxt);
		}

	}

	/**
	 * 跳转至其它Activity
	 * @param frmActObj:源Activity对象
	 * @param toActCls:目标Activity类的描述对象可用ActivityX.class
	 */
	protected void toActivity(Activity frmActObj, Class<?> toActCls) {
		
		//设定Activity跳转广播
		Intent toAppIntent = new Intent("ViewForwardBroadcast");
		
		String frmActClsName=frmActObj.getClass().getName();
		String toActClsName=toActCls.getName();
		toAppIntent.putExtra("frmActClsNm", frmActClsName);
		toAppIntent.putExtra("toActClsNm", toActClsName);

		//将源Activity的ViewMode打包
		if (this.vm != null) {
			Bundle bdlOfFrmAct = new Bundle();
			bdlOfFrmAct = vm.writeToBundle();
			toAppIntent.putExtras(bdlOfFrmAct);
		}
		
        //设定接收广播对象
		IntentFilter filter = new IntentFilter("ViewForwardBroadcast");
		registerReceiver(MyApplication.VWFWDRCV, filter);
		
		//发送广播
		sendBroadcast(toAppIntent);

	}

	public OnViewModelUpdated getListener() {
		return listener;
	}

	public void setListener(OnViewModelUpdated listener) {
		this.listener = listener;
	}
}
