package com.hupu.ad.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hupu.ad.cache.AdAttentionCache;
import com.hupu.ad.cache.AdCrowdCache;
import com.hupu.ad.cache.AdCustomCache;
import com.hupu.ad.cache.AdListCache;
import com.hupu.ad.cache.AdLogQueueCache;
import com.hupu.ad.cache.UserInfoCache;
import com.hupu.ad.domain.AdList;
import com.hupu.ad.domain.AdLog;
import com.hupu.ad.service.AdLogService;
import com.hupu.ad.service.SqlExecService;

/**
 * 用于后台管理广告缓存action
 * @author zzy
 */
@Controller
@RequestMapping("/cache")
public class AdListCacheAction {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource private AdListCache adListCache;
	@Resource private AdCrowdCache adCrowdCache;
	@Resource private AdAttentionCache adAttentionCache;
	@Resource private AdCustomCache adCustomCache;
	@Resource private AdLogQueueCache adLogQueueCache;
	@Resource private UserInfoCache userInfoCache;
	@Resource private AdLogService adLogService;
	@Resource private SqlExecService sqlExecService;
	

	@RequestMapping(value={"", "adlist"}, method=RequestMethod.GET)
	public String getAdListCacheAll(Model model){
		Collection<AdList> adListCacheList = adListCache.getAdListAll();
		model.addAttribute("adListCacheList", adListCacheList);
		return "adlistcache/list";
	}
	
	/**
	 * 投放条件年龄范围缓存管理
	 * 
	 */
	@RequestMapping(value="/agerange", method=RequestMethod.GET)
	public String getAgeRange(Model model){
		Map vmap = adCrowdCache.getAgeRangeAll();
		model.addAttribute("vmap", vmap);
		
		return "agerange/list";
	}
	
	/**
	 * 用户缓存管理
	 * 
	 */
	@RequestMapping(value="/userinfolist", method=RequestMethod.GET)
	public String getUserInfoListCacheAll(){
		return "usercache/list";
	}
	
	
	/**
	 * 用户关注广告
	 */
	@RequestMapping(value="/attentionlist", method=RequestMethod.GET)
	public String getAttentionListCacheAll(Model model){
		List<Object> lists  = adAttentionCache.getAdIdsAll();
		model.addAttribute("lists", lists);
		
		return "attentioncache/list";
	}
	
	
	/**
	 * 用户广告
	 */
	@RequestMapping(value="/uadlist", method=RequestMethod.GET)
	public String getUserAdListCacheAll(Model model){
		Map<String,String> vmap  = adCustomCache.getAdIdsAll();
		model.addAttribute("vmap", vmap);
		return "useradcache/list";
	}
	
	/**
	 * 广告-用户
	 */
	@RequestMapping(value="/adulist", method=RequestMethod.GET)
	public String getUserListCacheAllWithAdId(Model model){
		Map<String,String> vmap  = adCustomCache.getUidsAll();
		model.addAttribute("vmap", vmap);
		return "adusercache/list";
	}
	
	/**
	 * 用户广告
	 */
	@RequestMapping(value="/adlogqueue", method=RequestMethod.GET)
	public String getAdLogQueueCache(Model model){
		Collection<AdLog> adLogs = adLogQueueCache.getAdLogs(false);
		List<AdLog> adLogList = new ArrayList<AdLog>(adLogs);
		model.addAttribute("adLogList", adLogList);
		return "adlogqueuecache/list";
	}
	
	/**
	 * 用户信息详细
	 * @param adId 广告ID
	 */
	@RequestMapping(value="/userinfo/detail", method=RequestMethod.POST)
	public String userInfoDetail(@RequestParam String uId,Model model){
		if(StringUtils.isNotEmpty(uId)) {
			Map map = userInfoCache.getUser(Integer.parseInt(uId));
			model.addAttribute("userinfo", Objects.toString(map));
		}
		return "usercache/detail";
	}
	
	
	
	/**
	 * 生成单条广告缓存
	 * @param adId 广告ID
	 */
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public String create(@RequestParam String newAdId,Model model){
		if(StringUtils.isNotEmpty(newAdId)) {
			AdList adList = adListCache.saveAdList(Integer.parseInt(newAdId));
			if(null == adList) {
				model.addAttribute(newAdId, "该广告[newAdId]生成缓存失败");
				log.error("数据库不存在adList:{}", adList);
			}
		}
		return "redirect:/cache/adlist";
	}
	
