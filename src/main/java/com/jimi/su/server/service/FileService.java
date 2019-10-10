package com.jimi.su.server.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.jfinal.aop.Enhancer;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.jimi.su.server.entity.SQL;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.model.Branch;
import com.jimi.su.server.model.Branchtype;
import com.jimi.su.server.model.File;
import com.jimi.su.server.model.vo.FileVO;
import com.jimi.su.server.model.vo.PageUtil;
import com.jimi.su.server.service.base.SelectService;
import com.jimi.su.server.util.FileUtil;
import com.jimi.su.server.util.ResultUtil;


/**
 * 文件信息管理逻辑层
 * @type FileService
 * @Company 几米物联技术有限公司-自动化部
 * @author 汤如杰
 * @date 2018年11月2日
 */
public class FileService {

	private final static String FILE_BASE_PATH = java.io.File.separator + "home" + java.io.File.separator + "jimiftp" + java.io.File.separator;

	private static SelectService selectService = Enhancer.enhance(SelectService.class);
	
	private static BranchService branchService = Enhancer.enhance(BranchService.class);

	/**
	 * @author WYZ *.添加注释
	 * 上传文件
	 * @param MD5Code md5值
	 * @param uploadFile 上传的文件
	 * @param isForced 是否强制上传
	 * @param projectId 项目ID
	 * @param pcbVersion pcb版本
	 * @param hwVersion 硬件版本
	 * @param version 子版本
	 * @param gsensorVersion  gsensor版本
	 * @param domainName 域名
	 * @param paramVersion 参数版本
	 * @param branchTypeId 分支类型ID
	 * @param lock 锁
	 * @param pfVersion 平台版本
	 * @param patch
	 * @param composite 复合
	 * @param packTime 打包时间
	 * @param remarks 备注
	 * @param updateLog 更新日志
	 * @param compositeName 复合版本名
	 * @return
	 */
	public synchronized ResultUtil upload(String MD5Code,UploadFile uploadFile, Boolean isForced, Integer projectId, String pcbVersion, String hwVersion, String version, String gsensorVersion, String domainName, String paramVersion, Integer branchTypeId, String lock, String pfVersion, Boolean patch, Boolean composite, Date packTime, String remarks, String updateLog, String compositeName) {
		java.io.File mUploadFile = uploadFile.getFile();
		// w.获取上传文件的名字
		String fileName = uploadFile.getFileName();
		
		//w.将上传文件传入提取MD5值和前台传进来的MD5值做校验
		boolean md5 = FileUtil.checkMD5(mUploadFile.getPath(), MD5Code);
		if (!md5) {
			throw new ParameterException("校验失败！");
		}
		
		// long强转int 不适用于大文件，但是数据库中字段是int(11)
		Integer fileSize = (int) mUploadFile.length();
		// 防止文件名中携带&nbsp的空格，导致前端输入文件名查不到数据
		try {
			//w.将文件名转成utf-8格式
			fileName = URLEncoder.encode(fileName, "utf-8");
			//w.将文件名中的空格转成+号再转成%20，防止字符之间空格变成+号造成字符串拼接
			fileName = fileName.replaceAll("%C2%A0", "%20");
			//w.解码，转化成utf-8
			fileName = URLDecoder.decode(fileName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		// 获取分支名
		String branchName = fileName.substring(0, fileName.lastIndexOf("."));
		//w.根据下划线_提取分支名到数组
		String[] sp = branchName.split("_");
		if (sp.length >= 2) {
			//w.提取出日期，将日期拼接成字符串
			String dateString = sp[sp.length - 2] + sp[sp.length - 1];
			//w.将日期字符串转转化成日期格式
			Date date = parserDate(dateString);
			//w.当日期不为空时
			if (date != null) {
				//w.截取分支名
				branchName = branchName.substring(0, (branchName.length() - dateString.length() - 2));
			}
		}
		// 复合版本初始值
		if (StrKit.isBlank(compositeName)) {
			compositeName = "";
		}
		// 用于查询的版本名称
		String branchNamesforQuery = concatSqlParamValue(branchName, compositeName);
		// 所有的版本名称
		String branchNames = branchName + "," + compositeName;
		// 分支信息存入数据库
		List<Branch> branchs = branchService.add(branchNamesforQuery, branchTypeId, pcbVersion, hwVersion, version, gsensorVersion, domainName, paramVersion, lock, pfVersion, patch, composite, remarks, branchNames);
		// 从数据库中获取文件信息
		List<File> files = File.dao.find(SQL.SELECT_FILE_BY_NAME, fileName);
		//w.遍历更新分支信息返回的分支信息集合
		for (Branch branch : branchs) {
			//w.判断参数是否符合要求，例如非空判断
			if (files != null && !files.isEmpty() && files.get(0).getBranchID().equals(branch.getID()) && !isForced) {
				throw new OperationException("上传文件失败，文件名重复，若想更新文件，请勾选强制更新");
			}
		}
		//w.文件目录
		String path = FILE_BASE_PATH;
		java.io.File dir = new java.io.File(path);
		//w.测试此抽象路径名表示的目录是否存在。
		if (!dir.exists()) {
			//w.不存在时：创建此抽象路径名指定的目录，包括所有必需但不存在的父目录。
			dir.mkdirs();
		}
		java.io.File file = new java.io.File(path + fileName);
		//w.测试此抽象路径名表示的文件是否存在。
		if (file.exists()) {
			//w.不存在时：删除此抽象路径名表示的文件或目录。如果此路径名表示一个目录，则该目录必须为空才能删除。。
			file.delete();
		}
		try {
			//w.将上传文件复制到指定文件夹中
			FileUtils.copyFile(mUploadFile, file);
		} catch (IOException e) {
			throw new OperationException("文件保存失败");
		}
		//w.使用毫秒时间值构造 Timestamp 对象。+md5值
		Timestamp time = new Timestamp(packTime.getTime());
		File fileTemp = new File();
		if (files != null && !files.isEmpty()) {
			// 更新已经存在的文件记录
			for (File fileData : files) {
				fileData.setFileSize(fileSize).setPackTime(time).setUpdateLog(updateLog.trim()).setMD5Code(MD5Code).update();
			}
			// 已经存储到数据库的版本ID文件记录
			List<Integer> savedBranchIDList = new ArrayList<>();
			for (File temp : files) {
				savedBranchIDList.add(temp.getBranchID());
			}
			// 增加新的文件记录
			for (Branch branch : branchs) {
				if (!savedBranchIDList.contains(branch.getID())) {
					fileTemp.setFileName(fileName.trim()).setBranchID(branch.getID()).setFileSize(fileSize).setPath(file.getAbsolutePath()).setPackTime(time).setUpdateLog(updateLog.trim()).setMD5Code(MD5Code).save();
					fileTemp.remove("ID");
				}
			}
		} else {
			// 增加新的文件记录
			for (Branch branch : branchs) {
				fileTemp.setFileName(fileName.trim()).setBranchID(branch.getID()).setFileSize(fileSize).setPath(file.getAbsolutePath()).setPackTime(time).setUpdateLog(updateLog.trim()).setMD5Code(MD5Code).save();
				fileTemp.remove("ID");
			}
		}
		return ResultUtil.succeed();
	}


	/**
	 * 更新文件信息（文件名和路径其中之一发生改变均可能导致文件转存）
	 * @param id 文件Id
	 * @param fileName 文件名
	 * @param path 文件路径
	 * @param branchId 分支名
	 * @param fileSize 文件大小
	 * @return
	 */
	public synchronized ResultUtil update(Integer id, String fileName, Integer branchId, Date packTime, String updateLog) {
		//w.查询文件是否存在
		File fileData = File.dao.findById(id);
		if (fileData == null) {
			throw new OperationException("更新文件信息失败，文件信息记录不存在");
		}
		if (!fileData.getBranchID().equals(branchId)) {
			//w.查询分支是否存在
			Branch branch = Branch.dao.findById(branchId);
			if (branch == null) {
				throw new OperationException("更新文件信息失败，分支不存在");
			}
			//w.存在就赋值
			fileData.setBranchID(branchId);
		}
		if (updateLog != null) {
			fileData.setUpdateLog(updateLog.trim());
		} else {
			//w.设置为空字符串
			fileData.setUpdateLog("");
		}
		if (packTime != null) {
			fileData.setPackTime(packTime);
		} 
		//w.根据文件名查询文件信息集合
		List<File> files = File.dao.find(SQL.SELECT_FILE_BY_NAME, fileData.getFileName());
		// 如果文件名与文件路径未修改，直接更新数据
		if (fileName.equals(fileData.getFileName())) {
			for (File file : files) {
				fileData.remove("ID", "BranchID");
				file._setAttrs(fileData);
				file.update();
			}
			return ResultUtil.succeed();
		}
		//w.根据文件名查询文件信息
		File mfileData = File.dao.findFirst(SQL.SELECT_FILE_BY_NAME, fileName);
		if (mfileData != null) {
			throw new OperationException("更新文件信息失败，更改后文件名已存在");
		}
		// 如果文件名和路径其中之一发生改变
		java.io.File sourceFile = null;
		java.io.File desFile = null;
		sourceFile = new java.io.File(fileData.getPath());
		desFile = new java.io.File(fileData.getPath().substring(0, fileData.getPath().lastIndexOf(java.io.File.separator) + 1) + fileName);
		if (!sourceFile.exists()) {
			throw new OperationException("更新文件信息失败，文件不存在硬盘上，请手动删除该记录");
		}
		if (desFile.exists()) {
			desFile.delete();
		}
		try {
			FileUtils.copyFile(sourceFile, desFile);
		} catch (IOException e) {
			throw new OperationException("更新文件信息失败，文件转存失败");
		}
		sourceFile.delete();
		// 文件名与文件路径发生修改时，更新数据
		for (File file : files) {
			//w.删除此模型的属性。
			fileData.remove("ID", "BranchID");
			file._setAttrs(fileData);
			file.setFileName(fileName.trim());
			file.setPath(desFile.getAbsolutePath());
			file.update();
		}
		return ResultUtil.succeed();
	}


	/**@author HCJ
	 * 删除文件
	 * @param id 文件ID
	 * @param isForced 是否强制删除
	 * @date 2019年6月21日 下午3:35:27
	 */
	public synchronized ResultUtil delete(Integer id, Boolean isForced) {
		// 判断文件表中是否存在该文件信息
		File fileData = File.dao.findById(id);
		if (fileData == null) {
			throw new OperationException("删除失败，文件信息不存在");
		}
		//w.根据文件名查询文件集合
		List<File> files = File.dao.find(SQL.SELECT_FILE_BY_NAME, fileData.getFileName());
		if (!isForced) {
			if (files != null && files.size() > 1) {
				throw new OperationException("删除失败，文件被其他版本引用，若想删除文件，请选择强制删除");
			}
		}
		// 判断改文件信息是否被引用
		java.io.File file = new java.io.File(fileData.getPath());
		try {
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			throw new OperationException("文件删除失败，文件被引用");
		}
		for (File fileTemp : files) {
			fileTemp.delete();
		}
		return ResultUtil.succeed();
	}


	/**
	 * @author WYZ *.添加注释
	 * 条件分页查询
	 * @param filter 条件
	 * @param currentPage 当前页
	 * @param pageSize 总记录数
	 * @return 返回分页对象
	 */
	public ResultUtil select(String filter, Integer currentPage, Integer pageSize) {
		Page<Record> pageRecord = selectService.select("file", currentPage, pageSize, null, null, filter, null);
		List<FileVO> fileVOs = FileVO.fillList(pageRecord.getList());
		PageUtil<FileVO> pageUtil = new PageUtil<>();
		pageUtil.fill(pageRecord, fileVOs);
		return ResultUtil.succeed(pageUtil);
	}


	/**
	 * 根据fileName模糊查询文件信息，无法使用getFile作为方法名
	 * @param fileName
	 * @return
	 */
	public ResultUtil getFileData(String fileName) {
		StringBuilder filter = new StringBuilder();
		if (fileName != null) {
			filter.append("fileName #like#" + fileName);
		}
		String table = "file";
		//w.得到带分页参数的集合
		Page<Record> pageRecord = selectService.select(table, null, null, null, null, filter.toString(), null);
		//w.将record转化成model
		List<FileVO> fileVOs = FileVO.fillList(pageRecord.getList());
		//w.创建一个分页集合
		PageUtil<FileVO> pageUtil = new PageUtil<>();
		//w.封装数据
		pageUtil.fill(pageRecord, fileVOs);
		return ResultUtil.succeed(pageUtil);
	}

	/**
	 * @author WYZ *.添加注释
	 * 剥离文件名字
	 * @param fileName 文件名
	 * @return 返回文件名
	 */
	public static String parserBranchName(String fileName) {
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		String[] sp = fileName.split("_");
		if (sp.length >= 2) {
			String dateString = sp[sp.length - 2] + sp[sp.length - 1];
			Date date = parserDate(dateString);
			if (date != null) {
				return fileName.substring(0, (fileName.length() - dateString.length() - 2));
			}
		}
		return fileName;
	}

	/**
	 * @author WYZ *.添加注释
	 * 得到不同条件的file集合
	 * @param branchName 分支名
	 * @return file集合
	 */
	public List<File> getUpgradeFile(String branchName) {
		//w.根据分支名查询分支
		Branch branch = Branch.dao.findFirst(SQL.SELECT_BRANCH_BY_BRANCH_NAME, branchName);
		if (branch == null) {
			throw new OperationException("分支不存在");
		}
		Integer pcbId = branch.getPcbID();
		Integer hwId = branch.getHwID();
		Integer pfId = branch.getPfID();
		Integer branchTypeId = branch.getBranchTypeID();
		Integer gSensorId = branch.getGsensorID();
		String domainName = branch.getDomainName();
		String lock = branch.getLock();
		String paramVersion = branch.getParamVersion();
		// String remarks = branch.getRemarks();
		// String branchVersion = branch.getBranchVersion();
		Boolean composite = true;
		Branchtype branchtype = Branchtype.dao.findById(branchTypeId);
		//w.根据不同的参数拼接不同的SQL条件
		StringBuffer baseSql = new StringBuffer("SELECT * FROM file WHERE file.BranchID IN (SELECT branch.ID FROM branch WHERE branch.BranchTypeID IN (SELECT branchtype.ID from branchtype where branchtype.type = "+ branchtype.getType()+ " and branchtype.name = '"+ branchtype.getName()  +"' and branchtype.projectID in (SELECT rule.`to` FROM rule WHERE rule.attribute = 'ProjectID' AND rule.`from` = " + branchtype.getProjectID() + "))");
		if (pcbId != null) {
			baseSql.append(" AND branch.PcbID IN (SELECT rule.`to` FROM rule WHERE rule.attribute = 'PcbID' AND rule.`from` = " + pcbId + ")");
		}
		if (hwId != null) {
			baseSql.append(" AND branch.HwID IN (SELECT rule.`to` FROM rule WHERE rule.attribute = 'HwID' AND rule.`from` = " + hwId + ")");
		}
		if (pfId != null) {
			baseSql.append(" AND branch.PfID IN (SELECT rule.`to` FROM rule WHERE rule.attribute = 'PfID' AND rule.`from` = " + pfId + ")");
		}
		if (gSensorId != null) {
			baseSql.append(" AND branch.GsensorID IN (SELECT rule.`to` FROM rule WHERE rule.attribute = 'GsensorID' AND rule.`from` = " + gSensorId + ")");
		}
		if (domainName != null && !domainName.equals("")) {
			baseSql.append(" AND branch.DomainName = '" + domainName + "'");
		}
		if (paramVersion != null && !paramVersion.equals("")) {
			baseSql.append(" AND branch.ParamVersion = '" + paramVersion + "'");
		}
		if (lock != null && !lock.equals("")) {
			baseSql.append(" AND branch.`Lock` = '" + lock + "'");
		}
		/*
		 * if (patch != null) { baseSql.append("AND branch.Patch = " + patch); }
		 */
		if (branchTypeId != null) {
			baseSql.append(" AND (branch.Composite = " + composite);
			baseSql.append(" OR branch.BranchTypeID = " + branchTypeId + ")");
			
		}
		baseSql.append(")");
		baseSql.append(" ORDER BY file.PackTime desc");
		List<File> file = File.dao.find(baseSql.toString());
		return file;
	}

	
	/**
	 * @author WYZ *.添加注释
	 * 日期转换方法
	 * @param dateString 提取好的日期
	 * @return 返回日期对象
	 */
	public static Date parserDate(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = dateFormat.parse(dateString.replace(":", "").replace(" ", "") + "00");
			return date;
		} catch (Exception e) {
			return null;
		}
	}


	public static void main(String[] args) {
		System.out.println(parserBranchName("GT02F_20_61DM2_B25E_R0_V02_20151112_1042.rar"));
	}


	/**@author HCJ
	 * 根据传入的参数拼接sql语句IN的取值
	 * @param initialParameter 初始参数
	 * @param parameter 参数
	 * @date 2019年6月21日 下午3:24:02
	 */
	private String concatSqlParamValue(String initialParameter, String parameter) {
		StringBuilder sb = new StringBuilder();
		sb.append(" (");
		sb.append(" '" + initialParameter + "',");
		for (String value : parameter.split(",")) {
			if (!StrKit.isBlank(value)) {
				sb.append(" '" + value + "',");
			}
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(" )");
		return sb.toString();
	}
	
	
	/**
	 * @author 几米物联自动化部-韦姚忠
     * @date 2019年7月16日
     * 获得路径和MD5码
	 * @param id
	 * @return
	 */
	public java.io.File download(Integer id) {
		File file = File.dao.findById(id);
		if (file == null) {
			throw new OperationException("文件不存在");
		}
		return new java.io.File(file.getPath());
		
	}
	

	/**
	 * @author 几米物联自动化部-韦姚忠
     * @date 2019年7月16日
     * 获得路径和MD5码
	 * @param id
	 * @return
	 */
	public ResultUtil getMD5Code(Integer id) {
		File file = File.dao.findById(id);
		if (file == null) {
			throw new OperationException("文件不存在");
		}
		return ResultUtil.succeed(file.getMD5Code());
		
	}
	
	
}
