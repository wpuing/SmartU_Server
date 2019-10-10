package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class MailVO {
	
	private Integer id;
	
	private String emilAddress;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmilAddress() {
		return emilAddress;
	}

	public void setEmilAddress(String emilAddress) {
		this.emilAddress = emilAddress;
	}
	
	public static List<MailVO> fillList(List<Record> records) {
		List<MailVO> mailVOs = new ArrayList<MailVO>();
		for (Record record : records) {
			MailVO mailVO = new MailVO();
			mailVO.setId(record.getInt("ID"));
			mailVO.setEmilAddress(record.getStr("EMailAddress"));
			mailVOs.add(mailVO);
			
		}
		return mailVOs;
		
	}
}
