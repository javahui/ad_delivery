package com.hupu.ad.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import com.hupu.ad.dao.AdCrowdDao;
import com.hupu.ad.domain.AdCrowd;

/**
 * 广告投放信息缓存操作类
 * @author donghui
 */
@Component
public class AdCrowdCache extends AbstractCache{
	
	/**
	 * 投放信息表DAO
	 */
	@Resource private AdCrowdDao adCrowdDao;
	
	/**
	 * 投放条件key的前缀(ads:)
	 */
	private final String CROWD_KEY_PREFIX =  "ads:";
	
	/**
	 * 投放条件年龄key的前缀(ads_age:)
	 */
	private final String CROWD_AGE_KEY_PREFIX =  "ads_age:";
	
	/**
	 * 投放条件年龄最大值(999)
	 */
	private final int MAX_AGE =  999;
	
	/**
	 * 广告的投放条件生成的key按广告ID,广告ID为value,保存到缓存中
	 * @param adId 广告ID
	 */
	public void saveCrowdByadId(int adId) {
		Collection adIds = new HashSet();
		adIds.add(adId);
		this.saveCrowdByadId(adIds);
	}
	
	/**
	 * 广告的投放条件生成的key按广告ID,广告ID为value,保存到缓存中
	 * @param adIds 广告ID集合
	 */
	public void saveCrowdByadId(Collection<Integer> adIds) {
		SetOperations<String, String> ops = adCache.opsForSet();
		Collection<AdCrowd> adCrowdList = adCrowdDao.findByAdId(adIds);
		List<String> ageRangeList = new ArrayList();
		for (AdCrowd adCrowd : adCrowdList) {
			Set<String> crowdKeys = this.getCrowdByadId(adCrowd);
			int adId = adCrowd.getAdId();
			if(CollectionUtils.isEmpty(crowdKeys)){
				log.debug("广告没有投放条可供增加adId:{}", adId);
				return;
			}
			for (String key : crowdKeys) {
				ops.add(key, adId + "");
			}
			log.debug("生成投放条件size:{} adId:{} keys:{}", new Object[]{crowdKeys.size(), adId ,StringUtils.abbreviate(crowdKeys.toString(), 500) });
			ageRangeList.add(adCrowd.getAgeRange());
		}
		this.saveAgeRangeCache(ageRangeList);
	}
	
	
	/**
	 *  删除一个广告的投放条件的缓存
	 *   @param adId 广告ID
	 */
	public void delCrowdByadId(int adId) {
		Collection adIds = new HashSet();
		adIds.add(adId);
		this.delCrowdByadId(adIds);
	}
	
	/**
	 *  删除多个广告的投放条件的缓存
	 *   @param adIds 广告ID集合
	 */
	public void delCrowdByadId(Collection<Integer> adIds) {
		SetOperations<String, String> ops = adCache.opsForSet();
		for (Integer adId : adIds) {
			Set<String> crowdKeys = this.getCrowdByadId(adId);
			if(CollectionUtils.isEmpty(crowdKeys)){
				log.debug("广告没有投放条件可供删除adId:{}", adId);
				return;
			}
			for (String key : crowdKeys) {
				ops.remove(key, adId + "");
			}
			log.debug("删除投放条件size:{} adId:{} keys:{}", new Object[]{crowdKeys.size(), adId ,StringUtils.abbreviate(crowdKeys.toString(), 500) });
		}
	}
	
	/**
	 * 得到投放条件的所有adIds
	 * @param regionArray 地区
	 * @param age 年龄
	 * @param sexArray 性别
	 * @param interestArray 频道
	 * @return adIds 广告ID集
	 */
	public Collection<String> getAdIdsByCrowd(String[] regionArray, int age, Object[] sexArray, Object[] interestArray) {
		Set<String> ageRangeArray = this.getAgeRangeArray(age);
		if(CollectionUtils.isEmpty(ageRangeArray)){
			return CollectionUtils.EMPTY_COLLECTION;
		}
		ArrayList<String> keys = new ArrayList();
		for (String region : regionArray) {
			for (String ageRange : ageRangeArray) {
				for (Object sex : sexArray) {
					for (Object interest : interestArray) {
						keys.add(this.createCrowdCacheKey(region, ageRange, sex, interest));
					}
				}
			}
		}
		Set<String> adIds = adCache.opsForSet().union(" ", keys);
		log.debug("得到投放条件key:{}", StringUtils.join(keys,";"));
		log.debug("得到投放条件value 广告:{}", adIds);
		return adIds;
	}

