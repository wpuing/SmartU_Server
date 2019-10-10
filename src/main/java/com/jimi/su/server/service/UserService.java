package com.jimi.su.server.service;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Enhancer;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.su.server.entity.Permission;
import com.jimi.su.server.entity.SQL;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.model.Organization;
import com.jimi.su.server.model.Type;
import com.jimi.su.server.model.User;
import com.jimi.su.server.model.vo.PageUtil;
import com.jimi.su.server.model.vo.SimpleUserVO;
import com.jimi.su.server.model.vo.UserVO;
import com.jimi.su.server.service.base.SelectService;
import com.jimi.su.server.util.CommonUtil;
import com.jimi.su.server.util.ResultUtil;

import cc.darhao.dautils.api.MD5Util;


/**
 * 用户管理逻辑层
 * @type UserService
 * @Company 几米物联技术有限公司-自动化部
 * @author 汤如杰
 * @date 2018年11月2日
 */
public class UserService extends SelectService {

	private static SelectService selectService = Enhancer.enhance(SelectService.class);


	public ResultUtil add(String name, String password, Integer typeId) {
		User user = User.dao.findFirst(SQL.SELECT_USER_BY_NAME, name);
		if (user != null) {
			throw new OperationException("添加失败，用户已存在");
		}
		Type type = Type.dao.findById(typeId);
		if (type == null) {
			throw new OperationException("添加失败，用户类型不存在");
		}
		user = new User();
		user.setName(name.trim()).setPassword(MD5Util.MD5(password)).setEnable(true).setTypeId(typeId).save();
		return ResultUtil.succeed();
	}


	public ResultUtil update(Integer id, String name, String password, Boolean enable, Integer typeId) {
		User user = User.dao.findById(id);
		if (user == null) {
			throw new OperationException("更新失败，用户不存在");
		}
		User temp = User.dao.findFirst(SQL.SELECT_USER_BY_NAME, name);
		if (temp != null && !temp.getId().equals(id)) {
			throw new OperationException("更新失败，用户名已存在");
		}
		if (password != null) {
			user.setPassword(MD5Util.MD5(password.trim()));
		}
		if (name != null) {
			user.setName(name.trim());
		}
		if (enable != null) {
			user.setEnable(enable);
		}
		if (typeId != null) {
			if (!user.getTypeId().equals(typeId)) {
				Type type = Type.dao.findById(typeId);
				if (type == null) {
					throw new OperationException("更新失败，用户类型不存在");
				}
				user.setTypeId(typeId);
			}
		}

		user.update();
		return ResultUtil.succeed();
	}


	public ResultUtil select(Integer currentPage, Integer pageSize, String filter) {
		String[] table = {"user", "type"};
		String[] refers = {"user.typeId = type.id"};
		Page<Record> pageRecord = new Page<>();
		PageUtil<UserVO> pageUtil = new PageUtil<>();
		List<UserVO> userVOs = new ArrayList<>();
		pageRecord = selectService.select(table, refers, currentPage, pageSize, null, null, filter, null);
		userVOs = UserVO.fillList(pageRecord.getList());
		pageUtil.fill(pageRecord, userVOs);
		return ResultUtil.succeed(pageUtil);
	}


	public ResultUtil addUserType(String typeName, String permission, String pcPermission) {
		Type type = Type.dao.findFirst(SQL.SELECT_TYPE_BY_NAME, typeName);
		if (type != null) {
			throw new OperationException("添加失败，用户类型已存在");
		}
		String[] permisssions = permission.split(",");
		StringBuffer permissionSb = new StringBuffer("allow:");
		for (String string : permisssions) {
			List<String> perPermission = Permission.getSring(Integer.valueOf(string));
			for (String string2 : perPermission) {
				permissionSb.append(string2 + ",");
			}
		}
		if (permissionSb != null && permissionSb.length() != 0) {
			permissionSb.append("/user/login,/user/logout");
		}
		type = new Type().setTypeName(typeName.trim()).setPermission(permissionSb.toString()).setPcPermission(pcPermission);
		type.save();
		return ResultUtil.succeed();
	}


	public ResultUtil updateUserType(Integer typeId, String typeName, String permission, String pcPermission) {
		Type type = Type.dao.findById(typeId);
		if (type == null) {
			throw new OperationException("更新失败，用户类型不存在");
		}
		if (!type.getTypeName().equals(typeName)) {
			Type temp = Type.dao.findFirst(SQL.SELECT_TYPE_BY_NAME, typeName);
			if (temp != null) {
				throw new OperationException("更新失败，用户类型已存在");
			}
		}
		String[] permisssions = permission.split(",");
		StringBuffer permissionSb = new StringBuffer("allow:");
		for (String string : permisssions) {
			List<String> perPermission = Permission.getSring(Integer.valueOf(string));
			for (String string2 : perPermission) {
				permissionSb.append(string2 + ",");
			}
		}
		if (permissionSb != null && permissionSb.length() != 0) {
			permissionSb.append("/user/login,/user/logout");
		}
		type.setTypeName(typeName.trim()).setPermission(permissionSb.toString()).setPcPermission(pcPermission);
		type.update();
		return ResultUtil.succeed();
	}


