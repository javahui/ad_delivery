package com.hupu.ad.scheduling;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.stereotype.Component;

import com.hupu.ad.dao.AbstractDaoTest;
import com.hupu.ad.scheduling.CheckScheduling;
/**
 * 定时运行数据校验任务
 * @author donghui
 */
@Component
public class CheckSchedulingTest extends AbstractDaoTest{
	
	@Resource private CheckScheduling checkScheduling;
	
	@Ignore
	@Test
	public void checkCrowd(){
		checkScheduling.checkCrowd();
	}
	
	@Ignore
	@Test
	public void checkAdLogCount(){
		checkScheduling.checkAdLogCount();
	}
	
	@Ignore
	@Test
	public void checkAdLogPersonalCount(){
		checkScheduling.checkAdLogPersonalCount();
	}
	
	@Ignore
	@Test
	public void checkIncome(){
		checkScheduling.checkIncome();
	}
	
	@Ignore
	@Test
	public void checkCacheAttention(){
		checkScheduling.checkCacheAttention();
	}
	
	//@Ignore
	@Test
	public void checkAllAgeRangeCache(){
		checkScheduling.checkAllAgeRangeCache();
	}
}