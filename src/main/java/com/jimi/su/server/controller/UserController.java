package com.jimi.su.server.controller;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jimi.su.server.annotation.Log;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.model.User;
import com.jimi.su.server.service.UserService;
import com.jimi.su.server.util.ResultUtil;
import com.jimi.su.server.util.TokenBox;

import cc.darhao.dautils.api.MD5Util;


/**
 * 用户管理控制层
 * @type UserController
 * @Company 几米物联技术有限公司-自动化部
 * @author 汤如杰
 * @date 2018年10月29日
 */
public class UserController extends Controller {

	public static final String SESSION_KEY_LOGIN_USER = "loginUser";

	@Inject
	private UserService userService;


	@Log("添加用户，信息：{name}，{password}，{typeId}")
	public void add(String name, String password, Integer typeId) {
		if (name == null || name.trim().equals("") || password == null || password.trim().equals("") || typeId == null) {
			throw new OperationException("参数不能为空");
		}
		if (isSpecialChar(name)) {
			throw new OperationException("用户名不能包含特殊字符");
		}
		ResultUtil result = userService.add(name, password, typeId);
		renderJson(result);
	}


	@Log("登录用户，用户名是：{name}，用户密码：{password}")
	public void login(String name, String password) {
		if (name == null || password == null) {
			throw new OperationException("参数不能为空");
		}
		if (isSpecialChar(name)) {
			throw new OperationException("用户名不能包含特殊字符");
		}
		User user = userService.findVaildUser(name);
		if (user == null || !user.getPassword().equals(MD5Util.MD5(password))) {
			throw new OperationException("用户名或密码错误");
		}
		String tokenId = getPara(TokenBox.TOKEN_ID_KEY_NAME);
		if (tokenId != null) {
			User user2 = TokenBox.get(tokenId, SESSION_KEY_LOGIN_USER);
			if (user2 != null && user.getId().equals(user2.getId())) {
				throw new OperationException("用户已登录");
			}
		}
		tokenId = TokenBox.createTokenId();
		user.put(TokenBox.TOKEN_ID_KEY_NAME, tokenId);
		TokenBox.put(tokenId, SESSION_KEY_LOGIN_USER, user);
		renderJson(ResultUtil.succeed(user));
	}


	public void logout() {
		String tokenId = getPara(TokenBox.TOKEN_ID_KEY_NAME);
		User user = TokenBox.get(tokenId, SESSION_KEY_LOGIN_USER);
		if (user == null) {
			throw new OperationException("无用户登录，无法登出");
		}
		TokenBox.remove(tokenId);
		renderJson(ResultUtil.succeed());
	}


	@Log("更新用户信息：{id}，{name}，{password}，{enable}，{enable}，{typeId}")
	public void update(Integer id, String name, String password, Boolean enable, Integer typeId) {
		if (id == null || StringUtils.isBlank(name)) {
			throw new OperationException("参数不能为空");
		}
		if (isSpecialChar(name)) {
			throw new OperationException("用户名不能包含特殊字符");
		}
		ResultUtil result = userService.update(id, name, password, enable, typeId);
		String token = getPara(TokenBox.TOKEN_ID_KEY_NAME);
		User user = TokenBox.get(token, UserController.SESSION_KEY_LOGIN_USER);
		if (user.getId().equals(id) && enable == false) {
			TokenBox.remove(token);
		}
		renderJson(result);
	}


	public void getType() {
		renderJson(userService.getType());
	}


	public void select(Integer currentPage, Integer pageSize, String filter) {
		if (currentPage == null || pageSize == null) {
			throw new OperationException("参数不能为空");
		}
		if (currentPage <= 0 || pageSize <= 0) {
			throw new ParameterException("当前页码与每页行数均需要大于0");
		}
		renderJson(userService.select(currentPage, pageSize, filter));
	}


