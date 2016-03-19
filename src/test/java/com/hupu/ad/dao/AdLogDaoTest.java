package com.hupu.ad.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.hupu.ad.dao.AdLogDao;
import com.hupu.ad.domain.AdLog;

public class AdLogDaoTest extends AbstractDaoTest {

	@Autowired
	private AdLogDao adLogDao;
	
	@Ignore
	@Test
	public void findIncome(){
		adLogDao.findIncome();
	}
	
	//@Ignore
	@Test
	public void findStatistics(){
		adLogDao.findStatistics();
	}
	
	@Ignore
	@Test
	public void findLogCount(){
		adLogDao.findLogCount();
	}
	
	@Test
	public void isFindTodayClick(){
		AdLog adLog = new AdLog();
		adLog.setAdId(585);
		adLog.setActionDkcode(100025);
		adLogDao.isFindTodayClick(adLog);
	}
	
	@Ignore
	@Test
	public void batchInsertTest() {
		List<AdLog> list = new ArrayList<AdLog>();
		for (int i = 0; i < 1; i++) {
			AdLog log = new AdLog();
			log.setCid(1000001031);
			log.setAdId(0);
			log.setEventType(0);
			log.setUrl("Junit url");
			log.setIp("test");
			log.setDateline(99999);
			log.setIsUser(0);
			log.setActionDkcode(0);
			list.add(log);
		}
		adLogDao.batchInsert(list);
	}
	
	@Ignore
	@Test
	public void updateIncomeInValid() {
		adLogDao.updateIncomeInValid();
	}

}
