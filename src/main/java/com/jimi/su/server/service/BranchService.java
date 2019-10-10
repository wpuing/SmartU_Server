package com.jimi.su.server.service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.StrKit;
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
import com.jimi.su.server.model.File;
import com.jimi.su.server.model.Gsensor;
import com.jimi.su.server.model.Hardware;
import com.jimi.su.server.model.Operationversionbind;
import com.jimi.su.server.model.Orders;
import com.jimi.su.server.model.Pcb;
import com.jimi.su.server.model.Platform;
import com.jimi.su.server.model.Rule;
import com.jimi.su.server.model.vo.BranchVO;
import com.jimi.su.server.model.vo.GsensorVO;
import com.jimi.su.server.model.vo.HardwareVO;
import com.jimi.su.server.model.vo.PageUtil;
import com.jimi.su.server.model.vo.PcbVO;
import com.jimi.su.server.model.vo.PlatformVO;
import com.jimi.su.server.service.base.SelectService;
import com.jimi.su.server.util.ResultUtil;


/**
 * 版本管理逻辑层
 * @type BranchService
 * @Company 几米物联技术有限公司-自动化部
 * @author 汤如杰
 * @date 2018年11月2日
 */
public class BranchService {

	private static SelectService selectService = Enhancer.enhance(SelectService.class);


	/**@author HCJ
	 * 更新添加版本记录
	 * @param branchNamesforQuery 用于查询的版本名称
	 * @param branchTypeId 分支ID
	 * @param pcbVersion PCB版本
	 * @param hwVersion 硬件版本
	 * @param version 子版本号
	 * @param gsensorVersion GSensor版本
	 * @param domainName 动态域名
	 * @param paramVersion 参数版本
	 * @param lock 锁域
	 * @param pfVersion 平台版本
	 * @param patch 是否补丁
	 * @param composite 是否复合
	 * @param remarks 备注
	 * @param branchNames 所有版本名称
	 * @date 2019年6月24日 上午9:41:04
	 */
	public List<Branch> add(String branchNamesforQuery, Integer branchTypeId, String pcbVersion, String hwVersion, String version, String gsensorVersion, String domainName, String paramVersion, String lock, String pfVersion, Boolean patch, Boolean composite, String remarks, String branchNames) {
		String[] branchName = branchNames.split(",");
		Branch branch = new Branch();
		List<Branch> branchs = Branch.dao.find(SQL.SELECT_BRANCH_BY_BRANCH_NAMES + branchNamesforQuery);
		// 未存储到数据库的版本名集合
		List<String> unsavedBranchNameList = new ArrayList<>();
		// 已经存储到数据库的版本名集合
		List<String> savedBranchNameList = new ArrayList<>();
		if (branchs != null && !branchs.isEmpty()) {
			for (Branch branchTemp : branchs) {
				savedBranchNameList.add(branchTemp.getBranchName());
			}
			for (String name : branchName) {
				if (!savedBranchNameList.contains(name)) {
					unsavedBranchNameList.add(name);
				}
			}
			// 所有版本名的记录已经存在，直接返回
			if (unsavedBranchNameList.isEmpty()) {
				return branchs;
			}
		}
		Branchtype branchtype = Branchtype.dao.findById(branchTypeId);
		if (branchtype == null) {
			throw new OperationException("分支类型不存在，请先添加分支类型");
		}
		branch.setBranchTypeID(branchTypeId);
		if (pcbVersion != null) {
			Pcb pcb = Pcb.dao.findFirst(SQL.SELECT_PCB_BY_PCB_VERSION, pcbVersion);
			if (pcb == null) {
				pcb = new Pcb().setPcbVersion(pcbVersion.trim());
				pcb.save();
				new Rule().setAttribute("PcbID").setFrom(pcb.getID()).setTo(pcb.getID()).save();
			}
			branch.setPcbID(pcb.getID());
		} else {
			branch.setPcbID(0);
		}
		if (hwVersion != null) {
			Hardware hardware = Hardware.dao.findFirst(SQL.SELECT_HW_BY_HW_VERSION, hwVersion);
			if (hardware == null) {
				hardware = new Hardware().setHwVersion(hwVersion.trim());
				hardware.save();
				new Rule().setAttribute("HwID").setFrom(hardware.getID()).setTo(hardware.getID()).save();
			}
			branch.setHwID(hardware.getID());
		} else {
			branch.setHwID(0);
		}
		if (gsensorVersion != null) {
			Gsensor gsensor = Gsensor.dao.findFirst(SQL.SELECT_GSENSOR_BY_GSENSOR_VERSION, gsensorVersion);
			if (gsensor == null) {
				gsensor = new Gsensor().setGsensorVersion(gsensorVersion.trim());
				gsensor.save();
				new Rule().setAttribute("GsensorID").setFrom(gsensor.getID()).setTo(gsensor.getID()).save();
			}
			branch.setGsensorID(gsensor.getID());
		} else {
			branch.setGsensorID(0);
		}
		if (pfVersion != null) {
			Platform platform = Platform.dao.findFirst(SQL.SELECT_PLATFORM_BY_PLATFORM_VERSION, pfVersion);
			if (platform == null) {
				platform = new Platform().setPfVersion(pfVersion.trim());
				platform.save();
				new Rule().setAttribute("PfID").setFrom(platform.getID()).setTo(platform.getID()).save();
			}
			branch.setPfID(platform.getID());
		} else {
			branch.setPfID(0);
		}
		branch.setBranchTypeID(branchTypeId);
		if (domainName != null) {
			branch.setDomainName(domainName.trim());
		}
		if (remarks != null) {
			branch.setRemarks(remarks.trim());
		}
		if (lock != null) {
			branch.setLock(lock.trim());
		}
		if (paramVersion != null) {
			branch.setParamVersion(paramVersion.trim());
		}
		if (patch != null) {
			branch.setPatch(patch);
		}
		if (composite != null) {
			branch.setComposite(composite);
		}
		if (version != null) {
			branch.setBranchVersion(version.trim());
		}
		// 所有版本记录均不存在时
		if (branchs == null || branchs.isEmpty()) {
			branchs = new ArrayList<Branch>();
			for (String name : branchName) {
				if (!StrKit.isBlank(name)) {
					Branch branchTemp = new Branch();
					branchTemp._setAttrs(branch);
					branchTemp.setBranchName(name.trim()).save();
					branchs.add(branchTemp);
				}
			}
		} else {
			// 更新已经存在的版本记录
			for (Branch branchTemp : branchs) {
				branchTemp._setAttrs(branch).update();
			}
			// 增加新的版本记录
			branchs = new ArrayList<Branch>();
			for (String unsavedBranchName : unsavedBranchNameList) {
				if (!StrKit.isBlank(unsavedBranchName)) {
					Branch branchTemp = new Branch();
					branchTemp._setAttrs(branch);
					branchTemp.setBranchName(unsavedBranchName.trim()).save();
					branchs.add(branchTemp);
				}
			}
		}
		return branchs;
	}


