package com.hupu.ad.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.hupu.ad.domain.AdCost;

/**
 * 广告花费表
 * @author donghui
 */
@Component
public class AdCostDao extends AbstractDAO{

	@Resource private AdConfigDao adConfigDao;
	
	/**
	 * 得到有预算非免费的广告花费记录
	 * @param adIds
	 * @return
	 */
	public Collection<AdCost> findByAdIds(List<Integer> adIds){
		if(CollectionUtils.isEmpty(adIds)){
			return CollectionUtils.EMPTY_COLLECTION;
		}
		List<AdCost> result = adSqlSession.selectList("adCost.findByAdIds", adIds);
		log.debug("有预算非免费的广告花费记录adIds:{}", adIds);
		return result;
	}
	
	/**
	 * 通过adIds得到广告竞价信息
	 * @param adIds
	 * @return
	 */
	public List<AdCost> findBidByAdIds(List<Integer> adIds){
		if(CollectionUtils.isEmpty(adIds)){
			return null;
		}
		List<AdCost> result = adSqlSession.selectList("adCost.findBidByAdIds", adIds);
		log.debug("得到广告竞价信息记录adIds:{}", adIds);
		return result;
	}
	
	
	/**
	 * 得到所有超出预算的广告adIds
	 */
	public List<Integer> findOut(){
		List<Integer> adCostOutrAdId = adSqlSession.selectList("adCost.findAdCostOut");
		List<Integer> companyCostOutAdId = new ArrayList();
		if(!adConfigDao.isCurrentTimeFree()){
			companyCostOutAdId = adSqlSession.selectList("adCost.findCompanyCostOut");
		}
		List<Integer> result = ListUtils.union(adCostOutrAdId, companyCostOutAdId);
		log.debug("超出预算的广告adIds:{}", result);
		return result;
	}
	
	public Double findExceedCostMoney(Map param){
		return adSqlSession.selectOne("adCost.findExceedCostMoney", param);
	}
	
	/**
	 * 批处理更新cost_money的钱数
	 */
	public void batchUpdateCostMoney(List<Map> paramList){
		StopWatch sw = new StopWatch();
		sw.start();
		log.debug("开始更新广告花费表ad_cost");
		SqlSession sqlSession = adSqlSession.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		for (Map param : paramList) {
			sqlSession.update("adCost.updateCostMoneyById", param);
			log.debug("广告adId:{} 扣费:{}", param.get("adId"), param.get("costMoney") );
		}
		sqlSession.commit();
		sqlSession.close();
		log.debug("更新广告花费表ad_cost完成 time:{}", sw.getTime());
	}
	
	public Integer updateDayBudgetCostMoneySetZero(){
		Integer numUpdate = adSqlSession.update("adCost.updateDayBudgetCostMoneySetZero");
		log.debug("修改了{}条记录", numUpdate);
		return numUpdate;
	}
	
	/**
	 * 查找有郊的广告
	 */
	public Collection<Integer> findValid(String[] adIds){
		if(ArrayUtils.isEmpty(adIds)){
			return Collections.EMPTY_LIST;
		}
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (String adId : adIds) {
			list.add(Integer.valueOf(adId));
		}
		return adSqlSession.selectList("adCost.findValid", list);
	}
	
	/**
	 * 查找无郊(-1 已删除 1暂停 ,-1预算己达到)的广告
	 */
	public Collection<Integer> findInvalid(List<Integer> adIds){
		return adSqlSession.selectList("adCost.findInvalid", adIds);
	}
	
	
	/**
	 * 更新所有超出预算的记录isValidate字段
	 */
	public void updateIsValid(List<Integer> adIds){
		if(CollectionUtils.isEmpty(adIds)){
			log.debug("没有超出预算的记录");
			return;
		}
		adSqlSession.update("adCost.updateIsValid", adIds);
		log.debug("update ad_cost isValidate字段为达到预算 adIds:{}", adIds);
	}
	
	/**
	 * 更新公司下所有广告为1(预算未达到)
	 */
	public void updateIsValidByCid(List<Integer> adIds){
		if(CollectionUtils.isEmpty(adIds)){
			return;
		}
		adSqlSession.update("adCost.updateIsValidByCid", adIds);
	}
	
	/**
	 * 查出所有公司费用没花完,广告预算没达到,is_valid为(已达到:-1)的不正常记录 ,并更新为is_valid(1)
	 */
	public int updateInValidButMoney(){
		if(adConfigDao.isCurrentTimeFree()){
			return 0;
		}
		List<Integer> adIds = adSqlSession.selectList("adCost.findMoney");
		if(CollectionUtils.isEmpty(adIds)){
			return 0;
		}
		int updateNum = adSqlSession.update("updateIsValid(1)ByAdIds", adIds);
		log.debug("更新有费用,is_valid为(已达到:-1)的记录广告adIds:{} 共 {} 条", adIds, updateNum); 
		return updateNum;
	}
	
	/**
	 * 查出所有公司费用已花完或者广告预算已达到,is_valid为(未达到:1)到的不正常记录,并更新为is_valid(-1)
	 */
	public int updateValidButNoMoney(){
		if(adConfigDao.isCurrentTimeFree()){
			return 0;
		}
		List<Integer> adIds = adSqlSession.selectList("adCost.findNoMoney");
		if(CollectionUtils.isEmpty(adIds)){
			return 0;
		}
		int updateNum = adSqlSession.update("updateIsValid(-1)ByAdIds", adIds);
		log.debug("更新没费用,is_valid为(未达到:1)的记录广告adIds:{} 共 {} 条", adIds, updateNum);
		return updateNum;
	}
	
	/**
	 * 更新超出总费的花费金额
	 */
	public void updateOutMoney() {
		if(adConfigDao.isCurrentTimeFree()){
			return;
		}
		int updateNum = adSqlSession.update("updateOutMoney");
		log.debug("更新{}条公司费用超出的记录!",updateNum );
	}
	
}
