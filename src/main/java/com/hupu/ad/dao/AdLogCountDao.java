package com.hupu.ad.dao;

import java.util.Collection;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.hupu.ad.domain.AdLogCount;

/**
 * 对ad_log_count表的操作
 * @author donghui
 */
@Component
public class AdLogCountDao extends AbstractDAO{
	
	public Collection<AdLogCount> findAll() {
		return adSqlSession.selectList("adLogCount.findAll");
	}
	
	public int insert(AdLogCount adLogCount) {
		return adSqlSession.insert("adLogCount.insert", adLogCount);
	}
	
	public int updateCountById(AdLogCount adLogCount){
		return adSqlSession.update("adLogCount.updateCountById", adLogCount);
	}
	
	public int updateIncrementByAdIdAndDateline(AdLogCount adLogCount){
		return adSqlSession.update("adLogCount.updateIncrementByAdIdAndDateline", adLogCount);
	}
	
	public int updateCountByAdIdAndDateline(AdLogCount adLogCount){
		return adSqlSession.update("adLogCount.updateCountByAdIdAndDateline", adLogCount);
	}
	
	public int deleteByAdIdAndDateline(AdLogCount adLogCount){
		return adSqlSession.delete("adLogCount.deleteByAdIdAndDateline", adLogCount);
	}
	
	
	/**
	 * 批理更新点击和浏览数 ,存在则update,没有记录insert 
	 * @param adLogCounts
	 */
	public void batchIncrementCount(Collection<AdLogCount> adLogCounts){
		StopWatch sw = new StopWatch();
		sw.start();
		log.debug("开始更新和新增ad_log_count表");
		SqlSession sqlSession = adSqlSession.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		for (AdLogCount adLogCount : adLogCounts) {
			Integer id = (Integer)adSqlSession.selectOne("adLogCount.selectCountByadIdAndDateline", adLogCount);
			if(id == null){
				sqlSession.insert("adLogCount.insert", adLogCount);
				log.debug("insert ad_log_count表:{}", adLogCount);
			}
			else{
				adLogCount.setId(id);
				sqlSession.update("adLogCount.updateCountById", adLogCount);
				log.debug("update ad_log_count表:{}", adLogCount);
			}
		}
		sqlSession.commit();
		sqlSession.close();
		log.debug("更新广告花费表ad_log_count完成 time:{}", sw.getTime());
	}

}