package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;


public class OperationVO {

	private Integer id;
	private String operationName;


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


	public static List<OperationVO> fillList(List<Record> records) {
		List<OperationVO> operationVOs = new ArrayList<OperationVO>();
		for (Record record : records) {
			OperationVO operationVO = new OperationVO();
			operationVO.setId(record.getInt("Operation_ID"));
			operationVO.setOperationName(record.getStr("Operation_OperationName"));
			operationVOs.add(operationVO);
		}
		return operationVOs;
	}
}
