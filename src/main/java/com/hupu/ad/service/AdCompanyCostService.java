package com.hupu.ad.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hupu.ad.dao.AdCompanyCostDao;
import com.hupu.ad.dao.AdCostDao;
import com.hupu.ad.dao.AdPayLogDao;
import com.hupu.ad.domain.AdCost;
import com.hupu.ad.domain.AdLogCount;

@Component
public class AdCompanyCostService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Resource private AdCostDao adCostDao;
	@Resource private AdCompanyCostDao adCompanyCostDao;
	@Resource private AdPayLogDao adPayLogDao;
	@Resource private AdSysService adSysService;
	
	/**
	 * 公司扣费(ad_cost,ad_company_cost,ad_pay_log,ad_list,ad_list)
	 * @param adLogCountList
	 */
	public void pay(Collection<AdLogCount> adLogCounts){
		List<Map> paramList = this.composeAdMoeny(adLogCounts);
		if(CollectionUtils.isEmpty(paramList)){
			log.debug("没有公司支付信息");
			return;
		}
		//更新累加公司和广告花费
		adCostDao.batchUpdateCostMoney(paramList);
		adCompanyCostDao.batchUpdateCostMoney(paramList);
		//增加支付日志
		adPayLogDao.batchInsert(paramList);
		adCostDao.updateOutMoney();
		//暂停相关所有广告
		List<Integer> adIds = adCostDao.findOut();
		if(CollectionUtils.isEmpty(adIds)){
			log.debug("没有超出预算的广告adIds:");
			return;
		}
		adCostDao.updateIsValid(adIds);
		adSysService.stopAdList(StringUtils.join(adIds, ","));
	}
	
	/**
	 * 计算出相关参数为公司支付
	 * @param adLogCounts
	 */
	private List<Map> composeAdMoeny(Collection<AdLogCount> adLogCounts){
		StopWatch sw = new StopWatch();
		sw.start();
		log.debug("开始计算公司扣费参数");
		//一次性取出所有广告的adcost记录,
		ArrayList<Integer> adIds = new ArrayList();
		for (AdLogCount adLogCount : adLogCounts) {
			adIds.add(adLogCount.getAdId());
		}
		Collection<AdCost> adCostList = adCostDao.findByAdIds(adIds);
		if (CollectionUtils.isEmpty(adCostList)) {
			log.debug("没有算公司扣费参数记录!");
			return null;
		}
		Map<Integer,AdCost> adCostMap = new HashMap();
		for (AdCost adCost : adCostList) {
			adCostMap.put(adCost.getAdId(), adCost);
		}
		//计算广告费用
		List<Map> paramList = new ArrayList();
		for (AdLogCount adLogCount : adLogCounts) {
			AdCost adCost = adCostMap.get(adLogCount.getAdId());
			if(adCost == null){
				continue;
			}
			String chargeMode = adCost.getChargeMode();
			BigDecimal bigNum = new BigDecimal(1);
			if("CPM".equals(chargeMode)){
				bigNum = new BigDecimal(adLogCount.getShowCount()).divide(new BigDecimal(1000));
			}
			else if("CPC".equals(chargeMode)){
				bigNum = new BigDecimal(adLogCount.getClickCount());
			}
			else if("FANS".equals(chargeMode)){
				bigNum = new BigDecimal(adLogCount.getFansCount());
			}
			Map param = new HashMap();
			BigDecimal costMoney =  adCost.getBid().multiply(bigNum);
			if(costMoney.doubleValue() < 0.01){
				continue;
			}
			param.put("costMoney", costMoney);
			param.put("adId", adCost.getAdId());
			param.put("cid", adCost.getCid());
			param.put("chargeMode", chargeMode);
			log.debug("公司扣费参数:{}", param);
			paramList.add(param);
		}
		//广告单条预算超出预算,则扣费金额不能大于预算
		for (Map param : paramList) {
			Double costMoney = adCostDao.findExceedCostMoney(param);
			if(costMoney != null){
				param.put("costMoney", costMoney);
			}
		}
		log.debug("开始计算公司扣费参数完成 time:{}", sw.getTime());
		return paramList;
	}
}
