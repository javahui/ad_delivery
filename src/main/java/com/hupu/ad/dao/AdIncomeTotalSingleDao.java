package com.hupu.ad.dao;

import java.util.Collection;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.hupu.ad.domain.AdIncomeTotalSingle;
@Component
public class AdIncomeTotalSingleDao extends AbstractDAO{

	public void batchUpdate(Collection<AdIncomeTotalSingle> adIncomeTotalSingles){
		StopWatch sw = new StopWatch();
		sw.start();
		log.debug("开始更新ad_income_total_single表");
		SqlSession batchSqlSession = adSqlSession.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		for (AdIncomeTotalSingle adIncomeTotalSingle : adIncomeTotalSingles) {
			Integer id = adSqlSession.selectOne("adIncomeTotalSingle.find", adIncomeTotalSingle);
			if(id == null){
				batchSqlSession.insert("adIncomeTotalSingle.insert", adIncomeTotalSingle);
				log.debug("inert:{}", adIncomeTotalSingle);
			}
			else{
				adIncomeTotalSingle.setId(id);
				batchSqlSession.update("adIncomeTotalSingle.updateMoney", adIncomeTotalSingle);
				log.debug("update:{}", adIncomeTotalSingle);
			}
		}
		batchSqlSession.commit();
		batchSqlSession.close();
		log.debug("更新ad_income_total_single表结束 time:{}", sw.getTime());
	}
	
	public void deleteAll() {
		int delNum = adSqlSession.delete("adIncomeTotalSingle.deleteAll");
		log.debug("ad_income_total_single修改{}了记录", delNum);
	}

}
