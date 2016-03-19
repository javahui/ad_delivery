package com.hupu.ad.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.hupu.ad.domain.AdLog;

@ContextConfiguration(locations = { "classpath*:spring/*.xml" })
public class AdLogQueueCacheTest extends AbstractJUnit4SpringContextTests{
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource
	private AdLogQueueCache adLogQueueCache;
	
	@Ignore
	@Test
	public void setTest(){
		List adLogs = new ArrayList();
		
		AdLog log = new AdLog();
		log.setAdId(0);
		
		AdLog log1 = new AdLog();
		log1.setAdId(1);
		
		AdLog log2 = new AdLog();
		log2.setAdId(2);
		adLogs.add(log2);
		
		adLogs.add(log1);
		adLogs.add(log);
		adLogQueueCache.setAdLogs(adLogs);
	}
	
	@Test
	public void getTest(){
		Collection<AdLog> adlogs = adLogQueueCache.getAdLogs(true);
		log.debug(adlogs.toString());
	}
	
	
}
