package com.hupu.ad.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class AdPayLog implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer pId;

    private Integer cid;

    private Integer adId;

    private BigDecimal money;

    private String chargeMode;

    private Integer dateline;

	public Integer getpId() {
		return pId;
	}

	public void setpId(Integer pId) {
		this.pId = pId;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getAdId() {
		return adId;
	}

	public void setAdId(Integer adId) {
		this.adId = adId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getChargeMode() {
		return chargeMode;
	}

	public void setChargeMode(String chargeMode) {
		this.chargeMode = chargeMode;
	}

	public Integer getDateline() {
		return dateline;
	}

	public void setDateline(Integer dateline) {
		this.dateline = dateline;
	}
}
