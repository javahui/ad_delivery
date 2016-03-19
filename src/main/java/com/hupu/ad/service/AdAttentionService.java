package com.hupu.ad.service;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hupu.ad.cache.AdAttentionCache;
import com.hupu.ad.cache.UserInfoCache;
import com.hupu.ad.dao.AdAttentionDao;
import com.hupu.ad.service.interfaces.IAdAttentionService;

/**
 * 广告后台系统管理
 * @author donghui
 */
@Component
public class AdAttentionService implements IAdAttentionService	 {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Resource private AdAttentionDao adAttentionDao;
	@Resource private AdAttentionCache adAttentionCache;
	@Resource private AdLogService adLogService;
	@Resource private UserInfoCache userInfoCache;

	private static final int EVENT_TYPE_FANS = 3;
	
	/**
	 * 增加关注
	 */
	public int addAttention(String json){
		try{
			log.debug("增加关注Json:{}", json);
			JSONObject jsonObj = JSON.parseObject(json);
			int uidOrWebId = jsonObj.getIntValue("uid") + jsonObj.getIntValue("webId");
			int adId = jsonObj.getIntValue("adId");
			int actionDkcode = jsonObj.getIntValue("actionDkcode");
			if(uidOrWebId == 0 || adId == 0){
				log.error("uidOrWebId:{} adId:{}数据异常", uidOrWebId, adId);
				return 0;
			}
			String url = "";
			String ip = "";
			//如果是第一次关注adId广告,则产生扣费
			if(adAttentionDao.addAttention(uidOrWebId, adId)){
				int dkcode = MapUtils.getIntValue(userInfoCache.getUser(jsonObj.getIntValue("uid")), "dkcode");
				adLogService.writeAdLog(EVENT_TYPE_FANS, url, ip,dkcode, actionDkcode, adId);
			}
			adAttentionCache.addAttention(uidOrWebId, adId);
			return 1;
		}
		catch (Exception e) {
			log.error("AdAttentionService addAttention error",e);
			return 0;
		}
	}
	
	/**
	 * 取消关注
	 */
	public int delAttention(String json){
		try{
			log.debug("取消关注Json:{}", json);
			JSONObject jsonObj = JSON.parseObject(json);
			int uidOrWebId = jsonObj.getIntValue("uid") + jsonObj.getIntValue("webId");
			if(uidOrWebId == 0){
				log.error("uid和webId都为0");
				return 0;
			}
			int adId = jsonObj.getInteger("adId");
			adAttentionDao.delAttention(uidOrWebId, adId);
			adAttentionCache.delAttention(uidOrWebId, adId);
			return 1;
		}
		catch (Exception e) {
			log.error("AdAttentionService addAttention error",e);
			return 0;
		}
	}
	
}
