package com.jimi.su.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.jimi.su.server.entity.SUAuthenticator;

/**
 * 邮件工具类
 *@author 几米物联自动化部-韦姚忠
 *@date   2019年7月13日
 */
public class MainUtil {
	@SuppressWarnings("static-access")
    public static void sendMessage( String recipientAddress, String subject,
            String messageText) throws MessagingException, IOException {
		Properties props = new Properties();
		//读取配置文件
		props.load(new InputStreamReader(new FileInputStream(new File("./src/main/resources/mail.properties")), "UTF-8"));
		//发件人
		String mailForm = props.getProperty("mailForm");
		//发件人邮箱检验码
		String mailCheckCode = props.getProperty("mailCheckCode");
		//邮箱类型
		String smtpHost = props.getProperty("mail.smtp.host");
		//编码格式
		String messageType = props.getProperty("codedFormat");
		//邮件消息头部
		String messageHander = props.getProperty("messageHander");
        // 第一步：配置javax.mail.Session对象
        //创建会话对象
        Session mailSession = Session.getInstance(props, new SUAuthenticator(mailForm, mailCheckCode));
        // 第二步：编写消息
        //发送人
        InternetAddress fromAddress = new InternetAddress(mailForm,messageHander);
        //接收人
        InternetAddress toAddress = new InternetAddress(recipientAddress);
        //创建一个消息对象
        MimeMessage message = new MimeMessage(mailSession);
        //添加发送人
        message.setFrom(fromAddress);
        //添加接收人类型，接收人
        message.addRecipient(RecipientType.TO, toAddress);
        //消息的创建者指示消息已完成并准备交付的日期
        message.setSentDate(Calendar.getInstance().getTime());
        //邮件名字
        message.setSubject(subject);
        //邮件内容，编码格式
        message.setContent(messageText, messageType);
        // 第三步：发送消息
        //获取传输对象
        Transport transport = mailSession.getTransport("smtp");
        //连接到指定邮箱地址
        transport.connect(smtpHost,mailCheckCode);
        //发送消息
        transport.send(message, message.getRecipients(RecipientType.TO));
    }

    public static void main(String[] args) throws IOException {
    	
    	String mailAddr = "865230476@qq.com";
    	String mailNmae = "哈哈哈哈哈";
    	String info = "项目名为xxx、分支名为xxxx的版本已被xxx更新到";
        try {
        	MainUtil.sendMessage(mailAddr, mailNmae, info);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}


