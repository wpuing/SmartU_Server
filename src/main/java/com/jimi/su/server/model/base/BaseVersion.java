package com.jimi.su.server.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseVersion<M extends BaseVersion<M>> extends Model<M> implements IBean {

	public M setID(java.lang.Integer ID) {
		set("ID", ID);
		return (M)this;
	}
	
	public java.lang.Integer getID() {
		return getInt("ID");
	}

	public M setVersionName(java.lang.String VersionName) {
		set("VersionName", VersionName);
		return (M)this;
	}
	
	public java.lang.String getVersionName() {
		return getStr("VersionName");
	}

	public M setFileID(java.lang.Integer FileID) {
		set("FileID", FileID);
		return (M)this;
	}
	
	public java.lang.Integer getFileID() {
		return getInt("FileID");
	}

	public M setMainVersion(java.lang.Boolean MainVersion) {
		set("MainVersion", MainVersion);
		return (M)this;
	}
	
	public java.lang.Boolean getMainVersion() {
		return get("MainVersion");
	}

	public M setVersionNumber(java.lang.Integer VersionNumber) {
		set("VersionNumber", VersionNumber);
		return (M)this;
	}
	
	public java.lang.Integer getVersionNumber() {
		return getInt("VersionNumber");
	}

}
