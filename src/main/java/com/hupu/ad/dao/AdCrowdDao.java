package com.hupu.ad.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.hupu.ad.domain.AdCrowd;

@Component
public class AdCrowdDao extends AbstractDAO{

	/**
	 * 能过adId得到广告投放信息
	 * @param adId
	 * @return
	 */
	public AdCrowd findByAdId(int adId){
		return adSqlSession.selectOne("adCrowd.findByAdId", adId);
	}
	
	/**
	 * 能过adId得到广告投放信息
	 * @param adId
	 * @return
	 */
	public Collection<AdCrowd> findByAdId(Collection<Integer> adIds){
		if(CollectionUtils.isEmpty(adIds)){
			return CollectionUtils.EMPTY_COLLECTION;
		}
		return adSqlSession.selectList("adCrowd.findByAdIds", new ArrayList(adIds));
	}
	
	/**
	 * 能过adId得到广告投放信息的年龄范围
	 * @param adId
	 * @return
	 */
	public String findAgeByAdId(int adId){
		return adSqlSession.selectOne("adCrowd.findAgeByAdId", adId);
	}
	
	/**
	 * 得到所有年龄范围
	 * @return
	 */
	public List<String> findAllAgeRange(){
		return adSqlSession.selectList("adCrowd.findAllAgeRange");
	}
}
