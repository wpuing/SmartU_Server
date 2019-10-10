package com.jimi.su.server.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jimi.su.server.annotation.Log;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.service.OperationService;
import com.jimi.su.server.util.ResultUtil;


/**
 * 操作管理控制层
 * @type OperationController
 * @Company 几米物联技术有限公司-自动化部
 * @author 汤如杰
 * @date 2018年11月1日
 */
public class OperationController extends Controller {

	@Inject
	private OperationService operationService;


	/**
	 * 根据operationName 模糊查询操作记录
	 * @param operationName
	 */
	@Log("根据operationName模糊查询操作记录:{operationName}")
	public void getOperation(String operationName) {
		if (operationName == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = operationService.getOperation(operationName);
		renderJson(result);
	}


	public void selectOperation(String filter, Integer currentPage, Integer pageSize) {
		if (currentPage == null || pageSize == null) {
			throw new OperationException("参数不能为空");
		}
		if (currentPage <= 0 || pageSize <= 0) {
			throw new ParameterException("当前页码与每页行数均需要大于0");
		}
		ResultUtil result = operationService.select(filter, currentPage, pageSize);
		renderJson(result);
	}


	public void selectBind(String filter, Integer currentPage, Integer pageSize) {
		if (currentPage == null || pageSize == null) {
			throw new OperationException("参数不能为空");
		}
		if (currentPage <= 0 || pageSize <= 0) {
			throw new ParameterException("当前页码与每页行数均需要大于0");
		}
		ResultUtil result = operationService.selectBind(filter, currentPage, pageSize);
		renderJson(result);
	}

	@Log("要删除的约束id：{id}")
	public void deleteBind(Integer id) {
		if (id == null) {
			throw new OperationException("绑定关系ID不能为空");
		}
		ResultUtil result = operationService.deleteBind(id);
		renderJson(result);
	}


	@Log("更新操作：约束id：{id}，操作id：{operationId}，分支id：{branchId}")
	public void updateBind(Integer id, Integer operationId, Integer branchId) {
		if (id == null || operationId == null || branchId == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = operationService.updateBind(id, operationId, branchId);
		renderJson(result);
	}


	@Log("添加约束：操作id：{operationId}，分支id：{branchId}")
	public void addBind(Integer operationId, Integer branchId) {
		if (operationId == null || branchId == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = operationService.addBind(operationId, branchId);
		renderJson(result);
	}
}
