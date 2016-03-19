package com.hupu.core.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.hupu.core.dto.BaseDTO;

public class Page<T> {

	// -- 分页参数 --//
	protected int pageNo = 1;
	protected int pageSize = 10;

	// -- 返回结果 --//
	protected List<T> result = new ArrayList();

	// -- 总记录数 --//
	protected long totalCount = 0;

	// -- 构造函数 --//
	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	public Page(BaseDTO baseDTO) {
		if (baseDTO != null) {
			if (baseDTO.getPageNo() != null && baseDTO.getPageNo() > 0) {
				this.pageNo = baseDTO.getPageNo();
			}
			if (baseDTO.getPageSize() != null && baseDTO.getPageSize() > 0) {
				this.pageSize = baseDTO.getPageSize();
			}
		}
	}

	// -- 分页参数访问函数 --//
	/**
	 * 获得当前页的页号,序号从1开始,默认为1.
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
	 */
	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	/**
	 * 返回Page对象自身的setPageNo函数,可用于连续设置。
	 */
	public Page<T> attrPageNo(final int thePageNo) {
		setPageNo(thePageNo);
		return this;
	}

	/**
	 * 获得每页的记录数量, 默认为15.
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的记录数量.
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 返回Page对象自身的setPageSize函数,可用于连续设置。
	 */
	public Page<T> attrPageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	/**
	 * 根据pageNo和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
	 * criteria.setFirstResult((pageNo-1)*pageSize);
	 */
	public int getFirst() {
		return (pageNo - 1) * pageSize;
	}


	// -- 访问查询结果函数 --//

	/**
	 * 获得页内的记录列表.
	 */
	public List<T> getResult() {
		return result;
	}

	/**
	 * 设置页内的记录列表.
	 */
	public void setResult(final List<T> result) {
		this.result = result;
	}

	/**
	 * 获得总记录数, 默认值为0.
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置总记录数.
	 */
	public void setTotalCount(final long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 根据pageSize与totalCount计算总页数, 默认值为0.
	 */
	public long getTotalPages() {
		return (long) Math.ceil((double) totalCount / (double) pageSize);
	}

	/**
	 * 是否还有下一页.
	 */
	public boolean isHasNext() {
		return (pageNo + 1 <= getTotalPages());
	}

	/**
	 * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
	 */
	public int getNextPage() {
		if (isHasNext()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}

	/**
	 * 是否还有上一页.
	 */
	public boolean isHasPre() {
		return (pageNo - 1 >= 1);
	}

	/**
	 * 当前页是否为首页.
	 */
	public boolean isHasFirst() {
		return pageNo > 1;
	}

	/**
	 * 是否有尾页.
	 */
	public boolean isHasLast() {
		return pageNo < getTotalPages();// getTotalPages() > 0 && pageNo !=
										// getTotalPages();
	}

	/**
	 * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
	 */
	public int getPrePage() {
		if (isHasPre()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("pageNo", pageNo).append("pageSize", pageSize)
				.append("totalCount", totalCount)
				.append("prePage", getPrePage())
				.append("nextPage", getNextPage())
				.append("totalPages", getTotalPages()).toString();
	}

	/**
	 * 计算以当前页为中心的页面列表,如"首页,23,24,25,26,27,末页"
	 * 
	 * @param count
	 *            需要计算的列表大小
	 * @return List<Long> pageNo列表
	 */
	public List<Long> getSlider(int count) {
		int halfSize = count / 2;
		long totalPage = getTotalPages();

		long startPageNumber = Math.max(pageNo - halfSize, 1);
		long endPageNumber = Math.min(startPageNumber + count - 1, totalPage);

		if (endPageNumber - startPageNumber < count) {
			startPageNumber = Math.max(endPageNumber - count, 1);
		}

		List<Long> result = new ArrayList();
		for (long i = startPageNumber; i <= endPageNumber; i++) {
			result.add(new Long(i));
		}
		return result;
	}

}
