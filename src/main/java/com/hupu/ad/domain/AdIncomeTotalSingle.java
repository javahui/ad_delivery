package com.hupu.ad.domain;

public class AdIncomeTotalSingle extends AbstractPojo{
	private int id;
	private int adId;
	private int dkcode;
	private double money;
	
	public AdIncomeTotalSingle(AdIncomeExpensesSingle adIncomeExpensesSingle){
		this.adId = adIncomeExpensesSingle.getAdId();
		this.dkcode = adIncomeExpensesSingle.getDkcode();
	}
	public int getId() {return id;}
	public void setId(int id) {this.id = id;}
	
	public int getAdId() {return adId;}
	public void setAdId(int adId) {this.adId = adId;}
	
	public int getDkcode() {return dkcode;}
	public void setDkcode(int dkcode) {this.dkcode = dkcode;}
	
	public double getMoney() {return money;}
	public void setMoney(double money) {this.money = money;}
	
	/**
	 * 同一dkcore ,adId 在一批中的次数
	 */
	public void addMoney(AdIncomeExpensesSingle adIncomeExpensesSingle){
		this.money += adIncomeExpensesSingle.getMoney();
	}
}