package com.hupu.ad.domain;

import java.io.Serializable;

public class AdConfig implements Serializable {
	private static final long serialVersionUID = -1633907972149252324L;
	private String datavalue;
	private String var;
	public String getDatavalue() {
		return datavalue;
	}
	public void setDatavalue(String datavalue) {
		this.datavalue = datavalue;
	}
	public String getVar() {
		return var;
	}
	public void setVar(String var) {
		this.var = var;
	}
}
