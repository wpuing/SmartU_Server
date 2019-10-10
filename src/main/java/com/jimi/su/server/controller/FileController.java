package com.jimi.su.server.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.jimi.su.server.annotation.Log;
import com.jimi.su.server.annotation.Open;
import com.jimi.su.server.entity.GSensor;
import com.jimi.su.server.entity.VersionInfo;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.model.File;
import com.jimi.su.server.service.BranchService;
import com.jimi.su.server.service.FileService;
import com.jimi.su.server.service.ProjectService;
import com.jimi.su.server.util.ResultUtil;


/**
 * 文件管理控制层
 * @type FileController
 * @Company 几米物联技术有限公司-自动化部
 * @author 汤如杰
 * @date 2018年11月1日
 */
public class FileController extends Controller {

	@Inject
	private FileService fileService;

	@Inject
	private ProjectService projectService;
	
	@Inject
	private BranchService branchService;

	/**
	 * 上传文件
	 */
	@Log("上传文件：md5:{MD5code},{isForced}，{projectId}，{pcbVersion}，{hwVersion}，{version}，{gsensorVersion}，{domainName}，{paramVersion}，{branchTypeId}，{lock}，{pfVersion}，{patch}，{composite}，{packTime}，{remarks}，{updateLog}，{compositeName}")
	public void upload(String MD5code,UploadFile uploadFile, Boolean isForced, Integer projectId, String pcbVersion, String hwVersion, String version, String gsensorVersion, String domainName, String paramVersion, Integer branchTypeId, String lock, String pfVersion, Boolean patch, Boolean composite, String packTime, String remarks, String updateLog, String compositeName) {
		uploadFile = getFile();
		if (uploadFile == null || isForced == null || packTime == null) {
			throw new OperationException("参数不能为空");
		}
		if (branchTypeId == null) {
			throw new OperationException("定制信息不能为空");
		}
		if (MD5code == null || MD5code.equals("")) {
			throw new OperationException("md5值不能为空");
		}
		if (updateLog == null || updateLog.equals("")) {
			throw new OperationException("文件更新日志不能为空");
		}
		DateFormat bf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = null;
		if (packTime != null && !packTime.equals("")) {
			try {
				time = bf.parse(packTime);
			} catch (ParseException e) {
				throw new OperationException("日期格式不正确");
			}
		}
		if (composite != null) {
			Boolean isCompositeNameBlank = StrKit.isBlank(compositeName);
			if ((!composite && !isCompositeNameBlank) || (composite && isCompositeNameBlank)) {
				throw new ParameterException("请确认复合版本信息是否填写正确");
			}
		}
		ResultUtil result = fileService.upload(MD5code,uploadFile, isForced, projectId, pcbVersion, hwVersion, version, gsensorVersion, domainName, paramVersion, branchTypeId, lock, pfVersion, patch, composite, time, remarks, updateLog, compositeName);
		renderJson(result);
	}


	/**
	 * 更新文件
	 * @param id 文件Id
	 * @param fileName 文件名（发生改变时文件路径也会发生改变）
	 * @param branchId
	 */
	@Log("更新文件：文件id：{id}，文件名：{fileName}，分支id：{branchId}，打包时间：{packTime}，更新日志：{updateLog}")
	public void update(Integer id, String fileName, Integer branchId, String packTime, String updateLog) {
		if (id == null || fileName == null || branchId == null) {
			throw new OperationException("参数不能为空");
		}
		//w.判断更新日志
		isUpdateLog(updateLog);
		
		DateFormat bf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date time = null;
		if (packTime != null && !packTime.equals("")) {
			try {
				time = bf.parse(packTime);
			} catch (ParseException e) {
				throw new OperationException("日期格式不正确");
			}
		}
		ResultUtil result = fileService.update(id, fileName, branchId, time, updateLog);
		renderJson(result);
	}


	public void select(String filter, Integer currentPage, Integer pageSize) {
		if (currentPage == null || pageSize == null) {
			throw new OperationException("参数不能为空");
		}
		if (currentPage <= 0 || pageSize <= 0) {
			throw new ParameterException("当前页码与每页行数均需要大于0");
		}
		ResultUtil result = fileService.select(filter, currentPage, pageSize);
		renderJson(result);
	}


