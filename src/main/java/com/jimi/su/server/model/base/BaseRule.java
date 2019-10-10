package com.jimi.su.server.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseRule<M extends BaseRule<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setAttribute(java.lang.String attribute) {
		set("attribute", attribute);
		return (M)this;
	}
	
	public java.lang.String getAttribute() {
		return getStr("attribute");
	}

	public M setFrom(java.lang.Integer from) {
		set("from", from);
		return (M)this;
	}
	
	public java.lang.Integer getFrom() {
		return getInt("from");
	}

	public M setTo(java.lang.Integer to) {
		set("to", to);
		return (M)this;
	}
	
	public java.lang.Integer getTo() {
		return getInt("to");
	}

}
