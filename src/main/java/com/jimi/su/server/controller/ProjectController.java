package com.jimi.su.server.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jimi.su.server.annotation.Log;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.service.ProjectService;
import com.jimi.su.server.util.ResultUtil;

/**
 * 项目管理控制层
 * 
 * @type ProjectController
 * @Company 几米物联技术有限公司-自动化部
 * @author 汤如杰
 * @date 2018年11月1日
 */
public class ProjectController extends Controller {

	@Inject
	ProjectService projectService;


	@Log("添加项目，要添加的项目是：{projectName}")
	public void add(String projectName) {
		if (projectName == null) {
			throw new OperationException("项目名不能为空");
		}
		ResultUtil result = projectService.add(projectName);
		renderJson(result);
	}


	@Log("要删除额项目的id是：{id}，是否强制；{isForced}")
	public void delete(Integer id, Boolean isForced) {
		if (id == null || isForced == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = projectService.delete(id, isForced);
		renderJson(result);

	}


	@Log("更新项目，要更新的项目id是：{id}，要更新的项目名是：{projectName}")
	public void update(Integer id, String projectName) {
		if (id == null || projectName == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = projectService.update(id, projectName);
		renderJson(result);
	}


	public void select(String filter, Integer currentPage, Integer pageSize) {
		if (currentPage == null || pageSize == null) {
			throw new OperationException("参数不能为空");
		}
		if (currentPage <= 0 || pageSize <= 0) {
			throw new ParameterException("当前页码与每页行数均需要大于0");
		}
		ResultUtil result = projectService.select(filter, currentPage, pageSize);
		renderJson(result);
	}


	/**
	 * 根据projectName模糊查询项目信息
	 * @param projectName
	 */
	public void getProject(String projectName) {
		if (projectName == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = projectService.getProject(projectName);
		renderJson(result);
	}


	public void selectBranchType(Boolean type, Integer projectId, Integer currentPage, Integer pageSize) {
		if (type == null || projectId == null || currentPage == null || pageSize == null) {
			throw new OperationException("参数不能为空");
		}
		if (currentPage <= 0 || pageSize <= 0) {
			throw new ParameterException("当前页码与每页行数均需要大于0");
		}
		ResultUtil result = projectService.selectBranchType(type, projectId, currentPage, pageSize);
		renderJson(result);
	}


	/**@author HCJ
	 * 更新分支
	 * @param id 源分支ID
	 * @param branchTypeName 分支名称
	 * @param position 目标分支ID
	 * @date 2019年6月19日 下午4:17:11
	 */
	@Log("更新分支，源分支ID：{id},分支名称: {branchTypeName},目标分支ID:{position}")
	public void updateBranchType(Integer id, String branchTypeName, Integer position) {
		if (id == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = projectService.updateBranchType(id, branchTypeName, position);
		renderJson(result);
	}


	@Log("要删除的分支类型id：{id}，是否强制：{isForced}")
	public void deleteBranchType(Integer id, Boolean isForced) {
		if (id == null || isForced == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = projectService.deleteBranchType(id, isForced);
		renderJson(result);
	}


	@Log("要添加的分支类型：项目id：{projectId}，类型：{type}，分支类型名：{branchTypeName}")
	public void addBranchType(Integer projectId, Boolean type, String branchTypeName) {
		if (type == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = projectService.addBranchType(projectId, type, branchTypeName);
		renderJson(result);
	}
}
