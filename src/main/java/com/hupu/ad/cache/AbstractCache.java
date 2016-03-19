package com.hupu.ad.cache;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.hupu.ad.domain.AdList;
import com.hupu.ad.domain.AdLog;
import com.hupu.ad.util.CacheKeyVersionUtils;

public class AbstractCache {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 广告缓存(默认)
	 */
	@Resource protected RedisTemplate<String, String> adCache;
	
	/**
	 * 用户信息缓存
	 */
	@Resource protected RedisTemplate<Object, Object> userCache;
	
	/**
	 * 日志队列缓存
	 */
	@Resource protected RedisTemplate<String, AdLog> logCache;
	
	/**
	 * 广告记录队列缓存
	 */
	@Resource protected RedisTemplate<String, AdList> listCache;
	
	
	/**
	 * 广告记录adList的缓存key的版本
	 */
	protected static String adListVersion = CacheKeyVersionUtils.getAdListCacheKeyVersion();
	
	/**
	 * 广告日志adLog的缓存key的版本
	 */
	protected static String adLogVersion = CacheKeyVersionUtils.getAdLogCacheKeyVersion();
}