	@Log("要删除的文件id是:{id}，是否强制：{isForced}")
	public void delete(Integer id, Boolean isForced) {
		if (id == null || isForced == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = fileService.delete(id, isForced);
		renderJson(result);
	}


	/**
	 * 根据fileName模糊查询文件信息
	 * @param fileName
	 */
	public void getFileData(String fileName) {
		if (fileName == null) {
			throw new OperationException("参数不能为空");
		}
		ResultUtil result = fileService.getFileData(fileName);
		renderJson(result);
	}


	public void parserVersion(String fileName) {
		if (fileName == null) {
			throw new ParameterException("参数不能为空");
		}
		VersionInfo versionInfo = new VersionInfo();
		try {
			String[] versionSp = fileName.split("_");
			int i = 0;
			int length = versionSp.length;
			String projectName = "";
			String pcbVersion = "";
			String hwVersion = "";
			String version = "";
			String gSensor = "";
			String domainName = "";
			String paramVersion = "";
			String remarks = "";
			// 外贸
			String ft = "";
			// 定制
			String domestic = "";
			String lock = "";
			Boolean patch = false;
			Date date = null;
			// 解析项目名
			if (length > 0) {
				projectName = versionSp[i];
				if (!projectService.contianProject(projectName) && length > 1) {
					String projectNameTemp = projectName + "_" + versionSp[++i];
					if (projectService.contianProject(projectNameTemp)) {
						projectName = projectNameTemp;
					} else {
						i--;
					}
				}
				// 解析pcb版本
				if (length > i + 1) {
					pcbVersion = versionSp[++i];
				}
				// 解析硬件版本
				if (length > i + 1) {
					hwVersion = versionSp[++i];
				}
				// 解析版本
				int j = i + 1;
				for (; j < length; j++) {
					if (versionSp[j].startsWith("LG") || versionSp[j].startsWith("BG") || versionSp[j].startsWith("V") || versionSp[j].startsWith("L") || versionSp[j].startsWith("G")) {
						version = versionSp[j];
						break;
					}
				}

				for (int k = i + 1; k < j; k++) {
					// 解析gSensor&动态域名&参数版本&备注
					if (GSensor.isGSensor(versionSp[k]) && gSensor.equals("")) {
						gSensor = versionSp[k];
					} else if (versionSp[k].equals("D") && domainName.equals("")) {
						domainName = versionSp[k];
					} else if (paramVersion.equals("") && (versionSp[k].equals("R0") || versionSp[k].equals("F0"))) {
						paramVersion = versionSp[k];
					} else {
						if (!remarks.equals("")) {
							remarks = remarks + "_";
						}
						remarks = remarks + versionSp[k];
					}
				}
				// 解析外贸&定制&锁域&时间
				for (int k = j + 1; k < length; k++) {
					if (k == length - 2) {
						date = parserDate(versionSp[k] + versionSp[k + 1]);
						if (date != null) {
							k++;
						}
					} else if (lock.equals("") && (versionSp[k].equals("LA") || versionSp[k].equals("LAS") || versionSp[k].equals("NS"))) {
						lock = versionSp[k];
					} else if (ft.equals("") && versionSp[k].equals("WM")) {
						if (k + 1 < length && !isNum(versionSp[k + 1]) && !versionSp[k + 1].toLowerCase().equals("patch")) {
							ft = versionSp[++k];
						}
						ft = "默认";
					} else if (versionSp[k].toLowerCase().equals("patch")) {
						patch = true;
					} else {
						if (!domestic.equals("")) {
							domestic = domestic + "_";
						}
						domestic = domestic + versionSp[k];
					}
				}
				versionInfo.setProjectName(projectName);
				versionInfo.setPcbVersion(pcbVersion);
				versionInfo.setHwVersion(hwVersion);
				versionInfo.setVersion(version);
				versionInfo.setDomainName(domainName);
				versionInfo.setGsensorVersion(gSensor);
				versionInfo.setParamVersion(paramVersion);
				versionInfo.setLock(lock);
				versionInfo.setRemarks(remarks);
				versionInfo.setDomesticName(domestic);
				versionInfo.setFtName(ft);
				versionInfo.setPatch(patch);
				versionInfo.setPackTime(date);
			}
			renderJson(ResultUtil.succeed(versionInfo));
		} catch (Exception e) {
			renderJson(ResultUtil.succeed(versionInfo));
		}

	}


	@Open
	public void getUpgradeFile(String name) {
		if (name == null) {
			throw new ParameterException("参数不能为空");
		}
		List<File> files = fileService.getUpgradeFile(name);
		renderJson(ResultUtil.succeed(files));
	}


	@SuppressWarnings("unused")
	private Boolean isNum(String string) {
		try {
			Integer a = Integer.valueOf(string);
		} catch (Exception e) {
			return false;
		}
		return true;
	}


	private Date parserDate(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = dateFormat.parse(dateString.replace(":", "").replace(" ", "") + "00");
			return date;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * @author 几米物联自动化部-韦姚忠
     * @date 2019年7月15日
     * 判断文件更新日志
	 * @param updateLog 文件更新日志
	 */
	private void isUpdateLog(String updateLog) {
		if (updateLog == null || updateLog.equals("")) {
			throw new ParameterException("文件更新日志不能为空！");
		}
	}
	
	
	/**
	 * @author 几米物联自动化部-韦姚忠
     * @date 2019年7月16日 
     * 根据id下载文件
	 * @param id
	 */
	public void download(Integer id) {
		if (id == null) {
			throw new OperationException("参数不能为空");
		}
		java.io.File file = fileService.download(id);
		renderFile(file);
	}
	
	
	/**
	 * @author 几米物联自动化部-韦姚忠
     * @date 2019年7月16日 
     * 根据id获得文件校验码
	 * @param id
	 */
	public void getMD5(Integer id) {
		if (id == null) {
			throw new OperationException("参数不能为空");
		}
		renderJson(fileService.getMD5Code(id));
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
	public void filtrate(String version, String paramVersion, String pcbVersion,
			String hwVersion, String gsensorVersion, String pfVersion, String startTime,String endTime, 
			String branchName,Integer currentPage, Integer pageSize) {
		if (startTime !=null&&!startTime.equals("")&&endTime !=null&&!endTime.equals("")) {
			Date start = parserDate(startTime);
			Date end = parserDate(endTime);
			if (start==null||end==null) {
				throw new ParameterException("时间日期格式有误");
			}
		}
		if (currentPage == null || pageSize == null) {
			throw new OperationException("参数不能为空");
		}
		if (currentPage <= 0 || pageSize <= 0) {
			throw new ParameterException("当前页码与每页行数均需要大于0");
		}
		 Page<Record> page = branchService.filtrate(version, paramVersion, pcbVersion, hwVersion, 
				 gsensorVersion, pfVersion, startTime, endTime, branchName, currentPage, pageSize);
		renderJson(page);
	}
	
	
	
	public static void main(String[] args) {
		Date date = new FileController().parserDate("2018-13-17");
		System.out.println(date);
	}
	
	
	
}
