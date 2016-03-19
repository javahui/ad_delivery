package com.hupu.ad.dao;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.hupu.ad.dao.AdAttentionDao;
public class AdAttentionDaoTest extends AbstractDaoTest{

	@Resource
	private AdAttentionDao adAttentionDao;
	
	@Test
	public void findValid() {
		adAttentionDao.findValid();
	}
	
	@Ignore
	@Test
	public void addAttention(){
		adAttentionDao.addAttention(0,0);
	}
	
	@Ignore
	@Test
	public void delAttention(){
		adAttentionDao.delAttention(0,0);
	}
}
