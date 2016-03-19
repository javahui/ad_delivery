package com.hupu.core.dto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public abstract class BaseDTO {

	// -- 公共变量 --//
	public static final String ASC = "asc";
	
	public static final String DESC = "desc";
	
	protected Integer pageNo = 1;

	protected Integer pageSize = 10;

	protected String orderBy = null;

	protected String order = null;
	
	protected int first = 0;


	/**
	 * 返回Page对象自身的setOrderBy函数,可用于连续设置。
	 */
	public BaseDTO attrOrderBy(final String theOrderBy) {
		setOrderBy(theOrderBy);
		return this;
	}
	

	/**
	 * 返回Page对象自身的setOrder函数,可用于连续设置。
	 */
	public BaseDTO attrOrder(final String theOrder) {
		setOrder(theOrder);
		return this;
	}
	
	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获得排序字段,无默认值. 多个排序字段时用','分隔.
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置排序字段,多个排序字段时用','分隔.
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 获得排序方向, 无默认值.
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * 设置排序方式向.
	 * 
	 * @param order
	 *            可选值为desc或asc,多个排序字段时用','分隔.
	 */
	public void setOrder(final String order) {
		String lowcaseOrder = StringUtils.lowerCase(order);

		// 检查order字符串的合法值
		String[] orders = StringUtils.split(lowcaseOrder, ',');
		for (String orderStr : orders) {
			if (!StringUtils.equals(DESC, orderStr)
					&& !StringUtils.equals(ASC, orderStr)) {
				throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
			}
		}
		this.order = lowcaseOrder;
	}
	
	public int getFirst() {
		return (pageNo - 1) * pageSize;
	}
	
	public void setFirst(int first) {
		this.first = first;
	}

	/**
	 * 是否已设置排序字段,无默认值
	 */
	public boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils
				.isNotBlank(order));
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("order", this.order).append("orderBy", this.orderBy)
				.append("pageNo", this.pageNo)
				.append("pageSize", this.pageSize).toString();
	}

}
