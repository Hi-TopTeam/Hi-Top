package webservice;

public interface WebServiceDelegate {
	//�����쳣
	public void handleException(Exception ex);
	//����WebService�ķ���ֵ
	public void handleResultOfWebService(String methodName,Object result);
}