	/**
	 * 判断广告是否投放
	 * @param adId
	 * @return
	 */
	public List<String> getAdIdsByCrowd(String adId) {
		List<String> list = new ArrayList<String>();
		SetOperations<String, String> ops = adCache.opsForSet();
		if(StringUtils.isNotEmpty(adId)) {
			Set<String> keys = this.getCrowdByadId(Integer.parseInt(adId));
			for (String key : keys) {
				boolean result = ops.isMember(key, adId);
				list.add(key + "[" + result + "]");
			}
			log.debug("[广告id:{}]的投放条件结果:{}", adId, list);
		}
		return list;
	}
	
	/**
	 * 判断广告是否投放(只判断广告对应的一个投放条件)
	 * @param adId
	 * @return
	 */
	public boolean isCrowd(int adId){
		Set<String> keys = this.getCrowdByadId(adId);
		String key = keys.iterator().next();
		return adCache.opsForSet().isMember(key, adId+"");
	}
	
	/**
	 * 生成投放年龄段的缓存
	 */
	public void saveAgeRangeCache(List<String> ageRangeList){
		for (String ageRange : ageRangeList) {
			String[] ageArray = StringUtils.split(ageRange, "_");
			if(ArrayUtils.getLength(ageArray) != 2){
				log.error("年龄段数据不正常:{}", ageRange);
				continue;
			}
			int startAge = NumberUtils.toInt(ageArray[0]);
			int endAge = NumberUtils.toInt(ageArray[1]);
			//大于65岁,全部保存视为(ads_age:999)
			if(endAge > 65){
				adCache.opsForSet().add(CROWD_AGE_KEY_PREFIX + this.MAX_AGE, ageRange);
				endAge = 65;
			}
			for (int i = 0; i <= endAge - startAge; i++) {
				adCache.opsForSet().add(CROWD_AGE_KEY_PREFIX + (startAge + i), ageRange);
			}
			log.debug("生成投放广告年龄段的缓存:{}", ageRange);
		}
	}
	
	/**
	 * 删除所有投放条件年龄缓存
	 */
	public void delAllAgeRangeCache(){
		Set<String> set = adCache.keys(this.CROWD_AGE_KEY_PREFIX + "*");
		if(CollectionUtils.isNotEmpty(set)){
			adCache.delete(set);
		}
	}
	
	/**
	 * 根据缓存的key，获取所有对应的广告
	 */
	public Map getAgeRangeAll(){
		Map<String,String> map = new HashMap<String,String>();
		Set<String> sets = adCache.keys(this.CROWD_AGE_KEY_PREFIX + "*");
		for (String key : sets) {
			Set<String> value = adCache.opsForSet().members(key);
			map.put(key, value.toString());
		}
		return map;
	}
	
	/**
	 * 按广告生成投放条件的redis KEY的集合
	 * @param adId 广告ID
	 * @return [地区 + 年龄 + 性别 + 频道]的string集合
	 */
	private Set<String> getCrowdByadId(int adId) {
		AdCrowd adCrowd = adCrowdDao.findByAdId(adId);
		return this.getCrowdByadId(adCrowd);
	}
	
	/**
	 * 按广告生成投放条件的redis KEY的集合
	 * @param adCrowd 投放条件记录
	 * @return [地区 + 年龄 + 性别 + 频道]的string集合
	 */
	private Set<String> getCrowdByadId(AdCrowd adCrowd) {
		Set<String> result = new HashSet<String>();
		if(adCrowd == null){
			log.debug("投放条件记录为空");
			return result;
		}
		String[] regionArray = StringUtils.split(adCrowd.getRegion(), ",");//地区
		String[] genderArray = StringUtils.split(adCrowd.getGender(), ",");//投放性别
		String[] interestArray = StringUtils.split(adCrowd.getInterest(), ",");//投放频道
		int adId = adCrowd.getAdId();
		if(ArrayUtils.isEmpty(regionArray)){
			log.debug("投放地为空 adId:{}", adId);
			return result;
		}
		if(ArrayUtils.isEmpty(interestArray)){
			log.debug("投放频道为空 adId:{}", adId);
			return result;
		}
		for (String region_ : regionArray) {
			for (String gender_ : genderArray) {
				for (String interest_ : interestArray) {
					String crowdCacheKey = createCrowdCacheKey(region_, adCrowd.getAgeRange(), gender_, interest_);
					result.add(crowdCacheKey);
				}
			}
		}
		return result;
	}
	
	/**
	 * 组成投放信息的KEY
	 */
	private String createCrowdCacheKey(Object... args){
		return CROWD_KEY_PREFIX + StringUtils.join(args, ",");
	}
	
	/**
	 * 得到年龄区间值
	 */
	private Set<String> getAgeRangeArray(int age){
		if(age > 65){
			age = MAX_AGE;
		}
		Set<String> ageRangeArray = adCache.opsForSet().members(CROWD_AGE_KEY_PREFIX + age);
		log.debug("age:{} 年龄区间:{}",age, ageRangeArray);
		return ageRangeArray;
	}
}