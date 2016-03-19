package com.hupu.ad.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hupu.ad.domain.AdList;
import com.hupu.ad.util.UnixTimeUtils;

/**
 * 广告信息表DAO
 * @author donghui
 */
@Component
public class AdListDao extends AbstractDAO{

	/**
	 * 通过adId得到正常(sort=3)广告信息
	 * @param adId
	 * @return
	 */
	public AdList findNormalByAdId(int adId){
		return (AdList)adSqlSession.selectOne("adList.findNormalByAdId", adId);
	}
	
	/**
	 * 通过adId得到广告信息
	 * @param adId
	 * @return
	 */
	public Map find(int adId){
		return adSqlSession.selectOne("adList.find", adId);
	}
	
	/**
	 * 得到所有公司没钱的广告ID
	 */
	public Collection<Integer> findCompanyMoneyOut(){
		return adSqlSession.selectList("adList.findCompanyMoneyOut");
	}
	
	/**
	 * 能过adId得到广告信息
	 * @param adId
	 * @return adIds
	 */
	public Collection<Integer> findByCurdate(){
		return adSqlSession.selectList("adList.findByCurdate", UnixTimeUtils.getCurrentUnixTimeDD());
	}
	
	/**
	 * 查找公司所有出未到预算adId
	 */
	public List<Integer> findByCid(int cid){
		return adSqlSession.selectList("adList.findByCid", cid);
	}
	
	/**
	 * 查找公司所有正常的记录
	 */
	public List<Integer> findNormal(){
		return adSqlSession.selectList("adList.findNormal", UnixTimeUtils.getCurrentUnixTimeSS());
	}
}