	public ResultUtil delete(Integer id, Boolean isForced) {
		Branch branch = Branch.dao.findById(id);
		if (branch == null) {
			throw new OperationException("删除失败，分支不存在");
		}
		if (!isForced) {
			File file = File.dao.findFirst(SQL.SELECT_FILE_BY_BRANCH_ID, id);
			if (file != null) {
				throw new ParameterException("删除失败，分支已被引用，选择“强制删除”，将该记录以及引用该记录的其他表记录一并删除");
			}
			Command command = Command.dao.findFirst(SQL.SELECT_COMMAND_BY_BRANCH_ID, id);
			if (command != null) {
				throw new ParameterException("删除失败，分支已被引用，选择“强制删除”，将该记录以及引用该记录的其他表记录一并删除");
			}
			Operationversionbind bind = Operationversionbind.dao.findFirst(SQL.SELECT_OPERATIONVERSIONBIND_BY_BRANCH_ID, id);
			if (bind != null) {
				throw new ParameterException("删除失败，分支已被引用，选择“强制删除”，将该记录以及引用该记录的其他表记录一并删除");
			}
			Orders orders = Orders.dao.findFirst(SQL.SELECT_ORDERS_BY_BRANCH_ID, id);
			if (orders != null) {
				throw new ParameterException("删除失败，分支已被引用，选择“强制删除”，将该记录以及引用该记录的其他表记录一并删除");
			}
		}
		branch.delete();
		return ResultUtil.succeed();
	}


	public ResultUtil select(String filter, Integer currentPage, Integer pageSize) {
		String[] table = {"branch", "project", "branchtype", "pcb", "hardware", "gsensor", "platform"};
		String[] refers = {"branch.BranchTypeID = branchtype.ID","branchtype.ProjectID = project.ID", "branch.PcbID = pcb.ID", "branch.HwID = hardware.ID", "branch.GsensorID = gsensor.ID", "branch.PfID = platform.ID"};
		Page<Record> pageRecord = selectService.select(table, refers, currentPage, pageSize, "branchVersion", null, filter, null);
		List<BranchVO> branchVOs = BranchVO.fillList(pageRecord.getList());
		PageUtil<BranchVO> pageUtil = new PageUtil<>();
		pageUtil.fill(pageRecord, branchVOs);
		return ResultUtil.succeed(pageUtil);
	}


