package com.hupu.ad.domain;

import java.math.BigDecimal;

public class AdCost {
	private Integer id;
	private Integer adId;
	private Integer cid;
	private BigDecimal budget = BigDecimal.ZERO;;
	private Boolean budgetSort;
	private BigDecimal bid;
	private String chargeMode;
	private BigDecimal costMoney = BigDecimal.ZERO;

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
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

	public BigDecimal getBudget() {
		return budget;
	}

	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	public Boolean getBudgetSort() {
		return budgetSort;
	}

	public void setBudgetSort(Boolean budgetSort) {
		this.budgetSort = budgetSort;
	}

	public BigDecimal getBid() {
		return bid;
	}

	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	public String getChargeMode() {
		return chargeMode;
	}

	public void setChargeMode(String chargeMode) {
		this.chargeMode = chargeMode;
	}

	public BigDecimal getCostMoney() {
		return costMoney;
	}

	public void setCostMoney(BigDecimal costMoney) {
		this.costMoney = costMoney;
	}

}