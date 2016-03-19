package com.hupu.ad.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class AdCustomHistoryDao extends AbstractDAO{

	public void insert(Map param){
		adSqlSession.insert("adCustomHistory.insert", param);
	}
	
}
