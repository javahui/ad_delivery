package com.hupu.ad.cache;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Component;

import com.hupu.ad.dao.AdListDao;
import com.hupu.ad.domain.AdList;

/**
 * 广告记录缓存操作类
 * @author donghui
 */
@Component
public class AdListCache extends AbstractCache{
	
	protected static Executor exec = Executors.newFixedThreadPool(10); 
	
	/**
	 * 广告表DAO
	 */
	@Resource 
	private AdListDao adListDao;
	
	/**
	 * 广告记录的KEY的前缀
	 */
	private final String ADLIST_KEY_PREFIX = "adList_" + adListVersion;
	
	/**
	 * 先从数据库查找,再保存到缓存
	 * @param adId 广告ID
	 */
	public AdList saveAdList(int adId){
		final AdList adList = adListDao.findNormalByAdId(adId);
		if(adList == null){
			log.debug("广告数据库不存在adId:{}", adId);
		}
		else{
			exec.execute(new Runnable(){
				@Override
				public void run() {
					saveAdList(adList);
				}
			});
		}
		return adList;
	}
	
	/**
	 * 保存到缓存
	 * @param adList 广告记录
	 */
	public void saveAdList(AdList adList){
		BoundHashOperations<String, String, AdList> listOps = listCache.boundHashOps(ADLIST_KEY_PREFIX);
		String adId = adList.getAdId() + "";
		listOps.put(adId, adList);
		log.debug("hset广告记录key:{} field:{} value:{}", new Object[]{ADLIST_KEY_PREFIX, adId, adList});
	}
	
	/**
	 * 删除缓存
	 * @param adId 广告ID
	 */
	public void delAdList(int adId){
		BoundHashOperations<String, String, AdList> listOps = listCache.boundHashOps(ADLIST_KEY_PREFIX);
		listOps.delete(adId + "");
		log.debug("hdel广告记录key:{} field:{}",ADLIST_KEY_PREFIX, adId);
	}
	
	/**
	 * 在缓存中查找
	 * @param adId 广告Id
	 */
	public AdList getAdList(int adId){
		BoundHashOperations<String, String, AdList> listOps = listCache.boundHashOps(ADLIST_KEY_PREFIX);
		AdList adList = listOps.get(adId + "");
		return adList;
	}
	
	/**
	 * 从缓存中获取,如没有则数据库中查找并保存到缓存中
	 * @param adId 广告id
	 * @return 
	 */
	public AdList getAndsaveAdList(int adId) {
		AdList adList = getAdList(adId);
		if(adList == null){
			log.debug("广告adId:{}缓存没有,从DB取");
			return saveAdList(adId);
		}
		return adList;
	}
	
	/**
	 * 根据缓存的key，获取所有对应的广告
	 */
	public Collection<AdList> getAdListAll(){
		BoundHashOperations<String, String, AdList> listOps = listCache.boundHashOps(ADLIST_KEY_PREFIX);
		return listOps.values();
	}
}