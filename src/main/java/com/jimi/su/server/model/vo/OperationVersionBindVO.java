package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;


public class OperationVersionBindVO {

	private Integer id;
	private Integer operationId;
	private String operationName;
	private Integer branchId;
	private String branchName;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getOperationName() {
		return operationName;
	}


	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}


	public Integer getOperationId() {
		return operationId;
	}


	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}


	public Integer getBranchId() {
		return branchId;
	}


	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}


	public String getBranchName() {
		return branchName;
	}


	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}


	public static List<OperationVersionBindVO> fillList(List<Record> records) {
		List<OperationVersionBindVO> bindVOs = new ArrayList<OperationVersionBindVO>();
		for (Record record : records) {
			OperationVersionBindVO bindVO = new OperationVersionBindVO();
			bindVO.setId(record.getInt("Operationversionbind_ID"));
			bindVO.setOperationName(record.getStr("Operation_OperationName"));
			bindVO.setOperationId(record.getInt("Operationversionbind_OperationID"));
			bindVO.setBranchId(record.getInt("Operationversionbind_BranchID"));
			bindVO.setBranchName(record.getStr("Branch_BranchName"));
			bindVOs.add(bindVO);
		}
		return bindVOs;
	}
}
