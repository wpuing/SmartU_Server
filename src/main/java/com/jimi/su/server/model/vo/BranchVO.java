package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;


public class BranchVO {

	private Integer id;
	private Integer projectId;
	private String branchName;
	private String projectName;
	private String domainName;
	private String branchVersion;
	private String lock;
	private String paramVersion;
	private String patchString;
	private Boolean patch;
	private String compositeString;
	private Boolean composite;
	private String remarks;
	private String pcbVersion;
	private String hwVersion;
	private String gsensorVersion;
	private String ftName;
	private String domesticName;
	private String pfVersion;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getProjectId() {
		return projectId;
	}


	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}


	public String getBranchName() {
		return branchName;
	}


	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}


	public String getDomainName() {
		return domainName;
	}


	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}


	public String getBranchVersion() {
		return branchVersion;
	}


	public void setBranchVersion(String branchVersion) {
		this.branchVersion = branchVersion;
	}


	public String getLock() {
		return lock;
	}


	public void setLock(String lock) {
		this.lock = lock;
	}


	public String getParamVersion() {
		return paramVersion;
	}


	public void setParamVersion(String paramVersion) {
		this.paramVersion = paramVersion;
	}


	public String getPatchString() {
		return patchString;
	}


	public void setPatchString(String patchString) {
		this.patchString = patchString;
	}


	public String getCompositeString() {
		return compositeString;
	}


	public void setCompositeString(String compositeString) {
		this.compositeString = compositeString;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}


	public String getPcbVersion() {
		return pcbVersion;
	}


	public void setPcbVersion(String pcbVersion) {
		this.pcbVersion = pcbVersion;
	}


	public String getHwVersion() {
		return hwVersion;
	}


	public void setHwVersion(String hwVersion) {
		this.hwVersion = hwVersion;
	}


	public String getGsensorVersion() {
		return gsensorVersion;
	}


	public void setGsensorVersion(String gsensorVersion) {
		this.gsensorVersion = gsensorVersion;
	}


	public String getFtName() {
		return ftName;
	}


	public void setFtName(String ftName) {
		this.ftName = ftName;
	}


	public String getDomesticName() {
		return domesticName;
	}


	public void setDomesticName(String domesticName) {
		this.domesticName = domesticName;
	}


	public String getPfVersion() {
		return pfVersion;
	}


	public void setPfVersion(String pfVersion) {
		this.pfVersion = pfVersion;
	}


	public Boolean getPatch() {
		return patch;
	}


	public void setPatch(Boolean patch) {
		this.patch = patch;
	}


	public Boolean getComposite() {
		return composite;
	}


	public void setComposite(Boolean composite) {
		this.composite = composite;
	}


	public static List<BranchVO> fillList(List<Record> records) {
		List<BranchVO> branchVOs = new ArrayList<BranchVO>();
		for (Record record : records) {
			BranchVO branchVO = new BranchVO();
			branchVO.setId(record.getInt("Branch_ID"));
			branchVO.setProjectId(record.getInt("Branch_ProjectID"));
			branchVO.setBranchName(record.getStr("Branch_BranchName"));
			branchVO.setProjectName(record.getStr("Project_ProjectName"));
			branchVO.setBranchVersion(record.getStr("Branch_BranchVersion"));
			branchVO.setDomainName(record.getStr("Branch_DomainName"));
			branchVO.setPcbVersion(record.getStr("Pcb_PcbVersion"));
			branchVO.setHwVersion(record.getStr("Hardware_HwVersion"));
			branchVO.setGsensorVersion(record.getStr("Gsensor_GsensorVersion"));
			branchVO.setPfVersion(record.getStr("Platform_PfVersion"));
			branchVO.setFtName(record.getStr("Foregintrade_FtName"));
			branchVO.setDomesticName(record.getStr("Domestic_DomesticName"));
			branchVO.setLock(record.getStr("Branch_Lock"));
			branchVO.setParamVersion(record.getStr("Branch_ParamVersion"));
			branchVO.setRemarks(record.getStr("Branch_Remarks"));
			Boolean patch = record.getBoolean("Branch_Patch");
			if (patch == null || !patch) {
				branchVO.setPatch(false);
				branchVO.setPatchString("否");
			} else {
				branchVO.setPatch(true);
				branchVO.setPatchString("是");
			}
			Boolean composite = record.getBoolean("Branch_Composite");
			if (composite == null || !composite) {
				branchVO.setComposite(false);
				branchVO.setCompositeString("否");
			} else {
				branchVO.setComposite(true);
				branchVO.setCompositeString("是");
			}
			branchVOs.add(branchVO);
		}
		return branchVOs;
	}
}
