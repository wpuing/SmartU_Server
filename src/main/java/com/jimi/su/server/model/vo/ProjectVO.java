package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;


public class ProjectVO {

	private Integer id;
	private String projectName;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public static List<ProjectVO> fillList(List<Record> records) {
		List<ProjectVO> projectVOs = new ArrayList<ProjectVO>();
		for (Record record : records) {
			ProjectVO projectVO = new ProjectVO();
			projectVO.setId(record.getInt("Project_ID"));
			projectVO.setProjectName(record.getStr("Project_ProjectName"));
			projectVOs.add(projectVO);
		}
		return projectVOs;
	}
}
