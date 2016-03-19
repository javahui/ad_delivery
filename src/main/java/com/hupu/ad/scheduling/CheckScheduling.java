package com.hupu.ad.scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hupu.ad.cache.AdAttentionCache;
import com.hupu.ad.cache.AdCrowdCache;
import com.hupu.ad.dao.AdAttentionDao;
import com.hupu.ad.dao.AdCostDao;
import com.hupu.ad.dao.AdCrowdDao;
import com.hupu.ad.dao.AdIncomeExpensesSingleDao;
import com.hupu.ad.dao.AdIncomeTotalSingleDao;
import com.hupu.ad.dao.AdListDao;
import com.hupu.ad.dao.AdLogCountDao;
import com.hupu.ad.dao.AdLogDao;
import com.hupu.ad.dao.AdLogPersonalCountDao;
import com.hupu.ad.domain.AdLogCount;
import com.hupu.ad.domain.AdLogPersonalCount;
import com.hupu.ad.service.AdIncomeService;
import com.hupu.ad.service.AdSysService;
/**
 * 定时运行数据校验任务
 * @author donghui
 */
@Component
public class CheckScheduling {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	@Resource private AdCostDao adCostDao;
	@Resource private AdLogDao adLogDao;
	@Resource private AdLogCountDao adLogCountDao;
	@Resource private AdListDao adListDao;
	@Resource private AdIncomeTotalSingleDao adIncomeTotalSingleDao;
	@Resource private AdIncomeExpensesSingleDao adIncomeExpensesSingleDao;
	@Resource private AdCrowdCache adCrowdCache;
	@Resource private AdCrowdDao adCrowdDao;
	@Resource private AdSysService adSysService;
	@Resource private AdLogPersonalCountDao adLogPersonalCountDao;
	@Resource private AdIncomeService adIncomeService;
	@Resource private AdAttentionDao adAttentionDao;
	@Resource private AdAttentionCache adAttentionCache;
	
	/**
	 * 校验广告是否投放,开启应该有投放条件的广告
	 */
	public void checkCrowd(){
		StopWatch sw = new StopWatch();
		sw.start();
		log.debug("开始校验广告是否投放信息");
		adCostDao.updateInValidButMoney();
		adCostDao.updateValidButNoMoney();
		ArrayList<Integer> adIds = new ArrayList<Integer>();
		List<Integer> adIdList = adListDao.findNormal();
		for (Integer adId : adIdList) {
			if(!adCrowdCache.isCrowd(adId)){
				log.error("广告adId:{}没有投放信息", adId);
				adIds.add(adId);
			}
		}
		adSysService.startAdList(StringUtils.join(adIds.toArray(), ","));
		log.debug("校验广告是否投放信息结束 time:{}", sw.getTime());
	}
	
	/**
	 * 校验ad_log表记录与ad_log_count表的数据一致性
	 */
	public void checkAdLogCount(){
		StopWatch sw = new StopWatch();
		sw.start();
		log.debug("开始校验ad_log表记录与ad_log_count表的数据一致性完成");
		MultiKeyMap mkm = new MultiKeyMap();
		for (AdLogCount adLogCount : adLogCountDao.findAll()) {
			mkm.put(adLogCount.getAdId(), adLogCount.getDateline(), adLogCount);
		}
		
		for (AdLogCount adLogCount : adLogDao.findLogCount()) {
			int adId = adLogCount.getAdId();
			int dateline = adLogCount.getDateline();
			AdLogCount otherAdLogCount = (AdLogCount)mkm.get(adId, dateline);
			if(ObjectUtils.notEqual(adLogCount, otherAdLogCount)){
				log.debug("根据ad_log计算的{} 和 ad_log_count表:{}的记录不一致", adLogCount, otherAdLogCount );
				if(otherAdLogCount == null){
					adLogCountDao.insert(adLogCount);
					log.debug("新增:{}", adLogCount);
				}
				else{
					adLogCountDao.updateCountByAdIdAndDateline(adLogCount);
					log.debug("修改:{}",adLogCount);
				}
			}
		}
		log.debug("校验ad_log表记录与ad_log_count表的数据一致性完成 Time:{}", sw.getTime());
	}
	
	/**
	 * 校验ad_log表记录与ad_log_presonal_count表的数据一致性
	 */
	public void checkAdLogPersonalCount(){
		StopWatch sw = new StopWatch();
		sw.start();
		log.debug("开始校验ad_log表记录与ad_log_presonal_count表的数据一致性完成");
		MultiKeyMap mkm = new MultiKeyMap();
		for (AdLogPersonalCount adLogPersonalCount : adLogPersonalCountDao.findAll()) {
			mkm.put(adLogPersonalCount.getAdId(), adLogPersonalCount.getDkcode(), adLogPersonalCount);
		}
		
		for (AdLogPersonalCount adLogPersonalCount : adLogDao.findLogPersonalCount()) {
			int adId = adLogPersonalCount.getAdId();
			int dkcode = adLogPersonalCount.getDkcode();
			AdLogPersonalCount otherAdLogPersonalCount = (AdLogPersonalCount)mkm.get(adId, dkcode);
			if(ObjectUtils.notEqual(adLogPersonalCount, otherAdLogPersonalCount)){
				log.debug("根据ad_log计算的{} 和 ad_log_personal_count表:{}的记录不一致", adLogPersonalCount, otherAdLogPersonalCount );
				if(otherAdLogPersonalCount == null){
					adLogPersonalCountDao.insert(adLogPersonalCount);
					log.debug("新增:{}", adLogPersonalCount);
				}
				else{
					adLogPersonalCountDao.updateCountByAdIdAndDkcode(adLogPersonalCount);
					log.debug("修改:{}",adLogPersonalCount);
				}
			}
		}
		log.debug("校验ad_log表记录与ad_log_presonal_count表的数据一致性完成 Time:{}", sw.getTime());
	}
	
	/**
	 * 全部重新分成
	 */
	public void checkIncome(){
		adIncomeTotalSingleDao.deleteAll();
		adIncomeExpensesSingleDao.deleteAll();
		adLogDao.updateIncomeValid();
		adIncomeService.income();
	}
	
	/**
	 *  全部生成用户关注缓存
	 */
	public void checkCacheAttention(){
		List<Map> attentionList = adAttentionDao.findValid();
		adAttentionCache.addAttention(attentionList);
	}
	
	/**
	 *  全部重新生成年龄范围缓存
	 */
	public void checkAllAgeRangeCache(){
		adCrowdCache.delAllAgeRangeCache();
		List<String> ageRangeList = adCrowdDao.findAllAgeRange();
		adCrowdCache.saveAgeRangeCache(ageRangeList);
	}
	
}