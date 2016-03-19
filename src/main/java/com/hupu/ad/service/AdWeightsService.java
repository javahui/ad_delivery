package com.hupu.ad.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hupu.ad.dao.AdCostDao;
import com.hupu.ad.domain.AdCost;
import com.hupu.ad.domain.BidLevel;
import com.hupu.ad.util.RandomEngineUtils;

@Component
public class AdWeightsService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Resource private AdCostDao adCostDao;
	
	/**
	 * 权重排序
	 */
	public Set<String> weightSort(Set<String> adIds, int maxNum){
		log.debug("获取广告竞价排名列表 adIds:{} ", adIds);
		List<Integer> adIdList = new ArrayList<Integer>();
		for (String adId : adIds) {
			adIdList.add(Integer.parseInt(adId));
		}
		List<AdCost> adCostlist = adCostDao.findBidByAdIds(adIdList);
		if(CollectionUtils.isEmpty(adCostlist)) {
			log.debug("获取广告竞价排名列表adIds:{}为空", adIds);
		}
		adIds.clear();
		Map<String, Integer> keyChanceMap = new HashMap<String, Integer>();  
        Map<String, Integer> calResult = new HashMap<String, Integer>(); 
        List<BidLevel> bidLevelList = new ArrayList<BidLevel>();
        String level = "";
        Integer ratio = 0;
        for(AdCost adCost : adCostlist) {
        	level = getBidLevel(adCost.getBid().doubleValue());
        	ratio =getBidLevelRatio(level);
        	//log.debug("当前广告竞价 adId:{},bid:{},level:{},ratio:{}",new Object[]{adId,bid,level,ratio});
			keyChanceMap.put(adCost.getAdId()+"", ratio);
        }
        for(int i=0;i<adCostlist.size();i++) {
        	String key = RandomEngineUtils.chanceSelect(keyChanceMap);  
			//log.debug("当前广告竞价出场率level:{}",key);
            if(calResult.containsKey(key)) {  
            	calResult.put(key, calResult.get(key) + 1);  
            }  
            else {  
            	calResult.put(key, 1);  
            }  
        }
        for (String key : calResult.keySet()) {  
        	BidLevel bidLevel = new BidLevel();
        	bidLevel.setAdId(Integer.parseInt(key));
        	bidLevel.setCount(calResult.get(key));
        	bidLevelList.add(bidLevel);
        	log.debug("当前广告竞价结果key:{},value:{}",key,calResult.get(key));
        }  
        //取广告数num,广告集合size,最大广告数(6),取最小的值
        int num = NumberUtils.min(bidLevelList.size(), maxNum, 6);
        adIds = this.getData(bidLevelList, num);
        return adIds;
	}
	
	/**
	 * 获取排名运算后指定条数的广告Id
	 * 
	 * 
	 * @param srcList
	 * @param num
	 * @return
	 */
	private Set<String> getData(List<BidLevel> bidLevelList,int num) {
		if(bidLevelList.size() < num) {
			log.debug("获取广告竞价排名列表bidLevelList.size():{},num:{}", bidLevelList.size(),num);
			return null;
		}
		Set<String> adIdsList = new HashSet<String>();
		Collections.sort(bidLevelList,new Comparator<BidLevel>() {
			public int compare(BidLevel o1, BidLevel o2) {
				return o2.getCount()- o1.getCount() ;
			}
		});
		for(int k=0;k<num;k++) {
			adIdsList.add(bidLevelList.get(k).getAdId().toString());
		}
		return adIdsList;
	}
	
	/**
	 * 获取竞价的级别
	 * 
	 * @param bid
	 * @return
	 */
	private String getBidLevel(Double bid) {
		String level = "A";
		if(0.0<bid && 1.0 >= bid) {
    		level = "A";
    	}else if(1.0 < bid && 100.0 >= bid) {
    		level = "B";
    	}else if(100.0 < bid && 500.0 >= bid) {
    		level = "C";
    	}else if(500.0 < bid) {
    		level = "D";
    	}
		return level;
	}
	
	/**
	 * 获取竞价等级对应的概率
	 * 
	 * @param level
	 * @return
	 */
	private Integer getBidLevelRatio(String level) {
		Integer i = 0;
		if("A".equals(level)) {
			i = 10;
		}else if("B".equals(level)) {
			i = 20;
		}else if("C".equals(level)) {
			i = 30;
		}else if("D".equals(level)) {
			i = 40;
		}
		return i;
	}
	
}
