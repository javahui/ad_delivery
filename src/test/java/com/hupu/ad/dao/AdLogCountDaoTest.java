package com.hupu.ad.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.hupu.ad.domain.AdLogCount;

public class AdLogCountDaoTest extends AbstractDaoTest{

	@Resource
	private AdLogCountDao adLogCountDao;
	
	@Test
	public void findAll(){
		System.out.println(adLogCountDao.findAll());
	}
	
	@Ignore
	@Test
	public void saveAdLogCount(){
		AdLogCount adLogCount = new AdLogCount();
		adLogCount.setAdId(99999);
		adLogCount.setClickCount(999999);
		adLogCount.setShowCount(99999);
		adLogCount.setFansCount(9999);
		adLogCount.setDateline(999999);
		adLogCountDao.insert(adLogCount);
	}
	
	@Ignore
	@Test
	public void updateCountById(){
		AdLogCount adLogCount = new AdLogCount();
		adLogCount.setAdId(1);
		adLogCount.setClickCount(9);
		adLogCount.setDateline(1347292800);
		int updateNum = adLogCountDao.updateCountById(adLogCount);
		Assert.assertEquals(updateNum, 1);
	}
	
	@Ignore
	@Test
	public void batchIncrementCount(){
		List<AdLogCount> adLogCountList = new ArrayList();
		
		AdLogCount adLogCount1 = new AdLogCount();
		adLogCount1.setAdId(1);
		adLogCount1.setShowCount(11);
		adLogCount1.setClickCount(11);
		adLogCount1.setFansCount(11);
		adLogCount1.setDateline(999999999);
		adLogCountList.add(adLogCount1);
		
		AdLogCount adLogCount2 = new AdLogCount();
		adLogCount2.setAdId(999);
		adLogCount2.setShowCount(11);
		adLogCount2.setClickCount(11);
		adLogCount2.setDateline(1347292800);
		adLogCountList.add(adLogCount2);
		
		adLogCountDao.batchIncrementCount(adLogCountList);
	}
	
}
