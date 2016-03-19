package com.hupu.ad.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AdLogPersonalCount extends AbstractPojo{

	private int id;
    private int adId;
    private int showCount;
    private int clickCount;
	private int fansCount;
	private int dkcode;

    public AdLogPersonalCount(){}
    
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

	public int getDkcode() {return dkcode;}
	public void setDkcode(int dkcode) {this.dkcode = dkcode;}
	
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
		if(obj instanceof AdLogPersonalCount ){
			AdLogPersonalCount adLogPersonalCount = (AdLogPersonalCount)obj;
			return new EqualsBuilder()
				.append(this.adId, adLogPersonalCount.getAdId())
				.append(this.dkcode, adLogPersonalCount.getDkcode())
				.append(this.showCount, adLogPersonalCount.getShowCount())
				.append(this.clickCount, adLogPersonalCount.getClickCount())
				.append(this.fansCount, adLogPersonalCount.getFansCount())
				.isEquals();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(this.adId)
			.append(this.dkcode)
			.append(this.showCount)
			.append(this.clickCount)
			.append(this.fansCount)
			.hashCode();
	}
	
}