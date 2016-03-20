package com.hupu.ad.scheduling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hupu.ad.cache.AdLogQueueCache;
import com.hupu.ad.dao.AdCostDao;
import com.hupu.ad.domain.AdLog;
import com.hupu.ad.domain.AdLogCount;
import com.hupu.ad.service.AdCompanyCostService;
import com.hupu.ad.service.AdLogService;
import com.hupu.ad.service.AdSysService;

@Component
public class AdLogQueueScheduling {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Resource private AdLogQueueCache adLogQueueCache;
	@Resource private AdLogService adLogService;
	@Resource private AdSysService adSysService;
	@Resource private AdCompanyCostService adCompanyCostService;
	@Resource private AdCostDao adCostDao;
	/**
	 * 每几分钟,把队列数据保存到MYSQL中
	 */
	public void clearQueueAndWriteDB(){
		Collection<AdLog> adLogs = adLogQueueCache.getAdLogs(true);
		if(CollectionUtils.isEmpty(adLogs)){
			return;
		}
		log.debug("开始清队列-------------");
		adCostDao.updateInValidButMoney();
		adCostDao.updateValidButNoMoney();
		this.delStopLog(adLogs);
		Collection<AdLogCount> adLogCounts = adLogService.saveLogAndLogCount(adLogs);
		adCompanyCostService.pay(adLogCounts);
	}

	/**
	 * 删除队列中删除.暂停.达到预算的广告
	 */
	private void delStopLog(Collection<AdLog> adLogs){
		ArrayList<Integer> adIdList = new ArrayList();
		HashMap<Integer, AdLog> adLogMap = new HashMap();
		for (AdLog adLog : adLogs) {
			int adId = adLog.getAdId();
			adIdList.add(adId);
			adLogMap.put(adId, adLog); 
		}
		Collection<Integer> ids = adCostDao.findInvalid(adIdList);
		log.debug("删除队列中删除.暂停.达到预算的广告 adIds:{}",ids);
		
		
		for (Integer adId : ids) {
			adLogMap.remove(adId);
			adSysService.stopAdList(adId + "");
		}
		adLogs = adLogMap.values();
	}
}