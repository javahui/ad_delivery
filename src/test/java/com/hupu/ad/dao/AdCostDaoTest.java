package com.hupu.ad.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.hupu.ad.dao.AdCostDao;

public class AdCostDaoTest extends AbstractDaoTest {


	@Resource
	private AdCostDao adCostDao;

	@Test
	public void findByAdIds() {
		ArrayList list = new ArrayList();
		adCostDao.findByAdIds(list);
	}
	
	@Test
	public void findOut() {
		adCostDao.findOut();
	}
	
	@Test
	public void findValid() {
		adCostDao.findValid(new String[]{"1","0"});
	}
	
	@Test
	public void isValidByAdId(){
		ArrayList<Integer> adIds = new ArrayList();
		adIds.add(12);
		adCostDao.findInvalid(adIds);
	}
	
	@Test
	public void findBidByAdIds(){
		List<Integer> adIds = new ArrayList<Integer>();
		adIds.add(257);
		adIds.add(396);
		adIds.add(282);
		adCostDao.findBidByAdIds(adIds);
	}
	
	@Test
	public void updateIsValid(){
		ArrayList<Integer> adIds = new ArrayList();
		adIds.add(0);
		adCostDao.updateIsValid(adIds);
	}
	
	@Test
	public void updateIsValidByCid(){
		ArrayList<Integer> adIds = new ArrayList();
		adIds.add(0);
		adCostDao.updateIsValidByCid(adIds);
	}
	
	@Test
	public void findExceedCostMoney(){
		HashMap map = new HashMap();
		map.put("adId", 225);
		map.put("costMoney",15);
		System.out.println(adCostDao.findExceedCostMoney(map));
	}
	
	@Test
	public void updateInValidButMoney(){
		adCostDao.updateInValidButMoney();
	}
	
	@Test
	public void updateValidButNoMoney(){
		adCostDao.updateValidButNoMoney();
	}
	
	@Test
	public void updateOutMoney(){
		adCostDao.updateOutMoney();
	}
}
