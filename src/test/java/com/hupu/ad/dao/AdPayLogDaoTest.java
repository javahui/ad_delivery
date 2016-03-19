package com.hupu.ad.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.hupu.ad.dao.AdPayLogDao;

public class AdPayLogDaoTest extends AbstractDaoTest {

	@Resource
	private AdPayLogDao adPayLogDao;

	@Ignore
	@Test
	public void saveTest() {
		Map param = new HashMap();
		param.put("cid", 99999);
		param.put("adId", 999999);
		param.put("costMoney", 0.1);
		param.put("chargeMode", "CPM");
		List paramList = new ArrayList();
		paramList.add(param);
		adPayLogDao.batchInsert(paramList);
	}


}