	public ResultUtil deleteUserTypeById(Integer id) {
		Type type = Type.dao.findById(id);
		if (type == null) {
			throw new OperationException("删除失败，用户类型不存在");
		}
		try {
			type.delete();
		} catch (Exception e) {
			throw new OperationException("删除失败，用户类型已被引用");
		}
		return ResultUtil.succeed();
	}


	public ResultUtil delete(Integer id) {
		User user = User.dao.findById(id);
		if (user == null) {
			throw new OperationException("删除失败，用户不存在");
		}
		User.dao.deleteById(id);
		return ResultUtil.succeed();
	}


	public ResultUtil getType() {
		List<Type> types = Type.dao.find(SQL.SELECT_ALL_USERTYPE);
		for (Type type : types) {
			if (type.getPermission() != null && !type.getPermission().trim().equals("")) {
				String permissionInt = CommonUtil.getPermissionInt(type.getPermission());
				type.setPermission(permissionInt);
			}
		}
		return ResultUtil.succeed(types);
	}


	public User findVaildUser(String name) {
		String[] table = {"user", "type"};
		String[] refers = {"user.typeId = type.id"};
		String filter = "user.name#=#" + name + "#&#user.enable#=#1";
		Page<Record> pageRecord = new Page<>();
		pageRecord = selectService.select(table, refers, 1, 1, null, null, filter, null);
		UserVO userVO = UserVO.fill(pageRecord.getList());
		User user = null;
		if (userVO != null) {
			user = new User().setId(userVO.getId()).setName(userVO.getName()).setPassword(userVO.getPassword()).setEnable(userVO.getEnable()).setTypeId(userVO.getTypeId()).put("typeName", userVO.getTypeName()).put("permission", userVO.getPermission()).put("pcPermission", userVO.getPcPermission());
		}
		return user;
	}


	public User findUserById(Integer id) {
		return User.dao.findById(id);
	}


	public User findPermissionByUserId(Integer id) {

		User user = User.dao.findFirst(SQL.FIND_PERMISSION_BY_USER_ID, id);
		return user;
	}


	public Type findPersssionById(Integer id) {

		Type type = Type.dao.findById(id);
		return type;
	}


	public ResultUtil getUser(String userName) {
		StringBuilder filter = new StringBuilder();
		PageUtil<SimpleUserVO> pageUtil = new PageUtil<SimpleUserVO>();
		filter.append("enable #=# 1");
		if (userName != null) {
			filter.append("#&# name #like#" + userName);
		}
		Page<Record> pageRecord = selectService.select("user", null, null, null, null, filter.toString(), null);
		List<SimpleUserVO> simpleUserVOs = SimpleUserVO.fillList(pageRecord.getList());
		pageUtil.fill(pageRecord, simpleUserVOs);
		return ResultUtil.succeed(pageUtil);
	}
	
	
	/**
	 * @author WYZ
	 * @date 2019年7月13日
	 */
	public ResultUtil verify(String name,String password) {
		User user = User.dao.findFirst(SQL.SELECT_USER_BY_NAME_PASSWORD,name,MD5Util.MD5(password));
		if (user==null) {
			throw new OperationException("验证失败！");
		}
		return ResultUtil.succeed();
		
	}

	/**
	 * @author WYZ
	 * @date 2019年7月15日
	 */
	public ResultUtil addOrganization(String group, Integer userId) {
		Organization organization = new Organization();
		organization = Organization.dao.findFirst(SQL.SELECT_GROUP_BY_NAME, group);
		if (userId!=null) {
			User user = User.dao.findById(userId);
			if (user == null) {
				throw new ParameterException("用户不存在");
			}
		}
		if (organization == null) {
			organization = new Organization().setGroup(group).setUserID(userId);
			organization.save();
		}else {
			throw new OperationException("组织归属已存在!!!");
		}
		return ResultUtil.succeed();
	}

	/**
	 * @author WYZ
	 * @date 2019年7月15日
	 */
//	public ResultUtil selectOrganization(String filter, Integer currentPage, Integer pageSize) {
//		Page<Record> pageRecord = selectService.select("organization", currentPage, pageSize, null, null, filter, null);
//		List<OrganizationVO> organizationVOs = OrganizationVO.fillList(pageRecord.getList());
//		PageUtil<OrganizationVO> pageUtil = new PageUtil<OrganizationVO>();
//		pageUtil.fill(pageRecord, organizationVOs);
//		return ResultUtil.succeed(pageUtil);
//	}
	
	public ResultUtil selectOrganization(String filter) {
		List<Organization> organizations = new ArrayList<Organization>();
		if (filter!=null && !filter.equals("")) {
			organizations = Organization.dao.find(SQL.SELECT_VAGUE_ORGAN_BY_NAME+"'%"+filter+"%'");
		}else {
			organizations = Organization.dao.find(SQL.SELECT_ORGAN_ALL);
		}
		return ResultUtil.succeed(organizations);
	}

}
