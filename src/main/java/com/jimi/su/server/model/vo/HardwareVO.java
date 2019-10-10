package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class HardwareVO {

	private int id;
	
	private String hwVersion;

	
	public int getId() {
		return id;
	}

	
	public void setId(int id) {
		this.id = id;
	}
	
	
	public String getHwVersion() {
		return hwVersion;
	}


	
	public void setHwVersion(String hwVersion) {
		this.hwVersion = hwVersion;
	}


	public static List<HardwareVO> fillList(List<Record> records){
		List<HardwareVO> hardwardVOs = new ArrayList<HardwareVO>();
		for (Record record : records) {
			HardwareVO hardwareVO = new HardwareVO();
			hardwareVO.setId(record.getInt("Hardware_ID"));
			hardwareVO.setHwVersion(record.getStr("Hardware_HwVersion"));
			hardwardVOs.add(hardwareVO);
		}
		return hardwardVOs;
	}
}
