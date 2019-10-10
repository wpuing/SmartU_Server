package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class SimpleUserVO {

	private Integer id;

	private String userName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static List<SimpleUserVO> fillList(List<Record> records) {
		List<SimpleUserVO> simpleUserVOs = new ArrayList<SimpleUserVO>();
		for (Record record : records) {
			SimpleUserVO simpleUserVO = new SimpleUserVO();
			simpleUserVO.setId(record.getInt("User_Id"));
			simpleUserVO.setUserName(record.getStr("User_Name"));
			simpleUserVOs.add(simpleUserVO);
		}
		return simpleUserVOs;
	}
}
