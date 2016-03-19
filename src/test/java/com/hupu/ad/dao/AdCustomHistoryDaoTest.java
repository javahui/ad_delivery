package com.hupu.ad.dao;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.hupu.ad.dao.AdCustomHistoryDao;
public class AdCustomHistoryDaoTest extends AbstractDaoTest{

	@Resource
	private AdCustomHistoryDao adCustomHistoryDao;
	
	@Ignore
	@Test
	public void insert(){
		Map param = new HashMap();
		param.put("dkcode", 0);
		param.put("adIds", 0);
		param.put("dateline", 0);
		adCustomHistoryDao.insert(param);
	}
}
