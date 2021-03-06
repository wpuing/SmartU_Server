package com.jimi.su.server.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseDevice<M extends BaseDevice<M>> extends Model<M> implements IBean {

	public M setID(java.lang.Integer ID) {
		set("ID", ID);
		return (M)this;
	}
	
	public java.lang.Integer getID() {
		return getInt("ID");
	}

	public M setChipRID(java.lang.String ChipRID) {
		set("ChipRID", ChipRID);
		return (M)this;
	}
	
	public java.lang.String getChipRID() {
		return getStr("ChipRID");
	}

	public M setIMEI(java.lang.String IMEI) {
		set("IMEI", IMEI);
		return (M)this;
	}
	
	public java.lang.String getIMEI() {
		return getStr("IMEI");
	}

	public M setOldVersion(java.lang.String OldVersion) {
		set("OldVersion", OldVersion);
		return (M)this;
	}
	
	public java.lang.String getOldVersion() {
		return getStr("OldVersion");
	}

	public M setNewVersion(java.lang.String NewVersion) {
		set("NewVersion", NewVersion);
		return (M)this;
	}
	
	public java.lang.String getNewVersion() {
		return getStr("NewVersion");
	}

	public M setResult(java.lang.Integer Result) {
		set("Result", Result);
		return (M)this;
	}
	
	public java.lang.Integer getResult() {
		return getInt("Result");
	}

	public M setErrorCode(java.lang.Integer ErrorCode) {
		set("ErrorCode", ErrorCode);
		return (M)this;
	}
	
	public java.lang.Integer getErrorCode() {
		return getInt("ErrorCode");
	}

	public M setResultTime(java.util.Date ResultTime) {
		set("ResultTime", ResultTime);
		return (M)this;
	}
	
	public java.util.Date getResultTime() {
		return get("ResultTime");
	}

	public M setUpgradeLog(java.lang.String UpgradeLog) {
		set("UpgradeLog", UpgradeLog);
		return (M)this;
	}
	
	public java.lang.String getUpgradeLog() {
		return getStr("UpgradeLog");
	}

	public M setStatus(java.lang.String Status) {
		set("Status", Status);
		return (M)this;
	}
	
	public java.lang.String getStatus() {
		return getStr("Status");
	}

}
