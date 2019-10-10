package com.jimi.su.server.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.service.ActionLogService;
import com.jimi.su.server.util.ResultUtil;

/**
 * 显示用户操作记录控制层
 * 
 * @author 几米物联自动化部-韦姚忠
 * @date 2019年7月18日
 */
public class ActionLogController extends Controller {

	@Inject
	private ActionLogService actionLogService;

//	public void select(String filter, Integer currentPage, Integer pageSize) {
//		if (currentPage == null || pageSize == null) {
//			throw new OperationException("参数不能为空");
//		}
//		if (currentPage <= 0 || pageSize <= 0) {
//			throw new ParameterException("当前页码与每页行数均需要大于0");
//		}
//		ResultUtil result = actionLogService.select(filter, currentPage, pageSize);
//		renderJson(result);
//	}
	
	
	public void select(Integer currentPage, Integer pageSize) {
		if (currentPage == null || pageSize == null) {
			throw new OperationException("参数不能为空");
		}
		if (currentPage <= 0 || pageSize <= 0) {
			throw new ParameterException("当前页码与每页行数均需要大于0");
		}
		ResultUtil result = actionLogService.select(currentPage, pageSize);
		renderJson(result);
	}
}
