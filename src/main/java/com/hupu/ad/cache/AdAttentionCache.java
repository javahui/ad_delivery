package com.hupu.ad.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import com.hupu.ad.domain.AdAttention;

/**
 * 广告投放信息缓存操作类
 * @author donghui
 */
@Component
public class AdAttentionCache extends AbstractCache{
	
	/**
	 * 用记关注广告key前缀
	 */
	private final String UID_ADIDS_ATTENTION_KEY_PREFIX =  "uid_adId_attention:";
	
	/**
	 * 获取用户关注的所有广告ID
	 */
	public Set<String> getAdIdsByUid(int uid){
		String uidKey = UID_ADIDS_ATTENTION_KEY_PREFIX + uid;
		Set<String> adIds = adCache.opsForSet().members(uidKey);
		log.debug("获取关注缓存key:[{}] value:{}", uidKey, adIds);
		return adIds;
	}
	
	/**
	 * 添加用户关注缓存
	 * @param uid
	 * @param adid
	 */
	public void addAttention(int uid, int adId) {
		String uidKey = UID_ADIDS_ATTENTION_KEY_PREFIX + uid;
		adCache.opsForSet().add(uidKey, adId + "");
		log.debug("添加关注缓存key:[{}] value:{}", uidKey, adId);
	}
	
	/**
	 * 添加用户关注缓存
	 */
	public void addAttention(List<Map> attentionList) {
		SetOperations<String, String> ops = adCache.opsForSet();
		for (Map map : attentionList) {
			String uidKey = UID_ADIDS_ATTENTION_KEY_PREFIX + MapUtils.getString(map, "uid");
			String adId = MapUtils.getString(map, "adId");
			ops.add(uidKey, adId );
			log.debug("添加关注缓存key:[{}] value:{}", uidKey, adId);
		}
	}
	
	/**
	 * 取消用户关注缓存
	 * @param uid
	 * @param adid
	 */
	public void delAttention(int uid, int adId) {
		String uidKey = UID_ADIDS_ATTENTION_KEY_PREFIX + uid;
		adCache.opsForSet().remove(uidKey, adId + "");
		log.debug("删除关注缓存key:[{}] value:{}", uidKey, adId);
	}
	
	/**
	 * 获取缓存中的所有用户关注
	 */
	public List<Object> getAdIdsAll(){
		List<Object> result = new ArrayList<Object>();
		Set<String> keys = adCache.keys(UID_ADIDS_ATTENTION_KEY_PREFIX+"*");
		for (String key : keys) {
			Set<String> adIds = adCache.opsForSet().members(key);
			String adIdsStr =  StringUtils.join(adIds, ",");
			String mKey = key.split(":")[1];
			AdAttention adAttention = new AdAttention();
			adAttention.setUid(Integer.parseInt(mKey));
			adAttention.setAdIds(new ArrayList<String>(adIds));
			result.add(adAttention);
			log.debug("用户uid:{} 对广告adIds:{}", mKey, adIdsStr);
		}
		return result;
	}
	
}