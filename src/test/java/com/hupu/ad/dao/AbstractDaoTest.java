package com.hupu.ad.dao;

import org.apache.ibatis.session.SqlSession;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath*:spring/*.xml" })
public class AbstractDaoTest extends AbstractJUnit4SpringContextTests{

	protected  final Logger logger = LoggerFactory.getLogger(getClass());

	@Ignore
	@Test
	public void test() {}
	
	@Autowired
	protected SqlSession adSqlSession;
}
