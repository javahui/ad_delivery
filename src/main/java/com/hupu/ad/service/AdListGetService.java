package com.hupu.ad.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.hupu.ad.cache.AdAttentionCache;
import com.hupu.ad.cache.AdCrowdCache;
import com.hupu.ad.cache.AdCustomCache;
import com.hupu.ad.cache.AdListCache;
import com.hupu.ad.cache.UserInfoCache;
import com.hupu.ad.dao.AdCustomDao;
import com.hupu.ad.domain.AdList;

@Component
public class AdListGetService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Resource private AdListCache adListCache;
	@Resource private AdCrowdCache adCrowdCache;
	@Resource private AdCustomDao adCustomDao;
	@Resource private AdCustomCache adCustomCache;
	@Resource private AdAttentionCache adAttentionCache;
	@Resource private UserInfoCache userInfoCache;
	
	/**
	 * 取出多条广告记录
	 * @param uid 登录用户id
	 * @param interests 相关频道
	 * @param num 获取广告数
	 * @param webId 当前页面WebId
	 * @return
	 */
	public Collection<AdList> getWebAd( int uid, String interest, int num, int webId){
		log.debug("取WEB广告 uid:{} interests:{} num:{} webId:{}", new Object[]{uid, interest, num, webId});
		if("0".equals(interest)){
			log.debug("个人用户 interest为0");
			return null;
		}
		Map user = userInfoCache.getUser(uid);
		if(user == null){
			log.debug("缓存未命中用户uid:{}", uid);
			return null;
		}
		String cityid = MapUtils.getString(user, "cityid" ,"0");
		int birthday = MapUtils.getIntValue(user, "birthday");
		String sex = MapUtils.getString(user, "sex", "3");
		String[] regionArray = this.getRegionValue(cityid);
		int age = this.getAge(birthday);
		String[] sexArray = this.getSexValue(sex);
		Object[] interestArray = ArrayUtils.subarray(StringUtils.split(interest, "_"), 0, 2);
		
		Collection<String> crowdAdIds = adCrowdCache.getAdIdsByCrowd(regionArray, age, sexArray, interestArray);
		if(CollectionUtils.isEmpty(crowdAdIds)){
			log.debug("没有符合投放条件的广告记录!!");
			return null;
		}
		return this.getAdLists(uid, crowdAdIds, num, webId);
	}
	
	/**
	 * 得到广告按uid
	 * @param uid 当前用户
	 * @param actionDkcode 被访问者dkcode
	 * @param num 最大广告数
	 * @return 个人页面的广告集
	 */
	public Collection<AdList> getAdListByUid(int uid, int actionDkcode, int num){
		log.debug("获取个人广告 uid:{} actionDkcode:{} num:{}", new Object[]{uid, actionDkcode, num});
		Integer actionUid = adCustomDao.findUidByDkcode(actionDkcode);
		if(actionUid == null){
			log.debug("ad_custom表 dkcode:{}的uid为空", actionDkcode);
			return null;
		}
		Set<String> userAdIds = adCustomCache.getAdIdsByUid(actionUid);
		return this.getAdLists(uid, userAdIds, num , -1);
	}
	
	/**
	 * 对广告ID集合进行筛选
	 * @param currentUid 当前用户uid
	 * @param adIds 要进行筛选的广告id集合
	 * @param num 得到广告数量
	 * @param webId WEB广告页面用来不显示所有页面的广告
	 */
	private Collection<AdList> getAdLists(int currentUid, Collection<String> adIds, int num, int webId){
		//如果取出的web广告中,当前用户已关注过,则不显示
		Set<String> cuidAdIds = adAttentionCache.getAdIdsByUid(currentUid);
		ArrayList<String> adIdList = new ArrayList(CollectionUtils.subtract(adIds, cuidAdIds));
		//从广告ID集合中随机取出num个广告
		//this.weightSort(adIds, num);
		int len = NumberUtils.min(adIdList.size(), num, 6);
		Collection<AdList> result = new HashSet();
		for (int i = 0; i < len; i++) {
			int index = RandomUtils.nextInt(0, adIdList.size());
			int adId = NumberUtils.toInt(adIdList.get(index));
			AdList adList = adListCache.getAndsaveAdList(adId);
			//记录不为空,不是自己所在web页的广告
			if(adList != null && adList.getClassifyId() != webId){
				result.add(adList);
				adIdList.remove(index);
			}
		}
		log.debug("获取的广告记录:{}", JSON.toJSONString(result));
		return result;
	}
	
	/**
	 * 根据年龄返回所在范围的值
	 * @param age 年龄
	 * @return 范围
	 * 返回正常年龄值
	 */
	private int getAge(int birthday){
		//如年纪不限(为0).则随机20到50岁
		if(birthday == 0){
			return 20 + RandomUtils.nextInt(0, 30) ;
		}
		long currentTime = Calendar.getInstance().getTimeInMillis() / 1000;
		long age = (currentTime - birthday) / (365 * 24 * 60 * 60);
		return (int)age;
	}
	/**
	 * 保密(3)的用户,返回1:男,2:女
	 */
	private String[] getSexValue(String sex){
		if("3".equals(sex)){
			return new String[]{"1","2"};
		}
		return new String[]{sex};
	}
	
	/**
	 * 得到所有上级地区,直辖市为2位数
	 * @param cityid 城市id(例:330102)
	 * @return 按从城市到国家排列  (例:["3301","33","1"])
	 */
	private String[] getRegionValue (String cityid){
		if("0".equals(cityid) || StringUtils.isBlank(cityid)){
			return new String[]{"1"};
		}
		String city = StringUtils.substring(cityid, 0, 4);
		String province = StringUtils.substring(cityid, 0, 2);
		return new String[]{city, province, "1"};
	}
}
