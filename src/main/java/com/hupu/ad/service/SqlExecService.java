package com.hupu.ad.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hupu.ad.dao.SqlExecDao;

@Component
public class SqlExecService {
	public final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource private SqlExecDao sqlExecDao;
	
	@Transactional(readOnly=true)
	public List<Map<String,Object>> execSelect(String sql) {
		return sqlExecDao.execSelect(sql);
	}
	
	public int execUpdateOrInsert(String sql) {
		return sqlExecDao.execUpdateOrInsert(sql);
	}
}
