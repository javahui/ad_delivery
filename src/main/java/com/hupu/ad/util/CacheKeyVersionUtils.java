package com.hupu.ad.util;

import java.util.ResourceBundle;

/**
 * 获取cache_key_version.properties文件属性
 * @author donghui
 */
public class CacheKeyVersionUtils {
	private final static ResourceBundle cahceKeyVersion = ResourceBundle.getBundle("properties/cache_key_version");
	
	public static String getAdListCacheKeyVersion(){
		return cahceKeyVersion.getString("AdList");
	}
	
	public static String getAdLogCacheKeyVersion(){
		return cahceKeyVersion.getString("AdLog");
	}
}
