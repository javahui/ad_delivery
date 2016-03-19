package com.hupu.ad.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hupu.ad.cache.AdCrowdCache;
import com.hupu.ad.cache.AdCustomCache;
import com.hupu.ad.cache.AdListCache;
import com.hupu.ad.dao.AdCostDao;
import com.hupu.ad.dao.AdCustomDao;
import com.hupu.ad.dao.AdListDao;
import com.hupu.ad.domain.AdList;
import com.hupu.ad.util.UnixTimeUtils;

/**
 * 广告后台系统管理
 * @author donghui
 */
@Component
public class AdSysService{
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Resource private AdCostDao adCostDao;
	@Resource private AdListDao adListDao;
	@Resource private AdCustomDao adCustomDao;
	@Resource private AdCrowdCache adCrowdCache;
	@Resource private AdListCache adListCache;
	@Resource private AdCustomCache adCustomCache;
	
	public int audit(int adId){
		log.debug("审核广告adId:{}", adId);
		Map adListMap = adListDao.find(adId);
		if(adListMap == null){
			log.error("审核广告为空 adId:{}", adId);
			return 1;
		}
		//如果创建时间等于开始时间,则为立即投放广告
		int createTime = MapUtils.getIntValue(adListMap, "createTime");
		int startTime = MapUtils.getIntValue(adListMap, "startTime");
		int classify = MapUtils.getIntValue(adListMap, "classify");
		if(createTime == startTime && classify != 3) {
			adCrowdCache.saveCrowdByadId(adId);
		}
		adListCache.saveAdList(adId);
		return 1;
	}
	
	/**
	 * 保存选定的个人广告
	 * @param uid 用户ID
	 */
	public int saveUserAdList(String json){
		log.debug("个人用记选择广告记录 json:{}", json);
		JSONObject jsonObj = JSON.parseObject(json);
		int uid = jsonObj.getIntValue("uid");
		String adIds = jsonObj.getString("adIds");
		if(StringUtils.isBlank(adIds) || StringUtils.equals(adIds, "false")){
			adIds = "0";
		}
		String[] newAdIds = StringUtils.split(adIds, ",");
		//Collection<Integer> newAdIdCollection = adCostDao.findValid(newAdIds);
		Collection newAdIdCollection = Arrays.asList(newAdIds);
		String oldAdId = adCustomDao.getAdIdsByUid(uid);
		if(StringUtils.isNotBlank(oldAdId)){
			String[] oldAdIdArray = StringUtils.split(oldAdId, ",");
			adCustomCache.delUidByAdId(uid, oldAdIdArray);
		}
		adCustomCache.setAdIdsByUid(uid, newAdIdCollection);
		adCustomCache.setUidsByAdId(newAdIdCollection, uid);
		return 1;
	}
	
	/**
	 * 开启广告
	 */
	public int startAdList(String adIds) {
		log.debug("开启广告adIds:{}", adIds);
		String[] adIdArray = StringUtils.split(adIds, ",");
		for (String adIdString : adIdArray) {
			if(!NumberUtils.isDigits(adIdString)){
				log.error("开启广告adId:{}不是int类的字符串",adIdString);
				continue;
			}
			int adId = NumberUtils.toInt(adIdString);
			//开启广告时,推迟时间投放的广告不开启
			Map map = adListDao.find(adId);
			if(UnixTimeUtils.getCurrentUnixTimeSS() < MapUtils.getIntValue(map, "startTime")) {
				log.debug("不能开启推迟投放广告adId:{} ", adId);
				continue;
			}
			if(MapUtils.getIntValue(map, "isChecked") != 3) {
				log.debug("不能开启审核没通过的广告adId:{} ", adId);
				continue;
			}
			if(MapUtils.getIntValue(map, "isValid") == -1) {
				log.debug("不能开启己达到预算的广告adId:{} ", adId);
				continue;
			}
			AdList adlist = adListCache.getAndsaveAdList(adId);
			if(adlist == null){
				continue;
			}
			int isPersonal = adlist.getIsPersonal();
			adCrowdCache.saveCrowdByadId(adId);
			if(isPersonal == 1){
				adCustomCache.startUserAdList(adId);
			}
		}
		return 1;
	}
	
	/**
	 * 关闭广告
	 * @param adId 广告ID
	 */
	public int stopAdList(String adIds) {
			log.debug("暂停广告adIds:{}", adIds);
			String[] adIdArray = StringUtils.split(adIds, ",");
			this.stop(adIdArray);
			return 1;
	}
	
	/**
	 * 删除广告
	 * @param adId 广告ID
	 */
	public int delAdList(String adIds) {
		log.debug("删除广告adIds:{}", adIds);
		String[] adIdArray = StringUtils.split(adIds, ",");
		this.stop(adIdArray);
		for (String adIdString : adIdArray) {
			adListCache.delAdList(Integer.valueOf(adIdString));
		}
		return 1;
	}
	
	/**
	 * 开启公司下所有广告
	 * @param cid
	 */
	public int startCompAdList(int cid){
		log.debug("开启公司所有广告cid:{}", cid);
		List<Integer> adIds = adListDao.findByCid(cid);
		adCostDao.updateIsValidByCid(adIds);
		this.startAdList(StringUtils.join(adIds, ","));
		return 1;
	}

	/**
	 * 根据广告类型(classify)来删除缓存中的数据达到不显示的目的
	 * @param adId
	 */
	private void stop(String[] adIdArray){
		for (String adIdString : adIdArray) {
			if(!NumberUtils.isDigits(adIdString)){
				log.error("{} 不是int类的字符串",adIdString);
				continue;
			}
			int adId = NumberUtils.toInt(adIdString);
			AdList adList = adListCache.getAndsaveAdList(adId);
			if(adList == null){
				continue;
			}		 
			int isPersonal = adList.getIsPersonal();
			adCrowdCache.delCrowdByadId(adId);
			if(isPersonal == 1){
				adCustomCache.stopUserAdList(adId);
			}
		}
	}
	
}
