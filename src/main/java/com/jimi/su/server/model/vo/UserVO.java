package com.jimi.su.server.model.vo;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.plugin.activerecord.Record;
import com.jimi.su.server.util.CommonUtil;


public class UserVO {

	private Integer id;
	private Integer typeId;
	private String name;
	private String enableString;
	private Boolean enable;
	private String typeName;
	private String permission;
	private String pcPermission;
	private String password;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getTypeId() {
		return typeId;
	}


	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEnableString() {
		return enableString;
	}


	public void setEnableString(String enableString) {
		this.enableString = enableString;
	}


	public Boolean getEnable() {
		return enable;
	}


	public void setEnable(Boolean enable) {
		this.enable = enable;
	}


	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	public String getPermission() {
		return permission;
	}


	public void setPermission(String permission) {
		this.permission = permission;
	}


	public String getPcPermission() {
		return pcPermission;
	}


	public void setPcPermission(String pcPermission) {
		this.pcPermission = pcPermission;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public static List<UserVO> fillList(List<Record> records) {
		List<UserVO> userVOs = new ArrayList<UserVO>();
		for (Record record : records) {
			UserVO userVO = new UserVO();
			userVO.setId(record.getInt("User_Id"));
			userVO.setName(record.getStr("User_Name"));
			userVO.setTypeId(record.getInt("User_TypeId"));
			userVO.setTypeName(record.getStr("Type_TypeName"));
			userVO.setEnable(record.getBoolean("User_Enable"));
			if (userVO.getEnable()) {
				userVO.setEnableString("是");
			} else {
				userVO.setEnableString("否");
			}
			String permission = record.getStr("Type_Permission");
			if (permission != null && !permission.trim().equals("")) {
				String permissionInt = CommonUtil.getPermissionInt(permission);
				userVO.setPermission(permissionInt);
			}
			userVO.setPcPermission(record.getStr("Type_PcPermission"));
			userVOs.add(userVO);
		}
		return userVOs;
	}


	public static UserVO fill(List<Record> records) {
		UserVO userVO = null;
		if (records.size() > 0) {
			Record record = records.get(0);
			if (record == null) {
				return null;
			}
			userVO = new UserVO();
			userVO.setId(record.getInt("User_Id"));
			userVO.setName(record.getStr("User_Name"));
			userVO.setTypeId(record.getInt("User_TypeId"));
			userVO.setTypeName(record.getStr("Type_TypeName"));
			userVO.setEnable(record.getBoolean("User_Enable"));
			if (userVO.getEnable()) {
				userVO.setEnableString("是");
			} else {
				userVO.setEnableString("否");
			}
			userVO.setPassword(record.getStr("User_Password"));
			String permission = record.getStr("Type_Permission");
			if (permission != null && !permission.trim().equals("")) {
				String permissionInt = CommonUtil.getPermissionInt(permission);
				userVO.setPermission(permissionInt);
			}
			userVO.setPcPermission(record.getStr("Type_PcPermission"));
		}
		return userVO;
	}

}
