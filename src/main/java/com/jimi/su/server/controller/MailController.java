package com.jimi.su.server.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jimi.su.server.annotation.Log;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.service.MailService;
import com.jimi.su.server.util.ResultUtil;

/**
 * 邮件管理 控制层
 *@author 几米物联自动化部-韦姚忠
 *@date   2019年7月13日
 */
public class MailController extends Controller {
	
	@Inject
	private MailService mailService;

	
	/**
	 * @author 几米物联自动化部-韦姚忠
	 *     发送邮件
	 * @param mails 接收人集合
	 * @param mailTitle 邮件标题
	 * @param content 邮件内容
	 * @return
	 */
	@Log("发送邮件，发送人邮箱：{acceptors}，邮件头部：{mailTitle}")
	public void sendMail(String acceptors,String mailTitle,String content) {
		
		if (acceptors==null||acceptors.trim().equals("")||content ==null||content.trim().equals("")
				||mailTitle ==null||mailTitle.trim().equals("")) {
			throw new OperationException("参数不能为空");
		}
		String[] mails = acceptors.split(",");
		for (String mail : mails) {
			mailService.sendMail(mail, content, mailTitle);
		}
		renderJson(ResultUtil.succeed("发送完成！"));
	}
	
	
	/**
	 * @author 几米物联自动化部-韦姚忠
	 * @date   2019年7月15日
	 * 添加邮箱
	 * @param emailAddress 邮箱地址
	 * @param userId 用户id
	 */
	@Log("添加邮箱，邮箱地址为：{eMailAddress}，用户id：{eMailAddress}")
	public void add(String eMailAddress,Integer userId) {
		if (eMailAddress == null || eMailAddress.equals("")) {
			throw new ParameterException("请输入邮箱地址");
		}
		boolean email = isEmail(eMailAddress);
		if (!email) {
			throw new ParameterException("请输入正确的邮箱地址");
		}
		ResultUtil resultUtil = mailService.add(eMailAddress, userId);
		renderJson(resultUtil);
	}
	
	
	/**
	 * @author 几米物联自动化部-韦姚忠
	 * @date   2019年7月15日
	 * 更新邮箱
	 * @param id 邮箱主键
	 * @param eMailAddress 邮箱地址
	 * @param userId 用户id
	 */
	public void update(Integer id ,String eMailAddress,Integer userId) {
		if (id == null || eMailAddress == null || eMailAddress.equals("")) {
			throw new ParameterException("参数为空");
		}
		boolean email = isEmail(eMailAddress);
		if (!email) {
			throw new ParameterException("请输入正确的邮箱地址");
		}
		ResultUtil resultUtil = mailService.update(id,eMailAddress,userId);
		renderJson(resultUtil);
	}
	
	
	/**
	 * @author 几米物联自动化部-韦姚忠
	 * @date   2019年7月15日
	 * 删除邮箱
	 * @param id 邮箱主键
	 */
	public void delete(Integer id) {
		renderText("该功能尚未开放！");
	}
	
	/**
	 * @author 几米物联自动化部-韦姚忠
	 * @date   2019年7月15日
	 * 根据用户id查询用户邮箱
	 * @param filter
	 * @param currentPage
	 * @param pageSize
	 */
//	public void select(String filter, Integer currentPage, Integer pageSize) {
//		if (currentPage == null || pageSize == null) {
//			throw new OperationException("参数不能为空");
//		}
//		if (currentPage <= 0 || pageSize <= 0) {
//			throw new ParameterException("当前页码与每页行数均需要大于0");
//		}
//		ResultUtil result = mailService.select(filter, currentPage, pageSize);
//		renderJson(result);
//	}
	public void select(String eMailAddress) {
		ResultUtil result = mailService.select(eMailAddress);
		renderJson(result);
	}
	
	
	/**
	 * @author 几米物联自动化部-韦姚忠
	 * @date   2019年7月15日
	 * 验证邮箱
	 * @param 待验证的字符串
	 * @return 如果是符合的字符串,返回true ,否则为false
	 */
	public boolean isEmail(String str) {
		String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		return match(regex, str);
	}
	
	
	/**
	 * @author 几米物联自动化部-韦姚忠
	 * @date   2019年7月15日
	 * @param regex
	 * 正则表达式字符串
	 * @param str
	 * 要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 */
	private boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}
