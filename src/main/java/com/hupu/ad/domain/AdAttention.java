package com.hupu.ad.domain;

import java.util.List;


public class AdAttention  {
	private int adId;
	private int uid;
	private int isAttentioned;
	private int dateline;
	private List<String> adIds;
	
	public int getAdId() {return adId;}
	public void setAdId(int adId) {this.adId = adId;}
	
	public int getUid() {return uid;}
	public void setUid(int uid) {this.uid = uid;}
	
	public int getIsAttentioned() {return isAttentioned;}
	public void setIsAttentioned(int isAttentioned) {this.isAttentioned = isAttentioned;}
	
	public int getDateline() {return dateline;}
	public void setDateline(int dateline) {this.dateline = dateline;}
	
	public List<String> getAdIds() {return adIds;}
	public void setAdIds(List<String> adIds) {this.adIds = adIds;}
	
}
