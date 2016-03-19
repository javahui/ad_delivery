package com.hupu.ad.dao;

import java.util.Collection;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.hupu.ad.domain.AdIncomeExpensesSingle;

@Component
public class AdIncomeExpensesSingleDao extends AbstractDAO{

	public void batchUpdate(Collection<AdIncomeExpensesSingle> adIncomeExpensesSingles){
		StopWatch sw = new StopWatch();
		sw.start();
		log.debug("更新adIncome_expenses_single表");
		SqlSession batchSqlSession = adSqlSession.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		for (AdIncomeExpensesSingle adIncomeExpensesSingle : adIncomeExpensesSingles) {
			Integer id = adSqlSession.selectOne("adIncomeExpensesSingle.find", adIncomeExpensesSingle);
			if(id == null){
				batchSqlSession.insert("adIncomeExpensesSingle.insert", adIncomeExpensesSingle);
				log.debug("inert into ad_income_expenses_single表 {}", adIncomeExpensesSingle);
			}
			else{
				adIncomeExpensesSingle.setId(id);
				batchSqlSession.update("adIncomeExpensesSingle.updateMoney", adIncomeExpensesSingle);
				log.debug("update ad_income_expenses_single表 {}", adIncomeExpensesSingle);
			}
		}
		batchSqlSession.commit();
		batchSqlSession.close();
		log.debug("更新adIncome_expenses_single表结束 time:{}", sw.getTime());
	}
	
	public void deleteAll() {
		int delNum = adSqlSession.delete("adIncomeExpensesSingle.deleteAll");
		log.debug("ad_income_expenses_single修改{}了记录", delNum);
	}
}
