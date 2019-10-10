package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class GsensorVO {

	private int id;
	
	private String gsensorVersion;

	
	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}

	
	public String getGsensorVersion() {
		return gsensorVersion;
	}


	public void setGsensorVersion(String gsensorVersion) {
		this.gsensorVersion = gsensorVersion;
	}


	public static List<GsensorVO> fillList(List<Record> records){
		List<GsensorVO> gsensorVOs = new ArrayList<GsensorVO>();
		for (Record record : records) {
			GsensorVO gsensorVO = new GsensorVO();
			gsensorVO.setId(record.getInt("Gsensor_ID"));
			gsensorVO.setGsensorVersion(record.getStr("Gsensor_GsensorVersion"));
			gsensorVOs.add(gsensorVO);
		}
		return gsensorVOs;
	}
}
