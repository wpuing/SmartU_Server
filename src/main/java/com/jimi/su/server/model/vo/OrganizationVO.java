package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class OrganizationVO {

	private Integer id;
	
	private String group;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
	public static List<OrganizationVO> fillList(List<Record> records) {
		List<OrganizationVO> groupVOs = new ArrayList<OrganizationVO>();
		for (Record record : records) {
			OrganizationVO organization = new OrganizationVO();
			organization.setId(record.getInt("ID"));
			organization.setGroup(record.getStr("Group"));
			groupVOs.add(organization);
		}
		return groupVOs;
		
	}
	
}
