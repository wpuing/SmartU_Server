package com.jimi.su.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 文件工具类
 * @author 几米物联自动化部-韦姚忠
 * @date   2019年7月16日
 */
public class FileUtil {
	
	
	/**
	 * 提取检验码，判断校验码
	 * @param path
	 * @param code
	 * @return
	 */
	public static boolean checkMD5(String path,String code) {
		code = code.toUpperCase();
		StringBuffer sb = extractMD5(path);
		if (sb!=null) {
			if (code.trim().equals(sb.toString().trim())) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * 提取蛮MD5值
	 * @param path 文件路径
	 * @return
	 */
	private static StringBuffer extractMD5(String path) {
		StringBuffer sb = new StringBuffer();
		FileInputStream fis = null;
		try {
			File file = new File(path);
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            BigInteger bigInt = new BigInteger(1, md.digest());
            sb.append(bigInt.toString(16).toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			if (fis!=null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb;
	}

}
