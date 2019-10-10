package com.jimi.su.server.entity;

import java.util.Date;


public class VersionInfo {

	private String projectName;

	private String pcbVersion;

	private String hwVersion;

	private String version;

	private String gsensorVersion;

	private String domainName;

	private String paramVersion;

	private String remarks;

	private String ftName;

	private String domesticName;

	private String lock;

	private Boolean patch;

	private Date packTime;


	public String getProjectName() {
		return projectName;
	}


	public void setProjectName(String projectName) {
		this.projectName = projectName;
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


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public String getGsensorVersion() {
		return gsensorVersion;
	}


	public void setGsensorVersion(String gsensorVersion) {
		this.gsensorVersion = gsensorVersion;
	}


	public String getDomainName() {
		return domainName;
	}


	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}


	public String getParamVersion() {
		return paramVersion;
	}


	public void setParamVersion(String paramVersion) {
		this.paramVersion = paramVersion;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
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


	public String getLock() {
		return lock;
	}


	public void setLock(String lock) {
		this.lock = lock;
	}


	public Boolean getPatch() {
		return patch;
	}


	public void setPatch(Boolean patch) {
		this.patch = patch;
	}


	public Date getPackTime() {
		return packTime;
	}


	public void setPackTime(Date packTime) {
		this.packTime = packTime;
	}
}
