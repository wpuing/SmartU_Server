package com.jimi.su.server.service;

import java.util.List;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jimi.su.server.entity.SQL;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.model.Branch;
import com.jimi.su.server.model.Branchtype;
import com.jimi.su.server.model.Command;
import com.jimi.su.server.model.Project;
import com.jimi.su.server.model.Rule;
import com.jimi.su.server.model.vo.BranchTypeVO;
import com.jimi.su.server.model.vo.PageUtil;
import com.jimi.su.server.model.vo.ProjectVO;
import com.jimi.su.server.service.base.SelectService;
import com.jimi.su.server.util.ResultUtil;

/**
 * 项目管理逻辑层
 * 
 * @type ProjectService
 * @Company 几米物联技术有限公司-自动化部
 * @author 汤如杰
 * @date 2018年11月2日
 */
public class ProjectService extends SelectService {

	private static SelectService selectService = Enhancer.enhance(SelectService.class);


	public ResultUtil add(String projectName) {
		Project project = new Project();
		project = findProjectByName(projectName);
		if (project == null) {
			project = new Project().setProjectName(projectName.trim());
			project.save();
			new Rule().setAttribute("ProjectID").setFrom(project.getID()).setTo(project.getID()).save();
		} else {
			throw new OperationException("项目已存在，请直接引用");
		}
		return ResultUtil.succeed();
	}


	public ResultUtil delete(Integer id, Boolean isForced) {
		Project project = Project.dao.findById(id);
		if (project == null) {
			throw new ParameterException("删除失败，项目不存在");
		}
		if (!isForced) {
			Branchtype branch = Branchtype.dao.findFirst(SQL.SELECT_BRANCHTYPE_BY_PROJECT_ID, id);
			if (branch != null) {
				throw new ParameterException("删除失败，项目已被引用，选择“强制删除”，将该记录以及引用该记录的其他表记录一并删除");
			}
			Command command = Command.dao.findFirst(SQL.SELECT_COMMAND_BY_PROJECT_ID, id);
			if (command != null) {
				throw new ParameterException("删除失败，项目已被引用，选择“强制删除”，将该记录以及引用该记录的其他表记录一并删除");
			}
		}
		project.delete();
		return ResultUtil.succeed();
	}


	public ResultUtil update(Integer id, String projectName) {
		Project project = Project.dao.findById(id);
		if (project == null) {
			throw new ParameterException("更新失败，项目不存在");
		}
		Project temp = Project.dao.findFirst(SQL.SELECT_PROJECT_BY_NAME, projectName);
		if (temp != null && !temp.getID().equals(id)) {
			throw new OperationException("项目名已存在");
		}
		project.setProjectName(projectName.trim()).update();
		return ResultUtil.succeed();
	}


	public ResultUtil select(String filter, Integer currentPage, Integer pageSize) {
		Page<Record> pageRecord = selectService.select("project", currentPage, pageSize, "upper(ProjectName)", null, filter, null);
		List<ProjectVO> projectVOs = ProjectVO.fillList(pageRecord.getList());
		PageUtil<ProjectVO> pageUtil = new PageUtil<>();
		pageUtil.fill(pageRecord, projectVOs);
		return ResultUtil.succeed(pageUtil);
	}


	public ResultUtil getProject(String projectName) {
		String filter = "";
		if (projectName != null) {
			filter = "projectName #like#" + projectName;
		}
		Page<Record> pageRecord = selectService.select("project", null, null, null, null, filter, null);
		List<ProjectVO> projectVOs = ProjectVO.fillList(pageRecord.getList());
		PageUtil<ProjectVO> pageUtil = new PageUtil<>();
		pageUtil.fill(pageRecord, projectVOs);
		return ResultUtil.succeed(pageUtil);
	}


	public Boolean contianProject(String projectName) {
		Project project = Project.dao.findFirst(SQL.SELECT_PROJECT_BY_NAME, projectName);
		if (project != null) {
			return true;
		}
		return false;
	}


	public Project findProjectByName(String projectName) {
		Project project = Project.dao.findFirst(SQL.SELECT_PROJECT_BY_NAME, projectName);
		return project;
	}


	public ResultUtil selectBranchType(Boolean type, Integer projectId, Integer currentPage, Integer pageSize) {
		SqlPara sqlPara = new SqlPara();
		sqlPara.setSql(SQL.SELECT_BRANCHTYPE_BY_PROJECTDI_AND_TYPE);
		sqlPara.addPara(projectId);
		sqlPara.addPara(type);
		Page<Record> page = Db.paginate(currentPage, pageSize, sqlPara);
		List<BranchTypeVO> branchTypeVOs = BranchTypeVO.fillList(page.getList());
		PageUtil<BranchTypeVO> pageUtil = new PageUtil<>();
		pageUtil.fill(page, branchTypeVOs);
		return ResultUtil.succeed(pageUtil);
	}


	/**@author HCJ
	 * 更新分支
	 * @param id 源分支ID
	 * @param branchTypeName 分支名称
	 * @param position 目标分支ID
	 * @date 2019年6月19日 下午4:17:11
	 */
	public ResultUtil updateBranchType(Integer branchTypeId, String name, Integer position) {
		Branchtype branchtype = Branchtype.dao.findById(branchTypeId);
		if (branchtype == null) {
			throw new OperationException("定制信息不存在，请自行添加");
		}
		if (position == null) {
			if (name != null) {
				Branchtype branchtypeTemp = Branchtype.dao.findFirst(SQL.SELECT_BRANCHTYPE_BY_TYPE_AND_NAME_PROJECTID, branchtype.getType(), name, branchtype.getProjectID());
				if (branchtypeTemp != null && !branchtypeTemp.getID().equals(branchTypeId)) {
					throw new OperationException("定制信息已存在，无需修改");
				}
				branchtype.setName(name.trim()).update();
			}
		} else {
			if (Branchtype.dao.findById(position) == null) {
				throw new OperationException("目标定制信息不存在，请自行添加");
			}
			Db.update(SQL.UPDATE_BRANCHTYPE_POSITION, branchTypeId, position, position, branchTypeId);
		}
		return ResultUtil.succeed();
	}


	public ResultUtil deleteBranchType(Integer branchTypeId, Boolean isForced) {
		Branchtype branchtype = Branchtype.dao.findById(branchTypeId);
		if (branchtype == null) {
			throw new OperationException("定制信息不存在，请自行添加");
		}
		if (!isForced) {
			Branch branch = Branch.dao.findFirst(SQL.SELECT_BRANCH_BY_BRANCH_TYPE_ID, branchTypeId);
			if (branch != null) {
				throw new OperationException("删除失败，定制信息已被引用，选择“强制删除”，将该记录以及引用该记录的其他表记录一并删除");
			}
		}
		branchtype.delete();
		return ResultUtil.succeed();
	}


	public ResultUtil addBranchType(Integer projectId, Boolean type, String name) {
		Project project = Project.dao.findById(projectId);
		if (project == null) {
			throw new OperationException("项目不存在，请自行添加");
		}
		Branchtype branchtype = Branchtype.dao.findFirst(SQL.SELECT_BRANCHTYPE_BY_TYPE_AND_NAME_PROJECTID, type, name, projectId);
		if (branchtype != null) {
			throw new OperationException("定制信息已存在，请直接引用无需添加");
		}
		branchtype = new Branchtype().setType(type).setName(name.trim()).setProjectID(projectId);
		branchtype.save();
		branchtype.setPosition(branchtype.getID()).update();
		return ResultUtil.succeed();
	}
}
