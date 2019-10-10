package com.jimi.su.server.util;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

import com.jfinal.kit.PathKit;
import com.jimi.su.server.entity.Permission;

import cc.darhao.dautils.api.StringUtil;


public class CommonUtil {

	/**
	 * 拼接文件路径（web根目录/basePath/params1/params2...）
	 * @param basePath
	 * @param params
	 * @return
	 */
	public static String getFilePath(String basePath, String... params) {
		StringBuffer filePath = new StringBuffer();
		filePath.append(PathKit.getWebRootPath());
		filePath.append(File.separator);
		filePath.append(basePath);

		for (String param : params) {
			filePath.append(File.separator);
			filePath.append(param);
		}
		filePath.append(File.separator);
		return filePath.toString();

	}


	/**
	 * 拼接文件名，无后缀
	 * @param headName
	 * @param faceName
	 * @param firstCode
	 * @param secondCode
	 * @param debugCode
	 * @param suffixTime
	 * @return
	 */
	public static String getFileName(String headName, String faceName, String firstCode, String secondCode, String debugCode, String suffixTime) {
		String name = headName + "-" + faceName + "_" + firstCode + "." + secondCode + "." + debugCode + "_" + suffixTime;
		return name;
	}


	/**
	 * 根据路径和文件名(无文件后缀)查找文件，只返回一个文件
	 * @param filePath
	 * @param fileName
	 * @return
	 */
	public static File findFile(String filePath, final String fileName) {
		File dir = new File(filePath);
		File[] tempFile = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {

				String name = file.getName().substring(0, file.getName().lastIndexOf("."));
				if (name.equals(fileName)) {
					return true;
				}
				return false;
			}
		});
		if (tempFile == null || tempFile.length <= 0) {
			return null;
		}
		return tempFile[0];
	}


	/**
	 * 查询模块下所有文件
	 * @param filePath 模块路径
	 * @return
	 */
	public static File[] findFaceFiles(String filePath) {
		File dir = new File(filePath);
		File[] tempFile = dir.listFiles();
		if (tempFile == null || tempFile.length <= 0) {
			return null;
		}
		return tempFile;
	}


	/**
	 * 将字节列表直观转成某进制的无符号字符串
	 * @param bytes
	 * @param radix
	 * @return
	 */
	public static String parseBytesToXRadixString(List<Byte> bytes, int radix) {
		StringBuffer sb = new StringBuffer();
		for (Byte b : bytes) {
			switch (radix) {
			case 16:
				String hexString = Integer.toHexString(b).toUpperCase();
				hexString = StringUtil.fixLength(hexString, 2);
				sb.append(hexString);
				break;
			case 10:
				sb.append(Integer.toString(Byte.toUnsignedInt(b)));
				break;
			case 2:
				String binString = Integer.toBinaryString(b);
				binString = StringUtil.fixLength(binString, 8);
				sb.append(binString);
				break;
			default:
				break;
			}
			sb.append(" ");
		}
		return sb.toString().trim();
	}


	public static boolean rename(String path, String oldName, String newName) {
		if (!oldName.equals(newName)) {
			File oldFile = new File(path + oldName);
			File newFile = new File(path + newName);
			if (newFile.exists()) {
				return false;
			}
			oldFile.renameTo(newFile);
		}
		return true;
	}


	public static void appendAndConditions(StringBuilder filter) {
		if (!filter.toString().equals("")) {
			filter.append(" & ");
		}
	}


	public static String getPermissionInt(String permission) {
		StringBuffer permissionInt = new StringBuffer();
		for (Permission string : Permission.values()) {
			if (permission.indexOf(string.getString()) != -1) {
				if (permissionInt.indexOf(String.valueOf(string.getValue())) == -1) {
					permissionInt.append(string.getValue() + ",");
				}
			}
		}
		if (permissionInt != null && permissionInt.lastIndexOf(",") != -1) {
			permissionInt.delete(permissionInt.lastIndexOf(","), permissionInt.length());
		}
		return permissionInt.toString();
	}


	public static void main(String[] args) {
		/*
		 * System.out.println(getFilePath("BEAD", "SMT", "Display"));
		 * System.out.println(getFileName("SMT", "Display", "3", "1", "0",
		 * "201801021225"));
		 */

	}
}
