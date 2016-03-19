package com.hupu.ad.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 个人页面广告缓存操作类
 * @author donghui
 */
@Component
public class AdCustomCache extends AbstractCache{
	
	/**
	 * 个人用户页面以uid保存ad的集合KEY的前缀("uid_adIds:")
	 */
	private final String UID_ADIDS_KEY_PREFIX= "uid_adIds:";
	
	/**
	 * 个人用户页面以adId保存uid的集合KEY的前缀("adId_uids:")
	 */
	private final String ADID_UIDS_KEY_PREFIX= "adId_uids:";
	
	/**
	 * 清空adids重新保存adIds按uid的集合
	 * @param uid 用户ID
	 * @param adIds 广告id数组
	 */
	public void setAdIdsByUid(int uid,Collection adIds){
		String uidKey = UID_ADIDS_KEY_PREFIX + uid;
		adCache.delete(uidKey);
		for (Object adId : adIds) {
			adCache.opsForSet().add(uidKey, Objects.toString(adId));
		}
		log.debug("保存用户页面的广告uid:[{}] adIds values:{}", uidKey, ArrayUtils.toString(adIds));
	}
	
	/**
	 * 保存adId按uidSet的集合
	 * @param uid 用户ID
	 * @param adId 广告id
	 */
	public void setAdIdByUids(Set<String> uidSet,int adId){
		for (String uid : uidSet) {
			adCache.opsForSet().add(UID_ADIDS_KEY_PREFIX + uid, adId + "");
		}
		log.debug("保存用户页面的广告uidSet key:{}", UID_ADIDS_KEY_PREFIX + uidSet);
		log.debug("保存用户页面的广告adId values:{}", adId);
	}
	
	/**
	 * 保存adIds为多个key,uid为value的多个set集合
	 * @param adIds 广告id数组
	 * @param uid 用户ID
	 */
	public void setUidsByAdId(Collection adIds, int uid){
		for (Object adId : adIds) {
			adCache.opsForSet().add(ADID_UIDS_KEY_PREFIX + adId, uid + "");
		}
		log.debug("保存用户页面的广告adids key:{}", ArrayUtils.toString(adIds));
		log.debug("保存用户页面的广告uid values:{}", uid);
	}
	
	/**
	 * 得到广告按uid
	 * @param uid 用户ID
	 * @return 个人页面的广告集
	 */
	public Set<String> getAdIdsByUid(int uid){
		Set<String> adIds = adCache.opsForSet().members(UID_ADIDS_KEY_PREFIX + uid);
		log.debug("得到广告按uid key:{} value:{}", uid, adIds);
		return adIds;
	}
	
	
	/**
	 * 得到uid按adId
	 * @param uid 用户ID
	 * @return 个人页面的广告集
	 */
	public Set<String> getUidsByAdId(int adId){
		Set<String> uids = adCache.opsForSet().members(ADID_UIDS_KEY_PREFIX + adId);
		return uids;
	}
	
	/**
	 * 删除uidSet的广告
	 * @param uidSet 用户ID集合
	 * @param adId 广告id
	 */
	public void delUidByAdId(int uid,String[] adIds){
		for (String adId : adIds) {
			adCache.opsForSet().remove(ADID_UIDS_KEY_PREFIX + adId, uid + "");
		}
		log.debug("删除用户广告adIds:{}的uid:{}", adIds, uid);
	}
	
	/**
	 * 开启用户广告的所有个人用户
	 */
	public void startUserAdList(int adId){
		Set<String> uids = adCache.opsForSet().members(ADID_UIDS_KEY_PREFIX + adId);
		for (String uid : uids) {
			adCache.opsForSet().add(UID_ADIDS_KEY_PREFIX + uid, adId + "");
		}
		log.debug("开启用户广告adId:{}的所有个人用户uids:{}", adId, uids);
		
	}
	
	/**
	 * 关闭用户广告的所有个人用户
	 */
	public void stopUserAdList(int adId){
		Set<String> uids = adCache.opsForSet().members(ADID_UIDS_KEY_PREFIX + adId);
		for (String uid : uids) {
			adCache.opsForSet().remove(UID_ADIDS_KEY_PREFIX + uid, adId + "");
		}
		log.debug("关闭用户广告adId:{}的所有个人用户uids:{}", adId, uids);
	}
	
	
	/**
	 * 获取缓存中的所有用户广告
	 */
	public Map<String,String> getAdIdsAll(){
		Map<String,String> map = new HashMap<String,String>();
		Set<String> keys = adCache.keys(UID_ADIDS_KEY_PREFIX+"*");
		for (String key : keys) {
			Set<String> adIds = adCache.opsForSet().members(key);
			String adIdsStr =  StringUtils.join(adIds, ",");
			String mKey = key.split(":")[1];
			map.put(mKey, adIdsStr);
			log.debug("得到个人用户uid:{}的广告adIds:{}", mKey, adIdsStr);
		}
		return map;
	}
	
	/**
	 * 获取缓存中的广告对应的用户
	 */
	public Map<String,String> getUidsAll(){
		Map<String,String> map = new HashMap<String,String>();
		Set<String> keys = adCache.keys(ADID_UIDS_KEY_PREFIX+"*");
		for (String key : keys) {
			Set<String> uIds = adCache.opsForSet().members(key);
			String uIdsStr =  StringUtils.join(uIds, ",");
			String mKey = key.split(":")[1];
			map.put(mKey, uIdsStr);
			log.debug("得到个人用户adId:{}的广告uIds:{}", mKey, uIdsStr);
		}
		return map;
	}
}