	public ResultUtil update(Integer id, String branchName, String pcbVersion, String hwVersion, String branchVersion, String gsensorVersion, String domainName, String paramVersion, String lock, String pfVersion, Boolean patch, Boolean composite, String remarks) {
		Branch branch = Branch.dao.findById(id);
		if (branch == null) {
			throw new OperationException("更新失败，分支不存在");
		}
		if (pcbVersion != null) {
			Pcb pcb = Pcb.dao.findFirst(SQL.SELECT_PCB_BY_PCB_VERSION, pcbVersion);
			if (pcb == null) {
				pcb = new Pcb().setPcbVersion(pcbVersion.trim());
				pcb.save();
				new Rule().setAttribute("PcbID").setFrom(pcb.getID()).setTo(pcb.getID()).save();
			}
			branch.setPcbID(pcb.getID());
		} else {
			branch.setPcbID(0);
		}
		if (hwVersion != null) {
			Hardware hardware = Hardware.dao.findFirst(SQL.SELECT_HW_BY_HW_VERSION, hwVersion);
			if (hardware == null) {
				hardware = new Hardware().setHwVersion(hwVersion.trim());
				hardware.save();
				new Rule().setAttribute("HwID").setFrom(hardware.getID()).setTo(hardware.getID()).save();
			}
			branch.setHwID(hardware.getID());
		} else {
			branch.setHwID(0);
		}
		if (gsensorVersion != null) {
			Gsensor gsensor = Gsensor.dao.findFirst(SQL.SELECT_GSENSOR_BY_GSENSOR_VERSION, gsensorVersion);
			if (gsensor == null) {
				gsensor = new Gsensor().setGsensorVersion(gsensorVersion.trim());
				gsensor.save();
				new Rule().setAttribute("GsensorID").setFrom(gsensor.getID()).setTo(gsensor.getID()).save();
			}

			branch.setGsensorID(gsensor.getID());
		} else {
			branch.setGsensorID(0);
		}
		if (pfVersion != null) {
			Platform platform = Platform.dao.findFirst(SQL.SELECT_PLATFORM_BY_PLATFORM_VERSION, pfVersion);
			if (platform == null) {
				platform = new Platform().setPfVersion(pfVersion.trim());
				platform.save();
				new Rule().setAttribute("PfID").setFrom(platform.getID()).setTo(platform.getID()).save();
			}
			branch.setPfID(platform.getID());
		} else {
			branch.setPfID(0);
		}
		if (domainName != null) {
			branch.setDomainName(domainName.trim());
		} else {
			branch.setDomainName("");
		}
		if (remarks != null) {
			branch.setRemarks(remarks.trim());
		} else {
			branch.setRemarks("");
		}
		if (lock != null) {
			branch.setLock(lock.trim());
		} else {
			branch.setLock("");
		}
		if (paramVersion != null) {
			branch.setParamVersion(paramVersion.trim());
		} else {
			branch.setParamVersion("");
		}
		if (patch != null) {
			branch.setPatch(patch);
		}
		if (composite != null) {
			branch.setComposite(composite);
		}
		if (branchVersion != null) {
			branch.setBranchVersion(branchVersion.trim());
		} else {
			branch.setBranchVersion("");
		}
		if (!StrKit.isBlank(branchName)) {
			Branch temp = Branch.dao.findFirst(SQL.SELECT_BRANCH_BY_BRANCH_NAME, branchName);
			if (temp != null && !temp.getID().equals(id)) {
				throw new OperationException("版本名已存在");
			}
			branch.setBranchName(branchName.trim());
		}
		branch.update();
		return ResultUtil.succeed();
	}


	public ResultUtil getBranch(String branchName) {
		StringBuilder filter = new StringBuilder();
		PageUtil<BranchVO> pageUtil = new PageUtil<>();
		if (branchName != null) {
			filter.append("branchName #like#" + branchName);
		}
		Page<Record> pageRecord = selectService.select("branch", null, null, null, null, filter.toString(), null);
		List<BranchVO> branchVOs = BranchVO.fillList(pageRecord.getList());
		pageUtil = new PageUtil<>();
		pageUtil.fill(pageRecord, branchVOs);
		return ResultUtil.succeed(pageUtil);
	}


	public PageUtil<PcbVO> getPcb(String pcbVersion) {
		StringBuilder filter = new StringBuilder();
		PageUtil<PcbVO> pageUtil = new PageUtil<>();
		if (pcbVersion != null) {
			filter.append("PcbVersion #like#" + pcbVersion);
		}
		Page<Record> pageRecord = selectService.select("pcb", null, null, null, null, filter.toString(), null);
		List<PcbVO> pcbVOs = PcbVO.fillList(pageRecord.getList());
		pageUtil = new PageUtil<>();
		pageUtil.fill(pageRecord, pcbVOs);
		return pageUtil;
	}


