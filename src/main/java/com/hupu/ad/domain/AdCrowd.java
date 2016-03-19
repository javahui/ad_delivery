package com.hupu.ad.domain;


public class AdCrowd extends AbstractPojo{
	private int id;
	private String region;
	private String ageRange;
	private String gender;
	private String interest;
	private int adId;
	private String regionRank;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getAgeRange() {
		return ageRange;
	}
	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public int getAdId() {
		return adId;
	}
	public void setAdId(int adId) {
		this.adId = adId;
	}
	public String getRegionRank() {
		return regionRank;
	}
	public void setRegionRank(String regionRank) {
		this.regionRank = regionRank;
	}
}