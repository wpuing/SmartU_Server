package com.jimi.su.server.service;

import java.util.List;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.model.Branch;
import com.jimi.su.server.model.Operation;
import com.jimi.su.server.model.Operationversionbind;
import com.jimi.su.server.model.vo.OperationVO;
import com.jimi.su.server.model.vo.OperationVersionBindVO;
import com.jimi.su.server.model.vo.PageUtil;
import com.jimi.su.server.service.base.SelectService;
import com.jimi.su.server.util.ResultUtil;


/**
 * 操作管理逻辑层(包含操作记录查询以及操作版本绑定关系增删改查)
 * @type OperationService
 * @Company 几米物联技术有限公司-自动化部
 * @author 汤如杰
 * @date 2018年11月2日
 */
public class OperationService {

	private static SelectService selectService = Enhancer.enhance(SelectService.class);


	public ResultUtil select(String filter, Integer currentPage, Integer pageSize) {
		Page<Record> pageRecord = selectService.select("operation", currentPage, pageSize, null, null, filter, null);
		List<OperationVO> operationVOs = OperationVO.fillList(pageRecord.getList());
		PageUtil<OperationVO> pageUtil = new PageUtil<>();
		pageUtil.fill(pageRecord, operationVOs);
		return ResultUtil.succeed(pageUtil);
	}


	public ResultUtil getOperation(String operationName) {
		StringBuilder filter = new StringBuilder();
		if (operationName != null) {
			filter.append("operationName #like#" + operationName);
		}
		Page<Record> pageRecord = selectService.select("operation", null, null, null, null, filter.toString(), null);
		List<OperationVO> operationVOs = OperationVO.fillList(pageRecord.getList());
		PageUtil<OperationVO> pageUtil = new PageUtil<>();
		pageUtil.fill(pageRecord, operationVOs);
		return ResultUtil.succeed(pageUtil);
	}


	public ResultUtil addBind(Integer operationId, Integer branchId) {
		Operationversionbind bind = new Operationversionbind();
		Operation operation = Operation.dao.findById(operationId);
		if (operation == null) {
			throw new OperationException("添加失败，操作记录不存在");
		}
		Branch branch = Branch.dao.findById(branchId);
		if (branch == null) {
			throw new OperationException("添加失败，分支不存在");
		}
		bind.setOperationID(operationId).setBranchID(branchId).save();
		return ResultUtil.succeed();
	}


	public ResultUtil deleteBind(Integer id) {
		Operationversionbind bind = Operationversionbind.dao.findById(id);
		if (bind == null) {
			throw new OperationException("删除失败，绑定关系记录不存在");
		}
		bind.delete();
		return ResultUtil.succeed();
	}


	public ResultUtil selectBind(String filter, Integer currentPage, Integer pageSize) {
		String[] table = {"operationversionbind", "operation", "branch"};
		String[] refers = {"operationversionbind.BranchID = branch.ID", "operationversionbind.operationID = operation.ID"};
		Page<Record> pageRecord = selectService.select(table, refers, currentPage, pageSize, null, null, filter, null);
		List<OperationVersionBindVO> bindVOs = OperationVersionBindVO.fillList(pageRecord.getList());
		PageUtil<OperationVersionBindVO> pageUtil = new PageUtil<>();
		pageUtil.fill(pageRecord, bindVOs);
		return ResultUtil.succeed(pageUtil);
	}


	public ResultUtil updateBind(Integer id, Integer operationId, Integer branchId) {
		Operationversionbind bind = Operationversionbind.dao.findById(id);
		if (bind == null) {
			throw new OperationException("更新失败，绑定关系记录不存在");
		}
		if (!operationId.equals(bind.getOperationID())) {
			Operation operation = Operation.dao.findById(operationId);
			if (operation == null) {
				throw new OperationException("更新失败，操作记录不存在");
			}
			bind.setOperationID(operationId);
		}
		if (!branchId.equals(bind.getBranchID())) {
			Branch branch = Branch.dao.findById(branchId);
			if (branch == null) {
				throw new OperationException("更新失败，分支不存在");
			}
			bind.setBranchID(branchId);
		}
		bind.update();
		return ResultUtil.succeed();
	}
}
