package webservice;

public interface WebServiceDelegate {
	//处理异常
	public void handleException(Exception ex);
	//处理WebService的返回值
	public void handleResultOfWebService(String methodName,Object result);
}
