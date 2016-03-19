package com.hupu.ad;


import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.hupu.ad.domain.AdList;
import com.hupu.ad.domain.AdLog;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@ContextConfiguration(locations = { "classpath*:spring/*.xml" })
public class RedisTest extends AbstractJUnit4SpringContextTests{

	@Resource private RedisTemplate<String, String> adCache;
	@Resource private RedisTemplate<Object, Object> userCache;
	@Resource private RedisTemplate<String, AdLog> logCache;
	@Resource private RedisTemplate<String, AdList> listCache;
	
	private static final String STATISTICE_HASHKEY = "ads_statistics";
	@Test
	public void test() throws Exception {
		BoundHashOperations hashOps = adCache.boundHashOps(STATISTICE_HASHKEY);
		 hashOps.entries();

	}
	
}
