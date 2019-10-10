package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class OrderVO {
	
	private Integer id;

	private String orderName;
	
	private String userName;
	
	private String branchName;
	
	private Integer scalableNum;
	
	private Integer upgradedNum;
	
	private Integer userId;
	
	private Integer branchId;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Integer getScalableNum() {
		return scalableNum;
	}

	public void setScalableNum(Integer scalableNum) {
		this.scalableNum = scalableNum;
	}

	public Integer getUpgradedNum() {
		return upgradedNum;
	}

	public void setUpgradedNum(Integer upgradedNum) {
		this.upgradedNum = upgradedNum;
	}
	
	public static List<OrderVO> fillList(List<Record> records) {
		List<OrderVO> orderVOs = new ArrayList<OrderVO>();
		for (Record record : records) {
			OrderVO orderVO = new OrderVO();
			orderVO.setId(record.getInt("id"));
			orderVO.setOrderName(record.getStr("orderName"));
			orderVO.setUserName(record.getStr("userName"));
			orderVO.setBranchName(record.getStr("branchName"));
			orderVO.setScalableNum(record.getInt("scalableNum"));
			orderVO.setUpgradedNum(record.getInt("upgradedNum"));
			orderVO.setUserId(record.getInt("userId"));
			orderVO.setBranchId(record.getInt("branchId"));
			orderVOs.add(orderVO);
		}
		return orderVOs;
	}
}
