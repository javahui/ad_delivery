package com.hupu.ad.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.MultiKeyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hupu.ad.cache.AdListCache;
import com.hupu.ad.cache.AdLogQueueCache;
import com.hupu.ad.dao.AdLogCountDao;
import com.hupu.ad.dao.AdLogDao;
import com.hupu.ad.dao.AdLogPersonalCountDao;
import com.hupu.ad.domain.AdList;
import com.hupu.ad.domain.AdLog;
import com.hupu.ad.domain.AdLogCount;
import com.hupu.ad.domain.AdLogPersonalCount;
import com.hupu.ad.util.UnixTimeUtils;

@Component
public class AdLogService {
	public final Logger log = LoggerFactory.getLogger(getClass());
	@Resource private AdListCache adListCache;
	@Resource private AdLogQueueCache adLogQueueCache;
	@Resource private AdLogDao adLogDao;
	@Resource private AdLogCountDao adLogCountDao;
	@Resource private AdLogPersonalCountDao adLogPersonalCountDao;
	
	/**
	 *  写ad_log日志到队列中
	 * @param eventType 事件类型(1 展示 2点击 3关注)
	 * @param url 浏览者url
	 * @param ip
	 * @param dkcode 
	 * @param actionDkcode
	 * @param adId
	 */
	public void writeAdLog(int eventType, String url, String ip, int dkcode, int actionDkcode, int adId){
		AdList adList = adListCache.getAndsaveAdList(adId);
		if(adList == null){
			return;
		}
		Set adListSet = new HashSet();
		adListSet.add(adList);
		this.writeAdLog(eventType, url, ip, dkcode, actionDkcode, adListSet);
	}
	
	/**
	 * 写ad_log日志到队列中
	 * @param eventType 事件类型(1 展示 2点击 3关注)
	 * @param url 浏览者url
	 * @param ip 浏览者ip
	 * @param dkcode 
	 * @param actionDkcode 
	 * @param adLists 广告
	 */
	public void writeAdLog(int eventType, String url, String ip, int dkcode, int actionDkcode, Collection<AdList> adLists){
		if(CollectionUtils.isEmpty(adLists)){
			return;
		}
		int currentTime = UnixTimeUtils.getCurrentUnixTimeSS();
		Collection<AdLog> adLogs = new HashSet();
		for (AdList adList : adLists) {
			if(adList.getIsFree() == 1){
				log.debug("广告adId:{}为免费广告不写日志" , adList.getAdId());
				continue;
			}
			AdLog adLog = new AdLog();
			adLog.setAdId(adList.getAdId());
			adLog.setCid(adList.getCid());
			adLog.setEventType(eventType);
			adLog.setIp(ip);
			adLog.setUrl(url);
			adLog.setDateline(currentTime);
			if(adList.getIsPersonal() == 1){
				adLog.setIsUser(1);
			}
			else{
				adLog.setIsUser(0);
			}
			adLog.setDkcode(dkcode);
			adLog.setActionDkcode(actionDkcode);
			adLogs.add(adLog);
		}
		adLogQueueCache.setAdLogs(adLogs);
	}
	
	/**
	 * 保存ad_log,并更新ad_log_count,ad_log_personal_count
	 */
	public Collection<AdLogCount> saveLogAndLogCount(Collection<AdLog> adLogs) {
		this.removeTodayClick(adLogs);
		adLogDao.batchInsert(adLogs);
		Collection<AdLogCount> adLogCounts = this.composeAdLogCount(adLogs);
		adLogCountDao.batchIncrementCount(adLogCounts);
		adLogPersonalCountDao.batchIncrementCount(this.composeAdLogPersonalCount(adLogs));
		return adLogCounts;
	}
	
	/**
	 * 通过adId得到广告日志信息
	 * @param adId
	 * @return
	 */
	public AdLog getAdLogById(int adId){
		return adLogDao.findByAdId(adId);
	}
	
	/**
	 * ad_log日志合成ad_log_count记录
	 * @param adLogs ad_log日志集合
	 * @return ad_log_count记录
	 */
	private Collection<AdLogCount> composeAdLogCount(Collection<AdLog> adLogs){
		Map<Integer, AdLogCount> map = new HashMap<Integer, AdLogCount>();
		int currentTime = UnixTimeUtils.getCurrentUnixTimeDD();
		for (AdLog adLog : adLogs) {
			int adId = adLog.getAdId();
			AdLogCount adLogCount = map.get(adId);
			if(adLogCount == null){
				adLogCount = new AdLogCount(); 
				adLogCount.setAdId(adId);
				adLogCount.setDateline(currentTime);
			}
			int eventType = adLog.getEventType();
			adLogCount.incrementCount(eventType);
			map.put(adId, adLogCount);
		}
		return map.values();
	}
	
	/**
	 * ad_log合成ad_log_personal_count记录
	 * @param adLogs ad_log日志集合
	 * @return ad_log_personal_count记录
	 */
	private Collection<AdLogPersonalCount> composeAdLogPersonalCount(Collection<AdLog> adLogs){
		MultiKeyMap multiKeyMap = new MultiKeyMap();
		for (AdLog adLog : adLogs) {
			if(adLog.getActionDkcode() == 0){
				continue;
			}
			int adId = adLog.getAdId();
			int actionDkcode = adLog.getActionDkcode();
			AdLogPersonalCount adLogPersonalCount = (AdLogPersonalCount)multiKeyMap.get(adId, actionDkcode);
			if(adLogPersonalCount == null){
				adLogPersonalCount = new AdLogPersonalCount(); 
				adLogPersonalCount.setAdId(adId);
				adLogPersonalCount.setDkcode(actionDkcode);
			}
			int eventType = adLog.getEventType();
			adLogPersonalCount.incrementCount(eventType);
			multiKeyMap.put(adId, actionDkcode, adLogPersonalCount);
		}
		return multiKeyMap.values();
	}
	
	/**
	 * 删除已存在的当日的点击事件记录
	 */
	private void removeTodayClick(Collection<AdLog> adLogs){
		for (AdLog adLog : adLogs) {
			if(adLog.getEventType() == 2 && adLog.getActionDkcode() != 0){
				if(adLogDao.isFindTodayClick(adLog)){
					adLogs.remove(adLog);
				}
			}
		}
	}
	
}
