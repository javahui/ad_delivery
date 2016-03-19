package com.hupu.ad.dao;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 存放sqlSession
 * @author donghui
 */
public class AbstractDAO {
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 广告DAO
	 */
	@Resource 
	protected SqlSessionTemplate adSqlSession;
	
	@Resource
	protected JdbcTemplate adJdbcTemplate;
}
	
