package com.jimi.su.server.entity;

import java.util.ArrayList;
import java.util.List;


public enum Permission {
	
	VERSION_SELECT("/project/select,/project/selectBranchType,/branch/select,/file/select", 1),
	VERSION_CONFIG("/project/add,/project/delete,/project/update,/project/getProject,/project/updateBranchType,/project/deleteBranchType,/project/addBranchType,/branch/add,/branch/delete,/branch/update,/file/upload,/file/delete,/file/update,/file/getFileData,/branch/getGsensor,/branch/getHardware,/branch/getPcb,/branch/getPlatform,/file/parserVersion", 2),
	RULE_SELECT("/rule/select", 3),
	RULE_CONFIG("/rule/add,/rule/delete,/rule/update,/project/getProject,/branch/getGsensor,/branch/getHardware,/branch/getPcb,/branch/getPlatform", 4),
	COMMAND_SELECT("/command/select", 5),
	COMMAND_CONFIG("/command/add,/command/delete,/command/update,/project/getProject,/branch/getBranch", 6),
	OPERATION_SELECT("/operation/selectBind,/operation/selectOperation", 7),
	OPERATION_CONFIG("/operation/addBind,/operation/deleteBind,/operation/updateBind,/operation/getOperation,/branch/getBranch", 8),
	ORDER_SELECT("/order/select", 9),
	ORDER_CONFIG("/order/add,/order/delete,/order/update,/user/getUser,/branch/getBranch", 10),
	FILE_DOWNLOAD("/file/download",11);

	String string;
	Integer key;


	Permission(String string, Integer key) {
		this.string = string;
		this.key = key;
	}

	public String getString() {
		return this.string;
	}
	
	
	public Integer getValue() {
		return this.key;
	}

	public static Integer getValue(String string) {
		for (Permission permission : values()) {
			if (permission.string.equals(string)) {
				return permission.key;
			}
		}
		return null;
	}


	public static String getString(Integer key) {
		for (Permission permission : values()) {
			if (permission.key.equals(key)) {
				return permission.string;
			}
		}
		return "";
	}


	public static List<String> getSring(Integer key) {
		List<String> strings = new ArrayList<String>();
		for (Permission permission : values()) {
			if (permission.key.equals(key)) {
				strings.add(permission.string);
			}
		}
		return strings;
	}

}
