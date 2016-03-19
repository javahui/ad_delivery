package com.hupu.ad.dao;

import javax.annotation.Resource;

import org.junit.Test;

import com.hupu.ad.dao.AdConfigDao;

public class AdConfigDaoTest extends AbstractDaoTest{

	@Resource
	private AdConfigDao adConfigDao;
	@Test
	public void findAssign(){
		adConfigDao.findAssign();
	}
	
	@Test
	public void findBy(){
		adConfigDao.isCurrentTimeFree();
	}
}
