package com.hupu.ad.util;

import java.util.ResourceBundle;

/**
 * 获取cache_manager_login.properties文件属性
 * @author zzy
 */
public class CacheManagerLoginUtils {
	private final static ResourceBundle cacheManagerLogin = ResourceBundle.getBundle("properties/cache_manager_login");
	
	public static String getUsername(){
		return cacheManagerLogin.getString("username");
	}
	
	public static String getPassword(){
		return cacheManagerLogin.getString("password");
	}
}
