package com.hupu.ad.dao;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

@Component
public class AdConfigDao extends AbstractDAO{

	/**
	 * 得到分成比例值
	 */
	public double findAssign(){
		String result = adSqlSession.selectOne("AdConfig.findByVar", "assign_m");
		return NumberUtils.toDouble(result, 1);
	}
	
	/**
	 * 当前时间是否是免费时间
	 */
	public boolean isCurrentTimeFree(){
		String beginTime = adSqlSession.selectOne("AdConfig.findByVar", "free_begin_time");
		String endTime = adSqlSession.selectOne("AdConfig.findByVar", "free_end_time");
		String[] parsePatterns = {"yyyy-MM-dd"};
		Date beginTimeDate = null;
		Date endTimeDate = null;
		try {
			beginTimeDate = DateUtils.parseDate(beginTime, parsePatterns);
			endTimeDate = DateUtils.parseDate(endTime, parsePatterns);
		} catch (ParseException e) {
			log.error("AdConfigDao findFreeTime ParseException", e);
		}
		Date currentDate = new Date();
		return (beginTimeDate.before(currentDate) && endTimeDate.after(currentDate));
	}
	
}
