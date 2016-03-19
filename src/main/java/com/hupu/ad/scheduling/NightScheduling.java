package com.hupu.ad.scheduling;

import java.util.Collection;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hupu.ad.cache.AdCrowdCache;
import com.hupu.ad.dao.AdCostDao;
import com.hupu.ad.dao.AdListDao;
/**
 * 每天晚上定时运行机的任务
 * @author donghui
 */
@Component
public class NightScheduling {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	@Resource private AdCostDao adCostDao;
	@Resource private AdListDao adListDao;
	@Resource private AdCrowdCache adCrowdCache;
	
	/**
	 * 修正is_valid和费用的关系
	 * 上每日预算的记录设置金额为0,重新计算
	 */
	public void updateDayBudgetCostMoneySetZero(){
		log.debug("开始每日预算清零!!");
		adCostDao.updateDayBudgetCostMoneySetZero();
		adCostDao.updateInValidButMoney();
		adCostDao.updateValidButNoMoney();
	}
	
	/**
	 * 投放时间已到的广告,加入投放条件缓存中
	 */
	public void addCrowd(){
		StopWatch sw = new StopWatch();
		sw.start();
		log.debug("开始加入推迟投放的广告");
		Collection<Integer> adIds = adListDao.findByCurdate();
		adCrowdCache.saveCrowdByadId(adIds);
		log.debug("成功加入推迟投放的广告adIds:{} time:{}", adIds, sw.getTime());
	}
	
	
}