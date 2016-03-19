package com.hupu.ad.service;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.hupu.ad.service.AdWeightsService;

@ContextConfiguration(locations = { "classpath*:spring/*.xml" })
public class AdWeightsServiceTest extends AbstractJUnit4SpringContextTests{
	public final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource private AdWeightsService adWeightsService;
	
	/*//@Test
	public void getWebAd(){
		int uid=1000001552;
		String interest = "96482";
		int num = 1;
		int webId = 1465;
		
		adListGetService.getWebAd(uid, interest, num, webId);
	}*/
	
	@Test
	public void weightSort(){
		int num = 4;
		Set<String> adIds = new HashSet<String>();
		adIds.add(String.valueOf((240)));
		adIds.add(String.valueOf((241)));
		adIds.add(String.valueOf((243)));
		//adIds.add(String.valueOf((244)));
		//adIds.add(String.valueOf((253)));
		
		adIds = adWeightsService.weightSort(adIds, num);
		log.debug(adIds.toString());
	}
	
}
