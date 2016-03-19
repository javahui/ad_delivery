package com.hupu.ad.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.hupu.ad.util.UnixTimeUtils;
@Component
public class AdPayLogDao extends AbstractDAO{

	public void batchInsert(List<Map> paramList){
		StopWatch sw = new StopWatch();
		sw.start();
		int dateline = UnixTimeUtils.getCurrentUnixTimeSS();
		log.debug("开始新增扣费日志表");
		SqlSession sqlSession = adSqlSession.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		for (Map param : paramList) {
			param.put("dateline", dateline);
			sqlSession.insert("adPayLog.insert", param);
			log.debug(param.toString());
		}
		sqlSession.commit();
		sqlSession.close();
		log.debug("新增扣费日志表ad_pay_log完成 time:{}", sw.getTime());
	}
	
}
