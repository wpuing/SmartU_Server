package com.jimi.su.server.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseActionLog<M extends BaseActionLog<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Long id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.Long getId() {
		return getLong("id");
	}

	public M setTime(java.util.Date time) {
		set("time", time);
		return (M)this;
	}
	
	public java.util.Date getTime() {
		return get("time");
	}

	public M setAddress(java.lang.String address) {
		set("address", address);
		return (M)this;
	}
	
	public java.lang.String getAddress() {
		return getStr("address");
	}

	public M setUrl(java.lang.String url) {
		set("url", url);
		return (M)this;
	}
	
	public java.lang.String getUrl() {
		return getStr("url");
	}

	public M setParameters(java.lang.String parameters) {
		set("parameters", parameters);
		return (M)this;
	}
	
	public java.lang.String getParameters() {
		return getStr("parameters");
	}

	public M setResultCode(java.lang.Integer resultCode) {
		set("result_code", resultCode);
		return (M)this;
	}
	
	public java.lang.Integer getResultCode() {
		return getInt("result_code");
	}

	public M setUid(java.lang.String uid) {
		set("uid", uid);
		return (M)this;
	}
	
	public java.lang.String getUid() {
		return getStr("uid");
	}

	public M setConsumeTime(java.lang.Integer consumeTime) {
		set("consume_time", consumeTime);
		return (M)this;
	}
	
	public java.lang.Integer getConsumeTime() {
		return getInt("consume_time");
	}

	public M setAction(java.lang.String action) {
		set("action", action);
		return (M)this;
	}
	
	public java.lang.String getAction() {
		return getStr("action");
	}

}
