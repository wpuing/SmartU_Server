package com.jimi.su.server.interceptor;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.json.Json;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.su.server.annotation.Log;
import com.jimi.su.server.controller.UserController;
import com.jimi.su.server.model.ActionLog;
import com.jimi.su.server.util.TokenBox;


/**
 * 该版本的类无用户名列<br>
 * 操作日志拦截器，本拦截器会对带有@Log注解的方法进行日志记录，记录的详细信息为Log中的值，其中变量<br>
 * 使用<b>{变量}</b>表示，变量名和被注解的方法参数名必须一致，值为参数值。举个例子：<br><br>
 * @Log("获取了用户名为{userId}的用户")<br>
 * public void getUser(String userId){ ... }<br>
 * <br>
 * 如果传入参数值为"bobo"，则数据库的详细信息为"获取了用户名为bobo的用户"<br>
 * <br>
 * <b>2019年4月4日</b>
 * @author 几米物联自动化部-洪达浩
 */
public class ActionLogInterceptor implements Interceptor {

	private static final String REGEX = "\\{[a-zA-Z0-9]+\\}";
	
	@Override
	public void intercept(Invocation invocation) {
		Log log = invocation.getMethod().getAnnotation(Log.class);
		Controller controller = invocation.getController();
		ActionLog actionLog = new ActionLog();
		
		//如果存在@Log注解，则进行日志记录
		if(log != null) {
			//匹配参数并替换值
			Matcher matcher = Pattern.compile(REGEX).matcher(log.value());
			StringBuffer sb = new StringBuffer();  
			while(matcher.find()) {
				String a = matcher.group();
				a = a.substring(1, a.length() - 1);
				String value = controller.getPara(a);
				if(value == null) {
					value = "null";
				}
				//美元符转义
				if(value.contains("$")) {
					value = value.replaceAll("\\$", "\\\\\\$");
				}
				matcher.appendReplacement(sb, value);
			}
			matcher.appendTail(sb);
			//日志生成
			HttpServletRequest request = controller.getRequest();
			String url = request.getRequestURL().toString();
			String address = request.getRemoteAddr() +":"+ request.getRemotePort();
			String parameters = Json.getJson().toJson(request.getParameterMap());
			actionLog.setAddress(address);
			actionLog.setParameters(parameters);
			actionLog.setTime(new Date());
			actionLog.setUrl(url);
			actionLog.setAction(sb.toString());
			Record loginedUser = TokenBox.get(controller.getPara(TokenBox.TOKEN_ID_KEY_NAME), UserController.SESSION_KEY_LOGIN_USER);
			if(loginedUser != null) {
				actionLog.setUid(loginedUser.get("name"));
			}
			//执行
			long t1 = System.currentTimeMillis();
			try {
				invocation.invoke();
				long t2 = System.currentTimeMillis() - t1;
				actionLog.setConsumeTime((int) t2);
//				System.out.println("uid c get--->"+loginedUser.get("name"));
//				System.out.println("uid---->"+actionLog.getUid());
				actionLog.setResultCode(200);
				//插入
				actionLog.save();
			} catch (Exception e) {
				long t3 = System.currentTimeMillis() - t1;
				actionLog.setConsumeTime((int) t3);
//				System.out.println("e uid c get--->"+loginedUser.get("name"));
//				System.out.println("e uid---->"+actionLog.getUid());
				actionLog.setResultCode(ErrorLogInterceptor.getResultCode(e));
				//插入
				actionLog.save();
				throw e;
			}
		}else {
			invocation.invoke();
		}
	}
}
