package com.jimi.su.server.interceptor;

import java.io.File;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.upload.UploadFile;


public class FileDeleteInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {

		inv.invoke();
		// 判断请求的内容类型是否为"multipart/form-data"
		if (inv.getController().getRequest().getContentType().contains("multipart/form-data")) {
			// 获取文件
			UploadFile uploadFile = inv.getController().getFile();
			File file = uploadFile.getFile();
			if (file != null && file.exists()) {
				file.delete();
			}
		}

	}

}
