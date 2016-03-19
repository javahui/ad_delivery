package com.hupu.ad.domain;

import java.math.BigDecimal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 竞价等级规则
 * @author zzy
 *
 */
public class BidLevel {
	private Integer id;
	private Integer adId;
	private BigDecimal bid;
	private String level; //A:10 0~1 B:20 1~100 C:30 100~500 D:40 500以上
	private Integer ratio; 
	private Integer count;
	
	
	public BidLevel() {}
	public BidLevel(Integer adId,Double bid,String level,Integer ratio) {
		this.adId = adId;
		this.bid = new BigDecimal(bid);
		this.level = level;
		this.ratio = ratio;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAdId() {
		return adId;
	}
	public void setAdId(Integer adId) {
		this.adId = adId;
	}
	public BigDecimal getBid() {
		return bid;
	}
	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Integer getRatio() {
		return ratio;
	}
	public void setRatio(Integer ratio) {
		this.ratio = ratio;
	} 
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this,ToStringStyle.SHORT_PREFIX_STYLE);
	}
	
}