	/**
	 * 生成单条用户关注广告缓存
	 * @param adId 广告ID
	 */
	@RequestMapping(value="/attention/create", method=RequestMethod.POST)
	public String createAttention(@RequestParam String uId,Model model){
		if(StringUtils.isNotEmpty(uId)) {
			Set<String> adIdSets = adAttentionCache.getAdIdsByUid(Integer.parseInt(uId));
			for (String adId : adIdSets) {
				adAttentionCache.addAttention(Integer.parseInt(uId),Integer.parseInt(adId));
				log.info("生成用户关注广告缓存[用户id:"+uId+",广告id:"+adId+"]成功");
			}
		}
		return "redirect:/cache/attentionlist";
	}
	
	
	/**
	 * 生成单条用户关注广告缓存
	 * @param adId 广告ID
	 */
	@RequestMapping(value="/custom/create", method=RequestMethod.POST)
	public String createCustom(@RequestParam int uId,Model model){
		Set<String> adIdSets = adCustomCache.getAdIdsByUid(uId);
		adCustomCache.setAdIdsByUid(uId, adIdSets);
		return "redirect:/cache/uadlist";
	}
	
	/**
	 * 生成单条广告日志缓存
	 * @param adId 广告ID
	 */
	@RequestMapping(value="/adlogqueue/create", method=RequestMethod.POST)
	public String createAdLogQueue(@RequestParam String adId,Model model){
		if(StringUtils.isNotEmpty(adId)) {
			AdLog adLog = adLogService.getAdLogById(Integer.parseInt(adId));
			Collection<AdLog> adLogs = new HashSet<AdLog>();
			adLogs.add(adLog);
			adLogQueueCache.setAdLogs(adLogs);
		}
		return "redirect:/cache/adlogqueue";
	}
	
	
	
	/**
	 * 删除广告缓存
	 */
	@RequestMapping(value="/del", method=RequestMethod.POST)
	public String del(@RequestParam Collection<Integer> adIds){
		for (Integer adId : adIds) {
			adListCache.delAdList(adId);
			log.info("删除缓存[广告id:"+adId+"]成功");
		}
		return "redirect:/cache/adlist";
	}
	
	/**
	 * 删除广告投放条件缓存
	 */
	@RequestMapping(value="/del-crowd", method=RequestMethod.POST)
	public String delCrowd(HttpServletRequest request,Model model){
		List<String> adIds = getAdIds(request);
		
		for (String adId : adIds) {
			if(StringUtils.isNotEmpty(adId)) {
				adCrowdCache.delCrowdByadId(Integer.parseInt(adId));
				
				log.info("删除广告投放条件缓存[广告id:"+adId+"]成功");
			}
		}
		return "redirect:/cache/adlist";
	}
	
	/**
	 * 判断广告是否投放
	 */
	@RequestMapping(value="/crowd/valid", method=RequestMethod.GET)
	public String isCrowd(HttpServletRequest request,Model model){
		String adId = request.getParameter("adId");
		List<String> crowdList = adCrowdCache.getAdIdsByCrowd(adId);
		model.addAttribute("crowdListStr", StringUtils.join(crowdList, ";"));
		
		return "crowdcache/list";
	}
	
	/**
	 * 重新生成广告缓存
	 */
	@RequestMapping(value="/renew", method=RequestMethod.POST)
	public String renew(@RequestParam Collection<Integer> adIds){
		for (Integer adId : adIds) {
			adListCache.saveAdList(adId);
			log.info("重新生成广告缓存[广告id:"+adId+"]成功");
		}
		
		return "redirect:/cache/adlist";
	}
	
	
	/**
	 * 重新生成用户关注广告缓存
	 */
	@RequestMapping(value="/renew-attention", method=RequestMethod.POST)
	public String renewAttention(HttpServletRequest request,Model model){
		List<String> uIds = getAdIds(request);
		for (String uId : uIds) {
			if(StringUtils.isNotEmpty(uId)) {
				Set<String> adIdSets = adAttentionCache.getAdIdsByUid(Integer.parseInt(uId));
				for (String adId : adIdSets) {
					adAttentionCache.addAttention(Integer.parseInt(uId),Integer.parseInt(adId));
					log.info("重新生成用户关注广告缓存[用户id:"+uId+",广告id:"+adId+"]成功");
				}
			}
		}
		
		return "redirect:/cache/attentionlist";
	}
	
