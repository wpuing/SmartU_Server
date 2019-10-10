package com.jimi.su.server.controller;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jimi.su.server.annotation.Log;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.service.BranchService;
import com.jimi.su.server.util.ResultUtil;


/**
 * 分支管理控制层
 * @type BranchController
 * @Company 几米物联技术有限公司-自动化部
 * @author 汤如杰
 * @date 2018年11月1日
 */
public class BranchController extends Controller {

	@Inject
	private BranchService branchService;

	
	@Log("要删除分支的ID是：{id},是否强制删除：{isForced}")
	public void delete(Integer id, Boolean isForced) {
		if (id == null || isForced == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = branchService.delete(id, isForced);
		renderJson(result);
	}


	public void select(String filter, Integer currentPage, Integer pageSize) {
		if (currentPage == null || pageSize == null) {
			throw new OperationException("参数不能为空");
		}
		if (currentPage <= 0 || pageSize <= 0) {
			throw new OperationException("当前页码与每页行数均需要大于0");
		}
		ResultUtil result = branchService.select(filter, currentPage, pageSize);
		renderJson(result);
	}


	@Log("更新分支信息：{id}，{branchName}，{pcbVersion}，{hwVersion}，{branchVersion}，{gsensorVersion}，{domainName}，{paramVersion}，{lock}，{pfVersion}，{patch}，{composite}，{remarks}")
	public void update(Integer id, String branchName, String pcbVersion, String hwVersion, String branchVersion, String gsensorVersion, String domainName, String paramVersion, String lock, String pfVersion, Boolean patch, Boolean composite, String remarks) {
		if (id == null || branchName == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = branchService.update(id, branchName, pcbVersion, hwVersion, branchVersion, gsensorVersion, domainName, paramVersion,  lock, pfVersion, patch, composite, remarks);
		renderJson(result);
	}


	/**
	 * 根据branchName模糊查询分支信息
	 * @param branchName
	 */
	@Log("根据branchName模糊查询分支信息模糊查询分支信息：{branchName}")
	public void getBranch(String branchName) {
		if (branchName == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = branchService.getBranch(branchName);
		renderJson(result);
	}


	public void getPcb(String pcbVersion) {
		if (pcbVersion == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = ResultUtil.succeed(branchService.getPcb(pcbVersion));
		renderJson(result);
	}


	public void getHardware(String hwVersion) {
		if (hwVersion == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = ResultUtil.succeed(branchService.getHardware(hwVersion));
		renderJson(result);
	}


	public void getGsensor(String gsensorVersion) {
		if (gsensorVersion == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = ResultUtil.succeed(branchService.getGsensor(gsensorVersion));
		renderJson(result);
	}


	public void getPlatform(String pfVersion) {
		if (pfVersion == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = ResultUtil.succeed(branchService.getPlatform(pfVersion));
		renderJson(result);
	}

}
