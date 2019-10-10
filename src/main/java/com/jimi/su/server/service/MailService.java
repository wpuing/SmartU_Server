package com.jimi.su.server.service;

import java.util.ArrayList;
import java.util.List;

import com.jimi.su.server.entity.SQL;
import com.jimi.su.server.exception.OperationException;
import com.jimi.su.server.exception.ParameterException;
import com.jimi.su.server.model.Mail;
import com.jimi.su.server.model.User;
import com.jimi.su.server.service.base.SelectService;
import com.jimi.su.server.util.MainUtil;
import com.jimi.su.server.util.ResultUtil;

/**
 * 邮件业务层
 * 
 * @author 几米物联自动化部-韦姚忠
 * @date 2019年7月13日
 */
public class MailService extends SelectService{
	
//	private static SelectService selectService = Enhancer.enhance(SelectService.class);

	/**
	 * @author 几米物联自动化部-韦姚忠
	 * 发送邮件业务方法
	 * @param acceptor 接收人
	 * @param mailTitle 邮件标题
	 * @param content 邮件内容
	 * @return
	 */
	public boolean sendMail(String acceptor,String mailTitle,String content) {
		try {
			MainUtil.sendMessage(acceptor, content, mailTitle);
		} catch (Exception e) {
			throw new OperationException("发送邮件失败！");
		}
		return true;
		
	}
	
	
	public ResultUtil add(String eMailAddress,Integer userId) {
		Mail mail = new Mail();
		mail = findMailByAddress(eMailAddress);
		if (userId !=null) {
			User user = User.dao.findById(userId);
			if (user==null) {
				throw new ParameterException("用户不存在!!!");
			}
		}
		if (mail == null) {
			mail = new Mail().setEMailAddress(eMailAddress.trim()).setUserID(userId);
			mail.save();
		}else {
			throw new OperationException("邮箱已存在!!!");
		}
		return ResultUtil.succeed();
		
	}
	
	
	
//	public ResultUtil select(String filter, Integer currentPage, Integer pageSize) {
//		Page<Record> pageRecord = selectService.select("mail", currentPage, pageSize, null, null, filter, null);
//		List<MailVO> mailVOs = MailVO.fillList(pageRecord.getList());
//		PageUtil<MailVO> pageUtil = new PageUtil<MailVO>();
//		pageUtil.fill(pageRecord, mailVOs);
//		return ResultUtil.succeed(pageUtil);
//	}
	public ResultUtil select(String eMailAddress) {
		List<Mail> mails = new ArrayList<Mail>();
		if (eMailAddress != null && !eMailAddress.equals("")) {
			mails = Mail.dao.find(SQL.SELECT_VAGUE_MAIL_BY_NAME+"'%"+eMailAddress+"%'");
		}else {
			mails = Mail.dao.find(SQL.SELECT_MAIL_ALL);
		}
		return ResultUtil.succeed(mails);
	}
	
	
	private Mail findMailByAddress(String eMailAddress) {
		Mail mail = Mail.dao.findFirst(SQL.SELECT_MAIL_BY_NAME, eMailAddress);
		return mail;
		
	}


	@SuppressWarnings("null")
	public ResultUtil update(Integer id, String eMailAddress, Integer userId) {
		Mail mail = new Mail();
		mail =	Mail.dao.findById(id);
		if (mail == null) {
			throw new ParameterException("找不到该邮箱!!!");
		}
		if (userId !=null) {
			User user = User.dao.findById(userId);
			if (user==null) {
				throw new ParameterException("用户不存在!!!");
			}
		}else {
			//无用户默认为0
			userId = 0;
		}
		mail = findMailByAddress(eMailAddress);
		if (mail != null) {
			throw new ParameterException("邮箱已存在，不可重复添加!!!");
		}
		mail = new Mail();
		mail.setID(id);
		mail.setEMailAddress(eMailAddress);
		mail.setUserID(userId);
		mail.update();
		return ResultUtil.succeed();
	}


}
