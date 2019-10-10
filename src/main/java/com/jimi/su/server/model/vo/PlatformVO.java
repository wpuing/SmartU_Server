package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class PlatformVO {

	private int id;
	
	private String pfVersion;

	
	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}
	
	
	public String getPfVersion() {
		return pfVersion;
	}


	public void setPfVersion(String pfVersion) {
		this.pfVersion = pfVersion;
	}


	public static List<PlatformVO> fillList(List<Record> records){
		List<PlatformVO> platformVOs = new ArrayList<PlatformVO>();
		for (Record record : records) {
			PlatformVO platformVO = new PlatformVO();
			platformVO.setId(record.getInt("Platform_ID"));
			platformVO.setPfVersion(record.getStr("Platform_PfVersion"));
			platformVOs.add(platformVO);
		}
		return platformVOs;
	}
}
