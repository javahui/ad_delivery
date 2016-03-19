package com.hupu.ad.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.hupu.ad.dao.AdCompanyCostDao;

public class AdCompanyCostDaoTest extends AbstractDaoTest {


	@Resource
	private AdCompanyCostDao adCompanyDao;

	@Ignore
	@Test
	public void testUpdateAdCompanyCostMoney() {
		List<Map> paramList = new ArrayList();
		Map param = new HashMap();
		param.put("costMoney", 0.1);
		param.put("cid", 1);
		paramList.add(param);
		
		adCompanyDao.batchUpdateCostMoney(paramList);
	}
	

}
