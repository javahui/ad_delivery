package com.hupu.ad.cache;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.hupu.ad.cache.AdListCache;

/**
 * 广告记录缓存操作类
 * @author donghui
 */
@ContextConfiguration(locations = { "classpath*:spring/*.xml" })
public class AdListCacheTest extends AbstractJUnit4SpringContextTests{
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	/**
	 * 广告表DAO
	 */
	@Resource 
	private AdListCache adListCache;
	
	@Test
	public void getAdListTest(){
		log.debug("缓存adList记录:{}",adListCache.getAdList(282));
	}
}