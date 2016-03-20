package com.hupu.ad.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Component;

import com.hupu.ad.dao.AdLogDao;
import com.hupu.ad.domain.AdLog;
/**
 * 广告的队列缓存
 * @author donghui
 */
@Component
public class AdLogQueueCache extends AbstractCache{
	/**
	 * 用户信息的KEY的前缀
	 */
	private static final String QUEUE_KEY = "q_list_" + adLogVersion;
	
	/**
	 * 流量统计的KEY的前缀
	 */
	private static final String STATISTICE_HASHKEY = "ads_ads_statistics";
	
	@Resource private AdLogDao adLogDao;
	/**
	 * 用线程池来写ad_log日志到队列中
	 */
	private static ExecutorService exec = Executors.newFixedThreadPool(100);
	
	/**
	 * 初始化流量数据
	 */
	@PostConstruct
	public void initStatistics(){
		Map countMap = adLogDao.findStatistics();
		adCache.delete(STATISTICE_HASHKEY);
		BoundHashOperations hashOps = adCache.boundHashOps(STATISTICE_HASHKEY);
		hashOps.increment("showCount", MapUtils.getIntValue(countMap, "showCount"));
		hashOps.increment("clickCount", MapUtils.getIntValue(countMap, "clickCount"));
		hashOps.increment("fansCount", MapUtils.getIntValue(countMap, "fansCount"));
	}

	/**
	 * 得到队列中所有日志记录
	 * @param isClearQueue 是否清空队列
	 */
	public Collection<AdLog> getAdLogs(boolean isClearQueue){
		//重写AdLog的equals hashCode方法,利用CopyOnWriteArraySet类删除重复记录,防止遍历集合时抛出ConcurrentModificationException
		Collection<AdLog> adLogs = new CopyOnWriteArraySet<AdLog>();
		List<AdLog> adLogList = logCache.boundListOps(QUEUE_KEY).range(0, -1);
		if(isClearQueue){
			logCache.delete(QUEUE_KEY);
		}
		for (AdLog adLog : adLogList) {
			log.debug("得到队列中日志:{}", adLog);
			adLogs.add(adLog);
		}
		return adLogs;
	}
	
	/**
	 * 保存广告
	 * @param adLists 广告集合
	 */
	public void setAdLogs(final Collection<AdLog> adLogs) {
		exec.execute(new Runnable(){
			@Override
			public void run() {
				for (AdLog adLog : adLogs) {//1 展示 2点击 3关注
					logCache.opsForList().leftPush(QUEUE_KEY, adLog);
					String key = "";
					switch (adLog.getEventType()) {
						case 1:
							key = "showCount";
							break;
						case 2:
							key = "clickCount";
							break;
						case 3:
							key = "fansCount";
							break;
					}
					adCache.boundHashOps(STATISTICE_HASHKEY).increment(key, 1);
					log.debug("写入队列:{}", adLog);
				}
				//累加流量统计
				
			}
		});
	}
	
}