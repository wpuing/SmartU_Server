package com.jimi.su.server.interceptor;

import com.jfinal.aop.Aop;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jimi.su.server.annotation.Open;
import com.jimi.su.server.controller.UserController;
import com.jimi.su.server.exception.AccessException;
import com.jimi.su.server.model.Type;
import com.jimi.su.server.model.User;
import com.jimi.su.server.service.UserService;
import com.jimi.su.server.util.TokenBox;


/**
 * 用户权限拦截器
 * @type AccessInterceptor
 * @Company 几米物联技术有限公司-自动化部
 * @author 汤如杰
 * @date 2018年11月2日
 */
public class AccessInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Open open = inv.getMethod().getAnnotation(Open.class);
		if (open != null) {
			inv.invoke();
			return;
		}
		UserService userService = Aop.get(UserService.class);
		// 获取token
		String token = inv.getController().getPara(TokenBox.TOKEN_ID_KEY_NAME);
		//
		User user = TokenBox.get(token, UserController.SESSION_KEY_LOGIN_USER);
		int typeId = 0;
		String permission = null;
		if (user != null && user.getEnable()) {
			user = userService.findUserById(user.getId());
			if (user == null || !user.getEnable()) {
				TokenBox.remove(token);
				throw new AccessException("权限不足，请重新登录");
			}
			user = userService.findPermissionByUserId(user.getId());
			permission = user.get("permission");
		} else {
			Type type = userService.findPersssionById(typeId);
			permission = type.getPermission();
		}
		// 获取方法名
		String requestMethodName = inv.getActionKey();
		// 解析权限字
		String allowOrExpect = permission.split(":")[0];
		String permissionMethodNames = permission.split(":")[1];
		if (allowOrExpect.equals("allow")) {
			if (permissionMethodNames.equals("*")) {
				inv.invoke();
			} else {
				for (String permissionMethodName : permissionMethodNames.split(",")) {
					if (permissionMethodName.equals(requestMethodName)) {
						inv.invoke();
						return;
					}
				}
				throw new AccessException("权限不足，请重新登录");
			}
		} else {
			if (permissionMethodNames.equals("*")) {
				throw new AccessException("权限不足，请重新登录");
			} else {
				for (String permissionMethodName : permissionMethodNames.split(",")) {
					if (permissionMethodName.equals(requestMethodName)) {
						throw new AccessException("权限不足，请重新登录");
					}
				}
				inv.invoke();
			}
		}
	}

}
