package com.hupu.ad.dao;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.Resource;

import org.junit.Test;

import com.hupu.ad.dao.AdCrowdDao;
public class AdCrowdDaoTest extends AbstractDaoTest{

	@Resource
	private AdCrowdDao adCrowdDao;
	
//	@Test
//	public void findByAdId(){
//		int adId = 331;
//		adCrowdDao.findByAdId(adId);
//	}
	
	@Test
	public void findByAdIds(){
		Collection adIds = new ArrayList();
		adIds.add(331);
		adIds.add(332);
		adCrowdDao.findByAdId(adIds);
	}
	
//	@Test
//	public void findAgeByAdId(){
//		int adId = 331;
//		adCrowdDao.findAgeByAdId(adId);
//	}
//	
//	@Test
//	public void findAllAgeRange(){
//		adCrowdDao.findAllAgeRange();
//	}
}
