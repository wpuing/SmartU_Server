package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;


public class FileVO {

	private Integer id;
	private String fileName;
	private Integer branchId;
	private String path;
	private Integer fileSize;
	private Date packTime;
	private String updateLog;

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


	public Integer getBranchId() {
		return branchId;
	}


	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public Integer getFileSize() {
		return fileSize;
	}


	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}


	
	public Date getPackTime() {
		return packTime;
	}


	public void setPackTime(Date packTime) {
		this.packTime = packTime;
	}


	public String getUpdateLog() {
		return updateLog;
	}


	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}


	public static List<FileVO> fillList(List<Record> records) {
		List<FileVO> fileVOs = new ArrayList<FileVO>();
		for (Record record : records) {
			FileVO fileVO = new FileVO();
			fileVO.setId(record.getInt("File_ID"));
			fileVO.setBranchId(record.getInt("File_BranchID"));
			fileVO.setFileName(record.getStr("File_FileName"));
			fileVO.setFileSize(record.getInt("File_FileSize"));
			fileVO.setPath(record.getStr("File_Path"));
			fileVO.setPackTime(record.getDate("File_PackTime"));
			fileVO.setUpdateLog(record.getStr("File_UpdateLog"));
			fileVOs.add(fileVO);
		}
		return fileVOs;
	}
}