	public PageUtil<HardwareVO> getHardware(String hwVersion) {
		StringBuilder filter = new StringBuilder();
		PageUtil<HardwareVO> pageUtil = new PageUtil<>();
		if (hwVersion != null) {
			filter.append("HwVersion #like#" + hwVersion);
		}
		Page<Record> pageRecord = selectService.select("hardware", null, null, null, null, filter.toString(), null);
		List<HardwareVO> hardwareVOs = HardwareVO.fillList(pageRecord.getList());
		pageUtil = new PageUtil<>();
		pageUtil.fill(pageRecord, hardwareVOs);
		return pageUtil;
	}


	public PageUtil<GsensorVO> getGsensor(String gsensorVersion) {
		StringBuilder filter = new StringBuilder();
		PageUtil<GsensorVO> pageUtil = new PageUtil<>();
		if (gsensorVersion != null) {
			filter.append("GsensorVersion #like#" + gsensorVersion);
		}
		Page<Record> pageRecord = selectService.select("gsensor", null, null, null, null, filter.toString(), null);
		List<GsensorVO> gsensorVOs = GsensorVO.fillList(pageRecord.getList());
		pageUtil = new PageUtil<>();
		pageUtil.fill(pageRecord, gsensorVOs);
		return pageUtil;
	}


	public PageUtil<PlatformVO> getPlatform(String pfVersion) {
		StringBuilder filter = new StringBuilder();
		PageUtil<PlatformVO> pageUtil = new PageUtil<>();
		if (pfVersion != null) {
			filter.append("PfVersion #like#" + pfVersion);
		}
		Page<Record> pageRecord = selectService.select("platform", null, null, null, null, filter.toString(), null);
		List<PlatformVO> platformVOs = PlatformVO.fillList(pageRecord.getList());
		pageUtil = new PageUtil<>();
		pageUtil.fill(pageRecord, platformVOs);
		return pageUtil;
	}
	
	
	/**
	 * @author 韦姚忠
	 *     过滤查询
	 * @param version 子版本
	 * @param paramVersion 参数版本
	 * @param pcbVersion pbc版本
	 * @param hwVersion 硬件版本
	 * @param gsensorVersion gsensor版本
	 * @param pfVersion 平台协议
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param branchName 分支名
	 * @param currentPage 当前页
	 * @param pageSize 每页记录数当前页
	 * @return
	 */
	public Page<Record> filtrate(String version, String paramVersion, String pcbVersion,
			String hwVersion, String gsensorVersion, String pfVersion, String startTime,String endTime, 
			String branchName,Integer currentPage, Integer pageSize){
		//w.创建基础SQL
		String select = "SELECT f.ID,f.FileName,f.PackTime,b.BranchName ";
		StringBuffer sb = new StringBuffer("FROM file f LEFT JOIN branch b ON  f.BranchID = b.ID LEFT JOIN pcb p ON b.PcbID = p.ID LEFT JOIN hardware h ON b.HwID = h.ID LEFT JOIN gsensor g ON b.GsensorID = g.ID LEFT JOIN platform pf ON b.PfID = pf.ID WHERE f.BranchID = b.ID  ");
		//当用户传进来的字段不为空时拼接SQL语句
		if (version!=null&&!version.equals("")) {
			sb.append("AND b.BranchVersion LIKE '%"+version.trim()+"%'");
		}
		if (paramVersion!=null&&!paramVersion.equals("")) {
			sb.append("AND b.ParamVersion LIKE '%"+paramVersion.trim()+"%'");
		}
		if (pcbVersion!=null&&!pcbVersion.equals("")) {
			sb.append("AND p.PcbVersion LIKE '%"+pcbVersion.trim()+"%'");
		}
		if (hwVersion!=null&&!hwVersion.equals("")) {
			sb.append("AND h.HwVersion LIKE '%"+hwVersion.trim()+"%'");
		}
		if (gsensorVersion!=null&&!gsensorVersion.equals("")) {
			sb.append("AND g.GsensorVersion LIKE '%"+gsensorVersion.trim()+"%'");
		}
		if (pfVersion!=null&&!pfVersion.equals("")) {
			sb.append("AND pf.PfVersion LIKE '%"+pfVersion.trim()+"%'");
		}
		if (branchName!=null&&!branchName.equals("")) {
			sb.append("AND b.BranchName LIKE '%"+branchName.trim()+"%'");
		}
		if (startTime!=null) {
			sb.append("AND f.PackTime >= '"+startTime+"'");
		}
		if (endTime!=null) {
			sb.append("AND f.PackTime <= '"+endTime+"'");
		}
		//按时间排序
		sb.append("ORDER BY f.PackTime ");
		SqlPara sqlPara = new SqlPara();
		sqlPara.setSql(select + sb.toString());
		Page<Record> page = Db.paginate(currentPage, pageSize, sqlPara);
		return page;
	}

}
