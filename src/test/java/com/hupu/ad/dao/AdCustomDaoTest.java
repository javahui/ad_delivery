package com.hupu.ad.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.hupu.ad.dao.AdCustomDao;
public class AdCustomDaoTest extends AbstractDaoTest{

	@Resource
	private AdCustomDao adCustomDao;
	
	@Test
	public void findByAdId(){
		adCustomDao.getAdIdsByUid(0);
	}
	
	@Test
	public void findUidByDkcode(){
		adCustomDao.findUidByDkcode(0);
	}
	
	@Ignore
	@Test
	public void saveOrUpdate(){
		Map param = new HashMap();
		param.put("uid", 0);
		param.put("dkcode", 0);
		param.put("adIds", "0,1");
		param.put("class", "1");
		adCustomDao.saveOrUpdate(param);
	}
}
