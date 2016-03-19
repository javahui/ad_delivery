package com.hupu.ad.service;

import java.util.Collection;
import java.util.HashSet;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.hupu.ad.domain.AdLog;
import com.hupu.ad.service.AdLogService;
import com.hupu.ad.util.UnixTimeUtils;

@ContextConfiguration(locations = { "classpath*:spring/*.xml" })
public class AdLogServiceTest extends AbstractJUnit4SpringContextTests{
	public final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource private AdLogService adLogService;
	
	@Ignore
	@Test
	public void saveLogAndLogCount(){
		Collection<AdLog> adLogs = new HashSet();
		int currentTime = UnixTimeUtils.getCurrentUnixTimeSS();
		AdLog adlog = new AdLog();
		adlog.setAdId(1);
		adlog.setEventType(1);
		adlog.setDateline(currentTime);
		
		AdLog adlog1 = new AdLog();
		adlog1.setAdId(1);
		adlog1.setEventType(1);
		adlog1.setDateline(currentTime);
		
		AdLog adlog2 = new AdLog();
		adlog2.setAdId(2);
		adlog2.setEventType(1);
		adlog2.setDateline(currentTime);
		
		adLogs.add(adlog);
		adLogs.add(adlog1);
		adLogs.add(adlog2);
		
		adLogService.saveLogAndLogCount(adLogs);
	}
	
}
