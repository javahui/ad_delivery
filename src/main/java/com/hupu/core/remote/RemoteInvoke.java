package com.hupu.core.remote;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

@Component
public class RemoteInvoke implements RemoteInterface{
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public Object invoke(String springBeanName, String methodName, Object arg) {
		return this.springInvokeMethod(springBeanName, methodName, arg);
	}
	
	@Override
	public Object invoke(String springBeanName, String methodName, Object arg1, Object arg2) {
		return this.springInvokeMethod(springBeanName, methodName, arg1, arg2);
	}

	@Override
	public Object invoke(String springBeanName, String methodName, Object arg1, Object arg2, Object arg3) {
		return this.springInvokeMethod(springBeanName, methodName, arg1, arg2, arg3);
	}
	
	private Object springInvokeMethod(String springBeanName, String methodName, Object... args) {
		Object clz = ContextLoader.getCurrentWebApplicationContext().getBean(springBeanName);
		Throwable throwable = null;
		try {
			Object result = MethodUtils.invokeMethod(clz, methodName, args);
			log.debug("Hession远程调用springBean:{},方法名:{},参数:{} 结果:{}",new Object[]{springBeanName, methodName, args, result});
			return result;
		}
		catch (InvocationTargetException e) {
			throwable = e.getTargetException();
		}
		catch (Exception e) {
			throwable = e ;
		}
		String messageException = ExceptionUtils.getMessage(throwable);
		String argsStr = ArrayUtils.toString(args);
		int lineNum = throwable.getStackTrace()[0].getLineNumber();
		log.error("springBeanName:{} 方法名:{} 参数:{} 异常信息:{} 行数:{}", new Object[]{springBeanName, methodName, argsStr, messageException, lineNum});
		return 0;
	}

}
