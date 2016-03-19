package com.hupu.ad.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

/**
 * 公司花费表ad_company_cost
 * @author donghui
 */
@Component
public class AdCompanyCostDao extends AbstractDAO{

	@Resource private AdConfigDao adConfigDao;
	
	/**
	 * 公司扣费操作
	 */
	public void batchUpdateCostMoney(List<Map> paramList){
		if(adConfigDao.isCurrentTimeFree()){
			return;
		}
		StopWatch sw = new StopWatch();
		sw.start();
		log.debug("开始更新广告花费表ad_company_cost");
		SqlSession sqlSession = adSqlSession.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		for (Map param : paramList) {
			sqlSession.update("adCompanyCost.updateAdCompanyCostMoney", param);
			log.debug("公司cid:{} 扣费:{}", param.get("cid"), param.get("costMoney"));
		}
		sqlSession.commit();
		sqlSession.close();
		log.debug("更新广告花费表ad_company_cost完成 time:{}", sw.getTime());
	}

}
