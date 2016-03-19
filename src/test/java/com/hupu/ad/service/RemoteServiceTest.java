package com.hupu.ad.service;

import com.caucho.hessian.client.HessianProxyFactory;
import com.hupu.core.remote.RemoteInterface;

public class RemoteServiceTest {
	public static void main(String[] args) throws Exception {
		String url = "http://127.0.0.1:8080/dksns-ad-web/hessian/remote";
		HessianProxyFactory factory = new HessianProxyFactory();
		RemoteInterface remoteInterface = (RemoteInterface) factory.create(url);
		Object result = remoteInterface.invoke("springBeanName", "methodName", null);
		System.out.println(result);
	}
}
