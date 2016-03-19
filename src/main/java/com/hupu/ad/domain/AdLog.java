package com.hupu.ad.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AdLog extends AbstractPojo{
	public AdLog (){}
	
	private int id;
	private int cid;
	private int adId;
	private int eventType;
	private String url;
	private String ip;
	private int dateline;
	private int isUser;
	private int dkcode;
	private int actionDkcode;
	
	public int getId() {return id;}
	public void setId(int id) {this.id = id;}
	
	public int getCid() {return cid;}
	public void setCid(int cid) {this.cid = cid;}
	
	public int getAdId() {return adId;}
	public void setAdId(int adId) {this.adId = adId;}
	
	public int getEventType() {return eventType;}
	public void setEventType(int eventType) {this.eventType = eventType;}
	
	public String getUrl() {return url;}
	public void setUrl(String url) {this.url = url;}
	
	public String getIp() {return ip;}
	public void setIp(String ip) {this.ip = ip;}
	
	public int getDateline() {return dateline;}
	public void setDateline(int dateline) {this.dateline = dateline;}
	
	public int getIsUser() {return isUser;}
	public void setIsUser(int isUser) {this.isUser = isUser;}
	
	public int getDkcode() {return dkcode;}
	public void setDkcode(int dkcode) {this.dkcode = dkcode;}
	
	public int getActionDkcode() {return actionDkcode;}
	public void setActionDkcode(int actionDkcode) {this.actionDkcode = actionDkcode;}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null){return false;}
		if(obj instanceof AdLog ){
			AdLog adLog = (AdLog)obj;
			return new EqualsBuilder()
				.append(this.cid, adLog.getCid())
				.append(this.adId, adLog.getAdId())
				.append(this.eventType, adLog.getEventType())
				.append(this.url, adLog.getUrl())
				.append(this.isUser, adLog.getIsUser())
				.append(this.dkcode, adLog.getDkcode())
				.append(this.actionDkcode, adLog.getActionDkcode())
				.append(this.ip, adLog.getIp())
				.isEquals();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
		.append(this.cid)
		.append(this.adId)
		.append(this.eventType)
		.append(this.url)
		.append(this.isUser)
		.append(this.dkcode)
		.append(this.actionDkcode)
		.append(this.ip)
		.hashCode();
	}
	
}
