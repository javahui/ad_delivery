package com.hupu.ad.cache;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Component;

/**
 * 广告投放信息缓存操作类
 * @author donghui
 */
@Component
public class UserInfoCache extends AbstractCache{
	
	/**
	 * 用户信息的KEY的前缀
	 */
	private final String USER_KEY_PREFIX = "user:";
	
	/**
	 * 在缓存查找user用户信息
	 * @param uid 用户id
	 */
	public Map getUser(int uid) {
		String key = USER_KEY_PREFIX + uid;
		Map user = userCache.boundHashOps(key).entries();
		if (MapUtils.isEmpty(user)) {
			log.debug("缓存未命中用户uid:{}", uid);
			return null;
		}
		return user;
	}
	
}