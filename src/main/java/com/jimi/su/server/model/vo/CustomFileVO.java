package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;

public class CustomFileVO {

	private Integer id;
	
	private String fileName;
	
	private String packTime;
	
	private String branchName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getPackTime() {
		return packTime;
	}

	public void setPackTime(String packTime) {
		this.packTime = packTime;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	
	public static List<CustomFileVO> fillList(List<Record> records) {
		List<CustomFileVO> customFileVOs = new ArrayList<CustomFileVO>();
		for (Record record : records) {
			CustomFileVO customFileVO = new CustomFileVO();
			customFileVO.setId(record.getInt("ID"));
			customFileVO.setFileName(record.getStr("FileName"));
			customFileVO.setPackTime(record.getStr("PackTime"));
			customFileVO.setBranchName(record.getStr("BranchName"));
			customFileVOs.add(customFileVO);
		}
		return customFileVOs;
	}
}
