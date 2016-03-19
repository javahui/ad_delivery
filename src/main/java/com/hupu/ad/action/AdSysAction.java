package com.hupu.ad.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hupu.ad.scheduling.AdLogQueueScheduling;
import com.hupu.ad.scheduling.CheckScheduling;
import com.hupu.ad.service.AdAttentionService;
import com.hupu.ad.service.AdIncomeService;
import com.hupu.ad.service.AdSysService;

/**
 * 用于后台管理action
 * @author donghui
 */
@Controller
public class AdSysAction {
	//private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource private AdSysService adSysService;
	@Resource private AdAttentionService adAttentionService;
	@Resource private AdLogQueueScheduling adLogQueueScheduling;
	@Resource private AdIncomeService adIncomeService;
	@Resource private CheckScheduling checkScheduling;
	
	/**
	 * 开启广告
	 * @param adId 广告ID
	 */
	@RequestMapping(value="/startAdList/{adIds}", method=RequestMethod.GET)
	@ResponseBody
	public void startAdList(@PathVariable String adIds){
		adSysService.startAdList(adIds);
	}
	
	/**
	 * 暂停广告
	 * @param uid 用户ID
	 */
	@RequestMapping(value="/stopAdList/{adIds}", method=RequestMethod.GET)
	@ResponseBody
	public void stopAdList(@PathVariable String adIds){
		adSysService.stopAdList(adIds);
	}
	
	/**
	 * 删除广告
	 * @param uid
	 */
	@RequestMapping(value="/delAdList/{adIds}", method=RequestMethod.GET)
	@ResponseBody
	public void delAdList(@PathVariable String adIds){
		adSysService.delAdList(adIds);
	}
	
	/**
	 * 手动触动,校验广告是否投放,并开启
	 */
	@RequestMapping(value="/checkCrowd", method=RequestMethod.GET, produces="text/plain;charset=utf-8")
	@ResponseBody
	public String checkCrowd(HttpServletResponse response){
		checkScheduling.checkCrowd();
		return "手动触动校验广告是否投放成功!!!";
	}
	
	/**
	 * 手动触动,每天晚上统计个人广告日志，对用户进行分成
	 */
	@RequestMapping(value="/income", method=RequestMethod.GET )
	@ResponseBody
	public String income(HttpServletResponse response){
		adIncomeService.income();
		return "手动触动统计个人广告日志，对用户进行分成成功!!!";
	}
	
	/**
	 * 手动触动,校验ad_log表记录与ad_log_count表的数据一致性
	 */
	@RequestMapping(value="/data", method=RequestMethod.GET, produces="text/plain;charset=utf-8")
	@ResponseBody
	public String data(HttpServletResponse response){
		checkScheduling.checkAdLogCount();
		return "手动触动校验ad_log表记录与ad_log_count表的数据一致性成功!!!";
	}
	
	/**
	 * 手动触动,校验ad_log表记录与ad_log_presonal_count表的数据一致性
	 */
	@RequestMapping(value="/presonal", method=RequestMethod.GET, produces="text/plain;charset=utf-8")
	@ResponseBody
	public String presonal(HttpServletResponse response){
		checkScheduling.checkAdLogPersonalCount();
		return "手动触动校验ad_log表记录与ad_log_presonal_count表的数据一致性成功!!!";
	}
	
	
	/**
	 * 手动触动,全部重新分成
	 */
	@RequestMapping(value="/renewgen", method=RequestMethod.GET, produces="text/plain;charset=utf-8")
	@ResponseBody
	public String renewgen(HttpServletResponse response){
		checkScheduling.checkIncome();
		return "手动触动全部重新分成成功!!!";
	}
	
	/**
	 * 手动触动,全部生成用户关注缓存
	 */
	@RequestMapping(value="/uattenation", method=RequestMethod.GET, produces="text/plain;charset=utf-8")
	@ResponseBody
	public String uattenation(HttpServletResponse response){
		checkScheduling.checkCacheAttention();
		return "手动触动全部生成用户关注缓存成功!!!";
	}
	
	/**
	 * 手动触动,全部重新生成年龄范围缓存
	 */
	@RequestMapping(value="/agerange", method=RequestMethod.GET, produces="text/plain;charset=utf-8")
	@ResponseBody
	public String agerange(HttpServletResponse response){
		checkScheduling.checkAllAgeRangeCache();
		return "手动触动全部重新生成年龄范围缓存成功!!!";
	}
	
	/**
	 * 手动触动,对没进行分成的adlog记录进行分成
	 */
	@RequestMapping(value="/recordsinto", method=RequestMethod.GET, produces="text/plain;charset=utf-8")
	@ResponseBody
	public String recordsInto(HttpServletResponse response){
		adIncomeService.income();
		return "手动触动-对没进行分成的adlog记录进行分成成功!!!";
	}
	
	
	
	/**
	 * 手动触动,计算扣费
	 * @param cid
	 */
	@RequestMapping(value="/clearQueueAndWriteDB", method=RequestMethod.GET, produces="text/plain;charset=utf-8")
	@ResponseBody
	public String clearQueueAndWriteDB(){
		adLogQueueScheduling.clearQueueAndWriteDB();
		return "手动触动清空队列成功!!!";
	}
	
	/**
	 * 取消关注
	 * @param cid
	 */
	@RequestMapping(value="/delAttention", method=RequestMethod.GET, produces="text/plain;charset=utf-8")
	@ResponseBody
	public String delAttention(@RequestParam int uid,@RequestParam int adId){
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("uid", uid);
		map.put("adId", adId);
		adAttentionService.delAttention(JSON.toJSONString(map));
		return "取消广告缓存成功!!!";
	}
}