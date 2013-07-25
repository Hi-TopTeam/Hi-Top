package ui.viewModel;
/**
 * 
 * @author 福建师范大学软件学院   倪友聪、赵康
 *
 */
public class ModelErrorInfo {

	//错误属性名称
	private String errAttName;
	
	//错误消息
	
	private String errMsg;
	
	
	//获取属性名称
	public String getAttName() {
		return this.errAttName;
	}
	
	//设置属性名称
	public void setErrAttName(String aErrAttName){
		this.errAttName=aErrAttName;
	}
	
	//获取错误消息
	public String getErrMsg(){
		return this.errMsg;
	}
	//设置错误消息
	public void setErrMsg(String aErrMsg){
		this.errMsg=aErrMsg;
	}
	
	public ModelErrorInfo(){
		
	}
	
}
