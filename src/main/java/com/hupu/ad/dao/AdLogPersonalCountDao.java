package com.hupu.ad.dao;

import java.util.Collection;
import java.util.Objects;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.hupu.ad.domain.AdLogPersonalCount;

/**
 * 对ad_log_personal_count表的操作
 * @author donghui
 */
@Component
public class AdLogPersonalCountDao extends AbstractDAO{

	public Collection<AdLogPersonalCount> findAll() {
		return adSqlSession.selectList("adLogPersonalCount.findAll");
	}
	
	public int insert(AdLogPersonalCount adLogPersonalCount) {
		return adSqlSession.insert("adLogPersonalCount.insert", adLogPersonalCount);
	}
	
	public int updateCountByAdIdAndDkcode(AdLogPersonalCount adLogPersonalCount){
		return adSqlSession.update("adLogPersonalCount.updateCountByAdIdAndDkcode", adLogPersonalCount);
	}
	
	public void batchInsert(Collection<AdLogPersonalCount> adLogPersonalCounts){
		StopWatch sw = new StopWatch();
		sw.start();
		log.debug("批量新增ad_log_personal_count表");
		SqlSession batchSqlSession = adSqlSession.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		for (AdLogPersonalCount adLogPersonalCount : adLogPersonalCounts) {
			batchSqlSession.insert("adLogPersonalCount.insert", adLogPersonalCount);
			log.debug(Objects.toString(adLogPersonalCount));
		}
		batchSqlSession.commit();
		batchSqlSession.close();
		log.debug("批量新增ad_log_personal_count表()条记录结果 time:{}",adLogPersonalCounts.size(), sw.getTime());
	}
	
	/**
	 * 批理更新点击和浏览数 ,存在则update,没有记录insert 
	 * @param adLogPersonalCount
	 */
	public void batchIncrementCount(Collection<AdLogPersonalCount> adLogPersonalCounts){
		StopWatch sw = new StopWatch();
		sw.start();
		log.debug("开始更新和新增ad_log_personal_count表");
		SqlSession sqlSession = adSqlSession.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		for (AdLogPersonalCount adLogPersonalCount : adLogPersonalCounts) {
			Integer id = (Integer)adSqlSession.selectOne("adLogPersonalCount.findByDkcode", adLogPersonalCount);
			if(id == null){
				sqlSession.insert("adLogPersonalCount.insert", adLogPersonalCount);
				log.debug("insert ad_log_personal_count表:{}", adLogPersonalCount);
				
			}
			else{
				adLogPersonalCount.setId(id);
				sqlSession.update("adLogPersonalCount.updateIncrementById", adLogPersonalCount);
				log.debug("update ad_log_personal_count表:{}", adLogPersonalCount);
			}
		}
		sqlSession.commit();
		sqlSession.close();
		log.debug("更新广告花费表ad_log_count完成 time:{}", sw.getTime());
	}
	
}