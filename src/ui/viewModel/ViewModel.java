package ui.viewModel;

import java.util.ArrayList;
import java.util.List;

import ui.viewModel.attributeReflection.AttInfo;
import ui.viewModel.attributeReflection.AttributeReflection;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
/**
 * 
 * @author 福建师范大学软件学院   倪友聪、赵康
 *
 */
public abstract class ViewModel {

	static String tag = "ViewModel";

	/**
	 * 定义两个常量用ViewModel对象打包使用 CLASSNAME表示类名,FIELDLIST属性列表
	 */
	public static String CLASSNAME = "ClassName";
	public static String FIELDLIST = "FieldList";

	// 监听者列表
	List<IOnViewModelUpated> listeners = null;

	// 触发视图模型更新事件
	public void fireOnUpdted() {

		List<ModelErrorInfo> errs = verifyModel();
		// 检验视图模型是否正确
		if (errs == null) {//视图模型正确

			// 回调每一个监听者的onUpdated方法
			for (int i = 0; i < listeners.size(); i++) {
				listeners.get(i).onUpdated(this);
			}
		} else {//视图模型不正确;

			// 回调每一个监听者的onViewModelInError方法
			for (int i = 0; i < listeners.size(); i++) {
				listeners.get(i).onViewModelInError(errs);
			}
		}
	}

	/*
	 *检验模型，将出错属性及对应的错误消息构造ModelErrorInfo对象， 形成列表返回, 如果模型正确则返回null
	 */
	public abstract List<ModelErrorInfo> verifyModel();

	// 增加一个监听者
	public void addListener(IOnViewModelUpated aListener) {

		listeners.add(aListener);
	}

	// 删除一个监听者
	public Boolean removeListener(IOnViewModelUpated aListener) {
		return listeners.remove(aListener);
	}

	// 查找一个监听者
	public int findListener(IOnViewModelUpated aListener) {
		return listeners.indexOf(aListener);
	}

	public ViewModel() {
		this.listeners = new ArrayList<IOnViewModelUpated>();
	}

	/**
	 * 将ViewMode对象的每个属性，形成attInfo对象(可能含子对象)， 写入Bundle进行保存
	 * 
	 * @return: 返回ViewModel对象的打包,异常时返回null
	 */
	public Bundle writeToBundle() {
		Bundle bdl = null;
		List<AttInfo> attInfos = null;
		try {
			// 获取ViewModel对象对应的属性信息列表
			attInfos = AttributeReflection.getAttInfosFromObject(this);
			if (attInfos == null) {
				return bdl;
			}
			int len = attInfos.size();
			if (len > 0) {
				bdl = new Bundle();
				AttInfo[] arrayOfAttInfo=new AttInfo[len];
				for(int j=0;j<len;j++){
					arrayOfAttInfo[j]=attInfos.get(j);
				}
				
				bdl.putString(CLASSNAME, this.getClass().getName());
				bdl.putParcelableArray(FIELDLIST, arrayOfAttInfo);
			}
		} catch (InstantiationException e) {
			Log.e(tag, e.getMessage());
		} catch (IllegalArgumentException e) {
			Log.e(tag, e.getMessage());
		} catch (ClassNotFoundException e) {
			Log.e(tag, e.getMessage());
		} catch (IllegalAccessException e) {
			Log.e(tag, e.getMessage());
		}

		return bdl;

	}

	/**
	 * 从Pacel创建VieModel对象
	 * 
	 * @param in
	 *            :ViewModel对象的属性打包对象
	 * @return:返回解包后的ViewModel对象
	 * 
	 */
	public static ViewModel readFromBundle(Bundle in) {
		ViewModel vm = null;
		if(in==null){
			return vm;
		}
		String clsName = in.getString(CLASSNAME);
		Parcelable[] arrayOfField = in.getParcelableArray(FIELDLIST);

		if (clsName != null && arrayOfField!=null) {
			
			int len = arrayOfField.length;
			List<AttInfo> viewModelFields = new ArrayList<AttInfo>();

			for (int i = 0; i < len; i++) {
				viewModelFields.add((AttInfo) arrayOfField[i]);
			}
			try {
				vm = (ViewModel) AttributeReflection.getOjectFromAttInfos(clsName,
						viewModelFields);
			} catch (ClassNotFoundException e) {
				Log.e(tag, e.getMessage());
			} catch (InstantiationException e) {
				Log.e(tag, e.getMessage());
			} catch (IllegalAccessException e) {
				Log.e(tag, e.getMessage());
			} catch (NoSuchFieldException e) {
				Log.e(tag, e.getMessage());
			}
		}
		return vm;
	};

}
