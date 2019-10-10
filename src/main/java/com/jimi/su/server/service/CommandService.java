package com.jimi.su.server.service;

import java.util.List;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.su.server.entity.SQL;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.model.Branch;
import com.jimi.su.server.model.Command;
import com.jimi.su.server.model.Project;
import com.jimi.su.server.model.vo.CommandVO;
import com.jimi.su.server.model.vo.PageUtil;
import com.jimi.su.server.service.base.SelectService;
import com.jimi.su.server.util.ResultUtil;


/**
 * 指令管理逻辑层
 * @type CommandService
 * @Company 几米物联技术有限公司-自动化部
 * @author 汤如杰
 * @date 2018年11月2日
 */
public class CommandService extends SelectService {

	private static SelectService selectService = Enhancer.enhance(SelectService.class);


	public ResultUtil add(String commandName, String command, String recvTitle, String titleIndex, Integer limitLength, Integer startRange, Integer endRange, Integer readOrWrite, Integer reWrite, Integer projectId, Integer branchId) {
		Command commandData = new Command();
		if (projectId != null) {
			Project project = Project.dao.findById(projectId);
			if (project == null) {
				throw new OperationException("添加失败，项目不存在");
			}
			commandData.setProjectID(projectId);
		}
		if (branchId != null) {
			Branch branch = Branch.dao.findById(branchId);
			if (branch == null) {
				throw new OperationException("添加失败，分支不存在");
			}
			commandData.setBranchID(branchId);
		}
		if (commandName != null) {
			commandData.setCommandName(commandName.trim());
		}
		if (command != null) {
			commandData.setCommand(command.trim());
		}
		if (recvTitle != null) {
			commandData.setRecvTitle(recvTitle.trim());
		}
		if (titleIndex != null) {
			commandData.setTitleIndex(titleIndex.trim());
		}
		if (limitLength != null) {
			commandData.setLimitLength(new Long(limitLength));
		}
		if (startRange != null) {
			commandData.setStartRange(startRange);
		}
		if (endRange != null) {
			commandData.setEndRange(endRange);
		}
		if (reWrite != null) {
			commandData.setReWrite(reWrite);
		}
		commandData.setReadORWrite(readOrWrite);
		commandData.save();
		return ResultUtil.succeed();
	}


	public ResultUtil update(Integer id, String commandName, String command, String recvTitle, String titleIndex, Integer limitLength, Integer startRange, Integer endRange, Integer readOrWrite, Integer reWrite, Integer projectId, Integer branchId) {
		Command commandData = Command.dao.findById(id);
		if (commandData == null) {
			throw new ParameterException("更新失败，命令不存在");
		}
		commandData.setProjectID(projectId);
		if (projectId != null) {
			Project project = Project.dao.findById(projectId);
			if (project == null) {
				throw new OperationException("更新失败，项目不存在");
			}
		}
		commandData.setBranchID(branchId);
		if (branchId != null) {
			Branch branch = Branch.dao.findById(branchId);
			if (branch == null) {
				throw new OperationException("更新失败，分支不存在");
			}
		}
		commandData.setCommandName(commandName.trim());
		commandData.setCommand(command.trim());
		commandData.setRecvTitle(recvTitle.trim());
		commandData.setTitleIndex(titleIndex.trim());
		commandData.setLimitLength(new Long(limitLength));
		commandData.setStartRange(startRange);
		commandData.setEndRange(endRange);
		commandData.setReWrite(reWrite);
		commandData.setReadORWrite(readOrWrite);
		commandData.update();
		return ResultUtil.succeed();
	}


	public ResultUtil delete(Integer id) {
		Command commandData = Command.dao.findById(id);
		if (commandData == null) {
			throw new ParameterException("删除失败，命令不存在");
		}
		commandData.delete();
		return ResultUtil.succeed();
	}


	public ResultUtil select(String filter, Integer currentPage, Integer pageSize) {
		Page<Record> pageRecord = selectService.selectByBaseSql(SQL.SELECT_COMMAND_DETAIL_PART1, SQL.SELECT_COMMAND_DETAIL_PART2, currentPage, pageSize, null, null, filter);
		List<CommandVO> commandVOs = CommandVO.fillList(pageRecord.getList());
		PageUtil<CommandVO> pageUtil = new PageUtil<CommandVO>();
		pageUtil.fill(pageRecord, commandVOs);
		return ResultUtil.succeed(pageUtil);
	}

}
