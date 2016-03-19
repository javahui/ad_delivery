package com.hupu.ad.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hupu.ad.domain.AdAttention;
import com.hupu.ad.util.UnixTimeUtils;

@Component
public class AdAttentionDao extends AbstractDAO {
	
	
	public List<Map> findValid(){
		return adSqlSession.selectList("adAttention.findValid");
	}
	
	/**
	 * 添加广告关注
	 * @param uid 
	 * @param adId
	 * @return insert记录返回true,update记录返回false
	 */
	public boolean addAttention(int uid, int adId){
		AdAttention adAttention = new AdAttention();
		adAttention.setAdId(adId);
		adAttention.setUid(uid);
		adAttention.setIsAttentioned(1);
		if(adSqlSession.update("adAttention.updateIsAttentioned", adAttention) == 0){
			adAttention.setDateline(UnixTimeUtils.getCurrentUnixTimeSS());
			adSqlSession.insert("adAttention.insert", adAttention);
			return true;
		}
		return false;
	}
	
	/**
	 * 取消广告关注
	 */
	public void delAttention(int uid, int adId){
		AdAttention adAttention = new AdAttention();
		adAttention.setAdId(adId);
		adAttention.setUid(uid);
		adAttention.setIsAttentioned(0);
		adSqlSession.update("adAttention.updateIsAttentioned", adAttention);
	}
	
}
