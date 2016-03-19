package com.hupu.ad.dao;

import javax.annotation.Resource;

import org.junit.Test;

import com.hupu.ad.dao.AdListDao;
public class AdListDaoTest extends AbstractDaoTest{

	@Resource
	private AdListDao adListDao;
	
	@Test
	public void AdListDao(){
		int adId = 1;
		adListDao.findNormalByAdId(adId);
	}
	
	@Test
	public void find(){
		adListDao.find(283);
	}
	
	@Test
	public void findByCurdate(){
		adListDao.findByCurdate();
	}
	
	@Test
	public void findCompanyMoneyOut(){
		adListDao.findCompanyMoneyOut();
	}
	
	@Test
	public void findByCid(){
		adListDao.findByCid(0);
	}
	
	@Test
	public void findNormal(){
		adListDao.findNormal();
	}
}
