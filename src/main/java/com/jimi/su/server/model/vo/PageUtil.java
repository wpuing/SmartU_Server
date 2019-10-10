package com.jimi.su.server.model.vo;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;


public class PageUtil<T> {

	private Integer totalPage;
	private Integer totalData;
	private Integer currentPage;
	private Integer pageSize;
	private List<T> list;


	public Integer getCurrentPage() {
		return currentPage;
	}


	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public Integer getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(Integer totallyPage) {
		this.totalPage = totallyPage;
	}


	public Integer getTotalData() {
		return totalData;
	}


	public void setTotalData(Integer totallyData) {
		this.totalData = totallyData;
	}


	public List<T> getList() {
		return list;
	}


	public void setList(List<T> list) {
		this.list = list;
	}


	public void fill(Page<Record> pageRecord, List<T> list) {
		if (pageRecord !=null) {
			this.setCurrentPage(pageRecord.getPageNumber());
			this.setPageSize(pageRecord.getPageSize());
			this.setTotalData(pageRecord.getTotalRow());
			this.setTotalPage(pageRecord.getTotalPage());
		}
		if (list != null) {
			this.setList(list);
		}
	}
}
