package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;


public class CommandVO {

	private Integer startRange;
	private Integer branchId;
	private String branchName;
	private String titleIndex;
	private String command;
	private Integer readORWrite;
	private String commandName;
	private Integer reWrite;
	private String recvTitle;
	private Integer endRange;
	private Integer id;
	private String projectName;
	private Integer projectId;
	private Integer limitLength;


	public Integer getStartRange() {
		return startRange;
	}


	public void setStartRange(Integer startRange) {
		this.startRange = startRange;
	}


	public Integer getBranchId() {
		return branchId;
	}


	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}


	public String getBranchName() {
		return branchName;
	}


	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}


	public String getTitleIndex() {
		return titleIndex;
	}


	public void setTitleIndex(String titleIndex) {
		this.titleIndex = titleIndex;
	}


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}


	public Integer getReadORWrite() {
		return readORWrite;
	}


	public void setReadORWrite(Integer readORWrite) {
		this.readORWrite = readORWrite;
	}


	public String getCommandName() {
		return commandName;
	}


	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}


	public Integer getReWrite() {
		return reWrite;
	}


	public void setReWrite(Integer reWrite) {
		this.reWrite = reWrite;
	}


	public String getRecvTitle() {
		return recvTitle;
	}


	public void setRecvTitle(String recvTitle) {
		this.recvTitle = recvTitle;
	}


	public Integer getEndRange() {
		return endRange;
	}


	public void setEndRange(Integer endRange) {
		this.endRange = endRange;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public Integer getProjectId() {
		return projectId;
	}


	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}


	public Integer getLimitLength() {
		return limitLength;
	}


	public void setLimitLength(Integer limitLength) {
		this.limitLength = limitLength;
	}


	public static List<CommandVO> fillList(List<Record> records) {
		List<CommandVO> commandVOs = new ArrayList<CommandVO>();
		for (Record record : records) {
			CommandVO commandVO = new CommandVO();
			commandVO.setId(record.getInt("Command_ID"));
			commandVO.setProjectId(record.getInt("Command_ProjectID"));
			commandVO.setBranchName(record.getStr("Branch_BranchName"));
			commandVO.setProjectName(record.getStr("Project_ProjectName"));
			commandVO.setBranchId(record.getInt("Command_BranchID"));
			commandVO.setRecvTitle(record.getStr("Command_RecvTitle"));
			commandVO.setTitleIndex(record.getStr("Command_TitleIndex"));
			commandVO.setLimitLength(record.getInt("Command_LimitLength"));
			commandVO.setStartRange(record.getInt("Command_StartRange"));
			commandVO.setEndRange(record.getInt("Command_EndRange"));
			commandVO.setReadORWrite(record.getInt("Command_ReadORWrite"));
			commandVO.setReWrite(record.getInt("Command_ReWrite"));
			commandVO.setCommandName(record.getStr("Command_CommandName"));
			commandVO.setCommand(record.getStr("Command_Command"));
			commandVOs.add(commandVO);
		}
		return commandVOs;
	}
}
