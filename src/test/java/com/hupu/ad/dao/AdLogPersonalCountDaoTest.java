package com.hupu.ad.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.hupu.ad.dao.AdLogPersonalCountDao;
import com.hupu.ad.domain.AdLogPersonalCount;
public class AdLogPersonalCountDaoTest extends AbstractDaoTest{

	@Resource
	private AdLogPersonalCountDao adLogPersonalCountDao;
	
	@Test
	public void findAll() {
		adLogPersonalCountDao.findAll();
	}
	
	@Ignore
	@Test
	public void saveAdLogCount(){
		AdLogPersonalCount adLogPersonalCount = new AdLogPersonalCount();
		adLogPersonalCount.setAdId(0);
		adLogPersonalCount.setClickCount(10);
		adLogPersonalCount.setShowCount(10);
		adLogPersonalCount.setFansCount(10);
		adLogPersonalCount.setDkcode(0);
		adLogPersonalCountDao.insert(adLogPersonalCount);
	}
	
	
	@Ignore
	@Test
	public void batchIncrementCount(){
		List<AdLogPersonalCount> list = new ArrayList();
		
		AdLogPersonalCount obj1 = new AdLogPersonalCount();
		obj1.setAdId(0);
		obj1.setShowCount(11);
		obj1.setClickCount(11);
		obj1.setFansCount(11);
		obj1.setDkcode(0);
		list.add(obj1);
		
		AdLogPersonalCount obj2 = new AdLogPersonalCount();
		obj2.setAdId(999);
		obj2.setShowCount(11);
		obj2.setClickCount(11);
		obj2.setDkcode(10032);
		list.add(obj2);
		
		adLogPersonalCountDao.batchIncrementCount(list);
	}
	
}
