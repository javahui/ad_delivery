package com.hupu.ad.scheduling;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.stereotype.Component;

import com.hupu.ad.dao.AbstractDaoTest;
import com.hupu.ad.scheduling.NightScheduling;
/**
 * 定时运行数据校验任务
 * @author donghui
 */
@Component
public class NightSchedulingTest extends AbstractDaoTest{
	
	@Resource private NightScheduling nightScheduling;
	
	@Ignore
	@Test
	public void checkCrowd(){
		nightScheduling.addCrowd();
	}
	
}