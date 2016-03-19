package com.hupu.ad.action;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.util.encoders.UrlBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hupu.ad.domain.AdList;
import com.hupu.ad.service.AdListGetService;
import com.hupu.ad.service.AdLogService;

/**
 * 获取广告action
 * @author donghui
 */
@Controller
@RequestMapping(method=RequestMethod.GET, produces="text/plain;charset=UTF-8")
public class AdListGetAction {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource private AdLogService adLogService;
	@Resource private AdListGetService adListGetService;
	
	private static final int EVENT_TYPE_SHOW = 1;
	private static final int EVENT_TYPE_CLICK = 2;
	
	/**
	 * 取web页面的广告
	 */
	@RequestMapping("getWebAd") 
	@ResponseBody
	public String getWebAd(HttpServletRequest request, HttpServletResponse response, @RequestParam int webId,
		@RequestParam int uid, @RequestParam String interests, @RequestParam int num, @RequestParam(required = false) String callback){
		Collection<AdList> adListSet = adListGetService.getWebAd(uid, interests, num, webId);
		String url = request.getRequestURL().toString();
		String ip = request.getRemoteAddr();
	//	adLogService.writeAdLog(EVENT_TYPE_SHOW, url, ip, 0, adListSet);
		return this.formatPhpJson(adListSet, callback);
	}
	
	/**
	 * 取用户web页面的广告
	 */
	@RequestMapping("getUserWebAd")
	@ResponseBody
	public String getUserWebAd(HttpServletRequest request, HttpServletResponse response,
		@RequestParam int uid,@RequestParam int actionDkcode, @RequestParam int num, @RequestParam(required = false) String callback){
		Collection<AdList> adLists = adListGetService.getAdListByUid(uid, actionDkcode,num);
		String url = request.getRequestURL().toString();
		String ip = request.getRemoteAddr();
		//adLogService.writeAdLog(EVENT_TYPE_SHOW, url, ip, actionDkcode, adLists);
		return this.formatPhpJson(adLists, callback);
	}
	
	/**
	 * 广告点击跳转
	 */
	@RequestMapping("link")
	@ResponseBody
	public void link(HttpServletRequest request, HttpServletResponse response, 
		@RequestParam String encodeUrl, @RequestParam int dkcode, @RequestParam int adId){
		String url = request.getRequestURL().toString();
		String ip = request.getRemoteAddr();
		//adLogService.writeAdLog(EVENT_TYPE_CLICK, url, ip, dkcode, adId);
		String decodeUrl = new String(UrlBase64.decode(encodeUrl));
		try {
			response.sendRedirect(decodeUrl);
		}  catch (IOException e) {
			log.error("action:[AdListGetAction] method:[dispather] IOException error",e);
		}
	}
	
	/**
	 * 格式化成php页面能处理的JSON格式
	 */
	private String formatPhpJson(Collection<AdList> adListSet, String callback){
		Map map = new LinkedHashMap();
		int status = 1;
		String info = "";
		if(CollectionUtils.isEmpty(adListSet)){
			status = 0;
			info = "error";
		}
		else{
			for (AdList adList : adListSet) {
				//站内广告URL不加密
				if(adList.getClassify() == 1){
					continue;
				}
				byte[] bytes = UrlBase64.encode(adList.getUrl().getBytes());
				String encodeUrl = new String(bytes);
				adList.setUrl(encodeUrl);
			}
		}
		map.put("status", status);
		map.put("info", info);
		map.put("data", adListSet);
		
		return callback + "(" + JSON.toJSONString(map) +")";
	}
}
