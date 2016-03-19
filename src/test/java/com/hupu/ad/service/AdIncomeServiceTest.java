package com.hupu.ad.service;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.hupu.ad.service.AdIncomeService;

@ContextConfiguration(locations = { "classpath*:spring/*.xml" })
public class AdIncomeServiceTest extends AbstractJUnit4SpringContextTests{
	public final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource private AdIncomeService adIncomeService;
	
	@Ignore
	@Test
	public void income(){
		adIncomeService.income();
	}
	
}
