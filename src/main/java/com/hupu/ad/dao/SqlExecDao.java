package com.hupu.ad.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class SqlExecDao extends AbstractDAO {
	
	public List<Map<String,Object>> execSelect(String sql) {
		return adJdbcTemplate.queryForList(sql);
	}
	
	public int execUpdateOrInsert(String sql) {
		return adJdbcTemplate.update(sql);
	}
}
