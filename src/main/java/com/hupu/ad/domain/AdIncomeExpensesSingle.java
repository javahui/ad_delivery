package com.hupu.ad.domain;



public class AdIncomeExpensesSingle extends AbstractPojo{
	private int id;
	private int adId;
	private int dkcode;
	private double money;
	private int dateline;
	
	private boolean isShow;
	private double assign;
	private double bid;
	
	public AdIncomeExpensesSingle(int adId, int dkcode, int dateline, double bid, boolean isShow, double assign){
		this.adId = adId;
		this.dkcode = dkcode;
		this.dateline = dateline;
		
		this.bid = bid;
		this.isShow = isShow;
		this.assign = assign;
	}
	public int getId() {return id;}
	public void setId(int id) {this.id = id;}
	
	public int getAdId() {return adId;}
	public void setAdId(int adId) {this.adId = adId;}
	
	public int getDkcode() {return dkcode;}
	public void setDkcode(int dkcode) {this.dkcode = dkcode;}
	
	public double getMoney() {return money;}
	public void setMoney(double money) {this.money = money;}
	
	public int getDateline() {return dateline;}
	public void setDateline(int dateline) {this.dateline = dateline;}
	
	/**
	 * 同一dkcore ,adId 在一批中的次数
	 */
	public void addMoney(){
		this.money = (this.money + assign * bid);
		if(isShow){
			this.money = this.money /1000;
		}
	}
	
}