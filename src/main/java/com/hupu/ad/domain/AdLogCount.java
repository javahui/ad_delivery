package com.hupu.ad.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AdLogCount extends AbstractPojo{

	private int id;
    private int adId;
    private int showCount;
    private int clickCount;
	private int fansCount;
    private int dateline;

    public AdLogCount(){}
    
	public int getId() {return id;}
	public void setId(int id) {this.id = id;}

	public int getAdId() {return adId;}
	public void setAdId(int adId) {this.adId = adId;}

	public int getShowCount() {return showCount;}
	public void setShowCount(int showCount) {this.showCount = showCount;}

	public int getClickCount() {return clickCount;}
	public void setClickCount(int clickCount) {this.clickCount = clickCount;}

    public int getFansCount() {return fansCount;}
	public void setFansCount(int fansCount) {this.fansCount = fansCount;}

	public int getDateline() {return dateline;}
	public void setDateline(int dateline) {this.dateline = dateline;}
	
	/**
	 * 根据事件类型累加
	 * @param eventType 事件类型
	 */
	public void incrementCount(int eventType){
		if(eventType == 1){
			this.showCount ++ ;
		}
		else if(eventType == 2){
			this.clickCount ++ ;
		}
		else if(eventType == 3){
			this.fansCount ++ ;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null){return false;}
		if(obj instanceof AdLogCount ){
			AdLogCount adLogCount = (AdLogCount)obj;
			return new EqualsBuilder()
				.append(this.adId, adLogCount.getAdId())
				.append(this.showCount, adLogCount.getShowCount())
				.append(this.clickCount, adLogCount.getClickCount())
				.append(this.fansCount, adLogCount.getFansCount())
				.append(this.dateline, adLogCount.getDateline())
				.isEquals();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
		.append(this.adId)
		.append(this.showCount)
		.append(this.clickCount)
		.append(this.fansCount)
		.append(this.dateline)
		.hashCode();
	}

}