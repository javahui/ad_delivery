package com.hupu.ad.service;

import java.util.Collection;

import javax.annotation.Resource;

import org.bouncycastle.util.encoders.UrlBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hupu.ad.domain.AdList;

/**
 * 专用广告和个人广告调用
 * @author donghui
 */
@Component
public class AdInvokeService {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource private AdLogService adLogService;
	@Resource private AdListGetService adListGetService;
	
	private static final int EVENT_TYPE_SHOW = 1;
	private static final int EVENT_TYPE_CLICK = 2;
	
	/**
	 * 取web页面的广告
	 */
	public String getWebAd(String json){
		JSONObject jsonObj = JSON.parseObject(json);
		int uid = jsonObj.getIntValue("uid");
		int webId = jsonObj.getIntValue("webId");
		int num = jsonObj.getIntValue("num");
		int dkcode = jsonObj.getIntValue("dkcode");
		int actionDkcode = 0;
		String interests = jsonObj.getString("interests");
		String url = jsonObj.getString("url");
		String ip = jsonObj.getString("ip");
		
		Collection<AdList> adListSet = adListGetService.getWebAd(uid, interests, num, webId);
		adLogService.writeAdLog(EVENT_TYPE_SHOW, url, ip, dkcode, actionDkcode, adListSet);
		return this.formatPhpJson(adListSet);
	}
	
	/**
	 * 取用户web页面的广告
	 */
	public String getUserWebAd(String json){
		JSONObject jsonObj = JSON.parseObject(json);
		int uid = jsonObj.getIntValue("uid");
		int num = jsonObj.getIntValue("num");
		int dkcode = jsonObj.getIntValue("dkcode");
		int actionDkcode = jsonObj.getIntValue("actionDkcode");
		String url = jsonObj.getString("url");
		String ip = jsonObj.getString("ip");
		
		Collection<AdList> adLists = adListGetService.getAdListByUid(uid, actionDkcode,num);
		adLogService.writeAdLog(EVENT_TYPE_SHOW, url, ip, dkcode, actionDkcode, adLists);
		return this.formatPhpJson(adLists);
	}
	
	/**
	 * 广告点击跳转
	 */
	public String link(String json){
		JSONObject jsonObj = JSON.parseObject(json);
		String url = jsonObj.getString("url");
		String ip = jsonObj.getString("ip");
		String encodeUrl = jsonObj.getString("encodeUrl");
		int dkcode = jsonObj.getIntValue("dkcode");
		int actionDkcode = jsonObj.getIntValue("actionDkcode");
		int adId = jsonObj.getIntValue("adId");
		adLogService.writeAdLog(EVENT_TYPE_CLICK, url, ip, dkcode, actionDkcode, adId);
		return new String(UrlBase64.decode(encodeUrl));
	}
		
	
	/**
	 * 格式化成php页面能处理的JSON格式
	 */
	private String formatPhpJson(Collection<AdList> adListSet){
		for (AdList adList : adListSet) {
			//站内广告URL不加密
			if(adList.getClassify() == 1){
				continue;
			}
			byte[] bytes = UrlBase64.encode(adList.getUrl().getBytes());
			String encodeUrl = new String(bytes);
			adList.setUrl(encodeUrl);
		}
		return JSON.toJSONString(adListSet);

	}
}