	@Log("添加用户类型：{typeName}，{permission}，{pcPermission}")
	public void addUserType(String typeName, String permission, String pcPermission) {
		if (typeName == null || permission == null || permission.trim().equals("") || pcPermission == null || pcPermission.trim().equals("")) {
			throw new OperationException("参数不能为空");
		}
		if (isSpecialChar(typeName)) {
			throw new OperationException("用户类型不能包含特殊字符");
		}
		renderJson(userService.addUserType(typeName, permission, pcPermission));
	}


	@Log("更新用户类型：{typeId}，{typeName}，{permission}，{pcPermission}")
	public void updateUserType(Integer typeId, String typeName, String permission, String pcPermission) {
		if (typeId == null || typeName == null || permission == null || permission.trim().equals("") || pcPermission == null || pcPermission.trim().equals("")) {
			throw new OperationException("参数不能为空");
		}
		if (isSpecialChar(typeName)) {
			throw new OperationException("用户类型不能包含特殊字符");
		}
		if (typeId.equals(0)) {
			throw new OperationException("未登录者角色不能删除");
		}
		if (typeId.equals(3)) {
			throw new OperationException("管理员角色不能删除");
		}
		renderJson(userService.updateUserType(typeId, typeName, permission, pcPermission));
	}


	@Log("要删除的用户类型：id：{typeId}")
	public void deleteUserType(Integer typeId) {
		if (typeId == null) {
			throw new OperationException("参数不能为空");
		}
		renderJson(userService.deleteUserTypeById(typeId));
	}


	/**@author HCJ
	 * 根据用户名称模糊搜索相关信息
	 * @param userName 需要进行搜索的信息
	 * @date 2019年5月21日 下午3:58:51
	 */
	public void getUser(String userName) {
		if (userName == null) {
			throw new ParameterException("参数不能为空");
		}
		ResultUtil result = userService.getUser(userName);
		renderJson(result);
	}


	/**
	 * 判断是否含有特殊字符
	 *
	 * @param str
	 * @return true为包含，false为不包含
	 */
	private static boolean isSpecialChar(String str) {
		String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		boolean flag = m.find();
		if (!flag && str.indexOf("\\") == -1) {
			return false;
		}
		return true;
	}

	
	/**
	 * @author WYZ
	 * @date 2019年7月13日
	 * 校验用户账号密码
	 * @param name 用户账号
	 * @param password 用户密码
	 */
	@Log("检验用户名：{name},用户密码：{password}")
	public void verify(String name,String password) {
		if (name == null || name.trim().equals("") || password == null || password.trim().equals("")) {
			throw new OperationException("账号或密码有误！");
		}
		renderJson(userService.verify(name, password));
	}
	
	
	/**
	 * @author WYZ
	 * @date 2019年7月15日
	 * 增加组织归属
	 * @param group 组织归属名
	 * @param userId 用户id
	 */
	@Log("添加组织归属，信息：{group}，用户id：{userId}")
	public void addOrganization(String group,Integer userId) {
		if (group == null || group.equals("")) {
			throw new ParameterException("请输入组织归属名称");
		}
		ResultUtil resultUtil = userService.addOrganization(group,userId);
		renderJson(resultUtil);
	}
	
	
	/**
	 * @author WYZ
	 * @date 2019年7月15日
	 */
//	public void selectOrganization(String filter, Integer currentPage, Integer pageSize) {
//		if (currentPage == null || pageSize == null) {
//			throw new OperationException("参数不能为空");
//		}
//		if (currentPage <= 0 || pageSize <= 0) {
//			throw new ParameterException("当前页码与每页行数均需要大于0");
//		}
//		ResultUtil result = userService.selectOrganization(filter, currentPage, pageSize);
//		renderJson(result);
//	}
	
	public void selectOrganization(String filter) {
		ResultUtil result = userService.selectOrganization(filter);
		renderJson(result);
	}
	
	
	

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(isSpecialChar("\\"));
		String name = "你是啥";
		System.out.println(name.getBytes("utf8").length);
		
		
	}
	
	
	
	
}
