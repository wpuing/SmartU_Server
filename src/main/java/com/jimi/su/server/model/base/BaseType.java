package com.jimi.su.server.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseType<M extends BaseType<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setTypeName(java.lang.String typeName) {
		set("typeName", typeName);
		return (M)this;
	}
	
	public java.lang.String getTypeName() {
		return getStr("typeName");
	}

	public M setPermission(java.lang.String permission) {
		set("permission", permission);
		return (M)this;
	}
	
	public java.lang.String getPermission() {
		return getStr("permission");
	}

	public M setPcPermission(java.lang.String pcPermission) {
		set("pcPermission", pcPermission);
		return (M)this;
	}
	
	public java.lang.String getPcPermission() {
		return getStr("pcPermission");
	}

}