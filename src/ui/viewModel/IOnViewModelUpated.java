package ui.viewModel;

import java.util.List;

/**
 * 
 * @author 福建师范大学软件学院   倪友聪、赵康
 *
 */
public interface IOnViewModelUpated {

	//视图模型更新后的回调方法
	public void onUpdated(ViewModel vm);
	
	//发现视图模型错误时的回调方法
	public void onViewModelInError(List<ModelErrorInfo> errsOfVM);
	
}
