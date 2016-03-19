package com.hupu.ad.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.MultiKeyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hupu.ad.dao.AdConfigDao;
import com.hupu.ad.dao.AdIncomeExpensesSingleDao;
import com.hupu.ad.dao.AdIncomeTotalSingleDao;
import com.hupu.ad.dao.AdLogDao;
import com.hupu.ad.domain.AdIncomeExpensesSingle;
import com.hupu.ad.domain.AdIncomeTotalSingle;

/**
 * 广告后台系统管理
 * @author donghui
 */
@Component
public class AdIncomeService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Resource private AdLogDao adLogDao;
	@Resource private AdConfigDao adConfigDao;
	@Resource private AdIncomeExpensesSingleDao adIncomeExpensesSingleDao;
	@Resource private AdIncomeTotalSingleDao adIncomeTotalSingleDao;
	
	/**
	 * 对没成进行分成的adlog记录进行分成
	 */
	public void income(){
		Collection<Map> adLogs = adLogDao.findIncome();
		if (CollectionUtils.isEmpty(adLogs)) {
			log.debug("没有可分成的adlog记录.");
			return;
		}
		Collection<AdIncomeExpensesSingle> adIncomeExpensesSingles = this.composeIncome(adLogs);
		adIncomeExpensesSingleDao.batchUpdate(adIncomeExpensesSingles);
		Collection<AdIncomeTotalSingle> adIncomeTotalSingles = this.composeAdIncomeTotalSingles(adIncomeExpensesSingles);
		adIncomeTotalSingleDao.batchUpdate(adIncomeTotalSingles);
		adLogDao.updateIncomeInValid();
	}
	
	/**
	 * 整理ad_logs记录
	 * @param adLogs
	 */
	private Collection<AdIncomeExpensesSingle> composeIncome(Collection<Map> adLogs){
		Double assign = adConfigDao.findAssign();
		MultiKeyMap multiKeyMap = new MultiKeyMap();
		for (Map adLog : adLogs) {
			int adId = MapUtils.getIntValue(adLog, "adId");
			int dkcode = MapUtils.getIntValue(adLog, "dkcode");
			double bid = MapUtils.getDoubleValue(adLog, "bid");
			String eventType = MapUtils.getString(adLog, "eventType");
			int dateline = MapUtils.getIntValue(adLog, "dateline");
			boolean isShow = false;
			if("1".equals(eventType)){
				isShow = true;
			}
			AdIncomeExpensesSingle adIncome = (AdIncomeExpensesSingle)multiKeyMap.get(adId, dkcode, isShow, dateline);
			if(adIncome == null){
				adIncome = new AdIncomeExpensesSingle(adId, dkcode, dateline, bid, isShow, assign);
			}
			adIncome.addMoney();
			multiKeyMap.put(adId, dkcode, isShow, dateline, adIncome);
		}
		//删除费用为小于0.01的记录
		ArrayList result = new ArrayList();
		for (Object obj : multiKeyMap.values()) {
			if(((AdIncomeExpensesSingle)obj).getMoney() < 0.01){
				continue;
			}
			result.add(obj);
		}
		return result;
	}
	
	private Collection<AdIncomeTotalSingle> composeAdIncomeTotalSingles(Collection<AdIncomeExpensesSingle> adIncomeExpensesSingles){
		MultiKeyMap multiKeyMap = new MultiKeyMap();
		for (AdIncomeExpensesSingle adIncomeExpensesSingle : adIncomeExpensesSingles) {
			int adId = adIncomeExpensesSingle.getAdId();
			int dkcode = adIncomeExpensesSingle.getDkcode();
			AdIncomeTotalSingle adIncomeTotalSingle = (AdIncomeTotalSingle)multiKeyMap.get(adId, dkcode);
			if (adIncomeTotalSingle == null) {
				adIncomeTotalSingle = new AdIncomeTotalSingle(adIncomeExpensesSingle);
			}
			adIncomeTotalSingle.addMoney(adIncomeExpensesSingle);
			multiKeyMap.put(adId, dkcode, adIncomeTotalSingle);
		}
		return multiKeyMap.values();
	}
	
}