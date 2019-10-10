package com.jimi.su.server.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jimi.su.server.annotation.Log;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.service.CommandService;
import com.jimi.su.server.util.ResultUtil;


/**
 * 命令管理控制层
 * @type CommandController
 * @Company 几米物联技术有限公司-自动化部
 * @author 汤如杰
 * @date 2018年11月1日
 */
public class CommandController extends Controller {

	@Inject
	private CommandService commandService;


	@Log("添加命令，信息为：{commandName}，{command}，{recvTitle}，{titleIndex}，{limitLength}，{startRange}，{endRange}，{readORWrite}，{reWrite}，{projectId}，{branchId}")
	public void add(String commandName, String command, String recvTitle, String titleIndex, Integer limitLength, Integer startRange, Integer endRange, Integer readORWrite, Integer reWrite, Integer projectId, Integer branchId) {
		if (readORWrite == null) {
			throw new OperationException("读写类型不能为空");
		}
		if ((startRange == null && endRange != null) || (startRange != null && endRange == null)) {
			throw new OperationException("参数起始范围和参数结束范围仅可同时为空或同时不为空");
		}
		if (startRange != null && startRange >= endRange) {
			throw new OperationException("参数起始范围必须小于参数结束范围");
		}
		ResultUtil result = commandService.add(commandName, command, recvTitle, titleIndex, limitLength, startRange, endRange, readORWrite, reWrite, projectId, branchId);
		renderJson(result);
	}


	@Log("要删除的命令的ID：{id}")
	public void delete(Integer id) {
		if (id == null) {
			throw new OperationException("命令ID不能为空");
		}
		ResultUtil result = commandService.delete(id);
		renderJson(result);
	}


	public void select(String filter, Integer currentPage, Integer pageSize) {
		if (currentPage == null || pageSize == null) {
			throw new OperationException("参数不能为空");
		}
		if (currentPage <= 0 || pageSize <= 0) {
			throw new ParameterException("当前页码与每页行数均需要大于0");
		}
		ResultUtil result = commandService.select(filter, currentPage, pageSize);
		renderJson(result);
	}


	@Log("更新命令，信息为：{id}，{commandName}，{command}，{recvTitle}，{titleIndex}，{limitLength}，{startRange}，{endRange}，{readORWrite}，{reWrite}，{projectId}，{branchId}")
	public void update(Integer id, String commandName, String command, String recvTitle, String titleIndex, Integer limitLength, Integer startRange, Integer endRange, Integer readORWrite, Integer reWrite, Integer projectId, Integer branchId) {
		if (id == null) {
			throw new OperationException("参数不能为空");
		}
		if (readORWrite == null) {
			throw new OperationException("读写类型不能为空");
		}
		if ((startRange == null && endRange != null) || (startRange != null && endRange == null)) {
			throw new OperationException("参数起始范围和参数结束范围仅可同时为空或同时不为空");
		}
		if (startRange != null && startRange >= endRange) {
			throw new OperationException("参数起始范围必须小于参数结束范围");
		}
		ResultUtil result = commandService.update(id, commandName, command, recvTitle, titleIndex, limitLength, startRange, endRange, readORWrite, reWrite, projectId, branchId);
		renderJson(result);
	}
}
