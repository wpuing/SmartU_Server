package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class BranchTypeVO {

	private Integer id;
	
	private Boolean type;
	
	private String branchTypeName;

	private Integer projectId;
	
	public Integer getId() {
		return id;
	}

	
	public void setId(Integer id) {
		this.id = id;
	}

	
	public Boolean getType() {
		return type;
	}

	
	public void setType(Boolean type) {
		this.type = type;
	}

	
	public String getBranchTypeName() {
		return branchTypeName;
	}

	
	public void setBranchTypeName(String branchTypeName) {
		this.branchTypeName = branchTypeName;
	}
	
	
	public Integer getProjectId() {
		return projectId;
	}


	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}


	public static List<BranchTypeVO> fillList(List<Record> records){
		List<BranchTypeVO> branchTypeVOs = new ArrayList<BranchTypeVO>();
		for (Record record : records) {
			BranchTypeVO branchTypeVO = new BranchTypeVO();
			branchTypeVO.setId(record.getInt("ID"));
			branchTypeVO.setType(record.getBoolean("Type"));
			branchTypeVO.setBranchTypeName(record.getStr("Name"));
			branchTypeVO.setProjectId(record.getInt("ProjectID"));
			branchTypeVOs.add(branchTypeVO);
		}
		return branchTypeVOs;
	}
}