	/**
	 * 重新生成用户关注广告缓存
	 */
	@RequestMapping(value="/renew-custom", method=RequestMethod.POST)
	public String renewCustom(HttpServletRequest request,Model model){
		List<String> uIds = getAdIds(request);
		for (String uId : uIds) {
			if(StringUtils.isNotEmpty(uId)) {
				Set<String> adIdSets = adCustomCache.getAdIdsByUid(Integer.parseInt(uId));
				adCustomCache.setAdIdsByUid(Integer.parseInt(uId), adIdSets);
			}
		}
		return "redirect:/cache/uadlist";
	}
	
	/**
	 * 重新生成广告日志队列缓存
	 */
	@RequestMapping(value="/renew-adlogqueue", method=RequestMethod.POST)
	public String renewAdLogQueue(HttpServletRequest request,Model model){
		List<String> adIds = getAdIds(request);
		Collection<AdLog> adLogColl = new HashSet<AdLog>();
		for (String adId : adIds) {
			if(StringUtils.isNotEmpty(adId)) {
				AdLog adLog = adLogService.getAdLogById(Integer.parseInt(adId));
				adLogColl.add(adLog);
			}
		}
		adLogQueueCache.setAdLogs(adLogColl);
		return "redirect:/cache/adlogqueue";
	}
	
	/**
	 * 重新生成投放条件广告缓存
	 */
	@RequestMapping(value="/renewcrowd", method=RequestMethod.POST)
	public String reNewCrowd(HttpServletRequest request,Model model){
		List<String> adIds = getAdIds(request);
		for (String adId : adIds) {
			if(StringUtils.isNotEmpty(adId)) {
				adCrowdCache.saveCrowdByadId(Integer.parseInt(adId));
				log.info("重新生成投放条件广告缓存[广告id:"+adId+"]成功");
			}
		}
		return "redirect:/cache/adlist";
	}
	
	/**
	 * 拷贝日志文件到应用服务器目录下
	 */
	@RequestMapping(value="/logs", method=RequestMethod.GET)
	public String copyLogs(@RequestParam(required = false) String date, Model model){
		if(StringUtils.isBlank(date)) {
			date = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		}
		String fileName = String.format("/logs/dksns_ad.%s.log", date);
		try {
			List<String> logsList = FileUtils.readLines(new File(fileName));
			model.addAttribute("logsList", logsList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "findlog4j/list";
	}
	
	/**
	 * 跳转到sql执行页面
	 */
	@RequestMapping(value="/showsql", method=RequestMethod.GET)
	public String sql(){
		return "sqlexec/list";
	}
	
	/**
	 * sql执行
	 */
	@RequestMapping(value="/sql", method=RequestMethod.POST)
	public String sql(@RequestParam(required = false) String sqlSelect, @RequestParam(required = false) String sqlUpdateOrInsert, Model model){
		Object result;
		if(StringUtils.isNotEmpty(sqlSelect)) {
			result = sqlExecService.execSelect(sqlSelect);
		}else {
			result = sqlExecService.execUpdateOrInsert(sqlUpdateOrInsert);
		}
		model.addAttribute("result", Objects.toString(result));
		return "sqlexec/list";
	}
	
	/**
	 * 手动触发定时器
	 */
	@RequestMapping(value="/timertask", method=RequestMethod.GET)
	public String income(){
		return "income/list";
	}
	
	/**
	 * 获取选中的广告缓存id列表
	 * 
	 * @param request
	 * @return
	 */
	private List<String> getAdIds(HttpServletRequest request) {
		String adIds = request.getParameter("adIds");
		if(StringUtils.isNotEmpty(adIds)) {
			adIds = adIds.substring(0,adIds.lastIndexOf(","));
			String[] ids = adIds.split(",");
			return Arrays.asList(ids);
		}else {
			return null;
		}
	}
	
}
