package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;


public class RuleVO {

	private Integer id;
	private String attribute;
	private String from;
	private String to;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getAttribute() {
		return attribute;
	}


	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}


	public String getFrom() {
		return from;
	}


	public void setFrom(String from) {
		this.from = from;
	}


	public String getTo() {
		return to;
	}


	public void setTo(String to) {
		this.to = to;
	}


	public static List<RuleVO> fillList(List<Record> records) {
		List<RuleVO> ruleVOs = new ArrayList<RuleVO>();
		for (Record record : records) {
			RuleVO ruleVO = new RuleVO();
			ruleVO.setId(record.getInt("Rule_Id"));
			ruleVO.setAttribute(record.getStr("Rule_Attribute"));
			ruleVO.setFrom(record.getStr("Rule_From"));
			ruleVO.setTo(record.getStr("Rule_To"));
			ruleVOs.add(ruleVO);
		}
		return ruleVOs;
	}
}
