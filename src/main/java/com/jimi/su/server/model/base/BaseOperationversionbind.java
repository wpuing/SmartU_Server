package com.jimi.su.server.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseOperationversionbind<M extends BaseOperationversionbind<M>> extends Model<M> implements IBean {

	public M setID(java.lang.Integer ID) {
		set("ID", ID);
		return (M)this;
	}
	
	public java.lang.Integer getID() {
		return getInt("ID");
	}

	public M setBranchID(java.lang.Integer BranchID) {
		set("BranchID", BranchID);
		return (M)this;
	}
	
	public java.lang.Integer getBranchID() {
		return getInt("BranchID");
	}

	public M setOperationID(java.lang.Integer OperationID) {
		set("OperationID", OperationID);
		return (M)this;
	}
	
	public java.lang.Integer getOperationID() {
		return getInt("OperationID");
	}

}
