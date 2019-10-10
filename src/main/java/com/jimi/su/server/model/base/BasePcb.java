package com.jimi.su.server.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BasePcb<M extends BasePcb<M>> extends Model<M> implements IBean {

	public M setID(java.lang.Integer ID) {
		set("ID", ID);
		return (M)this;
	}
	
	public java.lang.Integer getID() {
		return getInt("ID");
	}

	public M setPcbVersion(java.lang.String PcbVersion) {
		set("PcbVersion", PcbVersion);
		return (M)this;
	}
	
	public java.lang.String getPcbVersion() {
		return getStr("PcbVersion");
	}

}
