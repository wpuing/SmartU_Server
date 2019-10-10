package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class PcbVO {

	private int id;
	
	private String pcbVersion;

	
	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}

	
	public String getPcbVersion() {
		return pcbVersion;
	}

	
	public void setPcbVersion(String pcbVersion) {
		this.pcbVersion = pcbVersion;
	}
	
	
	public static List<PcbVO> fillList(List<Record> records){
		List<PcbVO> pcbVOs = new ArrayList<PcbVO>();
		for (Record record : records) {
			PcbVO pcbVO = new PcbVO();
			pcbVO.setId(record.getInt("Pcb_ID"));
			pcbVO.setPcbVersion(record.getStr("Pcb_PcbVersion"));
			pcbVOs.add(pcbVO);
		}
		return pcbVOs;
	}
}
