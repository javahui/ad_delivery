package com.hupu.ad.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.hupu.ad.domain.AdLog;
import com.hupu.ad.domain.AdLogCount;
import com.hupu.ad.domain.AdLogPersonalCount;
import com.hupu.ad.util.UnixTimeUtils;

/**
 * 广告日志表(ad_log)DAO
 * @author donghui
 */
@Component
public class AdLogDao extends AbstractDAO{

	public List<Map> findIncome(){
		List<Map> list = adSqlSession.selectList("adLog.findIncome");
		return list;
	}
	
	/**
	 * 通过adId得到广告日志信息
	 * @param adId
	 * @return
	 */
	public AdLog findByAdId(int adId){
		return adSqlSession.selectOne("adLog.findAdLogByAdId", adId);
	}
	
	
	/**
	 * log全部记录合并成ad_log_persional_count
	 */
	public Map findStatistics(){
		return adSqlSession.selectOne("adLog.findStatistics");
	}
	
	/**
	 * log全部记录合并成ad_log_persional_count
	 */
	public List<AdLogPersonalCount> findLogPersonalCount(){
		return adSqlSession.selectList("adLog.findLogPersonalCount");
	}
	
	/**
	 * log全部记录合并成ad_log_count
	 */
	public List<AdLogCount> findLogCount(){
		return adSqlSession.selectList("adLog.findLogCount");
	}
	
	/**
	 * 是否找到今天的点击记录
	 */
	public boolean isFindTodayClick(AdLog adLog){
		HashMap map = new HashMap();
		map.put("startTime", UnixTimeUtils.getCurrentStartTime());
		map.put("endTime", UnixTimeUtils.getCurrentEndTime());
		map.put("adId", adLog.getAdId());
		map.put("actionDkcode", adLog.getActionDkcode());
		String adId = adSqlSession.selectOne("adLog.findTodayClick", map);
		if(StringUtils.isNotBlank(adId)){
			return true;
		}
		return false;
	}
	
	/**
	 * 批量插入AdLog,数据源来自mongodb
	 */
	public void batchInsert(Collection<AdLog> adLogs){
		StopWatch sw = new StopWatch();
		sw.start();
		log.debug("开始新增ad_log表");
		SqlSession batchSqlSession = adSqlSession.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		for (AdLog adLog : adLogs) {
			batchSqlSession.insert("adLog.insert",adLog);
			log.debug(adLog.toString());
		}
		batchSqlSession.commit();
		batchSqlSession.close();
		log.debug("新增ad_log表完成 time:{}", sw.getTime());
	}
	
	/**
	 * 更新为已分成
	 */
	public void updateIncomeInValid(){
		int updateNum = adSqlSession.update("adLog.updateIncomeInValid");
		log.debug("更新了{}条记录为已分成", updateNum);
	}
	
	/**
	 * 更新为未分成
	 */
	public void updateIncomeValid(){
		int updateNum = adSqlSession.update("adLog.updateIncomeValid");
		log.debug("更新了{}条记录为未分成", updateNum);
	}
}
