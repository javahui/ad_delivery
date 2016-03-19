package com.hupu.ad.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.hupu.ad.util.UnixTimeUtils;

/**
 * 个人用户定制广告表
 * @author donghui
 */
@Component
public class AdCustomDao extends AbstractDAO{

	public String getAdIdsByUid(int uid){
		return adSqlSession.selectOne("adCustom.findAdIdsByAdUid", uid);
	}
	
	public Integer findUidByDkcode(int dkcode){
		return adSqlSession.selectOne("adCustom.findUidByDkcode", dkcode);
	}
	
	public void saveOrUpdate(Map param){
		param.put("dateline", UnixTimeUtils.getCurrentUnixTimeSS());
		if(adSqlSession.update("adCustom.update", param) == 0){
			adSqlSession.insert("adCustom.insert", param);
		}
	}
	
	public void updateStatus(int uid){
		adSqlSession.update("adCustom.updateStatus", uid);
	}
	
}
