package com.hupu.core.remote;


public interface RemoteInterface {

	
	public Object invoke(String springBeanName, String methodName, Object arg);
	
	public Object invoke(String springBeanName, String methodName, Object arg1, Object arg2);
	
	public Object invoke(String springBeanName, String methodName, Object arg1, Object arg2, Object arg3);
	
    
}
